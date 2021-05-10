/*
 * Date: May 5th - 12th, 2021
 * Name: Kyle Chong
 * Teacher: Mr. Ho
 * Description: The class file 'App' is the file used to run the entire program. 
 * It's main purpose is to call the fxml file, 'DistributionChart.fxml', which then further calls its controler class, 'Mainframe.java'. 
 * This class, 'App', is also responsible for setting the separate window that the bar chart will be appearing on. 
 */

//Imports for JavaFX
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application{  //Extends application means that it inherits a class from another class/file location
   public void start(Stage primaryStage){ //Start means that it will execute this method upon start. It has the parameter "primaryStage" which is used for the graphic window
      /*
       * The method 'start' is responsible for initializing and setting parameters for the bar chart's window.
       * This method essentially starts a chain of files by calling "DistributionChart.fxml" which is essentially an empty bar chart template with no data. 
       * The fxml file then calls its controller file called "Mainframe.java" which is responsible for providing the data that the bar chart needs. 
       * The method then sets up the window with a window name, scene, and also makes the window actually appear.
       * When the class file is run, a separate window will appear with the filled out bar chart.
       */
       try{
           FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DistributionChart.fxml"));  //Calls the fxml file 'DistributionChart.fxml' made in SceneBuilder
           Parent root = (Parent) fxmlLoader.load();  //Loads FXML Loader
           Scene scene = new Scene(root); //The variable "scene" is defined as a new Scene derived from 'root' 

           primaryStage.setTitle("Distribution Chart");  //Sets the window title
           primaryStage.setScene(scene);  //Sets the window scene
           primaryStage.show();  //Makes the window visible/show
       } catch (Exception e) {   //Catches exception
           e.printStackTrace();  //Traces where the error occured
       }
   }
   public static void main(String[] args){   //Main loop
       /*
        * The main method for this specific class does nothing since we only need the 'start' method for JavaFX
        * In this case, the main loop has nothing in it except 'launch(args)' so that the main loop runs but doesn't do anything.
        */
       launch(args); //In this case we didn't need a main loop so 'launch(args)' initializes the main loop with nothing in it
   }
}
