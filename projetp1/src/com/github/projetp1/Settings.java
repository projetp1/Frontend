package com.github.projetp1;

import java.io.*;


/**
 * @author   alexandr.perez
 */
public class Settings implements Serializable {

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
	 * @uml.property  name="samplingRate"
	 */
	private int samplingRate;
	/**
	 * @uml.property  name="databaseName"
	 */
	private String databaseName;
	/**
	 * @uml.property  name="inputDelimiter"
	 */
	private String inputDelimiter;
	/**
	 * @uml.property  name="simulation"
	 */
	private Boolean simulation;

	public Settings() 
	{
		// TODO Auto-generated constructor stub
		loadFromFile();
	}
	
	private void  loadFromFile() 
	{
		String filename = "settings.lol";
		Serializer serializer = new Serializer();
		Settings deserialize =(Settings)serializer.deserialize(filename);
		if(deserialize != null) 
		{			
			
			this.port = deserialize.port;
			this.speed = deserialize.speed;	
			this.databit = deserialize.databit;
			this.stopbit = deserialize.stopbit;
			this.parity = deserialize.parity;
			this.flowControl = deserialize.flowControl;
			this.samplingRate = deserialize.samplingRate;
			this.databaseName = deserialize.databaseName;
			this.inputDelimiter = deserialize.inputDelimiter;
			this.simulation = deserialize.simulation;
		} 
		else 
		{
			this.port = jssc.SerialPortList.getPortNames()[0]; //1er port RS232 de la machine
			this.speed = 9600;	
			this.databit = 8;
			this.stopbit = 1;
			this.parity = "none";
			this.flowControl = "none";
			this.samplingRate = 25;
			this.databaseName = "hyg.db";
			this.inputDelimiter = ";";
			this.simulation = false;
		}
	}
	
	/**
	 * @return
	 * @uml.property  name="port"
	 */
	public String getPort () {
		return port;
	}
	
	/**
	 * @param  _port
	 * @uml.property  name="port"
	 */
	public void setPort (String _port) {
		this.port = _port;
	}

	/**
	 * @return
	 * @uml.property  name="speed"
	 */
	public int getSpeed () {
		return speed;
	}
	
	/**
	 * @param  _speed
	 * @uml.property  name="speed"
	 */
	public void setSpeed (int _speed) {
		this.speed = _speed;
	}
	
	/**
	 * @return
	 * @uml.property  name="databit"
	 */
	public int getDatabit () {
		return databit;
	}
	
	/**
	 * @param  _databit
	 * @uml.property  name="databit"
	 */
	public void setDatabit (int _databit) {
		this.databit = _databit;
	}
	
	/**
	 * @return
	 * @uml.property  name="stopbit"
	 */
	public int getStopbit () {
		return stopbit;
	}
	
	/**
	 * @param  _stopbit
	 * @uml.property  name="stopbit"
	 */
	public void setStopbit (int _stopbit) {
		this.stopbit = _stopbit;
	}
	
	/**
	 * @return
	 * @uml.property  name="parity"
	 */
	public String getParity () {
		return parity;
	}
	
	/**
	 * @param  _parity
	 * @uml.property  name="parity"
	 */
	public void setParity (String _parity) {
		this.parity = _parity;
	}
	
	/**
	 * @return
	 * @uml.property  name="flowControl"
	 */
	public String getFlowControl () {
		return flowControl;
	}
	
	/**
	 * @param  _flowControl
	 * @uml.property  name="flowControl"
	 */
	public void setFlowControl (String _flowControl) {
		this.flowControl = _flowControl;
	}
	
	/**
	 * @return
	 * @uml.property  name="simulation"
	 */
	public Boolean getSimulation () {
		return simulation;
	}
	
	/**
	 * @param  _simulation
	 * @uml.property  name="simulation"
	 */
	public void setSimulation (Boolean _simulation) {
		this.simulation = _simulation;
	}
	
	public int getSamplingRate() {
		return samplingRate;
	}

	public void setSamplingRate(int _samplingRate) {
		this.samplingRate = _samplingRate;
	}
	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getInputDelimiter() {
		return inputDelimiter;
	}

	public void setInputDelimiter(String inputDelimiter) {
		this.inputDelimiter = inputDelimiter;
	}
}