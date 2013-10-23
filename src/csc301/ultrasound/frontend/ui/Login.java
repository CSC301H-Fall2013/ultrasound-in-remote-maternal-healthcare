package csc301.ultrasound.frontend.ui;

import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import java.util.ArrayList;
import java.util.List;

import csc301.ultrasound.model.User;

public class Login extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private User   user     = null;
	private String userName = "";
	private String userPw   = "";
	private static List<User> userList;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					Login frame = new Login();
					
					frame.setVisible(true);
				} 
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Reading an existing playlist .txt file and initialize a Playlist object
	 * 
	 * @param File constructor object
	 */
	public static void parseUserlist(File file)
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String readLine;
			
			while ((readLine = br.readLine()) != null)
			{
				String[] info = readLine.split(" ");
				User newUser = new User(info[0], info[1], Integer.parseInt(info[2]));
				
				userList.add(newUser);
			}
			
			br.close();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame.
	 */
	public Login()
	{
		// Initialize userList.
		userList = new ArrayList<User>();

		// Import user list from file (temporary implementation, will change to
		// accessing DB in the future.)
		File file = new File("lib/demo/user.data");
		parseUserlist(file);

		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 474, 343);

		JPanel contentPane = new JPanel();
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("",
				"[-3.00][][][30.00][44.00][83.00,grow][38.00][][][]",
				"[][][][][][][][][][]"));

		JLabel userNameLbl = new JLabel("Username:");
		contentPane.add(userNameLbl, "cell 4 1");

		final JFormattedTextField userNameTf = new JFormattedTextField();
		contentPane.add(userNameTf, "cell 4 2 3 1,growx");

		JLabel pWLbl = new JLabel("Password:");
		contentPane.add(pWLbl, "cell 4 4");

		final JFormattedTextField passwordTf = new JFormattedTextField();
		contentPane.add(passwordTf, "cell 4 5 3 1,growx");

		JButton login = new JButton("Log in");
		login.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				userName = userNameTf.getText();
				userPw   = passwordTf.getText();
				
				if (userName == null || userName.isEmpty())
				{
					JOptionPane.showMessageDialog(null, "Email cannot be empty!", "Login Failure!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (userPw == null || userPw.isEmpty())
				{
					JOptionPane.showMessageDialog(null, "Password cannot be empty!", "Login Failure!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				ArrayList<String> loginInfo = new ArrayList<String>();
				loginInfo.add(userName);
				loginInfo.add(userPw);
				
				for (User userInfo : userList)
				{
					if (userInfo.getName().equals(userName) && userInfo.getCredential().equals(userPw))
					{
						user = new User(userName, userPw, userInfo.getType());
						
						//System.out.printf("%s, %s, %s", user.getName(), user.getCredential(), user.getType());
						
						dispose();
						
						return;
					}
				}
				
				// If inputed user info does not match, display error message.
				JOptionPane.showMessageDialog(null, "Wrong username or password!", "Login Failure!", JOptionPane.ERROR_MESSAGE);
			}
		});
		
		contentPane.add(login, "cell 3 8 3 1");

		JButton cancel = new JButton("Cancel");
		
		cancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
				dispose();
			}
		});

		contentPane.add(cancel, "cell 6 8 2 1");

		this.pack();
		
		// center the window
		this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width / 2) - (this.getSize().width / 2), 
				         (Toolkit.getDefaultToolkit().getScreenSize().height / 2) - (this.getSize().height / 2));
		
		this.setVisible(true);
	}

	public User getUser()
	{
		return user;
	}
}
