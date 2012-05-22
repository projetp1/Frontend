/**=====================================================================*
| This file declares the following classes:
|    SkyMap
|
| Description of the class SkyMap :
|	  This class is used to display the stars, and the arrow direction.
|	  It considers the zoom and screen size for the scale.
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre Perez
| @version : 1.0
|
|
 *========================================================================*/

package com.github.projetp1;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Calendar;

import com.sun.org.apache.bcel.internal.generic.LXOR;

@SuppressWarnings("serial")
public class SkyMap extends Container implements MouseListener
{
	private int zoom = 1;
	private double dXOrigin = 0;
	private double dYOrigin = 0;
	private ArrayList<CelestialObject> celestialObjects;
	MainView mainView = null;

	/**
	 * SkyMap Constructor
	 * 
	 * @param _sDataBase
	 *            : The name of the database that will use
	 * @param _sDelimiter
	 *            : The delimiter of the string of the searchbar
	 */
	public SkyMap(String _sDataBase, String _sDelimiter, MainView _mainView)
	{
		mainView = _mainView;

		this.addMouseListener(this);
		this.setBackground(Color.BLACK);
		// this.setOpaque(true);
		try
		{
			DataBase db = new DataBase(_sDataBase, _sDelimiter);
			celestialObjects = db.starsForCoordinates(Calendar.getInstance(), 47.039448, 6.799734);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void mousePressed(MouseEvent e)
	{

		int l_scale = (int) (this.getHeight() / 2);
		int l_xCenter = this.getWidth() / 2 - (int) (dXOrigin * l_scale * zoom);
		int l_yCenter = this.getHeight() / 2 + (int) (dYOrigin * l_scale * zoom);
		double l_dLastDelta = 10;
		double l_dXPressed = (double) (e.getX() - l_xCenter) / zoom / l_scale;
		double l_dYPressed = (double) (e.getY() - l_yCenter) / zoom / l_scale * -1;
		CelestialObject l_nearestCelestialObject = null;

		for (CelestialObject celestialObject : celestialObjects)
		{
			double l_dDelta = Math.sqrt(Math.pow(
					Math.abs(l_dXPressed - celestialObject.getXReal()), 2)
					+ Math.pow(Math.abs(l_dYPressed - celestialObject.getYReal()), 2));

			if (l_dLastDelta > l_dDelta)
			{
				l_dLastDelta = l_dDelta;
				l_nearestCelestialObject = celestialObject;
			}
		}

		mainView.updateInfo(l_nearestCelestialObject);
	}

	/**
	 * Repaint the stars with the last parameters
	 */
	public void updateSkyMap()
	{
		this.repaint();
	}

	public void paint(Graphics g)
	{
		CelestialObject sun = null;

		int l_scale = (int) (this.getHeight() / 2);
		int l_xCenter = this.getWidth() / 2 - (int) (dXOrigin * l_scale * zoom);
		int l_yCenter = this.getHeight() / 2 + (int) (dYOrigin * l_scale * zoom);
		for (CelestialObject celestialObject : celestialObjects)
		{
			int l_x, l_y;
			int l_d = getSizeForMagnitude(celestialObject.getMag()) + 1;
			l_x = l_xCenter + (int) (celestialObject.getXReal() * zoom * l_scale);
			l_y = l_yCenter - (int) (celestialObject.getYReal() * zoom * l_scale);
			g.setColor(getColorForColorIndex(celestialObject.getColorIndex()));
			String l_name = celestialObject.getProperName();

			if (l_name != null)
				g.drawString(l_name, l_x, l_y);
			if (celestialObject.getMag() < -20)
			{
				g.drawImage(getToolkit().getImage("res/sun.png"), l_x, l_y, null);
				sun = celestialObject;
			}
			else
				g.fillOval(l_x, l_y, l_d, l_d);
		}

		g.setColor(Color.red);
		g.fillOval(this.getWidth() / 2, this.getHeight() / 2, 30, 30);
		/**
		 * Graphics2D g2 = (Graphics2D) g; g2.rotate(-getArrowAngle(sun), this.getWidth()/2,
		 * this.getHeight()/2); //TODO voir valeur non constante
		 * g2.drawImage(getToolkit().getImage("res/arrow.png"), this.getWidth()/2,
		 * this.getHeight()/2, null);
		 */
	}

	private double getArrowAngle(CelestialObject _object)
	{
		/**
		 * System.out.print("orign x : " + dXOrigin + ", obj x : " + _object.getXReal());
		 * System.out.print("orign y : " + dYOrigin + ", obj y : " + _object.getYReal());
		 */

		double l_dangle = Math.atan((_object.getYReal() - dYOrigin)
				/ (_object.getXReal() - dXOrigin));

		if (dXOrigin > _object.getXReal())
			l_dangle += Math.PI;

		return l_dangle;
	}

	/**
	 * Returns the diameter of the point who display the star The diameter correspond to the star
	 * magnitude
	 * 
	 * @param _mag
	 *            : Magnitude of the star
	 * @return Size of the point in pixel
	 */
	private int getSizeForMagnitude(double _mag)
	{
		int size = 0;

		if (_mag >= 5.3)
			size = 1;
		else if (_mag >= 4.0)
			size = 2;
		else if (_mag >= 2.7)
			size = 3;
		else if (_mag >= 1.4)
			size = 4;
		else if (_mag >= 0.1)
			size = 5;
		else if (_mag >= -1.2)
			size = 6;
		else
			size = 7;

		return size;
	}

	/**
	 * Returns the color of the point who display the star The color correspond to the star color
	 * index
	 * 
	 * @param _colorIndex
	 *            : Color index of the star
	 * @return Color of the point
	 */
	private Color getColorForColorIndex(double _colorIndex)
	{
		Color c;
		if (_colorIndex >= 1.41)
			c = new Color(255, 200, 200);
		else if (_colorIndex >= 0.82)
			c = new Color(255, 225, 150);
		else if (_colorIndex >= 0.59)
			c = new Color(255, 255, 130);
		else if (_colorIndex >= 0.31)
			c = new Color(255, 255, 200);
		else if (_colorIndex >= 0.0)
			c = Color.WHITE;
		else
			c = new Color(215, 215, 255);

		return c;
	}

	public void setZoom(int _zoom)
	{
		this.zoom = _zoom;
	}

	public void setXOrigin(double _xOrigin)
	{
		this.dXOrigin = _xOrigin;
	}

	public void setYOrigin(double _yOrigin)
	{
		this.dYOrigin = _yOrigin;
	}

	public void mouseClicked(MouseEvent _arg0)
	{
	}

	public void mouseEntered(MouseEvent _arg0)
	{
	}

	public void mouseExited(MouseEvent _arg0)
	{
	}

	public void mouseReleased(MouseEvent _arg0)
	{
	}
}
