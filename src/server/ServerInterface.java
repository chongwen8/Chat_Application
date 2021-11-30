package java_final_lab.server;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
/**
 * 
 *this class is server interface and it's almost the same as client interface.
 *However, it will start many thread (specify each client) to accept client's message
 *,besides, at the same times, it could send message to every client and force quit client
 */
public class ServerInterface extends JFrame implements Runnable, ActionListener {
	static HashMap<String, ChatThread> userMap = new HashMap<String, ChatThread>();
	static List userList = new List();//the same as client's interface
	private JTextField chatBox = new JTextField();
	static JTextArea content = new JTextArea();
	private JSplitPane splitPane = new JSplitPane();
	private JButton deleteButton = new JButton("Force Quit");

	public ServerInterface() {
		super("Server");
		content.setEditable(false);
		content.setLineWrap(true);
		this.setLocation(400, 300);
		this.setSize(500, 700);
		this.add(content, BorderLayout.CENTER);
		this.add(userList, BorderLayout.EAST);
		this.add(splitPane, BorderLayout.SOUTH);
		splitPane.setLeftComponent(chatBox);
		chatBox.setColumns(35);
		chatBox.setFont(new Font("Monaco", Font.PLAIN, 15));
		chatBox.addActionListener(this);
		splitPane.setRightComponent(deleteButton);
		deleteButton.addActionListener(this);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		new Thread(this).start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ServerSocket server = new ServerSocket(9999);
			while (true) {
				Socket fromUser = server.accept();  
				// while server accept from a new socket, it will create a new thread for this socket and start it.
				ChatThread user = new ChatThread(fromUser);
				user.start();
			}
		} catch (Exception e) {
			// TODO: handle exception
			JOptionPane.showInternalMessageDialog(this, "connection is closed");
			e.printStackTrace();
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// click button to delete client
		if (e.getSource() == deleteButton) {
			String client = userList.getSelectedItem();
			userMap.get(client).fromServer.println("QUIT#" + client);
			userMap.remove(client);
		}
		else if (e.getSource() == chatBox) {
			String serverMessage = chatBox.getText();
			content.append("Server speak: " + serverMessage + "\n");
			for (ChatThread user : ServerInterface.userMap.values()) {
				user.fromServer.println("Server speak: " + serverMessage); // Server send message
			}
			chatBox.setText("");
		}
	}
}
