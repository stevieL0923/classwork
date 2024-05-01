package com.yamin.cms;

import java.util.Scanner;
import org.apache.commons.dbcp2.BasicDataSource;

public class JDBCApplication {
    /**************************************************************************************************************
     * Instance variable accessible to all metods in the class
     *************************************************************************************************************/

	// Define a reference to a Scanner object for user interaction via the keyboard
	private Scanner theKeyboard;	

	// Define a reference to a data source object for the database we want to access
	private BasicDataSource yaminGymDataSource;
	
	// Set up Database Connection
	private void setupDataSource() {
		/**************************************************************************************************************
		 * Instantiate and initialize data source for JDBC data base access
		 *************************************************************************************************************/
	
		yaminGymDataSource = new BasicDataSource();         // simple JDC data source
	
		// Set values in the data source for the database manager and database we want to access
		//                    access:dbmgr:server-name:port/databasename
		yaminGymDataSource.setUrl("jdbc:mysql://localhost:3306/yamindb");// connection string
		yaminGymDataSource.setUsername("student");                       // owner of the database
		yaminGymDataSource.setPassword("Java#1Rules");                   // password for owner
	}

}
