package com.github.projetp1;

import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Main
{
	/**
	 * The entry point of the program.
	 * 
	 * @param args The arguments. They are ignored.
	 */
	public static void main(String[] args)
	{
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).setFilter(new Filter()
		{

			@Override
			public boolean isLoggable(LogRecord _arg0)
			{
				return _arg0.getSourceClassName().equalsIgnoreCase("com.github.projetp1.MainView");
			}
		});

		Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		log.setLevel(Level.FINEST);

		new MainView();
	}

}
