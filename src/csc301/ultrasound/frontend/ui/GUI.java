package csc301.ultrasound.frontend.ui;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.SQLException;

import com.jgoodies.forms.layout.*;
import javax.swing.GroupLayout.Alignment;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import csc301.ultrasound.global.ImageDownloader;
import csc301.ultrasound.model.*;

public class GUI extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private User user = null;
	private Connection dbConnection = null;
	
	private JTable mainTable = null;
	private JTable infoTable = null;
	private JTable histTable = null;
	
	private ImagePanel imagePanel = null;
	private ResponsePanel responsePanel = null;
	private MessagePanel messagePanel = null;

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
				dispose();			// destroy the current session
				Frontend.login();	// create a new session
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
		
		this.getContentPane().setLayout(new MigLayout("", "[1057px,grow]", "[33px][grow]"));

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
		
		JButton btnManagerPanel = new JButton("Manager Panel");
		btnManagerPanel.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				UserPanel managerPanel = new UserPanel(dbConnection);
				managerPanel.setVisible(true);
			}
		});
		
		// Deactivate button if the authlevel of the user is not high enough.
		if (user.getAuthlevel() > 3)
			btnManagerPanel.setEnabled(false);
		
		toolBar.add(btnManagerPanel);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.2);
		splitPane.setOneTouchExpandable(true);
		splitPane.setContinuousLayout(true);
		this.getContentPane().add(splitPane, "cell 0 1,grow");

		JPanel editPanel = new JPanel();
		splitPane.setRightComponent(editPanel);

		JSplitPane subSplitPlane = new JSplitPane();
		subSplitPlane.setContinuousLayout(true);
		subSplitPlane.setResizeWeight(0.5);
		subSplitPlane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		GroupLayout gl_panel = new GroupLayout(editPanel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(subSplitPlane, GroupLayout.DEFAULT_SIZE, 662, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(subSplitPlane, GroupLayout.DEFAULT_SIZE, 668, Short.MAX_VALUE))
		);
		editPanel.setLayout(gl_panel);

		// info section tabs
		JTabbedPane infoTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		subSplitPlane.setBottomComponent(infoTabbedPane);

		JScrollPane infoPane = new JScrollPane();
		infoTabbedPane.addTab("Patient Info", null, infoPane, null);

		infoTable = new JTable();
		infoTable.setFillsViewportHeight(true);
		infoPane.setViewportView(infoTable);

		JScrollPane histPane = new JScrollPane();
		infoTabbedPane.addTab("Patient History", null, histPane, null);

		histTable = new JTable();
		histTable.setFillsViewportHeight(true);
		histPane.setViewportView(histTable);
		
		// image section tabs
		JTabbedPane imageTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		subSplitPlane.setTopComponent(imageTabbedPane);

		imagePanel    = new ImagePanel();
		responsePanel = new ResponsePanel(user, dbConnection);
		messagePanel  = new MessagePanel(user,dbConnection);
		
		imageTabbedPane.addTab("Review",  null, imagePanel,    null);
		imageTabbedPane.addTab("Respond", null, responsePanel, null);
		imageTabbedPane.addTab("Comment", null, messagePanel,  null);

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
		mainTable.getSelectionModel().addListSelectionListener(new MainListSelectionListener());
		mainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(mainTable);
		
		this.pack();
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	
	/**
	 * Quit the application.
	 */
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

	/**
	 * Update main table with new records.
	 */
	private void updateMainTable()
	{
		if (dbConnection != null)
		{
			mainTable.setModel(new RecordTableModel(dbConnection));
			mainTable.setAutoCreateRowSorter(true);
			mainTable.getRowSorter().toggleSortOrder(4);
		}
	}

	private class MainListSelectionListener implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent event)
		{
			if (event.getValueIsAdjusting())
				return;
			
			int listSelection[] = mainTable.getSelectedRows();
			
			if (listSelection.length > 0)
			{
				int currPatientId = Integer.parseInt(mainTable.getValueAt(listSelection[0], 1).toString());
				int currRID = Integer.parseInt(mainTable.getValueAt(listSelection[0], 0).toString());
				
				histTable.setModel(new PatientHistoryTableModel(currPatientId, dbConnection));
				infoTable.setModel(new PatientInformationTableModel(currPatientId, dbConnection));
				
				BufferedImage newImage = new ImageDownloader(dbConnection).downloadUltrasound(currRID);
				
				imagePanel.update(currRID, newImage);
				responsePanel.update(currRID, newImage);
				messagePanel.update(currRID);
			}
		}
	}
}
