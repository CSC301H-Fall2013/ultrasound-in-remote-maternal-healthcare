package csc301.ultrasound.frontend.ui;

import javax.swing.*;

import java.sql.*;
import java.util.ArrayList;
import java.awt.*;

import csc301.ultrasound.model.User;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import net.miginfocom.swing.MigLayout;

/**
 * A panel used to respond to a record.
 */
public class MessagePanel extends JPanel 
{
	private static final long serialVersionUID = 1L;
	
	private User user = null;
	private Connection connection = null;
	
	private int RID = -1;
	
	private JTextArea response = null;
	private JTextArea messages = null;
	
	private ArrayList<String[]> data = new ArrayList<String[]>();
	
	/**
	 * Instantiates a new message panel.
	 *
	 * @param user The current logged-in user.
	 * @param connection An established connection to the database.
	 */
	public MessagePanel(final User user, Connection connection) 
	{
		if (connection == null)
			return;
		
		this.user = user;
		this.connection = connection;
		
		initUI();
	}
	
	/**
	 * Creates the UI.
	 */
	private void initUI()
	{	
		JPanel commentPanel = new JPanel();
		commentPanel.setBackground(Color.LIGHT_GRAY);
		commentPanel.setLayout(new BoxLayout(commentPanel, BoxLayout.X_AXIS));
		
		response = new JTextArea();
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(response);
		commentPanel.add(scrollPane);
		
		JButton submissionButton = new JButton("Submit");
		submissionButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				onSubmission();
			}
		});
		setLayout(new MigLayout("", "[grow][129.00][453px]", "[225px,grow][16px][][25px][25px][16px][25px][25px][16px][25px][25px][16px][25px][25px][25px]"));
		
		messages = new JTextArea();
		messages.setEditable(false);
		messages.setText("No messages.");
		JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setViewportView(messages);
		add(scrollPane1, "cell 0 0 3 2,grow");
		
		JButton updateButton = new JButton("Update message");
		updateButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				messages.setText("");
				update(RID);
			}
		});
		add(updateButton, "cell 0 2 3 1,alignx center,aligny center");
		
		commentPanel.add(submissionButton);
		this.add(commentPanel, "cell 0 3 3 11,grow");
	}
	
	/**
	 * Submit message to the database.
	 */
	private void onSubmission()
	{
		try 
		{
			//Update the database with the new response and annotation.
			PreparedStatement statement = connection.prepareStatement("INSERT INTO ultrasound.Message(RID, UID, Message, Date) VALUES(?,?,?,?);");
			
			statement.setInt(1, RID);
			statement.setInt(2, user.getID());
			statement.setString(3, response.getText());
			statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			
			statement.executeUpdate();
			
			// make sure all of the constraints on the table are satisfied on the above query
			if (statement.getUpdateCount() > 0)
			{
				messages.setText("");
				response.setText("");
				
				getMessages();
				displayMessages();
			}
		} 
		catch (SQLException se)
        {
            System.err.println("SQL Exception. " + se.getMessage());
        }
	}
	
	/**
	 * Display the messages in "data". Make sure to call getMessages() first.
	 */
	private void displayMessages()
	{
		update(RID);
	}
	
	/**
	 * Update the panel to use a different RID.
	 *
	 * @param newRID The new RID.
	 */
	public void update(int newRID)
	{
		RID = newRID;
		
		// Clear data.
		data = new ArrayList<String[]>();
		
		getMessages();
		
		if (data != null)
		{
			messages.setText("");	// clear current contents
			
			// write new contents
			for (String[] i : data)
				messages.append(i[0] + " on " +i[2] + " : \n" + i[1] + "\n\n");
		} 
		else
			messages.setText("No messages.");
	}
	
	/**
	 * Pull messages from the database and save them into "data".
	 */
	private void getMessages()
	{
		try
		{
			Statement statement = connection.createStatement();
			
			String query = "select username, message, date "
					     + "from ultrasound.Message, ultrasound.users "
					     + "where RID = " + RID + " and UID = id "
					     + "order by Date";
			
			ResultSet rs = statement.executeQuery(query);

			while (rs.next())
			{
				String[] msgInfo = new String[3];
				
				// Extract data from the result set.
				msgInfo[0] = rs.getString("username");
				msgInfo[1] = rs.getString("message");
				msgInfo[2] = rs.getString("date");
				
				data.add(msgInfo);
			}
		} 
		catch (SQLException se)
		{
			se.printStackTrace();
		}
	}
}
