/**
 * 
 */
package com.github.projetp1;

/**
 * @author   alexandr.perez
 */

import java.util.concurrent.ConcurrentLinkedQueue;

import com.github.projetp1.rs232.RS232Command;

public class Pic extends Thread{

	/**
	 * @uml.property  name="longitude"
	 */
	private double longitude;
	/**
	 * @uml.property  name="latitude"
	 */
	private double latitude;
	/**
	 * @uml.property  name="angle"
	 */
	private double angle;
	/**
	 * @uml.property  name="compass"
	 */
	private double compass;
	
	private PicMode mode;
	
	public enum PicMode {
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
		SIMULATION
		; }
	
	private com.github.projetp1.rs232.RS232Command commande;
	
	protected MainView mainview;
	
	private com.github.projetp1.rs232.RS232 rs = new com.github.projetp1.rs232.RS232(mainview);
	
	/**
	 * 
	 */
	public Pic(MainView _mainview) {
		// TODO Auto-generated constructor stub
		this.mainview = _mainview;
		
		this.start();
	}

	/**
	 * @return
	 * @uml.property  name="longitude"
	 */
	public double getLongitude () {
		return longitude;
	}
	
	/**
	 * @param  _longitude
	 * @uml.property  name="longitude"
	 */
	public void setLongitude (double _longitude) {
		
	}
	
	/**
	 * @return
	 * @uml.property  name="latitude"
	 */
	public double getLatitude () {
		return latitude;
	}
	
	/**
	 * @param  _latitude
	 * @uml.property  name="latitude"
	 */
	public void setLatitude (double _latitude) {
		
	}
	
	/**
	 * @return
	 * @uml.property  name="angle"
	 */
	public double getAngle () {
		return angle;
	}
	
	/**
	 * @param  _angle
	 * @uml.property  name="angle"
	 */
	public void setAngle (double _angle) {
		
	}
	
	/**
	 * @return
	 * @uml.property  name="compass"
	 */
	public double getCompass () {
		return compass;
	}
	
	/**
	 * @param  _compass
	 * @uml.property  name="compass"
	 */
	public void setCompass (double _compass) {
		
	}

	public PicMode getMode() {
		return mode;
	}

	public void setMode(PicMode mode) {
		this.mode = mode;
	}
	public void run()
	{
		//Reception des données
		commande = rs.getLastCommand();
		
		//Switch pour trier les données
		switch(commande.getCommandNumber())
		{
			case EMPTY :
				break;
			case CHANGE_TO_POINT_MODE:
				break;
			case CHANGE_TO_ARROW_MODE:
				break;
			case LOCATION_UPDATE:
				break;
			case ACCELEROMETER_UPDATE:
				break;
			case MAGNETOMETER_UPDATE:
				break;
			case PIC_STATUS:
				break;
			default:
				break;
		}
	}
}
