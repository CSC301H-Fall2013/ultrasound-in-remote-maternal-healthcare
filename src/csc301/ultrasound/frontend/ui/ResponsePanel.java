package csc301.ultrasound.frontend.ui;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.awt.*;

import csc301.ultrasound.global.Transmission;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * The Class ResponsePanel.
 */
public class ResponsePanel extends JPanel 
{
	private static final long serialVersionUID = 1L;
	
	/** Submitted is true if the database is submitted, false otherwise. */
	private boolean submitted = false;
	
	/**
	 * Create the panel.
	 */
	public ResponsePanel() 
	{
		BufferedImage image = null;
		
		final Transmission t = new Transmission();
		final Connection connection = t.connectToDB();
		
		if (connection != null)
		{
			System.out.println("Connected!");
			
			try 
			{
				image = ImageIO.read(new File("/Users/assylay/Documents/ultrasound-in-remote-maternal-healthcare/resources/img/ultrasound.jpg"));
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
	
			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWidths = new int[]{361};
			gridBagLayout.rowHeights = new int[] {400, 80};
			gridBagLayout.columnWeights = new double[]{1.0};
			gridBagLayout.rowWeights = new double[]{0.0, 1.0};
			setLayout(gridBagLayout);
			
			final AnnotationPanel annotationPanel = new AnnotationPanel(new Dimension(640, 480), image);
			
			GridBagConstraints gbc_annotationPanel = new GridBagConstraints();
			gbc_annotationPanel.fill = GridBagConstraints.BOTH;
			gbc_annotationPanel.insets = new Insets(0, 0, 5, 0);
			gbc_annotationPanel.gridx = 0;
			gbc_annotationPanel.gridy = 0;
			this.add(annotationPanel, gbc_annotationPanel);
			
			JPanel commentPanel = new JPanel();
			commentPanel.setBackground(Color.LIGHT_GRAY);
			commentPanel.setLayout(new BoxLayout(commentPanel, BoxLayout.X_AXIS));
			
			final JTextArea response = new JTextArea();
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setViewportView(response);
			commentPanel.add(scrollPane);
			
			JButton submissionButton = new JButton("Submit");
			submissionButton.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					BufferedImage annotations = annotationPanel.getAnnotations();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					
					try 
					{
						ImageIO.write(annotations, "png", baos);
						baos.flush();
						
						byte compressed[] = t.compress(baos.toByteArray());
						String comments = response.getText();
						
						try {
							
							//Update the database with new response and annotation.
							PreparedStatement statement = connection.prepareStatement("UPDATE ultrasound.Records SET RadiologistResponse = ?, IMGAnnotation = ?, RadiologistRespondedOn = ?, RespondedBy = ?");
							statement.setString(1, comments);
							statement.setBytes(2, compressed);
							statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
							statement.setInt(4, 28);
							statement.executeUpdate();
							
							if (statement.getUpdateCount() > 0)
								setSubmitted(true);
						} 
						catch (SQLException se)
			            {
			                System.err.println("SQL Exception." + "<Message>: " + se.getMessage());
			            }
						
						System.out.println(compressed.length);
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
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
	}
	
	/**
	 * Checks if the response is submitted to the database correctly.
	 *
	 * @return true, if is submitted
	 */
	public boolean isSubmitted() 
	{
		return submitted;
	}

	/**
	 * Sets the submitted.
	 *
	 * @param submitted the new submitted
	 */
	private void setSubmitted(boolean submitted) 
	{
		this.submitted = submitted;
	}

	/*
	public static void main(String args[])
	{
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new ResponsePanel());
		frame.setResizable(false);
		frame.pack();
		
		frame.setVisible(true);
	}*/
}
