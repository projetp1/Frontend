/**
 * 
 */
package com.github.projetp1;

import jssc.*;

/**
 * @author alexandr.perez
 * @author sebastie.vaucher
 */
public class RS232 implements SerialPortEventListener {

	private String port;
	protected Settings _settings;
	private SerialPort _sp;
	
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
	
	public Boolean connect(String _port) {
		port = _port;
		return true;
	}
	
	public Boolean disconnect() {
		
		return true;
	}
	
	public void sendData(String data) {
		
	}

	
	//TODO: Utiliser un buffer car jssc envoie les données sans les séparer.
	@Override
	public void serialEvent(SerialPortEvent e) {
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
        if(received.toUpperCase().startsWith("CTRL")) { //Trame de contrôle
        	String[]datas = received.substring(5).split(";");
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
