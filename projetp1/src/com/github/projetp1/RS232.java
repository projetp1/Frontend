/**
 * 
 */
package com.github.projetp1;

import java.util.concurrent.ConcurrentLinkedQueue;

import jssc.*;

/**
 * The class that manages the RS-232 connection and communications.
 * 
 * @author alexandr.perez
 * @author sebastie.vaucher
 */
public class RS232 implements SerialPortEventListener {
	/** The Settings */
	protected Settings _settings;
	/** The MainView */
	protected MainView _mainview;
	/** The serial port object. */
	private SerialPort _sp;
	/** The queue of the latest received commands. */
	private ConcurrentLinkedQueue<RS232Command> commandQueue = new ConcurrentLinkedQueue<RS232Command>();
	/** The buffer in which the received datas are temporarily put. */
	private StringBuffer buffer = new StringBuffer();

	/**
	 * Instantiates a new RS232 object.
	 * 
	 * @param mainview The global MainView
	 * @throws Exception A generic Exception. May occur if the PIC isn't found.
	 * @throws SerialPortException A SerialPort Exception
	 */
	public RS232(MainView mainview) throws Exception, SerialPortException {
		this._mainview = mainview;
		this._settings = mainview.getSettings();

		// Initialisation du sp
		String port = _settings.getPort();
		if (port == null || port.equals("")) {
			try {
				port = SerialPortList.getPortNames()[0];
				if (port == null || port.equals(""))
					throw new Exception();
				System.out
						.println("The default SerialPort has been selected : "
								+ port);
				_settings.setPort(port);
			} catch (Exception e) {
				System.out.println("FATAL : No SerialPort has been found !");
				throw new SerialPortException("NoPort",
						"RS232.RS232(MainView)",
						"No RS-232 port found on the computer !");
			}
		}

		this._sp = new SerialPort(port);

		try {
			this._sp.openPort();// Open serial port
			this._sp.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8,	SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		} catch (SerialPortException ex) {
			System.out.println("SerialPortException at port opening : "
					+ ex.getMessage());
			throw ex;
		}

		if (!this.sendPing())
			throw new Exception(
					"The initial ping could not be sent, aborting...");

	}

	/**
	 * Send an EMPTY command to the PIC
	 * 
	 * @return True if it succeeds, false otherwise
	 */
	public Boolean sendPing() {
		try {
			String start = "$" + RS232CommandType.EMPTY.toString() + "*";
			_sp.writeString(start + computeCrc(start) + "\r\n");
			return true;
		} catch (SerialPortException ex) {
			return false;
		}
	}

	/**
	 * Send an NCK message to the PIC informing that a packet has been
	 * corrupted.
	 *
	 * @param command The command number
	 */
	public void sendNck(String command) {
		if (command.equalsIgnoreCase(RS232CommandType.PIC_STATUS.toString()))
			return;

		String trame = "$" + command.substring(0, 2) + "*";
		try {
			_sp.writeString(trame + RS232.computeCrc(trame) + "\r\n");
		} catch (SerialPortException ex) {
			System.out.println("RS232 : Unable to NCK port " + ex.getPortName()
					+ ". Possible future mismatch between the datas.");
		}
	}

	/**
	 * @see jssc.SerialPortEventListener#serialEvent(jssc.SerialPortEvent)
	 */
	@Override
	public void serialEvent(SerialPortEvent e) {
		// Callback du SerialPort
		if (!e.isRXCHAR()) // If no data is available
			return;

		// TODO: Try/Catch
		String received;
		try {
			received = _sp.readString();
		} catch (SerialPortException ex) {
			System.out
					.println("RS232 : port "
							+ ex.getPortName()
							+ " unreadable. Please check that we are the only one listening to this port.");
			return;
		}

		buffer.append(received);

		int pos;
		boolean newComs = false;

		while ((pos = buffer.indexOf("\r\n")) != -1) {
			RS232Command com;
			String chain = buffer.substring(0, pos + 2);
			try {
				com = new RS232Command(chain);
				commandQueue.add(com);
				newComs = true;
			} catch (CrcException e1) {
				this.sendNck(RS232Command.extractCommand(chain));
			} catch (Exception e1) {
				System.out.println(e1.getMessage());
			}
		}

		/*
		 * TODO: Call the delegate and inform him that new datas are available
		 * if(newComs) { // Call the delegate } //
		 */
	}

	/**
	 * Returns whether the chains and the CRC matches
	 * 
	 * @param datas The datas
	 * @param crc The received CRC
	 * @return True if it matches or false otherwise
	 */
	public static boolean checkCrc(String datas, byte crc) {
		return crc == (computeCrc(datas));
	}

	/**
	 * Returns the computed CRC of the chain Uses NMEA-0183 method
	 * 
	 * @param datas The String of which to compute the CRC
	 * @return The computed CRC as a byte
	 */
	public static byte computeCrc(String datas) {
		byte crc = 0;

		for (byte b : datas.getBytes()) {
			crc = (byte) (crc ^ b);
		}
		return crc;
	}
}
