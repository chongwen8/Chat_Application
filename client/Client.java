package java_final_lab.client;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
class Client  extends JFrame implements ActionListener,Runnable{
	private JTextField jtf = new JTextField();
	private PrintStream ps = null;
	private BufferedReader br = null;
	private String nickName;
	private JTextArea jta = new JTextArea();
	private List userList = new List();
	public Client() throws Exception{
		this.add(jtf, BorderLayout.SOUTH);
		this.add(jta,BorderLayout.CENTER);
		this.add(userList,BorderLayout.EAST);
		jtf.addActionListener(this);
		this.setSize(300,200);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		jtf.setFont(new Font("Monaco", Font.PLAIN, 15));
		nickName = JOptionPane.showInputDialog("Please input your name");
		this.setTitle(nickName);
		Socket s = new Socket("127.0.0.1",9999);
		ps = new PrintStream(s.getOutputStream());
		br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		ps.println("LOGIN#"+nickName);
		new Thread(this).start();
	}
	public void run(){
		while(true){
				try{
					String str = br.readLine();
					if(!str.startsWith("LOGIN#")){
						jta.append(str + "\n");
					}else{
						String[] strs = str.split("#");   
						userList.removeAll();
						for(int i=1; i<strs.length;i++){  userList.add(strs[i]);  }
					}
				}catch(Exception e){}
		}
	}
	public void actionPerformed(ActionEvent e){
		ps.println(nickName + "speak:" + jtf.getText());
	}
	public static void main(String[] args) throws Exception{	
		new Client().setVisible(true);
	}
}