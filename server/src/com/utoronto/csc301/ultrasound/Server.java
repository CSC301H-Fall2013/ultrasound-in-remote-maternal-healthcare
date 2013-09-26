package com.utoronto.csc301.ultrasound;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import com.utoronto.csc301.ultrasound.Transmission;

public class Server 
{
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		testCompression();
	}
	
	private static void testCompression()
	{
		String data = "blahblahblah??blahblahblah??blahblahblah??blahblahblah??blahblahblah??blahblahblah??";
		byte[] input = {};
		
		try 
		{
			input = data.getBytes("UTF-8");
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
		
		Transmission t = new Transmission();
		
		byte compressed[] = t.compress(input);
		byte decompressed[] = t.decompress(compressed);
		
		if (!Arrays.equals(input, decompressed))
			System.err.println("Error with compression!");
		
		StringBuilder sb = new StringBuilder();
		
	    for (byte b : input)
	        sb.append(String.format("%02X ", b));
	    
	    System.out.println(sb.toString());
	    
	    sb = new StringBuilder();
		
	    for (byte b : compressed)
	        sb.append(String.format("%02X ", b));
	    
	    System.out.println(sb.toString());
	    
	    sb = new StringBuilder();
		
	    for (byte b : decompressed)
	        sb.append(String.format("%02X ", b));
	    
	    System.out.println(sb.toString());
	}
}
