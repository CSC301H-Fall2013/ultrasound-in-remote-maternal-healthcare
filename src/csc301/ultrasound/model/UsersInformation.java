package csc301.ultrasound.model;

import java.sql.*;
import java.util.Date;

import javax.swing.table.AbstractTableModel;

/**
 * Create a two-dimensional table consisting of the users corresponding
 * to the given authlevel.
 */
public class UsersInformation extends AbstractTableModel
{
	private static final long serialVersionUID = 1L;
	
	private Object[][] data = null;
	private String[] columnNames = { "User ID", "Username", "Email", "Last Login", "Phone", "Location" };
	
	public UsersInformation(int authlevel, Connection dbConnection)
	{
		// Create a new table consisting of the patient's information.
		// Todo: Remove restriction on size.
		Object[][] userInfoTable = new Object[100][columnNames.length];

		if (dbConnection != null)
		{
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

				int rowCount = 0;
				
				while (rs.next())
				{
					// Extract data from the result set.
					int    id        = rs.getInt("id");
					String username  = rs.getString("username");
					String email     = rs.getString("email");
					Date   lastLogin = rs.getDate("last_login");
					int    phone     = rs.getInt("phone");
					String location  = rs.getString("location");

					// Record the data in the record table.
					userInfoTable[rowCount][0] = id;
					userInfoTable[rowCount][1] = username;
					userInfoTable[rowCount][2] = email;
					userInfoTable[rowCount][3] = lastLogin;
					userInfoTable[rowCount][4] = phone;
					userInfoTable[rowCount][5] = location;
					
					rowCount++;
				}
				
				data = userInfoTable;
			} 
			catch (SQLException se)
			{
				se.printStackTrace();
			}
		} 
		else
			System.out.println("Connection failure.");
	}

	public int getColumnCount()
	{
		return columnNames.length;
	}

	public int getRowCount()
	{
		return data.length;
	}

	public String getColumnName(int col)
	{
		return columnNames[col];
	}

	public Object getValueAt(int row, int col)
	{
		return data[row][col];
	}
}