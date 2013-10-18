package csc301.ultrasound.global;

import java.io.ByteArrayOutputStream;

import java.util.zip.*;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
	 * Encrypt the provided byte array using AES-128 encryption with a secret key cipher.
	 *
	 * @param toEncrypt The byte array to encrypt
	 * @param key The secret key to encrypt the data with
	 * @return The encrypted data. null if an error occured.
	 */
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
	
	/**
	 * Decrypt the provided byte array using AES-128 encryption with a secret key cipher.
	 * Note that x = decrypt(encrypt(x, k), k), must hold for any given byte array x, and
	 * a secret key k.
	 *
	 * @param toDecrypt The byte array to decrypt
	 * @param key The secret key used to encrypt the data
	 * @return The decrypted data. null if an error occured.
	 */
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
	 * @return true, if the connection was successfully closed. false otherwise, or an error occured.
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
}
