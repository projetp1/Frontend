/**
 * 
 */
package com.github.projetp1;

/**
 * @author   alexandr.perez
 */

import java.util.ArrayList;
import java.util.logging.Logger;


import jssc.SerialPortException;

import com.github.projetp1.rs232.*;
import com.github.projetp1.rs232.RS232.PicArrowDirection;

public class Pic extends Thread implements Observer
{

	private ArrayList<Observateur> listObservateur = new ArrayList<Observateur>();
	/**
	 * @uml.property name="longitude"
	 */
	private double longitude = 0.0;
	private double latitude = 0.0;

	private double azimuth = 0.0;
	private double pitch = 0.0;
	private double roll = 0.0;
	
	private int[] acc = new int[3];
	private int[] mag = new int[3];

	private PicMode mode = PicMode.SIMULATION;

	private Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	/**
	 * The different modes in which the PIC may operate
	 */
	public enum PicMode
	{
		
		/** The default mode : point, click and see. */
		POINTING,
		
		/** The searching star mode. */
		GUIDING,
		/**
		 * The simulation mode. No PIC is connected, user commands come from the keyboard
		 */
		SIMULATION;
	}

	/** The MainView object. */
	protected MainView mainview;

	private RS232 rs;

	/**
	 * Instantiates a new PIC.
	 *
	 * @param _mainview the _mainview
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
	 * Gets the longitude.
	 *
	 * @return the longitude
	 */
	public double getLongitude()
	{
		return longitude;
	}

	/**
	 * Sets the longitude.
	 *
	 * @param _longitude the new longitude
	 */
	protected void setLongitude(double _longitude)
	{
		if(Math.abs(_longitude) <= 180)
			this.longitude = _longitude;
	}

	/**
	 * Gets the latitude.
	 *
	 * @return the latitude
	 */
	public double getLatitude()
	{
		return latitude;
	}

	/**
	 * Sets the latitude.
	 *
	 * @param _latitude the new latitude
	 */
	protected void setLatitude(double _latitude)
	{
		if(Math.abs(_latitude) <= 90)
			this.latitude = _latitude;
	}

	/**
	 * Gets the angle.
	 *
	 * @return the angle
	 */
	@Deprecated
	public double getAngle()
	{
		return pitch;
	}

	/**
	 * Gets the compass.
	 *
	 * @return the compass
	 */
	@Deprecated
	public double getCompass()
	{
		return azimuth;
	}


	/**
	 * Gets the azimuth.
	 *
	 * @return the azimuth
	 */
	public double getAzimuth()
	{
		return azimuth;
	}

	/**
	 * Gets the pitch.
	 *
	 * @return the pitch
	 */
	public double getPitch()
	{
		return pitch;
	}

	/**
	 * Gets the roll.
	 *
	 * @return the roll
	 */
	public double getRoll()
	{
		return roll;
	}

	/**
	 * Gets the mode.
	 *
	 * @return the mode
	 */
	public PicMode getMode()
	{
		return mode;
	}

	/**
	 * Sets the mode.
	 *
	 * @param mode the new mode
	 */
	public void setMode(PicMode mode)
	{
		this.mode = mode;
	}

	public void setPicArrow(PicArrowDirection direction)
	{
		try
		{
			rs.sendArrowToPic(direction);
		}
		catch (SerialPortException ex)
		{
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
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
						this.setLatitude(Mathematics.picLat2Lat(Double.parseDouble(locComponents[0]), locComponents[1].charAt(0)));
					if(!locComponents[2].isEmpty() && !locComponents[3].isEmpty())
						this.setLongitude(Mathematics.picLon2Lon(Double.parseDouble(locComponents[2]), locComponents[3].charAt(0)));
					break;
				case ACCELEROMETER_UPDATE:
				case MAGNETOMETER_UPDATE:
					String[] components = commande.getDatas().split(",", 3);
					int[] values = new int[3];
					for (int l_i = 0; l_i < components.length; l_i++)
						values[l_i] = Integer.parseInt(components[l_i]);

					if (commande.getCommandNumber() == RS232CommandType.ACCELEROMETER_UPDATE)
						acc = values.clone();
					else if (commande.getCommandNumber() == RS232CommandType.MAGNETOMETER_UPDATE)
						mag = values.clone();
					
					double[] res = new double[3];
					Mathematics.calculateAngles(res, acc, mag);
					azimuth = res[0];
					pitch = res[1] * -1.0;
					roll = res[2];
					break;
				case PIC_STATUS:
					log.info("Error received from the Pic : " + commande.getDatas());
					break;
				default:
					log.severe("Unknown command number received");
					break;
			}
			
		updateObservateur();
		}
	}
	
	/**
	 * Ajoute un observateur à la liste
	 */
	public void addObservateur(Observateur obs) {
		this.listObservateur.add(obs);
	}
	/**
	 * Retire tous les observateurs de la liste
	 */
	public void delObservateur() {
		this.listObservateur = new ArrayList<Observateur>();
	}
	/**
	 * Avertit les observateurs que l'observable a changé 
	 * et invoque la méthode update de chaque observateur !
	 */
	public void updateObservateur() {
		for(Observateur obs : this.listObservateur )
			obs.update();
	}
}
