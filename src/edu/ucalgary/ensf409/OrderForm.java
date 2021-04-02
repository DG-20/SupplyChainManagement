package edu.ucalgary.ensf409;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class OrderForm extends DataBaseManipulator {
    public static void main(String[] args) throws IOException {
        String url = "jdbc:mysql://localhost/inventory"; 
        String username = "scm"; 
        String password = "ensf409"; 
        OrderForm object = new OrderForm(url, username, password); 
    }

    /* The constructor OrderForm takes in 3 arguments: A string containing the path
    *  to the file, a string for the username of the host, and a string for the password
    *  to the host connection. It then uses the stored values of the furniture, the type
    *  of the furniture, and the quantity desired to the creation method of the finalOrder.
    */
    public OrderForm(String url, String username, String password) throws IOException 
    {
        super(url, username, password); 
        finalOrderTextFile(super.furnitureChosen, super.typeChosen, super.quantity);
    }

    /* The following method finalOrderTextFile generates a order form and concatonates 
    *  many different strings that format and that are added onto the form from the 
    *  database. The method takes in 3 arguments: A string for the desired furniture, 
    *  a string for the type of furniture selected, and the quantity desired for that
    *  type of furniture. The formatting alongs with the various inputted strings are 
    *  then written to an output file called orderform. The orderform is then closed.
    */ 

    public void finalOrderTextFile(String furniture, String type, int quantity) throws IOException 
    {
        System.out.println("Printing Furniture Order Form...\n");
        String outputForm = "";
        outputForm = "┎                                                          ┓\n";
        outputForm = outputForm.concat("\nFurniture Order Form\n");
        outputForm = outputForm.concat("┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅\n");
        outputForm = outputForm.concat("Faculty Name:     " + "\n" + "Contact:      \n" + "Date:     \n" + "\n");
        outputForm = outputForm
                .concat("Original Request: " + type + " " + furniture + ", " + Integer.toString(quantity) + "\n");
        outputForm = outputForm.concat("\n" + "Items Ordered" + "\n");
        outputForm = outputForm.concat("ID: ");
        outputForm = outputForm.concat("\n\n" + "Total Price: "+ super.lowestPrice);
        outputForm = outputForm.concat("\n\n┗                                                          ┛\n");
        FileWriter fileOutput = new FileWriter("orderform.txt");
        fileOutput.write(outputForm);
        fileOutput.close();
        System.out.println("Furniture Order Form generated.");
    }
}
