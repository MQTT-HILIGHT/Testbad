package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import org.eclipse.paho.client.mqttv3.MqttException;

import config.MinState;
import main.NetProvider;
import minqtt.Publisher;
import minqtt.Subscriber;
import minqtt.extension.MinqttClient;

public class SubController {
	Scanner scan = new Scanner(System.in);
	String command;
	Subscriber sub;
	private String id = "min_sub";
	private String ip = "tcp://127.0.0.1:1883";
	private HashMap<String, Subscriber> subMap;
	private MinState status = MinState.ready;

	public SubController() throws Exception {
		// this.sub = new Subscriber(this.ip, this.id);
		// id=DataController.getInstance().getId();
		//System.out.println("sub id: " + id);
		subMap = new HashMap();
	}

	public SubController(String ip, String id) throws Exception {
		// id=DataController.getInstance().getId();
		subMap = new HashMap();
		// this.sub = new Subscriber(ip, id);
		// while(true) {
		//
		// command=scan.nextLine();
		// execute(command);
		// Thread.yield();
		// }
	}

	// sub!ip!topic!count!
	// 0 1 2 3
	synchronized public void execute(String command) {

		try {
			String[] line = command.split("!");
			for (int i = 0; i < line.length; i++) {
				//System.out.println("line" + i + ": " + line[i]);
			}

			if (line[0].equals("sub")) {
				this.status = MinState.subscribing;
				this.ip = "tcp://" + line[1];
				String topic = line[2];

				if (subMap.containsKey(topic)) {
					sub = subMap.get(topic);
					for (int i = 0; i < Integer.valueOf(line[3]); i++) {
						sub.subscribe(topic, sub.createClient(line[2]));
					}
				} else {

					//System.out.println("map size: " + subMap.size());
					sub = new Subscriber(this.ip, this.id + " -t: " + subMap.size());
					for (int i = 0; i < Integer.valueOf(line[3]); i++) {

						sub.subscribe(topic, sub.createClient(line[2]));

					}

					subMap.put(topic, sub);
				}
 
			}
			if (line[0].equals("unsub")) {
				
				String key;
				Iterator it = subMap.keySet().iterator();
				while (it.hasNext()) {
					key = (String) it.next();

					subMap.get(key).unSubscribeAll();
					subMap.remove(key);
					System.out.println("sub remain: "+ subMap.size());
					
				}
				this.status = MinState.ready;
				if (NetProvider.getInstance().getAlive() == true) {

					System.out.println("sendAck");
					NetProvider.getInstance().write("recvack");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			this.status = MinState.lost;
		}

	}

	///////////////////////////// getter and setter/////////////////
	public void setIP(String ip) {
		this.ip = ip;
	}

	public void setId(String id) {
		this.id = id;
	}

	synchronized public int getCount() {
		int result = 0;
		String key;
		Iterator it = subMap.keySet().iterator();
		while (it.hasNext()) {
			key = (String) it.next();

			result += subMap.get(key).displaySubCount();
		}

		return result;
	}

	public HashMap getMap() {
		return this.subMap;

	}

	public MinState getStatus() {
		return this.status;
	}

}
