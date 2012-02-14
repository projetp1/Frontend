/**
 * 
 */
package com.github.projetp1;

import java.util.ArrayList;

/**
 * @author   alexandr.perez
 */
public class SkyMap {

	/**
	 * @uml.property  name="zoom"
	 */
	private int zoom;
	/**
	 * @uml.property  name="longitude"
	 */
	private double longitude;
	/**
	 * @uml.property  name="latitude"
	 */
	private double latitude;
	/**
	 * @uml.property  name="arrayObject"
	 */
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

	/**
	 * @return
	 * @uml.property  name="zoom"
	 */
	public int getZoom() {
		return zoom;
	}

	/**
	 * @param  _zoom
	 * @uml.property  name="zoom"
	 */
	public void setZoom(int _zoom) {
		this.zoom = _zoom;
	}

	/**
	 * @return
	 * @uml.property  name="latitude"
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param  _latitude
	 * @uml.property  name="latitude"
	 */
	public void setLatitude(double _latitude) {
		this.latitude = _latitude;
	}

	/**
	 * @return
	 * @uml.property  name="longitude"
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param  _longitude
	 * @uml.property  name="longitude"
	 */
	public void setLongitude(double _longitude) {
		this.longitude = _longitude;
	}

	/**
	 * @return
	 * @uml.property  name="arrayObject"
	 */
	public ArrayList<CelestialObject> getArrayObject() {
		return arrayObject;
	}

	/**
	 * @param  _arrayObject
	 * @uml.property  name="arrayObject"
	 */
	public void setArrayObject(ArrayList<CelestialObject> _arrayObject) {
		this.arrayObject = _arrayObject;
	}
}
