package csc301.ultrasound.frontend.ui;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImagePanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private Image image = null;
	
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
        
        if (image != null)
        {
	        float scaleFactor = (float)Math.min(1.0f, Util.getScaleFactorToFit(new Dimension(image.getWidth(null), image.getHeight(null)), 
	        																   new Dimension(this.getWidth(),      this.getHeight())));
	
			// determine the parameters to fit the image into the given space, preserving the aspect ratio.
			int scaleWidth  = (int)Math.round(image.getWidth(null)  * scaleFactor);
			int scaleHeight = (int)Math.round(image.getHeight(null) * scaleFactor);
			
			int x = ((this.getWidth() - 1)  - scaleWidth)  / 2;
			int y = ((this.getHeight() - 1) - scaleHeight) / 2;
	        
	        g.drawImage(image, x, y, scaleWidth, scaleHeight, null);
        }
    }
	
	public void update(int newRID, BufferedImage newImage)
	{
		image = newImage;
		
		this.repaint();
	}
}
