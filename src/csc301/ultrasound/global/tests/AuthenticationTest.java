package csc301.ultrasound.global.tests;

import static org.junit.Assert.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import csc301.ultrasound.global.Authentication;

/**
 * Automated unit tests for the Authentication class.
 */
public class AuthenticationTest 
{
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
	 * Test the authentication method with the correct password.
	 */
	@Test
	public void testAuthenticateGoodPassword() 
	{
		System.out.println("Running testAuthenticateGoodPassword()...");
		
		Authentication auth = new Authentication();
		
		assertNotNull(auth.authenticate("foobar", "password"));
	}
	
	/**
	 * Test the authentication method with an incorrect password.
	 */
	@Test
	public void testAuthenticateBadPassword() 
	{
		System.out.println("Running testAuthenticateBadPassword()...");
		
		Authentication auth = new Authentication();

		assertNull(auth.authenticate("foobar", "foo"));
	}
}
