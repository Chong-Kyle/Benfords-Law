//NOTE THIS IS PATRICK'S ORIGINAL FILE BEFORE IT WAS EDITED TO FIT INSIDE THE MAIN PROGRAM 'APP.JAVA'
import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Scanner;

public class BenfordsLawAssignment {

	// calls menu methods
	public static void main(String[] args) {
		menu();
	}
	
	// displays input options, prompts and exception handles input, and calls certain method depending on input
	// @param none
	// @returns none
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
			checkSalesData();
			break;
		}
		menu();	// restarts menu
	}
	
	// opens sales.csv file
	// @param none
	// @returns none
	public static void readSalesData() {
		try {
			Desktop.getDesktop().open(new File("sales.csv").getAbsoluteFile());	// opens sales.csv
			System.out.println("Sales data loaded.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// scans frequency of digits 1 through 9 in sales.csv and loads and opens analysis of sales.csv in results.csv
	public static void checkSalesData() {
		try {
			Scanner s = new Scanner(new File("sales.csv").getAbsoluteFile());	// creates sales.csv scanner
			int[] fDigit = new int[9];	// stores frequency data of all 9 digits
			while (s.hasNextLine()) {	// while there is still data to scan in sales.csv
				String data = s.nextLine();	// scan and store data into string variable data
				try {
					data = data.substring(data.indexOf(',')+1, data.indexOf(',')+2);	// assigns first digit to string variable data
				} catch (Exception e) {}	// ignore exceptions
				switch (data) {	// increases the fDigit index that corresponds to first digit
				case "1":
					fDigit[0]++;
					break;
				case "2":
					fDigit[1]++;
					break;
				case "3":
					fDigit[2]++;
					break;
				case "4":
					fDigit[3]++;
					break;
				case "5":
					fDigit[4]++;
					break;
				case "6":
					fDigit[5]++;
					break;
				case "7":
					fDigit[6]++;
					break;
				case "8":
					fDigit[7]++;
					break;
				case "9":
					fDigit[8]++;
					break;
				}
			}
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
			NumberFormat nf = NumberFormat.getPercentInstance();	// creates numberformat nf for percentages
			nf.setMaximumFractionDigits(1);	// sets nf maximum decimal place to 1
			float perc1 = 0;	// declares float variable perc1 for the freq. perc. of the 1 first digit
			w.write("Firs.Digi.,Freq.Perc.,Visual\n");	// writes table titles
			for (int i = 0; i < 9; i++) {	// following code repeated 9 times with different first digit each time
				float perci = (float)fDigit[i]/(float)(fDigit[0]+fDigit[1]+fDigit[2]+fDigit[3]+fDigit[4]+fDigit[5]+fDigit[6]+fDigit[7]+fDigit[8]);	// calculates freq. perc. of current first digit
				if (i == 0) {	// if first digit is currently 1, then store freq. perc. to float variable parc1
					perc1 = perci;
				}
				w.write(i+1+","+nf.format(perci)+",");	// writes first digit and the freq. perc. into table
				for (int x = 0; x < perci*100; x++) {	// writes "[]" for each 1% of the first digit's freq. perc. rounded
					w.write("[]");
				}
				w.write("\n");	// filewriter moves down to begin next first digit
			}
			if (perc1*100 > 29 && perc1*100 < 32) {	// if freq. perc. of first digit 1 is between 29 and 32:
				w.write("The data indicates that fraud likely did not occur.");
			} else {
				w.write("The data indicates that fraud likely did occur.");
			}
			w.close();	// closes filewriter w
			Desktop.getDesktop().open(results);	// opens results.csv
			System.out.println("Sales data analysis loaded.");
		} catch (Exception e) {
			System.out.println("Error");
			e.printStackTrace();
		}
	}

}
