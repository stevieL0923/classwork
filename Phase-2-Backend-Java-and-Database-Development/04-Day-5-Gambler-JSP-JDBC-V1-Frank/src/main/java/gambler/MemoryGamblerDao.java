package gambler;

/*****************************************************************************************************************
 * Implement methods for manipulating are returning data from the gambler table
 * At minimum, implement the methods required by the GamblerDao interface
 
    gambler table as defined in the database
  	
 	+----------------+--------------+------+-----+---------+----------------+
    | Field          | Type         | Null | Key | Default | Extra          |
    +----------------+--------------+------+-----+---------+----------------+
    | id             | smallint     | NO   | PRI | NULL    | auto_increment |
    | gambler_name   | char(20)     | NO   |     | NULL    |                |
    | address        | char(20)     | YES  |     | NULL    |                |
    | birth_date     | date         | NO   |     | NULL    |                |
    | monthly_salary | decimal(9,2) | NO   |     | NULL    |                |
    +----------------+--------------+------+-----+---------+----------------+

 *****************************************************************************************************************/

import databaseerror.DataBaseErrorLog;
import errorlog.ErrorLog;
import exceptions.DataBaseDeleteException;
import exceptions.DataBaseInsertException;
import exceptions.DataBaseUpdateException;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;



public class MemoryGamblerDao implements GamblerDao {
	
	// define a reference to the DataSource object
	
	/******************************************************************************
	 * static so we only get one database, highestGablerId and DataBaseErrorLog
	 *    regardless of the number of processes using the DAO in the application
     ******************************************************************************/     
	private static List<Gambler> theDataBase = null;
	
	private static int highestGamblerId = -1;
	
	private static DataBaseErrorLog theDatabaseErrorLog = null;

	/******************************************************************************
	 * Constructor
     ******************************************************************************/
	public MemoryGamblerDao() throws IOException {
		if(theDataBase == null) {               // If data source is not defined...
			this.theDataBase = new ArrayList(); //    define it
			setupTheDataBase();                 //    and set it up
		}
		if(theDatabaseErrorLog == null) { // If the Data base Error log not defined
			this.theDatabaseErrorLog = new DataBaseErrorLog("database-"); // define it
		}
	}

	@Override  // Ask Java to be sure we are correctly providing the methods define in the interface
	public List<Gambler> getAllGamblers() {
        // Create a deep copy of the DataBase to return
		// rather than return the reference
		// to protect the member data from unintentional access
		
		List<Gambler> copyOfDataBase = new ArrayList();
		
		for(Gambler aGambler : theDataBase) {
			copyOfDataBase.add(new Gambler(aGambler)); // add a deep copy of Gambler to the list
		}
		return copyOfDataBase;
	}

	@SuppressWarnings("finally")  // This is to stop Eclipse from nagging about a problem that isn't a problem
	@Override
	// This method receives a gambler object containing the gambler info to be added
	//             returns whether the row was successfully added or not
	public boolean addGambler(Gambler gamblerToAdd) throws DataBaseInsertException {
		
		// Create a deep copy of Gambler to be added
		Gambler aNewGambler = new Gambler(gamblerToAdd);
		
		boolean didAddToDataBaseWork = false;
		
		highestGamblerId++;                   // Calculate new Gambler's Id
		aNewGambler.setId(highestGamblerId);  // Assign new Gambler Id to Gambler
		
		try {
		// Add new Gambler to the data base
			theDataBase.add(aNewGambler);
			
			didAddToDataBaseWork = true;
		}
		catch (NullPointerException exceptionObject) {
			didAddToDataBaseWork = false;
			
			throw new DataBaseInsertException("Cannot add a null object to data base");
		}
		finally {
			return didAddToDataBaseWork;
		}
	}
	
	@Override
	public Gambler findGamblerById(long id) {
	
		Gambler theGambler = null;
		
		for(Gambler currentGambler : theDataBase) {
			if(currentGambler.getId() == id) {
				//System.out.println("Found: " + currentGambler);  // debugging purpose message
				theGambler = new Gambler(currentGambler);
				//System.out.println("returning: " + theGambler);  // debugging purpose message
		}
		}
	    
	    // Return the object with the values from row in the data set
		return theGambler;
	}

