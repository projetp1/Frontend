/**
 * 
 */
package com.github.projetp1;

/**
 * @author   alexandr.perez
 */

import java.util.logging.Logger;

import jssc.SerialPortException;

import com.github.projetp1.*;
import com.github.projetp1.rs232.*;

public class Pic extends Thread
{

	/**
	 * @uml.property name="longitude"
	 */
	private double longitude = 0.0;
	/**
	 * @uml.property name="latitude"
	 */
	private double latitude = 0.0;
	/**
	 * @uml.property name="angle"
	 */
	private double angle;
	/**
	 * @uml.property name="compass"
	 */
	private double compass;
	
	private int accX = 0;
	private int accY = 0;
	private int accZ = 0;

	private PicMode mode = PicMode.SIMULATION;

	private Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public enum PicMode
	{
		/**
		 * The default mode : point, click and see
		 */
		POINTING,
		/**
		 * The searching star mode
		 */
		GUIDING,
		/**
		 * The simulation mode. No PIC is connected, user commands come from the keyboard
		 */
		SIMULATION;
	}

	protected MainView mainview;

	private RS232 rs;

	/**
	 * 
	 */
	public Pic(MainView _mainview)
	{
		// TODO Auto-generated constructor stub
		this.mainview = _mainview;

		try
		{
			rs = new RS232(mainview.getSettings(), this);
		}
		catch (SerialPortException ex)
		{
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		catch (Exception ex)
		{
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}

		this.start();
	}

	/**
	 * @return
	 * @uml.property name="longitude"
	 */
	public double getLongitude()
	{
		return longitude;
	}

	/**
	 * @param _longitude
	 * @uml.property name="longitude"
	 */
	public void setLongitude(double _longitude)
	{
		if(Math.abs(_longitude) <= 180)
			this.longitude = _longitude;
	}

	/**
	 * @return
	 * @uml.property name="latitude"
	 */
	public double getLatitude()
	{
		return latitude;
	}

	/**
	 * @param _latitude
	 * @uml.property name="latitude"
	 */
	public void setLatitude(double _latitude)
	{
		if(Math.abs(_latitude) <= 90)
			this.latitude = _latitude;
	}

	/**
	 * @return
	 * @uml.property name="angle"
	 */
	public double getAngle()
	{
		return angle;
	}

	/**
	 * @param _angle
	 * @uml.property name="angle"
	 */
	public void setAngle(double _angle)
	{
		if(Math.abs(_angle) <= 90)
			this.angle = _angle;
	}

	/**
	 * @return
	 * @uml.property name="compass"
	 */
	public double getCompass()
	{
		return compass;
	}

	/**
	 * @param _compass
	 * @uml.property name="compass"
	 */
	public void setCompass(double _compass)
	{
		if(_compass >= 0 && _compass < 360)
			this.compass = _compass;
	}

	public PicMode getMode()
	{
		return mode;
	}

	public void setMode(PicMode mode)
	{
		this.mode = mode;
	}

	public void run()
	{
		RS232Command commande;
		// Reception des données
		while ((commande = rs.getLastCommand()) != null)
		{
			// Switch pour trier les données
			switch (commande.getCommandNumber())
			{
				case LOCATION_UPDATE:
					String[] locComponents = commande.getDatas().split(",", 4);
					if(!locComponents[0].isEmpty() && !locComponents[1].isEmpty())
						this.setLongitude(Mathematics.picLon2Lon(Double.parseDouble(locComponents[0]), locComponents[1].charAt(0)));
					if(!locComponents[2].isEmpty() && !locComponents[3].isEmpty())
						this.setLatitude(Mathematics.picLat2Lat(Double.parseDouble(locComponents[2]), locComponents[3].charAt(0)));
					break;
				case ACCELEROMETER_UPDATE:
				case MAGNETOMETER_UPDATE:
					String[] components = commande.getDatas().split(",", 3);
					int[] values = new int[3];
					for (int l_i = 0; l_i < components.length; l_i++)
						values[l_i] = Integer.parseInt(components[l_i]);

					if (commande.getCommandNumber() == RS232CommandType.ACCELEROMETER_UPDATE)
					{
						accX = values[0];
						accY = values[1];
						accZ = values[2];
						this.angle = Mathematics.calculateAngleInclinometer(values[0], values[1],
								values[2]);
					}
					else {
						this.compass = Mathematics.calculateAngleCompass(accX, accY, accZ, values[0], values[1],
								values[2]);
						log.info("Heading : " + String.valueOf(compass));
					}
					break;
				case PIC_STATUS:
					log.info("Error received from the Pic : " + commande.getDatas());
					break;
				default:
					log.severe("Unknown command number received");
					break;
			}
		}
	}
}
