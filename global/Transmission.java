package com.utoronto.csc301.ultrasound;

import java.io.*;
import java.util.zip.*;

import javax.crypto.*;

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
	
	public void encrypt(byte toEncrypt[])
	{
	}
	
	public void decrypt(byte toDecrypt[])
	{
	}
}