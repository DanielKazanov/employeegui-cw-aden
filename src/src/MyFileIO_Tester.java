
import myfileio.MyFileIO;

// TODO: Auto-generated Javadoc
/**
 * The Class MyFileIO_Tester.
 */
public class MyFileIO_Tester {

	/**
	 * The main method. 
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		MyFileIO fileIO = new MyFileIO();
		/* You can use this main to test your MyFileIO methods...
		   For example, you might want to get a file handle for a file
		   that you know does not exist, check the various methods that File
		   has, and check what your checkTextFile returns.
		   Then create a new empty file - is it there? does it have the right size?
		   then delete it...
		   Then write a new file (in output/ directory), save and close,
		   then read it...
		   */
		
		System.out.println("Expected: true Actual: " + fileIO.createEmptyFile("Daniel's File"));
		
		System.out.println("Expected: true Actual: " + fileIO.deleteFile("Daniel's File"));
		
		System.out.println("Expected: false Actual: " + fileIO.deleteFile("NOT Daniel's File"));
		
	}

}
