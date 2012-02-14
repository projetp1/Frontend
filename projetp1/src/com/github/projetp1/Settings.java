/**
 * 
 */
package com.github.projetp1;

import java.util.*;

/**
 * @author  alexandr.perez
 */
public class Settings {

	/**
	 * @uml.property  name="port"
	 */
	private String port;
	/**
	 * @uml.property  name="speed"
	 */
	private int speed;
	/**
	 * @uml.property  name="databit"
	 */
	private int databit;
	/**
	 * @uml.property  name="stopbit"
	 */
	private int stopbit;
	/**
	 * @uml.property  name="parity"
	 */
	private String parity;
	/**
	 * @uml.property  name="flowControl"
	 */
	private String flowControl;
	/**
	 * @uml.property  name="simulation"
	 */
	private Boolean simulation;
	
	
	/**
	 * 
	 */
	public Settings() {
		// TODO Auto-generated constructor stub
		//ICI on récupère depuis le fichier
	}
	
	public void  saveToFile() {
			
	}
	
	private void  loadFromFile() {
		
	}
	
	/**
	 * @return
	 * @uml.property  name="port"
	 */
	public String getPort () {
		return port;
	}
	
	/**
	 * @param _port
	 * @uml.property  name="port"
	 */
	public void setPort (String _port) {
		
	}
	
	/**
	 * @return
	 * @uml.property  name="speed"
	 */
	public int getSpeed () {
		return speed;
	}
	
	/**
	 * @param _speed
	 * @uml.property  name="speed"
	 */
	public void setSpeed (int _speed) {
		
	}
	
	/**
	 * @return
	 * @uml.property  name="databit"
	 */
	public int getDatabit () {
		return databit;
	}
	
	/**
	 * @param _databit
	 * @uml.property  name="databit"
	 */
	public void setDatabit (int _databit) {
		
	}
	
	/**
	 * @return
	 * @uml.property  name="stopbit"
	 */
	public int getStopbit () {
		return stopbit;
	}
	
	/**
	 * @param _stopbit
	 * @uml.property  name="stopbit"
	 */
	public void setStopbit (int _stopbit) {
		
	}
	
	/**
	 * @return
	 * @uml.property  name="parity"
	 */
	public String getParity () {
		return parity;
	}
	
	/**
	 * @param _parity
	 * @uml.property  name="parity"
	 */
	public void setParity (String _parity) {
		
	}
	
	/**
	 * @return
	 * @uml.property  name="flowControl"
	 */
	public String getFlowControl () {
		return flowControl;
	}
	
	/**
	 * @param _flowControl
	 * @uml.property  name="flowControl"
	 */
	public void setFlowControl (String _flowControl) {
		
	}
	
	/**
	 * @return
	 * @uml.property  name="simulation"
	 */
	public Boolean getSimulation () {
		return simulation;
	}
	
	/**
	 * @param _simulation
	 * @uml.property  name="simulation"
	 */
	public void setSimulation (Boolean _simulation) {
		
	}
	
	private void getFromINI () {
		
	}
	
	private void setINI () {
		
	}

}
