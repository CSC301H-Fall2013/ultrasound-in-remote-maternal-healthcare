package csc301.ultrasound.model;

import java.sql.*;

import javax.swing.table.AbstractTableModel;

import csc301.ultrasound.global.Util;

/**
 * Create a two-dimensional table consisting of the information pretaining to a single record.
 */
public class RecordDetailsTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1L;
	
	/** The data to be displayed in the table. */
	private Object[][] data = null;
	
	/** The column names of the table. */
	private String[] columnNames = { "Prebirth", "Gestation", "Is bleeding", "Fetal Head Diam.", "Mothers Hip Diam." };
	
	/**
	 * Instantiates a new record table model.
	 *
	 * @param RID The RID to display.
	 * @param connection An established connection to the database.
	 */
	public RecordDetailsTableModel(int RID, Connection connection)
	{
		if (connection == null)
			return;
		
		// Create a new table consisting of the patient's information.
		Object[][] recordDetailsTable = new Object[1][columnNames.length];
		
		try
		{
			Statement statement = connection.createStatement();
			
			String query = "select prebirth, gestation, isBleeding, diameterFetalHead, diameterMotherHip "
					     + "from ultrasound.Records "
					     + "where RID = " + RID;
			
			ResultSet rs = statement.executeQuery(query);
			
			while (rs.next())
			{
				boolean prebirth          = Util.intToBool(rs.getInt("prebirth"));
				int     gestation         = rs.getInt("gestation");
				boolean isBleeding        = Util.intToBool(rs.getInt("isBleeding"));
				float   diameterFetalHead = rs.getFloat("diameterFetalHead");
				float   diameterMotherHip = rs.getFloat("diameterMotherHip");
			
				// Display all data
				recordDetailsTable[0][0] = prebirth;
				recordDetailsTable[0][1] = gestation;
				recordDetailsTable[0][2] = isBleeding;
				recordDetailsTable[0][3] = diameterFetalHead;
				recordDetailsTable[0][4] = diameterMotherHip;
			}
			
			data = recordDetailsTable;
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
