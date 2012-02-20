/**
 * 
 */
package com.github.projetp1;

import java.util.ArrayList;

/**
 * @author alexandr.perez
 *
 */
public class MainView {

	/**
	 * 
	 */
	public MainView() {
		// TODO Auto-generated constructor stub
		Settings set = new Settings();
		Serializer serializer = new Serializer();
		set.setPort("com4");
		serializer.serialize("settings.lol",set);
	}

	public void showHelpView() {
		
	}

	public void showSettingsView() {
		
	}
	
	public void updateCompass(double _degree) {
		
	}
	
	public void updateAngle(double _degree) {
		
	}
	
	public void setZoom(int _zoom) {
		//update skymap
	}
	
	public void updateInfo(CelestialObject _object) {
		
	}
	
	public ArrayList<CelestialObject> searchForTextInSearchField() {
		//request ddb
		//text form search field
		return null;
	}
}
