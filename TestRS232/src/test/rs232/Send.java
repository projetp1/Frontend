package test.rs232;

import jssc.*;

public class Send {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SerialPort serialPort = new SerialPort("COM4");
        try {
        	serialPort.openPort();//Open serial port
        		for(int num =0;;num++) {
	            serialPort.setParams(SerialPort.BAUDRATE_9600, 
	                                 SerialPort.DATABITS_8,
	                                 SerialPort.STOPBITS_1,
	                                 SerialPort.PARITY_NONE);//Set params. Also you can set params by this string: serialPort.setParams(9600, 8, 1, 0);
	            
	            serialPort.writeBytes(String.valueOf(num).getBytes());//Write data to port
	            System.out.println("Sent num "+String.valueOf(num));
	            Thread.sleep(1000);
        		}
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
