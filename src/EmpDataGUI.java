/**
 * 
 */


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author Scott
 *
 */
public class EmpDataGUI extends Application {
    private GridPane main = new GridPane();	
    private BorderPane borderPane = new BorderPane();
    private ListController controller = new ListController();
    
    // TODO #Part_2:
    private BorderPane view = new BorderPane();
    private ScrollPane scroll = new ScrollPane();
    
    private Scene scene1 = new Scene(borderPane, 400, 500);
    private Scene scene2 = new Scene(view, 400, 500);
    
    
    private Label employeeDataEntryLabel;
    private Label lastNameLabel;
    private Label firstNameLabel;
    private Label ssnLabel;
    private Label ageLabel;
    private Label pronounsLabel;
    private Label salaryLabel;
    private Label yearsLabel;
    private Label deptLabel;
    private TextField lastNameTextField;
    private TextField firstNameTextField;
    private TextField ssnTextField;
    private TextField ageTextField;
    private TextField pronounsTextField;
    private TextField salaryTextField;
    private TextField yearsTextField;
	
	
    @Override
    public void start(Stage primaryStage) {
    	employeeDataEntryLabel = new Label("EMPLOYEE DATA ENTRY");
        lastNameLabel = new Label("Last Name:");
        firstNameLabel = new Label("First Name:");
        ssnLabel = new Label("SSN:");
        ageLabel = new Label("Age:");
        pronounsLabel = new Label("Pronouns:");
        salaryLabel = new Label("Salary:");
        yearsLabel = new Label("Years:");
        deptLabel = new Label("Dept:");
	    lastNameTextField = new TextField();
	    firstNameTextField = new TextField();
	    ssnTextField = new TextField();
	    ageTextField = new TextField();
	    pronounsTextField = new TextField();
	    salaryTextField = new TextField();
	    yearsTextField = new TextField();
   
	    
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton engineeringRadioButton = new RadioButton("Engineering");
        engineeringRadioButton.setSelected(true);
        RadioButton marketingSalesRadioButton = new RadioButton("Marketing/Sales");
        RadioButton manufacturingRadioButton = new RadioButton("Manufacturing");
        RadioButton financeRadioButton = new RadioButton("Finance");
        RadioButton humanResourcesRadioButton = new RadioButton("Human Resources");
        RadioButton customerSupportRadioButton = new RadioButton("Customer Support");
        RadioButton managementRadioButton = new RadioButton("Management");
        engineeringRadioButton.setToggleGroup(toggleGroup);
        marketingSalesRadioButton.setToggleGroup(toggleGroup);
        manufacturingRadioButton.setToggleGroup(toggleGroup);
        financeRadioButton.setToggleGroup(toggleGroup);
        humanResourcesRadioButton.setToggleGroup(toggleGroup);
        customerSupportRadioButton.setToggleGroup(toggleGroup);
        managementRadioButton.setToggleGroup(toggleGroup);
      
	    Button addEmployeeButton = new Button("Add Employee");
	    addEmployeeButton.setOnAction(e -> validateAndAddEmployee(toggleGroup, engineeringRadioButton));
	    Button viewEmployeeButton = new Button("View Employee");
	    viewEmployeeButton.setOnAction(e -> {primaryStage.setScene(scene2); viewEmployeeDB();});
	    Button saveEmployeeDBButton = new Button("Save Employee DB");
	    saveEmployeeDBButton.setOnAction(e -> controller.saveEmployeeData());
        
    	primaryStage.setTitle("Employees");
        primaryStage.setScene(scene1);
        primaryStage.show();
        
        VBox vBox = new VBox(8);
        vBox.getChildren().addAll(engineeringRadioButton, marketingSalesRadioButton, manufacturingRadioButton, financeRadioButton, humanResourcesRadioButton, customerSupportRadioButton, managementRadioButton);
        HBox hBox = new HBox(8);
        hBox.getChildren().addAll(addEmployeeButton, viewEmployeeButton, saveEmployeeDBButton);
        
        borderPane.setCenter(main);
        borderPane.setBottom(hBox);
        
        main.add(lastNameLabel, 0, 0);
        main.add(firstNameLabel, 0, 1);
        main.add(ssnLabel, 0, 2);
        main.add(ageLabel, 0, 3);
        main.add(pronounsLabel, 0, 4);
        main.add(salaryLabel, 0, 5);
        main.add(yearsLabel, 0, 6);
        main.add(deptLabel, 0, 7);
        
        main.add(lastNameTextField, 1, 0);
        main.add(firstNameTextField, 1, 1);
        main.add(ssnTextField, 1, 2);
        main.add(ageTextField, 1, 3);
        main.add(pronounsTextField, 1, 4);
        main.add(salaryTextField, 1, 5);
        main.add(yearsTextField, 1, 6);
        main.add(vBox, 1, 7);
        
    	borderPane.setTop(employeeDataEntryLabel);
    	
    	
    	Label employeeDataViewLabel = new Label("EMPLOYEE DATA VIEW");
    	view.setTop(employeeDataViewLabel);
    	
    	view.setCenter(scroll);
    	
	    Button backButton = new Button("Back");
	    backButton.setOnAction(e -> {primaryStage.setScene(scene1); engineeringRadioButton.setSelected(true);});
	    Button sortByNameButton = new Button("Sort By Name");
	    sortByNameButton.setOnAction(e -> {controller.sortByName(); viewEmployeeDB();});
	    Button sortByIDButton = new Button("Sort By ID");
	    sortByIDButton.setOnAction(e -> {controller.sortByID(); viewEmployeeDB();});
	    Button sortBySalaryButton = new Button("Sort By Salary");
	    sortBySalaryButton.setOnAction(e -> {controller.sortBySalary(); viewEmployeeDB();});
    	
    	HBox viewHBox = new HBox(8);
    	viewHBox.getChildren().addAll(backButton, sortByNameButton, sortByIDButton, sortBySalaryButton);
    	view.setBottom(viewHBox);
    }

