package csc301.ultrasound.global;

import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;

public class ImageDownloader
{
	private static final String serverURLBase = "http://ultrasound.azurewebsites.net/";

	public ImageDownloader() { /* do nothing */ }
	
	public BufferedImage download(String filename)
	{
		try
		{
			return ImageIO.read(new URL(serverURLBase + filename));
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}