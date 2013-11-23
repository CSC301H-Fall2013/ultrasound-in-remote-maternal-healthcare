package csc301.ultrasound.model;

import java.sql.*;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

/**
 * Create a two-dimensional table consisting of records, which is used to
 * add to the table model of of records of the patient.
 */
public class RecordList extends AbstractTableModel
{
	private static final long serialVersionUID = 1L;
	
	private ArrayList<RecordListEntry> data = null;
	
	private String[] columnNames = { "Record ID", "Patient ID", "Submission Time", "Comments", "Status" };

	public RecordList(Connection dbConnection)
	{
		if (dbConnection != null)
		{
			// Create a record table containing records that need attending to.
			data = new ArrayList<RecordListEntry>();

			// Fill in the record table and keep track of how many entries were added.
			fillRecordTable(dbConnection);
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
			case 0:  return data.get(row).getRecordID();
			case 1:  return data.get(row).getPatientID();
			case 2:  return data.get(row).getDate();
			case 3:  return data.get(row).getComplaint();
			case 4:  return data.get(row).getStatus();
			default: return null;
		}
	}
	
	/**
	 * Fill the given record table with new records. Return the number of
	 * records added to the table.
	 */
	private int fillRecordTable(Connection connection)
	{
		// Keep track of how many records have been added to the table.
		int numRecords = 0;

		try
		{
			Statement statement = connection.createStatement();
			String query;
			ResultSet rs;

			// Query the database for records that have not yet been responded
			// to. Fill the record table with these records.
			query = "select RID, PID, Date, FieldworkerComments "
				  + "from ultrasound.Records "
				  + "where RadiologistResponse is null "
				  + "order by Date desc";
			rs = statement.executeQuery(query);
			numRecords = fillRecordTable(rs, numRecords, "No Response");

			// Query the database for records that a user has already responded
			// to, but the fieldworker has not yet received the response.
			// Fill the record table with these records.
			query = "select RID, PID, Date, FieldworkerComments "
				  + "from ultrasound.Records "
				  + "where RadiologistResponse is not null and FieldworkerSeen = 0 " 
				  + "order by Date desc";
			rs = statement.executeQuery(query);
			numRecords = fillRecordTable(rs, numRecords, "Responded");

			// Query the database for records that a user has already responded
			// to, and the fieldworker has received the response.
			// Fill the record table with these records.
			query = "select RID, PID, Date, FieldworkerComments "
				  + "from ultrasound.Records "
				  + "where RadiologistResponse is not null and FieldworkerSeen = 1 " 
				  + "order by Date desc";
			rs = statement.executeQuery(query);
			numRecords = fillRecordTable(rs, numRecords, "Received");

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
	private int fillRecordTable(ResultSet rs, int numRecords, String status)
	{
		try
		{
			while (rs.next())
			{
				// Extract data from the result set.
				int       recordID  = rs.getInt("RID");
				int       patientID = rs.getInt("PID");
				Timestamp date      = rs.getTimestamp("Date");
				String    complaint = rs.getString("FieldworkerComments");

				// Record the data in the record table.
				data.add(new RecordListEntry(recordID, patientID, date, complaint, status));

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
	
	class RecordListEntry
	{
		private int       recordID  = -1;
		private int       patientID = -1;
		private Timestamp date      = null;
		private String    complaint = null;
		private String    status    = null;
		
		RecordListEntry(int recordID, int patientID, Timestamp date, String complaint, String status)
		{
			this.recordID = recordID;
			this.patientID = patientID;
			this.date = date;
			this.complaint = complaint;
			this.status = status;
		}
		
		public int       getRecordID()  { return recordID; }
		public int       getPatientID() { return patientID; }
		public Timestamp getDate()      { return date; }
		public String    getComplaint() { return complaint; }
		public String    getStatus()    { return status; }
	}
}
