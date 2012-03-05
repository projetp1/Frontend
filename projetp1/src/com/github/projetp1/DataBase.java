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
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.sql.*;

public class DataBase 
{
	private String sDataBase;
	private Connection connection;
	private Statement statement;
	private int argumentCount = 0;
	private double dAL_KM = 9435053029704.605;//Source wikipédia
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

	private int executeRegex(String _sRegex,String _sString)
	{
        Pattern l_pattern = Pattern.compile(_sRegex);
        Matcher l_matcher = l_pattern.matcher(_sString);
        
        if (l_matcher.matches()) 
        	return 1;
        else
        	return 0;
	}
	
	private int isDouble(String _sString)
	{
		String l_sRegex  = "[-+]?[0-9]*\\.?[0-9]+";
        return executeRegex(l_sRegex,_sString);
	}
	
	private String addFieldsQuery(String _sFields[])
	{
		String l_sOut = "";
		for(int i = 0;i<_sFields.length;i++)
		{
			l_sOut += _sFields[i];
			if(i!=_sFields.length -1)
				l_sOut += ",";
		}	
		return l_sOut;
	}
	
	private String addTableQuery(String _sTable[])
	{
		String l_sOut = " FROM ";
		for(int i = 0;i<_sTable.length;i++)
		{
			l_sOut += _sTable[i];
			if(i!= _sTable.length -1)
				l_sOut += ",";
		}
		return l_sOut;		
	}
	
	private String addWhereQuery(String _sWhere[][],boolean secured)
	{
		String l_sOut = " WHERE ";

		for(int i=0;i<_sWhere.length;i++)
		{
			l_sOut += _sWhere[i][0] +  " " +_sWhere[i][1];
			if(secured)
			{
				l_sOut += " ? ";
				this.argumentCount++;
			}
			else
				l_sOut += " '" + _sWhere[i][2] + "'";
			if(i!=_sWhere.length-1)
				l_sOut += " AND ";
		}
		return l_sOut;
	}	

	private String addOrderByQuery(String _sOrderBy[])
	{
		String l_sOut = " ORDER BY ";
		
		for(int i=0;i<_sOrderBy.length;i++)
		{
			l_sOut += " " + _sOrderBy[i];
			if(i<_sOrderBy.length-2)
				l_sOut += ",";
		}		
		return l_sOut;
	}		
	
	private String addLimitQuery(int _Limit[])
	{
		String l_sOut = " LIMIT ";
		l_sOut += _Limit[0] + "," + _Limit[1];
		return l_sOut;
	}
	
	private ResultSet selectQuery(String _sFields[],String _sTable[],String _sWhere[][],String _sOrderBy[],int _Limit[],boolean secured) throws SQLException
	{
		String l_sQuery;
		
		l_sQuery = "SELECT ";
		l_sQuery += addFieldsQuery(_sFields);
		l_sQuery += addTableQuery(_sTable);
		l_sQuery += addWhereQuery(_sWhere,secured);
		l_sQuery += addOrderByQuery(_sOrderBy);
		l_sQuery += addLimitQuery(_Limit);
		l_sQuery += ";";
		
		if(secured)
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
	
	private HashMap<String, String> DecryptText(String _sText)
	{
		String l_sSeparateur = ";";
		String l_sTemp = "";
		String l_sKey = "";
		String l_sValue = "";
		String l_sUnit = "";
		String[] l_sString = _sText.split(l_sSeparateur);
		HashMap<String, String> hs_Out = new HashMap<String, String>(l_sString.length);
		
		for(int i = 0;i<l_sString.length;i++)
		{
			l_sTemp = l_sString[i];
			l_sKey = l_sTemp.substring(1, l_sTemp.indexOf(' '));
			l_sValue = l_sTemp.substring(l_sTemp.indexOf(' '),l_sTemp.length()-2);
			
			if(l_sKey.matches("distance"))
			{
				l_sUnit = l_sTemp.substring(l_sTemp.length()-2, l_sTemp.length());
				if(l_sUnit.matches("(km)$")){
					double l_dAnneeLumiere = Double.parseDouble(l_sValue)/dAL_KM;
					System.out.println(l_dAnneeLumiere);
				}
			}
			
			hs_Out.put(l_sKey, l_sValue);
		}
		
		return hs_Out;
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
		
		boolean secured = false;
		
		String field[] = {"id","StarID","HIP","HD","HR","Gliese","BayerFlamsteed","ProperName","RA","Dec","Distance","PMRA","PMDec","RV","Mag","AbsMag","Spectrum","ColorIndex","X","Y","Z","VX","VY","VZ"};

		String where[][] = {{"id","<","1100"},
				{"ProperName","LIKE","A%"}};
		
		//Injection SQL
		/*String where[][] = {{"id","=","'UNION SELECT * FROM stars WHERE id = 1 ;--"},
				{"ProperName","LIKE","A%"}};
		secured = true;
		*/
		
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
	    	al_stars.add(l_star);
	    	l_star = null;
		}
		result.close();
		
		return al_stars;		
	}
	
	public ArrayList<CelestialObject> starsForText (String _searchText) throws SQLException 
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
		
		HashMap<String, String> hm_sWhere = DecryptText("!distance 1km");
		Iterator<String> l_it = hm_sWhere.keySet().iterator();
		
		while(l_it.hasNext())
		{
			String l_sTemp = l_it.next().toString();
			System.out.println(l_sTemp + " -> " + hm_sWhere.get(l_sTemp).toString());
		}
		
		String field[] = {"id","StarID","HIP","HD","HR","Gliese","BayerFlamsteed","ProperName","RA","Dec","Distance","PMRA","PMDec","RV","Mag","AbsMag","Spectrum","ColorIndex","X","Y","Z","VX","VY","VZ"};

		String where[][] = {{"id","<","1100"},
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
	    	al_stars.add(l_star);
	    	l_star = null;
		}
		result.close();
		
		return al_stars;		
	}
}
