import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import myfileio.MyFileIO;

public class ListController {
	private ArrayList<Employee> employees;
	private static final boolean DEBUG = true;
	
	public ArrayList getEmployees() {
		return employees;
	}
	
	public int getNumEmployees() {
		return employees.size();
	}
	
	public ListController () {
		Employee.resetStaticID();
		employees = new ArrayList<Employee>();
		restoreEmployeeDataBase();
	}

	// adds a new employee
	String addEmployee(String firstName, String lastName, String ssn, String age, String pronouns, String salary, String years, String dept) {
		// TODO #7
		// controller needs to convert the numeric string data -
		// use Integer.parseInt() or Double.parseDouble() for ints and doubles
		// years should be int, salary should be a double....
		// Then, add the new employee to the employees list!
		// for initial demo and debugging, set DEBUG to true;
		if (DEBUG) System.out.println(employees);
    	
		if (!populatedFields(firstName, lastName, ssn, age, salary, years, dept).equals("")) {
			return "Error";
		} else if (!formatting(firstName, lastName, ssn, age, salary, years, dept).equals("")) {
			return "Error";
		} else if (!duplicateSsn(firstName, lastName, ssn, age, salary, years, dept).equals("")) {
			return "Error";
		}
		employees.add(new Employee(firstName, lastName, ssn, Integer.parseInt(age), pronouns, Double.parseDouble(salary), Integer.parseInt(years), dept));
		return "";
	}
	
    public String duplicateSsn(String lastName, String firstName, String ssn, String age, String salary, String years, String dept) {
    	if (!checkDuplicateSSN(ssn)) {
     		Alert alert = new Alert(AlertType.WARNING);
    		alert.setHeaderText("Add Employee Failed");
    		alert.setContentText("No duplicate SSN is allowed.");
    		alert.showAndWait();
    		return "Duplicate SSN";
    	}
    	return "";
    }
    
    public String formatting(String lastName, String firstName, String ssn, String age, String salary, String years, String dept) {
    	if (!salary.matches("\\d+")) {
     		Alert alert = new Alert(AlertType.WARNING);
    		alert.setHeaderText("Add Employee Failed");
    		alert.setContentText("Make sure formatting is correct.");
    		alert.showAndWait();
    		return "Formatting incorrect.";
    	}
    	if (!lastName.matches("\\D+") || !firstName.matches("\\D+") || !ssn.matches("\\d{3}-\\d{2}-\\d{4}") || !age.matches("\\d+") || !(Integer.parseInt(salary) >= 35000 && Integer.parseInt(salary) <= 20000000) || !years.matches("\\d+") || !dept.matches("\\D+")) {
     		Alert alert = new Alert(AlertType.WARNING);
    		alert.setHeaderText("Add Employee Failed");
    		alert.setContentText("Make sure formatting is correct.");
    		alert.showAndWait();
    		return "Formatting incrorrect.";
    	}
    	return "";
    }
    
    public String populatedFields(String lastName, String firstName, String ssn, String age, String salary, String years, String dept) {
    	if (lastName.equals("") || firstName.equals("") || ssn.equals("") || age.equals("") || salary.equals("") || years.equals("") || dept.equals("")) {
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.setHeaderText("Add Employee Failed");
    		alert.setContentText("Make sure that all fields are populated (with the exception of pronouns)");
    		alert.showAndWait();
    		return "Not all fields populated.";
    	}
    	return "";
    }
    
    private boolean checkDuplicateSSN(String ssn) {
    	for (int i = 0; i < employees.size(); i++) {
    		if (((Employee)employees.get(i)).getSsn().equals(ssn)) {
    			return false;
    		}
    	}
    	return true;
    }
	
	
	// returns a string array of the employee information to be viewed
	public String[] getEmployeeDataStr() {
		String[] array = new String[employees.size()];
		
		for (int i = 0; i < array.length; i++) {
			array[i] = employees.get(i) + "";
		}
		return array;
	}
	
	// TODO: #Part_3: Save/Restore | saves employee data base
	public void saveEmployeeData() {
		MyFileIO myFileIO = new MyFileIO();
		File file = myFileIO.getFileHandle("empDB.dat");
		int fileStatus = myFileIO.checkTextFile(file, false);
		
		if (fileStatus != MyFileIO.FILE_OK) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Failed to save");
			alert.setContentText("Something went wrong with opening save file, status: " + fileStatus);
			alert.showAndWait();	
			return;
		}
		
		BufferedWriter bufferedWriter = myFileIO.openBufferedWriter(file);
		
		try {
			for (int i = 0; i < employees.size(); i++) {
				Employee employee = employees.get(i);
				bufferedWriter.write(String.format("%s|,|%s|,|%s|,|%d|,|%s|,|%f|,|%d|,|%s\n",employee.getFirstName(), employee.getLastName(), employee.getSsn(), employee.getAge(), employee.getPronouns(), employee.getSalary(), employee.getYears(), employee.getDept()));
			}
			bufferedWriter.close();
		} catch (IOException ev) {			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Failed to save");
			alert.setContentText("Something went wrong with writing or closing save file. " + ev.getMessage());
			alert.showAndWait();
		}		
	}
	
	public void restoreEmployeeDataBase() {
		MyFileIO myFileIO = new MyFileIO();
		File file = myFileIO.getFileHandle("empDB.dat");
		if (!file.exists()) {
			return;
		}
		int fileStatus = myFileIO.checkTextFile(file, true);
		
		if (fileStatus != MyFileIO.FILE_OK || fileStatus == MyFileIO.FILE_DOES_NOT_EXIST) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Failed to restore employee data base");
			alert.setContentText("Something went wrong with opening save file, status: " + fileStatus);
			alert.showAndWait();
			return;
		}
		
		BufferedReader bufferedReader = myFileIO.openBufferedReader(file);
		
		try {
			String line = bufferedReader.readLine();
			while (line != null && !line.equals("")) {
				String[] tokens = line.split("(\\|,\\|)");
				addEmployee(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4], tokens[5], tokens[6], tokens[7]);
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
		} catch (IOException ev) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Failed to restore");
			alert.setContentText("Something went wrong with reading or closing save file. " + ev.getMessage());
			alert.showAndWait();
		}
	}
	
	public void sortByName() {
		Collections.sort(employees, new ByName());
	}
	
	private class ByName implements Comparator<Employee> {
		public int compare(Employee o1, Employee o2) {
			if (o1.getLastName().equals(o2.getLastName())) {
				return o2.getFirstName().compareTo(o2.getFirstName());
			}
			
			return o1.getLastName().compareTo(o2.getLastName());
		}
	}
	
	public void sortByID() {
		Collections.sort(employees, new ByID());
	}
	
	private class ByID implements Comparator<Employee> {
		public int compare(Employee o1, Employee o2) {
			return Integer.compare(o1.getEmpID(), o2.getEmpID());
		}
	}
	
	public void sortBySalary() {
		Collections.sort(employees, new BySalary());
	}
	
	private class BySalary implements Comparator<Employee> {
		public int compare(Employee o1, Employee o2) {
			return Double.compare(o1.getSalary(), o2.getSalary());
		}
	}
}