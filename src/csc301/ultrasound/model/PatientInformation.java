package csc301.ultrasound.model;

import java.sql.*;
import java.util.Date;

import javax.swing.table.AbstractTableModel;

/**
 * Create a two-dimensional table consisting of the patient information of
 * the patient with the given patientID.
 */
public class PatientInformation extends AbstractTableModel
{
	private static final long serialVersionUID = 1L;
	
	private Object[][] data = null;
	private String[] columnNames = { "Patient ID", "Name", "Birthday", "Country" };
	
	public PatientInformation(int currPatientId, Connection dbConnection)
	{
		// Create a new table consisting of the patient's information.
		Object[][] patientInfoTable = new Object[1][4];

		if (dbConnection != null)
		{
			try
			{
				Statement statement = dbConnection.createStatement();
				String query;
				ResultSet rs;

				// Query the database for information about the patient.
				query = "select FirstName, LastName, Birthdate, Country "
						+ "from ultrasound.Patients " + "where PID = "
						+ Integer.toString(currPatientId);
				
				rs = statement.executeQuery(query);

				while (rs.next())
				{
					// Extract data from the result set.
					String firstName = rs.getString("FirstName");
					String lastName = rs.getString("LastName");
					Date birthday = rs.getDate("Birthdate");
					String country = rs.getString("Country");

					// Record the data in the record table.
					patientInfoTable[0][0] = currPatientId;
					patientInfoTable[0][1] = firstName + " " + lastName;
					patientInfoTable[0][2] = birthday;
					patientInfoTable[0][3] = country;
				}
				
				data = patientInfoTable;
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