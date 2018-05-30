package service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import object.MachineManager;

public class ServerSocketProvider extends Thread {

	private ServerSocket serverSocket = null;
	private MachineManager mManager;
	
	private static ServerSocketProvider instance = null;

	public static ServerSocketProvider getInstance() throws Exception {
		if (instance == null) {
			
			instance = new ServerSocketProvider();
		
			return instance;
		}
		return instance;
	}

	private ServerSocketProvider() throws Exception {
		serverSocket = new ServerSocket(1336);
		mManager=MachineManager.getInstance();
		System.out.println("logger: server socket run!");
		this.start();
	}

	public void run() {
		try {
			while (true) {
				Thread.sleep(10);
				Socket socket = serverSocket.accept();
				//socket.setKeepAlive(true);
				SocketHandler sh = new SocketHandler(socket);
				TestMachine machine = new TestMachine(socket.getInetAddress().toString().split("/")[1], sh);
				System.out.println("con from id: "+ socket.getInetAddress().toString());
				mManager.addMachine(machine);
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	

}
