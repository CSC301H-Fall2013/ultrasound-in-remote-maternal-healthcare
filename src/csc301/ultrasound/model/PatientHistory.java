package csc301.ultrasound.model;

import java.sql.*;

import javax.swing.table.AbstractTableModel;

/**
 * Create a two-dimensional table consisting of the patient history of the
 * patient with the given patientID. The patient history consists of the
 * last numRecords number of records of the patient.
 */
public class PatientHistory extends AbstractTableModel
{
	private static final long serialVersionUID = 1L;
	
	private static Object[][] data;
	private static String[] columnNames = { "Record ID", "Patient ID", "Submission Time", "Comments", "Response" };
	
	public PatientHistory(int currPatientId, Connection dbConnection)
	{
		// The maximum number of records that can be displayed
		int maxPatientRecords = 100;

		// The number of patient records added to the table.
		int numPatientRecords = 0;

		// Create a new table consisting of the patient's information.
		Object[][] patientHistoryTable;
		patientHistoryTable = new Object[maxPatientRecords][5];
		
		if (dbConnection != null)
		{
			try
			{
				Statement statement = dbConnection.createStatement();
				String query;
				ResultSet rs;

				// Query the database for previous records of the patient.
				query = "select RID, Date, FieldworkerComments, RadiologistResponse "
						+ "from ultrasound.Records "
						+ "where PID = "
						+ Integer.toString(currPatientId)
						+ " "
						+ "order by Date desc";
				rs = statement.executeQuery(query);

				while (rs.next() && (numPatientRecords < maxPatientRecords))
				{
					// Extract data from the result set.
					int recordID = rs.getInt("RID");
					Timestamp date = rs.getTimestamp("Date");
					String comments = rs.getString("FieldworkerComments");
					String response = rs.getString("RadiologistResponse");

					// Record the data in the record table.
					patientHistoryTable[numPatientRecords][0] = recordID;
					patientHistoryTable[numPatientRecords][1] = currPatientId;
					patientHistoryTable[numPatientRecords][2] = date;
					patientHistoryTable[numPatientRecords][3] = comments;
					patientHistoryTable[numPatientRecords][4] = response;

					numPatientRecords++;
				}

				data = patientHistoryTable;
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
