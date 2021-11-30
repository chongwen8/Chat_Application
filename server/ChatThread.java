package java_final_lab.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
/**
 * thread class was used to accept message from client.
 * while client quits, it will send message to update other's userList before it ends
 *
 */
public class ChatThread extends Thread {
	BufferedReader fromUser;
	PrintStream fromServer;
	String nickName;
	Socket me;

	/***
	 * 
	 * @param s accept socket from ServerSocket
	 * @throws Exception
	 */
	public ChatThread(Socket s) throws Exception {
		this.me = s;
		fromUser = new BufferedReader(new InputStreamReader(me.getInputStream()));
		fromServer = new PrintStream(me.getOutputStream());
	}

	public void run() {
		while (true) {
			try {
				String str = fromUser.readLine();//while client quit , this will throws NullPointerException
				// according to sign to judge user and action
				if (str.startsWith("LOGIN#")) {
					String[] strs = str.split("#");
					nickName = strs[1];
					ServerInterface.userMap.put(this.nickName, this); 
					sendMessage(updateUserList("LOGIN#"));
				} else if (str.startsWith("Private#")) {
					String[] strs = str.split("#");
					ServerInterface.userMap.get(strs[1]).fromServer.println(str);
					ServerInterface.content.append(
							"Private conversation :" + "(" + nickName + " to " + strs[1] + ")\n" + nickName + strs[3] + "\n");

				} else {
					ServerInterface.content.append(str + "\n");
					sendMessage(str);
				}
			} catch (NullPointerException e) { // execute NullPointerException, send message to everybody then end itself.
				ServerInterface.userMap.remove(this.nickName);
				sendMessage(updateUserList("LOGIN#"));
				fromServer.close();
				try {
					fromUser.close();
					me.close();
				} catch (IOException e2) {
					// close read
					e2.printStackTrace();
				}
				break;

			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

	}
/***
 * update other's userList
 * @param str
 * @return
 */
	public String updateUserList(String str) {
		ServerInterface.userList.removeAll();
		for (ChatThread user : ServerInterface.userMap.values()) {
			ServerInterface.userList.add(user.nickName);
			str = str + user.nickName + "#";

		}
		return str;

	}
/***
 * make other's user's list update and display this client
 * @param str
 */
	public void sendMessage(String str) {
		for (ChatThread user : ServerInterface.userMap.values()) {
			user.fromServer.println(str); // relay message
		}
	}
}
