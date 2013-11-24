package csc301.ultrasound.model;

import java.sql.*;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

/**
 * Creates a two-dimensional table consisting of the patient history of the
 * patient with the given patientID.
 */
public class PatientHistoryTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1L;

	/** The data to be displayed in the table. */
	private ArrayList<PatientHistoryTableModelEntry> data = null;
	
	/** The column names. */
	private String[] columnNames = { "Record ID", "Patient ID", "Submission Time", "Comments", "Response" };
	
	/**
	 * Instantiates a new patient history table model.
	 *
	 * @param value the value
	 * @param dbColumnToFilter the db column to filter
	 * @param niceName the nice name
	 * @param dbConnection the db connection
	 */
	public PatientHistoryTableModel(int patientID, Connection dbConnection)
	{
		data = new ArrayList<PatientHistoryTableModelEntry>();
		
		try
		{
			Statement statement = dbConnection.createStatement();

			// Query the database for previous records of the patient.
			String query = "select RID, Date, FieldworkerComments, RadiologistResponse "
						 + "from ultrasound.Records "
						 + "where PID = " + patientID + " "
						 + "order by Date desc";
			
			ResultSet rs = statement.executeQuery(query);

			while (rs.next())
			{
				// Extract data from the result set.
				int recordID = rs.getInt("RID");
				Timestamp date = rs.getTimestamp("Date");
				String comments = rs.getString("FieldworkerComments");
				String response = rs.getString("RadiologistResponse");

				// Record the data in the record table.
				data.add(new PatientHistoryTableModelEntry(recordID, patientID, date, comments, response));
			}
		} 
		catch (SQLException se)
		{
			se.printStackTrace();
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
		// we overrode this method to utilze ArrayLists over static arrays
		switch (col)
		{
			case 0:  return data.get(row).getRecordID();
			case 1:  return data.get(row).getPatientID();
			case 2:  return data.get(row).getDate();
			case 3:  return data.get(row).getComments();
			case 4:  return data.get(row).getResponse();
			default: return null;
		}
	}
	
	/**
	 * Container class for each row of the table.
	 */
	class PatientHistoryTableModelEntry
	{
		private int       recordID  = -1;
		private int       patientID = -1;
		private Timestamp date      = null;
		private String    comments  = null;
		private String    response  = null;
		
		/**
		 * Instantiates a new record table model entry.
		 *
		 * @param recordID The record id
		 * @param patientID The patient id
		 * @param date The date
		 * @param comments The comments
		 * @param response The response
		 */
		PatientHistoryTableModelEntry(int recordID, int patientID, Timestamp date, String comments, String response)
		{
			this.recordID = recordID;
			this.patientID = patientID;
			this.date = date;
			this.comments = comments;
			this.response = response;
		}
		
		/**
		 * Returns the record id.
		 *
		 * @return The record id
		 */
		public int getRecordID()
		{ 
			return recordID; 
		}
		
		/**
		 * Returns the patient id.
		 *
		 * @return The patient id
		 */
		public int getPatientID() 
		{ 
			return patientID; 
		}
		
		/**
		 * Returns the date.
		 *
		 * @return The date
		 */
		public Timestamp getDate()
		{ 
			return date; 
		}
		
		/**
		 * Returns the comments.
		 *
		 * @return The comments
		 */
		public String getComments()
		{ 
			return comments; 
		}
		
		/**
		 * Returns the response.
		 *
		 * @return The response
		 */
		public String getResponse()
		{ 
			return response; 
		}
	}
}
