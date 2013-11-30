package csc301.ultrasound.frontend.ui;

import javax.swing.*;

import java.awt.image.BufferedImage;
import java.sql.*;
import java.awt.*;

import csc301.ultrasound.global.ImageUploader;
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
	 * @param user The user responding to this record.
	 * @param connection An established connection to the database.
	 */
	public ResponsePanel(final User user, Connection connection) 
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
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths  = new int[]{361};
		gridBagLayout.rowHeights    = new int[] {300, responseHeight};
		gridBagLayout.columnWeights = new double[]{1.0};
		gridBagLayout.rowWeights    = new double[]{1.0, 0.0};
		setLayout(gridBagLayout);
		
		annotationPanel = new AnnotationPanel();
		
		GridBagConstraints gbc_annotationPanel = new GridBagConstraints();
		gbc_annotationPanel.fill   = GridBagConstraints.BOTH;
		gbc_annotationPanel.insets = new Insets(0, 0, 5, 0);
		gbc_annotationPanel.gridx  = 0;
		gbc_annotationPanel.gridy  = 0;
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
	
	/**
	 * Send the response and annotation image to the database.
	 */
	private void onSubmission()
	{
		BufferedImage annotations = annotationPanel.getAnnotations();
		
		try
		{
			// check to see if this record already has a response
			PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS Responded FROM Ultrasound.Records WHERE RID = ? AND RespondedBy IS NOT NULL;");
			statement.setInt(1, RID);
			
			ResultSet rs = statement.executeQuery();
			
			if (!rs.next())
				return;
			
			int responded = rs.getInt("Responded");
			
			// this record has no response
			if (responded == 0)
			{
				// Update the database with the new response and annotation.
				statement = connection.prepareStatement("UPDATE ultrasound.Records SET RadiologistResponse = ?, RadiologistRespondedOn = ?, RespondedBy = ? WHERE RID = ?");
				statement.setString(1, response.getText());
				statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
				statement.setInt(3, user.getID());
				statement.setInt(4, RID);
				statement.executeUpdate();
				
				// make sure all of the constraints on the table are satisfied on the above query
				if (statement.getUpdateCount() > 0)
				{
					ImageUploader uploader = new ImageUploader(connection);
					int nRowsUpdated = uploader.uploadFile(annotations, RID, user);
					
					if (nRowsUpdated > 0)
					{
						setSubmitted(true);
						JOptionPane.showMessageDialog(null, String.format("Response to record %d was stored successfully.", RID), "Success!", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "This record already has a recorded response.", "Access Denied", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} 
		catch (SQLException se)
        {
            System.err.println("SQL Exception." + "<Message>: " + se.getMessage());
        }
		
		if (isSubmitted() == false)
			JOptionPane.showMessageDialog(null, String.format("An error occured while storing the response to record %d.", RID), "Error!", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Update the panel to use a new RID and ultrasound image (of that RID).
	 *
	 * @param newRID The new RID.
	 * @param newImage The new ultrasound image.
	 */
	public void update(int newRID, BufferedImage newImage)
	{
		RID = newRID;
		annotationPanel.update(newRID, newImage);
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
