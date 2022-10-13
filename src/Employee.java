
public class Employee {
	
	private String lastName;
	private String firstName;
	private String ssn;
	private int age;
	private String pronouns;
	private double salary;
	private int years;
	private String dept;
	private int empID;
	private static int EMPLOYEEID = 1;

	public Employee (String firstName, String lastName, String ssn, int age, String pronouns, double salary, int years, String dept) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.ssn = ssn;
		this.age = age;
		this.pronouns = pronouns;
		this.salary = salary;
		this.years = years;
		this.dept = dept;
		empID = EMPLOYEEID;
		EMPLOYEEID++;
	}
	
	public int getEmpID() {
		return empID;
	}
	
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getPronouns() {
		return pronouns;
	}

	public void setPronouns(String pronouns) {
		this.pronouns = pronouns;
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

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}
	
	protected static void resetStaticID() {
		EMPLOYEEID = 1;
	}

	public String toString( ) {
		String str = String.format("%-8s\t","Name:")+firstName+" "+lastName;
		str += (!pronouns.isEmpty()) ? " ("+pronouns+")\n" : "\n";
		str = str +    String.format("%-8s\t", "SSN")+String.format("%-16s\t",ssn );
		str = str +    String.format("%-16s\t","Employee ID:")+empID+"\n";
		str = str +    String.format("%-8s\t","Age:")+String.format("%-16d\t\t", age)+String.format("%-20s\t","Years Exp:")+years+"\n";
		if(dept.equals("Finance")) {
			str = str +    String.format("%-8s\t","Dept:")+String.format("%-16s\t\t",dept);
		} else {
			str = str +    String.format("%-8s\t","Dept:")+String.format("%-16s\t",dept);		
		}
		str = str +    String.format("%-20s\t\t","Salary:")+ String.format("%.2f",((long) (salary*100)/100.0));
		return(str);
	}
}