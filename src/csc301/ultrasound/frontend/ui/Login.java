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

import csc301.ultrasound.model.User;
import csc301.ultrasound.global.Authentication;

public class Login extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private User   user     = null;
	private String userEmail = "";
	private String userPassword   = "";

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
	 * Create the log-in window.
	 */
	public Login()
	{
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
		this.getRootPane().setDefaultButton(login);
		
		login.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				Authentication auth = new Authentication();
				User authedUser = null;
				
				userEmail    = userNameTf.getText();
				userPassword = passwordTf.getText();
				
				authedUser = auth.authenticate(userEmail, userPassword);
				
				if (userEmail == null || userEmail.isEmpty())
				{
					JOptionPane.showMessageDialog(null, "Email cannot be empty!", "Login Failure!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (userPassword == null || userPassword.isEmpty())
				{
					JOptionPane.showMessageDialog(null, "Password cannot be empty!", "Login Failure!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				// A username and password was entered, but the user is still not authenicated. Their password and/or username must be wrong.
				if (authedUser == null)
				{
					JOptionPane.showMessageDialog(null, "Incorrect username and/or password.", "Login Failure!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				else
				{
					JOptionPane.showMessageDialog(null, String.format("Authenticated. Hello %s.", authedUser.getName()), "Login Success!", JOptionPane.INFORMATION_MESSAGE);
				}
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
