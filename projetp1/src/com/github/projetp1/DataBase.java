 /**=====================================================================*
 | This file declares the following classes:
 |    DataBase
 |
 | Description of the class DataBase :
 |	  Use to access to the database. You can select the stars that you want, with some informations.
 |	  All functions return an ArrayList with object of CelestialObject	
 |
 | <p>Copyright : EIAJ, all rights reserved</p>
 | @autor : Diego Antognini
 | @version : 1.0
 |
 |
 *========================================================================*/

package com.github.projetp1;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.sql.*;

public class DataBase 
{
	private String sDataBase;
	private String sDelimiter;
	private Connection connection;
	private Statement statement;
	
	private int argumentCount;
	static private final double kdAL_KM = 9435053029704.605;//Source wikipédia
	
	/** 
	 * Database
	 * Constructor
	 * @param string _sDataBase : It's the name of the database that will use
	 * @param string _sDelimiter : It's the delimiter of the string of the searchbar
	 */
	public DataBase(String _sDataBase,String _sDelimiter) throws SQLException, Exception
	{
		Class.forName("org.sqlite.JDBC");//Load the drivers for SQLite
		this.argumentCount = 0;
		this.sDataBase = _sDataBase;
		this.sDelimiter = _sDelimiter;
		this.connection = createConnection();
		this.statement = createStatement();
	}
	
	/** 
	 * closeConnection
	 *  You can close the connection of a connection object
	 */
	public void closeConnection() throws SQLException
	{
		this.connection.close();
	}
	
	/** 
	 * OpenConnection
	 * You can open the connection of a connection object. After that, you can do everything in the database
	 */
	public void openConnection() throws SQLException
	{
		this.connection = createConnection();
	}
	
	/** 
	 * createConnection
	 * After open a connection, you have to load the database and create a connection to this
	 *@return return a Connection object that has loaded the database
	 */
	private Connection createConnection() throws SQLException
	{
		return DriverManager.getConnection("jdbc:sqlite:res/"+this.sDataBase);
	}
	
	/** 
	 * createStatement
	 * When you have create a connection, load the database you can now do sql queries
	 * @return : return a Statement object that will be use for queries
	 */
	private Statement createStatement() throws SQLException
	{
		return connection.createStatement();
	}

	/** 
	 * executeRegex
	 * Used for execute a Regex. For example when you want to know if the is a unity for the value 
	 * @param string _sRegex : It's the regex that the function will use
	 * @param string _sString : It's the string that will be analyze by the regex
	 * @return : Return a boolean with true if the regex has found the string or return false
	 */
	private boolean executeRegex(String _sRegex,String _sString)
	{
        Pattern l_pattern = Pattern.compile(_sRegex);
        Matcher l_matcher = l_pattern.matcher(_sString);
        
        return l_matcher.matches(); 
	}
	
	/** 
	 * isDouble
	 * Say if it's a Double or not with a regex.
	 * @param _sString : It's the string that will be analyze
	 * @return : Return a boolean with true if the regex has found that the string is a double or return false
	 */
	private boolean isDouble(String _sString)
	{
		String l_sRegex  = "[-+]?[0-9]*\\.?[0-9]+";
        return executeRegex(l_sRegex,_sString);
	}
	 
	/** 
	 * addFieldsQuery
	 * Add to the query the fields that you want to have.
	 * @param String[] _sFields : Array that contains the fields that you want to have in the query
	 * @return : Return the string that contains the fields for the query
	 */
	private String addFieldsQuery(String _sFields[])
	{
		String l_sOut = "";
		//Analyze all the array and add the fields to the query
		for(int i = 0;i<_sFields.length;i++)
		{
			l_sOut += _sFields[i];
			if(i!=_sFields.length -1)
				l_sOut += ",";
		}	
		return l_sOut;
	}
	
	/** 
	 * addTableQuery
	 * Add the table that will be used in the query
	 * @param String[] _sTable : The array where the tables are
	 * @return : Return a string that contains the table for the query
	 */
	private String addTableQuery(String _sTable[])
	{
		String l_sOut = " FROM ";
		//Analyze all the array and had the tables to the query
		for(int i = 0;i<_sTable.length;i++)
		{
			l_sOut += _sTable[i];
			if(i!= _sTable.length -1)
				l_sOut += ",";
		}
		return l_sOut;		
	}
	/**
	 * addWhereQuery
	 * Add the clause "WHERE" to the query
	 * @param String[][] _sWhere : Array 2D with the format : {{"field","sign","value"},{...}}
	 * @param boolean secured : True if you want a prepared statement or false for a basic statement
	 * @return : Return a string that contains the clause WHERE for the query
	 */
	private String addWhereQuery(String _sWhere[][],boolean _bsecured)
	{
		String l_sOut = " WHERE ";

		//Analyze all the array and add the where clause to the query
		//If secured = true, the function will prepared a prepared statement, replace all the value by "?"
		for(int i=0;i<_sWhere.length;i++)
		{
			l_sOut += _sWhere[i][0] +  " " +_sWhere[i][1];
			if(_bsecured)
			{
				l_sOut += " ? ";
				//Count the element,because after you have to replace these values
				this.argumentCount++;
			}
			else
				l_sOut += " '" + _sWhere[i][2] + "'";
			if(i!=_sWhere.length-1)
				l_sOut += " AND ";
		}
		return l_sOut;
	}	
	
