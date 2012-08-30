/**
 * 
 */
package com.github.projetp1.rs232;

/**
 * This enum contains the list of the possible commands
 *
 * @author sebastie.vaucher
 */
public enum RS232CommandType
{
	
	/** Ping command. */
	EMPTY("00"),
	
	/** Changes the PIC mode to pointing. */
	CHANGE_TO_POINT_MODE("01"),
	
	/** Changes the PIC mode to search + Send the arrow. */
	CHANGE_TO_ARROW_MODE("02"),
	
	/** Contains the GPS coordinates. */
	LOCATION_UPDATE("03"),
	
	/** Contains the accelerometer vectors. */
	ACCELEROMETER_UPDATE("04"),
	
	/** Contains the compass vectors. */
	MAGNETOMETER_UPDATE("05"),
	
	/** Used by the PIC to communicates its status to the PC. */
	PIC_STATUS("99");

	/** The code. */
	private final String code;

	/**
	 * Instantiates a new RS232CommandType
	 *
	 * @param c The command number
	 */
	private RS232CommandType(String c)
	{
		code = c;
	}

	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString()
	{
		return code;
	}

	/**
	 * Checks if it is a valid command number.
	 *
	 * @param commandNumber The number of the command
	 * @return true, if it's valid. False otherwise
	 */
	public static boolean isCommandNumber(String commandNumber)
	{
		for (RS232CommandType elem : RS232CommandType.values())
		{
			if (elem.toString().equalsIgnoreCase(commandNumber))
				return true;
		}

		return false;
	}

	/**
	 * Creates an RS232CommandType from its number.
	 * 
	 * @param num
	 *            The command number
	 * @return An RS232CommandType
	 * @throws IllegalArgumentException
	 *             Thrown when there is no such command number known
	 */
	public static RS232CommandType valueOfByNum(String num) throws IllegalArgumentException
	{
		for (RS232CommandType t : RS232CommandType.values())
		{
			if (t.toString().equalsIgnoreCase(num))
				return t;
		}

		throw new IllegalArgumentException("RS232CommandType : " + num
				+ "is not a valid command type !");
	}
}
