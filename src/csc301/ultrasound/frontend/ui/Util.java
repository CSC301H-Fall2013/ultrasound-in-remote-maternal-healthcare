package csc301.ultrasound.frontend.ui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * Contains UI related utility methods.
 */
public class Util
{
	/**
	 * Determines the scale factor of an image in order to fit it into a given size.
	 * 
	 * @see http://stackoverflow.com/a/11959928
	 *
	 * @param original The original size of the image to fit.
	 * @param toFit The size of the area to fit the image into.
	 * @return The scale factor.
	 */
	public static float getScaleFactorToFit(Dimension original, Dimension toFit) 
	{
	    float scale = 1.0f;

	    if (original != null && toFit != null) 
	    {
	        float scaleWidth  = (float)toFit.width  / (float)original.width;
	        float scaleHeight = (float)toFit.height / (float)original.height;

	        scale = Math.min(scaleHeight, scaleWidth);
	    }

	    return scale;
	}
	
	/**
	 * Center the supplied window.
	 *
	 * @param frame The JFrame to center.
	 */
	public static void centerWindow(JFrame frame)
	{
		frame.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width  / 2) - (frame.getSize().width  / 2), 
			         	  (Toolkit.getDefaultToolkit().getScreenSize().height / 2) - (frame.getSize().height / 2));
	}
}
