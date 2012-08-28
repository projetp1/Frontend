/**=====================================================================*
| This file declares the following classes:
|    CelestialObject
|
| Description of the CelestialObject:
|	  Use to have a celestial object with all its informations.	
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Diego Antognini
| @version : 1.0
|
|
 *=======================================================================*/

package com.github.projetp1;

public class Constellation
{
	private String sProperName;
	private double dLines[][];
	private int i = 0;

	public Constellation(String l_sProperName)
	{
		this.sProperName = l_sProperName;
	}

	public void addLine(double l_X,double l_Y,double l_Xp,double l_Yp)
	{
		double[] l_dTemp = {l_X,l_Y,l_Xp,l_Yp};
		this.dLines[i++] = l_dTemp;
	}
	
	public String getProperName()
	{
		return this.sProperName;
	}
	
	public double[][] getLines()
	{
		return this.dLines;
	}
}