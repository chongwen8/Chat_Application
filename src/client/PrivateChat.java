package java_final_lab.client;


import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
/**
 * Private chat interface almost like client main interface without friends' list
 * while client double click friend's name , it will display
 *
 */
public class PrivateChat extends JDialog {
	JTextField chatBox = new JTextField();
	JTextArea content = new JTextArea();
	private String title;
	

	public PrivateChat(String title, JFrame father) {
		super(father, title, false);
		this.title = title;
		this.setSize(380, 600);
		chatBox.setFont(new Font("Monaco", Font.PLAIN, 15));
		this.add(chatBox, BorderLayout.SOUTH);
		this.add(content, BorderLayout.CENTER);
		this.addWindowFocusListener(new WindowAdapter() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
				// set which private friend is being focused
				ClientInterface.nowFocus = title;
			}
			@Override
			public void windowClosing(WindowEvent e) {
				// while close this window, remove this from private friend's map
				ClientInterface.privateFriends.remove(title);
			}
		});
		
		// TODO Auto-generated constructor stub
		this.setVisible(true);
	}

}
