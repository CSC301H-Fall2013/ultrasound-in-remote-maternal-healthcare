package csc301.ultrasound.frontend.ui;

import static org.junit.Assert.assertNotNull;

import java.awt.EventQueue;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;

import com.jgoodies.forms.layout.*;
import javax.swing.GroupLayout.Alignment;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.table.DefaultTableModel;

import csc301.ultrasound.global.Authentication;
import csc301.ultrasound.global.Transmission;
import csc301.ultrasound.model.*;

import java.sql.*;
import java.util.Date;

public class GUI
{
	private JFrame frmUrmhClient;
	private JTable table;
	private static Login loginFrame;
	private static User usr;
	
	// The maximum number of record rows to be displayed
	// on the interface at one time.
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

		JMenuItem mntmLogOut = new JMenuItem("Log Out");
		mntmLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        frmUrmhClient.dispose();
				login();
			}
		});
		mnFile.add(mntmLogOut);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmUrmhClient.dispose();
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
				// Establish a connection with the database.
				Transmission t = new Transmission();
				Connection connection = t.connectToDB();
				
				if (connection != null) {	
					
					// Create a record table containing records that need attending to.
					String[] recordTableHeadings = { "Record ID", "Patient ID", "Submission Time", "Comments", "Status" };
					Object[][] recordTable = new Object[maxRecordRows][recordTableHeadings.length];
					
					// Fill in the record table and keep track of how many entries were added.
					int numRecordRows = fillRecordTable(recordTable, connection);
					
					// Print the records.
					// NOTE: These records will be displayed using a Jtable in the future.
					System.out.println("---------------\nRecords:");

					int m; int n;
					for (n = 0; n < recordTableHeadings.length; n++) {
						System.out.print(recordTableHeadings[n] + " - ");
					}
					
					System.out.println("");
					for (m = 0; m < numRecordRows; m++) {
						for (n = 0; n < recordTableHeadings.length; n++) {
							System.out.print(recordTable[m][n] + " - ");
						}
					System.out.println("");
					}
					System.out.println("---------------");
					
					// Disconnect from the database.
					t.disconnectFromDB(connection);
					patientInfoTable(1);
					patientHistoryTable(1);
				} else {
					System.out.println("Connection failure.");
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
	private int fillRecordTable(Object[][] recordTable, Connection connection)
	{	
		// Keep track of how many records have been added to the table.
		int numRecords = 0;
		
		try 
		{
			Statement statement = connection.createStatement();
			String query;
			ResultSet rs;
	
			// Query the database for records that have not yet been responded to.
			// Fill the record table with these records.
			query = "select RID, PID, Date, FieldworkerComments " 
					+ "from ultrasound.Records "
					+ "where RadiologistResponse is null "
					+ "order by Date desc";
			rs = statement.executeQuery(query);
			numRecords = fillRecordTable(rs, recordTable, numRecords, "No Responses");
			
			// Query the database for records that a user has already responded to,
			// but the fieldworker has not yet received the response.
			// Fill the record table with these records.
			query = "select RID, PID, Date, FieldworkerComments " 
					+ "from ultrasound.Records "
					+ "where RadiologistResponse is not null "
					+ "and FieldworkerSeen = 0 "
					+ "order by Date desc";
			rs = statement.executeQuery(query);
			numRecords = fillRecordTable(rs, recordTable, numRecords, "Has Unseen Response");
			
			// Query the database for records that a user has already responded to,
			// and the fieldworker has received the response.
			// Fill the record table with these records.
			query = "select RID, PID, Date, FieldworkerComments " 
					+ "from ultrasound.Records "
					+ "where RadiologistResponse is not null "
					+ "and FieldworkerSeen = 1 "
					+ "order by Date desc";
			rs = statement.executeQuery(query);
			numRecords = fillRecordTable(rs, recordTable, numRecords, "Has Seen Response");
			
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
	 * Fill the given record table with the contents of the result set, 
	 * starting at the row indicated by the given record number, 
	 * setting the last value of each row in the table as the status string.
	 * Return the number of records that have been added to the table.
	 */
	private int fillRecordTable(ResultSet rs, Object[][] recordTable, int numRecords, String status)
	{
		try 
		{
			while (rs.next() && (numRecords < recordTable.length)) {
				
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
	
	/**
	 * Create a two-dimensional table consisting of the 
	 * patient information of the patient with the given patientID.
	 */
	private void patientInfoTable(int patientID) {
		
		// Create a new table consisting of the patient's information.
		String[] patientInfoTableHeadings = { "Patient ID", "Name", "Birthday", "Country" };
		Object[][] patientInfoTable = new Object[1][patientInfoTableHeadings.length];
		
		// Establish a connection with the database.
		Transmission t = new Transmission();
		Connection connection = t.connectToDB();
		
		if (connection != null) {
			try 
			{
				Statement statement = connection.createStatement();
				String query;
				ResultSet rs;

				// Query the database for information about the patient.
				query = "select FirstName, LastName, Birthdate, Country " 
						+ "from ultrasound.Patients "
						+ "where PID = "  + Integer.toString(patientID);
				rs = statement.executeQuery(query);

				while (rs.next()) {
					// Extract data from the result set.
					String firstName = rs.getString("FirstName");
					String lastName = rs.getString("LastName");
					Date birthday = rs.getDate("Birthdate");
					String country = rs.getString("Country");
				
					// Record the data in the record table.
					patientInfoTable[0][0] = patientID;
					patientInfoTable[0][1] = firstName + " " + lastName;
					patientInfoTable[0][2] = birthday;
					patientInfoTable[0][3] = country; 
				}
				// Print the records.
				// NOTE: These records will be displayed using a Jtable in the future.
				System.out.println("---------------\nPatient Info:");
	
				int m; int n;
				for (n = 0; n < patientInfoTableHeadings.length; n++) {
					System.out.print(patientInfoTableHeadings[n] + " - ");
				}
				
				System.out.println("");
				for (m = 0; m < 1; m++) {
					for (n = 0; n < patientInfoTableHeadings.length; n++) {
						System.out.print(patientInfoTable[m][n] + " - ");
					}
				System.out.println("");
				}
				System.out.println("---------------");
				
				// Disconnect from the database.
				t.disconnectFromDB(connection);
			}
			catch (SQLException se) 
			{
				System.err.println("SQL Exception.<Message>: " + se.getMessage());
			}
		} else {
			System.out.println("Connection failure.");
		}
	}
	
	/**
	 * Create a two-dimensional table consisting of the 
	 * patient history of the patient with the given patientID.
	 * The patient history consists of the last numRecords number
	 * of records of the patient.
	 */
	private void patientHistoryTable(int patientID) {
		
		// The maximum number of records that can be displayed
		int maxPatientRecords = 5;
		
		// The number of patient records added to the table.
		int numPatientRecords = 0;
		
		// Create a new table consisting of the patient's information.
		String[] patientHistoryTableHeadings = { "Record ID", "Patient ID", "Submission Time", "Comments", "Response" };
		Object[][] patientHistoryTable = new Object[maxPatientRecords][patientHistoryTableHeadings.length];
		
		// Establish a connection with the database.
		Transmission t = new Transmission();
		Connection connection = t.connectToDB();
		
		if (connection != null) {
			try 
			{
				Statement statement = connection.createStatement();
				String query;
				ResultSet rs;

				// Query the database for previous records of the patient.
				query = "select RID, Date, FieldworkerComments, RadiologistResponse "
						+ "from ultrasound.Records "
						+ "where PID = "  + Integer.toString(patientID) + " "
						+ "order by Date desc";
				rs = statement.executeQuery(query);

				while (rs.next() && (numPatientRecords < maxPatientRecords)) {
					
					// Extract data from the result set.
					int recordID = rs.getInt("RID");
					Timestamp date = rs.getTimestamp("Date");
					String comments = rs.getString("FieldworkerComments");
					String response = rs.getString("RadiologistResponse");
					
					// Record the data in the record table.
					patientHistoryTable[numPatientRecords][0] = recordID;
					patientHistoryTable[numPatientRecords][1] = patientID;
					patientHistoryTable[numPatientRecords][2] = date;
					patientHistoryTable[numPatientRecords][3] = comments;
					patientHistoryTable[numPatientRecords][4] = response;
					
					numPatientRecords++;
				}
				// Print the records.
				// NOTE: These records will be displayed using a Jtable in the future.
				System.out.println("---------------\nPatient History:");
	
				int m; int n;
				for (n = 0; n < patientHistoryTableHeadings.length; n++) {
					System.out.print(patientHistoryTableHeadings[n] + " - ");
				}
				
				System.out.println("");
				for (m = 0; m < numPatientRecords; m++) {
					for (n = 0; n < patientHistoryTableHeadings.length; n++) {
						System.out.print(patientHistoryTable[m][n] + " - ");
					}
				System.out.println("");
				}
				System.out.println("---------------");
				
				// Disconnect from the database.
				t.disconnectFromDB(connection);
			}
			catch (SQLException se) 
			{
				System.err.println("SQL Exception.<Message>: " + se.getMessage());
			}
		} else {
			System.out.println("Connection failure.");
		}
	}
}
