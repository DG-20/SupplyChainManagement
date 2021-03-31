package edu.ucalgary.ensf409;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class orderform {
    private String selectedFurniture;
    private String selectedType;
    private int quantity;

    public orderform(String furnitureItem, String furnitureType, int qunatityFurniture) throws IOException {
        this.selectedFurniture = furnitureItem;
        this.selectedType = furnitureType;
        this.quantity = qunatityFurniture;

        finalOrderTextFile(selectedFurniture, selectedType, quantity);
    }

    public void finalOrderTextFile(String furniture, String type, int quantity) throws IOException {
        String outputForm = "";
        outputForm = "Furniture Order Form\n" + "\n" + "Faculty Name:     " + "\n" + "Contact:      \n" + "Date:     \n"
                + "\n";
        outputForm = outputForm
                .concat("Original Request: " + type + " " + furniture + ", " + Integer.toString(quantity) + "\n");
        FileWriter fileOutput = new FileWriter("orderform.txt");
        fileOutput.write(outputForm);
        fileOutput.close();
    }
}
