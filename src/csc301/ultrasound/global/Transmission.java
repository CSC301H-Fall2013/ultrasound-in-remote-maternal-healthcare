package csc301.ultrasound.global;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import java.io.*;

import java.util.zip.*;

import java.sql.*;

// TODO: Auto-generated Javadoc
/**
 * Main class that handles transmitting data to and from the database.
 */
public class Transmission
{
	/**
	 * Compress the provided byte array using ZLib. Note that it is not guaranteed that the
	 * resultant byte array will be smaller than the original.
	 *
	 * @param toCompress The byte array to compress
	 * @return The compressed byte array. null if an error occured.
	 */
	public byte[] compress(byte toCompress[])
	{
		try 
		{
			Deflater compressor = new Deflater(Deflater.BEST_COMPRESSION);
			
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			DeflaterOutputStream compressedStream = new DeflaterOutputStream(stream, compressor);
			
			compressedStream.write(toCompress);
			compressedStream.close();
			
			stream.flush();
			
			compressor.end();
			
			return stream.toByteArray();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Decompress the provided byte array using ZLib. Note that x = decompress(compress(x)),
	 * must hold for any given byte array x.
	 *
	 * @param toDecompress The byte array to decompress
	 * @return The decompressed bytes. null if an error occured.
	 */
	public byte[] decompress(byte toDecompress[])
	{
		try 
		{
			Inflater decompressor = new Inflater();
			
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			InflaterOutputStream decompressedStream = new InflaterOutputStream(stream, decompressor);
			
			decompressedStream.write(toDecompress);
			decompressedStream.close();
			
			stream.flush();
			
			return stream.toByteArray();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Connect to the SQL Server database. Note that this will timeout in 30 seconds if your 
	 * IP address range is not whitelisted in the Windows Azure SQL Firewall.
	 *
	 * @return The established connection. null if an error occured.
	 */
	public Connection connectToDB()
	{
		// try to load the driver
		try 
		{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} 
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			
			return null;
		}
		
		// connection URL provided by Windows Azure
		String connectionUrl = "jdbc:sqlserver://ze7duqnsz2.database.windows.net:1433;database=Ultrasound;user=ultrasound@ze7duqnsz2;password=csc301-erie;encrypt=true;hostNameInCertificate=data.bl2-4.database.windows.net;loginTimeout=30;";
		
		Connection connection = null;
		
		try 
		{
			connection = DriverManager.getConnection(connectionUrl);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			
			return null;
		}
		
		return connection;
	}
	
	/**
	 * Disconnect from the SQL Server database.
	 *
	 * @param connection The established connection.
	 * @return True if the connection was successfully closed. False otherwise, or an error occured.
	 */
	public boolean disconnectFromDB(Connection connection)
	{
		try 
		{
			if (connection != null && !connection.isClosed())
				connection.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * Retreives the ultrasound image from the database.
	 *
	 * @param RID The record id to update.
	 * @param connection The established connection.
	 * @return The ultrasound image.
	 */
	public BufferedImage getUltrasoundFromDB(int RID, Connection connection)
	{
		BufferedImage image = null;
		
		try
		{
			if (connection != null && !connection.isClosed())
			{
				String query = "SELECT ultrasound.Records.IMGUltrasound "
						     + "FROM ultrasound.Records "
						     + "WHERE ultrasound.Records.RID = ?;";
				
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setInt(1, RID);
				
				ResultSet rs = statement.executeQuery();
				
				if (rs.next() == false)
					return null;		// no results
				
				byte compressedImageBytes[] = rs.getBytes(1);
				
				try
				{
					byte imageBytes[] = new Transmission().decompress(compressedImageBytes);
				
					image = ImageIO.read(new ByteArrayInputStream(imageBytes));
				} 
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			
			return null;
		}
		
		return image;
	}
	
	/**
	 * Compress and upload an ultrasound image to the database.
	 *
	 * @param RID The record id to update.
	 * @param image The ultrasound image.
	 * @param connection The established connection.
	 * @return The number of sucessfully updated rows. Should always be 1, 0, or -1 on error.
	 */
	public int compressAndUploadUltrasoundToDB(int RID, BufferedImage image, Connection connection)
	{
		return compressAndUploadImageToDB(RID, image, "IMGUltrasound", connection);
	}

	/**
	 * Compress and upload an annotation image to the database.
	 *
	 * @param RID The record id to update.
	 * @param image The annotation image.
	 * @param connection The established connection.
	 * @return The number of sucessfully updated rows. Should always be 1, 0, or -1 on error.
	 */
	public int compressAndUploadAnnotationImgToDB(int RID, BufferedImage image, Connection connection)
	{
		return compressAndUploadImageToDB(RID, image, "IMGAnnotation", connection);
	}
	
	/**
	 * Compress and upload an image to the database.
	 *
	 * @param RID The record id to update.
	 * @param image The image to upload.
	 * @param attribute The attribute in the Records table to update.
	 * @param connection The established connection.
	 * @return The number of sucessfully updated rows. Should always be 1, 0, or -1 on error.
	 */
	private int compressAndUploadImageToDB(int RID, BufferedImage image, String attribute, Connection connection)
	{	
		if (ridExists(RID, connection))
		{
			try
			{
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				
				ImageIO.write(image, "png", baos);
				
				baos.flush();
			
				byte compressed[] = compress(baos.toByteArray());
				
				PreparedStatement statement = connection.prepareStatement(String.format("UPDATE ultrasound.Records SET %s = ? WHERE RID = ?", attribute));
				statement.setBytes(1, compressed);
				statement.setInt(2, RID);
				statement.executeUpdate();
				
				return statement.getUpdateCount();
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		return -1;
	}
	
	/**
	 * Checks if an RID (Record ID) exists in the database.
	 *
	 * @param RID The record id to look for.
	 * @param connection The established connection.
	 * @return True, if the RID exists. False otherwise.
	 */
	public boolean ridExists(int RID, Connection connection)
	{
		String query = "SELECT COUNT(*) "
                     + "FROM ultrasound.Records "
                     + "WHERE RID = ?;";
		
		try
		{
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, RID);
			statement.executeQuery();
			
			ResultSet rs = statement.getResultSet();
			
			if (rs.next() != false)
			{
				int ridCount = rs.getInt(1);
				
				if (ridCount > 0)
					return true;
			}
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
}
