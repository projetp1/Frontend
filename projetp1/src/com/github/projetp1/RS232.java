/**
 * 
 */
package com.github.projetp1;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.sun.xml.internal.bind.v2.util.FatalAdapter;

import jssc.*;

/**
 * @author alexandr.perez
 * @author sebastie.vaucher
 */
public class RS232 implements SerialPortEventListener
{

	private String port;
	protected Settings _settings;
	private SerialPort _sp;
	private ConcurrentLinkedQueue<Object[]> _dataQueue = new ConcurrentLinkedQueue<>();
	private ConcurrentLinkedQueue<String> _ctrlQueue = new ConcurrentLinkedQueue<>();
	private String buffer = "";
	
	private final char EOT = 4;
	
	/**
	 * 
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
	
	public Boolean connect(String _port)
	{
		port = _port;
		return true;
	}
	
	public Boolean disconnect()
	{
		
		return true;
	}
	
	private void sendAck(int frame) throws SerialPortException
	{
		_sp.writeString("CTRL:ACK"+String.valueOf(frame));
	}
	
	private void sendNck(int frame) throws SerialPortException
	{
		_sp.writeString("CTRL:NCK"+String.valueOf(frame));
	}
	
	//TODO: Utiliser un buffer car jssc envoie les données sans les séparer.
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
			// TODO Auto-generated catch block
			ex.printStackTrace();
			return;
		}
		
		if(received.indexOf(EOT) == -1)
		{
			buffer += received;
			return;
		}
		
        if(received.toUpperCase().startsWith("CTRL")) { //Trame de contrôle
        	String[]datas = received.substring(5).split(";");
        	for (String str : datas)
        		_ctrlQueue.add(str);
        	
        	
        }
        else if (received.toUpperCase().startsWith("DATA")) { //Trame de donnée
        	
        }
        else //Trame invalide
        {
        	System.err.println("Trame invalide reçue");
        	return;
        }
	}
}