    private void viewEmployeeDB() {
    	String[] empDataStr = controller.getEmployeeDataStr();
    	ListView listView = new ListView(FXCollections.observableArrayList(empDataStr));
    	listView.setPrefWidth(400);
    	scroll.setContent(listView);
    }
    
    private void validateAndAddEmployee(ToggleGroup toggleGroup, RadioButton engineeringRadioButton) {
    	String lastName = lastNameTextField.getText();
    	String firstName = firstNameTextField.getText();
    	String ssn = ssnTextField.getText();
    	String age = ageTextField.getText();
    	String pronouns = pronounsTextField.getText(); // should be optional
    	String salary = salaryTextField.getText();
    	String years = yearsTextField.getText();
    	RadioButton dept = (RadioButton)toggleGroup.getSelectedToggle();
    	
    	String s = controller.addEmployee(firstName, lastName, ssn, age, pronouns, salary, years, dept.getText());
    	
    	if (s.equals("Error: populated fields")) {
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.setHeaderText("Add Employee Failed");
    		alert.setContentText("Make sure that all fields are populated (with the exception of pronouns)");
    		alert.showAndWait();
    		return;
    	}  else if (s.equals("Error: Age incorrect.")) {
     		Alert alert = new Alert(AlertType.WARNING);
    		alert.setHeaderText("Add Employee Failed");
    		alert.setContentText("Make sure age is correct.");
    		alert.showAndWait();
    	} else if (s.equals("Error: Formatting")) {
     		Alert alert = new Alert(AlertType.WARNING);
    		alert.setHeaderText("Add Employee Failed");
    		alert.setContentText("Make sure formatting is correct.");
    		alert.showAndWait();
    	} else if (s.equals("Error: duplicate SSN")) {
     		Alert alert = new Alert(AlertType.WARNING);
    		alert.setHeaderText("Add Employee Failed");
    		alert.setContentText("No duplicate SSN is allowed.");
    		alert.showAndWait();
    		return;
    	} else if (Integer.parseInt(age) < controller.MIN_AGE || Integer.parseInt(age) > controller.MAX_AGE) {
    		System.out.println("Check age.");
        	clearTextFields();
        	engineeringRadioButton.setSelected(true);
        	return;
    	} else if (s.equals("")) {
        	clearTextFields();
        	engineeringRadioButton.setSelected(true);
        	return;
    	}
    }
    
    
    
    private void clearTextFields() {
    	lastNameTextField.setText("");
    	firstNameTextField.setText("");
    	ssnTextField.setText("");
    	ageTextField.setText("");
    	pronounsTextField.setText("");
    	salaryTextField.setText("");
    	yearsTextField.setText("");
    }
    
  /**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Application.launch(args);
	}

} ;
