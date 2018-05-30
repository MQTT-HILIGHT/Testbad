package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.plaf.synth.SynthSeparatorUI;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import config.MinState;
import main.NetProvider;
import minqtt.Publisher;
import minqtt.Subscriber;
import minqtt.extension.MinqttClient;

public class DataController {

	private static DataController instance = null;

	private JSONObject obj;
	private static PubThreadController pub;
	private static SubController sub;
	private String id;
	private String status = "ready";
	private NetProvider nProvider;
	private Subscriber tSub;
	private String spentTime = "0";
	private boolean checkAlive = false;

	public static void initDataController(PubThreadController spub, SubController ssub) {
		System.out.println("dataController ready");
		pub = spub;
		sub = ssub;

	}

	synchronized public static DataController getInstance() throws Exception {

		if (instance == null) {
			instance = new DataController(pub, sub);
			return instance;
		}
		return instance;
	}

	private DataController(PubThreadController pub, SubController sub) throws Exception {
		this.pub = pub;
		this.sub = sub;
		
		

	}

	public void updateStatus() {
		MinState pubState = pub.getStatus();
		MinState subState = sub.getStatus();
		if (pubState == MinState.ready && subState == MinState.ready) {
			this.status = "ready";
		} else if (pubState == MinState.publishing && subState == MinState.ready) {
			this.status = "publishing";
		} else if (pubState == MinState.ready && subState == MinState.subscribing) {
			this.status = "subscribing";
		} else if (pubState == MinState.publishing && subState == MinState.subscribing) {
			this.status = "both";
		} else {
			this.status = "error";
		}

	}

	synchronized public JSONObject update() throws Exception {
		obj = new JSONObject();
		obj.put("id", this.id);
		obj.put("total_count", (int) pub.getCount() + (int) sub.getCount());
		obj.put("status", this.status);
		obj.put("spenttime", spentTime);
		nProvider = NetProvider.getInstance();
		updateStatus();

		JSONArray pa = new JSONArray();
		HashMap<String, Vector<Publisher>> pubMap = pub.getMap();
		Vector<Publisher> pl;
		String key;
		Iterator it = pubMap.keySet().iterator();
		while (it.hasNext()) {
			key = (String) it.next();
			pl = pubMap.get(key);

			JSONObject pData = new JSONObject();

			pData.put("type", "pub");
			pData.put("topic", pl.get(0).getTopic());
			pData.put("ip", pub.getIp());
			pData.put("count", String.valueOf(pl.size()));
			pData.put("message", pl.get(0).getMessage());
			pData.put("last_sent", pub.getTime());
			pData.put("do", pub.getDo());
			pData.put("interval", pl.get(0).getTimeInterval());

			pa.add(pData);

		}
		obj.put("pub", pa);

		JSONArray sa = new JSONArray();
		HashMap<String, Subscriber> subMap = sub.getMap();

		Subscriber sl;
		Iterator sit = subMap.keySet().iterator();
		while (sit.hasNext()) {
			key = (String) sit.next();
			sl = subMap.get(key);

			JSONObject sData = new JSONObject();

			sData.put("type", "sub");
			sData.put("topic", sl.getTopic());
			sData.put("ip", sl.getIp());
			sData.put("count", sl.displaySubCount());
			sa.add(sData);
		}
		obj.put("sub", sa);
		// System.out.println(obj.toJSONString());
		return obj;

	}

	public JSONObject getJSON() {

		return obj;
	}

	public void setId(String id) {
		System.out.println("set id: " + id);
		this.id = id;
		pub.setId("p_" + this.id);
		sub.setId("s_" + this.id);

	}

	public void runLoadChecker() {
		
		System.out.println("run loadChecker:"+ pub.getIp());
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while (true) {
					
						Thread.sleep(3000);
						if (checkAlive == false) {
							checkAlive = true;

					
							MinqttClient tsClient = new MinqttClient(pub.getIp(), "timechecker_sub112",
									new MemoryPersistence());
							MinqttClient tpClient = new MinqttClient(pub.getIp(), "timechecker_pub32",
									new MemoryPersistence());
							
							tpClient.connect();
							tsClient.setCallback(new MqttCallback() {

								@Override
								public void messageArrived(String topic, MqttMessage message) throws Exception {
									
									long now  = Long.valueOf(pub.getCurrentTimeByM());
									long old =  Long.valueOf(message.toString());
								
									
									spentTime = String.valueOf(now-old);
									
									//System.out.println(spentTime);
									tsClient.disconnect();
									tsClient.close();
									checkAlive = false;

								}

								@Override
								public void deliveryComplete(IMqttDeliveryToken token) {
									// TODO Auto-generated method stub

								}

								@Override
								public void connectionLost(Throwable cause) {
									checkAlive = false;

								}
							});
							tsClient.connect();
							tsClient.subscribe("timecheck_min");
							tpClient.publish("timecheck_min", new MqttMessage(pub.getCurrentTimeByM().getBytes()));
							tpClient.disconnect();
							tpClient.close();
						}
					}
				} catch (MqttException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		}).start();
	}

}

/**/