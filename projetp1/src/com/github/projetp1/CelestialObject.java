package com.github.projetp1;

/**
 * @author   Diego Antognini
 */
public class CelestialObject {


	private int id;
	private int StarId;
	private int HIP;
	private int HD;
	private int HR;
	private int Gliese;
	private int BayerFlamsteed;
	private String ProperName;
	private double RA;
	private double Dec;
	private double Distance;
	private double PMRA;
	private double PMDec;
	private double RV;
	private double Mag;
	private double AbsMag;
	private String Spectrum;
	private double ColorIndex;
	private double xyz[] = new double[3];
	private double vxyz[] = new double[3];

	public CelestialObject(int _id,int _StarId,int _HIP,int _HD,int _HR,int _Gliese,int _BayerFlamsteed,String _ProperName,double _RA,double _Dec, double _Distance, double _PMRA,double _PMDec,double _RV,double _Mag
			,double _AbsMag,String _Spectrum,double _ColorIndex,double _x,double _y,double _z,double _vx,double _vy,double _vz) {
		
		this.id = _id;
		this.StarId = _StarId;
		this.HIP = _HIP;
		this.HR = _HR;
		this.Gliese = _Gliese;
		this.BayerFlamsteed = _BayerFlamsteed;
		this.ProperName = _ProperName;
		this.RA = _RA;
		this.Dec = _Dec;
		this.Distance = _Distance;
		this.PMRA = _PMRA;
		this.PMDec = _PMDec;
		this.RV = _RV;
		this.Mag = _Mag;
		this.AbsMag = _AbsMag;
		this.Spectrum = _Spectrum;
		this.ColorIndex = _ColorIndex;
		this.xyz[0] = _x;
		this.xyz[1] = _y;
		this.xyz[2] = _z;
		this.vxyz[0] = _vx;
		this.vxyz[1] = _vy;
		this.vxyz[2] = _vz;	
	}

	public void getHeader()
	{
		System.out.println("ID\tStarID\tHIP\tHD\tHR\tGliese\tBayerFlamsteed\tProperName\tRA\tDec\tDistance\tPMRA\tPMDec\tRV\tMag\tAbsMag\tSpectrum\tColorIndex\tX\tY\tZ\tVX\tVY\tVZ");
	}
	public void getInfos()
	{
		System.out.println(this.id + "\t" + this.StarId+ "\t" + this.HIP+ "\t" + this.HD + "\t" + this.HR + "\t" + this.Gliese + "\t" + this.BayerFlamsteed + "\t\t" + this.ProperName + "\t\t" + 
				this.RA + "\t" + this.Dec + "\t" + this.Distance + "\t" + this.PMRA + "\t" + this.PMDec + "\t" + this.RV + "\t" + this.Mag + "\t" + this.AbsMag + "\t" + this.Spectrum + "\t\t" + 
				this.ColorIndex + "\t\t" + this.xyz[0] + "\t" + this.xyz[1] + "\t" + this.xyz[2] + "\t" + this.vxyz[0] + "\t" + this.vxyz[1] + "\t" + this.vxyz[2]);

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
		return this.ProperName;
	}

	public double getRA() {
		return this.RA;
	}

	public double getDec() {
		return this.Dec;
	}

	public double getDistance() {
		return this.Distance;
	}

	public double getPMRA() {
		return this.PMRA;
	}

	public double getPMDec() {
		return this.PMDec;
	}

	public double getRV() {
		return this.RV;
	}

	public double getMag() {
		return this.Mag;
	}

	public double getAbsMag() {
		return this.AbsMag;
	}

	public String getSpectrum() {
		return this.Spectrum;
	}

	public double getColorIndex() {
		return this.ColorIndex;
	}

	public double[] getXyz() {
		return this.xyz;
	}

	public double[] getVxyz() {
		return this.vxyz;
	}
	
}
