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
	
	private ArrayList<UserInformationTableModelEntry> data = null;
	private String[] columnNames = { "User ID", "Username", "Email", "Last Login", "Phone", "Location" };
	
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

	public int getColumnCount()
	{
		return columnNames.length;
	}

	public int getRowCount()
	{
		return data.size();
	}

	public String getColumnName(int col)
	{
		return columnNames[col];
	}
	
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
	
	class UserInformationTableModelEntry
	{
		private int    id        = -1;
		private String username  = null;
		private String email     = null;
		private Date   lastLogin = null;
		private int    phone     = -1;
		private String location  = null;
		
		UserInformationTableModelEntry(int id, String username, String email, Date lastLogin, int phone, String location)
		{
			this.id = id;
			this.username = username;
			this.email = email;
			this.lastLogin = lastLogin;
			this.phone = phone;
			this.location = location;
		}
		
		public int    getID()        { return id; }
		public String getUsername()  { return username; }
		public String getEmail()     { return email; }
		public Date   getLastLogin() { return lastLogin; }
		public int    getPhone()     { return phone; }
		public String getLocation()  { return location; }
	}
}