import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.stage.Stage;

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

	}
	
	
	// returns a string array of the employee information to be viewed
	public String[] getEmployeeDataStr() {
		// temporary placeholder for compilation reasons - will remove later...
		return(new String[1]);
		
	}
}
 
