package com.github.projetp1;

import java.sql.*;

public class Test {

	public static void main(String[] args) throws Exception {
		 	Class.forName("org.sqlite.JDBC");
		    Connection conn = DriverManager.getConnection("jdbc:sqlite:res/hyg.db");
		    Statement stat = conn.createStatement();
		    //stat.executeUpdate("drop table if exists people;");
		    //stat.executeUpdate("create table people (name, occupation);");
		    //PreparedStatement prep = conn.prepareStatement("insert into people values (?, ?);");

		    /*prep.setString(1, "Gandhi");
		    prep.setString(2, "politics");
		    prep.addBatch();
		    prep.setString(1, "Turing");
		    prep.setString(2, "computers");
		    prep.addBatch();
		    prep.setString(1, "Wittgenstein");
		    prep.setString(2, "smartypants");
		    prep.addBatch();*/

		    conn.setAutoCommit(false);
		    //prep.executeBatch();
		    conn.setAutoCommit(true);

		    ResultSet rs = stat.executeQuery("select * from stars limit 0,10;");
		    while (rs.next()) {
		      System.out.println("name = " + rs.getString("ProperName"));
		      System.out.println("id = " + rs.getString("id"));
		    }
		    rs.close();
		    conn.close();
		  }
	}
