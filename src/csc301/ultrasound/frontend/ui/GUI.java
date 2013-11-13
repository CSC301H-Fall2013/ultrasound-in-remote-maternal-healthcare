package csc301.ultrasound.frontend.ui;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;

import com.jgoodies.forms.layout.*;
import javax.swing.GroupLayout.Alignment;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import csc301.ultrasound.model.*;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class GUI extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private User user = null;
	private Connection dbConnection = null;

	// The maximum number of record rows to be displayed
	// on the interface at one time.
	private JTable mainTable = null;
	private JTable infoTable = null;
	private JTable histTable = null;
	
	private ImagePanel viewPnl = null;

	/**
	 * Create the application.
	 */
	public GUI(User user, Connection dbConnection)
	{
		this.user = user;
		this.dbConnection = dbConnection;
		
		buildUI();
		
		updateMainTable();
		
		this.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void buildUI()
	{
		this.setTitle("URMH Client");
		
		/*
		 * frmUrmhClient .setIconImage(Toolkit .getDefaultToolkit() .getImage(
		 * GUI.class
		 * .getResource("/img/41612_394588193886419_1849416814_q.jpg")));
		 */
		
		this.setBounds(100, 100, 1093, 794);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmLogOut = new JMenuItem("Log Out");
		mntmLogOut.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				quit();
			}
		});
		mnFile.add(mntmLogOut);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				quit();
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
		this.getContentPane().setLayout(
				new MigLayout("", "[1057px,grow]", "[33px][grow]"));

		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		this.getContentPane().add(toolBar, "cell 0 0,grow");

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
		this.getContentPane().add(splitPane, "cell 0 1,grow");

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
		gbl_panel_2.columnWidths = new int[] { 0, 0 };
		gbl_panel_2.rowHeights = new int[] { 0, 0 };
		gbl_panel_2.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panel_2.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
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
		gbl_patientInfo.columnWidths = new int[] { 0, 0 };
		gbl_patientInfo.rowHeights = new int[] { 0, 0 };
		gbl_patientInfo.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_patientInfo.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
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
		gbl_patientHist.columnWidths = new int[] { 0, 0 };
		gbl_patientHist.rowHeights = new int[] { 0, 0 };
		gbl_patientHist.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_patientHist.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
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
		
		tabPanel1.setMinimumSize(new Dimension(100, 400));

		JTabbedPane tabbedPane1 = new JTabbedPane(JTabbedPane.TOP);
		tabPanel1.add(tabbedPane1, "2, 2, fill, fill");
		
		viewPnl = new ImagePanel(dbConnection);
		tabbedPane1.addTab("View image", null, viewPnl, null);

		JPanel annotatePnl = new JPanel();
		tabbedPane1.addTab("Annotate image", null, annotatePnl, null);

		JPanel msgPnl = new JPanel();
		tabbedPane1.addTab("Leave message", null, msgPnl, null);
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

		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane, "2, 4, 5, 1, fill, fill");

		mainTable = new JTable();
		mainTable.setFillsViewportHeight(true);
		mainTable.setRowSelectionAllowed(true);
		mainTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		mainTable.getSelectionModel().addListSelectionListener(
				new MainListSelectionListener());
		mainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(mainTable);

		this.pack();
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	
	private void quit()
	{
		this.dispose();
		
		try
		{
			dbConnection.close();
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
		
		System.exit(0);
	}

	private void updateMainTable()
	{
		if (dbConnection != null)
			mainTable.setModel(new RecordList(dbConnection));
	}

	private class MainListSelectionListener implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent event)
		{
			if (event.getValueIsAdjusting())
				return;
			
			int[] listSelection = mainTable.getSelectedRows();
			
			int currPatientId = Integer.parseInt(mainTable.getModel().getValueAt(listSelection[0], 1).toString());
			int currRID = Integer.parseInt(mainTable.getModel().getValueAt(listSelection[0], 0).toString());
			
			histTable.setModel(new PatientHistory(currPatientId, dbConnection));
			infoTable.setModel(new PatientInformation(currPatientId, dbConnection));
			
			viewPnl.setRID(currRID);
		}
	}
}
