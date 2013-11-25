package csc301.ultrasound.global;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.imageio.ImageIO;

public class ImageDownloader
{	
	private static final String uploadServletURL = "http://ultrasound.azurewebsites.net/";
	
	private Connection dbConnection = null;

	public ImageDownloader(Connection dbConnection)
	{
		if (dbConnection == null)
			return;
		
		this.dbConnection = dbConnection;
	}
	
	public BufferedImage downloadUltrasound(int RID)
	{
		return download("IMGUref", RID);
	}

	public BufferedImage downloadAnnotation(int RID)
	{
		return download("IMGAref", RID);
	}
	
	private BufferedImage download(String tableColumn, int RID)
	{
		BufferedImage downloaded = null;
		
		try
		{
			// get the location of the ultrasound image
			String query = "SELECT IMGUref FROM Ultrasound.Records WHERE RID = ?;";
			PreparedStatement statement = dbConnection.prepareStatement(query);
			statement.setInt(1, RID);
			
			ResultSet rs = statement.executeQuery();
			
			if (!rs.next())
				return downloaded;
			
			String localFileLocation = rs.getString("IMGUref");
			
			downloaded = ImageIO.read(new URL(String.format("%s%s", uploadServletURL, localFileLocation)));
			
			return downloaded;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return downloaded;
	}
}