	/**
	 * addOrderByQuery
	 * Add the clause "ORDER BY" in the query
	 * @param String[] _sOrderBy : Array 1D with the format : {"field1","field2"}. 
	 * 							The query sort with the first value, then the second ...
	 * @return : Return a string that contains the clause ORDER BY for the query
	 */
	private String addOrderByQuery(String _sOrderBy[])
	{
		String l_sOut = " ORDER BY ";
		
		//Analyze all the array and add the ORDER BY clause to the query
		for(int i=0;i<_sOrderBy.length;i++)
		{
			l_sOut += " " + _sOrderBy[i];
			if(i<_sOrderBy.length-2)
				l_sOut += ",";
		}		
		return l_sOut;
	}		
	
	/**
	 * addLimitQuery
	 * Add the clause "LIMIT" in the query
	 * @param String[] _Limit : Array 1D with the format {"begin","end"}
	 * @return : Return a string that contains the clause LIMIT for the query
	 */
	private String addLimitQuery(int _Limit[])
	{
		String l_sOut = " LIMIT ";
		l_sOut += _Limit[0] + "," + _Limit[1];
		return l_sOut;
	}
	/**
	 * selectQuery()
	 * Do a selectQuery with the fields,tables and all the clause for the query
	 * @param _sFields
	 * @param _sTable
	 * @param _sWhere
	 * @param _sOrderBy
	 * @param _Limit
	 * @param secured
	 * @return
	 * @throws SQLException
	 */
	private ResultSet selectQuery(String _sFields[],String _sTable[],String _sWhere[][],String _sOrderBy[],int _Limit[],boolean _bsecured) throws SQLException
	{
		String l_sQuery;
		
		//Create the entire query with all the function
		l_sQuery = "SELECT ";
		l_sQuery += addFieldsQuery(_sFields);
		l_sQuery += addTableQuery(_sTable);
		if(_sWhere.length > 0)
			l_sQuery += addWhereQuery(_sWhere,_bsecured);
		if(_sOrderBy.length > 0)
			l_sQuery += addOrderByQuery(_sOrderBy);
		if(_Limit.length > 0)
			l_sQuery += addLimitQuery(_Limit);
		l_sQuery += ";";
		
		//If we want to have a prepare statement, we have to replace all value by the real value in s_Where[][]
		if(_bsecured)
		{
			PreparedStatement pStatement = this.connection.prepareStatement(l_sQuery);
			for(int i = 0;i<this.argumentCount;i++)
				pStatement.setString(i+1, _sWhere[i][2]);
			
			this.argumentCount=0;
			return pStatement.executeQuery();
		}
		else
			return this.statement.executeQuery(l_sQuery);
	}
	/**
	 * decryptText
	 * Use to decrypt the text inputed by the user.
	 * @param String[] _sText : String that contains the text to decrypt
	 * @return : Return an HashMap object that contains all the words of the string with the value.
	 * 				The key of the hashmap is the string word
	 * 				The value is the number after the space
	 */
	private HashMap<String, String> decryptText(String _sText)
	{
		//If the string is null or haven't a "!" return a wrong hashmap
		if(_sText.length()==0 || _sText.charAt(0)!='!')
		{
			HashMap<String, String> l_hs_Out = new HashMap<String, String>(1);
			l_hs_Out.put("0","Paramètre incorrect");
			return l_hs_Out;
		}
			
		String l_sSeparateur = this.sDelimiter;
		String l_sTemp = "";
		String l_sKey = "";
		String l_sValue = "";
		String l_sUnit = "";
		String[] l_sString = _sText.split(l_sSeparateur);
		HashMap<String, String> hs_Out = new HashMap<String, String>(l_sString.length);
		
		//Analyze all the string
		for(int i = 0;i<l_sString.length;i++)
		{
			l_sTemp = l_sString[i];
			l_sKey = l_sTemp.substring(1, l_sTemp.indexOf(' '));//Key is the word string
			
			//If the string is "distance", it could have a unity 
			if(l_sKey.matches("distance"))
			{
				l_sValue = l_sTemp.substring(l_sTemp.indexOf(' '),l_sTemp.length()-2);
				l_sUnit = l_sTemp.substring(l_sTemp.length()-2, l_sTemp.length());
				if(l_sUnit.matches("(km)$")){//Convert the km to year light
					double l_dAnneeLumiere = Double.parseDouble(l_sValue)/kdAL_KM;
					l_sValue = String.valueOf(l_dAnneeLumiere);
				}
			}
			else
				l_sValue = l_sTemp.substring(l_sTemp.indexOf(' '),l_sTemp.length());
			System.out.println("Key -> " + l_sKey + "\tValue -> " + l_sValue + "\n");
			hs_Out.put(l_sKey, l_sValue);
		}
		
		return hs_Out;
	}
	

