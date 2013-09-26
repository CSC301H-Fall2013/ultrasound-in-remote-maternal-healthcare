package csc301.ultrasound.global.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import csc301.ultrasound.global.Transmission;

public class TransmissionTest 
{
	private static byte originalData[] = { 98, 108, 97, 104, 98, 108, 97, 104, 98, 108, 97, 104, 63, 63, 
										   98, 108, 97, 104, 98, 108, 97, 104, 98, 108, 97, 104, 63, 63,  
										   98, 108, 97, 104, 98, 108, 97, 104, 98, 108, 97, 104, 63, 63, 
										   98, 108, 97, 104, 98, 108, 97, 104, 98, 108, 97, 104, 63, 63, 
										   98, 108, 97, 104, 98, 108, 97, 104, 98, 108, 97, 104, 63, 63, 
										   98, 108, 97, 104, 98, 108, 97, 104, 98, 108, 97, 104, 63, 63};
	
	private static byte gtCompressedData[] = {120, -38, 75, -54, 73, -52, 72, -126, 98, 123, -5, 36, -86, 
											  -16, 0, 73, 3, 31, -109};
	
	private static byte gtEncryptedData[] = {-68, 77, -102, 29, -16, -72, -125, -121, -102, -87, 125, -34, -33, 110, 
											  38, 39, 76, -73, -117, 12, 90, -84, -85, -111, -28, 12, -45, -18,
											 -107, -89, 101, -37, -118, 108, 76, 35, -113, 36, 44, 11, 34, 125,
											  0, -46, 49, -112, 94, -53, 121, -117, 103, -30, 89, 39, 81, -70,
											 -101, -45, -44, -107, -72, 35, 31, 77, 115, 64, -114, -43, 16, 66,
											 -5, -31, -13, -109, 82, -74, -113, -83, -47, -109, -99, -27, 65, 111,
											 -120, -22, -98, -95, 16, 42, -40, 127, -72, -76, 20, -63};
	
	@BeforeClass
	public static void testSetup() 
	{
	}

	@AfterClass
	public static void testCleanup() 
	{
	}
	  
	@Test
	public void testCompress() 
	{
		Transmission t = new Transmission();
		
		byte compressed[] = t.compress(originalData);
		
		assertEquals("The length of the compressed data must match the length of the ground truth.", compressed.length, gtCompressedData.length);
		
		for (int i = 0; i < compressed.length; i++)
			assertTrue("Every byte of the compressed data must equal the ground truth's.", compressed[i] == gtCompressedData[i]);
	}

	@Test
	public void testDecompress() 
	{
		Transmission t = new Transmission();
		
		byte decompressed[] = t.decompress(gtCompressedData);
		
		assertEquals("The length of the decompressed data must match the length of the ground truth.", decompressed.length, originalData.length);
		
		for (int i = 0; i < decompressed.length; i++)
			assertTrue("Every byte of the decompressed data must equal the ground truth's.", decompressed[i] == originalData[i]);
	}

	@Test
	public void testEncrypt() 
	{
		Transmission t = new Transmission();
		
		String key = "ThisIsASecretKey";
		
		try 
		{
			byte encrypted[] = t.encrypt(originalData, key.getBytes("UTF-8"));

			assertEquals("The length of the encrypted data must match the length of the ground truth.", encrypted.length, gtEncryptedData.length);
			
			for (int i = 0; i < encrypted.length; i++)
				assertTrue("Every byte of the encrypted data must equal the ground truth's.", encrypted[i] == gtEncryptedData[i]);
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
	}

	@Test
	public void testDecrypt() 
	{
		Transmission t = new Transmission();
		
		String key = "ThisIsASecretKey";
		
		try 
		{
			byte decrypted[] = t.decrypt(gtEncryptedData, key.getBytes("UTF-8"));

			assertEquals("The length of the decrypted data must match the length of the ground truth.", decrypted.length, originalData.length);
			
			for (int i = 0; i < decrypted.length; i++)
				assertTrue("Every byte of the decrypted data must equal the ground truth's.", decrypted[i] == originalData[i]);
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
	}
}
