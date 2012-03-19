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

	private String CRC;
	
	/**
	 * @return the faulty CRC
	 */
	public String getCRC() {
		return CRC;
	}

	/**
	 * @param crc The faulty CRC
	 */
	public CrcException(String crc) {
		super();
		
		this.CRC = crc;
	}


}
