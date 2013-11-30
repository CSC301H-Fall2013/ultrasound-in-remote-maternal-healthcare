package csc301.ultrasound.frontend.ui;

import java.awt.Dimension;

public class Util
{
	// http://stackoverflow.com/a/11959928
	private static float getScaleFactor(int masterSize, int targetSize) 
	{
		float scale = 1;
	    
		if (masterSize > targetSize)
	        scale = (float)targetSize / (float)masterSize;
		else
			scale = (float)targetSize / (float)masterSize;
	    
		return scale;
	}

	// http://stackoverflow.com/a/11959928
	public static float getScaleFactorToFit(Dimension original, Dimension toFit) 
	{
	    float scale = 1.0f;

	    if (original != null && toFit != null) 
	    {
	        float scaleWidth  = getScaleFactor(original.width, toFit.width);
	        float scaleHeight = getScaleFactor(original.height, toFit.height);

	        scale = Math.min(scaleHeight, scaleWidth);
	    }

	    return scale;
	}
}
