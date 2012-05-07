package com.github.projetp1.rs232;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import com.github.projetp1.*;
import com.github.projetp1.Pic.PicMode;

import jssc.*;

/**
 * The class that manages the RS-232 connection and communications.
 * 
 * @author alexandr.perez
 * @author sebastie.vaucher
 */
public class RS232 implements SerialPortEventListener
{
	/** The Settings */
	protected Settings settings;
	/** The object maintaining the PIC status */
	protected Pic pic;
	/** The serial port object. */
	private SerialPort sp;
	/** The queue of the latest received commands. */
	private ConcurrentLinkedQueue<RS232Command> commandQueue = new ConcurrentLinkedQueue<RS232Command>();
	/** The buffer in which the received datas are temporarily put. */
	private StringBuffer buffer = new StringBuffer();
	private Timer pingTimer;
	private Timer timeoutTimer;
	
	Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	/** The delay between 2 pings */
	private static final int PINGDELAY = 3000;
	/** The delay after which a ping timeout occurs */
	private static final int PINGTIMEOUT = 2000;
	
	/**
	 * Represents the direction of the arrow that is displayed on the PIC screen.
	 */
	public enum PicArrowDirection
	{
		NORTH(0), NORTHWEST(1), WEST(2), SOUTHWEST(3), SOUTH(4), SOUTHEAST(5), EAST(6), NORTHEAST(7);
		private int number;
		private PicArrowDirection(int _number)
		{
			number = _number;
		}
	}

	/**
	 * Instantiates a new RS232 object.
	 *
	 * @param _settings A Settings object
	 * @param _pic A Pic object
	 * @throws Exception A generic Exception. May occur if the PIC isn't found.
	 * @throws SerialPortException A SerialPortException
	 */
	public RS232(Settings _settings, Pic _pic) throws Exception, SerialPortException
	{
		this.settings = _settings;
		this.pic = _pic;

		// Initialisation du sp
		String port = settings.getPort();
		if (port == null || port.equals(""))
		{
			try
			{
				port = SerialPortList.getPortNames()[0];
				if (port == null || port.equals(""))
					throw new Exception();
				log.info("The default SerialPort has been selected : " + port);
				settings.setPort(port);
			}
			catch (Exception e)
			{
				log.severe("No SerialPort has been found !");
				throw new SerialPortException("NoPort", "RS232.RS232(MainView)",
						"No RS-232 port found on the computer !");
			}
		}

		this.sp = new SerialPort(port);

		try
		{
			this.sp.openPort();// Open serial port
			this.sp.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            this.sp.setEventsMask(SerialPort.MASK_RXCHAR);//Set mask
            this.sp.addEventListener(this);//Add SerialPortEventListener
		}
		catch (SerialPortException ex)
		{
			log.warning("SerialPortException at port opening : " + ex.getMessage());
			throw ex;
		}

		if (!this.sendPing())
		{
			Exception ex = new Exception("The initial ping could not be sent, aborting...");
			log.warning(ex.getMessage());
			throw ex;
		}
		log.info("Serial port opened");

		this.connect();
		log.info("RS232 object created");
	}

	/**
	 * Send an EMPTY command to the PIC and resets the timeout
	 * 
	 * @return True if it succeeds, false otherwise
	 */
	protected Boolean sendPing()
	{
		try
		{
			String start = "$" + RS232CommandType.EMPTY.toString() + "*";
			sp.writeString(start + computeCrc(start) + "\r\n");
			log.info("Ping sent");
			return true;
		}
		catch (SerialPortException ex)
		{
			log.warning("Unable to send ping");
			return false;
		}
	}
	
	/**
	 * Connect to the PIC (send ping, schedule ping)
	 */
	private void connect()
	{
		if(pingTimer != null)
		{
			pingTimer.cancel();
			pingTimer = null;
		}
		
		if(pic.getMode() == PicMode.SIMULATION)
			pic.setMode(PicMode.POINTING);
		if(pic.getMode() == PicMode.POINTING)
		{
			pingTimer = new Timer("pingDaemon", true);
			pingTimer.schedule(new TimerTask()
			{
				public void run()
				{
					sendPing();
					resetTimeout();
				}
			}, 0, PINGDELAY);
		}
	}
	
	/**
	 * Disconnect from the PIC (cancel ping scheduling)
	 */
	private void disconnect()
	{
		log.info("Disconnecting the PIC");
		
		if(pingTimer != null)
		{
			pingTimer.cancel();
			pingTimer = null;
		}
		if(timeoutTimer != null)
		{
			timeoutTimer.cancel();
			timeoutTimer = null;
		}
		
		pic.setMode(PicMode.SIMULATION);
	}
	
