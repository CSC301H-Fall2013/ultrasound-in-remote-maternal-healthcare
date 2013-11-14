package csc301.ultrasound.frontend.ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

import csc301.ultrasound.global.Transmission;
import csc301.ultrasound.model.User;

public class Frontend
{	
	/**
	 * Display the login window. If the user is valid, open the main interface.
	 */
	public static void login()
	{
		final Login login = new Login();

		login.addWindowListener(new WindowAdapter()
		{
			public void windowClosed(WindowEvent e)
			{
				User user = login.getUser();
				
				if (user != null)
					launchMainUI(user);
			}
		});
	}
	
	private static void launchMainUI(User user)
	{
		Transmission t = new Transmission();
		Connection dbConnection = t.connectToDB();
		
		new GUI2(user, dbConnection);
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		login();
		//launchMainUI(null);
	}
}
