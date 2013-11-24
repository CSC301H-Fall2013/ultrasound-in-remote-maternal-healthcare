package csc301.ultrasound.model;

import java.sql.*;
import java.util.Date;

import javax.swing.table.AbstractTableModel;

/**
 * Create a two-dimensional table consisting of the patient information of
 * the patient with the given patientID.
 */
public class PatientInformationTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1L;
	
	/** The data to be displayed in the table. */
	private Object[][] data = null;
	
	/** The column names of the table. */
	private String[] columnNames = { "Patient ID", "Name", "Birthday", "Country" };
	
	/**
	 * Instantiates a new patient information table model.
	 *
	 * @param currPatientId The patient id to display.
	 * @param dbConnection An established connection to the database.
	 */
	public PatientInformationTableModel(int currPatientId, Connection dbConnection)
	{
		// Create a new table consisting of the patient's information.
		Object[][] patientInfoTable = new Object[1][columnNames.length];

		if (dbConnection != null)
		{
			try
			{
				Statement statement = dbConnection.createStatement();

				// Query the database for information about the patient.
				String query = "select FirstName, LastName, Birthdate, Country "
						     + "from ultrasound.Patients " 
						     + "where PID = " + Integer.toString(currPatientId);
				
				ResultSet rs = statement.executeQuery(query);

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
		return data.length;
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
		return data[row][col];
	}
}