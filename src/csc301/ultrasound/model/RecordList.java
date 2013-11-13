package csc301.ultrasound.model;

import java.sql.*;

import javax.swing.table.AbstractTableModel;

/**
 * Create a two-dimensional table consisting of records, which is used to
 * add to the table model of of records of the patient.
 */
public class RecordList extends AbstractTableModel
{
	private static final long serialVersionUID = 1L;
	
	private static final int maxRecordRows = 20;

	private Object[][] data = null;
	private String[] columnNames = { "Record ID", "Patient ID", "Submission Time", "Comments", "Status" };

	public RecordList(Connection dbConnection)
	{
		if (dbConnection != null)
		{
			// Create a record table containing records that need attending
			// to.
			Object[][] recordTable = new Object[maxRecordRows][columnNames.length];

			// Fill in the record table and keep track of how many entries
			// were added.
			//int numRecordRows = fillRecordTable(recordTable, connection);
			fillRecordTable(recordTable, dbConnection);
			
			// Set dataset
			data = recordTable;
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
	
	/**
	 * Fill the given record table with new records. Return the number of
	 * records added to the table.
	 */
	private int fillRecordTable(Object[][] recordTable, Connection connection)
	{
		// Keep track of how many records have been added to the table.
		int numRecords = 0;

		try
		{
			Statement statement = connection.createStatement();
			String query;
			ResultSet rs;

			// Query the database for records that have not yet been responded
			// to.
			// Fill the record table with these records.
			query = "select RID, PID, Date, FieldworkerComments "
					+ "from ultrasound.Records "
					+ "where RadiologistResponse is null "
					+ "order by Date desc";
			rs = statement.executeQuery(query);
			numRecords = fillRecordTable(rs, recordTable, numRecords,
					"No Responses");

			// Query the database for records that a user has already responded
			// to,
			// but the fieldworker has not yet received the response.
			// Fill the record table with these records.
			query = "select RID, PID, Date, FieldworkerComments "
					+ "from ultrasound.Records "
					+ "where RadiologistResponse is not null "
					+ "and FieldworkerSeen = 0 " + "order by Date desc";
			rs = statement.executeQuery(query);
			numRecords = fillRecordTable(rs, recordTable, numRecords,
					"Has Unseen Response");

			// Query the database for records that a user has already responded
			// to,
			// and the fieldworker has received the response.
			// Fill the record table with these records.
			query = "select RID, PID, Date, FieldworkerComments "
					+ "from ultrasound.Records "
					+ "where RadiologistResponse is not null "
					+ "and FieldworkerSeen = 1 " + "order by Date desc";
			rs = statement.executeQuery(query);
			numRecords = fillRecordTable(rs, recordTable, numRecords,
					"Has Seen Response");

			// Return the number of records added to the records table.
			return numRecords;
		} 
		catch (SQLException se)
		{
			System.err.println("SQL Exception.<Message>: " + se.getMessage());
			return numRecords;
		}
	}

	/**
	 * Fill the given record table with the contents of the result set, starting
	 * at the row indicated by the given record number, setting the last value
	 * of each row in the table as the status string. Return the number of
	 * records that have been added to the table.
	 */
	private int fillRecordTable(ResultSet rs, Object[][] recordTable, int numRecords, String status)
	{
		try
		{
			while (rs.next() && (numRecords < recordTable.length))
			{
				// Extract data from the result set.
				int recordID = rs.getInt("RID");
				int patientID = rs.getInt("PID");
				Timestamp date = rs.getTimestamp("Date");
				String complaint = rs.getString("FieldworkerComments");

				// Record the data in the record table.
				recordTable[numRecords][0] = recordID;
				recordTable[numRecords][1] = patientID;
				recordTable[numRecords][2] = date;
				recordTable[numRecords][3] = complaint;
				recordTable[numRecords][4] = status;

				numRecords++;
			}
			
			return numRecords;
		} 
		catch (SQLException se)
		{
			System.err.println("SQL Exception.<Message>: " + se.getMessage());
			return numRecords;
		}
	}
}