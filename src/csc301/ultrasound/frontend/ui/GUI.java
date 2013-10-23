package csc301.ultrasound.frontend.ui;

import java.awt.EventQueue;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;

import com.jgoodies.forms.layout.*;
import javax.swing.GroupLayout.Alignment;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.table.DefaultTableModel;

import csc301.ultrasound.global.Transmission;
import csc301.ultrasound.model.*;

import java.sql.*;

public class GUI
{
	private JFrame frmUrmhClient;
	private JTable table;
	private static Login loginFrame;
	private static User usr;
	private static Transmission t;
	private static Connection connection;
	private int maxRecordRows = 20;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					login();
				} 
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Display the login window.
	 */
	public static void login()
	{
		loginFrame = new Login();
		
		loginFrame.addWindowListener(new WindowAdapter() 
		{
			public void windowClosed(WindowEvent e) 
			{

				usr = loginFrame.getUser();
				System.out.printf("%s has successfully logged in.\n", usr.getName());
				
				// Establish a connection with the database.
				System.out.println("Connecting to the database...");
				t = new Transmission();
				connection = t.connectToDB();
				
				if (connection != null) {
					System.out.println("The connection has been established.");
				} else {
					System.out.println("The connection has failed.");
				}
				runApplication();
			}
		});
	}
	
	/**
	 * Run the application.
	 */
	public static void runApplication()
	{
		GUI window = new GUI();
		window.frmUrmhClient.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	
	/**
	 * Create the application.
	 */
	public GUI()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frmUrmhClient = new JFrame();
		frmUrmhClient.setTitle("URMH Client");
		/*frmUrmhClient
				.setIconImage(Toolkit
						.getDefaultToolkit()
						.getImage(
								GUI.class
										.getResource("/img/41612_394588193886419_1849416814_q.jpg")));*/
		frmUrmhClient.setBounds(100, 100, 1093, 794);
		frmUrmhClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frmUrmhClient.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		// Handles when the user logs out.
		JMenuItem mntmLogOut = new JMenuItem("Log Out");
		mntmLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				t.disconnectFromDB(connection);
				System.out.println("The connection has been terminated.");
		        frmUrmhClient.dispose();
		        System.out.printf("%s has successfully logged out.\n", usr.getName());
				login();
			}
		});
		mnFile.add(mntmLogOut);

		// Handles when the user exits the application.
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				t.disconnectFromDB(connection);
				System.out.println("The connection has been terminated.");
		        frmUrmhClient.dispose();
		        System.out.printf("%s has terminated the application.\n", usr.getName());
		        System.exit(0);
			}
		});
		mnFile.add(mntmExit);

		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);

		JMenu mnTools = new JMenu("Tools");
		menuBar.add(mnTools);

		JMenu mnWindow = new JMenu("Window");
		menuBar.add(mnWindow);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		frmUrmhClient.getContentPane().setLayout(
				new MigLayout("", "[1057px,grow]", "[33px][grow]"));

		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		frmUrmhClient.getContentPane().add(toolBar, "cell 0 0,grow");

		JButton updateBtn = new JButton("Update");
		updateBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (connection != null) {

					// Create record table containing records that need attending to.
					String[] recordTableHeadings = { "Name", "Time", "Status" };
					Object[][] recordTable = new Object[maxRecordRows][3];
					int numRecordRows = fillRecordTable(recordTable);

					// Print the records.
					// NOTE: These records will be displayed using a Jtable in the future.
					System.out.println("---------------\nRecords:");
					int m; int n;
					for (n = 0; n < 3; n++){
						System.out.print(recordTableHeadings[n] + " - ");
					}
					
					System.out.println("");
					for (m = 0; m < numRecordRows; m++){
						for (n=0; n<3; n++){
							System.out.print(recordTable[m][n] + " - ");
						}
						System.out.println("");
					}
					System.out.println("---------------");
				}
			}
		});
		toolBar.add(updateBtn);

		JButton commitBtn = new JButton("Commit changes");
		toolBar.add(commitBtn);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.2);
		splitPane.setOneTouchExpandable(true);
		frmUrmhClient.getContentPane().add(splitPane, "cell 0 1,grow");

		JPanel panel = new JPanel();
		splitPane.setRightComponent(panel);

		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setResizeWeight(0.7);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(
				Alignment.LEADING).addComponent(splitPane_1,
				Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 1044,
				Short.MAX_VALUE));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(
				Alignment.LEADING).addComponent(splitPane_1,
				Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 668,
				Short.MAX_VALUE));

		JPanel panel_2 = new JPanel();
		splitPane_1.setRightComponent(panel_2);
		panel_2.setLayout(new MigLayout("", "[]", "[]"));

		JPanel panel_3 = new JPanel();
		splitPane_1.setLeftComponent(panel_3);
		panel_3.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"), }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"), }));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel_3.add(tabbedPane, "2, 2, fill, fill");

		JPanel viewPnl = new JPanel();
		tabbedPane.addTab("View image", null, viewPnl, null);

		JPanel annotatePnl = new JPanel();
		tabbedPane.addTab("Annotate image", null, annotatePnl, null);

		JPanel msgPnl = new JPanel();
		tabbedPane.addTab("Leave message", null, msgPnl, null);
		panel.setLayout(gl_panel);

		JPanel panel_1 = new JPanel();
		splitPane.setLeftComponent(panel_1);
		panel_1.setLayout(new FormLayout(
				new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"),
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] {
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("default:grow"), }));

		JLabel lblIncomingImages = new JLabel("Incoming images");
		panel_1.add(lblIncomingImages, "2, 2, left, fill");

		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] { { null, null },
				{ null, null }, }, new String[] { "New column", "New column" }));
		panel_1.add(table, "2, 4, 5, 1, fill, fill");
		
		frmUrmhClient.pack();
		frmUrmhClient.setVisible(true);
	}
	
	/**
	 * Fill the given record table with new records.
	 * Return the number of records added to the table.
	 */
	private int fillRecordTable(Object[][] recordTable)
	{
		// Keep track of how many records have been added to the table.
		int numRecords = 0;
		
		try {
			Statement statement = connection.createStatement();
			String query;
			ResultSet rs;
			
			// Query the database for records that have not yet been responded to.
			// Fill the record table with these records.
			query = "select firstName, lastName, time " 
					+ "from Record, Patient "
					+ "where (Record.pID = Patient.pID) "
					+ "and (Record.recID not in (select recID from Response))";
			rs = statement.executeQuery(query);
			numRecords = fillRecordTable(rs, recordTable, numRecords, "No Response");
			
			// Query the database for records that a different user has responded to,
			// but have not yet been approved.  Fill the record table with these records.
			query = "select firstName, lastName, time "
					+ "from Record, Patient, Response "
					+ "where (Record.pID = patient.pID) "
					+ "and (Record.recID = Response.recID) "
					+ "and (Record.recID not in (select recID from Approve))"
					+ "and (username not like '%" + usr.getName() + "%')";
			rs = statement.executeQuery(query);
			numRecords = fillRecordTable(rs, recordTable, numRecords, "Has Response");
			return numRecords;
		} catch (SQLException se) {
			return numRecords;
		}
	}
	
	/**
	 * Fill the given record table with the contents of the result set,
	 * starting at the row indicated by the given record number,
	 * setting the last value of each row in the table as the status string.
	 * Return the number of records the have been added to the table.
	 */
	private int fillRecordTable(ResultSet rs, Object[][] recordTable, int numRecords, String status)
	{
		try {

			while (rs.next() && (numRecords < recordTable.length)) {
			
				// Extract data from the result set.
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName");
				Timestamp time = rs.getTimestamp("time");
			
				// Record the data in the record table.
				recordTable[numRecords][0] = firstName + " " + lastName;
				recordTable[numRecords][1] = time;
				recordTable[numRecords][2] = status;
			
				numRecords++;
			}
			return numRecords;
		} catch (SQLException se)
        {
			System.out.println(status + " records have not been added.");
            return numRecords;
        }
	}
}
