package csc301.ultrasound.frontend.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import net.miginfocom.swing.MigLayout;

import java.sql.Connection;

import csc301.ultrasound.model.UserInformationTableModel;
import csc301.ultrasound.global.Constants;

/**
 * Creates a window for Managers and above to see the contact details of users
 * currently in the system.
 */
public class UserPanel extends JFrame 
{
	private static final long serialVersionUID = 1L;
	
	private Connection dbConnection = null;

	private JTable aTable  = null;		// admin
	private JTable suTable = null;		// super user
	private JTable mTable  = null;		// manager
	private JTable rTable  = null;		// radiolgist
	private JTable fwTable = null;		// fieldworker

	/**
	 * Create the window.
	 *
	 * @param dbConnection An established connection to the database.
	 */
	public UserPanel(Connection dbConnection) 
	{
		if (dbConnection == null)
			return;
		
		this.dbConnection = dbConnection;
		
		initUI();
		
		updateTables();
		
		Util.centerWindow(this);
	}
	
	/**
	 * Creates the UI.
	 */
	public void initUI()
	{
		setTitle("Users");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 480);
		
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow]", "[][grow]"));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, "cell 0 1,grow");

		aTable  = createTable("Administrators", tabbedPane);
		suTable = createTable("Super Users",    tabbedPane);
		mTable  = createTable("Managers",       tabbedPane);
		rTable  = createTable("Radiologists",   tabbedPane);
		fwTable = createTable("Fieldworkers",   tabbedPane);
	}
	
	/**
	 * Creates a table to contain a certain authlevel and inserts it 
	 * into the supplied tabbed pane.
	 *
	 * @param tabName The tab name.
	 * @param tabbedPane The tabbed pane to add this to.
	 * @return The newly created table.
	 */
	private JTable createTable(String tabName, JTabbedPane tabbedPane)
	{
		JTable table = new JTable();
		JPanel panel = new JPanel();
		
		tabbedPane.addTab(tabName, null, panel, null);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0};
		gbl_panel.rowHeights = new int[]{0, 0};
		gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		table.setFillsViewportHeight(true);
		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.gridx = 0;
		gbc_table.gridy = 0;
		panel.add(new JScrollPane(table), gbc_table);
		
		return table;
	}
	
	/**
	 * Update the tables.
	 */
	private void updateTables()
	{
		if (dbConnection != null)
		{
			UserInformationTableModel aModel  = new UserInformationTableModel(Constants.AUTHLEVEL_ADMIN,       dbConnection);		// admin
			UserInformationTableModel suModel = new UserInformationTableModel(Constants.AUTHLEVEL_SUPERUSER,   dbConnection);		// superuser
			UserInformationTableModel mModel  = new UserInformationTableModel(Constants.AUTHLEVEL_MANAGER,     dbConnection);		// manager
			UserInformationTableModel rModel  = new UserInformationTableModel(Constants.AUTHLEVEL_RADIOLOGIST, dbConnection);		// radiologist
			UserInformationTableModel fwModel = new UserInformationTableModel(Constants.AUTHLEVEL_FIELDWORKER, dbConnection);		// fieldworker
			
			aTable.setModel(aModel);
			suTable.setModel(suModel);
			mTable.setModel(mModel);
			rTable.setModel(rModel);
			fwTable.setModel(fwModel);
		}
	}
}
