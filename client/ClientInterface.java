package java_final_lab.client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.awt.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
/**
 * this class is a client main graphic interface to chat with other publicly
 *  it will show private chat dialog, when double click a users' list, so u can talk with others privately.
 * @author Allen Wen
 *
 */
public class ClientInterface extends JFrame implements ActionListener, Runnable {
	private JTextField chatBox = new JTextField();
	private PrintStream fromClient = null;
	private BufferedReader fromServer = null;
	private String nickName;
	private JTextArea content = new JTextArea();
	private JScrollPane textAreaJScrollPane = new JScrollPane(content);
	private List userList = new List();
	static HashMap<String, PrivateChat> privateFriends =  new HashMap<String, PrivateChat>(); //private friends map
	static String nowFocus;//which friendWindow is now focused
	private Socket client;

	public ClientInterface() {
		// Arrange GUI component	
		content.setEditable(false);
		content.setLineWrap(true);
		this.add(chatBox, BorderLayout.SOUTH);
		this.add(textAreaJScrollPane, BorderLayout.CENTER);
		this.add(userList, BorderLayout.EAST);
		chatBox.addActionListener(this);
		userList.addActionListener(this);
		this.setLocation(500, 200);
		this.setSize(400, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		chatBox.setFont(new Font("Monaco", Font.PLAIN, 15));
		nickName = JOptionPane.showInputDialog("Please input your name");
		this.setTitle(nickName);
		this.setVisible(true);
		try {
			if (nickName == null) {
				return;//if user doesn't input name , it will quit automatically 
			}
			client = new Socket("127.0.0.1", 9999);// server is default your PC
			fromClient = new PrintStream(client.getOutputStream());
			fromServer = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.exit(1);
		}
		fromClient.println("LOGIN#" + nickName);
		new Thread(this).start();//start thread to receive message from server.
	}

	@Override
	/***
	 * this method distinguish different messages according to prefix 
	 * 
	 */
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				String str = fromServer.readLine();
				if (str.startsWith("LOGIN#")) {
					String[] strs = str.split("#");
					userList.removeAll();
					for (int i = 1; i < strs.length; i++){
						userList.add(strs[i]);
					}
				} else if (str.startsWith("QUIT#")){
					client.close();
					new QuitWindow(this, "  You have been forced offline by the admin");
					break;
				}else if (str.startsWith("Private#")){
					String privateMessage = "";
					String[] strs = str.split("#");
					for (int i = 2; i < strs.length; i++){
						privateMessage += strs[i];
						
					}
					content.append("Private conversation :" + "(" + strs[2] + " to " + strs[1] + ")\n" + privateMessage + "\n");
					if (strs[2].equals(nowFocus)) {
						privateFriends.get(strs[2]).content.append(privateMessage + "\n");
					}
					
				}else {
					content.append(str + "\n");
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
				new QuitWindow(this, "  Server is closed");
				break;
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {// listen main frame' action and private frame's action
		// TODO Auto-generated method stub
		if (e.getSource() == chatBox) {
			fromClient.println(nickName + " speak: " + chatBox.getText());//get chat box's content
			chatBox.setText(""); //set chat box empty
		}
		else if (e.getSource() == userList) {
			//private chat
			String friendName = userList.getSelectedItem();
			privateFriends.put(friendName, new PrivateChat(friendName, this));//append friend's UI to  map
			privateFriends.get(friendName).chatBox.addActionListener(this);
		}
		//this method will get specify private chat box's message and send them to server,according to now focus.
		else if (e.getSource() == privateFriends.get(nowFocus).chatBox) {
			String privateMessage = privateFriends.get(nowFocus).chatBox.getText();
			fromClient.println("Private#" + nowFocus + "#" + nickName + "#" + " speak: " + privateMessage);// message contian's sender and receiver
			privateFriends.get(nowFocus).content.append(nickName + " speak: " + privateMessage  + "\n");
			content.append("Private conversation :" + "(" + nickName + " to " + nowFocus + ")\n" + nickName + " speak " + privateMessage  + "\n");
			privateFriends.get(nowFocus).chatBox.setText("");
			
		}

	}


}
