package com.github.projetp1;

public class RS232Command
{
	private String _commandNumber;
	private String _datas;
	
	/**
	 * Create a new RS232Command object
	 * @param commandNumber The number of the command (an RS232CommandType)
	 * @param datas The datas. Can be empty in some cases
	 * @param crc The CRC as described in the docs
	 * @throws CrcException
	 * @throws Exception
	 */
	public RS232Command(String commandNumber, String datas, String crc) throws CrcException, Exception
	{
		if(commandNumber.length() == 2)
			this._commandNumber = commandNumber;
		else
			throw new Exception("Command number does not have a length of 2");
		
		if(!RS232CommandType.isCommandNumber(commandNumber))
			throw new Exception("Not a valid command number : " + commandNumber);
		
		this._datas = datas;
		
		if(!RS232.checkCrc(datas, crc));
			throw new CrcException(crc);
	}
	
	public RS232Command(String chain) throws CrcException, Exception
	{
		if(!chain.startsWith("$") && !chain.endsWith("\r\n"))
			throw new Exception("The chain doesn't start with a $");
		
		// Extract the CRC
		
		// Extract the datas
		this._datas = extractDatas(chain);
		this._commandNumber = extractCommand(chain);
		
		if(!RS232.checkCrc(_datas, extractCrc(chain)))
			throw new CrcException(extractCrc(chain));		
	}
	
	public void sendNck(RS232 rs)
	{
		rs.sendNck(_commandNumber);
	}
	
	public static String extractDatas(String chain)
	{
		return chain.substring(3, chain.indexOf("*"));
	}
	
	public static String extractCommand(String chain)
	{
		return chain.substring(1, 3);
	}
	
	public static String extractCrc(String chain)
	{
		return chain.substring(chain.indexOf("*") + 1, chain.length() - 2);
	}
}
