/**
 * 
 */
package com.github.projetp1;

/**
 * @author sebastie.vaucher
 * 
 */
public enum RS232CommandType {
	EMPTY("00"),
	CHANGE_TO_POINT_MODE("01"),
	CHANGE_TO_ARROW_MODE("02"),
	PIC_STATUS("99")
	;

	private String code;

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
}
