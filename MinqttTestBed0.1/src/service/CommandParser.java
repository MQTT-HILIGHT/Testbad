package service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import object.MachineManager;

public class CommandParser {

	private static CommandParser instance;
	private MachineManager mManager;

	private String testClientId;
	private String conIp;
	private String topic;
	private String clientCount;
	private int pubClientThread;
	private int pubRepeat;
	private int pubTimeInterval;
	private String message;

	synchronized public static CommandParser getInstance() {
		if (instance == null) {
			instance = new CommandParser();
			return instance;
		}
		return instance;
	}

	private CommandParser() {
		mManager = MachineManager.getInstance();
	}

	public void parserCommand(HttpServletRequest request, HttpServletResponse response) {
		try {
			String command = request.getParameter("command");

			if (command.equals("testfile")) {

				response.sendRedirect(request.getContextPath() + "/" + request.getParameter("file"));
				return;
			}
			if (command.equals("sub")) {
				testClientId = request.getParameter("testclientid");
				conIp = request.getParameter("conip");
				topic = request.getParameter("topic");
				clientCount = request.getParameter("client_count");

				String str = command + "!" + conIp + "!" + topic + "!" + clientCount + "!" + "&";
				if (testClientId.equals("all")) {
					mManager.executeAll(str);
				} else {

					mManager.execute(testClientId, str);
				}

			} else if (command.equals("getdata")) {

				try {
					mManager.executeAll(command);
					response.getWriter().append(mManager.getData().toJSONString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (command.equals("following")) {

			}

			// pub command: id/pub/conIp/topic/thread count/repeat/timeInterval/message/
			// 0 1 2 3 4 5 6
			else if (command.equals("pub")) {
				testClientId = request.getParameter("testclientid");
				conIp = request.getParameter("conip");
				topic = request.getParameter("topic");
				pubClientThread = Integer.valueOf(request.getParameter("pubclientthread"));
				pubRepeat = Integer.valueOf(request.getParameter("pubrepeat"));
				pubTimeInterval = Integer.valueOf(request.getParameter("pubtimeinterval"));
				message = request.getParameter("message");

				String str = command + "!" + conIp + "!" + topic + "!" + pubClientThread + "!" + pubRepeat + "!"
						+ pubTimeInterval + "!" + message + "!" + "&";
				if (testClientId.equals("all")) {
					mManager.executeAll(str);
				} else {

					mManager.execute(testClientId, str);
				}

			} else if (command.equals("unsub")) {
				testClientId = request.getParameter("testclientid");
				String str = command + "!";
				if (testClientId.equals("all")) {
					mManager.executeAll(str);
				} else {

					mManager.execute(testClientId, str);
				}

			} else if (command.equals("unpub")) {

				testClientId = request.getParameter("testclientid");
				String str = command + "!";
				if (testClientId.equals("all")) {
					mManager.executeAll(str);
				} else {

					mManager.execute(testClientId, str);
				}
			} else if (command.equals("unfollowing")) {

			}
			else if(command.equals("disconnect")) {
				testClientId = request.getParameter("testclientid");
				String str1 = command + "!";
				
				mManager.execute(testClientId,str1);
			}
			
			else {
				try {
					mManager.executeAll(command);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
