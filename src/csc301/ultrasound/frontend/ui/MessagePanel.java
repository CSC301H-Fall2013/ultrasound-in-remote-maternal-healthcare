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
	
	private boolean submitted = false;
	
	private User user = null;
	private Connection connection = null;
	
	private int RID = -1;
	
	private JTextArea response = null;
	private JTextArea messages = null;
	
	private ArrayList<String[]> data = new ArrayList<String[]>();
	
	
	
	/**
	 * Instantiates a new response panel.
	 *
	 * @param user The user responding to this record.
	 * @param connection The established connection.
	 */
	public MessagePanel(final User user, Connection connection) 
	{
		if (connection == null)
			return;
		
		this.user = user;
		this.connection = connection;
		
		initUI();
	}
	
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
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				messages.setText("");
				update(RID);
			}
		});
		add(updateButton, "cell 0 2 3 1,alignx center,aligny center");
		
		commentPanel.add(submissionButton);
		this.add(commentPanel, "cell 0 3 3 11,grow");
	}
	
	private void onSubmission()
	{
		try 
		{
			//Update the database with the new response and annotation.
			PreparedStatement statement = connection.prepareStatement(
					"INSERT INTO ultrasound.Message(RID, UID, Message, Date) VALUES(?,?,?,?);"
					);
			statement.setInt(1, RID);
			statement.setInt(2, user.getID());
			statement.setString(3, response.getText());
			statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			
			
			statement.executeUpdate();
			
			// make sure all of the constraints on the table are satisfied on the above query
			if (statement.getUpdateCount() > 0)
			{
					setSubmitted(true);
					messages.setText("");
					update(RID);
			}
		} 
		catch (SQLException se)
        {
            System.err.println("SQL Exception." + "<Message>: " + se.getMessage());
        }
		
		if (isSubmitted() == false)
			JOptionPane.showMessageDialog(null, String.format("An error occured while storing the response to record %d.", RID), "Error!", JOptionPane.ERROR_MESSAGE);
	}
	
	public void update(int newRID)
	{
		RID = newRID;
		getMessage(connection);
		if(data != null){
			messages.setText("");
			for(String[] i : data){
				messages.append(i[0] + " on " +i[2] + " : \n" + i[1] + "\n \n");
			}
		} else {messages.setText("No messages.");}
		// Clear data.
		data = new ArrayList<String[]>();
		
		
	}
	
	/**
	 * Checks to see if the response was submitted to the database correctly.
	 *
	 * @return True if submitted successfully. False otherwise
	 */
	public boolean isSubmitted() 
	{
		return submitted;
	}
	
	/**
	 * Sets whether the response was submitted to the database correctly.
	 *
	 * @param submitted Whether or not the response was submitted to the database correctly.
	 */
	private void setSubmitted(boolean submitted) 
	{
		this.submitted = submitted;
	}
	
	private void getMessage(Connection connection)
	{
		try
		{
			Statement statement = connection.createStatement();
			
			String query = "select username, message, date "
					     + "from ultrasound.Message, ultrasound.users "
					     + "where RID = " + Integer.toString(RID) 
					     + " and UID = id "
					     + "order by Date ";
			
			ResultSet rs = statement.executeQuery(query);
			String[] msgInfo = new String[3];

			while (rs.next())
			{
				// Extract data from the result set.
				msgInfo[0] = rs.getString("username");
				msgInfo[1] = rs.getString("message");
				msgInfo[2] = rs.getString("date");
				
				// Record the data in the arraylist.
				data.add(msgInfo);
				msgInfo = new String[3];
				
			}

		} 
		catch (SQLException se)
		{
			se.printStackTrace();
		}
	}
}
