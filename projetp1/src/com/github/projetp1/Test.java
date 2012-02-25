package com.github.projetp1;

import java.util.ArrayList;

public class Test {

	public static void main(String[] args) throws Exception {
			String sDataBase = "hyg.db";
		 	Class.forName("org.sqlite.JDBC");
		    DataBase db = new DataBase(sDataBase);
		    
		    //CelestialObject test = new CelestialObject(1,0,-1,-1,-1,-1,-1,"Sol",0,0,0.000004848,0,0,0,-26.73,4.85,"G2V",0.656,0,0,0,0,0,0);//-1 = null car dans ces champs, cette valeur est impossible !
		    
		    ArrayList<CelestialObject> stars = new ArrayList<CelestialObject>();
		    stars = db.starsForCoordinates(1.0, -2.0); 

		    if(stars.size() != 0)
		    {
			    stars.get(0).getHeader();
			    
			    for(int i=0;i<stars.size();i++)
			    	stars.get(i).getInfos();
		    }
		    else
		    	System.out.println("Aucun résultat n'a été trouvé dans la base de données");

		    
		    db.closeConnection();
		  }
	}
