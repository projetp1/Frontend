package com.github.projetp1;

import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Main {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) 
	{
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).setFilter(new Filter()
		{
			
			public boolean isLoggable(LogRecord _arg0)
			{
				return false;
			}
		});
		
		Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		log.setLevel(Level.FINEST);
		
		MainView mv = new MainView();
	}

}
