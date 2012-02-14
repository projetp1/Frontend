/**
 * 
 */
package com.github.projetp1;

/**
 * @author alexandr.perez
 *
 */
public class RS232 {

	private String port;
	
	/**
	 * 
	 */
	public RS232() {
		// TODO Auto-generated constructor stub
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
