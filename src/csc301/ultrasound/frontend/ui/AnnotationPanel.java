package csc301.ultrasound.frontend.ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;

import csc301.ultrasound.frontend.ui.Util;

/**
 * A panel used to annotate an image.
 */
public class AnnotationPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	/** The previously drawn color. */
	private Color prevColor = Color.RED;
	
	/** The previous mouse location. */
	private Point2D prevLoc;
	
	/** The current mouse location. */
	private Point2D currLoc;
	
	/** The graphics context of the annotation image. */
	private Graphics2D g2dAnnotation = null;
	
	/** The ultrasound image. */
	private Image image = null;
	
	/** The annotation image. */
	private BufferedImage annotationImage = null;
	
	/** The size of the color button */
	private static final int colorButtonSize = 20;

	private int RID = -1;
	
	/**
	 * Instantiates a new annotation panel.
	 */
	public AnnotationPanel()
	{
		initUI();
	}
	
	private void initUI()
	{
		this.setLayout(new BorderLayout(0, 0));
		this.setBackground(Color.BLACK);
		
		// create the panel used for choosing a color
		JPanel colorPanel = new JPanel();
		colorPanel.setBackground(Color.BLACK);
		colorPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 2, 2));
		
		JLabel l = new JLabel("Color: ");
		l.setForeground(Color.WHITE);
		colorPanel.add(l);
		
		final JButton color = new JButton("");
		color.setBackground(Color.RED);
		color.setPreferredSize(new Dimension(colorButtonSize, colorButtonSize));
		
		// add the mouse listener to listen for clicks on the color button
		color.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				JColorChooser cc = new JColorChooser();
				
				// show only the HSB panel. Solution found here:
				// http://stackoverflow.com/questions/9079807/jcolorchooser-hide-all-default-panels-and-show-hsb-panel-only
				AbstractColorChooserPanel[] panels = cc.getChooserPanels();
				
                for (AbstractColorChooserPanel accp : panels) 
                {
                    if (accp.getDisplayName().equals("HSB"))
                        JOptionPane.showMessageDialog(null, accp);
                }
                
                Color selectedColor = cc.getColor();
                
                g2dAnnotation.setColor(selectedColor);
				color.setBackground(selectedColor);
				prevColor = selectedColor;
			}
		});
		
		colorPanel.add(color);
		
		this.add(colorPanel, BorderLayout.NORTH);
		
		this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		
		// add the mouse listener for the pen tool
		MouseListener ml = new MouseListener();
		this.addMouseListener(ml);
		this.addMouseMotionListener(ml);
	}
	
	/**
	 * The listener interface for receiving mouse events.
	 * The class that is interested in processing a mouse
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addMouseListener<code> method. When
	 * the mouse event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see MouseEvent
	 */
	class MouseListener extends MouseAdapter
	{
		/* (non-Javadoc)
		 * @see java.awt.event.MouseAdapter#mouseDragged(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseDragged(MouseEvent arg0) 
		{
			if (RID == -1)
				return;
			
			currLoc = arg0.getPoint();
			
			updateCanvas();
			
			prevLoc = currLoc;
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
		 */
		@Override
		public void mousePressed(MouseEvent arg0) 
		{
			if (RID == -1)
				return;
			
			currLoc = arg0.getPoint();
			prevLoc = currLoc;
		}
	}
	
	/**
	 * Update the canvas.
	 */
	private void updateCanvas()
	{
		g2dAnnotation.setPaint(prevColor);
		g2dAnnotation.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
        g2dAnnotation.drawLine((int)prevLoc.getX(), (int)prevLoc.getY() - colorButtonSize, 
        		               (int)currLoc.getX(), (int)currLoc.getY() - colorButtonSize);
        
        repaint();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) 
	{
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        
        float scaleFactor = (float)Math.min(1.0f, Util.getScaleFactorToFit(new Dimension(image.getWidth(null), image.getHeight(null)), 
        															       new Dimension(this.getWidth(),      this.getHeight() - colorButtonSize)));
        
        // determine the parameters to fit the image into the given space, preserving the aspect ratio.
        int scaleWidth  = (int)Math.round(image.getWidth(null)  * scaleFactor);
        int scaleHeight = (int)Math.round(image.getHeight(null) * scaleFactor);

        int x = ((this.getWidth() - 1)  - scaleWidth)  / 2;
        int y = ((this.getHeight() - 1) - scaleHeight) / 2;
     
        // draw both images on top of one another. Since annotationImage is RGBA, it will overlay.
        g2d.drawImage(image,           x, y + colorButtonSize, scaleWidth, scaleHeight, null);
        g2d.drawImage(annotationImage, x, y + colorButtonSize, scaleWidth, scaleHeight, null);
	}
	
	public void update(int newRID, BufferedImage newImage)
	{
		RID = newRID;
		
		image = newImage;
		
		// create an image to draw into
		annotationImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		g2dAnnotation = annotationImage.createGraphics();
        g2dAnnotation.setColor(prevColor);
        
        g2dAnnotation.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2dAnnotation.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		this.repaint();
	}
	
	/**
	 * Get the annotation image.
	 *
	 * @return The annotation image.
	 */
	public BufferedImage getAnnotations()
	{
		return annotationImage;
	}
}
