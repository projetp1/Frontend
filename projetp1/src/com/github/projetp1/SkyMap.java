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
	 * @param _mainView
	 *            : The MainView class
	 */
	public SkyMap(String _sDataBase, String _sDelimiter, MainView _mainView)
	{
		mainView = _mainView;

		this.addMouseListener(this);
		Color l_BackgroundColor = new Color(5,30,50);
		this.setBackground(l_BackgroundColor);
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

		int l_scale = (int)(this.getHeight() / 2);
		int l_xCenter = this.getWidth() / 2 - (int)(dXOrigin * l_scale * zoom);
		int l_yCenter = this.getHeight() / 2 + (int)(dYOrigin * l_scale * zoom);
		for (CelestialObject celestialObject : celestialObjects)
		{
			int l_x, l_y;
			int l_d = getSizeForMagnitude(celestialObject.getMag());
			Color l_color = getColorForColorIndex(celestialObject.getColorIndex(), 255);
			l_x = l_xCenter + (int) (celestialObject.getXReal() * zoom * l_scale) / 2;
			l_y = l_yCenter - (int) (celestialObject.getYReal() * zoom * l_scale) / 2;
			g.setColor(l_color);
			String l_name = celestialObject.getProperName();

			if (l_name != null)
				g.drawString(l_name, l_x, l_y - 10);
			if (celestialObject.getMag() < -20)
			{
				Image l_imgSun = getToolkit().getImage("res/sun.png");
				g.drawImage(l_imgSun, l_x - (l_imgSun.getHeight(null) / 2), l_y - (l_imgSun.getHeight(null) / 2), null);
				sun = celestialObject;
			}
			else
			{
				g.fillOval(l_x - l_d / 2, l_y - l_d / 2, l_d, l_d);
				l_color = getColorForColorIndex(celestialObject.getColorIndex(), 200);
				g.setColor(l_color);
				l_d += 1;
				g.fillOval(l_x - l_d / 2, l_y - l_d / 2, l_d, l_d);
				l_color = getColorForColorIndex(celestialObject.getColorIndex(), 100);
				g.setColor(l_color);
				l_d += 1;
				g.fillOval(l_x - l_d / 2, l_y - l_d / 2, l_d, l_d);
			}

			sun = celestialObject;
		}

		g.setColor(Color.red);
		g.fillOval(this.getWidth() / 2 - 15, this.getHeight() / 2 - 15, 30, 30);
		
		Image l_imgArrow = getToolkit().getImage("res/arrow.png");
		
		double l_dangle = -getArrowAngle(sun);
		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(l_dangle, this.getWidth() / 2,this.getHeight() / 2);
		/*g2.drawImage(
				l_imgArrow, 
				(int)(this.getWidth() / 2 + Math.cos(l_dangle)*200 + Math.sin(l_dangle) * (l_imgArrow.getHeight(null) / 2)),
				(int)(this.getHeight() / 2 - Math.sin(l_dangle)*200 - Math.cos(l_dangle) * (l_imgArrow.getHeight(null) / 2)), 
				null);*/
		g2.drawImage(
				l_imgArrow, 
				(int)(this.getWidth() / 2 - l_imgArrow.getWidth(null) / 2),
				(int)(this.getHeight() / 2 - l_imgArrow.getHeight(null) / 2), 
				null);
	}

	private double getArrowAngle(CelestialObject _object)
	{
		/**
		 * System.out.print("orign x : " + dXOrigin + ", obj x : " + _object.getXReal());
		 * System.out.print("orign y : " + dYOrigin + ", obj y : " + _object.getYReal());
		 */

		double l_dangle = Math.atan((_object.getYReal() - dYOrigin) / (_object.getXReal() - dXOrigin));
		
		if (dXOrigin > _object.getXReal()) 
			l_dangle += Math.PI;

		
		//System.out.print("angle : " + Math.toDegrees(l_dangle) + "\r\n");
		
		/*if(mainView.getPic().getMode() == Pic.PicMode.GUIDING)
		{
			if(l_dangle > 67.5 && l_dangle <= 112.5)
				mainView.getPic().sendArrow;
			else if(l_dangle > 112.5 && l_dangle <= 157.5)
				1;
			else if(l_dangle > 157.5 && l_dangle <= 202.5)
				2;
			else if(l_dangle > 202.5 && l_dangle <= 247.5)
				3;
			else if(l_dangle > 247.5 && l_dangle <= 292.5)
				4;
			else if(l_dangle > 292.5 && l_dangle <= 337.5)
				5;
			else if(l_dangle > 337.5 && l_dangle <= 22.5)
				6;
			else
				7;
		}
		*/
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
		if (_mag >= 6.0)
			size = 0;
		else if (_mag >= 5.3)
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
	private Color getColorForColorIndex(double _colorIndex, int _alpha)
	{
		Color c;
		if (_colorIndex >= 1.41)
			c = new Color(255, 200, 200, _alpha);
		else if (_colorIndex >= 0.82)
			c = new Color(255, 225, 150, _alpha);
		else if (_colorIndex >= 0.59)
			c = new Color(255, 255, 130, _alpha);
		else if (_colorIndex >= 0.31)
			c = new Color(255, 255, 200, _alpha);
		else if (_colorIndex >= 0.0)
			c = new Color(255, 255, 255, _alpha);
		else
			c = new Color(215, 215, 255, _alpha);

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
