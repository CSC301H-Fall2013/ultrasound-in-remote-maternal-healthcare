package csc301.ultrasound.frontend.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 474, 343);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[-3.00][][][30.00][44.00][83.00,grow][38.00][][][]", "[][][][][][][][][][]"));
		
		JLabel userNameLbl = new JLabel("Username:");
		contentPane.add(userNameLbl, "cell 4 1");
		
		JFormattedTextField userName = new JFormattedTextField();
		contentPane.add(userName, "cell 4 2 3 1,growx");
		
		JLabel pWLbl = new JLabel("Password:");
		contentPane.add(pWLbl, "cell 4 4");
		
		JFormattedTextField password = new JFormattedTextField();
		contentPane.add(password, "cell 4 5 3 1,growx");
		
		JButton login = new JButton("Log in");
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		contentPane.add(login, "cell 3 8 3 1");
		
		JButton cancel = new JButton("Cancel");
		cancel.addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent arg0) {
				dispose();
				System.exit(0);
			}
		});
		contentPane.add(cancel, "cell 6 8 2 1");
	}

}
