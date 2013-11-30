package csc301.ultrasound.global;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import csc301.ultrasound.model.User;

public class ImageUploader
{
	private static final String lineEnd    = "\r\n";
	private static final String twoHyphens = "--";
	private static final String boundary   = "*****";
	
	private static final String uploadServletURL = "http://ultrasound.azurewebsites.net/index.php/dbtest/uploader";
	private static final String localServerLocation = "./annotated/";
	
	private Connection dbConnection = null;
	
	public ImageUploader(Connection dbConnection)
	{
		if (dbConnection == null)
			return;
		
		this.dbConnection = dbConnection;
	}
	
	public int uploadFile(BufferedImage image, int RID, User user)
	{
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyHHmmss");
		
		String filename = String.format("%s%s%s.png", localServerLocation, user.getUsername(), dateFormat.format(new Date()));

		HttpURLConnection conn = null;
		DataOutputStream dos   = null;

		try
		{
			// open a URL connection to the Servlet
			//FileInputStream fileInputStream = new FileInputStream(sourceFile);
			URL url = new URL(uploadServletURL);

			// Open a HTTP connection to the URL
			conn = (HttpURLConnection)url.openConnection();
			conn.setDoInput(true); 		// Allow Inputs
			conn.setDoOutput(true); 	// Allow Outputs
			conn.setUseCaches(false); 	// Don't use a Cached Copy
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("ENCTYPE", "multipart/form-data");
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			conn.setRequestProperty("uploaded_file", filename);

			dos = new DataOutputStream(conn.getOutputStream());

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes(String.format("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"%s\"%s", filename, lineEnd));

			dos.writeBytes(lineEnd);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			ImageIO.write(image, "png", baos);
			
			baos.flush();
			
			dos.write(baos.toByteArray());
			
			// send multipart form data necesssary after file data...
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			
			baos.close();
			
			dos.flush();
			dos.close();
			
			// Responses from the server (code and message)
			int serverResponseCode = conn.getResponseCode();
			
			if (serverResponseCode == 200)	// Data was sent correctly
			{
				// do DB stuff here
				String query = "UPDATE Ultrasound.Records "
							 + "SET IMGAref = ? "
							 + "WHERE RID = ?;";
				
				PreparedStatement statement = dbConnection.prepareStatement(query);
				statement.setString(1, filename);
				statement.setInt(2, RID);
				
				return statement.executeUpdate();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return -1;
	}
}
