/*
 * Date: May 5th - 12th, 2021
 * Name: Kyle Chong & Patrick Kwok
 * Teacher: Mr. Ho
 * Description: The class file 'mainframe.java' is the file that provides the bar chart (DistributionChart.fxml) with all its data.
 * This class file will sort through numbers in a file that the user inputs ie. sales.csv. It will then keep track of the first 
 * digit of every number while also ignoring any words (anything that doesn't start with 0-9). 
 * The program will then calculate the percentage occurance of each first digit and determine if the sales numbers are fraudulent (WIP for Patrick)
 */

//Imports
import java.io.*;
import java.util.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class Mainframe implements Initializable{
    private double digitPercent[] = new double[10]; //Defines the double array variable digitPercent[] with a length of 10
    
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
         * In this case, the method calls the readFile (explains later) which essentially gets the percent occurance of each first digit in a data set. 
         * The percent occurance is then stored inside of an array. Each digit's percent is then sent to the bar chart by using 'getData().add'.
         * In a sense, the initialize method acts as a 'main' loop in any normal code
         */
        
        try{    //Tries the readFile() method
            readFile();
        } catch(FileNotFoundException e){   //If it catches an exception,
            e.printStackTrace();    //If an exception is caught, it will print where the error occured 
        }
        
        XYChart.Series<String, Double> setl = new XYChart.Series<>();   //Sets a new XYChart series with parameters <String, Double> under the variable setl
        
        for(int i = 1; i < 10; i++){    //For each digit 0-9
            setl.getData().add(new XYChart.Data<String,Double>(Integer.toString(i), digitPercent[i]));  //Adds the x and y values i and digitPercent[i] to the chart's data
        }
        distributionChart.getData().addAll(setl);   //Essentially sends the data for the fxml to read and implement into the bar chart
    }

    public void readFile() throws FileNotFoundException {
        /*
         * The readFile method calls other methods while also asking for the user's input.
         * The method asks the user to input the name of the file they want analysed. 
         * The file name is then stored inside of 'filePath' and then with the Scanner 'fileInput', this scanner uses 'filePath' to read the contents of the specified file.
         * The variable 'dir' is there to brute force the file search because of an issue with VSCode and projects. I discovered an issue where VSCode will go through the 
         * 'appdata' file in your computer and go into the class file's location however, for some reason will NOT look inside of the 'src' folder which is where all the 
         * progarm files are stored. To fix this, \\src\\ is manually added to the end of 'fileInput' so that the program properly looks through the 'src' folder.
         * The methods 'countDigits' and 'reportResults' are then called. (These will be explained later)
         * 
         * @param reader - a Scanner used to prompt user input
         * @param filePath - the name of the file that the user wants to be analyzed
         * @param dir - the class's location within the computer. (will be explained later)
         * @param digitCount - an array that stores the occurence of each first digit
         */
        Scanner reader = new Scanner(System.in);    //Initializes new Scanner
        System.out.println("To determine if the data is fraudulent, please enter the file name: "); //Text that prompts user to input the file name
        String filePath = reader.nextLine();    //Defines the String 'filePath' which will store the user's input
        String dir = System.getProperty("user.dir");    //The class file location where the program looks for input. Long story short, this is here because of an issue with VSCode. 
        Scanner fileInput = new Scanner(new File(dir + "\\src\\"+ filePath));   //Sets the the Scanner 'fileInput' with the file path to where the file is stored (explained in docstring)
        fileInput.useDelimiter("[,\n]");    //Defines the delimiters used
        int[] digitCount = countDigits(fileInput);  //Changes the value of th array 'digitCount' by calling the 'countDigits' method
        reportResults(digitCount);  //Calls the 'reportResults' method
        reader.close(); //Closes the Scanner 'reader'
    }
    
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

    // returns the first nonzero digit of a string, 0 if no such digit found
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

    public static int sumOf(int[] arr) {
        /*
         * The method 'sumOf' gets the sum of the integers in the given array.
         * For example it's later called in the 'reportResults' array and the given array is digitCount.
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

    // Reports percentages for each leading digit, excluding zeros
    public void reportResults(int[] digitCount) {
        /*
         * This method, 'reportResults' reports the end result of the percentage occurence for each leading digit.
         * The method also then prints out a table showing the occurence data for each leading digit. 
         * 
         * @param digitCount[] - an array containing the number of times each leading digit occured
         *
         * @returns digitPercent[] - an array containing the percentage occurence for each digit
         * @returns - a table containing the data for each leading digit
         */
        int total = sumOf(digitCount) - digitCount[0];
        System.out.println("\nDigit Count Percent"); //Prints out the category headers
        for (int i = 1; i < digitCount.length; i++) {   //Goes through each element in the array
            digitPercent[i] = digitCount[i] * 100.0 / total;    //Gets the percent occurence for each leading digit
            System.out.printf("%5d %5d %6.2f\n", i, digitCount[i], digitPercent[i]);    //Prints out the results for each leading digit
        }
        System.out.printf("Total %5d %6.2f\n", total, 100.0);   //Prints out bottom headers that line up with the data 
    }
}
