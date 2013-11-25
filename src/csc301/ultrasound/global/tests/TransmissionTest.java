package csc301.ultrasound.global.tests;

import java.sql.Connection;

import static org.junit.Assert.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import csc301.ultrasound.global.Transmission;

/**
 * Automated unit tests for the Transmission class.
 */
public class TransmissionTest 
{
	/** The input data to test against. */
	private static byte originalData[] = { 98, 108, 97, 104, 98, 108, 97, 104, 98, 108, 97, 104, 63, 63, 
										   98, 108, 97, 104, 98, 108, 97, 104, 98, 108, 97, 104, 63, 63,  
										   98, 108, 97, 104, 98, 108, 97, 104, 98, 108, 97, 104, 63, 63, 
										   98, 108, 97, 104, 98, 108, 97, 104, 98, 108, 97, 104, 63, 63, 
										   98, 108, 97, 104, 98, 108, 97, 104, 98, 108, 97, 104, 63, 63, 
										   98, 108, 97, 104, 98, 108, 97, 104, 98, 108, 97, 104, 63, 63};
	
	/** The expected compressed value of originalData[]. */
	private static byte gtCompressedData[] = {120, -38, 75, -54, 73, -52, 72, -126, 98, 123, -5, 36, -86, 
											  -16, 0, 73, 3, 31, -109};
	
	/**
	 * Any initialization needed before the tests begin.
	 */
	@BeforeClass
	public static void testSetup() 
	{
	}

	/**
	 * Any cleanup needed after the tests are run.
	 */
	@AfterClass
	public static void testCleanup() 
	{
	}
	  
	/**
	 * Test the compression method.
	 */
	@Test
	public void testCompress() 
	{
		System.out.println("Running testCompress()...");
		
		Transmission t = new Transmission();
		
		byte compressed[] = t.compress(originalData);
		
		assertEquals("The length of the compressed data must match the length of the ground truth.", compressed.length, gtCompressedData.length);
		
		for (int i = 0; i < compressed.length; i++)
			assertTrue("Every byte of the compressed data must equal the ground truth's.", compressed[i] == gtCompressedData[i]);
	}

	/**
	 * Test the decompression method.
	 */
	@Test
	public void testDecompress() 
	{
		System.out.println("Running testDecompress()...");
		
		Transmission t = new Transmission();
		
		byte decompressed[] = t.decompress(gtCompressedData);
		
		assertEquals("The length of the decompressed data must match the length of the ground truth.", decompressed.length, originalData.length);
		
		for (int i = 0; i < decompressed.length; i++)
			assertTrue("Every byte of the decompressed data must equal the ground truth's.", decompressed[i] == originalData[i]);
	}
	
	/**
	 * Test the database connection method.
	 */
	@Test
	public void testConnectToDB()
	{
		System.out.println("Running testConnectToDB()...");
		
		Transmission t = new Transmission();
		
		Connection connection = t.connectToDB();
		
		assertNotNull(connection);
	}
	
	/**
	 * Test the database disconnection method.
	 */
	@Test
	public void testDisconnectFromDB()
	{
		System.out.println("Running testDisconnectFromDB()...");
		
		Transmission t = new Transmission();
		
		Connection connection = t.connectToDB();
		
		boolean successful = t.disconnectFromDB(connection);
		
		assertTrue(successful);
	}

	/**
	 * Test the method to check if an RID exists or not with a valid RID.
	 */
	@Test
	public void testRidExistsGoodRID()
	{
		System.out.println("Running testRidExistsGoodRID()...");
		
		Transmission t = new Transmission();
		
		Connection connection = t.connectToDB();
		
		if (t.ridExists(15, connection) == false)
			System.err.println("Error. The RID that this test relies on has been removed. Please specify a new one in TransmissionTest.java");
		
		boolean exists = t.ridExists(15, connection);
		
		assertTrue(exists);
	}
	
	/**
	 * Test the method to check if an RID exists or not with an invalid RID.
	 */
	@Test
	public void testRidExistsBadRID()
	{
		System.out.println("Running testRidExistsBadRID()...");
		
		Transmission t = new Transmission();
		
		Connection connection = t.connectToDB();
		
		boolean exists = t.ridExists(-1, connection);
		
		assertFalse(exists);
	}
}
