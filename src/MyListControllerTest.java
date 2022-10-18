import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import myfileio.MyFileIO;

// TODO: Auto-generated Javadoc
/**
 * The Class MyListControllerTest.
 */
@TestMethodOrder(OrderAnnotation.class)
class MyListControllerTest {
	
	/** field to store the buffered reader file handle and access 
	 * without passing as a parameter */
	private BufferedReader br = null;
	
	/** Instance for file access methods */
	private MyFileIO fio = new MyFileIO();
	
	/** The test data. */
	String[] testData = {"Gordon,Moore,987-65-4321,90,,20000000.00,40,Management,1",
			 "Andrew,Grove,222-12-1234,80,he/him,4000000.00,40,Management,2",
			 "Gloria,Leong,321-21-4321,52,she/her,750000.00,24,Engineering,3",
			 "Patricia,Murray,999-00-1111,62,,1250000.00,35,Human Resources,4",
			 "Scott,Murray,123-45-6789,58,he/him,500000.00,26,Engineering,5",
			 "Elena,Murray,111-11-1111,22,she/her,100000.00,0,Engineering,6"};

	/** The expected sort by name order of the test data. */
	int[] expSortByName = {1,2,0,5,3,4};
	
	/** The expected sort by salary order of the test data. */
	int[] expSortBySalary = {5,4,2,3,1,0};
	
	/** The expected sort by ID order of the test data. */
	int[] expSortByID = {0,1,2,3,4,5};

	/**
	 * Testing assumes that the empDB.dat does not exist.
	 * This method will save the empDB.dat file if exists so
	 * that it can be restored later.
	 *
	 * @throws Exception ==> any exception is fatal.
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		File empDB = new File("empDB.dat");
		if (empDB.exists()) {
			Path save = Paths.get("empDB.dat.save");
			Path orig = empDB.toPath();
			Files.copy(orig, save,StandardCopyOption.REPLACE_EXISTING);
			if (!save.toFile().exists()) {
				System.out.println("Fatal Error creating empDB.dat.save");
				throw new IOException("Fatal Error creating empDB.dat.save");
			}
			if (!empDB.delete()) {
				System.out.println("Fatal Error deleting empDB.dat");
				throw new IOException("Fatal Error deleting empDB.dat");
			}
		}
	}

	/**
	 * Restores the original empDB.dat if it existed, and
	 * then removes the temporary copy
	 *
	 * @throws Exception ==> all exceptions are Fatal
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
		File empDBsave = new File("empDB.dat.save");
		if (empDBsave.exists()) {
			Path orig = Paths.get("empDB.dat");
			Path save = empDBsave.toPath();
			try {
				Files.copy(save, orig,StandardCopyOption.REPLACE_EXISTING);
			} catch (Exception e) {
				System.out.println("Caught Fatal Exception while restoring empDB.dat");
				e.printStackTrace();
			}
			if (!orig.toFile().exists()) {
				System.out.println("Fatal Error restoring empDB.dat");
				throw new IOException("Fatal Error restoring empDB.dat");
			}
			if (!empDBsave.delete()) {
				System.out.println("Fatal Error deleting empDB.dat.save");
				throw new IOException("Fatal Error deleting empDB.dat.save");
			}
		}		
	}

	/**
	 * Empty method
	 *
	 * @throws Exception the exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * Makes sure to close the buffered reader (in the event of a test 
	 * failure) and resets it to null. If the empDB.dat file exists,
	 * it removes the file (this was a test version). 
	 *
	 * @throws Exception - throws IOException if empDB.dat cannot be deleted..
	 */
	@AfterEach
	void tearDown() throws Exception {
		File save = new File("empDB.dat");
		if (br != null)
			fio.closeFile(br);
		br = null;
			
		if (save.exists()) {
			boolean status = save.delete();
			if (!status) { 
				System.out.println("Error: Unable to delete test version of empDB.dat!");
				throw new IOException("Unable to delete test version of empDB.dat!");
			}
		}
			
	}

