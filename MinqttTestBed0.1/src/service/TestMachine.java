package service;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import config.MinState;
import object.MachineManager;

public class TestMachine extends Thread {

	private MinState state;
	private MinState prestate;

	private SocketHandler sh;
	private String mid;
	private JSONObject result;

	public TestMachine(String id, SocketHandler sh) throws Exception {
		// this.mid = id.split("/")[1];;
		this.mid = id;
		this.sh = sh;
		state = MinState.connected;
		prestate = MinState.connected;
		handShake();
	}

	public void execute(String command) {
		System.out.println(mid + " testmachine: " + command);
		
		sh.execute(command);

	}

	public void listen() {

		this.start();

	}

	public void run() {
		String recv;
		try {
			while (true) {
				recv = sh.read();
				if (recv.contains("null")) {
					this.interrupt();
				}
				if (recv.contains("recvack")) {

					state = prestate;
					continue;
				} else {

					state = prestate;
				}
				// System.out.println("recv from tc: " + recv);
				JSONParser parser = new JSONParser();

				String objStr = new String(recv);
				objStr = objStr.trim();
				// System.out.println("result: "+objStr);
				result = (JSONObject) parser.parse(objStr);

				MachineManager.getInstance().updateMachine(result, mid);

			}
		} catch (Exception e) {
			this.interrupt();
			removeFromArray();
			sh.netClose();
			e.printStackTrace();
		}
	}

	public void handShake() throws Exception {
		sh.sockWrite("connectok" + "!");
		if (sh.read().contains("okack")) {
			sh.sockWrite("conid!" + this.mid + "!");
			System.out.println("handshake done machine start!");
			state = MinState.ready;
			prestate = MinState.ready;
			sh.setAlive();
			listen();
		}
	}

	public void removeFromArray() {

		MachineManager.getInstance().removeMachine(this);
	}

	public MinState getMinState() {
		return state;
	}

	public void setState(MinState state) {
		if (state != MinState.pending)
			this.prestate = this.state;
		this.state = state;

	}

	public SocketHandler getSh() {
		return sh;
	}

	public void setSh(SocketHandler sh) {
		this.sh = sh;
	}

	public String getmId() {
		return mid;
	}

	public void setId(String id) {

		this.mid = id;
		// this.mid = id.split("/")[1];
	}
}
