package com.github.projetp1;

public class Settings {

	private String port;
	private int speed;	
	private int databit;
	private int stopbit;
	private String parity;
	private String flowControl;
	private Boolean simulation;
	
	public Settings() {
		// TODO Auto-generated constructor stub
		//ICI on récupère depuis le fichier
	}
	
	public void  saveToFile() {
			
	}
	
	private void  loadFromFile() {
		
	}
	
	public String getPort () {
		return port;
	}
	
	public void setPort (String _port) {
		
	}

	public int getSpeed () {
		return speed;
	}
	
	public void setSpeed (int _speed) {
		
	}
	
	public int getDatabit () {
		return databit;
	}
	
	public void setDatabit (int _databit) {
		
	}
	
	public int getStopbit () {
		return stopbit;
	}
	
	public void setStopbit (int _stopbit) {
		
	}
	
	public String getParity () {
		return parity;
	}
	
	public void setParity (String _parity) {
		
	}
	
	public String getFlowControl () {
		return flowControl;
	}
	
	public void setFlowControl (String _flowControl) {
		
	}
	
	public Boolean getSimulation () {
		return simulation;
	}
	
	public void setSimulation (Boolean _simulation) {
		
	}
	
	private void getFromINI () {
		
	}
	
	private void setINI () {
		
	}
}