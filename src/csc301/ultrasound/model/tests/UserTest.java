package csc301.ultrasound.model.tests;

import static org.junit.Assert.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import csc301.ultrasound.model.User;

/**
 * Automated unit tests for the User class.
 */
public class UserTest 
{
	private static User user = null;
	
	private static int    userID        = 1234;
	private static String username      = "foobar";
	private static String userName      = "Foo Bar";
	private static String userEmail     = "foo@email.com";
	private static String userLocation  = "Bundy";
	private static int    userPhone     = 1234567890;
	private static int    userAuthlevel = 0;
	
	/**
	 * Any initialization needed before the tests begin.
	 */
	@BeforeClass
	public static void testSetup() 
	{
		user = new User(userID, username, userName, userEmail, userLocation, userPhone, userAuthlevel);
	}

	/**
	 * Any cleanup needed after the tests are run.
	 */
	@AfterClass
	public static void testCleanup() 
	{
	}
	
	@Test
	public void testUserGetID() 
	{
		System.out.println("Running testUserGetID()...");
		
		assertEquals(user.getID(), userID);
	}
	
	@Test
	public void testUserGetUsername() 
	{
		System.out.println("Running testUserGetUsername()...");
		
		assertEquals(user.getUsername(), username);
	}
	
	@Test
	public void testUserGetName() 
	{
		System.out.println("Running testUserGetName()...");
		
		assertEquals(user.getName(), userName);
	}
	
	@Test
	public void testUserGetEmail()
	{
		System.out.println("Running testUserGetEmail()...");
		
		assertEquals(user.getEmail(), userEmail);
	}
	
	@Test
	public void testUserGetLocation() 
	{
		System.out.println("Running testUserGetLocation()...");
		
		assertEquals(user.getLocation(), userLocation);
	}
	
	@Test
	public void testUserGetPhone() 
	{
		System.out.println("Running testUserGetPhone()...");
		
		assertEquals(user.getPhone(), userPhone);
	}
	
	@Test
	public void testUserGetAuthlevel() 
	{
		System.out.println("Running testUserGetAuthlevel()...");
		
		assertEquals(user.getAuthlevel(), userAuthlevel);
	}
}
