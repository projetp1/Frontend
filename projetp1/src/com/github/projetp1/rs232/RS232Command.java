package com.github.projetp1.rs232;

import java.util.logging.Logger;

public class RS232Command
{
	private RS232CommandType commandNumber;
	public RS232CommandType getCommandNumber() { return commandNumber; }
	private String datas;
	public String getDatas() { return datas; }
	
	/**
	 * Create a new RS232Command object (with CRC check)
	 * @param commandNumber The number of the command (an RS232CommandType)
	 * @param datas The datas. Can be empty in some cases
	 * @param crc The CRC as described in the docs (NMEA-0183)
	 * @throws CrcException
	 * @throws Exception
	 */
	public RS232Command(String commandNumber, String datas, byte crc) throws CrcException, IllegalArgumentException
	{
		this.commandNumber = RS232CommandType.valueOfByNum(commandNumber);
		this.datas = datas;
		
		if(!RS232.checkCrc(datas, crc))
			throw new CrcException(crc);
	}
	
	/**
	 * Create a new RS232Command object (without CRC check).
	 *
	 * @param commandNumber The number of the command (an RS232CommandType)
	 * @param datas The datas. Can be empty in some cases
	 */
	public RS232Command(RS232CommandType commandNumber, String datas)
	{
		this.commandNumber = commandNumber;
		this.datas = datas;
	}
	
	/**
	 * Instantiates a new RS-232Command object. The parsing is automatic.
	 *
	 * @param chain The original string, as received by RS-232
	 * @throws CrcException Thrown if the CRC is faulty
	 * @throws IllegalArgumentException Can be thrown when the String does not conform with the standards.
	 */
	public RS232Command(String chain) throws CrcException, IllegalArgumentException
	{
		if(!chain.startsWith("$") && !chain.endsWith("\r\n"))
			throw new IllegalArgumentException("The chain doesn't start with a $");
		
		// Extract the CRC
		
		// Extract the datas
		this.datas = extractDatas(chain);
		this.commandNumber = extractCommand(chain);
		
		if(!RS232.checkCrc(chain.substring(0, chain.indexOf("*") + 1), extractCrc(chain)))
			throw new CrcException(extractCrc(chain));	
	}
	
	/**
	 * Send a message to the PIC telling that a command was faulty
	 *
	 * @param rs The RS-232 object sending the request.
	 */
	public void sendNck(RS232 rs)
	{
		rs.sendNck(commandNumber);
	}
	
	/**
	 * Extract the datas from a received String.
	 *
	 * @param chain The String that was received via RS-232
	 * @return The datas as a String. May be empty.
	 */
	public static String extractDatas(String chain)
	{
		return chain.substring(chain.indexOf(",") + 1, chain.indexOf("*"));
	}
	
	/**
	 * Extract the command number from a received String.
	 *
	 * @param chain The String that was received via RS-232
	 * @return The command number as an RS232CommandType.
	 */
	public static RS232CommandType extractCommand(String chain)
	{
		try {
			return RS232CommandType.valueOfByNum(chain.substring(1, chain.indexOf(",")));
		} catch (IllegalArgumentException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).warning("Not a valid command number !");
			return null;
		}
	}
	
	/**
	 * Extract the CRC from a received String..
	 *
	 * @param chain The String that was received via RS-232
	 * @return A byte representing the CRC
	 */
	public static byte extractCrc(String chain)
	{
		String crcS = chain.substring(chain.indexOf("*") + 1, chain.indexOf("\r\n"));
		int crcI = Integer.parseInt(crcS, 16);
		return (byte) crcI;
	}
}
