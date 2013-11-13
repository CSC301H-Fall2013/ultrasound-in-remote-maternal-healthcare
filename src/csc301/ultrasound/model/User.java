package csc301.ultrasound.model;

/**
 * Container for a single user. Responsible for high level functions specifically pertaining
 * to a single user.
 */
public class User
{
	private int    id        = -1;
	private String email     = "";
	private String name      = "";
	private String location  = "";
	private int    phone     = -1;
	private int    authlevel = -1;
	
	/**
	 * Instantiates a new user.
	 *
	 * @param email The email address of the registered user
	 * @param name The name of the registered user
	 * @param location The location of the registered user
	 * @param phone The phone number of the registered user
	 * @param authlevel The authentication level of the registered user
	 */
	public User(int id, String email, String name, String location, int phone, int authlevel) 
	{
		// Todo: Check for valid email, phone number and authlevel
		this.id        = id;
		this.email     = email;
		this.name      = name;
		this.location  = location;
		this.phone     = phone;
		this.authlevel = authlevel;
	}
	
	/**
	 * Returns the unique ID of the registered user.
	 *
	 * @return The user's id
	 */
	public int getID()
	{ 
		return id; 
	}

	/**
	 * Returns the email address of the registered user.
	 *
	 * @return The user's email address
	 */
	public String getEmail()
	{ 
		return email; 
	}
	
	/**
	 * Returns the name of the registered user.
	 *
	 * @return The user's name
	 */
	public String getName() 
	{ 
		return name; 
	}
	
	/**
	 * Returns the location of the registered user.
	 *
	 * @return The user's location
	 */
	public String getLocation()  
	{ 
		return location; 
	}
	
	/**
	 * Returns the phone number of the registered user.
	 *
	 * @return The user's phone number
	 */
	public int getPhone()     
	{ 
		return phone; 
	}
	
	/**
	 * Returns the authorization level of the registered user.
	 *
	 * @return The user's authorization level
	 */
	public int getAuthlevel()
	{ 
		return authlevel; 
	}
}