	/**
	 * Indicate to this object that the operation mode has changed
	 * @param _mode The new mode of operation
	 * @throws SerialPortException If the PIC can not be reached
	 */
	public void modeHasChanged(PicMode _mode) throws SerialPortException
	{
		switch (_mode)
		{
			case SIMULATION:
				disconnect();
				break;
			case GUIDING:
				connect();
				sendArrowToPic(PicArrowDirection.NORTH);
				break;
			case POINTING:
				connect();
				sendFrame(RS232CommandType.CHANGE_TO_POINT_MODE, "");
				break;
			default:
				log.warning("Mode not yet supported, the state may be inaccurate !");
				disconnect();
				break;
		}
		
		log.info("PIC mode switched to : " + _mode);
	}
	
	
	/**
	 * Send the direction of the arrow to the PIC.
	 *
	 * @param _dir The direction of the arrow
	 * @throws SerialPortException 
	 */
	public void sendArrowToPic(PicArrowDirection _dir) throws SerialPortException
	{
		this.sendFrame(RS232CommandType.CHANGE_TO_ARROW_MODE, String.valueOf(_dir.number));
	}

	/**
	 * Send an NCK message to the PIC informing that a packet has been corrupted.
	 * 
	 * @param command The command number
	 */
	public void sendNck(RS232CommandType command)
	{
		if (pic.getMode() == PicMode.GUIDING || command.equals(RS232CommandType.PIC_STATUS))
			return;

		try
		{
			sendFrame(command, "");
		}
		catch (SerialPortException ex)
		{
			log.warning("Unable to send a NCK on port " + ex.getPortName());
		}
	}

	
	/**
	 * Send a frame to the PIC
	 * @param cNum The command to send
	 * @param datas The datas (can be empty)
	 * @throws SerialPortException If the SerialPort cannot be written
	 */
	public synchronized void sendFrame(RS232CommandType cNum, String datas) throws SerialPortException
	{
		String trame = "$" + cNum.toString() + "*" + datas;
		trame += RS232.computeCrc(trame) + "\r\n";
		sp.writeString(trame);
		log.info("Frame sent : " + trame);
	}

	/**
	 * Reset the timeout of the ping
	 */
	private void resetTimeout()
	{
		log.info("Timeout reset");
		
		if(timeoutTimer != null)
		{
			timeoutTimer.cancel();
			timeoutTimer = null;
		}

		if(pic.getMode() == PicMode.POINTING)
		{
			timeoutTimer = new Timer();
			timeoutTimer.schedule(new TimerTask()
			{
				@Override
				public void run()
				{
					disconnect();
				}
			}, PINGTIMEOUT);
		}
	}

	/**
	 * @see jssc.SerialPortEventListener#serialEvent(jssc.SerialPortEvent)
	 */
	@Override
	public void serialEvent(SerialPortEvent e)
	{
		// Callback du SerialPort
		if (!e.isRXCHAR()) // If no data is available
			return;

		String received;
		try
		{
			received = sp.readString();
			log.info("Data received : " + received);
		}
		catch (SerialPortException ex)
		{
			log.warning("Port " + ex.getPortName()
					+ " unreadable. Please check that we are the only one listening to this port.");
			return;
		}

		buffer.append(received);

		int pos;
		boolean newComs = false;

		while ((pos = buffer.indexOf("\r\n")) != -1)
		{
			RS232Command com;
			String chain = buffer.substring(0, pos + 2);
			try
			{
				com = new RS232Command(chain);
				if(com.getCommandNumber() != RS232CommandType.EMPTY)
					commandQueue.add(com);
				else
				{
					log.info("Ping received");
					resetTimeout();
				}
				newComs = true;
			}
			catch (CrcException e1)
			{
				this.sendNck(RS232Command.extractCommand(chain));
				log.info("Frame corrupted");
			}
			catch (Exception e1)
			{
				log.warning("Exception while trying to decode frame : " + e1.getMessage());
			}
		}

		if (newComs)
		{
			pic.run();
		}

	}

	public RS232Command getLastCommand()
	{
		return commandQueue.poll();
	}
	
	/**
	 * Check the CRC against the raw string received via RS-232
	 * 
	 * @param datas The data against which to check the CRC
	 * @param crc The received CRC
	 * @return True if it matches or false otherwise
	 */
	public static boolean checkCrc(String datas, byte crc)
	{
		return crc == (computeCrc(datas));
	}

	/**
	 * Returns the computed CRC of the string following NMEA-0183 method
	 * 
	 * @param datas The String of which to compute the CRC
	 * @return The computed CRC as a byte
	 */
	public static byte computeCrc(String datas)
	{
		byte crc = 0;

		for (byte b : datas.getBytes())
		{
			crc = (byte) (crc ^ b);
		}
		return crc;
	}
}
