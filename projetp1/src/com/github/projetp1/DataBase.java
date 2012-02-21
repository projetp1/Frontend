 /*=====================================================================*
 | This file declares the following classes:
 |    DataBase
 |
 | Description of the class DataBase :
 |	  Use to access to the database. You can select the stars that you want, with some informations.
 |	  All fonctions return an ArrayList with object of CelestialObject	
 |
 | <p>Copyright : EIAJ, all rights reserved</p>
 | @autor : Diego Antognini
 | @version : 1.0
 |
 |
 *=====================================================================*/

package com.github.projetp1;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.sql.*;

public class DataBase 
{
	private String sDataBase;
	private Connection connection;
	private Statement statement;
	
	public DataBase(String _sDataBase) throws SQLException, Exception
	{
		Class.forName("org.sqlite.JDBC");//Load the drivers for SQLite
		this.sDataBase = _sDataBase;
		this.connection = createConnection();
		this.statement = createStatement();
	}
	
	public void closeConnection() throws SQLException
	{
		this.connection.close();
	}
	
	public void openConnection() throws SQLException
	{
		this.connection = createConnection();
	}
	
	private Connection createConnection() throws SQLException
	{
		return DriverManager.getConnection("jdbc:sqlite:res/"+this.sDataBase);
	}
	
	private Statement createStatement() throws SQLException
	{
		return connection.createStatement();
	}
	
	private int isDouble(String _sString)
	{
		String l_sRegex  = "[-+]?[0-9]*\\.?[0-9]+";
        String l_sInput = _sString;
        
        Pattern l_pattern = Pattern.compile(l_sRegex);
        Matcher l_matcher = l_pattern.matcher(l_sInput);
        
        if (l_matcher.matches()) 
        	return 1;
        else
        	return 0;
	}
	
	private ResultSet selectQuery(String _sTable,String _sWhere[][]) throws SQLException
	{
		String l_sQuery = "SELECT * FROM " + _sTable + " WHERE ";
		
		for(int i=0;i<_sWhere.length;i++)
		{
			l_sQuery += _sWhere[i][0] + "=" + "'" + _sWhere[i][1] + "'";
			if(i!=_sWhere.length-1)
				l_sQuery += " AND ";
		}
		
		return this.statement.executeQuery(l_sQuery);
	}
	
	//private 
	//Manque d'autres paramètres
	public ArrayList<CelestialObject> starsForCoordinates (double _dLongitude, double _dLatitude) throws SQLException 
	{
		ArrayList<CelestialObject> al_stars = new ArrayList<CelestialObject>();
		
		int l_id;
		int l_StarId;
		int l_HIP;
		int l_HD;
		int l_HR;
		int l_Gliese;
		int l_BayerFlamsteed;
		String l_ProperName;
		double l_dRA;
		double l_Dec;
		double l_dDistance;
		double l_dPMRA;
		double l_dPMDec;
		double l_dRV;
		double l_dMag;
		double l_dAbsMag;
		String l_sSpectrum;
		double l_dColorIndex;
		double l_dXYZ[] = new double[3];
		double l_dVXYZ[] = new double[3];
		
		if(isDouble(Double.toString(_dLongitude))==0 || isDouble(Double.toString(_dLatitude))==0)
			return null;
		
		/*double l_dLonMax = _dLongitude+0.5;
		double l_dLonMin = _dLongitude-0.5;
		double l_dLanMax = _dLatitude+0.5;
		double l_dLanMin = _dLatitude-0.5;*/
				
		//Requête de test,il manque les calculs
		//String l_sQuery = "SELECT * FROM stars WHERE x > " + l_dLonMin + " AND x < " + l_dLonMax + " AND y > " + l_dLanMin + " AND y > " + l_dLanMax + ";";
		
		String where[][] = {{"id","1"},
							{"ProperName","Sol"}};		
		
		ResultSet result = selectQuery("stars",where);
		
		if(result == null)
			return null;
	    
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
			l_dRA = result.getDouble("RA");
			l_Dec = result.getDouble("Dec");
			l_dDistance = result.getDouble("Distance");
			l_dPMRA = result.getDouble("PMRA");
			l_dPMDec = result.getDouble("PMDec");
			l_dRV = result.getDouble("RV");
			l_dMag = result.getDouble("Mag");
			l_dAbsMag = result.getDouble("AbsMag");
			l_sSpectrum = result.getString("Spectrum");
			l_dColorIndex = result.getDouble("ColorIndex");
			l_dXYZ[0] = result.getDouble("VX");
			l_dXYZ[1] = result.getDouble("VY");
			l_dXYZ[2] = result.getDouble("VZ");
			l_dVXYZ[0] = result.getDouble("VX");
			l_dVXYZ[1] = result.getDouble("VY");
			l_dVXYZ[2] = result.getDouble("VZ");

			
	    	CelestialObject l_star = new CelestialObject(l_id,l_StarId,l_HIP,l_HD,l_HR,l_Gliese,l_BayerFlamsteed,l_ProperName,l_dRA,l_Dec,l_dDistance,l_dPMRA,l_dPMDec,l_dRV,l_dMag,l_dAbsMag,l_sSpectrum,l_dColorIndex,l_dXYZ[0],l_dXYZ[1],l_dXYZ[2],l_dVXYZ[0],l_dVXYZ[1],l_dVXYZ[2]);
	    	al_stars.add(l_star);
	    	l_star = null;
		}
		result.close();
		
		return al_stars;		
	}
	
	public ArrayList<CelestialObject> starsForText (String _searchText) 
	{
		return null;		
	}

	/*public ResultSet Select(Statement _state) throws Exception
	{
		String query = "SELECT * FROM stars ORDER BY id ;";
		ResultSet rs = _state.executeQuery(query);
		return rs;
	}*/
	
	/*
	public Connection getConnection()
	{
		return connection;
	}
	
	public Statement getStatement()
	{
		return statement;
	}*/
}
