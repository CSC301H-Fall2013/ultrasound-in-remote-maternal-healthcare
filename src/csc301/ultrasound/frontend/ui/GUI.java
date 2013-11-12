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

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;


import csc301.ultrasound.global.Authentication;
import csc301.ultrasound.global.Transmission;
import csc301.ultrasound.model.*;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;

public class GUI
{
	private JFrame frmUrmhClient;
	private static Login loginFrame;
	private static User usr;
	
	// The maximum number of record rows to be displayed
	// on the interface at one time.
	private int maxRecordRows = 20;
	private JTable mainTable;
	private JTable infoTable;
	private JTable histTable;
	
	
	//non-swing fields
	private int currPatientId;


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
//					login()
					runApplication();
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
				updateMainTable();
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

		JSplitPane subSplitPlane = new JSplitPane();
		subSplitPlane.setResizeWeight(0.65);
		subSplitPlane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(
				Alignment.LEADING).addComponent(subSplitPlane,
				Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 1044,
				Short.MAX_VALUE));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(
				Alignment.LEADING).addComponent(subSplitPlane,
				Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 668,
				Short.MAX_VALUE));

		JPanel panel_2 = new JPanel();
		subSplitPlane.setRightComponent(panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{0, 0};
		gbl_panel_2.rowHeights = new int[]{0, 0};
		gbl_panel_2.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		panel_2.add(tabbedPane, gbc_tabbedPane);
		
		JPanel patientInfo = new JPanel();
		tabbedPane.addTab("Patient Info", null, patientInfo, null);
		GridBagLayout gbl_patientInfo = new GridBagLayout();
		gbl_patientInfo.columnWidths = new int[]{0, 0};
		gbl_patientInfo.rowHeights = new int[]{0, 0};
		gbl_patientInfo.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_patientInfo.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		patientInfo.setLayout(gbl_patientInfo);
		
		JScrollPane infoPane = new JScrollPane();
		GridBagConstraints gbc_infoPane = new GridBagConstraints();
		gbc_infoPane.fill = GridBagConstraints.BOTH;
		gbc_infoPane.gridx = 0;
		gbc_infoPane.gridy = 0;
		patientInfo.add(infoPane, gbc_infoPane);
		
		infoTable = new JTable();
		infoTable.setFillsViewportHeight(true);
		infoPane.setViewportView(infoTable);
		
		JPanel patientHist = new JPanel();
		tabbedPane.addTab("Patient History", null, patientHist, null);
		GridBagLayout gbl_patientHist = new GridBagLayout();
		gbl_patientHist.columnWidths = new int[]{0, 0};
		gbl_patientHist.rowHeights = new int[]{0, 0};
		gbl_patientHist.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_patientHist.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		patientHist.setLayout(gbl_patientHist);
		
		JScrollPane histPane = new JScrollPane();
		GridBagConstraints gbc_histPane = new GridBagConstraints();
		gbc_histPane.fill = GridBagConstraints.BOTH;
		gbc_histPane.gridx = 0;
		gbc_histPane.gridy = 0;
		patientHist.add(histPane, gbc_histPane);
		
		histTable = new JTable();
		histTable.setFillsViewportHeight(true);
		histPane.setViewportView(histTable);

		JPanel tabPanel1 = new JPanel();
		subSplitPlane.setLeftComponent(tabPanel1);
		tabPanel1.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"), }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"), }));
		Dimension minimumSize = new Dimension(100, 50);
		tabPanel1.setMinimumSize(new Dimension(100, 400));

		JTabbedPane tabbedPane1 = new JTabbedPane(JTabbedPane.TOP);
		tabPanel1.add(tabbedPane1, "2, 2, fill, fill");

		JPanel viewPnl = new JPanel();
		tabbedPane1.addTab("View image", null, viewPnl, null);

		JPanel annotatePnl = new JPanel();
		tabbedPane1.addTab("Annotate image", null, annotatePnl, null);

		JPanel msgPnl = new JPanel();
		tabbedPane1.addTab("Leave message", null, msgPnl, null);
		panel.setLayout(gl_panel);

		JPanel panel_1 = new JPanel();
		splitPane.setLeftComponent(panel_1);
		panel_1.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));

		JLabel lblIncomingImages = new JLabel("Incoming images");
		panel_1.add(lblIncomingImages, "2, 2, left, fill");
		
		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane, "2, 4, 5, 1, fill, fill");
		
		mainTable = new JTable();
		mainTable.setFillsViewportHeight(true);
		mainTable.setRowSelectionAllowed(true);
		mainTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		mainTable.getSelectionModel().addListSelectionListener(new MainListSelectionListener());
		mainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(mainTable);
		
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
	 * Create a two-dimensional table consisting of records,
	 * which is used to add to the table model of 
	 * of records of the patient.
	 */	
	public class ListModel extends AbstractTableModel {
	ListModel(){
		
		// Establish a connection with the database.
		Transmission t = new Transmission();
		Connection connection = t.connectToDB();
		
		if (connection != null) {	
			
			// Create a record table containing records that need attending to.
			Object[][] recordTable = new Object[maxRecordRows][5];
			
			// Fill in the record table and keep track of how many entries were added.
			int numRecordRows = fillRecordTable(recordTable, connection);
			
			// Set dataset
			data = recordTable;

			// Disconnect from the database.
			t.disconnectFromDB(connection);
		} else {
			System.out.println("Connection failure.");
		}
		
    }
    
    
    private Object[][] data;
    private String[] columnNames = { "Record ID", "Patient ID", "Submission Time", "Comments", "Status" };
    
    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    	
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }
}
	
	/**
	 * Create a two-dimensional table consisting of the 
	 * patient history of the patient with the given patientID.
	 * The patient history consists of the last numRecords number
	 * of records of the patient.
	 */	
	public class PHModel extends AbstractTableModel {
		PHModel(){
			// The maximum number of records that can be displayed
			int maxPatientRecords = 5;
			
			// The number of patient records added to the table.
			int numPatientRecords = 0;
			
			// Create a new table consisting of the patient's information.
			Object[][] patientHistoryTable;
			patientHistoryTable = new Object[maxPatientRecords][5];
			
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
							+ "where PID = "  + Integer.toString(currPatientId) + " "
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
						patientHistoryTable[numPatientRecords][1] = currPatientId;
						patientHistoryTable[numPatientRecords][2] = date;
						patientHistoryTable[numPatientRecords][3] = comments;
						patientHistoryTable[numPatientRecords][4] = response;
						
						numPatientRecords++;
					}

					
					data = patientHistoryTable;
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
	    
	    
	    private Object[][] data;
	    private String[] columnNames = { "Record ID", "Patient ID", "Submission Time", "Comments", "Response" };
	    
	    public int getColumnCount() {
	        return columnNames.length;
	    }

	    public int getRowCount() {
	        return data.length;
	    }

	    public String getColumnName(int col) {
	        return columnNames[col];
	    }

	    public Object getValueAt(int row, int col) {
	        return data[row][col];
	    }
	}

	/**
	 * Create a two-dimensional table consisting of the 
	 * patient information of the patient with the given patientID.
	 */
	public class PIModel extends AbstractTableModel {
		PIModel(){
			// Create a new table consisting of the patient's information.
			Object[][] patientInfoTable = new Object[1][4];
			
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
							+ "where PID = "  + Integer.toString(currPatientId);
					rs = statement.executeQuery(query);
	
					while (rs.next()) {
						// Extract data from the result set.
						String firstName = rs.getString("FirstName");
						String lastName = rs.getString("LastName");
						Date birthday = rs.getDate("Birthdate");
						String country = rs.getString("Country");
					
						// Record the data in the record table.
						patientInfoTable[0][0] = currPatientId;
						patientInfoTable[0][1] = firstName + " " + lastName;
						patientInfoTable[0][2] = birthday;
						patientInfoTable[0][3] = country; 
					}
					data = patientInfoTable;
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
    
    private Object[][] data;
    private String[] columnNames = { "Patient ID", "Name", "Birthday", "Country" };
    
    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }
}

private void updateMainTable(){
	mainTable.setModel(new ListModel());
 }

private class MainListSelectionListener implements ListSelectionListener {
    public void valueChanged(ListSelectionEvent event) {
        if (event.getValueIsAdjusting()) {
            return;
        }
        int[] listSelection = mainTable.getSelectedRows();
        currPatientId = (int) mainTable.getModel().getValueAt(listSelection[0], 1);
        histTable.setModel(new PHModel());
        infoTable.setModel(new PIModel());
    }
} 
	
	
}
