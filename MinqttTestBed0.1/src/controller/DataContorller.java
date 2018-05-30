package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import object.MachineManager;
import service.CommandParser;
import service.ServerSocketProvider;

/**
 * Servlet implementation class DataContorller
 */
@WebServlet("/DataContorller")
public class DataContorller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static ServerSocketProvider ssp;
	private MachineManager mManager;
	private CommandParser mParser;

	public DataContorller() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		if (request.getParameter("run").contains("ok")) {
			try {

				ssp = ServerSocketProvider.getInstance();
				mManager = MachineManager.getInstance();
				mParser = CommandParser.getInstance();

				return;

			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		mParser.parserCommand(request, response);
		
		/*
		 * String command = request.getParameter("command");
		 * 
		 * String client = request.getParameter("client"); String clientCount =
		 * request.getParameter("client_count"); String topic =
		 * request.getParameter("topic"); String message =
		 * request.getParameter("message"); String conIp =
		 * request.getParameter("conip"); // sub/ip/topic/count/
		 * 
		 * 
		 * if(command.contains("sub")) {
		 * 
		 * try { String str=command+"!"+conIp+"!"+topic+"!"+clientCount+"!";
		 * mManager.executeAll(str); } catch (Exception e) {
		 * 
		 * e.printStackTrace(); }
		 * 
		 * 
		 * 
		 * } else if(command.contains("getdata")) {
		 * 
		 * 
		 * try { mManager.executeAll(command);
		 * response.getWriter().append(mManager.getData().toJSONString());
		 * }catch(Exception e) { e.printStackTrace(); } } else
		 * if(command.contains("following")) {
		 * 
		 * }
		 * 
		 * // pub command: id/pub/ip/topic/thread count/repeat/timeInterval/message/ //
		 * 0 1 2 3 4 5 6 else if(command.contains("pub")) { String id =
		 * request.getParameter("id"); } else if(command.contains("unsub")) {
		 * 
		 * 
		 * } else if(command.contains("unpub")) {
		 * 
		 * } else if(command.contains("unfollowing")) {
		 * 
		 * } else { try { mManager.executeAll(command); }catch(Exception e) {
		 * e.printStackTrace(); } }
		 */

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
