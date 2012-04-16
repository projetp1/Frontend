package com.github.projetp1.rs232;

public class RS232Command
{
	private RS232CommandType _commandNumber;
	private String _datas;
	
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
		this._commandNumber = RS232CommandType.valueOfByNum(commandNumber);
		this._datas = datas;
		
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
		this._commandNumber = commandNumber;
		this._datas = datas;
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
		this._datas = extractDatas(chain);
		this._commandNumber = RS232CommandType.valueOfByNum(extractCommand(chain));
		
		if(!RS232.checkCrc(_datas, extractCrc(chain)))
			throw new CrcException(extractCrc(chain));		
	}
	
	/**
	 * Send a message to the PIC telling that a command was faulty
	 *
	 * @param rs The RS-232 object sending the request.
	 */
	public void sendNck(RS232 rs)
	{
		rs.sendNck(_commandNumber.toString());
	}
	
	/**
	 * Extract the datas from a received String.
	 *
	 * @param chain The String that was received via RS-232
	 * @return The datas as a String. May be empty.
	 */
	public static String extractDatas(String chain)
	{
		return chain.substring(3, chain.indexOf("*"));
	}
	
	/**
	 * Extract the command number from a received String.
	 *
	 * @param chain The String that was received via RS-232
	 * @return The command number as a String.
	 */
	public static String extractCommand(String chain)
	{
		return chain.substring(1, 3);
	}
	
	/**
	 * Extract the CRC from a received String..
	 *
	 * @param chain The String that was received via RS-232
	 * @return A byte representing the CRC
	 */
	public static byte extractCrc(String chain)
	{
		return (byte) chain.charAt(chain.indexOf("*") + 1);
	}
}
