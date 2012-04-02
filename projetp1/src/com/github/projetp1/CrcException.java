/**
 * 
 */
package com.github.projetp1;

/**
 * CRC Exception
 * 
 * @author sebastie.vaucher
 *
 */
public class CrcException extends Exception {

	private byte CRC;
	
	/**
	 * @return the faulty CRC
	 */
	public String getCRC() {
		return String.valueOf(CRC);
	}

	/**
	 * @param b The faulty CRC
	 */
	public CrcException(byte b) {
		super();
		
		this.CRC = b;
	}


}
