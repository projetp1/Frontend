/**
 * 
 */
package com.github.projetp1;

import jssc.*;

/**
 * @author alexandr.perez
 * @author sebastie.vaucher
 */
public class RS232 {

	private String port;
	protected Settings _settings;
	private SerialPort _sp;
	
	/**
	 * 
	 */
	public RS232() {
		// Initialisation des valeurs par défaut :
		this.port = _settings.getPort();
		if(this.port == null) {
			
			this.port = (SerialPortList.getPortNames())[0];
			this._settings.setPort(this.port);
		}
		else {
			this.port = this._settings.getPort();
		}
	}
	
	public Boolean connect(String _port) {
		port = _port;
		return true;
	}
	
	public Boolean disconnect() {
		
		return true;
	}
	
	public void receiveData() {
		
	}
	
	public void sendData() {
		
	}
}
