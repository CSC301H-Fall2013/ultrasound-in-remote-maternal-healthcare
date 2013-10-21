package csc301.ultrasound.frontend.ui;

import java.awt.EventQueue;
import java.awt.Frame;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.event.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.jgoodies.forms.layout.*;
import javax.swing.GroupLayout.Alignment;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.table.DefaultTableModel;
import csc301.ultrasound.model.*;

public class GUI
{
	private JFrame frmUrmhClient;
	private JTable table;
	private static Login loginFrame;
	private static User	user;

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
					GUI window = new GUI();
					window.frmUrmhClient.setVisible(true);
					window.frmUrmhClient
							.setExtendedState(JFrame.MAXIMIZED_BOTH);
					
					final ExecutorService service;
			        final Future<User> task;

			        service = Executors.newFixedThreadPool(1); 
			        Login login = new Login();
			        login.setVisible(true);
			        task  = service.submit(login);

			        try 
			        {

			            wait(60000);
			            user = task.get();
			            System.out.printf("%s %s %d", user.getName(), user.getCredential(), user.getName());
			        }
			        catch(final InterruptedException ex)
			        {
			            ex.printStackTrace();
			        }
			        catch(final ExecutionException ex)
			        {
			            ex.printStackTrace();
			        }

			        service.shutdownNow();
					
//				      Thread t = new Thread(new Runnable()
//						{
//							public void run()
//							{
//								try
//								{
//									Login login = new Login();
//									login.setVisible(true);
//								}catch (Exception e)
//								{
//									e.printStackTrace();
//								}}});
//				      // this will call run() function
//				      t.start();
//				      t.
//				      t.join();

//					do{
//						continue;
//					}while(loginFrame.getUser()==null);
//					user = loginFrame.getUser();
//					synchronized (window) {
//						
//					    window.wait();
//						window.notify();
//						user = loginFrame.getUser();
//						System.out.printf("%s,  %s, %s", user.getName(), user.getType(), user.getCredential());
//					} 
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
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
		frmUrmhClient.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				System.out.println("noob");
			}
		});
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
		mnFile.add(mntmLogOut);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        System.exit(0);
		        frmUrmhClient.dispose();
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
			public void actionPerformed(ActionEvent arg0)
			{
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
	}
}
