package csc301.ultrasound.model;

import java.sql.*;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

/**
 * Create a two-dimensional table consisting of the patient history of the
 * patient with the given patientID. The patient history consists of the
 * last numRecords number of records of the patient.
 */
public class PatientHistoryTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1L;

	/** The data to be displayed in the table. */
	private static Object[][] data;
	
	/** The column names. */
	private Vector<String> columnNames = null;
	
	/**
	 * Instantiates a new patient history table model.
	 *
	 * @param value the value
	 * @param dbColumnToFilter the db column to filter
	 * @param niceName the nice name
	 * @param dbConnection the db connection
	 */
	public PatientHistoryTableModel(int value, String dbColumnToFilter, String niceName, Connection dbConnection)
	{
		columnNames = new Vector<String>();
		columnNames.add("Record ID");
		columnNames.add(niceName);
		columnNames.add("Submission Time");
		columnNames.add("Comments");
		columnNames.add("Response");
		
		// The maximum number of records that can be displayed
		// TODO: Remove restriction by using LinkedLists instead
		int maxPatientRecords = 100;

		// The number of patient records added to the table.
		int numPatientRecords = 0;

		// Create a new table consisting of the patient's information.
		Object[][] patientHistoryTable = new Object[maxPatientRecords][columnNames.size()];
		
		if (dbConnection != null)
		{
			try
			{
				Statement statement = dbConnection.createStatement();

				// Query the database for previous records of the patient.
				String query = "select RID, Date, FieldworkerComments, RadiologistResponse "
							 + "from ultrasound.Records "
							 + "where " + dbColumnToFilter + " = " + value + " "
							 + "order by Date desc";
				
				ResultSet rs = statement.executeQuery(query);

				while (rs.next() && (numPatientRecords < maxPatientRecords))
				{
					// Extract data from the result set.
					int recordID = rs.getInt("RID");
					Timestamp date = rs.getTimestamp("Date");
					String comments = rs.getString("FieldworkerComments");
					String response = rs.getString("RadiologistResponse");

					// Record the data in the record table.
					patientHistoryTable[numPatientRecords][0] = recordID;
					patientHistoryTable[numPatientRecords][1] = value;
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

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount()
	{
		return columnNames.size();
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
		return columnNames.elementAt(col);
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int col)
	{
		return data[row][col];
	}
}
