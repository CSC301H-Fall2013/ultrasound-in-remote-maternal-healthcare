package csc301.ultrasound.frontend.ui;

import java.awt.*;

import java.sql.Connection;

import javax.swing.JPanel;

import csc301.ultrasound.global.Transmission;

public class ImagePanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private Connection dbConnection = null;
	private Image      image        = null;
	
	public ImagePanel(Connection dbConnection)
	{
		if (dbConnection == null)
			return;
		
		this.dbConnection = dbConnection;
		
		this.setLayout(new BorderLayout(0, 0));
		this.setBackground(Color.BLACK);
	}
	
	public ImagePanel(int RID, Connection dbConnection)
	{	
		if (dbConnection == null)
			return;
		
		this.dbConnection = dbConnection;
		
		image = new Transmission().getUltrasoundFromDB(RID, dbConnection);
		
		this.setLayout(new BorderLayout(0, 0));
		this.setBackground(Color.BLACK);
	}
	
	protected void paintComponent(Graphics g) 
	{
        super.paintComponent(g);
        
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
    }
	
	public void setRID(int RID)
	{
		image = new Transmission().getUltrasoundFromDB(RID, dbConnection);
		this.repaint();
	}
}
