package com.github.projetp1;

import java.io.Serializable;

/**
 * @author alexandr.perez
 */
public class Settings implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6746857373082607851L;

	private String port;
	private int speed;
	private int databit;
	private int stopbit;
	private String parity;
	private String flowControl;
	private int samplingRate;
	private String databaseName;
	private String inputDelimiter;
	private boolean simulation;

	public Settings()
	{
		loadFromFile();
	}

	private void loadFromFile()
	{
		String filename = "settings.conf";
		Serializer serializer = new Serializer();
		Settings deserialize = (Settings) serializer.deserialize(filename);
		if (deserialize != null)
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
			if (jssc.SerialPortList.getPortNames().length > 0)
				this.port = jssc.SerialPortList.getPortNames()[0]; // 1er port RS232 de la machine
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

	public String getPort()
	{
		return port;
	}

	public void setPort(String _port)
	{
		this.port = _port;
	}

	public int getSpeed()
	{
		return speed;
	}

	public void setSpeed(int _speed)
	{
		this.speed = _speed;
	}

	public int getDatabit()
	{
		return databit;
	}

	public void setDatabit(int _databit)
	{
		this.databit = _databit;
	}

	public int getStopbit()
	{
		return stopbit;
	}

	public void setStopbit(int _stopbit)
	{
		this.stopbit = _stopbit;
	}

	public String getParity()
	{
		return parity;
	}

	public void setParity(String _parity)
	{
		this.parity = _parity;
	}

	public String getFlowControl()
	{
		return flowControl;
	}

	public void setFlowControl(String _flowControl)
	{
		this.flowControl = _flowControl;
	}

	public Boolean getSimulation()
	{
		return simulation;
	}

	public void setSimulation(Boolean _simulation)
	{
		this.simulation = _simulation;
	}

	public int getSamplingRate()
	{
		return samplingRate;
	}

	public void setSamplingRate(int _samplingRate)
	{
		this.samplingRate = _samplingRate;
	}

	public String getDatabaseName()
	{
		return databaseName;
	}

	public void setDatabaseName(String databaseName)
	{
		this.databaseName = databaseName;
	}

	public String getInputDelimiter()
	{
		return inputDelimiter;
	}

	public void setInputDelimiter(String inputDelimiter)
	{
		this.inputDelimiter = inputDelimiter;
	}
}