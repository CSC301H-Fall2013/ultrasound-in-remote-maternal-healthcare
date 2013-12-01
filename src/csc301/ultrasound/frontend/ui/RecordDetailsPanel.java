package csc301.ultrasound.frontend.ui;

import java.awt.GridBagConstraints;
import java.sql.Connection;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import csc301.ultrasound.model.RecordDetailsTableModel;

public class RecordDetailsPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private int RID = -1;
	private Connection connection = null;
	
	private JTable detailTable = null;
	
	public RecordDetailsPanel(int RID, Connection connection)
	{
		if (connection == null)
			return;
		
		this.RID = RID;
		this.connection = connection;
		
		detailTable = new JTable();
		
		initUI();
	}
	
	private void initUI()
	{
		detailTable.setModel(new RecordDetailsTableModel(RID, connection));
		detailTable.setFillsViewportHeight(true);
		
		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.gridx = 0;
		gbc_table.gridy = 0;
		this.add(new JScrollPane(detailTable), gbc_table);
	}
	
	/**
	 * Update the panel with a new RID.
	 *
	 * @param newRID the new RID.
	 */
	public void update(int newRID)
	{
		RID = newRID;

		detailTable.setModel(new RecordDetailsTableModel(RID, connection));
	}
}
