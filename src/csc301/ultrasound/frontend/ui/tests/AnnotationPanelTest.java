package csc301.ultrasound.frontend.ui.tests;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.awt.Robot;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.sql.Connection;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.junit.Test;

import csc301.ultrasound.frontend.ui.AnnotationPanel;
import csc301.ultrasound.frontend.ui.GUI;
import csc301.ultrasound.global.Transmission;

/**
 * Test for AnnotationPanel.
 */
public class AnnotationPanelTest
{
	/**
	 * Test the annotationPanel method.
	 */
	@Test
	public void testAnnotationPanel()
	{
		System.out.println("Running testAnnotationPanel()...");
		
		// Open a window wrapping the panel, draw in it, then check if the drawing matches the ground-truth.
		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		Dimension windowSize = new Dimension(640, 480);
		
		try
		{
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
			{
				if ("Nimbus".equals(info.getName()))
				{
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} 
		catch (Exception e)
		{
			java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
		}
		
		try
		{
			JFrame frame = new JFrame();
			frame.setSize(windowSize.width, windowSize.height);
			
			// center the window
			frame.setLocation((screenSize.width / 2) - (windowSize.width / 2),
							  (screenSize.height / 2) - (windowSize.height / 2));
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			Transmission t = new Transmission();
			Connection connection = t.connectToDB();
			
			AnnotationPanel ap = new AnnotationPanel(connection);
			
			// attach the panel
			frame.setContentPane(ap);
			
			frame.setResizable(false);
			frame.setBackground(Color.WHITE);
			//frame.pack();
			
			frame.setVisible(true);
			
			ap.setRID(15);
			
			Point p = new Point();
			
			// simulate some drawing
			Robot r = new Robot();
			r.delay(1500);
			
			r.mouseMove(screenSize.width / 2, screenSize.height / 2);

			r.mousePress(InputEvent.BUTTON1_MASK);
			
			for (float i = 0; i < Math.PI; i += 0.5f)
			{
				for (float j = 0; j < Math.PI; j += 0.5f)
				{
					p.x = ((int)(Math.sin(i) * 100) - 50) + (screenSize.width / 2);
					p.y = ((int)(Math.sin(j) * 100) - 50) + (screenSize.height / 2);
					
					r.mouseMove(p.x, p.y);
					
					r.delay(100);
				}
			}
			
			r.mouseRelease(InputEvent.BUTTON1_MASK);
			
			BufferedImage an = ap.getAnnotations();
			BufferedImage gtAn = ImageIO.read(this.getClass().getResource("/testimg/gtAnnotation.png"));
			
			// compare the hashes of both images to ensure that they are the same
			MessageDigest md = MessageDigest.getInstance("MD5");
			
			md.update(bufferedImageToByteArray(an, "png"));
			byte[] anHash = md.digest();
			String strHashAn = org.apache.commons.codec.binary.Hex.encodeHexString(anHash);
			
			md.update(bufferedImageToByteArray(gtAn, "png"));
			byte[] gtAnHash = md.digest();
			String strHashGtan = org.apache.commons.codec.binary.Hex.encodeHexString(gtAnHash);
			
			connection.close();
			
			org.junit.Assert.assertArrayEquals(strHashAn + " must equal " + strHashGtan, anHash, gtAnHash);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			org.junit.Assert.fail("Exception trying to test AnnotationPanel.");
		}
	}
	
	/**
	 * Convert a BufferedImage into a byte array.
	 *
	 * @param image The image to convert
	 * @param extension The extension of the image
	 * @return The converted byte array
	 */
	private static byte[] bufferedImageToByteArray(BufferedImage image, String extension)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try
		{
			ImageIO.write(image, extension, baos);
			baos.flush();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
			
			return null;
		}
		
		return baos.toByteArray();
	}
}
