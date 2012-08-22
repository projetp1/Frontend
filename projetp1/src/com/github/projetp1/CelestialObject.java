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

public class CelestialObject {

	private int id;
	//private int StarId;
	private int HIP;
	private int HD;
	private int HR;
	//private int Gliese;
	//private int BayerFlamsteed;
	private String sProperName;
	private double dRA;
	private double dDec;
	private double dDistance;
	//private double dPMRA;
	//private double dPMDec;
	//private double dRV;
	private double dMag;
	//private double dAbsMag;
	//private String sSpectrum;
	private double dColorIndex;
	//private double dXYZ[] = new double[3];
	//private double dVXYZ[] = new double[3];
	private double dXReal;
	private double dYReal;

	/*
	 * CelestialObject
	 * Constructor
	 * @param _id : The id of the celestial object
	 * @param _StarId : The database primary key from a larger "master database" of stars.
	 * @param _HIP
	 * @param _HD : The star's ID in the Henry Draper catalog, if known.
	 * @param _HR : The star's ID in the Harvard Revised catalog, which is the same as its number in the Yale Bright Star Catalog.
	 * @param _Gliese : The star's ID in the third edition of the Gliese Catalog of Nearby Stars.
	 * @param _BayerFlamsteed : The Bayer / Flamsteed designation, from the Fifth Edition of the Yale Bright Star Catalog. This is a combination of the two designations. The Flamsteed number, if present, is given first; then a three-letter abbreviation for the Bayer Greek letter; the Bayer superscript number, if present; and finally, the three-letter constellation abbreviation. Thus Alpha Andromedae has the field value "21Alp And", and Kappa1 Sculptoris (no Flamsteed number) has "Kap1Scl".
	 * @param _sProperName : A common name for the star, such as "Barnard's Star" or "Sirius". I have taken these names primarily from the Hipparcos project's web site, which lists representative names for the 150 brightest stars and many of the 150 closest stars. I have added a few names to this list. Most of the additions are designations from catalogs mostly now forgotten (e.g., Lalande, Groombridge, and Gould ["G."]) except for certain nearby stars which are still best known by these designations.
	 * @param _dRA : The star's right ascension for epoch 2000.0
	 * @param _dDec : The star's declination for epoch 2000.0
	 * @param _dDistance : The star's distance in parsecs, the most common unit in astrometry. To convert parsecs to light years, multiply by 3.262. A value of 10000000 indicates missing or dubious (e.g., negative) parallax data in Hipparcos.
	 * @param _dPMRA
	 * @param _dPMDec
	 * @param _dRV
	 * @param _dMag : The star's apparent visual magnitude.
	 * @param _dAbsMag : The star's absolute visual magnitude (its apparent magnitude from a distance of 10 parsecs).
	 * @param _sSpectrum : The star's spectral type, if known.
	 * @param _dColorIndex : The star's color index (blue magnitude - visual magnitude), where known.
	 * @param _x : The Cartesian coordinates of the star, in a system based on the equatorial coordinates as seen from Earth. +X is in the direction of the vernal equinox (at epoch 2000)
	 * @param _y : The Cartesian coordinates of the star, in a system based on the equatorial coordinates as seen from Earth. +Y in the direction of R.A. 6 hours, declination 0 degrees.
	 * @param _z : The Cartesian coordinates of the star, in a system based on the equatorial coordinates as seen from Earth.+Z towards the north celestial pole
	 * @param _vx,_vy,_vz = The Cartesian velocity components of the star, in the same coordinate system described immediately above. They are determined from the proper motion and the radial velocity (when known). The velocity unit is parsecs per year; these are small values (around 10-5 to 10-6), but they enormously simplify calculations using parsecs as base units for celestial mapping.
	 */
	//public CelestialObject(int _id,int _StarId,int _HIP,int _HD,int _HR,int _Gliese,int _BayerFlamsteed,String _sProperName,double _dRA,double _dDec, double _dDistance, double _dPMRA,double _dPMDec,double _dRV,double _dMag
	//		,double _dAbsMag,String _sSpectrum,double _dColorIndex,double _x,double _y,double _z,double _vx,double _vy,double _vz) {
	
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
		//this.StarId = _StarId;
		this.HIP = _HIP;
		this.HR = _HR;
		//this.Gliese = _Gliese;
		//this.BayerFlamsteed = _BayerFlamsteed;
		this.sProperName = _sProperName;
		this.dRA = _dRA;
		this.dDec = _dDec;
		this.dDistance = _dDistance;
		//this.dPMRA = _dPMRA;
		//this.dPMDec = _dPMDec;
		//this.dRV = _dRV;
		this.dMag = _dMag;
		//this.dAbsMag = _dAbsMag;
		//this.sSpectrum = _sSpectrum;
		this.dColorIndex = _dColorIndex;
		//this.dXYZ[0] = _x;
		//this.dXYZ[1] = _y;
		//this.dXYZ[2] = _z;
		//this.dVXYZ[0] = _vx;
		//this.dVXYZ[1] = _vy;
		//this.dVXYZ[2] = _vz;	
	}

	///**
	// * getHeader
	// * Gives the header of columns from the database
	// */
	//public void getHeader()
	//{
	//	System.out.println("ID\tStarID\tHIP\tHD\tHR\tGliese\tBayerFlamsteed\tProperName\tRA\tDec\tDistance\tPMRA\tPMDec\tRV\tMag\tAbsMag\tSpectrum\tColorIndex\tX\tY\tZ\tVX\tVY\tVZ\tXReal\tYReal");
	//}
	
	/*
	 * getInfos
	 * Writes all the value of the star
	 */
	//public void getInfos()
	//{
	//	System.out.println(this.id + "\t" + this.StarId+ "\t" + this.HIP+ "\t" + this.HD + "\t" + this.HR + "\t" + this.Gliese + "\t" + this.BayerFlamsteed + "\t\t" + this.sProperName + "\t\t" + this.dRA + "\t" + this.dDec + "\t" + this.dDistance + "\t" + this.dPMRA + "\t" + this.dPMDec + "\t" + this.dRV + "\t" + this.dMag + "\t" + this.dAbsMag + "\t" + this.sSpectrum + "\t\t" + this.dColorIndex + "\t\t" + this.dXYZ[0] + "\t" + this.dXYZ[1] + "\t" + this.dXYZ[2] + "\t" + this.dVXYZ[0] + "\t" + this.dVXYZ[1] + "\t" + this.dVXYZ[2] + "\t" + this.dXReal + "\t" + this.dYReal);
	//}
	
	/**
	 * getId
	 * getter of the private value
	 * @return the private variable
	 */
	public int getId() {
		return this.id;
	}

	/*
	 * getStarId
	 * getter of the private value
	 * @return the private variable
	 */
	//public int getStarId() {
	//	return this.StarId;
	//}

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
	 * getBayerFlamsteed
	 * getter of the private value
	 * @return the private variable
	 */
	//public int getBayerFlamsteed() {
	//	return this.BayerFlamsteed;
	//}

	/*
	 * getGliese
	 * getter of the private value
	 * @return the private variable
	 */
	//public int getGliese() {
	//	return this.Gliese;
	//}

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

	/*
	 * getPMRA
	 * getter of the private value
	 * @return the private variable
	 */
	//public double getPMRA() {
	//	return this.dPMRA;
	//}

	/*
	 * getPMDec
	 * getter of the private value
	 * @return the private variable
	 */
	//public double getPMDec() {
	//	return this.dPMDec;
	//}

	/*
	 * getRV
	 * getter of the private value
	 * @return the private variable
	 */
	//public double getRV() {
	//	return this.dRV;
	//}

	/**
	 * getMag
	 * getter of the private value
	 * @return the private variable
	 */
	public double getMag() {
		return this.dMag;
	}

	/*
	 * getAbsMag
	 * getter of the private value
	 * @return the private variable
	 */
	//public double getAbsMag() {
	//	return this.dAbsMag;
	//}

	/*
	 * getSpectrum
	 * getter of the private value
	 * @return the private variable
	 */
	//public String getSpectrum() {
	//	return this.sSpectrum;
	//}

	/**
	 * getColorIndex
	 * getter of the private value
	 * @return the private variable
	 */
	public double getColorIndex() {
		return this.dColorIndex;
	}
	
	/*
	 * getXYZ
	 * getter of the private value
	 * @return the private variable
	 */
	//public double[] getXYZ() {
	//	return this.dXYZ;
	//}

	/*
	 * getVXYZ
	 * getter of the private value
	 * @return the private variable
	 */
	//public double[] getVXYZ() {
	//	return this.dVXYZ;
	//}
	
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
	public void setXReal(double x) {
		this.dXReal = x;
	}
	
	/**
	 * setYReal
	 * setter of the private value
	 */
	public void setYReal(double y) {
		this.dYReal = y;
	}
	
	/**
	 * setdRA
	 * setter of the private value
	 */
	public void setdRA(double y) {
		this.dRA = y;
	}
	
	/**
	 * setdDec
	 * setter of the private value
	 */
	public void setdDec(double y) {
		this.dDec = y;
	}
	
	/**
	 * setdMag
	 * setter of the private value
	 */
	public void setdMag(double x) {
		this.dMag = x;
	}
}