	@Override
	public List<Gambler> findGamblerByName(String searchName) {
		
		// Define an object hold the return value
		List<Gambler> gamblersFound = new ArrayList();	
	
		for(Gambler currentGambler : theDataBase) {
			if(currentGambler.getName().contains(searchName)) {  // find a full or partial name
				gamblersFound.add(new Gambler(currentGambler));
			}
		}
	    
	    // Return the object with the values from row in the data bas	
		return gamblersFound;
	}

	@Override	
	// This method receives an gambler object with changed and unchanged data including change
	// We do this so we don't don't have to worry about what has changed
	//    The application is what worries about what has changed
	//    All we do is send and receive data to data base
	// Application logic does not belong in a DOA
	public void update(Gambler gamblerPassed) throws DataBaseUpdateException, DataBaseDeleteException {
	
		Gambler updatedGambler = new Gambler(gamblerPassed);
		
		// Try to find existing Gambler in the data base
		Gambler existingGambler = findGamblerById(gamblerPassed.getId());
		
		// Remove existing Gambler from the data base
		
		try {
			delete(gamblerPassed.getId());
		} 
		catch (DataBaseDeleteException exceptionObject)	{
			theDatabaseErrorLog.writeToDatabaseErrorLog("Cannot find Gambler you are trying to up in the database: Id=" 
                                                       + gamblerPassed.getId()
                                                       +" Name: " + gamblerPassed.getName());
			theDatabaseErrorLog.writeExceptionInfoToDatabaseErrorLog(exceptionObject);
			
			throw new DataBaseUpdateException("Error updating data base; Please review the Data Base Error Log: "
					                         + theDatabaseErrorLog.getErrorLogFileName());
		}
		// Add updated Gambler to the data base
		theDataBase.add(gamblerPassed);	
	}

	@Override
	public void delete(long idToBeDeleted) throws DataBaseDeleteException {
	
		Gambler existingGambler = findGamblerById(idToBeDeleted);
				
		if(existingGambler == null) {

			theDatabaseErrorLog.writeToDatabaseErrorLog("Cannot find Gambler you are trying to delete in the database: Id=" + idToBeDeleted);	

			throw new DataBaseDeleteException("Error deleting from data base: Gambler Id: " + idToBeDeleted
					                        + "\n Please review the Data Base Error Log: \n\t"
                                            + theDatabaseErrorLog.getErrorLogFileName());
		}
		theDataBase.remove(existingGambler);
	}		
	
	private void setupTheDataBase() {
		DateTimeFormatter EurDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		 
		theDataBase.add(new Gambler(54 , "Neil Bransfield", "Seattle, WA"    , LocalDate.parse("1959-03-11", EurDateFormat) , 5000.00  ));
		theDataBase.add(new Gambler(137, "Phil Donahuge"  , null             , LocalDate.parse("1946-12-29", EurDateFormat) , 3250.25  ));
		theDataBase.add(new Gambler(382, "Stickman Nelson", "Cumberland, MD" , LocalDate.parse("1955-10-21", EurDateFormat) , 12983.75 ));
		theDataBase.add(new Gambler(292, "Betina Chavez"  , "Fresno, CA"     , LocalDate.parse("1963-09-09", EurDateFormat) , 2950.00  ));
		theDataBase.add(new Gambler(12 , "T Judson Smith" , "Los Angeles, CA", LocalDate.parse("1972-05-01", EurDateFormat) , 9839.87  ));
		theDataBase.add(new Gambler(49 , "Dana Imori"     , null             , LocalDate.parse("1989-02-13", EurDateFormat) , 8456.87  ));
		theDataBase.add(new Gambler(191, "Frank Mint"     , "El Paso, TX"    , LocalDate.parse("1993-04-28", EurDateFormat) , 9200.00  ));
		theDataBase.add(new Gambler(572, "Al Mostbroke"   , "Clayton, MO"    , LocalDate.parse("1987-01-18", EurDateFormat) , 28950.00 ));
		theDataBase.add(new Gambler(74 , "Red Squiggledoc", "Java, Indonesia", LocalDate.parse("1996-01-23", EurDateFormat) , 100000.00));
	
		for(Gambler aGambler : theDataBase) {
			if(highestGamblerId < aGambler.getId()) {
				highestGamblerId = aGambler.getId();
			}
		}
		}

	
} // End of class
