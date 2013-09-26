package csc301.ultrasound.global;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterOutputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

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
			
			float ratio = (float)compressor.getBytesWritten() / (float)compressor.getBytesRead();
			
			System.out.format("Compressed %d bytes to %d bytes. Ratio: %.2f\n", compressor.getBytesRead(), compressor.getBytesWritten(), ratio);
			
			compressor.end();
			
			return stream.toByteArray();
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
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
			
			float ratio = (float)decompressor.getBytesWritten() / (float)decompressor.getBytesRead();
			
			System.out.format("Decompressed %d bytes to %d bytes. Ratio: %.2f\n", decompressor.getBytesRead(), decompressor.getBytesWritten(), ratio);
			
			return stream.toByteArray();
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
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
}
