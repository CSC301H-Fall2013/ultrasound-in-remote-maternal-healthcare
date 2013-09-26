package csc301.ultrasound.global.tests;

import static org.junit.Assert.*;

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
		fail("Not yet implemented");
	}

	@Test
	public void testDecrypt() 
	{
		fail("Not yet implemented");
	}

}
