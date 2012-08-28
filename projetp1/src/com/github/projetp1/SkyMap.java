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

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Logger;

import com.github.projetp1.Pic.PicMode;
import com.github.projetp1.rs232.RS232.PicArrowDirection;

@SuppressWarnings("serial")
public class SkyMap extends Container implements MouseListener
{
	private int zoom = 1;
	private double dXOrigin = 0;
	private double dYOrigin = 0;
	private double dLongitude = 6.937892;
	private double dLatitude = 46.997415;
	private ArrayList<CelestialObject> celestialObjects;
	private CelestialObject celestialObjectSearched = null;
	MainView mainView = null;
	private Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	private PicArrowDirection lastArrowSent = null;

	/**
	 * SkyMap Constructor
	 * 
	 * @param _mainView
	 *            The MainView class
	 */
	public SkyMap(MainView _mainView)
	{
		this.addMouseListener(this);
		Color l_BackgroundColor = new Color(5, 30, 50);
		this.setBackground(l_BackgroundColor);

		mainView = _mainView;
	}

	/**
	 * Find the nearest star to the place pointed, and show its information
	 * 
	 * @param _e
	 *            The MouseEvent
	 */
	@Override
	public void mousePressed(MouseEvent _e)
	{
		if (celestialObjects == null)
		{
			log.severe("No celestial object in celestialObjects");
			return;
		}


		double l_dRoll = 0.0;
		if(this.mainView.getPic() != null)
			l_dRoll = this.mainView.getPic().getRoll();
		
		int l_scale = (this.getHeight() / 2);
		int l_xCenter = this.getWidth() / 2 - (int) (dXOrigin * l_scale * zoom);
		int l_yCenter = this.getHeight() / 2 + (int) (dYOrigin * l_scale * zoom);
		double l_dLastDelta = 10; // big value for be sure that will be changed
		double l_dXPressed = (double) (Mathematics.getNewXYRotation(_e.getX(), _e.getY(), -l_dRoll)[0] - l_xCenter) / zoom / l_scale;
		double l_dYPressed = (double) ((Mathematics.getNewXYRotation(_e.getX(), _e.getY(), -l_dRoll)[1] - l_yCenter) / zoom / l_scale) * -1;
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
	 * Get infos of the PIC and repaint the stars with the last parameters
	 */
	public void updateSkyMap()
	{
		
		if (this.mainView.getPic() != null && this.mainView.getPic().getMode() != PicMode.SIMULATION)
		{
			dLatitude = this.mainView.getPic().getLatitude();
			dLongitude = this.mainView.getPic().getLongitude();
			double[] l_dOrigin = Mathematics.getOrigin(this.mainView.getPic().getPitch(), this.mainView.getPic().getAzimuth());
			dXOrigin = l_dOrigin[0];
			dYOrigin = l_dOrigin[1];
		}

		if (mainView.getDataBase() != null)
		{
			try
			{
				celestialObjects = mainView.getDataBase().starsForCoordinates(
						Calendar.getInstance(), dLatitude, dLongitude);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		this.repaint();
	}

	/**
	 * Paint the stars, the sun, the moon and the arrow that point the searched star.
	 * 
	 * @param _g
	 *            The Graphics object for paint the window
	 */
	@Override
	public void paint(Graphics _g)
	{
		if (celestialObjects == null)
		{
			log.severe("No celestial objects to display");
			return;
		}

		double l_dRoll = 0.0;
		if(this.mainView.getPic() != null)
			l_dRoll = this.mainView.getPic().getRoll();
		
		int l_scale = (this.getHeight() / 2);
		int l_xCenter = this.getWidth() / 2 - (int) (dXOrigin * l_scale * zoom);
		int l_yCenter = this.getHeight() / 2 + (int) (dYOrigin * l_scale * zoom);
		for (CelestialObject celestialObject : celestialObjects)
		{
			if(celestialObjectSearched != null && celestialObject.getId() == celestialObjectSearched.getId())
				celestialObjectSearched = celestialObject;
			
			int l_x, l_y;
			int l_d = getSizeForMagnitude(celestialObject.getMag());
			Color l_color = getColorForColorIndex(celestialObject.getColorIndex(), 255);
			l_x = l_xCenter + (int) (Mathematics.getNewXYRotation(celestialObject.getXReal(), celestialObject.getYReal(), l_dRoll)[0] * zoom * l_scale);
			l_y = l_yCenter - (int) (Mathematics.getNewXYRotation(celestialObject.getXReal(), celestialObject.getYReal(), l_dRoll)[1] * zoom * l_scale);
			
			_g.setColor(l_color);
			String l_name = celestialObject.getProperName();

			if (l_name != null && l_name.equals("Sun"))
			{
				Image l_imgSun = getToolkit().getImage("res/sun.png");
				_g.drawImage(l_imgSun,
						l_x - (l_imgSun.getHeight(null) / 2),
						l_y - (l_imgSun.getHeight(null) / 2),
						null);
			}
			else if (l_name != null && l_name.equals("Moon"))
			{
				double l_dMoon = celestialObject.getMag();
				Image l_imgMoon = null;
				if (l_dMoon > 87 || l_dMoon < -87)
					l_imgMoon = getToolkit().getImage("res/moon_0.png");
				else if (l_dMoon > -88 && l_dMoon < -62)
					l_imgMoon = getToolkit().getImage("res/moon_1.png");
				else if (l_dMoon > -63 && l_dMoon < -37)
					l_imgMoon = getToolkit().getImage("res/moon_2.png");
				else if (l_dMoon > -38 && l_dMoon < -12)
					l_imgMoon = getToolkit().getImage("res/moon_3.png");
				else if (l_dMoon > -13 && l_dMoon < 13)
					l_imgMoon = getToolkit().getImage("res/moon_7.png");
				else if (l_dMoon > 14 && l_dMoon < 38)
					l_imgMoon = getToolkit().getImage("res/moon_4.png");
				else if (l_dMoon > 39 && l_dMoon < 63)
					l_imgMoon = getToolkit().getImage("res/moon_5.png");
				else if (l_dMoon > 64 && l_dMoon < 88)
					l_imgMoon = getToolkit().getImage("res/moon_6.png");

				if (l_imgMoon != null)
					_g.drawImage(l_imgMoon, 
							l_x - (l_imgMoon.getHeight(null) / 2),
							l_y - (l_imgMoon.getHeight(null) / 2), 
							null);
			}
			else
			{
				_g.fillOval(l_x - l_d / 2, l_y - l_d / 2, l_d, l_d);
				l_color = getColorForColorIndex(celestialObject.getColorIndex(), 200);
				_g.setColor(l_color);
				l_d += 1;
				_g.fillOval(l_x - l_d / 2, l_y - l_d / 2, l_d, l_d);
				l_color = getColorForColorIndex(celestialObject.getColorIndex(), 100);
				_g.setColor(l_color);
				l_d += 1;
				_g.fillOval(l_x - l_d / 2, l_y - l_d / 2, l_d, l_d);
			}

			if (l_name != null)
				_g.drawString(l_name, l_x, l_y - 10);
		}

		Image l_imgCenter = getToolkit().getImage("res/center.png");
		_g.drawImage(l_imgCenter,
				(this.getWidth() / 2 - l_imgCenter.getWidth(null) / 2),
				(this.getHeight() / 2 - l_imgCenter.getHeight(null) / 2),
				null);
		
		if (celestialObjectSearched != null)
		{
			int l_xStarPointed = l_xCenter + (int) (Mathematics.getNewXYRotation(celestialObjectSearched.getXReal(), celestialObjectSearched.getYReal(), l_dRoll)[0] * zoom * l_scale);
			int l_yStarPointed = l_yCenter - (int) (Mathematics.getNewXYRotation(celestialObjectSearched.getXReal(), celestialObjectSearched.getYReal(), l_dRoll)[1] * zoom * l_scale);
			
			if(celestialObjectSearched.getProperName() == null || !celestialObjectSearched.getProperName().equals("Moon") && !celestialObjectSearched.getProperName().equals("Sun"))
			{
				Image l_imgStarHighlight = getToolkit().getImage("res/star_highlight.png");
				_g.drawImage(l_imgStarHighlight,
						l_xStarPointed - l_imgStarHighlight.getWidth(null) / 2,
						l_yStarPointed - l_imgStarHighlight.getHeight(null) / 2,
						null);
			}

			if (!(l_xStarPointed > this.getWidth() * 0.1 && l_xStarPointed < this.getWidth() * 0.9  //Marge de 10%
					&& l_yStarPointed > this.getHeight() * 0.1 && l_yStarPointed < this.getHeight() * 0.9))
			{
				Image l_imgArrow = getToolkit().getImage("res/arrow.png");
				double l_dAngle = -getArrowAngle(celestialObjectSearched);
				Graphics2D g2 = (Graphics2D) _g;
				g2.rotate(l_dAngle, this.getWidth() / 2, this.getHeight() / 2);
				g2.drawImage(l_imgArrow, (this.getWidth() / 2 - l_imgArrow.getWidth(null) / 2),
						(this.getHeight() / 2 - l_imgArrow.getHeight(null) / 2), null);
			}
		}
	}

	/**
	 * Returns the angle between the PIC and the star selected. And send the code of the arrow who's
	 * displayed on the PIC. 0 is for the arrow that point the top and each incrementation
	 * correspond to 45° in trigonometric sense.
	 * 
	 * @param _object
	 *            The star object
	 * @return The angle
	 */
	private double getArrowAngle(CelestialObject _object)
	{
		double l_dAngle = Math.atan((_object.getYReal() - dYOrigin)
				/ (_object.getXReal() - dXOrigin));

		if (dXOrigin > _object.getXReal())
			l_dAngle += Math.PI;

		double dblAngle = Math.toDegrees(l_dAngle);
		dblAngle += 90;
		dblAngle /= 45;
		int iAngle = (int) Math.round(dblAngle);

		// log.info("angle : " + Math.toDegrees(l_dangle) + "\r\n");

		if(mainView.getPic() != null && mainView.getPic().getMode() == Pic.PicMode.GUIDING)
		{
			PicArrowDirection dir = null;

			switch (iAngle)
			{
				case 0:
					dir = PicArrowDirection.SOUTH;
					break;
				case 1:
					dir = PicArrowDirection.SOUTHEAST;
					break;
				case 2:
					dir = PicArrowDirection.EAST;
					break;
				case 3:
					dir = PicArrowDirection.NORTHEAST;
					break;
				case 4:
					dir = PicArrowDirection.NORTH;
					break;
				case 5:
					dir = PicArrowDirection.NORTHWEST;
					break;
				case 6:
					dir = PicArrowDirection.WEST;
					break;
				case 7:
					dir = PicArrowDirection.SOUTHWEST;
					break;
				default:
					break;
			}
			if (dir != null && (lastArrowSent == null || lastArrowSent != dir))
			{
				mainView.getPic().setPicArrow(dir);
				lastArrowSent = dir;
			}
		}

		return l_dAngle;
	}

	/**
	 * Returns the diameter of the point who display the star The diameter correspond to the star
	 * magnitude
	 * 
	 * @param _mag
	 *            Magnitude of the star
	 * @return Size of the point in pixel
	 */
	private int getSizeForMagnitude(double _mag)
	{
		int l_size = 0;
		if (_mag >= 6.0)
			l_size = 0;
		else if (_mag >= 5.3)
			l_size = 1;
		else if (_mag >= 4.0)
			l_size = 2;
		else if (_mag >= 2.7)
			l_size = 3;
		else if (_mag >= 1.4)
			l_size = 4;
		else if (_mag >= 0.1)
			l_size = 5;
		else if (_mag >= -1.2)
			l_size = 6;
		else
			l_size = 7;

		return l_size;
	}

	/**
	 * Returns the color of the point who display the star The color correspond to the star color
	 * index
	 * 
	 * @param _colorIndex
	 *            Color index of the star
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

	public double getdLongitude()
	{
		return dLongitude;
	}

	public double getdLatitude()
	{
		return dLatitude;
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

	public void setCelestialObjectSearched(CelestialObject _celestialObjectSearched)
	{
		celestialObjectSearched = _celestialObjectSearched;
	}

	@Override
	public void mouseClicked(MouseEvent _arg0)
	{
	}

	@Override
	public void mouseEntered(MouseEvent _arg0)
	{
	}

	@Override
	public void mouseExited(MouseEvent _arg0)
	{
	}

	@Override
	public void mouseReleased(MouseEvent _arg0)
	{
	}
}
