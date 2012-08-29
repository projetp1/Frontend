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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataBase
{
	private String sDataBase;
	private String sDelimiter;
	private String sTable;
	private Connection connection;
	private Statement statement;
	private ArrayList<CelestialObject> allStars = new ArrayList<CelestialObject>();
	private ArrayList<CelestialObject> allConstellations = new ArrayList<CelestialObject>();
	private Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private int argumentCount;
	static private final double kdAL_KM = 9435053029704.605;// Source wikipédia

	/**
	 * Database Constructor
	 * 
	 * @param _sDataBase
	 *            It's the name of the database that will use
	 * @param _sDelimiter
	 *            It's the delimiter of the string of the searchbar
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public DataBase(String _sDataBase, String _sDelimiter) throws SQLException,
			ClassNotFoundException
	{
		try
		{
			Class.forName("org.sqlite.JDBC");// Load the drivers for SQLite
			this.argumentCount = 0;
			this.sDataBase = _sDataBase;
			this.sDelimiter = _sDelimiter;
			this.sTable = "stars";
		}
		catch (ClassNotFoundException e)
		{
			log.warning("Can't load the SQLite's drivers !");
			System.exit(1);
		}

		try
		{
			openConnection();
		}
		catch (SQLException e)
		{
			log.warning("Impossible to open a connection !");
			System.exit(1);
		}

		try
		{
			this.statement = createStatement();
		}
		catch (SQLException e)
		{
			log.warning("Impossible to execute SQL queries !");
			System.exit(1);
		}

		// Generate the ArrayList with all the stars
		int l_Bayer = 0;
		int l_id;
		int l_HIP;
		int l_HD;
		int l_HR;
		String l_ProperName;
		double l_dRA;
		double l_Dec;
		double l_dDistance;
		double l_dMag;
		double l_dColorIndex;

		String[] field = { "*" };
		String[] table = { this.sTable };
		String[][] where = {};
		String[] orderby = {"id"};
		int[] limit = {};
		boolean secured = true;
		ResultSet result = selectQuery(field, table, where, orderby, limit, secured);

		while (result.next())
		{
			l_id = result.getInt("id");
			l_HIP = result.getInt("HIP");
			l_HD = result.getInt("HD");
			l_HR = result.getInt("HR");
			l_ProperName = result.getString("ProperName");
			l_dRA = result.getDouble("RA");
			l_Dec = result.getDouble("Dec");
			l_dDistance = result.getDouble("Distance");
			l_dMag = result.getDouble("Mag");
			l_dColorIndex = result.getDouble("ColorIndex");

			CelestialObject l_star = new CelestialObject(l_id, l_HIP, l_HD, l_HR, l_ProperName,
					l_dRA, l_Dec, l_dDistance, l_dMag, l_dColorIndex,l_Bayer);
			this.allStars.add(l_star);
		}
		
		//Now the same for the constellations
		l_HIP = 0;
		l_HD = 0;
		l_HR = 0;
		l_dRA = 0;
		l_Dec = 0;
		l_ProperName = "";
		l_id = 0;
		l_dDistance = 0.0;
		l_dMag = 0;
		l_dColorIndex = 0;
		
		String[] field2 = { "*" };
		String[] table2 = {"constellations"};
		String[][] where2 = {};
		String[] orderby2 = { "ProperName", "Bayer" };
		int[] limit2 = {};
		secured = true;//No entries from the user ...
		
		result = selectQuery(field2, table2, where2, orderby2, limit2, secured);

		while (result.next())
		{
			l_HIP = result.getInt("HIP");
			l_HD = result.getInt("HD");
			l_HR = result.getInt("HR");
			l_ProperName = result.getString("ProperName");
			l_dRA = result.getDouble("RA");
			l_Dec = result.getDouble("Dec");
			l_Bayer = result.getInt("Bayer");

			CelestialObject l_star = new CelestialObject(l_id, l_HIP, l_HD, l_HR, l_ProperName,
					l_dRA, l_Dec, l_dDistance, l_dMag, l_dColorIndex,l_Bayer);
			this.allConstellations.add(l_star);
		}
		result.close();
	}

	/**
	 * You can close the connection of a connection object
	 * 
	 * @throws SQLException
	 */
	public void closeConnection() throws SQLException
	{
		this.connection.close();
	}

	/**
	 * You can open the connection of a connection object. After that, you can do everything in the
	 * database
	 * 
	 * @throws SQLException
	 */
	public void openConnection() throws SQLException
	{
		this.connection = createConnection();
	}

	/**
	 * After open a connection, you have to load the database and create a connection to this
	 * 
	 * @throws SQLException
	 * @return return a Connection object that has loaded the database
	 */
	private Connection createConnection() throws SQLException
	{
		return DriverManager.getConnection("jdbc:sqlite:res/" + this.sDataBase);
	}

	/**
	 * When you have create a connection, load the database you can now do sql queries
	 * 
	 * @throws SQLException
	 * @return return a Statement object that will be use for queries
	 */
	private Statement createStatement() throws SQLException
	{
		return this.connection.createStatement();
	}

	/**
	 * Used for execute a Regex. For example when you want to know if there is a unity for the value
	 * 
	 * @param _sRegex
	 *            It's the regex that the function will use
	 * @param _sString
	 *            It's the string that will be analyze by the regex
	 * @return Return a boolean with true if the regex has found the string or return false
	 */
	private boolean executeRegex(String _sRegex, String _sString)
	{
		Pattern l_pattern = Pattern.compile(_sRegex);
		Matcher l_matcher = l_pattern.matcher(_sString);

		return l_matcher.matches();
	}

	/**
	 * Say if it's a Double or not with a regex.
	 * 
	 * @param _sString
	 *            It's the string that will be analyze
	 * @return Return a boolean with true if the regex has found that the string is a double or
	 *         return false
	 */
	private boolean isDouble(String _sString)
	{
		String l_sRegex = "[-+]?[0-9]*\\.?[0-9]+";
		return executeRegex(l_sRegex, _sString);
	}

	/**
	 * Add to the query the fields that you want to have.
	 * 
	 * @param _sFields
	 *            Array that contains the fields that you want to have in the query
	 * @return Return the string that contains the fields for the query
	 */
	private String addFieldsQuery(String _sFields[])
	{
		String l_sOut = "";
		// Analyze all the array and add the fields to the query
		for (int i = 0; i < _sFields.length; i++)
		{
			l_sOut += _sFields[i];
			if (i != _sFields.length - 1)
				l_sOut += ",";
		}
		return l_sOut;
	}

	/**
	 * Add the table that will be used in the query
	 * 
	 * @param _sTable
	 *            The array where the tables are
	 * @return Return a string that contains the table for the query
	 */
	private String addTableQuery(String _sTable[])
	{
		String l_sOut = " FROM ";
		// Analyze all the array and had the tables to the query
		for (int i = 0; i < _sTable.length; i++)
		{
			l_sOut += _sTable[i];
			if (i != _sTable.length - 1)
				l_sOut += ",";
		}
		return l_sOut;
	}

	/**
	 * Add the clause "WHERE" to the query
	 * 
	 * @param _sWhere
	 *            Array 2D with the format : {{"field","operator","value"},{...}}
	 * @param secured
	 *            True if you want a prepared statement or false for a basic statement
	 * @return Return a string that contains the clause WHERE for the query
	 */
	private String addWhereQuery(String _sWhere[][], boolean _bsecured)
	{
		String l_sOut = " WHERE ";

		// Analyze all the array and add the where clause to the query
		// If secured = true, the function will prepared a prepared statement, replace all the value
		// by "?"
		for (int i = 0; i < _sWhere.length; i++)
		{
			l_sOut += "LOWER(" + _sWhere[i][0] + ")" + " " + _sWhere[i][1];
			if (_bsecured)
			{
				l_sOut += " ? ";
				// Count the element,because after you have to replace these values
				this.argumentCount++;
			}
			else
				l_sOut += " '" + _sWhere[i][2] + "'";
			if (i != _sWhere.length - 1)
				l_sOut += " AND ";
		}
		return l_sOut;
	}

	/**
	 * Add the clause "ORDER BY" in the query
	 * 
	 * @param _sOrderBy
	 *            Array 1D with the format : {"field1","field2"}. The query sort with the first
	 *            value, then the second ...
	 * @return Return a string that contains the clause ORDER BY for the query
	 */
	private String addOrderByQuery(String _sOrderBy[])
	{
		String l_sOut = " ORDER BY ";

		// Analyze all the array and add the ORDER BY clause to the query
		for (int i = 0; i < _sOrderBy.length; i++)
		{
			l_sOut += " " + _sOrderBy[i];
			if (i < _sOrderBy.length - 1)
				l_sOut += ",";
		}
		return l_sOut;
	}

	/**
	 * Add the clause "LIMIT" in the query
	 * 
	 * @param _Limit
	 *            Array 1D with the format {"begin","end"}
	 * @return Return a string that contains the clause LIMIT for the query
	 */
	private String addLimitQuery(int _Limit[])
	{
		String l_sOut = " LIMIT ";
		l_sOut += _Limit[0] + "," + _Limit[1];
		return l_sOut;
	}

	/**
	 * Do a selectQuery with the fields,tables and all the clause for the query
	 * 
	 * @param _sFields
	 *            Array that contains the fields that you want to have in the query
	 * @param _sTable
	 *            The array where the tables are
	 * @param _sWhere
	 *            Array 2D with the format : {{"field","operator","value"},{...}}
	 * @param _sOrderBy
	 *            Array 1D with the format : {"field1","field2"}. The query sort with the first
	 *            value, then the second ...
	 * @param _Limit
	 *            Array 1D with the format {"begin","end"}
	 * @param secured
	 *            True if you want a prepared statement or false for a basic statement
	 * @throws SQLException
	 * @return The ResultSet of the query
	 */
	private ResultSet selectQuery(String _sFields[], String _sTable[], String _sWhere[][],
			String _sOrderBy[], int _Limit[], boolean _bsecured) throws SQLException
	{
		String l_sQuery;
		try
		{
			// Create the entire query with all the function
			l_sQuery = "SELECT ";
			l_sQuery += addFieldsQuery(_sFields);// Obligatory string for the query
			l_sQuery += addTableQuery(_sTable);// Obligatory string for the query
			if (_sWhere.length > 0)
				l_sQuery += addWhereQuery(_sWhere, _bsecured);
			if (_sOrderBy.length > 0)
				l_sQuery += addOrderByQuery(_sOrderBy);
			if (_Limit.length > 0)
				l_sQuery += addLimitQuery(_Limit);
			l_sQuery += ";";

			// If we want to have a prepare statement, we have to replace all value by the real
			// value in s_Where[][]
			if (_bsecured)
			{
				PreparedStatement pStatement = this.connection.prepareStatement(l_sQuery);
				for (int i = 0; i < this.argumentCount; i++)
					pStatement.setString(i + 1, _sWhere[i][2]);

				this.argumentCount = 0;
				return pStatement.executeQuery();
			}
			else
				return this.statement.executeQuery(l_sQuery);
		}
		catch (SQLException e)
		{
			log.warning("Error in the query ! : " + e.getLocalizedMessage());
			this.statement.close();

			l_sQuery = "SELECT * from " + this.sTable + " WHERE id=0"; // Return an empty Result
			return this.statement.executeQuery(l_sQuery);
		}
	}

	/**
	 * Use to decrypt the text inputed by the user.
	 * 
	 * @param _sText
	 *            String that contains the text to decrypt
	 * @return Return an HashMap object that contains all the words of the string with the value.
	 *         The key of the hashmap is the string word The value is the number after the space
	 */
	public HashMap<String, String> decryptText(String _sText)
	{
		// If the string is null or haven't a "!" return a wrong hashmap
		if (_sText.length() == 0 || _sText.charAt(0) != '!')
		{
			HashMap<String, String> l_hs_Out = new HashMap<String, String>(1);
			l_hs_Out.put("0", "Incorrect Parameter");
			return l_hs_Out;
		}

		String l_sSeparateur = this.sDelimiter;
		String l_sTemp = "";
		String l_sKey = "";
		String l_sValue = "";
		String l_sUnit = "";
		String[] l_sString = _sText.split(l_sSeparateur);
		HashMap<String, String> hs_Out = new HashMap<String, String>(l_sString.length);

		// Analyze all the string
		for (int i = 0; i < l_sString.length; i++)
		{
			l_sTemp = l_sString[i];
			l_sKey = l_sTemp.substring(1, l_sTemp.indexOf(' '));// Key is the word string

			// If the string is "distance", it could have a unity
			if (l_sKey.matches("distance"))
			{
				l_sValue = l_sTemp.substring(l_sTemp.indexOf(' '), l_sTemp.length() - 2);
				l_sUnit = l_sTemp.substring(l_sTemp.length() - 2, l_sTemp.length());
				if (l_sUnit.matches("(km)$"))// Convert the km to year light
				{
					double l_dAnneeLumiere = Double.parseDouble(l_sValue) / kdAL_KM;
					l_sValue = String.valueOf(l_dAnneeLumiere);
				}
			}
			else
				l_sValue = l_sTemp.substring(l_sTemp.indexOf(' '), l_sTemp.length());

			hs_Out.put(l_sKey, l_sValue);
		}

		return hs_Out;
	}

	/**
	 * Search all the constellations that could be in the hemisphere
	 * 
	 * @param _date
	 *            It's the date that will be use to search the stars
	 * @param _dLat
	 *            It's the latitude of the star's pointer
	 * @param _dLon
	 *            It's the longitude of the star's pointer
	 * @throws SQLException
	 * @return Return an arraylist that contains all the constellations could be possible to see
	 */
	public ArrayList<Constellation> getConstellations(Calendar _date,double _dLat,double _dLon)
	{
		ArrayList<Constellation> al_const = new ArrayList<Constellation>();
		
		int l_Bayer = 0;
		String l_ProperName;
		double l_dRA;
		double l_Dec;

		double l_oldBayer = 1;
		double l_X = 0.0;
		double l_Y = 0.0;
		double l_Xp = 0.0;
		double l_Yp = 0.0;
		boolean l_bStop = false;
		
		int i = 0;
		try
		{
			if (!isDouble(Double.toString(_dLon)) || !isDouble(Double.toString(_dLat)))
				throw new IllegalArgumentException(
						"Error : Illegal latitude/longitude. Please check the coordinates !");
		}
		catch (IllegalArgumentException e)
		{
			log.warning(e.getMessage());
			_dLat = _dLon = 0;
		}

		Mathematics l_calc = new Mathematics(_date, _dLat, _dLon);
		Constellation l_consts = new Constellation();
		
		for (CelestialObject l_const : this.allConstellations)
		{
			l_Bayer = l_const.getBayer();
			l_ProperName = Messages.getString("MainView.Const_" + l_const.getProperName());
			l_dRA = l_const.getRA();
			l_Dec = l_const.getDec();
			
			if(l_Bayer == 1 && l_oldBayer != 1)
				l_bStop = false;
			
			if(!l_bStop)
			{
				if(l_Bayer == 1 && l_oldBayer != 1 || l_Bayer == 0)
				{
					al_const.add(l_consts);
					l_consts = null;
					l_consts = new Constellation();
					i=0;
				}
				
				if(i++==0)
					l_consts.setProperName(l_ProperName);
				
				l_calc.calculateAll(l_Dec,l_dRA);
	
				if (l_calc.getHeight() >= 0)
				{
					l_X = l_calc.getX();
					l_Y = l_calc.getY();
					
					if(l_oldBayer == l_Bayer && i!=1)
						l_consts.addLine(l_Xp, l_Yp, l_X, l_Y);
	
					l_Xp = l_X;
					l_Yp = l_Y;
				}
				else
					l_bStop = true;
				
				l_oldBayer = l_Bayer;
				
				if(l_bStop)
				{
					l_consts = null;
					l_consts = new Constellation();
					i=0;
				}
			}
		}
		return al_const;
	}
	
	/**
	 * Search all the stars that could be in the hemisphere
	 * 
	 * @param _date
	 *            It's the date that will be use to search the stars
	 * @param _dLat
	 *            It's the latitude of the star's pointer
	 * @param _dLon
	 *            It's the longitude of the star's pointer
	 * @throws SQLException
	 * @return Return an arraylist that contains all the stars could be possible to see
	 */
	public ArrayList<CelestialObject> starsForCoordinates(Calendar _date, double _dLat, double _dLon)
			throws SQLException
	{
		ArrayList<CelestialObject> al_stars = new ArrayList<CelestialObject>();

		int l_Bayer = 0;
		int l_id;
		int l_HIP;
		int l_HD;
		int l_HR;
		String l_ProperName;
		double l_dRA;
		double l_Dec;
		double l_dDistance;
		double l_dMag;
		double l_dColorIndex;

		try
		{
			if (!isDouble(Double.toString(_dLon)) || !isDouble(Double.toString(_dLat)))
				throw new IllegalArgumentException(
						"Error : Illegal latitude/longitude. Please check the coordinates !");
		}
		catch (IllegalArgumentException e)
		{
			log.warning(e.getMessage());
			_dLat = _dLon = 0;
		}

		Mathematics l_calc = new Mathematics(_date, _dLat, _dLon);

		for (CelestialObject star : this.allStars)
		{
			l_id = star.getId();
			l_HIP = star.getHIP();
			l_HD = star.getHD();
			l_HR = star.getHR();
			l_ProperName = star.getProperName();
			l_dRA = star.getRA();
			l_Dec = star.getDec();
			l_dDistance = star.getDistance();
			l_dMag = star.getMag();
			l_dColorIndex = star.getColorIndex();

			CelestialObject l_star = new CelestialObject(l_id, l_HIP, l_HD, l_HR, l_ProperName,
					l_dRA, l_Dec, l_dDistance, l_dMag, l_dColorIndex,l_Bayer);

			if (l_id == 1)// Sun
			{
				l_calc.calculatePositionSun();
				l_star.setDec(l_calc.getDeclination());
				l_star.setRA(l_calc.getAscension());
			}
			else if (l_id == 2)// Moon
			{
				l_calc.calculatePositionMoon();
				l_star.setDec(l_calc.getDeclination());
				l_star.setRA(l_calc.getAscension());
				double l_now = l_calc.getMoonBrightness(false);
				double l_yes = l_calc.getMoonBrightness(true);
				if (l_yes > l_now)
					l_now *= -1;

				l_star.setMag(l_now);
			}
			else
				l_calc.calculateAll(l_star.getDec(), l_star.getRA());

			if (l_calc.getHeight() >= 0)
			{
				l_star.setXReal(l_calc.getX());
				l_star.setYReal(l_calc.getY());
				l_star.SetAzimuth(l_calc.getAzimuth());
				l_star.SetHeight(l_calc.getHeight());
				al_stars.add(l_star);
			}

			l_star = null;
		}

		return al_stars;
	}

	/**
	 * Search all the stars that has all the condition of the string
	 * 
	 * @param _searchText
	 *            The string that will be analyze by the function decryptText
	 * @param _date
	 *            It's the date that will be use to search the stars
	 * @param _dLat
	 *            It's the latitude of the star's pointer
	 * @param _dLon
	 *            It's the longitude of the star's pointer
	 * @throws SQLException
	 * @return Return an arraylist of CelestialObject.
	 */
	public ArrayList<CelestialObject> starsForText(String _searchText, Calendar _date,
			double _dLat, double _dLon) throws SQLException
	{
		ArrayList<CelestialObject> al_stars = new ArrayList<CelestialObject>();

		int l_id;
		int l_HIP;
		int l_HD;
		int l_HR;
		String l_ProperName;
		double l_dRA;
		double l_Dec;
		double l_dDistance;
		double l_dMag;
		double l_dColorIndex;

		boolean secured = false;

		HashMap<String, String> hm_sWhere = decryptText(_searchText);
		Iterator<String> l_it = hm_sWhere.keySet().iterator();

		String[] table = { this.sTable };
		String[] field = { "*" };
		String[][] where = new String[hm_sWhere.size()][3];
		String[] orderby = { "id"};
		int[] limit = {};
		secured = true;

		int i = 0;

		while (l_it.hasNext())
		{
			String l_sTemp = l_it.next().toString();
			where[i][0] = l_sTemp;
			where[i][1] = "LIKE";
			where[i][2] = "%" + hm_sWhere.get(l_sTemp).toString().trim() + "%";
			i++;
		}

		ResultSet result = selectQuery(field, table, where, orderby, limit, secured);

		try
		{
			if (!isDouble(Double.toString(_dLon)) || !isDouble(Double.toString(_dLat)))
				throw new IllegalArgumentException(
						"Error : Illegal latitude/longitude. Please check the coordinates !");
		}
		catch (IllegalArgumentException e)
		{
			log.warning(e.getMessage());
			_dLat = _dLon = 0;
		}

		Mathematics l_calc = new Mathematics(_date, _dLat, _dLon);

		try
		{
			while (result.next())
			{
				int l_Bayer = 0;
				l_id = result.getInt("id");
				l_HIP = result.getInt("HIP");
				l_HD = result.getInt("HD");
				l_HR = result.getInt("HR");
				l_ProperName = result.getString("ProperName");
				l_dRA = result.getDouble("RA");
				l_Dec = result.getDouble("Dec");
				l_dDistance = result.getDouble("Distance");
				l_dMag = result.getDouble("Mag");
				l_dColorIndex = result.getDouble("ColorIndex");

				CelestialObject l_star = new CelestialObject(l_id, l_HIP, l_HD, l_HR, l_ProperName,
						l_dRA, l_Dec, l_dDistance, l_dMag, l_dColorIndex,l_Bayer);

				if (l_id == 1)
				{
					l_calc.calculatePositionSun();
					l_star.setDec(l_calc.getDeclination());
					l_star.setRA(l_calc.getAscension());
				}
				else if (l_id == 2)
				{
					l_calc.calculatePositionMoon();
					l_star.setDec(l_calc.getDeclination());
					l_star.setRA(l_calc.getAscension());
					double l_now = l_calc.getMoonBrightness(false);
					double l_yes = l_calc.getMoonBrightness(true);
					if (l_yes > l_now)
						l_now *= -1;

					l_star.setMag(l_now);
				}
				else
					l_calc.calculateAll(l_star.getDec(), l_star.getRA());

				if (l_calc.getHeight() > 0)
				{
					l_star.setXReal(l_calc.getX());
					l_star.setYReal(l_calc.getY());
					l_star.SetAzimuth(l_calc.getAzimuth());
					l_star.SetHeight(l_calc.getHeight());
					al_stars.add(l_star);
				}

				l_star = null;
			}
		}
		catch (SQLException e)
		{
			log.warning("Problem with the query's result !");
		}
		result.close();
		return al_stars;
	}
}
