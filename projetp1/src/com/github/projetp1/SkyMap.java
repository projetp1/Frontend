/**
 * 
 */
package com.github.projetp1;

import java.util.ArrayList;

/**
 * @author alexandr.perez
 *
 */
public class SkyMap {

	private int zoom;
	private double longitude;
	private double latitude;
	private ArrayList<CelestialObject> arrayObject;
	
	/**
	 * 
	 */
	public SkyMap(int _zoom) {
		// TODO Auto-generated constructor stub
		this.setZoom(_zoom);
	}

	public void updateSkyMap() {
		
	}
	
	public void updateArrow(CelestialObject _object) {
		
	}

	public int getZoom() {
		return zoom;
	}

	public void setZoom(int _zoom) {
		this.zoom = _zoom;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double _latitude) {
		this.latitude = _latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double _longitude) {
		this.longitude = _longitude;
	}

	public ArrayList<CelestialObject> getArrayObject() {
		return arrayObject;
	}

	public void setArrayObject(ArrayList<CelestialObject> _arrayObject) {
		this.arrayObject = _arrayObject;
	}
}
