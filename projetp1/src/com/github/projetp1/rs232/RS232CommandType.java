/**
 * 
 */
package com.github.projetp1.rs232;

/**
 * @author sebastie.vaucher
 * 
 */
public enum RS232CommandType {
	/** Ping command */
	EMPTY("00"),
	/** Changes the PIC mode to pointing */
	CHANGE_TO_POINT_MODE("01"),
	/** Changes the PIC mode to search + Send the arrow */
	CHANGE_TO_ARROW_MODE("02"),
	/** Used by the PIC to communicates its status to the PC */
	PIC_STATUS("99")
	;

	private final String code;

	private RS232CommandType(String c) {
		code = c;
	}

	@Override
	public String toString() {
		return code;
	}
	
	/**
	 * Checks if it is a valid command number
	 *
	 * @param commandNumber The number of the command
	 * @return true, if it's valid. False otherwise
	 */
	public static boolean isCommandNumber(String commandNumber) {
		for (RS232CommandType elem : RS232CommandType.values()) {
			if(elem.toString().equalsIgnoreCase(commandNumber))
				return true;
		}
		
		return false;
	}
	
	public static RS232CommandType valueOfByNum(String num) throws IllegalArgumentException {
		for (RS232CommandType t : RS232CommandType.values()) {
			if(t.toString().equalsIgnoreCase(num))
				return t;
		}
		
		throw new IllegalArgumentException("RS232CommandType : " + num + "is not a valid command type !");
	}
}
