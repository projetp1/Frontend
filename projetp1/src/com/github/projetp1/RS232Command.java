package com.github.projetp1;

public class RS232Command {
	
	private char[] _commandNumber;
	private String _datas;
	private boolean _crcOk;
	
	public RS232Command(char[] commandNumber, String datas, String crc) throws CrcException, Exception
	{
		if(commandNumber.length == 2)
			this._commandNumber = commandNumber;
		else
			throw new Exception("Command number does not have a length of 2");
		
		this._datas = datas;
		
		_crcOk = RS232.computeCrc(datas, crc);
	}
}
