/**
 * 
 */


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * @author Scott
 *
 */
public class EmpDataGUI extends Application {
    private GridPane main = new GridPane();	
    private ListController controller = new ListController();
    
    // TODO #Part_2
    private BorderPane view = new BorderPane();
    private ScrollPane scroll = new ScrollPane();
    private Scene scene2 = new Scene(view, 400, 400);
    
    // TODO #1:
    // create private TextField variables for Name, SSN, Salary and Years
    // so that they can be accessed directly by methods inside this class.
	private TextField Name;
	private TextField SSN;
	private TextField Salary;
	private TextField Years;
    @Override
    public void start(Stage primaryStage) {
    
    	Scene scene = new Scene(main,400,400);

	// TODO #2:
    	// create Labels for Name, SSN, Salary and Years
    Label labelName = new Label("Name:");
    Label labelSSN = new Label("SSN:");
    Label labelSalary = new Label("Salary:");
    Label labelYears  = new Label("Years:");
    	
	// TODO #4
    	// instantiate (new) TextFields (already declared above) for Name, SSN, Salary and Years
    Name = new TextField();
    SSN = new TextField();
    Salary = new TextField();
    Years = new TextField();
	
	// TODO #5
        // Create Add Employee Button, and write the setOnAction handler to call the controller
    	// to add the new Employee data
    Button button = new Button("Add Employee");
    button.setOnAction(e -> controller.addEmployee(Name.getText(), SSN.getText(), Salary.getText(), Years.getText()));
;	// TODO #6
    	// add all the labels, textfields and button to gridpane main. refer to the slide
    	// for ordering...
    	main.add(labelName, 0, 0);
    	main.add(labelSSN, 0, 1);
    	main.add(labelSalary, 0, 2);
    	main.add(labelYears, 0, 3);
    	main.add(button, 0, 4);
    	
    	main.add(Name, 1, 0);
    	main.add(SSN, 1, 1);
    	main.add(Salary, 1, 2);
    	main.add(Years, 1, 3);
    	
    	primaryStage.setTitle("Employees");
        primaryStage.setScene(scene);
        primaryStage.show();
        
     // TODO #Part_2
        Label labelEmployeeData = new Label("Employee Data");
        view.setTop(labelEmployeeData);
        Button buttonBack = new Button("Back");
        view.setBottom(buttonBack);
        buttonBack.setOnAction(e -> primaryStage.setScene(scene));
        view.setCenter(scroll);
        Button buttonViewEmployees = new Button("View Employees");
        main.add(buttonViewEmployees, 1, 4);
        buttonViewEmployees.setOnAction(e -> {primaryStage.setScene(scene2); viewEmployeeDB();});
        
     // TODO #Part_3: Save/Restore
        Button buttonSaveDB = new Button("Save DB");
        main.add(buttonSaveDB, 0, 5);
//        buttonSaveDB.setOnAction(e -> controller.);
    }

    // don't worry about this yet - part of part2
    private void viewEmployeeDB() {
    	String[] empDataStr = controller.getEmployeeDataStr();
//    	ListView<String> listView = new ListView<>(FXCollections.observableArrayList(empDataStr));
    	ListView listView = new ListView(FXCollections.observableArrayList(empDataStr));
    	listView.setPrefWidth(400);
    	scroll.setContent(listView);
    }
    
  /**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Application.launch(args);
	}

} ;
