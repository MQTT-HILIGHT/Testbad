package main;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import controller.DataController;

public class NetProvider {

	private static NetProvider instance = null;

	private Socket sock = null;
	private static String ip = null;
	private static int port;

	private DataInputStream din;
	private DataOutputStream dout;
	private DataController dc;
	private boolean alive = false;

	synchronized public static NetProvider getInstance() throws Exception {
		if (instance == null) {
			instance = new NetProvider();
			return instance;
		}
		return instance;
	}

	private NetProvider() throws Exception {

		System.out.println("start");
		this.sock = new Socket(ip, port);

		din = new DataInputStream(sock.getInputStream());
		dout = new DataOutputStream(sock.getOutputStream());

		dc = DataController.getInstance();

	}

	public boolean getAlive() {
		return alive;
	}
	
	public boolean handshake() throws IOException {
		String arg = read();
		String line[] = arg.split("!");
		if (arg.contains("connectok")) {

			write("okack");
			String recv = read();
			//System.out.println(recv);
			line = recv.split("!");
			dc.setId(line[1]);
			alive = true;
			return true;
		}
		return false;

	}

	public static void initNetProvider(String sip, int sport) {
		ip = sip;
		port = sport;

	}

	public String read() throws IOException {
		String result = null;
		int readCount;
		byte[] readBuf = new byte[200];
		if ((readCount = din.read(readBuf)) > -1) {
			
			result = new String(readBuf, "utf-8");
			
		}
		return result;
 
	}

	public void write(String data) throws IOException {

		dout.write(data.getBytes());
		dout.flush();

	}

	public void netClose() {
		try {
			din.close();
			dout.flush();
			dout.close();
			sock.close();
			System.out.println("connection end");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
