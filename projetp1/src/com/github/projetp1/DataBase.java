/**
 * 
 */
package com.github.projetp1;
import java.util.ArrayList;

import java.sql.*;

/**
 * @author Diego Antognini
 *
 */

public class DataBase {

	private Connection connection;
	private Statement statement;
	
	public DataBase() throws Exception {
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:res/hyg.db");
		statement = connection.createStatement();
	}

	public ArrayList<CelestialObject> starsForCoordinates (Statement _state,double _longitude, double _latitude) throws SQLException 
	{
		ArrayList<CelestialObject> stars = new ArrayList<CelestialObject>();
		
		int l_id;
		int l_StarId;
		int l_HIP;
		int l_HD;
		int l_HR;
		int l_Gliese;
		int l_BayerFlamsteed;
		String l_ProperName;
		double l_RA;
		double l_Dec;
		double l_Distance;
		double l_PMRA;
		double l_PMDec;
		double l_RV;
		double l_Mag;
		double l_AbsMag;
		String l_Spectrum;
		double l_ColorIndex;
		double l_xyz[] = new double[3];
		double l_vxyz[] = new double[3];
		
		double l_lomax = _longitude+0.5;
		double l_lomin = _longitude-0.5;
		double l_lamax = _latitude+0.5;
		double l_lamin = _latitude-0.5;
		
		//Requête de test,il manque les calculs
		String l_query = "SELECT * FROM stars WHERE x > " + l_lomin + " AND x < " + l_lomax + " AND y > " + l_lamin + " AND y > " + l_lamax + ";";
		//String l_query = "SELECT * FROM stars;";
		System.out.println(l_query);
		ResultSet result = _state.executeQuery(l_query);
		
	    while (result.next()) 
	    {
	    	l_id = result.getInt("id");
			l_StarId = result.getInt("StarID");
			l_HIP = result.getInt("HIP");
			l_HD = result.getInt("HD");
			l_HR = result.getInt("HR");
			l_Gliese = result.getInt("Gliese");
			l_BayerFlamsteed = result.getInt("BayerFlamsteed");
			l_ProperName = result.getString("ProperName");
			l_RA = result.getDouble("RA");
			l_Dec = result.getDouble("Dec");
			l_Distance = result.getDouble("Distance");
			l_PMRA = result.getDouble("PMRA");
			l_PMDec = result.getDouble("PMDec");
			l_RV = result.getDouble("RV");
			l_Mag = result.getDouble("Mag");
			l_AbsMag = result.getDouble("AbsMag");
			l_Spectrum = result.getString("Spectrum");
			l_ColorIndex = result.getDouble("ColorIndex");
			l_xyz[0] = result.getDouble("VX");
			l_xyz[1] = result.getDouble("VY");
			l_xyz[2] = result.getDouble("VZ");
			l_vxyz[0] = result.getDouble("VX");
			l_vxyz[1] = result.getDouble("VY");
			l_vxyz[2] = result.getDouble("VZ");

			
	    	CelestialObject l_star = new CelestialObject(l_id,l_StarId,l_HIP,l_HD,l_HR,l_Gliese,l_BayerFlamsteed,l_ProperName,l_RA,l_Dec,l_Distance,l_PMRA,l_PMDec,l_RV,l_Mag,l_AbsMag,l_Spectrum,l_ColorIndex,l_xyz[0],l_xyz[1],l_xyz[2],l_vxyz[0],l_vxyz[1],l_vxyz[2]);
	    	stars.add(l_star);
	    	l_star = null;
		}
		result.close();
		
		return stars;		
	}
	
	public ArrayList<CelestialObject> starsForText (String _searchText) {
		return null;		
	}

	/*public ResultSet Select(Statement _state) throws Exception
	{
		String query = "SELECT * FROM stars ORDER BY id ;";
		ResultSet rs = _state.executeQuery(query);
		return rs;
	}*/
	
	
	public Connection getConnection()
	{
		return connection;
	}
	
	public Statement getStatement()
	{
		return statement;
	}
}
