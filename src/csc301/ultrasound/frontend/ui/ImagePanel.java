package csc301.ultrasound.frontend.ui;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.sql.Connection;

import javax.swing.JPanel;

import csc301.ultrasound.global.Transmission;

public class ImagePanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	int   RID   = -1;
	Image image = null;
	
	public ImagePanel(int RID, Connection dbConnection, Dimension panelSize)
	{
		if (dbConnection == null)
			return;
		
		this.RID = RID;
		
		BufferedImage bImage = new Transmission().getUltrasoundFromDB(RID, dbConnection);
		
		image = bImage.getScaledInstance(panelSize.width, panelSize.height, Image.SCALE_SMOOTH);

		this.setLayout(new BorderLayout(0, 0));
		this.setBackground(Color.BLACK);
		this.setPreferredSize(new Dimension(panelSize.width, panelSize.height));
	}
	
	protected void paintComponent(Graphics g) 
	{
        super.paintComponent(g);
        
        g.drawImage(image, 0, 0, null);
    }
}