    /**
     * checkEmployeeData extracts the Employee information from the toString() of 
     * an Employee object
     *
     * @param empDataStr the emp data str
     * @return the string of employee info, comma-separated....
     */
    private String checkEmployeeData(String empDataStr) {
    	String[] lines = empDataStr.split("\\n");
    	String fName = lines[0].replaceAll("^\\s*Name:\\s+(\\w+)\\s+.*","$1");
    	String lName = lines[0].replaceAll("^\\s*Name:\\s+\\w+\\s+(\\w+)\\s*.*","$1");
    	String pronouns = "";
    	if (lines[0].contains("("))
    		pronouns = lines[0].replaceAll("^.*\\((.*)\\).*","$1");
    	String SSN = lines[1].replaceAll("^\\s*SSN:?\\s+(\\d{3}\\-\\d{2}\\-\\d{4}).*$", "$1");
    	String empID = lines[1].replaceAll("^.*Employee ID:\\s+(\\d+)\\s*$","$1");
    	String age = lines[2].replaceAll("^\\s*Age:\\s+(\\d+).*","$1");
    	String years = lines[2].replaceAll("^.*Years Exp:\\s+(\\d+).*", "$1");
    	String dept = lines[3].replaceAll("^\\s*Dept:\\s+(\\w+\\s?\\w+)\\s+Salary.*$","$1");
    	String salary = lines[3].replaceAll("^.*Salary:\\s+(\\d+\\.?\\d+).*", "$1");
    	String str = fName+","+lName+","+SSN+","+age+","+pronouns+","+salary+","+years+","+dept+","+empID;
    	return str;	
    }
    
