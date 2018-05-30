package service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.json.simple.JSONObject;

public class SocketHandler implements Executable {

	private Socket sock;
	private DataInputStream din;
	private DataOutputStream dout;
	private byte[] buffer = new byte[1000];
	private boolean waitack;
	private boolean alive = false;
	private int count = 0;
	public void setAlive() {
		this.alive = true;
	}

	public SocketHandler(Socket sock) throws Exception {

		this.sock = sock;
		din = new DataInputStream(sock.getInputStream());
		dout = new DataOutputStream(sock.getOutputStream());
		System.out.println(sock.getInetAddress() + " connected");
		waitack = false;

	}

	public String read() {
		String result = null;
		int readCount;
		byte[] readBuf = new byte[1000];

		try {
			if ((readCount = din.read(readBuf)) > -1) {
				//System.out.println("server read: " + new String(readBuf));
				result = new String(readBuf);
				waitack = false;
				
				result.trim();
				if (result.equals("recvack")) {
					waitack = false;

				}
			}
		} catch (Exception e) {
			netClose();
			return null;
		}
		return result;

	}

	@Override
	public void execute(String command) {

		while (alive) {
			try {
			
				if(count >60)
					netClose();
				Thread.sleep(500);
				count++;
				//System.out.println("run");
				if (waitack == false && alive == true) {
					count = 0;
					sockWrite(command);
					setWaitack();
					return;
				} else if (alive == false) {
					sockWrite(command);
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}

	public void setWaitack() {
		waitack = true;
	}
	public void setWaitackFalse() {
		waitack = false;
	}

	public void sockWrite(String command) throws Exception {
		// String dummy = " ";
		// String sendCommand = command + dummy;

		//System.out.println("write : " + command);
		dout.write(command.getBytes());
		dout.flush();

	}

	public void netClose() {
		try {
			din.close();
			dout.flush();
			dout.close();
			sock.close();
			alive = false;
			System.out.println("connection end");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
