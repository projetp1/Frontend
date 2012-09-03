package com.github.projetp1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is used to access, read and write the settings of the application
 * 
 * @author sebastie.vaucher
 */
public class Settings
{
	private String port;
	private int speed;
	private int databit;
	private int stopbit;
	private int parity;
	private int flowControl;
	private double magnitude;
	private boolean constellation;
	private boolean simulation;
	
	private static final String KEY_PORT = "port";
	private static final String KEY_SPEED = "speed";
	private static final String KEY_DATABIT = "databit";
	private static final String KEY_STOPBIT = "stopbit";
	private static final String KEY_PARITY = "parity";
	private static final String KEY_FLOWCONTROL = "flowctrl";
	private static final String KEY_MAGNITUDE = "magnitude";
	private static final String KEY_CONSTELLATION = "constellation";
	private static final String KEY_SIMULATION = "simulation";
	
	private String filename = "settings.conf";
	
	private Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	/**
	 * Instantiates a new Settings object
	 *
	 * @param _filename The path of the settings file. If null, the default one will be used.
	 */
	public Settings(String _filename)
	{
		// If the filename is null, we use the default one
		if(_filename != null)
			filename = _filename;
		
		// If the settings file does not exist or loading the settings is unsuccessful,
		// we use the default settings
		if(!new File(filename).exists() || !loadFromFile())
		{
			if (jssc.SerialPortList.getPortNames().length > 0)
				this.port = jssc.SerialPortList.getPortNames()[0]; // 1er port RS232 de la machine
			this.speed = jssc.SerialPort.BAUDRATE_9600;
			this.databit = jssc.SerialPort.DATABITS_8;
			this.stopbit = jssc.SerialPort.STOPBITS_1;
			this.parity = jssc.SerialPort.PARITY_NONE;
			this.flowControl = jssc.SerialPort.FLOWCONTROL_NONE;
			this.magnitude =  6.5;
			this.constellation = false;
			this.simulation = false;
			
			saveToFile();
		}
	}

	/**
	 * Load from file.
	 *
	 * @return true, if successful
	 */
	public boolean loadFromFile()
	{
		String fileContent;
		try
		{
			fileContent = readFile(filename);
			
			JSONObject jRoot = new JSONObject(fileContent);
			
			try
			{
				port = jRoot.getString(KEY_PORT);
			}
			catch(JSONException ex)
			{
				log.info("No port defined in the settings, trying to identify the default one");
				if(jssc.SerialPortList.getPortNames().length > 0)
					port = jssc.SerialPortList.getPortNames()[0];
			}
			speed = jRoot.getInt(KEY_SPEED);
			databit = jRoot.getInt(KEY_DATABIT);
			stopbit = jRoot.getInt(KEY_STOPBIT);
			parity = jRoot.getInt(KEY_PARITY);
			flowControl = jRoot.getInt(KEY_FLOWCONTROL);
			magnitude = jRoot.getInt(KEY_MAGNITUDE);
			constellation = jRoot.getBoolean(KEY_CONSTELLATION);
			simulation = jRoot.getBoolean(KEY_SIMULATION);
		}
		catch (IOException ex)
		{
			log.warning("Impossible to read from " + filename + ":\n" + ex.getLocalizedMessage());
			return false;
		}
		catch (JSONException ex)
		{
			log.warning(filename + " is corrupted, deleting...\n" + ex.getLocalizedMessage());
			new File(filename).delete();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Save to file.
	 *
	 * @return true, if successful
	 */
	public boolean saveToFile()
	{
		JSONObject jRoot = new JSONObject();
		
		try
		{
			jRoot.put(KEY_PORT, port);
			jRoot.put(KEY_SPEED, speed);
			jRoot.put(KEY_DATABIT, databit);
			jRoot.put(KEY_STOPBIT, stopbit);
			jRoot.put(KEY_PARITY, parity);
			jRoot.put(KEY_FLOWCONTROL, flowControl);
			jRoot.put(KEY_MAGNITUDE, magnitude);
			jRoot.put(KEY_CONSTELLATION, constellation);
			jRoot.put(KEY_SIMULATION, simulation);
		}
		catch (JSONException ex)
		{
			log.severe("Erreur inconnue : " + ex.getLocalizedMessage());
			return false;
		}
		
		try
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename, false));
			bw.write(jRoot.toString(4));
			bw.close();
		}
		catch (IOException ex)
		{
			log.severe("Impossible d'écrire le fichier de configuration : " + ex.getLocalizedMessage());
			JOptionPane.showMessageDialog(null, Messages.getString("Settings.NoWriteMessage"), Messages.getString("Settings.NoWrite"), JOptionPane.WARNING_MESSAGE);
			return false;
		}
		catch (JSONException ex)
		{
			log.severe("Erreur inconnue : " + ex.getLocalizedMessage());
			return false;
		}
		
