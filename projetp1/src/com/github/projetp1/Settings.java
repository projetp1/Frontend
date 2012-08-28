package com.github.projetp1;

import java.io.Serializable;

/**
 * @author alexandr.perez
 */
public class Settings implements Serializable
{
	private static final long serialVersionUID = -8939567320279249059L;

	private String port;
	private int speed;
	private int databit;
	private int stopbit;
	private int parity;
	private int flowControl;
	private boolean simulation;

	public Settings()
	{
		loadFromFile();
	}

	private void loadFromFile()
	{
		String filename = "settings.conf";
		Settings deserialize = (Settings) Serializer.deserialize(filename);
		if (deserialize != null)
		{

			this.port = deserialize.port;
			this.speed = deserialize.speed;
			this.databit = deserialize.databit;
			this.stopbit = deserialize.stopbit;
			this.parity = deserialize.parity;
			this.flowControl = deserialize.flowControl;
			this.simulation = deserialize.simulation;
		}
		else
		{
			if (jssc.SerialPortList.getPortNames().length > 0)
				this.port = jssc.SerialPortList.getPortNames()[0]; // 1er port RS232 de la machine
			this.speed = jssc.SerialPort.BAUDRATE_9600;
			this.databit = jssc.SerialPort.DATABITS_8;
			this.stopbit = jssc.SerialPort.STOPBITS_1;
			this.parity = jssc.SerialPort.PARITY_NONE;
			this.flowControl = jssc.SerialPort.FLOWCONTROL_NONE;
			this.simulation = false;

			Serializer.serialize("settings.conf", this);
		}
	}

	public String getPort()
	{
		return port;
	}
	
	public static String[] getPortList()
	{
		return jssc.SerialPortList.getPortNames();
	}

	public void setPort(String _port)
	{
		for (String existingPort : jssc.SerialPortList.getPortNames())
		{
			if(existingPort.equals(_port))
				this.port = _port;
		}
	}

	public int getSpeed()
	{
		return speed;
	}
	
	public static int[] getSpeedList()
	{
		return new int[] {
			jssc.SerialPort.BAUDRATE_110,
		    jssc.SerialPort.BAUDRATE_300,
		    jssc.SerialPort.BAUDRATE_600,
		    jssc.SerialPort.BAUDRATE_1200,
		    jssc.SerialPort.BAUDRATE_4800,
		    jssc.SerialPort.BAUDRATE_9600,
		    jssc.SerialPort.BAUDRATE_14400,
		    jssc.SerialPort.BAUDRATE_19200,
		    jssc.SerialPort.BAUDRATE_38400,
		    jssc.SerialPort.BAUDRATE_57600,
		    jssc.SerialPort.BAUDRATE_115200,
		    jssc.SerialPort.BAUDRATE_128000,
		    jssc.SerialPort.BAUDRATE_256000
		    };
	}

	public void setSpeed(int _speed)
	{
		for (int l_i : getSpeedList())
		{
			if(l_i == _speed)
				this.speed = _speed;
		}
	}

	public int getDatabit()
	{
		return databit;
	}
	
	public static int[] getDatabitList()
	{
		return new int[] {
				jssc.SerialPort.DATABITS_5,
				jssc.SerialPort.DATABITS_6,
				jssc.SerialPort.DATABITS_7,
				jssc.SerialPort.DATABITS_8
		};
	}

	public void setDatabit(int _databit)
	{
		for (int l_i : getDatabitList())
		{
			if(l_i == _databit)
				this.databit = _databit;
		}
	}

	public int getStopbit()
	{
		return stopbit;
	}
	
	public static int[] getStopbitList()
	{
		return new int[] {
				jssc.SerialPort.STOPBITS_1,
				jssc.SerialPort.STOPBITS_1_5,
				jssc.SerialPort.STOPBITS_2
		};
	}

	public void setStopbit(int _stopbit)
	{
		for (int l_i : getStopbitList())
		{
			if(l_i == _stopbit)
				this.stopbit = _stopbit;
		}
	}

	public int getParity()
	{
		return parity;
	}
	
	public static int[] getParityList()
	{
		return new int[] {
				jssc.SerialPort.PARITY_NONE,
				jssc.SerialPort.PARITY_ODD,
				jssc.SerialPort.PARITY_EVEN,
				jssc.SerialPort.PARITY_MARK,
				jssc.SerialPort.PARITY_SPACE
		};
	}

	public void setParity(int _parity)
	{
		for(int l_i : getParityList())
		{
			if(l_i == _parity)
				this.parity = _parity;
		}
	}

	public int getFlowControl()
	{
		return flowControl;
	}
	
	public static int[] getFlowControlList()
	{
		return new int[] {
				jssc.SerialPort.FLOWCONTROL_NONE,
				jssc.SerialPort.FLOWCONTROL_RTSCTS_IN,
				jssc.SerialPort.FLOWCONTROL_RTSCTS_OUT,
				jssc.SerialPort.FLOWCONTROL_XONXOFF_IN,
				jssc.SerialPort.FLOWCONTROL_XONXOFF_OUT
		};
	}

	public void setFlowControl(int _flowControl)
	{
		for(int l_i : getFlowControlList())
		{
			if(l_i == _flowControl)
				this.flowControl = _flowControl;
		}
	}

	public Boolean getSimulation()
	{
		return simulation;
	}

	public void setSimulation(boolean _simulation)
	{
		this.simulation = _simulation;
	}
}