
public class Employee {
	private String name;
	private double salary;
	private int years;
	private String SSN;


public Employee (String name, String SSN, double salary, int years) {
	this.name = name;
	this.SSN = SSN;
	this.salary = salary;
	this.years = years;
}


public String getName() {
	return name;
}


public void setName(String name) {
	this.name = name;
}


public double getSalary() {
	return salary;
}


public void setSalary(double salary) {
	this.salary = salary;
}


public int getYears() {
	return years;
}


public void setYears(int years) {
	this.years = years;
}


public String getSSN() {
	return SSN;
}


public void setSSN(String sSN) {
	SSN = sSN;
}


public String toString( ) {
	    return("\n  Name: "+name + "\n   SSN: " + SSN + "\nSalary: " + salary + "\n Years: " + years);
}



}