		return true;
	}
	
	/**
	 * Reads an arbitrary file given by its file path.
	 * 
	 * From : http://stackoverflow.com/a/326440/1045559
	 *
	 * @param path The path to the file
	 * @return The contents of the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static String readFile(String path) throws IOException
	{
		FileInputStream stream = new FileInputStream(new File(path));
		try
		{
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			/* Instead of using default, pass in a decoder. */
			return Charset.defaultCharset().decode(bb).toString();
		}
		finally
		{
			stream.close();
		}
	}

	/**
	 * Gets the port.
	 *
	 * @return the port
	 */
	public String getPort()
	{
		return port;
	}
	
	/**
	 * Gets the port list.
	 *
	 * @return the port list
	 */
	public static String[] getPortList()
	{
		return jssc.SerialPortList.getPortNames();
	}

	/**
	 * Sets the port.
	 *
	 * @param _port the new port
	 */
	public void setPort(String _port)
	{
		for (String existingPort : jssc.SerialPortList.getPortNames())
		{
			if(existingPort.equals(_port))
				this.port = _port;
		}
	}

	/**
	 * Gets the speed.
	 *
	 * @return the speed
	 */
	public int getSpeed()
	{
		return speed;
	}
	
	/**
	 * Gets the speed list.
	 *
	 * @return the speed list
	 */
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

	/**
	 * Sets the speed.
	 *
	 * @param _speed the new speed
	 */
	public void setSpeed(int _speed)
	{
		for (int l_i : getSpeedList())
		{
			if(l_i == _speed)
				this.speed = _speed;
		}
	}

	/**
	 * Gets the databit.
	 *
	 * @return the databit
	 */
	public int getDatabit()
	{
		return databit;
	}
	
	/**
	 * Gets the databit list.
	 *
	 * @return the databit list
	 */
	public static int[] getDatabitList()
	{
		return new int[] {
				jssc.SerialPort.DATABITS_5,
				jssc.SerialPort.DATABITS_6,
				jssc.SerialPort.DATABITS_7,
				jssc.SerialPort.DATABITS_8
		};
	}

	/**
	 * Sets the databit.
	 *
	 * @param _databit the new databit
	 */
	public void setDatabit(int _databit)
	{
		for (int l_i : getDatabitList())
		{
			if(l_i == _databit)
				this.databit = _databit;
		}
	}

	/**
	 * Gets the stopbit.
	 *
	 * @return the stopbit
	 */
	public int getStopbit()
	{
		return stopbit;
	}
	
	/**
	 * Gets the stopbit list.
	 *
	 * @return the stopbit list
	 */
	public static int[] getStopbitList()
	{
		return new int[] {
				jssc.SerialPort.STOPBITS_1,
				jssc.SerialPort.STOPBITS_1_5,
				jssc.SerialPort.STOPBITS_2
		};
	}

	/**
	 * Sets the stopbit.
	 *
	 * @param _stopbit the new stopbit
	 */
	public void setStopbit(int _stopbit)
	{
		for (int l_i : getStopbitList())
		{
			if(l_i == _stopbit)
				this.stopbit = _stopbit;
		}
	}

	/**
	 * Gets the parity.
	 *
	 * @return the parity
	 */
	public int getParity()
	{
		return parity;
	}
	
	/**
	 * Gets the parity list.
	 *
	 * @return the parity list
	 */
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

	/**
	 * Sets the parity.
	 *
	 * @param _parity the new parity
	 */
	public void setParity(int _parity)
	{
		for(int l_i : getParityList())
		{
			if(l_i == _parity)
				this.parity = _parity;
		}
	}

	/**
	 * Gets the flow control.
	 *
	 * @return the flow control
	 */
	public int getFlowControl()
	{
		return flowControl;
	}
	
	/**
	 * Gets the flow control list.
	 *
	 * @return the flow control list
	 */
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

	/**
	 * Sets the flow control.
	 *
	 * @param _flowControl the new flow control
	 */
	public void setFlowControl(int _flowControl)
	{
		for(int l_i : getFlowControlList())
		{
			if(l_i == _flowControl)
				this.flowControl = _flowControl;
		}
	}
	
	/**
	 * Gets the magnitude.
	 *
	 * @return the magnitude
	 */
	public double getMagnitude()
	{
		return magnitude;
	}

	/**
	 * Sets the magnitude.
	 *
	 * @param _magnitude the new magnitude
	 */
	public void setMagnitude(double _magnitude)
	{
	
		this.magnitude = _magnitude;
		
	}

	/**
	 * Gets the constellation.
	 *
	 * @return the constellation
	 */
	public Boolean getConstellation()
	{
		return constellation;
	}

	/**
	 * Sets the constellation.
	 *
	 * @param _constellation the new constellation
	 */
	public void setConstellation(boolean _constellation)
	{
		this.constellation = _constellation;
	}
	
	/**
	 * Gets the simulation.
	 *
	 * @return the simulation
	 */
	public Boolean getSimulation()
	{
		return simulation;
	}

	/**
	 * Sets the simulation.
	 *
	 * @param _simulation the new simulation
	 */
	public void setSimulation(boolean _simulation)
	{
		this.simulation = _simulation;
	}
}