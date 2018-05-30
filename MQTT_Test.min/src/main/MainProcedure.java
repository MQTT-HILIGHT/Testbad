package main;

import java.io.IOException;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.simple.JSONObject;

import config.MinState;
import controller.DataController;
import controller.PubThreadController;
import controller.SubController;
import minqtt.Publisher;
import minqtt.Subscriber;

public class MainProcedure {
	public static final String defaultIp = "127.0.0.1";
	public static final int defaultPort = 1336;

	NetProvider nProvider = null;
	private PubThreadController pc;
	private SubController sc;
	private DataController dc;

	public MainProcedure(String ip, int port) throws Exception {

		nProvider.initNetProvider(ip, port);
		pc = new PubThreadController();
		sc = new SubController();
		nProvider = NetProvider.getInstance();
		dc.initDataController(pc, sc);

		if (nProvider.handshake() == true) {

			System.out.println("handshake done, listening!");
			listen();
		}
	}

	public MainProcedure() throws Exception {

		nProvider.initNetProvider(defaultIp, defaultPort);
		pc = new PubThreadController();
		sc = new SubController();
		nProvider = NetProvider.getInstance();

		dc.initDataController(pc, sc);

		if (nProvider.handshake() == true) {
			dc = DataController.getInstance();
			System.out.println("handshake done, listening!");

			listen();
		}
	}

	public void listen() throws Exception {
		String command;
		JSONObject obj;
		while (true) {
			//
			command = nProvider.read();
			System.out.println("command: " + command);
			if (command == null) {
				continue;
			}
			if (command.contains("ask") && (pc.getCount() + sc.getCount()) == 0) {
				if (nProvider.getAlive() == true) {

					System.out.println("sendAck");
					nProvider.write("recvack");
				}
			}
			if (command.contains("disconnect")) {
				System.exit(0);
				dc.update();

			}
			if (command.contains("sub")) {
				sc.execute(command);
			} else if (command.contains("pub")) {
				pc.execute(command);
			} else if (command.contains("getdata")) {
				if (dc == null) {
					System.out.println("DataController not ready");
					continue;
				}
				obj = dc.update();
				nProvider.write(obj.toJSONString());
			}
			obj = dc.update();
			if (nProvider.getAlive() == true) {

				System.out.println("sendAck");
				nProvider.write("recvack");
			}
			System.out.println("tesk done, listen");
		}
	}

}
