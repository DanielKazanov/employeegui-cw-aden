import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import myfileio.MyFileIO;

public class ListController {
	private ArrayList<Employee> employees;
	private static final boolean DEBUG = true;
	
	public ListController () {
		employees = new ArrayList<Employee>();
	}

	// adds a new employee
	void addEmployee(String name, String SSN, String salary, String years) {
		// TODO #7
		// controller needs to convert the numeric string data -
		// use Integer.parseInt() or Double.parseDouble() for ints and doubles
		// years should be int, salary should be a double....
		// Then, add the new employee to the employees list!
		// for initial demo and debugging, set DEBUG to true;
		if (DEBUG) System.out.println(employees);
		employees.add(new Employee(name, SSN, Double.parseDouble(salary), Integer.parseInt(years)));
	}
	
	
	// returns a string array of the employee information to be viewed
	public String[] getEmployeeDataStr() {
		// temporary placeholder for compilation reasons - will remove later...
		String[] array = new String[employees.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = employees.get(i) + "";
		}
		return array;
		
	}
	
	// TODO: #Part_3: Save/Restore | saves employee data base
	public void saveEmployeeDataBase() {
		MyFileIO myFileIO = new MyFileIO();
		File file = myFileIO.getFileHandle("empDB.dat");
		int fileStatus = myFileIO.checkTextFile(file, false);
		
		if (fileStatus != MyFileIO.FILE_OK && fileStatus != MyFileIO.WRITE_EXISTS) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Failed to save");
			alert.setContentText("Something went wrong with opening save file, status: " + fileStatus);
			alert.showAndWait();	
			return;
		}
		
		BufferedWriter bufferedWriter = myFileIO.openBufferedWriter(file);
		
		try {
			for (int i = 0; i < employees.size(); i++) { // Employee employee : employees
				Employee employee = employees.get(i);
				bufferedWriter.write(String.format("|%s,%s,%f,%d|\n",employee.getName(),employee.getSSN(),employee.getSalary(),employee.getYears()));
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
		int fileStatus = myFileIO.checkTextFile(file, true);
		
		if (fileStatus != MyFileIO.FILE_OK) {
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
				String[] tokens = line.substring(1, line.length() - 1).split(",");
				addEmployee(tokens[0], tokens[1], tokens[2], tokens[3]);
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
}