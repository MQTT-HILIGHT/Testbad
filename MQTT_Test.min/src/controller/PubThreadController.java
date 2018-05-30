package controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Vector;

import config.MinState;
import main.NetProvider;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import minqtt.Publisher;

public class PubThreadController {

	private String command;

	private int threadC;
	private String id = "min_pub";
	private String ip = "tcp://127.0.0.1:1883";

	private Vector<Publisher> pubList;
	private boolean alive = false;
	private String time;
	private String doing;
	private MinState status;
	private HashMap<String, Vector<Publisher>> pubMap;

	public PubThreadController() throws Exception {
		pubList = new Vector();
		pubMap = new HashMap();
		this.status = MinState.ready;

	}

	public PubThreadController(String ip, String id) throws Exception {

		this.id = id;
		this.ip = ip;
		pubList = new Vector();
		pubMap = new HashMap();
		this.status = MinState.ready;

		/*
		 * while (true) {
		 * 
		 * command = scan.nextLine(); execute(command); Thread.yield(); }
		 */
	}

	// pub command: pub/ip/topic/thread count/repeat/timeInterval/message/
	// 0 1 2 3 4 5 6
	public void execute(String command) throws NumberFormatException, Exception {

		command = command.trim();
		String[] commandline = command.split("&");
		for (int t = 0; t < commandline.length; t++) {
			String[] line = commandline[t].split("!");

			//System.out.println("command: " + commandline[t]);
			if (line[0].equals("pub") && alive == true) {
				this.ip = "tcp://" + line[1];
				threadAdd(line[2], Integer.valueOf(line[3]), Integer.valueOf(line[4]), Integer.valueOf(line[5]),
						line[6]);
				threadStart();
			}
			if (line[0].equals("pub") && alive == false) {
				this.ip = "tcp://" + line[1];
				alive = true;

				threadInit(line[2], Integer.valueOf(line[3]), Integer.valueOf(line[4]), Integer.valueOf(line[5]),
						line[6]);
				threadStart();

			} else if (line[0].equals("unpub")) {
				threadInterrupt();
				pubList.clear();
				pubMap.clear();
			//	System.out.println("unpub: " + this.getCount() + "done");
				if (NetProvider.getInstance().getAlive() == true) {

					System.out.println("sendAck");
					NetProvider.getInstance().write("recvack");
				}
			}
		}
		/*
		 * String[] line = command.split("!"); this.ip = "tcp://" + line[1];
		 * 
		 * if (line[0].equals("pub") && alive == true) {
		 * 
		 * threadAdd(line[2], Integer.valueOf(line[3]), Integer.valueOf(line[4]),
		 * Integer.valueOf(line[5]), line[6]); threadStart(); } if
		 * (line[0].equals("pub") && alive == false) { alive = true;
		 * 
		 * threadInit(line[2], Integer.valueOf(line[3]), Integer.valueOf(line[4]),
		 * Integer.valueOf(line[5]), line[6]); threadStart();
		 * 
		 * } else if (line[0].equals("unpub")) { threadInterrupt(); pubList.clear();
		 * pubMap.clear(); }
		 */

	}

	///////////////////////////// getter and setter/////////////////

	public void setId(String id) {
		this.id = id;
	}

	public String getCurrentTimeByM() {
		long time = System.currentTimeMillis();
		return String.valueOf(time);
	}

	public String getCurrentTime() {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		this.time = dayTime.format(new Date(time));

		return this.time;

	}

	public String getTime() {
		return time;
	}

	public HashMap getMap() {
		return this.pubMap;
	}

	public int getCount() {
		String key;
		int result = 0;
		Iterator it = pubMap.keySet().iterator();
		while (it.hasNext()) {
			key = (String) it.next();

			result += pubMap.get(key).size();
		}
		// System.out.println("count: " + result);
		return result;
	}

	public String getIp() {
		return ip;
	}

	public String getDo() {
		return doing;
	}

	////////////////////////////// for Thread///////////////////////
	private void threadAdd(String topic, int count, int repeat, int interval, String message) {
		System.out.println(this.id + ": thread add");
		Vector<Publisher> ls;
		doing = "connected";
		if (pubMap.containsKey(topic)) {
			ls = pubMap.get(topic);
			for (int i = 0; i < count; i++) {
				System.out.println("count: " + getCount());
				ls.add(new Publisher(ip, id + String.valueOf(getCount()), topic, interval, 0, repeat, message, ls));

			}

		} else {
			ls = new Vector();
			for (int i = 0; i < count; i++) {

				ls.add(new Publisher(ip, id + String.valueOf(i + getCount()), topic, interval, 0, repeat, message, ls));
			}
			pubMap.put(topic, ls);

		}

	}

	// pub command: pub!ip!topic!thread count!repeat!timeInterval!message!
	private void threadInit(String topic, int count, int repeat, int interval, String message) throws Exception {

		DataController.getInstance().runLoadChecker();
		Vector<Publisher> ls;
		doing = "connected";
		if (pubMap.containsKey(topic)) {
			ls = pubMap.get(topic);
			for (int i = 0; i < count; i++) {

				ls.add(new Publisher(ip, id + String.valueOf(i + getCount()), topic, interval, 0, repeat, message, ls));
			}

		} else {
			ls = new Vector();
			for (int i = 0; i < count; i++) {

				ls.add(new Publisher(ip, id + String.valueOf(i + getCount()), topic, interval, 0, repeat, message, ls));
			}
			pubMap.put(topic, ls);

		}

	}

	private void threadStart() {
		Vector<Publisher> pl;
		String key;
		Iterator it = pubMap.keySet().iterator();
		while (it.hasNext()) {
			key = (String) it.next();
			pl = pubMap.get(key);
			for (int i = 0; i < pl.size(); i++) {
				if (!pl.get(i).isAlive())
					pl.get(i).start();
			}
		}
		doing = "sending";
		this.status = MinState.publishing;

	}

	synchronized private void threadInterrupt() throws IOException, Exception {
		Vector<Publisher> pl;
		String key;
		Iterator it = pubMap.keySet().iterator();
		int cnt = getCount();
		while (it.hasNext()) {
			key = (String) it.next();
			pl = pubMap.get(key); 
			for (int i = 0; i < pl.size(); i++) {
				System.out.println("order unpub: " +cnt--);
				pl.get(i).interruptThread();

			}
			for (int i = 0; i < pl.size(); i++) {
				
				pl.remove(pl.get(i));

			}
		}

		this.status = MinState.ready;
		if (NetProvider.getInstance().getAlive() == true) {

			System.out.println("sendAck");
			NetProvider.getInstance().write("recvack");
		}

	}

	public MinState getStatus() {

		return this.status;
	}
}
