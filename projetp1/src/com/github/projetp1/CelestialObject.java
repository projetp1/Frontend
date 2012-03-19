 /**=====================================================================*
 | This file dDeclares the following classes:
 |    CelestialObject
 |
 | Description of the CelestialObject DataBase :
 |	  Use to have an object of a celestial object with all his informations.	
 |
 | <p>Copyright : EIAJ, all rights resedRVed</p>
 | @autor : Diego Antognini
 | @version : 1.0
 |
 |
 *=====================================================================*/

package com.github.projetp1;

public class CelestialObject {


	private int id;
	private int StarId;
	private int HIP;
	private int HD;
	private int HR;
	private int Gliese;
	private int BayerFlamsteed;
	private String sProperName;
	private double dRA;
	private double dDec;
	private double dDistance;
	private double dPMRA;
	private double dPMDec;
	private double dRV;
	private double dMag;
	private double dAbsMag;
	private String sSpectrum;
	private double dColorIndex;
	private double dXYZ[] = new double[3];
	private double dVXYZ[] = new double[3];

	public CelestialObject(int _id,int _StarId,int _HIP,int _HD,int _HR,int _Gliese,int _BayerFlamsteed,String _sProperName,double _dRA,double _dDec, double _dDistance, double _dPMRA,double _dPMDec,double _dRV,double _dMag
			,double _dAbsMag,String _sSpectrum,double _dColorIndex,double _x,double _y,double _z,double _vx,double _vy,double _vz) {
		
		this.id = _id;
		this.StarId = _StarId;
		this.HIP = _HIP;
		this.HR = _HR;
		this.Gliese = _Gliese;
		this.BayerFlamsteed = _BayerFlamsteed;
		this.sProperName = _sProperName;
		this.dRA = _dRA;
		this.dDec = _dDec;
		this.dDistance = _dDistance;
		this.dPMRA = _dPMRA;
		this.dPMDec = _dPMDec;
		this.dRV = _dRV;
		this.dMag = _dMag;
		this.dAbsMag = _dAbsMag;
		this.sSpectrum = _sSpectrum;
		this.dColorIndex = _dColorIndex;
		this.dXYZ[0] = _x;
		this.dXYZ[1] = _y;
		this.dXYZ[2] = _z;
		this.dVXYZ[0] = _vx;
		this.dVXYZ[1] = _vy;
		this.dVXYZ[2] = _vz;	
	}

	public void getHeader()
	{
		System.out.println("ID\tStarID\tHIP\tHD\tHR\tGliese\tBayerFlamsteed\tProperName\tRA\tDec\tDistance\tPMRA\tPMDec\tRV\tMag\tAbsMag\tSpectrum\tColorIndex\tX\tY\tZ\tVX\tVY\tVZ");
	}
	public void getInfos()
	{
		System.out.println(this.id + "\t" + this.StarId+ "\t" + this.HIP+ "\t" + this.HD + "\t" + this.HR + "\t" + this.Gliese + "\t" + this.BayerFlamsteed + "\t\t" + this.sProperName + "\t\t" + 
				this.dRA + "\t" + this.dDec + "\t" + this.dDistance + "\t" + this.dPMRA + "\t" + this.dPMDec + "\t" + this.dRV + "\t" + this.dMag + "\t" + this.dAbsMag + "\t" + this.sSpectrum + "\t\t" + 
				this.dColorIndex + "\t\t" + this.dXYZ[0] + "\t" + this.dXYZ[1] + "\t" + this.dXYZ[2] + "\t" + this.dVXYZ[0] + "\t" + this.dVXYZ[1] + "\t" + this.dVXYZ[2]);

	}
	
	public int getId() {
		return this.id;
	}

	public int getStarId() {
		return this.StarId;
	}

	public int getHIP() {
		return this.HIP;
	}

	public int getHD() {
		return this.HD;
	}
	
	public int getHR() {
		return this.HR;
	}

	public int getBayerFlamsteed() {
		return this.BayerFlamsteed;
	}

	public int getGliese() {
		return this.Gliese;
	}

	public String getProperName() {
		return this.sProperName;
	}

	public double getRA() {
		return this.dRA;
	}

	public double getDec() {
		return this.dDec;
	}

	public double getDistance() {
		return this.dDistance;
	}

	public double getPMRA() {
		return this.dPMRA;
	}

	public double getPMDec() {
		return this.dPMDec;
	}

	public double getRV() {
		return this.dRV;
	}

	public double getMag() {
		return this.dMag;
	}

	public double getAbsMag() {
		return this.dAbsMag;
	}

	public String getSpectrum() {
		return this.sSpectrum;
	}

	public double getColorIndex() {
		return this.dColorIndex;
	}

	public double[] getXYZ() {
		return this.dXYZ;
	}

	public double[] getVXYZ() {
		return this.dVXYZ;
	}
	
}
