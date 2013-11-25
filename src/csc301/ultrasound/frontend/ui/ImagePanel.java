package csc301.ultrasound.frontend.ui;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImagePanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private Image      image        = null;
	
	public ImagePanel()
	{
		initUI();
	}
	
	public ImagePanel(int RID)
	{	
		initUI();
		
		update(RID, null);
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
	
	public void update(int newRID, BufferedImage newImage)
	{
		image = newImage;
		
		this.repaint();
	}
}
