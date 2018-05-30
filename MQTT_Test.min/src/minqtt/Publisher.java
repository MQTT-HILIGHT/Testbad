package minqtt;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import controller.PubThreadController;
import minqtt.extension.MinqttClient;
import minqtt.extension.MinqttMessage;

public class Publisher extends Thread implements noti {

	private String id;
	private String ip;
	private int timeInterval;
	private int QoS;
	private String topic;
	private String payload;
	private int repeat;
	private MinqttClient client;
	private MinqttMessage msg;
	private Vector<Publisher> vec;

	public Publisher(String ip, String id, String topic, int timeInterval, int qos, int repeat, String payload,
			Vector<Publisher> vec) {
		this.id = id;
		this.ip = ip;
		this.topic = topic;
		this.timeInterval = timeInterval;
		this.QoS = qos;
		this.repeat = repeat;
		this.payload = payload;
		this.vec = vec;
		try {
			this.client = new MinqttClient(ip, id, new MemoryPersistence());
			this.msg = new MinqttMessage();

			client.setTopic(topic);
			msg.setQos(QoS);
			msg.setPayload(payload.getBytes());

			pubConnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {

		try {

			if (repeat == -1) {
				while (true) {

					Thread.sleep(timeInterval);
					publish();

				}
			} else if (repeat > 0) {
				for (int i = 0; i < repeat; i++) {
					Thread.sleep(timeInterval);
					publish();
				}
			}

			clientDisConnect();
			return;
		} catch (MqttException e) {

			e.printStackTrace();
		} catch (InterruptedException e) {

			clientDisConnect();
			// System.out.println("remain pub:"+ vec.);
			return;
		}

	}

	private void pubConnect() throws MqttSecurityException, MqttException {

		client.connect();
		System.out.println(id + ": connection success");

	}

	private void publish() throws MqttPersistenceException, MqttException {

		client.publish(topic, msg);
		// System.out.println(id + ": pub! topic: " + topic + "payload: " + payload);

	}

	public void clientDisConnect() {
		try {
			setPriority(8);
			repeat = 1;
			client.disconnect();
			client.close();
			//vec.remove(this);
			System.out.println("remain pub---:" + vec.size());
		} catch (MqttException e) {
			Debuger.printError(e);
		}
	}

	@Override
	public void interruptThread() {

		this.interrupt();

	}

	@Override
	public void notifyThread() {
		this.start();
	}

	public String getTopic() {
		return topic;
	}

	public String getMessage() {
		return payload;
	}

	public String getTimeInterval() {
		return String.valueOf(this.timeInterval);
	}
}
