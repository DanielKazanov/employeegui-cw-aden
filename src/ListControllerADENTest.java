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
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import myfileio.MyFileIO;

@TestMethodOrder(OrderAnnotation.class)
class ListControllerADENTest {
	private static final String MIN_WAGE = "14.99";
	private BufferedReader br = null;
	private MyFileIO fio = new MyFileIO();
	
	String[] testData = {"Gordon,Moore,987-65-4321,90,,20000000.00,40,Management,1",
			 "Andrew,Grove,222-12-1234,80,he/him,4000000.00,40,Management,2",
			 "Gloria,Leong,321-21-4321,52,she/her,750000.00,24,Engineering,3",
			 "Patricia,Murray,999-00-1111,62,,1250000.00,35,Human Resources,4",
			 "Scott,Murray,123-45-6789,58,he/him,500000.00,26,Engineering,5",
			 "Elena,Murray,111-11-1111,22,she/her,100000.00,0,Engineering,6"};

	int[] expSortByName = {1,2,0,5,3,4};
	int[] expSortBySalary = {5,4,2,3,1,0};
	int[] expSortByID = {0,1,2,3,4,5};

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

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
		File save = new File("empDB.dat");
		if (br != null)
			fio.closeFile(br);
		br = null;
			
		if (save.exists()) {
			boolean status = save.delete();
			if (!status) 
				System.out.println("Error: Unable to delete test version of empDB.dat!");
		}
			
	}

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
//		//The department can never be blank, so no need to test it...
//		msg = ctrl.addEmployee("Scott","Murray","111-22-3333","58","he/him","500000","26","");
//		assertNotEquals("",msg);
//		assertEquals(numEmployees,ctrl.getNumEmployees());
//		System.out.println(msg);
		System.out.println("Error Checking for empty required fields PASSED");
	}

	@Test
	@Order(2)
	void test_AddWithBadSSN() {
		ListController ctrl = new ListController();
		int numEmployees = ctrl.getNumEmployees();
		assertEquals(0,numEmployees);
		String msg = "";
		System.out.println("Testing that incorrect SSN formats will be flagged as errors");
		msg = ctrl.addEmployee("John","Smith","XXX-xx-yyyy","26","","50000.00","5","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		msg = ctrl.addEmployee("John","Smith","98-12-345","26","","50000.00","5","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		msg = ctrl.addEmployee("John","Smith","606-1-4011","26","","50000.00","5","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		msg = ctrl.addEmployee("John","Smith","606-12-401","26","","50000.00","5","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		msg = ctrl.addEmployee("John","Smith","606-12-40115","26","","50000.00","5","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		msg = ctrl.addEmployee("John","Smith","60x-12-4011","26","","50000.00","5","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		msg = ctrl.addEmployee("John","Smith","606-x2-4011","26","","50000.00","5","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		msg = ctrl.addEmployee("John","Smith","606-12-40y1","26","","50000.00","5","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		msg = ctrl.addEmployee("John","Smith","606-12-4011y","26","","50000.00","5","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		msg = ctrl.addEmployee("John","Smith","y606-12-4011","26","","50000.00","5","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		msg = ctrl.addEmployee("John","Smith"," 606-12-4011","26","","50000.00","5","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		msg = ctrl.addEmployee("John","Smith","606-12-4011 ","26","","50000.00","5","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		System.out.println("Testing for detection of incorrect SSN formats PASSED");
	}
	
	@Test
	@Order(3)
	void test_AddWithDuplicateSSN() {
		ListController ctrl = new ListController();
		int numEmployees = ctrl.getNumEmployees();
		assertEquals(0,numEmployees);
		String msg = "";
		System.out.println("Testing that Duplicate SSN formats will be flagged as errors");
		String[] td = testData[0].split(",");
		msg = ctrl.addEmployee(td[0],td[1],td[2],td[3],td[4],td[5],td[6],td[7]);
		assertEquals("",msg);
		assertEquals(++numEmployees,ctrl.getNumEmployees());
		// add exactly the same employee
		msg = ctrl.addEmployee(td[0],td[1],td[2],td[3],td[4],td[5],td[6],td[7]);
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		// check for duplicate SSN only...
		msg = ctrl.addEmployee("John","Smith",td[2],"26","","50000.00","5","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		System.out.println("Testing for detection of duplicate SSN formats PASSED");
	}
	
	@Test
	@Order(4)
	void test_AddWithBadAge() {
		ListController ctrl = new ListController();
		int numEmployees = ctrl.getNumEmployees();
		assertEquals(0,numEmployees);
		String msg = "";
		System.out.println("Testing that invalid age data is flagged");
		msg = ctrl.addEmployee("John","Smith","111-11-1111","x","","50000.00","5","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		msg = ctrl.addEmployee("John","Smith","111-11-1111","5x","","50000.00","5","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		msg = ctrl.addEmployee("John","Smith","111-11-1111","x5","","50000.00","5","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		msg = ctrl.addEmployee("John","Smith","111-11-1111","-5","","50000.00","5","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		msg = ctrl.addEmployee("John","Smith","111-11-1111","15","","50000.00","5","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		msg = ctrl.addEmployee("John","Smith","111-11-1111","16 ","","50000.00","5","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		msg = ctrl.addEmployee("John","Smith","111-11-1111"," 16","","50000.00","5","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		msg = ctrl.addEmployee("John","Smith","111-11-1111","100","","50000.00","5","Engineering");
		if ("".equals(msg)) {
			System.out.println("Are you sure that you will have employees that are 100? REALLY? :)");
			assertEquals(++numEmployees,ctrl.getNumEmployees());
		} else {
			assertEquals(numEmployees,ctrl.getNumEmployees());			
		}
		assertEquals(numEmployees,ctrl.getNumEmployees());
		System.out.println("Testing for detection of invalid age PASSED");
	}
	
	@Test
	@Order(5)
	void test_AddWithBadSalary() {
		ListController ctrl = new ListController();
		int numEmployees = ctrl.getNumEmployees();
		assertEquals(0,numEmployees);
		String msg = "";
		System.out.println("Testing that invalid salary data is flagged");
		msg = ctrl.addEmployee("John","Smith","111-11-1111","26","","x","5","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		msg = ctrl.addEmployee("John","Smith","111-11-1111","26","","-1","5","Engineering");
		assertNotEquals("",msg);
		
//		next year, enable minimum wage check
//		assertEquals(numEmployees,ctrl.getNumEmployees());
//		msg = ctrl.addEmployee("John","Smith","111-11-1111","26","",MIN_WAGE,"5","Engineering");
//		assertNotEquals("",msg);

		assertEquals(numEmployees,ctrl.getNumEmployees());
		msg = ctrl.addEmployee("John","Smith","111-11-1111","26","","50x","5","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		msg = ctrl.addEmployee("John","Smith","111-11-1111","26","","x50","5","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		msg = ctrl.addEmployee("John","Smith","111-11-1111","26","","5000.xx","5","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		msg = ctrl.addEmployee("John","Smith","111-11-1111","26","","xx.50","5","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		System.out.println("Testing for detection of invalid salary PASSED");
	}
	
	@Test
	@Order(6)
	void test_AddWithBadYears() {
		ListController ctrl = new ListController();
		int numEmployees = ctrl.getNumEmployees();
		assertEquals(0,numEmployees);
		String msg = "";
		System.out.println("Testing that invalid years data is flagged");
		msg = ctrl.addEmployee("John","Smith","111-11-1111","16 ","","50000.00","x","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		msg = ctrl.addEmployee("John","Smith","111-11-1111","16 ","","50000.00","x0","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		msg = ctrl.addEmployee("John","Smith","111-11-1111","16 ","","50000.00"," 0","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		msg = ctrl.addEmployee("John","Smith","111-11-1111","16 ","","50000.00","0 ","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		msg = ctrl.addEmployee("John","Smith","111-11-1111","16 ","","50000.00","-1","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		msg = ctrl.addEmployee("John","Smith","111-11-1111","16 ","","50000.00","1","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		msg = ctrl.addEmployee("John","Smith","111-11-1111","32 ","","50000.00","17","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		msg = ctrl.addEmployee("John","Smith","111-11-1111","20 ","","50000.00","5","Engineering");
		assertNotEquals("",msg);
		assertEquals(numEmployees,ctrl.getNumEmployees());
		System.out.println("Testing for detection of invalid years data PASSED");
	}
	
	private boolean addAndCheckEmployee(ListController ctrl) {
		String[] td;
		String msg = "";
		int numEmployees = ctrl.getNumEmployees();
		for (int i = 0; i < testData.length; i++) {
			td = testData[i].split(",");
			msg = ctrl.addEmployee(td[0],td[1],td[2],td[3],td[4],td[5],td[6],td[7]);
			assertEquals("",msg);
			assertEquals(++numEmployees,ctrl.getNumEmployees());
			assertEquals(testData[i],checkEmployeeData(ctrl.getEmployeeDataStr()[i]));
		}
		return true;
		
	}
	
	@Test
	@Order(7)
	void test_AddEmployee() {
		ListController ctrl = new ListController();
		int numEmployees = ctrl.getNumEmployees();
		assertEquals(0,numEmployees);
		System.out.println("Testing that employees are successfully added to the database");
		assertTrue(addAndCheckEmployee(ctrl));
		System.out.println("Testing AddEmployee PASSED");
	}
	
	@Test
	@Order(8)
	void test_sortByName() {
		ListController ctrl = new ListController();
		int numEmployees = ctrl.getNumEmployees();
		assertEquals(0,numEmployees);
		System.out.println("Testing that sortByName function works");

		assertTrue(addAndCheckEmployee(ctrl));
		ctrl.sortByName();
		numEmployees = ctrl.getNumEmployees();
		String[] empDataStrArray = ctrl.getEmployeeDataStr();
		assertEquals(numEmployees,empDataStrArray.length);
		for (int i = 0; i < numEmployees; i++) {
			assertEquals(testData[expSortByName[i]],checkEmployeeData(empDataStrArray[i]));
		}
		System.out.println("Testing sortByName PASSED");
	}
	
	@Test
	@Order(8)
	void test_sortBySalary() {
		ListController ctrl = new ListController();
		int numEmployees = ctrl.getNumEmployees();
		assertEquals(0,numEmployees);
		System.out.println("Testing that sortByName function works");
		
		assertTrue(addAndCheckEmployee(ctrl));
		ctrl.sortBySalary();
		numEmployees = ctrl.getNumEmployees();
		String[] empDataStrArray = ctrl.getEmployeeDataStr();
		assertEquals(numEmployees,empDataStrArray.length);
		for (int i = 0; i < numEmployees; i++) {
			assertEquals(testData[expSortBySalary[i]],checkEmployeeData(empDataStrArray[i]));
		}
		System.out.println("Testing sortBySalary PASSED");
	}
	
	@Test
	@Order(9)
	void test_sortByID() {
		ListController ctrl = new ListController();
		int numEmployees = ctrl.getNumEmployees();
		assertEquals(0,numEmployees);
		System.out.println("Testing that sortByName function works");
		
		assertTrue(addAndCheckEmployee(ctrl));
		ctrl.sortByName();
		numEmployees = ctrl.getNumEmployees();
		String[] empDataStrArray = ctrl.getEmployeeDataStr();
		assertEquals(numEmployees,empDataStrArray.length);
		for (int i = 0; i < numEmployees; i++) {
			assertEquals(testData[expSortByName[i]],checkEmployeeData(empDataStrArray[i]));
		}

		ctrl.sortByID();
		empDataStrArray = ctrl.getEmployeeDataStr();
		assertEquals(numEmployees,empDataStrArray.length);
		for (int i = 0; i < numEmployees; i++) {
			assertEquals(testData[i],checkEmployeeData(empDataStrArray[i]));
		}
		System.out.println("Testing sortByID PASSED");
	}
	
	private int[] buildTokenMap(String expTokens[],String[] actTokens) {
		int[] map = new int[expTokens.length];
		for (int j = 0; j < expTokens.length; j++) {
			for (int k = 0; k < actTokens.length; k++) {
				if (expTokens[j].equals(actTokens[k]))
					map[j]=k;
			}
		}
		return map;
	}
	
	private boolean checkSavedData(File save) {
		String[] actTokens, expTokens;
		int[] map = null;
		br = fio.openBufferedReader(save);
		String line=null;
		for (int i = 0; i < testData.length; i++) {
			try {
				line = br.readLine();
			}
			catch (IOException e) {
				System.out.println("Caught an IO Exception while reading saved employee data");
				fail();
			}
			assertNotNull(line);
			actTokens = line.split("\\|.\\|");
			expTokens = testData[i].split(",");
			assertEquals(expTokens.length,actTokens.length);
			if (map == null) 
				map = buildTokenMap(expTokens,actTokens);
			for (int j = 0; j < expTokens.length; j++) {
				assertEquals(expTokens[j],actTokens[map[j]]);
			}
		}
		fio.closeFile(br);
		return true;
	}
	
	@Test
	@Order(10)
	// this is just going to check the save method works..
	void test_SaveMethod() {
		System.out.println("Testing functionality of the save method");
		File save = new File("empDB.dat");
		assertFalse(save.exists());
		ListController ctrl = new ListController();
		int numEmployees = ctrl.getNumEmployees();
		assertEquals(0,numEmployees);
		assertTrue(addAndCheckEmployee(ctrl));
		ctrl.saveEmployeeData();
		assertTrue(save.delete());
		assertFalse(save.exists());
		ctrl.sortByName();
		ctrl.saveEmployeeData();
		assertTrue(save.exists());
		assertTrue(checkSavedData(save));
		assertTrue(save.delete());
		System.out.println("Save method functionality PASSED");		
	}

	@Test
	@Order(11)
	// this is just going to check the save method works..
	void test_RestoreMethod() {
		System.out.println("Testing functionality of the restore method");
		File save = new File("empDB.dat");
		assertFalse(save.exists());
		ListController ctrl = new ListController();
		int numEmployees = ctrl.getNumEmployees();
		assertEquals(0,numEmployees);
		assertTrue(addAndCheckEmployee(ctrl));
		ctrl.saveEmployeeData();

		ctrl = new ListController(); // force the save file to be read
		numEmployees = ctrl.getNumEmployees();
		assertEquals(testData.length,numEmployees);
		String[] empDataStr = ctrl.getEmployeeDataStr();
		for (int i = 0; i < numEmployees; i++) {
			assertEquals(testData[i],checkEmployeeData(empDataStr[i]));
		}
		assertTrue(save.delete());
		System.out.println("Restore method functionality PASSED");		
	}
}
