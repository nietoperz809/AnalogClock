import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;

///////////////////////////////////////////////////////////////////
///////////////////// C L O C K ///////////////////////////////////
class Clock extends JPanel
{
	// Image coords
	final int imgw=78, imgh=78;
	final Point center = new Point (imgw/2, imgh/2);

	BufferedImage offimg = new BufferedImage(imgw,imgh, BufferedImage.TYPE_INT_RGB);;
	Graphics offgraph = offimg.createGraphics();

	Image clockFace, m_imgdays;

	Clock (Image img, Image img2)
	{
		clockFace = img;
		m_imgdays = img2;

		offgraph.setColor(Color.GREEN);
		offgraph.fillRect(0,0, imgw, imgh);
	}

	/**
	 * Calculate end points of a line beginning at the center of a circle
	 * @param center center of circle
	 * @param rad length of line
	 * @param angle Angle
	 * @return endpoint
	 */
	private Point CirclePoint (Point center, int rad, int angle)
	{
		double ang = (double)(angle-90) / 180 * Math.PI;
		double x = (double)rad * Math.cos (ang);
		double y = (double)rad * Math.sin (ang);
		return new Point (center.x+(int)x, center.y+(int)y);
	}
	
	// Tick to drive the clock
	public synchronized void Tick()
	{
		GregorianCalendar cal = new GregorianCalendar();
		int sec = cal.get (Calendar.SECOND);
		int min = cal.get (Calendar.MINUTE);
		int hr = 5 * cal.get (Calendar.HOUR_OF_DAY) + min/12;
		int day = cal.get (Calendar.DAY_OF_WEEK)-1;

		// Face
		offgraph.drawImage (clockFace, 0,0, null);
		// Day
		CropImageFilter crop = new CropImageFilter (day*18, 0, 18, 8);
		FilteredImageSource fim = new FilteredImageSource (m_imgdays.getSource(), crop);
		Image imgthisday = createImage (fim);
		offgraph.drawImage (imgthisday, center.x+12, center.y-4, this);
		// Pointers
		DrawPointer (sec, 35, Color.YELLOW);
		DrawPointer (min, 34, Color.WHITE);
		DrawPointer (hr, 25, Color.WHITE);
		repaint();
	}

	/**
	 * Draw a pointer
	 * @param position angle
	 * @param len length length
	 * @param col color color
	 */
	private void DrawPointer (int position, int len, Color col)
	{
		Point pt = CirclePoint (center, len, position*6);
		Point pt2 = CirclePoint (center, 10, position*6+180);
		offgraph.setColor (col);
		offgraph.drawLine (pt2.x, pt2.y, pt.x, pt.y);
	}

	@Override
	public synchronized void update (Graphics g)
	{
		// Do nuthin'
	}

	@Override
	public synchronized void paint (Graphics g)
	{
		g.drawImage (offimg, 0,0, getWidth(), getHeight(),null);
	}	

	public Dimension getPreferredSize()
	{
		return new Dimension (300, 300);
	}
}
