/**
 * @author Divyansh Goyal <a href="mailto:divyansh.goyal@ucalgary.ca">divyansh.goyal@ucalgary.ca</a>
 * 
 * @author Maheen Hossain <a href="mailto:maheen.hossain@ucalgary.ca">maheen.hossain@ucalgary.ca</a>
 * 
 * @author Liam Parmar <a href="mailto:liam.parmar@ucalgary.ca">liam.parmar@ucalgary.ca</a>
 * 
 * @author Curtis Silva <a href="mailto:curtis.silva@ucalgary.ca">curtis.silva@ucalgary.ca</a>
 * 
 * @version 85.0
 * 
 * @since 1.0
 */
package edu.ucalgary.ensf409;

/*
 * Importing the libraries necessary to read in from the terminal to obtain user choices.
 */
import java.io.*;
import java.util.Scanner;

/**
 * OrderForm is a class which is the output part for this program.
 * OrderForm extends DataBaseManipulator which in turn extends
 * InputReader. Thus, the main method is implemented here as calling
 * the constructor of this also calls the constrcutor of the other two.
 * OrderFrom outputs a txt file which summarizes the order by using information
 * from DataBaseManipulator.
 */
public class OrderForm extends DataBaseManipulator {

    private String outputFormInfo;
    /** 
     * The main method specifies the connection for the database.
     * It also creates an object of type OrderForm which is where everything
     * begins to run from.
     */
    public static void main(String[] args) throws IOException {
        String url = "jdbc:mysql://localhost/inventory"; 
        String username = "scm"; 
        String password = "ensf409"; 
        OrderForm myOrderForm = new OrderForm(url, username, password); 
    }

    /** 
     * The constructor OrderForm takes in 3 arguments: A string containing the path
     * to the file, a string for the username of the host, and a string for the password
     * to the host connection. It then calls the super constructor and passes in these values
     * so that DataBaseManipulator can initialize the connection.
     * It then uses the stored values of the furniture, the type
     * of the furniture, and the quantity desired to the creation method of 
     * the finalOrder, finalOrderTextFile.
     */
    public OrderForm(String url, String username, String password) throws IOException 
    {
        super(url, username, password);
        finalOrderTextFile(super.furnitureChosen, super.typeChosen, super.quantity);
    }


    /** 
     * This constructor overloads OrderForm and takes in 7 arguments: A string containing the path
     * to the file, a string for the username of the host, and a string for the password
     * to the host connection. It then calls the super constructor and passes in these values
     * so that DataBaseManipulator can initialize the connection. There are also String arguments for
     * the type and furniture, as well as an int for the quantity asked for and a boolean to check is its a full test. 
     * It then uses the asrgument values of the furniture, the type
     * of the furniture, and the quantity desired to the creation method of 
     * the finalOrder, finalOrderTextFile. This constructor is only used for test purposes.
     */
    public OrderForm(String furniture, String type, int quantityAskedFor, String url, String username, String password, boolean fullTest)
    {
        super(furniture, type, quantityAskedFor, url, username, password, fullTest);
        try
        {
            finalOrderTextFile(super.furnitureChosen, super.typeChosen, super.quantity);
        }
        catch (Exception e)
        {
            System.out.println("Something went wrong when trying to generate orderform.txt!");
            System.exit(1);
        }
    }

    /**
     * The following method finalOrderTextFile generates an order form and concatenates 
     *  many different strings that are formatted in specified ways using information from
     *  DataBaseManipulator. The method takes in 3 arguments: A string for the desired furniture, 
     *  a string for the type of furniture selected, and the quantity desired for that
     *  type of furniture. The formatting along with the various inputted strings are 
     *  then written to an output file called OrderForm.txt. The orderform is then closed.
     */ 
    public void finalOrderTextFile(String furniture, String type, int quantity) throws IOException 
    {
        System.out.println("Printing Furniture Order Form...\n");
        outputFormInfo = "";
        outputFormInfo = "???                                                          ???\n";
        outputFormInfo = outputFormInfo.concat("\nFurniture Order Form\n");
        outputFormInfo = outputFormInfo.concat("?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????\n");
        outputFormInfo = outputFormInfo.concat("Faculty Name:     " + "\n" + "Contact:      \n" + "Date:     \n" + "\n");
        outputFormInfo = outputFormInfo
                .concat("Original Request: " + type + " " + furniture + ", " + Integer.toString(quantity) + "\n");
        outputFormInfo = outputFormInfo.concat("\n" + "Items Ordered" + "\n");
        for(int i = 0; i < super.codes.size(); i++)
        {
            outputFormInfo = outputFormInfo.concat("ID: " + super.codes.get(i));
            outputFormInfo = outputFormInfo.concat("\n");
        }
        outputFormInfo = outputFormInfo.concat("\n\n" + "Total Price: " + super.getLowestPrice());
        outputFormInfo = outputFormInfo.concat("\n\n???                                                          ???\n");
        FileWriter fileOutput = new FileWriter("OrderForm.txt");
        fileOutput.write(outputFormInfo);
        fileOutput.close();
        System.out.println("Furniture Order Form generated.");
    }

    /**
     * getter method which returns the value for the private String method outputFormCode.
     */
    public String getOrderFormCode()
    {
        return(this.outputFormInfo);
    }
}
