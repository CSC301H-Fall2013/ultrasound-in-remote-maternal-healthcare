package csc301.ultrasound.global;

import java.io.ByteArrayOutputStream;

import java.util.zip.*;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Transmission
{
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
	
	public byte[] encrypt(byte toEncrypt[], byte key[])
	{
		try 
		{
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			
			SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			
			return cipher.doFinal(toEncrypt);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public byte[] decrypt(byte toDecrypt[], byte key[])
	{
		try
		{
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		
			SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
		
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
		
			return cipher.doFinal(toDecrypt);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Connection connectToDB()
	{
		try 
		{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} 
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			
			return null;
		}
		
		String connectionUrl = "jdbc:sqlserver://ul4h2kjyow.database.windows.net:1433;database=Ultrasound;user=ultrasound@ul4h2kjyow;password=*REPLACE_ME*;encrypt=true;hostNameInCertificate=data.ch1-3.database.windows.net;loginTimeout=30;";
		
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
		
		System.out.println("Connected!");
				
		
		return connection;
	}
	
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
}
