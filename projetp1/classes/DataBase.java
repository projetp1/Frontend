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

	public ArrayList<CelestialObject> starsForCoordinates (double _longitude, double _latitude) {
		return null;		
	}
	
	public ArrayList<CelestialObject> starsForText (String _searchText) {
		return null;		
	}

	public ResultSet Select(Statement _state) throws Exception
	{
		String query = "SELECT * FROM stars ORDER BY id ;";
		ResultSet rs = _state.executeQuery(query);
		return rs;
	}
	
	
	public Connection getConnection()
	{
		return connection;
	}
	
	public Statement getStatement()
	{
		return statement;
	}
}
