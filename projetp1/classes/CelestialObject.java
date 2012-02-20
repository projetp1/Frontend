package com.github.projetp1;

/**
 * @author   Diego Antognini
 */
public class CelestialObject {


	private int id;
	private int StartID;
	private int HIP;
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

	public CelestialObject() {
		
	}

	
	
	
	
	
	
	
	
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStartID() {
		return this.StartID;
	}

	public void setStartID(int startID) {
		StartID = startID;
	}

	public int getHIP() {
		return this.HIP;
	}

	public void setHIP(int hIP) {
		HIP = hIP;
	}

	public int getHR() {
		return this.HR;
	}

	public void setHR(int hR) {
		HR = hR;
	}

	public int getBayerFlamsteed() {
		return this.BayerFlamsteed;
	}

	public void setBayerFlamsteed(int bayerFlamsteed) {
		BayerFlamsteed = bayerFlamsteed;
	}

	public int getGliese() {
		return this.Gliese;
	}

	public void setGliese(int gliese) {
		Gliese = gliese;
	}

	public String getProperName() {
		return this.ProperName;
	}

	public void setProperName(String properName) {
		ProperName = properName;
	}

	public double getRA() {
		return this.RA;
	}

	public void setRA(double rA) {
		RA = rA;
	}

	public double getDec() {
		return this.Dec;
	}

	public void setDec(double dec) {
		Dec = dec;
	}

	public double getDistance() {
		return this.Distance;
	}

	public void setDistance(double distance) {
		Distance = distance;
	}

	public double getPMRA() {
		return this.PMRA;
	}

	public void setPMRA(double pMRA) {
		PMRA = pMRA;
	}

	public double getPMDec() {
		return this.PMDec;
	}

	public void setPMDec(double pMDec) {
		PMDec = pMDec;
	}

	public double getRV() {
		return this.RV;
	}

	public void setRV(double rV) {
		RV = rV;
	}

	public double getMag() {
		return this.Mag;
	}

	public void setMag(double mag) {
		Mag = mag;
	}

	public double getAbsMag() {
		return this.AbsMag;
	}

	public void setAbsMag(double absMag) {
		AbsMag = absMag;
	}

	public String getSpectrum() {
		return this.Spectrum;
	}

	public void setSpectrum(String spectrum) {
		Spectrum = spectrum;
	}

	public double getColorIndex() {
		return this.ColorIndex;
	}

	public void setColorIndex(double colorIndex) {
		ColorIndex = colorIndex;
	}

	public double[] getXyz() {
		return this.xyz;
	}

	public void setXyz(double xyz[]) {
		this.xyz = xyz;
	}

	public double[] getVxyz() {
		return this.vxyz;
	}

	public void setVxyz(double vxyz[]) {
		this.vxyz = vxyz;
	}
	

}
