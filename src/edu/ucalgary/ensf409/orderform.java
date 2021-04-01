package edu.ucalgary.ensf409;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class OrderForm extends DataBaseManipulator {
    public static void main(String[] args) throws IOException {
        System.out.println("Gets to orderform");
        String url = "jdbc:mysql://localhost/inventory";
        String username = "scm";
        String password = "ensf409";
        OrderForm object = new OrderForm(url, username, password);
    }

    public OrderForm(String url, String username, String password) throws IOException {
        super(url, username, password);
        finalOrderTextFile(super.furnitureChosen, super.typeChosen, super.quantity);
    }

    public void finalOrderTextFile(String furniture, String type, int quantity) throws IOException {
        String outputForm = "";
        outputForm = "Furniture Order Form\n" + "\n" + "Faculty Name:     " + "\n" + "Contact:      \n" + "Date:     \n"
                + "\n";
        outputForm = outputForm
                .concat("Original Request: " + type + " " + furniture + ", " + Integer.toString(quantity) + "\n");
        outputForm = outputForm.concat("\n" + "Items Ordered" + "\n");
        outputForm = outputForm.concat("ID: ");
        outputForm = outputForm.concat("\n" + "Total Price: ");
        FileWriter fileOutput = new FileWriter("orderform.txt");
        fileOutput.write(outputForm);
        fileOutput.close();
    }
}
