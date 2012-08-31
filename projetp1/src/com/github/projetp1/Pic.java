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

import com.github.projetp1.rs232.RS232;
import com.github.projetp1.rs232.RS232.PicArrowDirection;
import com.github.projetp1.rs232.RS232Command;
import com.github.projetp1.rs232.RS232CommandType;

/**
 * The Class Pic.
 */
public class Pic extends Thread implements Observer
{

	/** The list of observateur. */
	private ArrayList<Observateur> listObservateur = new ArrayList<Observateur>();

	/** The longitude received from the GPS. */
	private double longitude = 0.0;
	
	/** The latitude received from the GPS. */
	private double latitude = 0.0;

	/** The azimuth angle. */
	private double azimuth = 0.0;
	
	/** The pitch angle. */
	private double pitch = 90.0;
	
	/** The roll angle. */
	private double roll = 0.0;
	
	/** The time (in ms) when the latest smooth was performed. */
	private long lastSmooth = 0;
	
	/** The time in ms after which it isn't relevant to smooth the values anymore. */
	private final int SMOOTH_AGE = 600;

	/** The 3 vectors of the accelerometer. */
	private int[] acc = new int[3];
	
	/** The 3 vectors of the magnetometer. */
	private int[] mag = new int[3];

	// Déclinaison magnétique pour l'endroit en cours
	/** The magnetic declination at the current location. */
	float magneticDeclination = 0.0f;
	
	/** True when the magnetic declination has been computed. */
	boolean magSet = false;

	/** The mode in which the PIC is currently operating. */
	private PicMode mode = PicMode.SIMULATION;

	/** The log. */
	private Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	/**
	 * The different modes in which the PIC may operate.
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

	/** The RS232 object. */
	private RS232 rs;

	/**
	 * Instantiates a new PIC.
	 * 
	 * @param _mainview
	 *            The MainView class of the program
	 */
	public Pic(MainView _mainview)
	{
		this.mainview = _mainview;

		try
		{
			rs = new RS232(mainview.getSettings(), this);
		}
		catch (SerialPortException ex)
		{
			ex.printStackTrace();
		}
		catch (Exception ex)
		{
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
	 * @param _longitude
	 *            the new longitude
	 */
	protected void setLongitude(double _longitude)
	{
		if (Math.abs(_longitude) <= 180)
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
	 * @param _latitude
	 *            the new latitude
	 */
	protected void setLatitude(double _latitude)
	{
		if (Math.abs(_latitude) <= 90)
			this.latitude = _latitude;
	}

	/**
	 * Gets the angle.
	 * 
	 * @return the angle
	 * @deprecated
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
	 * @deprecated
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
	 * @param mode
	 *            the new mode
	 */
	public void setMode(PicMode mode)
	{
		this.mode = mode;
	}

	/**
	 * Sets the PIC arrow. It also make the PIC operate in GUIDING mode.
	 * 
	 * @param direction
	 *            the new pic arrow
	 */
	public void setPicArrow(PicArrowDirection direction)
	{
		try
		{
			rs.sendArrowToPic(direction);
			this.setMode(PicMode.GUIDING);
		}
		catch (SerialPortException ex)
		{
			ex.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
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
					if (!locComponents[0].isEmpty() && !locComponents[1].isEmpty())
						this.setLatitude(Mathematics.picLat2Lat(
								Double.parseDouble(locComponents[0]), locComponents[1].charAt(0)));
					if (!locComponents[2].isEmpty() && !locComponents[3].isEmpty())
						this.setLongitude(Mathematics.picLon2Lon(
								Double.parseDouble(locComponents[2]), locComponents[3].charAt(0)));

					if (!magSet)
					{
						magneticDeclination = (new GeomagneticField((float) latitude,
								(float) longitude, 200, System.currentTimeMillis())
								.getDeclination());
						magSet = true;
					}
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
					
					long currentTime = System.currentTimeMillis();
					
					double l_azimuth = res[0] + magneticDeclination;
					double l_pitch = res[1] * -1.0;
					double l_roll = res[2];
					
					// Si le dernier smooth est récent, on continue
					if((currentTime - lastSmooth) <= SMOOTH_AGE)
					{
						azimuth = Mathematics.smooth(l_azimuth, azimuth, 0.5, 40);
						pitch = Mathematics.smooth(l_pitch, pitch, 0.5, 25);
						roll = Mathematics.smooth(l_roll, roll, 0.5, 25);
						lastSmooth = currentTime;
					}
					else
					{
						azimuth = l_azimuth;
						pitch = l_pitch;
						roll = l_roll;
					}

					log.info("A: " + res[0] + "\nP: " + res[1] + "\nR: " + res[2]);
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
	 * Ajoute un observateur à la liste.
	 *
	 * @param obs the obs
	 */
	@Override
	public void addObservateur(Observateur obs)
	{
		this.listObservateur.add(obs);
	}

	/**
	 * Retire tous les observateurs de la liste.
	 */
	@Override
	public void delObservateur()
	{
		this.listObservateur = new ArrayList<Observateur>();
	}

	/**
	 * Averti les observateurs que l'observable a changé et invoque la méthode update de chaque
	 * observateur !.
	 */
	@Override
	public void updateObservateur()
	{
		for (Observateur obs : this.listObservateur)
			obs.updatePIC();
	}
}
