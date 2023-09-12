import javax.swing.*;
//This line imports the Swing library, which is used to create graphical user interfaces in Java.//

public class Main {  //This is the main class of your Java application.//

    public static void main(String[] args) {  //This is the entry point of your application. 
        //The main method is where your program starts execution.//
         
        SwingUtilities.invokeLater(TaxCalculatorFrameWithFeatures::new);
    }
}

//This line schedules the creation of an instance of the TaxCalculatorFrameWithFeatures class to run on the EDT. 
//The TaxCalculatorFrameWithFeatures::new syntax is a method reference that refers to the constructor of 
//the TaxCalculatorFrameWithFeatures class. By invoking it using invokeLater, 
//you ensure that the GUI components are created and updated on the EDT. //