/**=====================================================================*
| This file declares the following classes:
|    Constellation
|
| Description of the Constellation:
|	  Represents a Constellation
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Diego Antognini
| @version : 1.0
|
|
 *=======================================================================*/

package com.github.projetp1;

import java.util.ArrayList;

/**
 * This class represents a constellation of stars.
 */
public class Constellation
{
	
	/** The human readable name of the constellation. */
	private String sProperName;
	
	/** The lines that make the constellation. */
	private ArrayList<double[]> dLines = new ArrayList<double[]>();

	/**
	 * Instantiates a new constellation.
	 */
	public Constellation()
	{
		
	}
	
	/**
	 * Adds a new line to the constellation.
	 *
	 * @param l_X The X coordinate of the first point
	 * @param l_Y The Y coordinate of the first point
	 * @param l_Xp The X coordinate of the second point
	 * @param l_Yp The Y coordinate of the second point
	 */
	public void addLine(double l_X,double l_Y,double l_Xp,double l_Yp)
	{
		double[] l_dTemp = {l_X,l_Y,l_Xp,l_Yp};
		this.dLines.add(l_dTemp);
	}
	
	/**
	 * Sets the proper name of the constellation.
	 *
	 * @param _x the new proper name
	 */
	public void setProperName(String _x)
	{
		this.sProperName = _x;
	}
	
	/**
	 * Gets the proper name of the constellation.
	 *
	 * @return the proper name
	 */
	public String getProperName()
	{
		return this.sProperName;
	}
	
	/**
	 * Gets the lines of the constellation used to draw it.
	 *
	 * @return the lines
	 */
	public ArrayList<double[]> getLines()
	{
		return this.dLines;
	}
}