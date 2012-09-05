package com.github.projetp1;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Main class of the project
 * It only contains the entry point of the program
 */
public class Main
{
	/**
	 * The entry point of the program.
	 * 
	 * @param args The arguments. They are ignored.
	 */
	public static void main(String[] args)
	{
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).setLevel(Level.OFF);

		new MainView();
	}

}