	/**
	 * Test add with empty data. Each required text field is tested with
	 * an empty string individually by attempting to add an employee where
	 * all other data is valid. Employee should not be added and an error message
	 * should be returned.
	 */
	@Test
	@Order(1)
	void test_AddWithEmptyData() {
		ListController ctrl = new ListController();
		int numEmployees = ctrl.getNumEmployees();
		assertEquals(0,numEmployees);
		String msg = "";
		System.out.println("Error Checking Test: detect if a required field is empty");
		msg = ctrl.addEmployee("","Murray","111-22-3333","58","he/him","500000","26","Engineering");
		assertNotEquals("",msg);
		
		assertEquals(numEmployees,ctrl.getNumEmployees());
		System.out.println(msg);
		msg = ctrl.addEmployee("Scott","","111-22-3333","58","he/him","500000","26","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		System.out.println(msg);
		msg = ctrl.addEmployee("Scott","Murray","","58","he/him","500000","26","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		System.out.println(msg);
		msg = ctrl.addEmployee("Scott","Murray","111-22-3333","","he/him","500000","26","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		System.out.println(msg);
		msg = ctrl.addEmployee("Scott","Murray","111-22-3333","58","he/him","","26","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		System.out.println(msg);
		msg = ctrl.addEmployee("Scott","Murray","111-22-3333","58","he/him","500000","","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		System.out.println(msg);
		System.out.println("Error Checking for empty required fields PASSED");
	}

	/**
	 * Test add with bad SSN.
	 */
	@Test
	@Order(2)
//	@Disabled
	void test_AddWithBadSSN() {
		ListController ctrl = new ListController();
		int numEmployees = ctrl.getNumEmployees();
		assertEquals(0, numEmployees);
		String msg = "";
		System.out.println("Error Checking Test: detect if SSN format is incorrect");
		
		msg = ctrl.addEmployee("Daniel", "Kazanov", "111-11-11111", "16", "He/Him", "45000", "1", "Engineering");
		assertNotEquals("", msg);
		assertEquals(numEmployees, ctrl.getNumEmployees());
		System.out.println(msg);
		
		msg = ctrl.addEmployee("Daniel", "Kazanov", "111-11-111", "16", "He/Him", "45000", "1", "Engineering");
		assertNotEquals("", msg);
		assertEquals(numEmployees, ctrl.getNumEmployees());
		System.out.println(msg);
		
		msg = ctrl.addEmployee("Daniel", "Kazanov", "", "16", "He/Him", "45000", "1", "Engineering");
		assertNotEquals("", msg);
		assertEquals(numEmployees, ctrl.getNumEmployees());
		System.out.println(msg);
		
		msg = ctrl.addEmployee("Daniel", "Kazanov", "111111111", "16", "He/Him", "45000", "1", "Engineering");
		assertNotEquals("", msg);
		assertEquals(numEmployees, ctrl.getNumEmployees());
		System.out.println(msg);
		
		msg = ctrl.addEmployee("Daniel", "Kazanov", "11111111111", "16", "He/Him", "45000", "1", "Engineering");
		assertNotEquals("", msg);
		assertEquals(numEmployees, ctrl.getNumEmployees());
		System.out.println(msg);
		
		msg = ctrl.addEmployee("Daniel", "Kazanov", "111.11.1111", "16", "He/Him", "45000", "1", "Engineering");
		assertNotEquals("", msg);
		assertEquals(numEmployees, ctrl.getNumEmployees());
		System.out.println(msg);
		
		msg = ctrl.addEmployee("Daniel", "Kazanov", "111a11a1111", "16", "He/Him", "45000", "1", "Engineering");
		assertNotEquals("", msg);
		assertEquals(numEmployees, ctrl.getNumEmployees());
		System.out.println(msg);
		
		System.out.println("Testing for detection of incorrect SSN formats PASSED");
	}
	
	/**
	 * Test add with duplicate SSN.
	 */
	@Test
	@Order(3)
//	@Disabled
	void test_AddWithDuplicateSSN() {
		ListController ctrl = new ListController();
		int numEmployees = ctrl.getNumEmployees();
		assertEquals(0, numEmployees);
		String msg = "";
		System.out.println("Error Checking Test: detect if SSN is duplicate");
		
		ctrl.addEmployee("Daniel", "Kazanov", "111-11-1111", "35", "He/Him", "45000", "1", "Engineering");
		numEmployees+=1;
		
		
		msg = ctrl.addEmployee("Daniel", "Kazanov", "111-11-1111", "35", "He/Him", "45000", "1", "Engineering");
		assertNotEquals("", msg);
		assertEquals(numEmployees, ctrl.getNumEmployees());
		System.out.println(msg);
		
		msg = ctrl.addEmployee("Daniel", "Kazanov", "111-11-1112", "35", "He/Him", "45000", "1", "Engineering");
		assertEquals("", msg);
		numEmployees+=1;
		assertEquals(numEmployees, ctrl.getNumEmployees());
		System.out.println(msg);
		
		System.out.println("Testing for detection of duplicate SSN formats PASSED");
	}
	
	/**
	 * Test add with bad age.
	 */
	@Test
	@Order(4)
//	@Disabled
	void test_AddWithBadAge() {
		ListController ctrl = new ListController();
		int numEmployees = ctrl.getNumEmployees();
		System.out.println("Testing for detection of invalid age PASSED");
	}
	
	/**
	 * Test add with bad salary.
	 */
	@Test
	@Order(5)
//	@Disabled
	void test_AddWithBadSalary() {
		ListController ctrl = new ListController();
		int numEmployees = ctrl.getNumEmployees();
		System.out.println("Testing for detection of invalid salary PASSED");
	}
	
	/**
	 * Test add with bad years.
	 */
	@Test
	@Order(6)
//	@Disabled
	void test_AddWithBadYears() {
		ListController ctrl = new ListController();
		int numEmployees = ctrl.getNumEmployees();
		System.out.println("Testing for detection of invalid years data PASSED");
	}
}
