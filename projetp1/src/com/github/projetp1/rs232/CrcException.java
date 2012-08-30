/**
 * 
 */
package com.github.projetp1.rs232;

/**
 * CRC Exception.
 *
 * @author sebastie.vaucher
 */
@SuppressWarnings("serial")
public class CrcException extends Exception
{

	private byte CRC;

	/**
	 * Gets the faulty CRC.
	 *
	 * @return the faulty CRC
	 */
	public String getCRC()
	{
		return String.valueOf(CRC);
	}

	/**
	 * Instantiates a new CRC exception.
	 *
	 * @param b The faulty CRC
	 */
	public CrcException(byte b)
	{
		super();

		this.CRC = b;
	}

}
