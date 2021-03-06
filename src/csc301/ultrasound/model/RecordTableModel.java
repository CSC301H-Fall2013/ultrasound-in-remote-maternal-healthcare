package csc301.ultrasound.model;

import java.sql.*;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import csc301.ultrasound.global.Constants;

/**
 * Create a two-dimensional table consisting of the record information of all
 * records in the database.
 */
public class RecordTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1L;
	
	/** The data to be displayed in the table. */
	private ArrayList<RecordTableModelEntry> data = null;
	
	/** The column names of the table. */
	private String[] columnNames = { "Record ID", "Patient ID", "Submission Time", "Comments", "Status" };
	
	private Connection connection = null;
	private User user = null;

	/**
	 * Instantiates a new record table model.
	 *
	 * @param user The current user.
	 * @param dbConnection An established connection to the database.
	 */
	public RecordTableModel(User user, Connection dbConnection)
	{
		if (dbConnection == null)
			return;
		
		this.connection = dbConnection;
		this.user = user;
		
		// Create a record table containing records that need attending to.
		data = new ArrayList<RecordTableModelEntry>();
		
		populateRecordTable();
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
			case 4:  return data.get(row).getStatus();
			default: return null;
		}
	}
	
	/**
	 * Populate the record table with new records.
	 */
	private void populateRecordTable()
	{
		try
		{
			Statement statement = connection.createStatement();
			
			String query = "select RID, PID, Date, FieldworkerComments, RadiologistResponse, FieldworkerSeen, RespondedBy "
					     + "from ultrasound.Records "
					     + "order by Date desc";
			
			ResultSet rs = statement.executeQuery(query);
			
			insertResultSet(rs);
		} 
		catch (SQLException se)
		{
			se.printStackTrace();
		}
	}

	/**
	 * Inserts the values from the supplied ResultSet into the table.
	 *
	 * @param rs The result set from a query.
	 */
	private void insertResultSet(ResultSet rs)
	{
		try
		{
			while (rs.next())
			{
				// Extract data from the result set.
				int       recordID    = rs.getInt("RID");
				int       patientID   = rs.getInt("PID");
				Timestamp date        = rs.getTimestamp("Date");
				String    comments    = rs.getString("FieldworkerComments");
				String    radResponse = rs.getString("RadiologistResponse");
				int       fwSeen      = rs.getInt("FieldworkerSeen");
				int       respondedBy = rs.getInt("RespondedBy");
				
				String status = null;
				
				if (radResponse != null)
				{
					if (fwSeen == 0)
						status = "Responded";
					else if (fwSeen == 1)
						status = "Received";
				}
				else
					status = "No response";
				
				if (user.getAuthlevel() > Constants.AUTHLEVEL_MANAGER)	// Radiologist or Fieldworker
				{
					// Display data that has not be responded to, or was responded to by the current user.
					if (radResponse == null || (radResponse != null && respondedBy == user.getID()))
						data.add(new RecordTableModelEntry(recordID, patientID, date, comments, status));
				}
				else
				{
					// Display all data
					data.add(new RecordTableModelEntry(recordID, patientID, date, comments, status));
				}
			}
		} 
		catch (SQLException se)
		{
			se.printStackTrace();
		}
	}
	
	/**
	 * Container class for each row of the table.
	 */
	class RecordTableModelEntry
	{
		private int       recordID  = -1;
		private int       patientID = -1;
		private Timestamp date      = null;
		private String    comments  = null;
		private String    status    = null;
		
		/**
		 * Instantiates a new record table model entry.
		 *
		 * @param recordID The record id
		 * @param patientID The patient id
		 * @param date The date
		 * @param comments The comments
		 * @param status The status
		 */
		RecordTableModelEntry(int recordID, int patientID, Timestamp date, String comments, String status)
		{
			this.recordID = recordID;
			this.patientID = patientID;
			this.date = date;
			this.comments = comments;
			this.status = status;
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
		 * @return the date
		 */
		public Timestamp getDate()
		{ 
			return date; 
		}
		
		/**
		 * Returns the comments.
		 *
		 * @return the comments
		 */
		public String getComments()
		{ 
			return comments; 
		}
		
		/**
		 * Returns the status.
		 *
		 * @return the status
		 */
		public String getStatus()
		{ 
			return status; 
		}
	}
}