	/** 
	 * starsForCoordinates
	 * Search all the stars that could be in the hemisphere
	 * @param double _lat : It's the latitude of the star's pointer
	 * @param double _lon : It's the longitude of the star's pointer
	 * @return : Return an arraylist that contains all the stars could be possible to see
	 */
	public ArrayList<CelestialObject> starsForCoordinates (Calendar _date, double _dLat, double _dLon) throws SQLException 
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
		
		if(!isDouble(Double.toString(_dLon)) || !isDouble(Double.toString(_dLat)))
			return null;

		boolean secured = true;
		
		String field[] = {"id","StarID","HIP","HD","HR","Gliese","BayerFlamsteed","ProperName","RA","Dec","Distance","PMRA","PMDec","RV","Mag","AbsMag","Spectrum","ColorIndex","X","Y","Z","VX","VY","VZ"};

		String l_condition_stars_visible;	
		String l_Sign;
	    if (_dLat > 0.0)
	    {
	    	l_condition_stars_visible = String.valueOf(90.0 - _dLat);
	    	l_Sign = ">";
	    }
	    else
	    {
	    	l_condition_stars_visible = String.valueOf(-90.0 - _dLat);
	    	l_Sign = "<";
	    }
		
		String where[][] = {{"dec",l_Sign,l_condition_stars_visible}};
		
		//Injection SQL
		//String where[][] = {{"id","=","'UNION SELECT * FROM stars WHERE id = 1 ;--"},
		//		{"ProperName","LIKE","A%"}};
		secured = false;
	
		String orderby[]={"dec","DESC"}; 
		
		int limit[] = {};
		String table[] = {"stars"};
		ResultSet result = selectQuery(field,table,where,orderby,limit,secured);
		
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
	    	Mathematics l_calc = new Mathematics(_date,_dLat, _dLon,l_star.getDec(),l_star.getRA());
	    	l_star.setXReal(l_calc.getX());
	    	l_star.setYReal(l_calc.getY());
	    	l_calc.get_all();
	    	al_stars.add(l_star);
	    	l_star = null;
	    	l_calc = null;
		}
		result.close();
		
		return al_stars;		
	}
	/*
	/**
	 * starsForText
	 * Search all the stars that has all the condition of the string
	 * @param String[] _searchText : The string that will be analyze by the function decryptText
	 * @return : Return an arraylist of ClestialObject.
	 * @throws SQLException
	 */
	public ArrayList<CelestialObject> starsForText (String _searchText,Calendar _date, double _dLat, double _dLon) throws SQLException 
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
		
		boolean secured = true;
		
		HashMap<String, String> hm_sWhere = decryptText(_searchText);
		Iterator<String> l_it = hm_sWhere.keySet().iterator();
		
		while(l_it.hasNext())
		{
			String l_sTemp = l_it.next().toString();
			System.out.println(l_sTemp + " -> " + hm_sWhere.get(l_sTemp).toString());
		}
		
		String field[] = {"id","StarID","HIP","HD","HR","Gliese","BayerFlamsteed","ProperName","RA","Dec","Distance","PMRA","PMDec","RV","Mag","AbsMag","Spectrum","ColorIndex","X","Y","Z","VX","VY","VZ"};

		String l_condition_stars_visible;	
		String l_Sign;
	    if (_dLat > 0.0)
	    {
	    	l_condition_stars_visible = String.valueOf(90.0 - _dLat);
	    	l_Sign = ">";
	    }
	    else
	    {
	    	l_condition_stars_visible = String.valueOf(-90.0 - _dLat);
	    	l_Sign = "<";
	    }
	    
		String where[][] = {{"dec",l_Sign,l_condition_stars_visible},
				{"ProperName","LIKE",_searchText}};
		
		String orderby[]={"id","ProperName","DESC"}; 
		
		int limit[] = {0,1};
		String table[] = {"stars"};
		ResultSet result = selectQuery(field,table,where,orderby,limit,secured);
		
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
	    	Mathematics l_calc = new Mathematics(_date,_dLat, _dLon,l_star.getDec(),l_star.getRA());
	    	l_star.setXReal(l_calc.getX());
	    	l_star.setYReal(l_calc.getY());
	    	al_stars.add(l_star);
	    	l_star = null;
	    	l_calc = null;
		}
		result.close();
		
		return al_stars;		
	}
}
