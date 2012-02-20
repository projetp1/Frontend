package com.github.projetp1;

import java.sql.*;
import java.util.ArrayList;

public class Test {

	public static void main(String[] args) throws Exception {
		 	Class.forName("org.sqlite.JDBC");
		    DataBase db = new DataBase();
		    Connection connection = db.getConnection();
		    Statement statement = db.getStatement();
		    
		    //CelestialObject test = new CelestialObject(1,0,-1,-1,-1,-1,-1,"Sol",0,0,0.000004848,0,0,0,-26.73,4.85,"G2V",0.656,0,0,0,0,0,0);//-1 = null car dans ces champs, cette valeur est impossible !
		    //test.getHeader();
		    //test.getInfos();
		    
		    ArrayList<CelestialObject> stars = new ArrayList<CelestialObject>();
		    stars = db.starsForCoordinates(statement,01111111111111111111111.0, -2.0); 

		    stars.get(0).getHeader();
		    
		    for(int i=0;i<stars.size();i++)
		    	stars.get(i).getInfos();
		    
		    connection.close();
		  }
	}
