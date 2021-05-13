/*
 * Date: May 5th - 12th, 2021
 * Name: Kyle Chong & Patrick Kwok
 * Teacher: Mr. Ho
 * Description: The class file 'mainframe.java' is the file that provides the bar chart (DistributionChart.fxml) with all its data.
 * This class file will sort through numbers in a file that the user inputs ie. sales.csv. It will then keep track of the first 
 * digit of every number while also ignoring any words (anything that doesn't start with 1-9). 
 * The program will then calculate the percentage occurance of each first digit and determine if the sales numbers are fraudulent
 * 
 * Git Repository Link: https://github.com/Chong-Kyle/Benfords-Law 
 */

//Imports
import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
import java.awt.Desktop;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class Mainframe implements Initializable{
    private static double digitPercent[] = new double[10]; //Defines the double array variable digitPercent[] with a length of 10
    
    @FXML   //@FXML separates certain code from the rest of the code. In this case it's used to differentiate the JavaFX layout code for the bar chat from regular code 
    private BarChart<String, Double> distributionChart; //Defines the name of the bar chart used by the fxml file, 'DistributionChart.fxml' with the arguments <String,Double>

    @FXML
    private CategoryAxis digit; //Defines the x axis variable used by the fxml file

    @FXML
    private NumberAxis percent; //Defines the y axis variable ued by the fxml file

    @Override   //@Override allows the following method to override the parent method
    public void initialize(URL url, ResourceBundle rb){ 
        /*
         * The initialize method's purpose is to get all the data that the bar chart needs and adds that data to the fxml file. 
         * In this case, the method calls menu() (explains later).
         * The percent occurance is then stored inside of an array. Each digit's percent is then sent to the bar chart by using 'getData().add'.
         * In a sense, the initialize method acts as a 'main' loop in any normal code
         */
        
        menu(); //Calls the menu() method
        
        XYChart.Series<String, Double> setl = new XYChart.Series<>();   //Sets a new XYChart series with parameters <String, Double> under the variable setl
        
        for(int i = 1; i < 10; i++){    //For each digit 1-9
            setl.getData().add(new XYChart.Data<String,Double>(Integer.toString(i), digitPercent[i]));  //Adds the x and y values i and digitPercent[i] to the chart's data
        }
        distributionChart.getData().addAll(setl);   //Sends the data for the fxml to read and implement into the bar chart
    }

    //Initally Designed by Patrick, Edited by Kyle
    public static void menu() {
		Scanner s = new Scanner(System.in);	// creates console input scanner
		System.out.println("Enter 'r' to read sales data and 'c' to check for accounting fraud.");
		String input = s.nextLine();
		while (!(input.equals("r") || input.equals("c"))) {	// while input isn't "r" or "c"
			System.out.println("Please enter either 'r' or 'c'.");	// prompts user again
			input = s.nextLine();	// scans user input again
		}
		switch (input) {
		case "r":
			readSalesData();
            break;
			
		case "c":
            try{    //Tries the readFile() method
                readFile();
                break;
            } catch(FileNotFoundException e){   //If it catches an exception,
                e.printStackTrace();    //If an exception is caught, it will print where the error occured 
            }
		}
        s.close();
	}

    // opens sales.csv file
	// @param none
	// @returns none
    //Initally Designed by Patrick, Edited by Kyle
	public static void readSalesData() {
        Scanner s = new Scanner(System.in);    //Initializes new Scanner
        System.out.println("Please enter the file name: "); //Text that prompts user to input the file name
        String filePath = s.nextLine();    //Defines the String 'filePath' which will store the user's input
        String fileInput = (filePath);   //Sets the the Scanner 'fileInput' with the file path
		try {
			Desktop.getDesktop().open(new File(fileInput).getAbsoluteFile());	// opens sales.csv
			System.out.println("Sales data loaded.");
		} catch (IOException e) {
			e.printStackTrace();
		}
        menu();
        s.close();
	}

    //KYLE
    public static void readFile() throws FileNotFoundException {
        /*
         * The readFile method calls other methods while also asking for the user's input.
         * The method asks the user to input the name of the file they want analysed. 
         * The file name is then stored inside of 'filePath' and then with the Scanner 'fileInput', this scanner uses 'filePath' to read the contents of the specified file.
         * 
         * @param reader - a Scanner used to prompt user input
         * @param filePath - the name of the file that the user wants to be analyzed
         * @param digitCount - an array that stores the occurence of each first digit
         */
        Scanner reader = new Scanner(System.in);    //Initializes new Scanner
        System.out.println("To determine if the data is fraudulent, please enter the file name: "); //Text that prompts user to input the file name
        String filePath = reader.nextLine();    //Defines the String 'filePath' which will store the user's input
        Scanner fileInput = new Scanner(new File(filePath));   //Sets the the Scanner 'fileInput' with the file path to where the file is stored (explained in docstring)
        fileInput.useDelimiter("[,\n]");    //Defines the delimiters used

        int[] digitCount = countDigits(fileInput);  //Changes the value of the array 'digitCount' by calling the 'countDigits' method
        int total = sumOf(digitCount) - digitCount[0];  //Updates the value of 'total' by calling 'sumOf' - digitCount[0]
        double[] digitPercent = getPercent(digitCount, total);  //Changes the values in the array 'digitPercent'by calling the 'getPercent' method

        try{
            writeResults(digitPercent); //Calls the 'writeResults' method that opens/creates the results.csv file
        } catch (IOException e) {   //Catches exceptions
            System.out.println("ERROR");
        }

        reportResults(digitPercent, digitCount, total);  //Calls the 'reportResults' method
        reader.close(); //Closes the Scanner 'reader'
    }
    
    //KYLE
    public static int[] countDigits(Scanner fileInput) {
        /*
         * The countDigits method is responsible computing an array of occurences for each leading digit (1-9). 
         * The method reads the input file token by token by calling the 'firstChar' and 'firstDigit' methods (explained their own methods).
         * When the 'firstChar' method returns the numbers 1-9 (or 0 if it's an invalid number), it stores the occurences within an array
         * 
         * @returns digitCount - an array responsible for storing the number of occurences for each first digit
         */
        int[] digitCount = new int[10]; //Defines the digitCount array with a size of 10 elements
        while (fileInput.hasNext()) {   //while the file Scanner can find a new token
            int n = firstDigitOf(fileInput.next());    //Defines a variable n which stores the digit returned from the method 'firstChar'
            digitCount[n]++;    //Increases the count of occurences for 'n' (which stores the first digit)
        }
        return digitCount;  //Returns digitCount
    }

    //KYLE
    public static int firstDigitOf(String token) {
        /*
         * The method 'firstCharacterOf' is responsible for returning the first nonzero digit of the string. 
         * If the program finds a first digit/character that starts with 1-9, it will return that number.
         * If the program finds a first digit/character that is not 1-9, it will return it as 0; an invalid token
         * This method is called in the 'countDigits' method
         * 
         * @returns firstChar - the first digit of a valid token 
         * @returns - 0 if token does not start with 1-9; an invalid token
         */
        for (char firstDigit : token.toCharArray()) {    //For-each loop; the first character in the token, 'token.toCharArray()'
            if (firstDigit >= '1' && firstDigit <= '9') { //If the first digit is 1-9
                return firstDigit - '0'; //Returns the first digit except 0
            }
            else {  //If the first digit is not 1-9; an invalid token
                return 0;   //Returns 0
            }
        }
        return 0;   //Returns 0
    }

    //KYLE
    public static int sumOf(int[] arr) {
        /*
         * The method 'sumOf' gets the sum of the integers in the given array.
         * For example it's later called in the 'reportResults' array. The given array is 'digitCount'.
         * The 'sumOf' method then counts each occurence and adds them together to get the total number of first digits
         * 
         * @param arr[] - the given array when the method is called 
         *
         * @returns sum - the sum of integers in the given array
         */
        int sum = 0;    //Sets the value of 'sum' to 0
        for (int i : arr) { //For-each loop; int i with int[] arr being the iterable object
            sum += i;   //Increases 'sum' by i
        }
        return sum; //Returns sum
    }

    //KYLE
    public static double[] getPercent(int[] digitCount, int total){
        /*
         * The method 'getPercent' gets the percent occurence of each first digit based on the given count for each digit and the total count of tokens
         * 
         * @param digitCount[] - an array holding the counts for each leading digit
         * 
         * @returns digitPercent[] - an array holding the percent occurence of each first digit 
         * 
         */ 
        for (int i = 1; i < digitCount.length; i++) {   //Goes through each element in the array
            digitPercent[i] = digitCount[i] * 100.0 / total;    //Gets the percent occurence for each leading digit
        }
        return digitPercent;    //Returns the 'digitPercent' array
    }

    //KYLE
    public static void reportResults(double[] digitPercent, int[] digitCount, int total) {
        /*
         * This method prints out a table showing the percentage data and occurence count for each leading digit.
         * 
         * @param digitCount[] - an array containing the number of times each leading digit occured
         *
         * @returns - a table containing the data for each leading digit
         */
        System.out.println("\nDigit Count Percent"); //Prints out the category headers
        for (int i = 1; i < digitCount.length; i++) {   //Goes through each element in the array
            System.out.printf("%5d %5d %6.2f\n", i, digitCount[i], digitPercent[i]);    //Prints out the results for each leading digit
        }
        System.out.printf("Total %5d %6.2f\n", total, 100.0);   //Prints out bottom headers that line up with the data 
    }

    //Initally Designed by Patrick, Edited by Kyle
    public static void writeResults(double[] digitPercent) throws IOException{
        File results = new File("results.csv");	// creates file results.csv
        results.createNewFile();	// creates results.csv onto computer
        FileWriter w = null;	// declares filewriter w
        try {
            w = new FileWriter("results.csv");	// assigns filewriter w to results.csv
        } catch (Exception e) {
            System.out.println("Waiting for results.csv to close...");	
            for (boolean loop = true; loop == true;) {	// loops following code until boolean variable loop is set to false
                try {
                    w = new FileWriter("results.csv");	// assigns results.csv to filewriter w, throws exception if results.csv is open
                    loop = false;	// loop ends assuming zero exceptions
                } catch (Exception e1) {}	// exception ignored and loop restarted
            }
        }
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        w.write("Firs.Digi.,Freq.Perc.,Visual\n");	// writes table titles
        for (int i = 1; i < 10; i++) {	// following code repeated 9 times with different first digit each time
            w.write(i+","+numberFormat.format(digitPercent[i])+",");	// writes first digit and the freq. perc. into table
            for (int x = 0; x < digitPercent[i]; x++) {	// writes "[]" for each 1% of the first digit's freq. perc. rounded
                w.write("[]");
            }
            w.write("\n");	// filewriter moves down to begin next first digit
        }
        if (digitPercent[1] > 29 && digitPercent[1] < 32) {	// if freq. perc. of first digit 1 is between 29 and 32:
            w.write("The data indicates that fraud likely did not occur.");
        } else {
            w.write("The data indicates that fraud likely did occur.");
        }
        w.close();	// closes filewriter w
        Desktop.getDesktop().open(results);	// opens results.csv
        System.out.println("Sales data analysis loaded.");
    }
}
