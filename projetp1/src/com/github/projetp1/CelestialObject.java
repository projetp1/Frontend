 /**=====================================================================*
 | This file declares the following classes:
 |    CelestialObject
 |
 | Description of the CelestialObject:
 |	  Use to have a celestial object with all its informations.	
 |
 | <p>Copyright : EIAJ, all rights reserved</p>
 | @autor : Diego Antognini
 | @version : 1.0
 |
 |
 *=======================================================================*/

package com.github.projetp1;

import java.util.logging.Logger;

public class CelestialObject {

	private Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private int id;
	private int HIP;
	private int HD;
	private int HR;
	private String sProperName;
	private double dRA;
	private double dDec;
	private double dDistance;
	private double dHeight;
	private double dAzimuth;
	private double dMag;
	private double dColorIndex;
	private double dXReal;
	private double dYReal;

	/**
	 * CelestialObject
	 * Constructor
	 * @param _id : The id of the celestial object
	 * @param _HIP
	 * @param _HD : The star's ID in the Henry Draper catalog, if known.
	 * @param _HR : The star's ID in the Harvard Revised catalog, which is the same as its number in the Yale Bright Star Catalog.
	 * @param _sProperName : A common name for the star, such as "Barnard's Star" or "Sirius". I have taken these names primarily from the Hipparcos project's web site, which lists representative names for the 150 brightest stars and many of the 150 closest stars. I have added a few names to this list. Most of the additions are designations from catalogs mostly now forgotten (e.g., Lalande, Groombridge, and Gould ["G."]) except for certain nearby stars which are still best known by these designations.
	 * @param _dRA : The star's right ascension for epoch 2000.0
	 * @param _dDec : The star's declination for epoch 2000.0
	 * @param _dDistance : The star's distance in parsecs, the most common unit in astrometry. To convert parsecs to light years, multiply by 3.262. A value of 10000000 indicates missing or dubious (e.g., negative) parallax data in Hipparcos.
	 * @param _dMag : The star's apparent visual magnitude.
	 * @param _dColorIndex : The star's color index (blue magnitude - visual magnitude), where known.
	 */
	public CelestialObject(int _id,int _HIP,int _HD,int _HR,String _sProperName,double _dRA,double _dDec, double _dDistance,double _dMag,double _dColorIndex) {
	
		this.id = _id;
		this.HIP = _HIP;
		this.HR = _HR;
		this.sProperName = _sProperName;
		this.dRA = _dRA;
		this.dDec = _dDec;
		this.dDistance = _dDistance;
		this.dMag = _dMag;
		this.dColorIndex = _dColorIndex;
	}
	
	/**
	 * getId
	 * getter of the private value
	 * @return the private variable
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * getHIP
	 * getter of the private value
	 * @return the private variable
	 */
	public int getHIP() {
		return this.HIP;
	}

	/**
	 * getHD
	 * getter of the private value
	 * @return the private variable
	 */
	public int getHD() {
		return this.HD;
	}
	
	/**
	 * getHR
	 * getter of the private value
	 * @return the private variable
	 */
	public int getHR() {
		return this.HR;
	}

	/**
	 * getProperName
	 * getter of the private value
	 * @return the private variable
	 */
	public String getProperName() {
		return this.sProperName;
	}

	/**
	 * getRA
	 * getter of the private value
	 * @return the private variable
	 */
	public double getRA() {
		return this.dRA;
	}

	/**
	 * getDec
	 * getter of the private value
	 * @return the private variable
	 */
	public double getDec() {
		return this.dDec;
	}

	/**
	 * getDistance
	 * getter of the private value
	 * @return the private variable
	 */
	public double getDistance() {
		return this.dDistance;
	}

	/**
	 * getMag
	 * getter of the private value
	 * @return the private variable
	 */
	public double getMag() {
		return this.dMag;
	}

	/**
	 * getColorIndex
	 * getter of the private value
	 * @return the private variable
	 */
	public double getColorIndex() {
		return this.dColorIndex;
	}
	
	/**
	 * getHeight
	 * getter of the private value
	 * @return the private variable
	 */
	public double getHeight() {
		return this.dHeight;
	}

	/**
	 * getAzimuth
	 * getter of the private value
	 * @return the private variable
	 */
	public double getAzimuth() {
		return this.dAzimuth;
	}
	
	/**
	 * getXReal
	 * getter of the private value
	 * @return the private variable
	 */
	public double getXReal() {
		return this.dXReal;
	}

	/**
	 * getYReal
	 * getter of the private value
	 * @return the private variable
	 */
	public double getYReal() {
		return this.dYReal;
	}
	
	/**
	 * setXReal
	 * setter of the private value
	 */
	public void setXReal(double _x) {
		this.dXReal = _x;
	}
	
	/**
	 * setYReal
	 * setter of the private value
	 */
	public void setYReal(double _y) {
		this.dYReal = _y;
	}
	
	/**
	 * setRA
	 * setter of the private value
	 */
	public void setRA(double _y) {
		this.dRA = _y;
	}
	
	/**
	 * setDec
	 * setter of the private value
	 */
	public void setDec(double _y) {
		this.dDec = _y;
	}
	
	/**
	 * setMag
	 * setter of the private value
	 */
	public void setMag(double _x) {
		this.dMag = _x;
	}
	
	/**
	 * setHeight
	 * setter of the private value
	 */
	public void SetHeight(double _x) {
		this.dHeight = _x;
	}

	/**
	 * setAzimuth
	 * setter of the private value
	 */
	public void SetAzimuth(double _x) {
		this.dAzimuth = _x;
	}
}