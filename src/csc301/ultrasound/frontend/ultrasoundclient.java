package csc301.ultrasound.frontend;

import csc301.ultrasound.frontend.ui.*;

public class ultrasoundclient
{
	/**
	 * A main class that calls and runs the GUI.
	 */
	public static void main(String[] args)
	{
		/* Set the Nimbus look and feel */
		try
		{
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
			{
				if ("Nimbus".equals(info.getName()))
				{
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} 
		catch (Exception e)
		{
			java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
		}

		/* Create and display the GUI form */
		java.awt.EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				new GUI().setVisible(true);
			}
		});
	}
}
