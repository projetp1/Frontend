package test.rs232;

import java.math.BigInteger;

import jssc.*;

public class Receive {

    static SerialPort serialPort;

    public static void main(String[] args) {
        serialPort = new SerialPort("COM3"); 
        try {
            serialPort.openPort();//Open port
            serialPort.setParams(9600, 8, 1, 0);//Set params
            int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;//Prepare mask
            serialPort.setEventsMask(mask);//Set mask
            serialPort.addEventListener(new SerialPortReader());//Add SerialPortEventListener
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }
    
    /*
     * In this class must implement the method serialEvent, through it we learn about 
     * events that happened to our port. But we will not report on all events but only 
     * those that we put in the mask. In this case the arrival of the data and change the 
     * status lines CTS and DSR
     */
    static class SerialPortReader implements SerialPortEventListener {

        public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR()){//If data is available            
                        try {
                        	String received = serialPort.readString();
                        	System.out.print(received);
						} catch (SerialPortException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}  
            }
        }
    }
}