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

import java.util.ArrayList;

public class Constellation
{
	private String sProperName;
	private ArrayList<double[]> dLines = new ArrayList<double[]>();

	public Constellation()
	{
		
	}
	
	public void addLine(double l_X,double l_Y,double l_Xp,double l_Yp)
	{
		double[] l_dTemp = {l_X,l_Y,l_Xp,l_Yp};
		this.dLines.add(l_dTemp);
	}
	
	public void setProperName(String _x)
	{
		this.sProperName = _x;
	}
	
	public String getProperName()
	{
		return this.sProperName;
	}
	
	public ArrayList<double[]> getLines()
	{
		return this.dLines;
	}
}