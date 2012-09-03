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

	private double dXName = 0.0;
	private double dYName = 0.0;
	
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
	 * Set the coordinates of the constellation's name
	 */
	public void setNameCoordinates()
	{
		double[] l_T = Mathematics.getBarycenter(this.dLines);
		this.dXName = l_T[0];
		this.dYName = l_T[1];
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
	 * Gets the X name's position of the constellation.
	 *
	 * @return the X coordinate
	 */
	public double getX()
	{
		return this.dXName;
	}
	
	/**
	 * Gets the Y name's position of the constellation.
	 *
	 * @return the Y coordinate
	 */
	public double getY()
	{
		return this.dYName;
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