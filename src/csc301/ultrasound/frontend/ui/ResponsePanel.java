package csc301.ultrasound.frontend.ui;

import javax.swing.*;

import java.awt.image.BufferedImage;
import java.sql.*;
import java.awt.*;

import csc301.ultrasound.global.Transmission;
import csc301.ultrasound.model.User;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * A panel used to respond to a record.
 */
public class ResponsePanel extends JPanel 
{
	private static final long serialVersionUID = 1L;
	
	private boolean submitted = false;
	
	private AnnotationPanel annotationPanel = null;
	private User user = null;
	private Connection connection = null;
	
	private int RID = -1;
	
	private JTextArea response = null;
	
	private static final int responseHeight = 60;
	
	/**
	 * Instantiates a new response panel.
	 *
	 * @param RID The record id.
	 * @param user The user responding to this record.
	 * @param connection The established connection.
	 */
	public ResponsePanel(final User user, Connection connection) 
	{
		//BufferedImage image = null;
		
		if (connection == null)
			return;
		
		this.user = user;
		this.connection = connection;
		
		initUI();
		
		//System.out.println("Connected!");
		
		//final Transmission t = new Transmission();
		
		/*image = t.getUltrasoundFromDB(RID, connection);
		
		if (image != null)
		{
			
		}
		else
		{
			System.err.println(String.format("Could not retreive ultrasound image from RID %d.", RID));
		}*/
	}
	
	private void initUI()
	{
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{361};
		gridBagLayout.rowHeights = new int[] {300, responseHeight};
		gridBagLayout.columnWeights = new double[]{1.0};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0};
		setLayout(gridBagLayout);
		
		annotationPanel = new AnnotationPanel(connection);
		
		GridBagConstraints gbc_annotationPanel = new GridBagConstraints();
		gbc_annotationPanel.fill = GridBagConstraints.BOTH;
		gbc_annotationPanel.insets = new Insets(0, 0, 5, 0);
		gbc_annotationPanel.gridx = 0;
		gbc_annotationPanel.gridy = 0;
		this.add(annotationPanel, gbc_annotationPanel);
		
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
		
		commentPanel.add(submissionButton);
		
		GridBagConstraints gbc_commentPanel = new GridBagConstraints();
		gbc_commentPanel.insets = new Insets(0, 0, 5, 0);
		gbc_commentPanel.fill = GridBagConstraints.BOTH;
		gbc_commentPanel.gridx = 0;
		gbc_commentPanel.gridy = 1;
		this.add(commentPanel, gbc_commentPanel);
	}
	
	private void onSubmission()
	{
		BufferedImage annotations = annotationPanel.getAnnotations();
		
		try 
		{
			//Update the database with the new response and annotation.
			PreparedStatement statement = connection.prepareStatement("UPDATE ultrasound.Records SET RadiologistResponse = ?, RadiologistRespondedOn = ?, RespondedBy = ? WHERE RID = ?");
			statement.setString(1, response.getText());
			statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			statement.setInt(3, user.getID());
			statement.setInt(4, RID);
			statement.executeUpdate();
			
			// make sure all of the constraints on the table are satisfied on the above query
			if (statement.getUpdateCount() > 0)
			{
				int nRowsUpdated = new Transmission().compressAndUploadAnnotationImgToDB(RID, annotations, connection);
				
				// make sure we actually inserted an image
				if (nRowsUpdated > 0)
				{
					setSubmitted(true);
					JOptionPane.showMessageDialog(null, String.format("Response to record %d was stored successfully.", RID), "Success!", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		} 
		catch (SQLException se)
        {
            System.err.println("SQL Exception." + "<Message>: " + se.getMessage());
        }
		
		if (isSubmitted() == false)
			JOptionPane.showMessageDialog(null, String.format("An error occured while storing the response to record %d.", RID), "Error!", JOptionPane.ERROR_MESSAGE);
	}
	
	public void setRID(int RID)
	{
		this.RID = RID;
		annotationPanel.setRID(RID);
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
}
