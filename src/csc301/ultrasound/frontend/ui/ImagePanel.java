package csc301.ultrasound.frontend.ui;

import java.awt.*;

import java.sql.Connection;

import javax.swing.JPanel;

import csc301.ultrasound.global.ImageDownloader;

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
		
		initUI();
	}
	
	public ImagePanel(int RID, Connection dbConnection)
	{	
		if (dbConnection == null)
			return;
		
		this.dbConnection = dbConnection;
		
		initUI();
		
		setRID(RID);
	}
	
	private void initUI()
	{
		this.setBackground(Color.BLACK);
	}
	
	protected void paintComponent(Graphics g) 
	{
        super.paintComponent(g);
        
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight() - 10, null);
    }
	
	public void setRID(int RID)
	{
		image = new ImageDownloader(dbConnection).downloadUltrasound(RID);
		
		this.repaint();
	}
}
