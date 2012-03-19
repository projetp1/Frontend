/**
 * 
 */
package com.github.projetp1;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.zip.CRC32;

import sun.misc.CRC16;

import com.sun.xml.internal.bind.v2.util.FatalAdapter;

import jssc.*;

// TODO: Auto-generated Javadoc
/**
 * The class that manages the RS-232 connection and communications.
 *
 * @author alexandr.perez
 * @author sebastie.vaucher
 */
public class RS232 implements SerialPortEventListener
{

	/** The RS-232 port. */
	private String port;
	
	/** The Settings object. */
	protected Settings _settings;
	
	/** The serial port object. */
	private SerialPort _sp;
	
	/** The queue of the latest received commands. */
	private ConcurrentLinkedQueue<RS232Command> commandQueue = new ConcurrentLinkedQueue<RS232Command>();
	
	/** The buffer in which the received datas are temporarily put. */
	private StringBuffer buffer = new StringBuffer();

	
	/**
	 * Instantiates a new r s232.
	 */
	public RS232()
	{
		// Initialisation des valeurs par défaut :
		
		// Initialisation du port:
		this.port = _settings.getPort();
		if(this.port == null)
		{
			this.port = (SerialPortList.getPortNames())[0];
			this._settings.setPort(this.port);
		}
		
		// Initialisation des settings
		//TODO: this._settings = new ();
		
		// Initialisation du sp
		this._sp = new SerialPort(this.port);
		
		try 
		{
			this._sp.openPort();//Open serial port
			this._sp.setParams(SerialPort.BAUDRATE_9600, 
									SerialPort.DATABITS_8,
									SerialPort.STOPBITS_1,
									SerialPort.PARITY_NONE);//Set params. Also you can set params by this string: serialPort.setParams(9600, 8, 1, 0);
		}
        catch (Exception ex) {
            System.out.println(ex);
        }
	}
	
	/**
	 * Connect.
	 *
	 * @param _port the _port
	 * @return the boolean
	 */
	public Boolean connect(String _port)
	{
		port = _port;
		return true;
	}
	
	/**
	 * Disconnect.
	 *
	 * @return the boolean
	 */
	public Boolean disconnect()
	{
		
		return true;
	}
	
	/**
	 * Send ack.
	 *
	 * @param frame the frame
	 * @throws SerialPortException the serial port exception
	 */
	private void sendAck(int frame) throws SerialPortException
	{
		_sp.writeString("CTRL:ACK"+String.valueOf(frame));
	}
	
	/**
	 * Send nck.
	 *
	 * @param frame the frame
	 * @throws SerialPortException the serial port exception
	 */
	private void sendNck(int frame) throws SerialPortException
	{
		_sp.writeString("CTRL:NCK"+String.valueOf(frame));
	}
	
	//TODO: Utiliser un buffer car jssc envoie les données sans les séparer.
	/* (non-Javadoc)
	 * @see jssc.SerialPortEventListener#serialEvent(jssc.SerialPortEvent)
	 */
	@Override
	public void serialEvent(SerialPortEvent e)
	{
		// Callback du SerialPort
		if(!e.isRXCHAR()) //If no data is available
			return;
		        
		// TODO: Try/Catch
        String received;
		try {
			received = _sp.readString();
		} catch (SerialPortException ex) {
			System.out.println("RS232 : port " + ex.getPortName() + " unreadable");
			return;
		}

		buffer.append(received);
		
		int pos = buffer.indexOf("\r\n");
	}
	
	public static boolean computeCrc(String datas, String crc)
	{
		return true;
	}
}

