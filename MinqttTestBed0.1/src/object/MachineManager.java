package object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONObject;

import config.MinState;
import service.TestMachine;

public class MachineManager {

	private static MachineManager instance = null;
	private HashMap<String, TestMachine> machineMap;
	private JSONObject machineData = null;
	private int askCount = 0;

	private MachineManager() {
		machineMap = new HashMap();
		machineData = new JSONObject();
	}

	public static MachineManager getInstance() {
		if (instance == null) {
			instance = new MachineManager();
			return instance;
		}
		return instance;
	}

	synchronized public void executeAll(String command) throws Exception {
		System.out.println("size: " + machineMap.size() + " ServerSocketProvider excute All: " + command);

		String key;
		Iterator it = machineMap.keySet().iterator();
		synchronized (machineMap) {
			while (it.hasNext()) {
				key = (String) it.next();
				if (command.contains("disconnect")) {
					removeMachine(machineMap.get(key));

				}
				while (machineMap.get(key).getMinState() == MinState.pending) {

					if (command.equals("getdata")) {
						return;
					}
					Thread.yield();
					Thread.sleep(500);
					if (askCount > 30) {
						machineMap.get(key).getSh().sockWrite("ask");
						askCount =0 ;
					}
					// System.out.println("wait: " + machineMap.get(key).getMinState()+"command: "+
					// command);

				}
				machineMap.get(key).setState(MinState.pending);
				machineMap.get(key).execute(command);

			}
		}

	}

	public void execute(String id, String command) throws Exception {
		System.out.println("id: " + id);
		machineMap.get(id).execute(command);
		if (command.equals("disconnect")) {
			machineMap.get(id).removeFromArray();
			machineData.remove(id);
		}
	}

	public void addMachine(TestMachine machine) {
		while (machineMap.containsKey(machine.getmId())) {
			machine.setId(machine.getmId() + "a");
		}
		machineMap.put(machine.getmId(), machine);
	}

	public void removeMachine(TestMachine machine) {

		machineMap.remove(machine.getmId(), machine);
		machineData.remove(machine.getmId());
		System.out.println("remove: " + machineMap.size());

	}

	public int getTotal() {
		return machineMap.size();
	}

	synchronized public JSONObject getData() {

		return machineData;
	}

	synchronized public void updateMachine(JSONObject obj, String id) {

		machineData.put(id, obj);
	}

}
