package minqtt;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import minqtt.extension.MinqttClient;

public class Subscriber extends Thread {
	private int clientNum;
	private String clientId;
	private String IP;
	private String topic;
	private ArrayList<MinqttClient> subCl = new ArrayList<>();

	public Subscriber(String IP, String clientId) throws MqttException {
		this.clientId = clientId;
		this.IP = IP;
		clientNum = 0;

	}

	private void connectClient(MinqttClient cl) throws MqttException {

		cl.connect();
		System.out.println(cl.getClientId() + ": connection success");
	}

	public void addClient(MinqttClient client) {
		subCl.add(client);
	}

	public MinqttClient createClient(String topic) throws MqttException {
		this.topic = topic;
		String id = clientId + String.valueOf(clientNum);
		setclientNum(this.clientNum + 1);
		MinqttClient client = new MinqttClient(IP, id, new MemoryPersistence());
		client.setTopic(topic);
		subCl.add(client);
		System.out.println(id + ": client creation success");
		connectClient(client);
		return client;

	}

	public void unSubscribeByTopic(String topic) throws MqttException {
		int count = 0;

		MinqttClient curr;
		for (Iterator<MinqttClient> it = subCl.iterator(); it.hasNext();) {
			curr = it.next();
			if (curr.getTopic().equals(topic))
				it.remove();
			curr.unsubscribe(topic);
			curr.disconnect();
			curr.close();
			count++;

		}

		System.out.println("unSub finish: " + count);

	}

	public void unSubscribeAll() throws MqttException {
		setPriority(8);
		int count = 0;
		for (int i = 0; i < subCl.size(); i++) {
			subCl.get(i).unsubscribe(subCl.get(i).getTopic());
			subCl.get(i).disconnect();
			subCl.get(i).close();
			System.out.println("unsub: " + i);
			count++;
		}
		subCl.removeAll(subCl);
		System.out.println("unSub finish: " + count);
	}

	public void unSubscribeByNum(int num) throws MqttException {
		int count = 0;
		if (num > subCl.size()) {
			System.out.println("current subs are less than delete order!: return");
			return;
		}
		for (int i = 0; i < num; i++) {
			System.out.println(i);
			subCl.get(0).unsubscribe(subCl.get(0).getTopic());
			subCl.get(0).disconnect();
			subCl.get(0).close();
			subCl.remove(0);
			count++;

		}
		System.out.println("unSub finish: " + count);
	}

	public void subscribe(String topic, MinqttClient cl) throws MqttException {

		cl.setTopic(topic);
		cl.setCallback(new MqttCallback() {

			@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {

				// System.out.println(cl.getClientId() + ": " + new String(message.getPayload(),
				// StandardCharsets.UTF_8));
			}

			@Override
			public void deliveryComplete(IMqttDeliveryToken token) {
			}

			@Override
			public void connectionLost(Throwable cause) {
				try {
					cl.disconnect();
					cl.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		cl.subscribe(topic);

	}

	public int getclientNum() {
		return clientNum;
	}

	public void setclientNum(int num) {
		this.clientNum = num;
	}

	public MinqttClient getClient(int index) {
		return subCl.get(index);
	}

	public int displaySubCount() {
		//System.out.println("sub Count: " + subCl.size());
		return subCl.size();
	}

	public String getTopic() {
		return this.topic;
	}

	public void close() throws MqttException {
		unSubscribeAll();
		System.out.println("exit client");

	}

	public String getIp() {
		return this.IP;
	}

}
