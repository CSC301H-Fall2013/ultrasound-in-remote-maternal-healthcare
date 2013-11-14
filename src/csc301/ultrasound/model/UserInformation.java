package csc301.ultrasound.model;

import java.sql.*;

import javax.swing.table.AbstractTableModel;

/**
 * Create a two-dimensional table consisting of the patient history of the
 * patient with the given patientID. The patient history consists of the
 * last numRecords number of records of the patient.
 */
public class UserInformation extends AbstractTableModel
{
	private static final long serialVersionUID = 1L;
	
	private static Object[][] data;
	private static String[] columnNames = { "User ID", "User Name", "email", "Phone Number", "Location" };
	
	public UserInformation(int authLevel, Connection dbConnection)
	{
		// The number of user records added to the table.
		int numUserRecords = 0;

		// Create a new table consisting of the patient's information.
		Object[][] userTable;
		userTable = new Object[100][5];
		
		if (dbConnection != null)
		{
			try
			{
				Statement statement = dbConnection.createStatement();
				String query;
				ResultSet rs;

				// Query the database for user information with the provided authlevel.
				query = "select id, username, email, phone, location "
						+ "from ultrasound.users "
						+ "where authlevel = " 
						+ Integer.toString(authLevel)
						+ " "
						+ "order by id desc ";
				rs = statement.executeQuery(query);

				while (rs.next())
				{
					// Extract data from the result set.
					int userID = rs.getInt("id");
					String username = rs.getString("username");
					String email = rs.getString("email");
					int phoneNo = rs.getInt("phone");
					String location = rs.getString("location");
					

					// Record the data in the record table.
					userTable[numUserRecords][0] = userID;
					userTable[numUserRecords][1] = username;
					userTable[numUserRecords][2] = email;
					userTable[numUserRecords][3] = phoneNo;
					userTable[numUserRecords][4] = location;

					numUserRecords++;
				}

				data = userTable;
			} 
			catch (SQLException se)
			{
				se.printStackTrace();
			}
		} 
		else
		{
			System.out.println("Connection failure.");
		}
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
