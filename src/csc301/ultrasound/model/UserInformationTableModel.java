package csc301.ultrasound.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.table.AbstractTableModel;

/**
 * Create a two-dimensional table consisting of the users corresponding
 * to the given authlevel.
 */
public class UserInformationTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1L;

	/** The data to be displayed in the table. */
	private ArrayList<UserInformationTableModelEntry> data = null;

	/** The column names of the table. */
	private String[] columnNames = { "User ID", "Username", "Email", "Last Login", "Phone", "Location" };
	
	/**
	 * Instantiates a new user information table model.
	 *
	 * @param authlevel The authlevel of interest.
	 * @param dbConnection An established connection to the database.
	 */
	public UserInformationTableModel(int authlevel, Connection dbConnection)
	{
		// Create a new table consisting of the patient's information.
		if (dbConnection != null)
		{
			data = new ArrayList<UserInformationTableModelEntry>();
			
			try
			{
				Statement statement = dbConnection.createStatement();
				String query;
				ResultSet rs;

				// Query the database for information about the patient.
				query = "select id, username, email, last_login, phone, location "
					  + "from ultrasound.Users "
					  + "where authlevel = " + Integer.toString(authlevel) + " and activated = 1 and banned = 0";
				
				rs = statement.executeQuery(query);
				
				while (rs.next())
				{
					// Extract data from the result set.
					int    id        = rs.getInt("id");
					String username  = rs.getString("username");
					String email     = rs.getString("email");
					Date   lastLogin = rs.getDate("last_login");
					int    phone     = rs.getInt("phone");
					String location  = rs.getString("location");
					
					
					data.add(new UserInformationTableModelEntry(id, username, email, lastLogin, phone, location));
				}
			} 
			catch (SQLException se)
			{
				se.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount()
	{
		return columnNames.length;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount()
	{
		return data.size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	public String getColumnName(int col)
	{
		return columnNames[col];
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int col)
	{
		switch (col)
		{
			case 0:  return data.get(row).getID();
			case 1:  return data.get(row).getUsername();
			case 2:  return data.get(row).getEmail();
			case 3:  return data.get(row).getLastLogin();
			case 4:  return data.get(row).getPhone();
			case 5:  return data.get(row).getLocation();
			default: return null;
		}
	}
	
	/**
	 * Container class for each row of the table.
	 */
	class UserInformationTableModelEntry
	{
		private int    id        = -1;
		private String username  = null;
		private String email     = null;
		private Date   lastLogin = null;
		private int    phone     = -1;
		private String location  = null;
		
		/**
		 * Instantiates a new user information table model entry.
		 *
		 * @param id The users's user id
		 * @param username The user's username
		 * @param email The user's email address
		 * @param lastLogin The user's last login time
		 * @param phone The user's phone number
		 * @param location The user's location
		 */
		UserInformationTableModelEntry(int id, String username, String email, Date lastLogin, int phone, String location)
		{
			this.id = id;
			this.username = username;
			this.email = email;
			this.lastLogin = lastLogin;
			this.phone = phone;
			this.location = location;
		}
		
		/**
		 * Returns the user's user id.
		 *
		 * @return The users's user id
		 */
		public int getID()
		{ 
			return id; 
		}
		
		/**
		 * Returns the user's username.
		 *
		 * @return The user's username
		 */
		public String getUsername()
		{ 
			return username; 
		}
		
		/**
		 * Returns the user's email address.
		 *
		 * @return The user's email address
		 */
		public String getEmail()
		{ 
			return email; 
		}
		
		/**
		 * Returns the user's last login time.
		 *
		 * @return The user's last login time
		 */
		public Date getLastLogin() 
		{ 
			return lastLogin; 
		}
		
		/**
		 * Returns the user's phone number.
		 *
		 * @return The user's phone number
		 */
		public int getPhone()
		{ 
			return phone;
		}
		
		/**
		 * Returns the user's location.
		 *
		 * @return The user's location
		 */
		public String getLocation()
		{ 
			return location; 
		}
	}
}