package java_final_lab.client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
/**
 * while client was forced by server or server quit , this dialog would display
 * and exit JVM
 */
public class QuitWindow extends JDialog{
	JLabel sorry = new JLabel("      Sorry！");
	public QuitWindow(JFrame father, String content) {
		super(father, true);
		JLabel hint = new JLabel("  " + content);
		// TODO Auto-generated constructor stub
		this.setSize(500, 300);
		this.setLocation(400, 200);
		hint.setFont(new Font("Monaco", Font.PLAIN, 17));
		this.add(hint, BorderLayout.CENTER);
		sorry.setFont(new Font("Menlo", Font.PLAIN, 40));
		this.add(sorry, BorderLayout.NORTH);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() { 
		    @Override 
		    public void windowClosed(WindowEvent e) { 
		      System.exit(1);//default exit JVM
		    }
		  });
		this.setVisible(true);
		
	}

}
