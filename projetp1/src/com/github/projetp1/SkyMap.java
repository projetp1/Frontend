/**=====================================================================*
| This file declares the following classes:
|    SkyMap
|
| Description of the class SkyMap :
|	  This class is used to display the stars, the constellations and the arrow direction.
|	  It considers the zoom and screen size for display a part of the sky.
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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Logger;

import com.github.projetp1.Pic.PicMode;

@SuppressWarnings("serial")
public class SkyMap extends Container implements MouseListener, Runnable
{
	private int zoom = 1;
	private double dXOrigin = 0.0;
	private double dYOrigin = 0.0;
	private double dLongitude = 6.937892;
	private double dLatitude = 46.997415;
	private double dMagnitudeMax = 6.5;
	private boolean bShowConstellations = false;
	private ArrayList<CelestialObject> celestialObjects = null;
	private ArrayList<Constellation> constellations = null;
	private CelestialObject celestialObjectSearched = null;
	private MainView mainView = null;	
	private Image[] starHighlight = null;
	private int starHighlightCurrentImage = 0;
	private int intervalle = 0;
	private Thread runner = null;
	
	private Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private static final int kINTERVALWITHHIGHLIGHTIMAGE = 40;

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
		
		bShowConstellations = mainView.getSettings().getConstellation();
		dMagnitudeMax = mainView.getSettings().getMagnitude();

		starHighlight = new Image[10];
		
		for (int i = 0; i < 10; i++)
		{
			String imageURL = "/pointeur" + (i+1) + ".png";
			starHighlight[i] = getToolkit().getImage(getClass().getResource(imageURL));
		}
		
		intervalle = kINTERVALWITHHIGHLIGHTIMAGE;
		
		this.start();
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
		
		int l_scale = (this.getHeight() / 2);
		int l_xCenter = this.getWidth() / 2 - (int) (dXOrigin * l_scale * zoom);
		int l_yCenter = this.getHeight() / 2 + (int) (dYOrigin * l_scale * zoom);
		
		double l_dLastDelta = 10; //big value for be sure that will be changed
		double l_dXPressed = ((double)(_e.getX() - l_xCenter) / zoom / l_scale);
		double l_dYPressed = ((double)(_e.getY() - l_yCenter) / zoom / l_scale) * -1;
		CelestialObject l_nearestCelestialObject = null;
		
		for (CelestialObject celestialObject : celestialObjects)
		{
			double l_dDelta = Math.sqrt(Math.pow(
					Math.abs(l_dXPressed - celestialObject.getXReal()), 2)
					+ Math.pow(Math.abs(l_dYPressed - celestialObject.getYReal()), 2));

			if (l_dLastDelta > l_dDelta && celestialObject.getMag() <= dMagnitudeMax)
			{
				l_dLastDelta = l_dDelta;
				l_nearestCelestialObject = celestialObject;
			}
		}
		
		celestialObjectSearched = l_nearestCelestialObject;

		
		if(mainView.getPic() != null && mainView.getPic().getMode() != PicMode.SIMULATION)
		{
			mainView.getPic().changeMode(PicMode.GUIDING);
			mainView.compassPanel.setRedNeedle(celestialObjectSearched.getAzimuth() * 180 / Math.PI);
			mainView.inclinometerPanel.setRedNeedle(celestialObjectSearched.getHeight() * 180 / Math.PI);
			mainView.compassPanel.setSearchMode(true);
			mainView.inclinometerPanel.setSearchMode(true);
		}
		
		mainView.updateInfo(celestialObjectSearched);
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
				celestialObjects = mainView.getDataBase().starsForCoordinates(Calendar.getInstance(), dLatitude, dLongitude);
				
				if(bShowConstellations)
					constellations = mainView.getDataBase().getConstellations(Calendar.getInstance(), dLatitude, dLongitude);
				else
					constellations = null;
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		this.repaint();
	}

	/**
	 * Paint the stars, the sun, the moon, the constellations and the arrow that point the searched star.
	 * 
	 * @param _g
	 *            The Graphics object for paint the window
	 */
	@Override
	public void paint(Graphics _g)
	{
		Graphics2D g2 = (Graphics2D)_g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if (celestialObjects == null)
		{
			log.severe("No celestial objects to display");
			return;
		}
				
		Image l_imgArrow = getToolkit().getImage(getClass().getResource("/arrow.png"));

		Font l_font = new Font("Calibri" , Font.BOLD, 16);
		_g.setFont(l_font);
		
		if(constellations == null)
		{
			log.severe("No constellations to display");
		}
		else
		{
			for (Constellation constellation : constellations)
			{
				int[] l_coordinatesName = convertCoordinatesForSkymap(constellation.getX(), constellation.getY());
				for (double[] line : constellation.getLines())
				{
					_g.setColor(new Color(130, 200, 255));
					int[] l_coordinatesFirstStar = convertCoordinatesForSkymap(line[0], line[1]);
					int[] l_coordinatesSecondStar = convertCoordinatesForSkymap(line[2], line[3]);
					_g.drawLine(
							l_coordinatesFirstStar[0],
							l_coordinatesFirstStar[1],
							l_coordinatesSecondStar[0],
							l_coordinatesSecondStar[1]
							);
				}
				_g.drawString(((constellation.getProperName() != null) ? constellation.getProperName() : ""), l_coordinatesName[0], l_coordinatesName[1]);
			}
		}
		
		l_font = new Font("Calibri" , Font.PLAIN, 16);
		_g.setFont(l_font);
		
		for (CelestialObject celestialObject : celestialObjects)
		{
			if(celestialObjectSearched != null && celestialObject.getId() == celestialObjectSearched.getId())
				celestialObjectSearched = celestialObject;

			int[] l_coordinatesCelestialObject = convertCoordinatesForSkymap(celestialObject.getXReal(), celestialObject.getYReal());
			int l_d = getSizeForMagnitude(celestialObject.getMag());
			
			Color l_color = getColorForColorIndex(celestialObject.getColorIndex(), 255);
			_g.setColor(l_color);
			
			String l_name = celestialObject.getProperName();

			if (l_name != null && l_name.equals("Sun"))
			{
				Image l_imgSun = getToolkit().getImage(getClass().getResource("/sun.png"));
				_g.drawImage(l_imgSun,
						l_coordinatesCelestialObject[0] - (l_imgSun.getHeight(this) / 2),
						l_coordinatesCelestialObject[1] - (l_imgSun.getWidth(this) / 2),
						this);
				_g.drawString(l_name, l_coordinatesCelestialObject[0] - (l_imgSun.getHeight(null) / 2) + 20, l_coordinatesCelestialObject[1] - (l_imgSun.getHeight(null) / 2) + 5);
			}
			else if (l_name != null && l_name.equals("Moon"))
			{
				double l_dMoon = celestialObject.getMag() + 99; //La propriété magnitude contient l'etat lunaire (pleine lune..) (+99 pour avoir de 0 à 199)

				l_dMoon -= 200/23/2;
				int l_moon = (int) (l_dMoon / (200/23));
				Image l_imgMoon = null;
				
				switch (l_moon)
				{
					case 0:
						l_imgMoon = getToolkit().getImage(getClass().getResource("/moon-13.png"));
						break;
					case 1:
						l_imgMoon = getToolkit().getImage(getClass().getResource("/moon-14.png"));
						break;
					case 2:
						l_imgMoon = getToolkit().getImage(getClass().getResource("/moon-15.png"));
						break;
					case 3:
						l_imgMoon = getToolkit().getImage(getClass().getResource("/moon-16.png"));
						break;
					case 4:
						l_imgMoon = getToolkit().getImage(getClass().getResource("/moon-17.png"));
						break;
					case 5:
						l_imgMoon = getToolkit().getImage(getClass().getResource("/moon-18.png"));
						break;
					case 6:
						l_imgMoon = getToolkit().getImage(getClass().getResource("/moon-19.png"));
						break;
					case 7:
						l_imgMoon = getToolkit().getImage(getClass().getResource("/moon-20.png"));
						break;
					case 8:
						l_imgMoon = getToolkit().getImage(getClass().getResource("/moon-21.png"));
						break;
					case 9:
						l_imgMoon = getToolkit().getImage(getClass().getResource("/moon-22.png"));
						break;
					case 10:
						l_imgMoon = getToolkit().getImage(getClass().getResource("/moon-23.png"));
						break;
					case 11:
						l_imgMoon = getToolkit().getImage(getClass().getResource("/moon-1.png"));
						break;
					case 12:
						l_imgMoon = getToolkit().getImage(getClass().getResource("/moon-2.png"));
						break;
					case 13:
						l_imgMoon = getToolkit().getImage(getClass().getResource("/moon-3.png"));
						break;
					case 14:
						l_imgMoon = getToolkit().getImage(getClass().getResource("/moon-4.png"));
						break;
					case 15:
						l_imgMoon = getToolkit().getImage(getClass().getResource("/moon-5.png"));
						break;
					case 16:
						l_imgMoon = getToolkit().getImage(getClass().getResource("/moon-6.png"));
						break;
					case 17:
						l_imgMoon = getToolkit().getImage(getClass().getResource("/moon-7.png"));
						break;
					case 18:
						l_imgMoon = getToolkit().getImage(getClass().getResource("/moon-8.png"));
						break;
					case 19:
						l_imgMoon = getToolkit().getImage(getClass().getResource("/moon-9.png"));
						break;
					case 20:
						l_imgMoon = getToolkit().getImage(getClass().getResource("/moon-10.png"));
						break;
					case 21:
						l_imgMoon = getToolkit().getImage(getClass().getResource("/moon-11.png"));
						break;
					case 22:
						l_imgMoon = getToolkit().getImage(getClass().getResource("/moon-12.png"));
						break;
					default:
						l_imgMoon = getToolkit().getImage(getClass().getResource("/moon-13.png"));
						break;
				}

				if (l_imgMoon != null)
				{
					_g.drawImage(l_imgMoon, 
							l_coordinatesCelestialObject[0] - (l_imgMoon.getHeight(null) / 2),
							l_coordinatesCelestialObject[1] - (l_imgMoon.getHeight(null) / 2), 
							this);
					_g.drawString(l_name, l_coordinatesCelestialObject[0] - (l_imgMoon.getHeight(null) / 2) + 5, l_coordinatesCelestialObject[1] - (l_imgMoon.getHeight(null) / 2) - 5);
				}
			}
			else
			{
				_g.fillOval(l_coordinatesCelestialObject[0] - l_d / 2, l_coordinatesCelestialObject[1] - l_d / 2, l_d, l_d);
				if (l_name != null && l_d > 0)
					_g.drawString(l_name, l_coordinatesCelestialObject[0], l_coordinatesCelestialObject[1] + 15);
			}
		}

		Image l_imgCenter = getToolkit().getImage(getClass().getResource("/center.png"));
		_g.drawImage(l_imgCenter,
				(this.getWidth() / 2 - l_imgCenter.getWidth(null) / 2),
				(this.getHeight() / 2 - l_imgCenter.getHeight(null) / 2),
				this);
		
		if (celestialObjectSearched != null)
		{
			int[] l_coordinatesCelestialObjectSearched = convertCoordinatesForSkymap(celestialObjectSearched.getXReal(), celestialObjectSearched.getYReal());
			
			if(starHighlight[starHighlightCurrentImage] != null && celestialObjectSearched.getProperName() == null || !celestialObjectSearched.getProperName().equals("Moon") && !celestialObjectSearched.getProperName().equals("Sun"))
			{
				_g.drawImage(starHighlight[starHighlightCurrentImage],
						l_coordinatesCelestialObjectSearched[0] - starHighlight[starHighlightCurrentImage].getWidth(null) / 2,
						l_coordinatesCelestialObjectSearched[1] - starHighlight[starHighlightCurrentImage].getHeight(null) / 2,
						this);
			}

			if (!(l_coordinatesCelestialObjectSearched[0] > this.getWidth() * 0.1 && l_coordinatesCelestialObjectSearched[0] < this.getWidth() * 0.9  //Marge de 10%
					&& l_coordinatesCelestialObjectSearched[1] > this.getHeight() * 0.1 && l_coordinatesCelestialObjectSearched[1] < this.getHeight() * 0.9))
			{
				double l_dAngle = -getArrowAngle(celestialObjectSearched);
				g2.rotate(l_dAngle, this.getWidth() / 2, this.getHeight() / 2);
				g2.drawImage(l_imgArrow, (this.getWidth() / 2 - l_imgArrow.getWidth(null) / 2), (this.getHeight() / 2 - l_imgArrow.getHeight(null) / 2), this);
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
		double l_dAngle = Math.atan((_object.getYReal() - dYOrigin) / (_object.getXReal() - dXOrigin));

		if (dXOrigin > _object.getXReal())
			l_dAngle += Math.PI;

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
			l_size = 1;
		else if (_mag >= 5.3)
			l_size = 2;
		else if (_mag >= 4.0)
			l_size = 3;
		else if (_mag >= 2.7)
			l_size = 4;
		else if (_mag >= 1.4)
			l_size = 5;
		else if (_mag >= 0.1)
			l_size = 6;
		else if (_mag >= -1.2)
			l_size = 7;
		else
			l_size = 8;

		if(_mag > dMagnitudeMax)
			l_size = 0;

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
			//pink 255 200 200
			c = new Color(255, 215, 215, _alpha);
		else if (_colorIndex >= 0.82)
			//orange 255 225 150
			c = new Color(255, 230, 170, _alpha);
		else if (_colorIndex >= 0.59)
			//yellow 255 255 130
			c = new Color(255, 255, 160, _alpha);
		else if (_colorIndex >= 0.31)
			//beige 255 255 200
			c = new Color(255, 255, 220, _alpha);
		else if (_colorIndex >= 0.0)
			//white 255 255 255
			c = new Color(255, 255, 255, _alpha);
		else
			//blue 215 215 255
			c = new Color(240, 240, 255, _alpha);

		return c;
	}
	
	private int[] convertCoordinatesForSkymap(double _x, double _y)
	{		
		int l_scale = (int)(this.getHeight() / 2);
		int l_xCenter = (int)(this.getWidth() / 2 - dXOrigin * l_scale * zoom);
		int l_yCenter = (int)(this.getHeight() / 2 + dYOrigin * l_scale * zoom);
		
		int l_newX = l_xCenter + (int) (_x * zoom * l_scale);
		int l_newY = l_yCenter - (int) (_y * zoom * l_scale);		
		
		return new int[] {
				l_newX,
				l_newY
		};
	}
	
	public void run()
	{
		Thread thisThread = Thread.currentThread();
		int l_refreshAll = (int)(30000 / kINTERVALWITHHIGHLIGHTIMAGE);
		int l_refreshAllCounter = 0;
		while (runner == thisThread)
		{
			if (++starHighlightCurrentImage >= starHighlight.length)
				starHighlightCurrentImage = 0;
			
			if(++l_refreshAllCounter >= l_refreshAll)
			{
				l_refreshAllCounter = 0;
				this.updateSkyMap();				
			}
			else
			{
				this.repaint();
			}
			
			try
			{
				Thread.sleep(intervalle);
			}
			catch (InterruptedException e)
			{
				log.severe(e.toString());
			}
		}
	}

	public void stop()
	{
		if (runner != null)
		{
			runner = null;
		}
	}
	
	public void start()
	{
		if (runner == null)
		{
			runner = new Thread(this);
			runner.start();
		}
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

	public void setShowConstellations(boolean _bShowConstellations)
	{
		bShowConstellations = _bShowConstellations;
		this.updateSkyMap();
	}
	
	public void setMagnitudeMax(double _dMagnitudeMax)
	{
		dMagnitudeMax = _dMagnitudeMax;
		this.updateSkyMap();
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
