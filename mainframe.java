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

public class mainframe implements Initializable{

    private double digitPercent[] = new double[10]; 

    @FXML
    private BarChart<String, Double> distributionChart;

    @FXML
    private CategoryAxis digit;

    @FXML
    private NumberAxis percent;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        try{
            readFile();
            System.out.println("hi");
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }
        
        XYChart.Series<String, Double> setl = new XYChart.Series<>();
        
        for(int i = 0; i < 10; i++){
            setl.getData().add(new XYChart.Data<String,Double>(Integer.toString(i), digitPercent[i]));
        }
        

        distributionChart.getData().addAll(setl);
    }

    public void readFile() throws FileNotFoundException {
        Scanner reader = new Scanner(System.in);
        System.out.println("To determine if the data is fraudulent, please enter the file name: ");
        String filePath = reader.nextLine();
        String dir = System.getProperty("user.dir");
        Scanner fileInput = new Scanner(new File(dir + "\\src\\"+ filePath));
        int[] digitCount = countDigits(fileInput);
        reportResults(digitCount); 
        reader.close(); 
    }

    // Reads integers from input, computing an array of counts
    // for the occurrences of each leading digit (0-9).
    public static int[] countDigits(Scanner fileInput) {
        int[] digitCount = new int[10];
        while (fileInput.hasNext()) {
            int n = firstDigitOf(fileInput.next());
            digitCount[firstDigit(n)]++;
        }
        return digitCount;
    }

    // returns the first nonzero digit of a string, 0 if no such digit found
    public static int firstDigitOf(String token) {
        for (char ch : token.toCharArray()) {
            if (ch >= '1' && ch <= '9') {
                return ch - '0';
            }
        }
        return 0;
    }

    // returns the first digit of the given number
    public static int firstDigit(int n) {
        int result = Math.abs(n);
        while (result >= 10) {
            result = result / 10;
        }
        return result;
   }

    // returns the sum of the integers in the given array
    public static int sum(int[] data) {
        int sum = 0;
        for (int n : data) {
            sum += n;
        }
        return sum;
    }

    // Reports percentages for each leading digit, excluding zeros
    public void reportResults(int[] digitCount) {
        
        System.out.println();
        int total = sum(digitCount) - digitCount[0];
        System.out.println("Digit Count Percent");
        for (int i = 1; i < digitCount.length; i++) {
            digitPercent[i] = digitCount[i] * 100.0 / total;
            System.out.printf("%5d %5d %6.2f\n", i, digitCount[i], digitPercent[i]);
        }
        System.out.printf("Total %5d %6.2f\n", total, 100.0);
    }
}

