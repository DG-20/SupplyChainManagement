/**
 * @author Divyansh Goyal <a href="mailto:divyansh.goyal@ucalgary.ca">divyansh.goyal@ucalgary.ca</a>
 * 
 * @author Maheen Hossain <a href="mailto:maheen.hossain@ucalgary.ca">maheen.hossain@ucalgary.ca</a>
 * 
 * @author Liam Parmar <a href="mailto:liam.parmar@ucalgary.ca">liam.parmar@ucalgary.ca</a>
 * 
 * @author Curtis Silva <a href="mailto:curtis.silva@ucalgary.ca">curtis.silva@ucalgary.ca</a>
 * 
 * @version 63.0
 * 
 * @since 1.0
 */
package edu.ucalgary.ensf409;

// Importing the necessary packages.
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.io.FileWriter;

/**
 * DataBaseManipulator is a class which performs the algorithms required 
 * to calculate the cheapest combination available for the order. If no such
 * combination is possible, this class prints the suggested manufacturers
 * associated with the selected type of furniture. This class is a child class
 * of InputReader and thus has access to the selections that are stored as 
 * protected member variables. DataBaseManipulator also updates the database
 * if the order is fulfilled, by deleting the rows that were used to fulfil 
 * the order. To do so, the connection to the database, inventory.sql, is
 * also established here.
 */
public class DataBaseManipulator extends InputReader {

    public static void main(String[] args) throws IOException {
    }

    // The URL, USERNAME, and PASSWORD are member variables which are used in 
    // the initializeConnection method to establish a connection with inventory.sql.
    private final String URL;
    private final String USERNAME;
    private final String PASSWORD;
    // The dataBaseConnection member just holds the connection to the database so that
    // the methods which deal with reading or writing to the database can create and apply
    // queries and statements. 
    protected Connection dataBaseConnection;
    // The following four String arrays provide the names of the available manufacturers 
    // that supply the specified type of furniture.
    private final String[] MANUCHAIRS = { "Office Furnishings", "Chairs R Us", "Furniture Goods", "Fine Office Supplies" };
    private final String[] MANUDESKS = { "Academic Desks", "Office Furnsishings, Furniture Goods", "Fine Office Supplies" };
    private final String[] MANUFILINGS = { "Office Furnishings", "Furniture Goods", "Fine Office Supplies" };
    private final String[] MANULAMP = { "Office Furnishings", "Furniture Goods", "Fine Office Supplies" };
    // The storage 2D array is created in the method create2DArray and is used throughout the class
    // to have a version of the database rows which are relevant to the selections made in InputReader.
    // This is used in all three algorithms to iterate over and create combinations which can fulfill the order.
    protected String[][] storage;
    // The following three members are instantiated in minFinder and correspond to the index of the 
    // cell which contains the combination of rows to obtain the cheapest price, the cheapest price
    // itself, and the actual rows, stored in the specified index as a String.
    private int rowToAdd;
    protected int lowestPrice;
    private String lowestPriceCell;
    // The following ArrayList codes is used in the method getCodes to store the parts 
    // that are to be used in the cheapeast combination. This ArrayList is then accessed 
    // later on in the method deleteFromDatabase to delete the rows corresponding to these IDs.
    protected ArrayList<String> codes= new ArrayList<String>();
    // quantityStored retrieves the quantity member field from InputReader by using super and storing it.
    private int quantityStored = super.quantity;
    protected int rowsAffected = 0;
    private boolean done = false;

    /** 
     * The constructor is where the appropriate algorithm is called based on the stored variables
     * in InputReader. This is also where, if no combinations are found, it prints the list
     * of manufacturers. The constructor takes in three Strings, url, username, and password
     * and stores them in the corresponding member fields.
     */
    public DataBaseManipulator(String url, String username, String password) throws IOException {
        this.URL = url;
        this.USERNAME = username;
        this.PASSWORD = password;
        // Initialize the connection to the database.
        initializeConnection();

        // Get the quantity requested from InputReader.
        int numOfItemsRequested = super.quantity;

        // Create the 2D array by calling this method.
        create2DArray();

        boolean status = false;

        int priceStore = 0;

        // If "lamp" was selected as the furniture, keep running the algorithm
        // until either the whole order is fulfilled, or the algorithm is unable
        // to complete the full order. And on every run, add the lowest price to 
        // obtain the total for the order.
        if (super.furnitureChosen.equals("lamp")) {
            for (int i = 0; i < numOfItemsRequested; i++)
            {
                status = algorithmToCreateOrderForLamp();
                this.quantityStored--;
                if (status == false)
                  break;  
                if (this.done == true)
                {
                    priceStore += getLowestPrice();
                    break;
                }
                priceStore += getLowestPrice();
            }
        }

        setLowestPrice(priceStore);

        // If "chair" was selected as the furniture, keep running the algorithm
        // until either the whole order is fulfilled, or the algorithm is unable
        // to complete the full order. And on every run, add the lowest price to 
        // obtain the total for the order.
        if (super.furnitureChosen.equals("chair")) {
            for (int i = 0; i < numOfItemsRequested; i++)
            {
                status = algorithmToCreateOrderForChair();
                this.quantityStored--;
                if (status == false)
                  break;  
                if (this.done == true)
                {
                    priceStore += getLowestPrice();
                    break;
                }
                priceStore += getLowestPrice();
            }
        }

        setLowestPrice(priceStore);

        // If "desk" or "filing" (they both call upon the algorithmToCreateOrderForElse) 
        // was selected as the furniture, keep running the algorithm
        // until either the whole order is fulfilled, or the algorithm is unable
        // to complete the full order. And on every run, add the lowest price to 
        // obtain the total for the order.
        if (super.furnitureChosen.equals("desk") || super.furnitureChosen.equals("filing")) {
            for (int i = 0; i < numOfItemsRequested; i++)
            {
                status = algorithmToCreateOrderForElse();
                this.quantityStored--;
                if (status == false)
                  break;  
                if (this.done == true)
                {
                    priceStore += getLowestPrice();
                    break;
                }
                priceStore += getLowestPrice();
            }
        }

        setLowestPrice(priceStore);

        // If the for loops above were unable, at any point, to create
        // the complete order.
        if (status == false)
        {
            // General print statements.
            System.out.println("\nUnable to create the order as there are not enough materials in stock to do so!");
            System.out.println("Here are a list of manufacturers that may have the needed items: \n");
            // If the furniture selected was a desk, print the appropriate manufacturers.
            if (super.furnitureChosen.equals("desk"))
            {
                for (int i = 0; i < MANUDESKS.length; i++)
                {
                    System.out.println(i+1 + ". " + MANUDESKS[i]);
                }
            }
            // If the furniture selected was a filing, print the appropriate manufacturers.
            if (super.furnitureChosen.equals("filing"))
            {
                for (int i = 0; i < MANUFILINGS.length; i++)
                {
                    System.out.println(i+1 + ". " + MANUFILINGS[i]);
                }
            }
            // If the furniture selected was a chair, print the appropriate manufacturers.
            if (super.furnitureChosen.equals("chair"))
            {
                for (int i = 0; i < MANUCHAIRS.length; i++)
                {
                    System.out.println(i+1 + ". " + MANUCHAIRS[i]);
                }
            }
            // If the furniture selected was a lamp, print the appropriate manufacturers.
            if (super.furnitureChosen.equals("lamp"))
            {
                for (int i = 0; i < MANULAMP.length; i++)
                {
                    System.out.println(i+i + ". " + MANULAMP[i]);
                }
            }

            resetOrderForm();
            // Terminate the program.
            System.exit(1);
        }
    }

    /**
     * resetOrderForm() is a private method that returns nothing. The method resetOrderForm contains 
     * a String called outputForm that creates a string for the OrderForm text file. This outputForm
     * String shows the original request the user made and also a message that states "Unable to fullfil
     * request at this time" in the orderForm text file. The Total price is also N/A. All this information 
     * gets provided and printed to the OrderFrom text file. Also this method gets called in the constructor
     * if status equals false. Otherwise the finalOrderTextFile in the OrderForm constructor will provide
     * proper contents to the orderForm text file (with the ID codes and Total Price) because the request
     * is able to happen.
     */
    private void resetOrderForm() throws IOException
    {
        String outputForm = "";
        outputForm = "┎                                                          ┓\n";
        outputForm = outputForm.concat("\nFurniture Order Form\n");
        outputForm = outputForm.concat("┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅┅\n");
        outputForm = outputForm.concat("Faculty Name:     " + "\n" + "Contact:      \n" + "Date:     \n" + "\n");
        outputForm = outputForm
                .concat("Original Request: " + typeChosen + " " + furnitureChosen + ", " + quantity + "\n");
        outputForm = outputForm.concat("\n" + "Items Ordered" + "\n");
        outputForm = outputForm.concat("Unable to fulfill request at this time.");
        outputForm = outputForm.concat("\n");

        outputForm = outputForm.concat("\n\n" + "Total Price: N/A");
        outputForm = outputForm.concat("\n\n┗                                                          ┛\n");
        FileWriter fileOutput = new FileWriter("OrderForm.txt");
        fileOutput.write(outputForm);
        fileOutput.close();
        System.out.println("Furniture Order Form generated.");
    }

    /** 
     * sumAllRows is a method that totals up all of the sums of a specified furniture object and its specific sub type.
     * It makes a statement called newstmt which is used to execute a select from statement. The result of the executed
     * statement is then stored and the string contained under the "Type" column is compared to the desired type. If  
     * they match, then the "Price" column of that particular result is taken in and added onto the price. This method is
     * used for the base cases in all three algorithm methods, in case all rows are required to fulfil the specified order.
    */
    protected void sumAllRows(String furniture, String type) 
    {
        Statement newstmt;
        int lowestPriceToAdd = 0;
        try
        {
            newstmt = this.dataBaseConnection.createStatement();
            ResultSet results = newstmt.executeQuery("SELECT * FROM " + furniture); 
            while(results.next())
            {
                 if((results.getString("Type").equals(type)))
                {
                    String idToAdd = results.getString("ID");
                    if (!this.codes.contains(idToAdd))
                        this.codes.add(idToAdd);
                    lowestPriceToAdd += Integer.parseInt(results.getString("Price"));
                }
            }
        setLowestPrice(lowestPriceToAdd);
        }

        catch(SQLException e)
        {
            System.out.println("Unable to calculate sum of all rows");
            System.exit(1);
        }
    }

    /** 
     * deleteAllRows is a method that deletes all the rows that match the specified furniture type.
     * A query is created that holds the instruction to delete from a specified furniture name where
     * the desired type to delete is stored. This method is used in conjunction with sumAllRows,
     * if the base case is needed, this deletes all the rows of the specified furniture type after
     * using them all to add to the price.
    */
    protected void deleteAllRows(String furnitureName, String type) {
        PreparedStatement myStmt;
        try {
            String query = "DELETE FROM " + furnitureName + " WHERE Type = ?";
            myStmt = dataBaseConnection.prepareStatement(query);
            myStmt.setString(1, type);
             myStmt.executeUpdate();
        }

        catch (SQLException e) {
            System.out.println("Unable to delete all rows of type " + type);
            System.exit(1);
        }
    }

    /** 
     * deleteFromDataBase is a method which creates a statement and query which 
     * deletes certain rows in the database using the IDs which have been used
     * already to create a combination to obtain the lowest price when fulfilling 
     * an order. This is done within a for loop which iterates over the size of the
     * ArrayList which contains the codes. Then, within each iteration, the statement
     * is executed and thus the corresponding rows are deleted from the database.
     * Note: The updates to the entire database only appear after the code has terminated
     * and thus the 2D array containing the relevant part of the database is updated at a later
     * point in the code. 
     */
    protected void deleteFromDataBase(ArrayList<String> codesToDel) {
        PreparedStatement myStmt;
        try {
            for (int i = 0; i < codesToDel.size(); i++)
            {
                String query = "DELETE FROM " + this.furnitureChosen + " WHERE ID = ?";
                myStmt = this.dataBaseConnection.prepareStatement(query);
                myStmt.setString(1, codesToDel.get(i));
                this.rowsAffected += myStmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Unable to delete from the database! Something went wrong :(");
            System.exit(1);
        }
    }

    /*
     * create2DArray is the method which recreates the relevant part, the part that 
     * we are interested in, into a 2D array to be used locally. Every Y is included
     * in the array as the ID representing the specific row of the database. Every N
     * is included in the array as a -1. Both are written as Strings so that the IDs
     * can be re-used for deleting rows from the database.
     * This method initially begins with reading data in from the database
     * to count the number of rows for the type of furniture chosen to get the height
     * needed for the 2D array. Then, another try/catch block is used to read in the Y/N
     * from each cell in the database in the relevant rows and then storing either the 
     * ID associated with the row for a Y or "-1" in each cell in the 2D array iteratively.
     * Thus, this method creates and populates a 2D array to then be used for all other methods 
     * in DataBaseManipulator.
     */
    private void create2DArray() {
        int numOfCols = 0;
        String[] furnitureParts;

        if (super.furnitureChosen.equals("chair")) {
            numOfCols = 5;
            String[] furniturePartsTemp = { "Legs", "Arms", "Seat", "Cushion", "Price" };
            furnitureParts = furniturePartsTemp.clone();
        } else if (super.furnitureChosen.equals("desk")) {
            numOfCols = 4;
            String[] furniturePartsTemp = { "Legs", "Top", "Drawer", "Price" };
            furnitureParts = furniturePartsTemp.clone();
        } else if (super.furnitureChosen.equals("filing")) {
            numOfCols = 4;
            String[] furniturePartsTemp = { "Rails", "Drawers", "Cabinet", "Price" };
            furnitureParts = furniturePartsTemp.clone();
        } else if (super.furnitureChosen.equals("lamp")) {
            numOfCols = 3;
            String[] furniturePartsTemp = { "Base", "Bulb", "Price" };
            furnitureParts = furniturePartsTemp.clone();
        } else {
            furnitureParts = new String[5];
        }

        Statement newstmt;
        int numOfRows = 0;
        try {
            newstmt = this.dataBaseConnection.createStatement();
            ResultSet results = newstmt.executeQuery("SELECT * FROM " + super.furnitureChosen);
            while (results.next()) {
                if (results.getString("Type").equals(super.typeChosen))
                    numOfRows++;
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong when trying to read from database!");
            System.exit(1);
        }

        storage = new String[numOfRows][numOfCols];

        try {
            int i = 0;
            newstmt = this.dataBaseConnection.createStatement();
            ResultSet results = newstmt.executeQuery("SELECT * FROM " + super.furnitureChosen);
            while (results.next()) {
                if (results.getString("Type").equals(super.typeChosen)) {
                    for (int j = 0; j < furnitureParts.length; j++) {

                        if (results.getString(furnitureParts[j]).equals("Y") && j < furnitureParts.length - 1
                                && results.getString("Type").equals(super.typeChosen)) {
                            storage[i][j] = results.getString("ID");
                        } else if (j == furnitureParts.length - 1
                                && results.getString("Type").equals(super.typeChosen)) {
                            storage[i][j] = results.getString(furnitureParts[j]);
                        } else {
                            if (results.getString("Type").equals(super.typeChosen)) {
                                storage[i][j] = "-1";
                            }
                        }
                    }
                    i++;
                }

            }

        } catch (SQLException e) {
            System.out.println("Something went wrong when trying to read from database!");
            System.exit(1);
        }

    }

    /** 
     * initializeConnection is a method that creates a connection from the code to the database.
     * dataBaseConnection stores the connection between the sql database and the code by taking 
     * in a url, username, and password for the host connection storing the database.
    */
    protected void initializeConnection() {
        try {
            this.dataBaseConnection = DriverManager.getConnection(getURL(), getUSERNAME(), getPASSWORD());
        } catch (SQLException e) {
            System.out.println("Could not connect to the database!");
            System.exit(1);
        }
    }

    /*
     * algorithmToCreateOrderForChair is a method which applies an algorithm if the 
     * user provides an order for chairs. This method does not take in any arguments, 
     * and returns a boolean based on whether or not it was successful 
     * in satisfying the order. Initially, the base condition is checked
     * by calling upon loopMethod four times, once for each column in the chair table,
     * and storing them into four variables. Then, if the number of Ys in each column
     * of all the rows for the selected furniture is less than the quantity desired,
     * the complete order is not possible and thus this method returns false.
     * Then, if the total amount of Y's in each column is exactly equal to the 
     * ordered quantity, this method calls sumAllRows and then deleteAllRows to
     * delete all the rows from the database and then returns true.
     * If these base cases are not met, then the algorithm takes place. 
     * The algorithm utilizes an ArrayList and iterates over each column in the array
     * in nested for loops, to get all possible combinations to get Y in all four columns
     * (0, 1, 2, and 3). Each combination is combined into a String seperated by a dash and
     * added to the ArrayList. Then, if no combinations were found, meaning the size of the
     * ArrayList == 0, then return false. Next, two arrays listOfPrices and listOfRows 
     * are created and a for loop iterates over all combinations in the ArrayList, adding
     * each price into the array and the rows corresponding to those prices are added to 
     * listOfRows as a String seperated by a comma. In this for loop, to avoid duplicating prices
     * for the same row, all conditions of duplicate rows are written into if statements, and the
     * arrays are updated accordingly. Then, this method calls 
     * minFinder with both arrays. Then, deleteFromDataBase is called to update the database
     * and then this method returns true.
     */
    private boolean algorithmToCreateOrderForChair() {
        ArrayList<String> combinations = new ArrayList<String>();
        int yChecker1 = loopMethod(0);
        int yChecker2 = loopMethod(1);
        int yChecker3 = loopMethod(2);
        int yChecker4 = loopMethod(3);

        if (yChecker1 < this.quantityStored || yChecker2 < this.quantityStored || yChecker3 < this.quantityStored
            || yChecker4 < this.quantityStored) {
                return false;
            }

        if (yChecker1 == super.quantity && yChecker2 == super.quantity && yChecker3 == super.quantity
                && yChecker4 == super.quantity) {
            sumAllRows("chair",super.typeChosen);
            deleteAllRows("chair", super.typeChosen);
            this.done = true;
            return true;
        }

        for (int i = 0; i < storage.length; i++) {
            if (!storage[i][0].equals("-1")) {
                for (int j = 0; j < storage.length; j++) {
                    if (!storage[j][1].equals("-1")) {
                        for (int k = 0; k < storage.length; k++) {
                            if (!storage[k][2].equals("-1")) {
                                for (int l = 0; l < storage.length; l++) {
                                    if (!storage[l][3].equals("-1")) {
                                        combinations.add(i + "-" + j + "-" + k + "-" + l);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (combinations.size() == 0) {
            return false;
        }

        int[] listOfPrices = new int[combinations.size()];
        String[] listOfRows = new String[combinations.size()];
        String combo;
        int positionOfDash;
        int positionOfDash2;
        int positionOfDash3;
        int callRow1;
        int callRow2;
        int callRow3;
        int callRow4;

        for (int i = 0; i < combinations.size(); i++) {
            combo = combinations.get(i);
            positionOfDash = combo.indexOf('-');
            positionOfDash3 = combo.lastIndexOf('-');
            positionOfDash2 = combo.indexOf('-', positionOfDash + 1);
            callRow1 = Integer.parseInt(combo.substring(0, positionOfDash));
            callRow2 = Integer.parseInt(combo.substring(positionOfDash + 1, positionOfDash2));
            callRow3 = Integer.parseInt(combo.substring(positionOfDash2 + 1, positionOfDash3));
            callRow4 = Integer.parseInt(combo.substring(positionOfDash3 + 1, combo.length()));
            // All rows are the same.
            if (callRow1 == callRow3 && callRow1 == callRow2 && callRow1 == callRow4) {
                listOfPrices[i] = Integer.parseInt(storage[callRow1][storage[callRow1].length - 1]);
                listOfRows[i] = String.format("%d,%d", callRow1);
            } 
            // All combos of three rows being the same.
            else if (callRow1 == callRow2 && callRow1 == callRow3) {
                listOfPrices[i] = Integer.parseInt(storage[callRow1][storage[callRow1].length - 1])
                        + Integer.parseInt(storage[callRow4][storage[callRow4].length - 1]);
                listOfRows[i] = String.format("%d,%d", callRow1, callRow4);
            } else if (callRow1 == callRow3 && callRow1 == callRow4) {
                listOfPrices[i] = Integer.parseInt(storage[callRow1][storage[callRow1].length - 1])
                        + Integer.parseInt(storage[callRow2][storage[callRow2].length - 1]);
                listOfRows[i] = String.format("%d,%d", callRow1, callRow2);
            } else if (callRow1 == callRow2 && callRow1 == callRow4) {
                listOfPrices[i] = Integer.parseInt(storage[callRow3][storage[callRow3].length - 1])
                        + Integer.parseInt(storage[callRow1][storage[callRow1].length - 1]);
                listOfRows[i] = String.format("%d,%d", callRow1, callRow3);
            } else if (callRow3 == callRow2 && callRow3 == callRow4) {
                listOfPrices[i] = Integer.parseInt(storage[callRow3][storage[callRow3].length - 1])
                        + Integer.parseInt(storage[callRow1][storage[callRow1].length - 1]);
                listOfRows[i] = String.format("%d,%d", callRow1, callRow3);
            }
            // All combos of pairs being the same.
            else if (callRow1 == callRow2) {

                if (callRow3 != callRow4) {
                    listOfPrices[i] = Integer.parseInt(storage[callRow3][storage[callRow3].length - 1])
                            + Integer.parseInt(storage[callRow1][storage[callRow1].length - 1])
                            + Integer.parseInt(storage[callRow4][storage[callRow4].length - 1]);
                    listOfRows[i] = String.format("%d,%d,%d", callRow1, callRow3, callRow4);
                } else {
                    listOfPrices[i] = Integer.parseInt(storage[callRow3][storage[callRow3].length - 1])
                            + Integer.parseInt(storage[callRow1][storage[callRow1].length - 1]);
                    listOfRows[i] = String.format("%d,%d", callRow1, callRow3);
                }
            } else if (callRow1 == callRow3) {

                if (callRow2 != callRow4) {
                    listOfPrices[i] = Integer.parseInt(storage[callRow2][storage[callRow2].length - 1])
                            + Integer.parseInt(storage[callRow1][storage[callRow1].length - 1])
                            + Integer.parseInt(storage[callRow4][storage[callRow4].length - 1]);
                    listOfRows[i] = String.format("%d,%d,%d", callRow1, callRow2, callRow4);
                } else {
                    listOfPrices[i] = Integer.parseInt(storage[callRow2][storage[callRow2].length - 1])
                            + Integer.parseInt(storage[callRow1][storage[callRow1].length - 1]);
                    listOfRows[i] = String.format("%d,%d", callRow1, callRow2);
                }
            } else if (callRow1 == callRow4) {
                if (callRow2 != callRow3) {
                    listOfPrices[i] = Integer.parseInt(storage[callRow2][storage[callRow2].length - 1])
                            + Integer.parseInt(storage[callRow1][storage[callRow1].length - 1])
                            + Integer.parseInt(storage[callRow3][storage[callRow3].length - 1]);
                    listOfRows[i] = String.format("%d,%d,%d", callRow1, callRow2, callRow3);

                } else {
                    listOfPrices[i] = Integer.parseInt(storage[callRow2][storage[callRow2].length - 1])
                            + Integer.parseInt(storage[callRow1][storage[callRow1].length - 1]);
                    listOfRows[i] = String.format("%d,%d", callRow1, callRow2);
                }
            } else if (callRow2 == callRow3) {
                if (callRow1 != callRow4) {
                    listOfPrices[i] = Integer.parseInt(storage[callRow2][storage[callRow2].length - 1])
                            + Integer.parseInt(storage[callRow1][storage[callRow1].length - 1])
                            + Integer.parseInt(storage[callRow4][storage[callRow4].length - 1]);
                    listOfRows[i] = String.format("%d,%d,%d", callRow1, callRow2, callRow4);
                } else {
                    listOfPrices[i] = Integer.parseInt(storage[callRow2][storage[callRow2].length - 1])
                            + Integer.parseInt(storage[callRow1][storage[callRow1].length - 1]);
                    listOfRows[i] = String.format("%d,%d", callRow1, callRow2);
                }

            } else if (callRow2 == callRow4) {
                if (callRow1 != callRow3) {
                    listOfPrices[i] = Integer.parseInt(storage[callRow2][storage[callRow2].length - 1])
                            + Integer.parseInt(storage[callRow1][storage[callRow1].length - 1])
                            + Integer.parseInt(storage[callRow3][storage[callRow3].length - 1]);
                    listOfRows[i] = String.format("%d,%d,%d", callRow1, callRow2, callRow3);
                } else {
                    listOfPrices[i] = Integer.parseInt(storage[callRow2][storage[callRow2].length - 1])
                            + Integer.parseInt(storage[callRow1][storage[callRow1].length - 1]);
                    listOfRows[i] = String.format("%d,%d", callRow1, callRow2);
                }
            } else if (callRow3 == callRow4) {
                if (callRow1 != callRow2) {
                    listOfPrices[i] = Integer.parseInt(storage[callRow2][storage[callRow2].length - 1])
                            + Integer.parseInt(storage[callRow1][storage[callRow1].length - 1])
                            + Integer.parseInt(storage[callRow3][storage[callRow3].length - 1]);
                    listOfRows[i] = String.format("%d,%d,%d", callRow1, callRow2, callRow3);
                }
                else {
                    listOfPrices[i] = Integer.parseInt(storage[callRow3][storage[callRow3].length - 1])
                            + Integer.parseInt(storage[callRow1][storage[callRow1].length - 1]);
                    listOfRows[i] = String.format("%d,%d", callRow1, callRow3);

                }
            }
            // Everything being different.
            else {
                listOfPrices[i] = Integer.parseInt(storage[callRow1][storage[callRow1].length - 1])
                        + Integer.parseInt(storage[callRow2][storage[callRow2].length - 1])
                        + Integer.parseInt(storage[callRow3][storage[callRow3].length - 1])
                        + Integer.parseInt(storage[callRow4][storage[callRow4].length - 1]);
                listOfRows[i] = String.format("%d,%d,%d,%d", callRow1, callRow2, callRow3, callRow4);
            }
        }

        minFinder(listOfPrices, listOfRows);
        deleteFromDataBase(this.codes);
        return true;
    }

    /* 
     * getCodes is a private method in the DataBaseManipulator java file. It takes in a 
     * string argument and does not return anything. The method getCodes gets called 
     * within the private method minFinder as the last thing in the method. The private
     * method getCodes recieves a String in the form of number followed by commas (ex; #,#,#).
     * The numbers indicate what rows in the selected table should be taken in and used to 
     * find the minimum price for the quantity requested (with all parts). The rows are scanned 
     * to find the ID code which is then stored in a String array. Once the ID codes are stored
     * the row in the storage array is set to -1's.
     */
    private void getCodes(String lowestPriceCell) {
        char[] charArray = lowestPriceCell.toCharArray();
        int commas = 0;
        for(int charachter = 0; charachter < charArray.length; charachter++)
        {
            if(charArray[charachter] == ',')
            {
                commas++;
            }
        }

        int[] rowCells = new int[charArray.length - commas];
        String[] codeInserts = new String[charArray.length - commas];
        int cell = 0;

        String concatenator = "";
        for (int c = 0; c < charArray.length; c++)
        {
            if (Character.isDigit(charArray[c])) 
            {
                concatenator += charArray[c];
            }
            if((charArray[c] == ',') || (c == charArray.length - 1))
            {
                rowCells[cell] = Integer.parseInt(concatenator);
                cell++;
                concatenator = "";
            }
        }

        int cellInsert = 0;
        for (int i = 0; i < rowCells.length; i++) {
            for (int j = 0; j < storage[rowCells[i]].length; j++) 
            {
                char[] charArray2 = storage[rowCells[i]][j].toCharArray();
                if (Character.isLetter(charArray2[0])) 
                {
                    codeInserts[cellInsert] = storage[rowCells[i]][j];
                    cellInsert++;
                    break;
                }
            }
        }

        for (int goThrough = 0; goThrough < charArray.length - commas; goThrough++) 
        {
            codes.add(codeInserts[goThrough]);
        }

        for (int ch : rowCells)
        {
            for (int i = 0; i < storage[ch].length - 1; i++)
            {
                storage[ch][i] = "-1";
            }

        }
    }

    /*
     * algorithmToCreateOrderForLamp is a method which applies an algorithm if the 
     * user provides an order for lamps. This method does not take in any arguments, 
     * and returns a boolean based on whether or not it was successful 
     * in satisfying the order. Initially, the base condition is checked
     * by calling upon loopMethod twice, once for each column in the lamp table,
     * and storing them into two variables. Then, if the number of Ys in each column
     * of all the rows for the selected furniture is less than the quantity desired,
     * the complete order is not possible and thus this method returns false.
     * Then, if the total amount of Y's in each column is exactly equal to the 
     * ordered quantity, this method calls sumAllRows and then deleteAllRows to
     * delete all the rows from the database and then returns true.
     * If these base cases are not met, then the algorithm takes place. 
     * The algorithm utilizes an ArrayList and iterates over each column in the array
     * in a nested for loop, to get all possible combinations to get Y in both columns
     * (0 and 1). Each combination is combined into a String seperated by a dash and
     * added to the ArrayList. Then, if no combinations were found, meaning the size of the
     * ArrayList == 0, then return false. Next, two arrays listOfPrices and listOfRows 
     * are created and a for loop iterates over all combinations in the ArrayList, adding
     * each price into the array and the rows corresponding to those prices are added to 
     * listOfRows as a String seperated by a comma. Then, this method calls 
     * minFinder with both arrays. Then, deleteFromDataBase is called to update the database
     * and then this method returns true.
     */
    private boolean algorithmToCreateOrderForLamp() {
        int yChecker1 = loopMethod(0);
        int yChecker2 = loopMethod(1);

        if (yChecker1 < this.quantityStored || yChecker2 < this.quantityStored) {
                return false;
            }
        if (yChecker1 == super.quantity && yChecker2 == super.quantity) {
            sumAllRows("lamp",super.typeChosen);
            deleteAllRows("lamp", super.typeChosen);
            this.done = true;
            return true;
        }

        ArrayList<String> combinations = new ArrayList<String>();
        for (int i = 0; i < storage.length; i++) {
            if (!storage[i][0].equals("-1")) {
                for (int j = 0; j < storage.length; j++) {
                    if (!storage[j][1].equals("-1")) {
                        combinations.add(i + "-" + j);
                    }
                }
            }
        }

        if (combinations.size() == 0) {
            return false;
        }

        int[] listOfPrices = new int[combinations.size()];
        String[] listOfRows = new String[combinations.size()];
        String combo;
        int positionOfDash;
        int callRow1;
        int callRow2;
        for (int i = 0; i < combinations.size(); i++) {
            combo = combinations.get(i);
            positionOfDash = combo.indexOf('-');
            callRow1 = Integer.parseInt(combo.substring(0, positionOfDash));
            callRow2 = Integer.parseInt(combo.substring(positionOfDash + 1, combo.length()));
            // If the rows are the same.
            if (callRow1 == callRow2) {
                listOfPrices[i] = Integer.parseInt(storage[callRow1][storage[callRow1].length - 1]);
                listOfRows[i] = String.format("%d", callRow1);
            } 
            // If the rows are different.
            else {
                listOfPrices[i] = Integer.parseInt(storage[callRow1][storage[callRow1].length - 1])
                        + Integer.parseInt(storage[callRow2][storage[callRow2].length - 1]);
                listOfRows[i] = String.format("%d,%d", callRow1, callRow2);
            }
        }

        minFinder(listOfPrices, listOfRows);
        deleteFromDataBase(this.codes);
        return true;
    }

     /*
      * algorithmToCreateOrderForElse is a method which applies an algorithm if the 
      * user provides an order for desks or filing. This method does not take in any arguments 
      * and returns a boolean based off whether or not it was successful in satisfying the order
      * Initially, the base condition is checked by calling upon loopMethod twice, once for each column 
      * in the desired desk/filing table, and storing them into three variables. Then, if the 
      * number of Ys in each column of all the rows for the selected furniture is less than the 
      * quantity desired, the complete order is not possible and thus this method returns false.
      * Then, if the total amount of Y's in each column is exactly equal to the 
      * ordered quantity, this method calls sumAllRows and then deleteAllRows to
      * delete all the rows from the database and then reutrns true.
      * If these base cases are not met, then the algorithm takes place. 
      * The algorithm utilizes an ArrayList and iterates over each column in the array
      * in nested for loops, to get all possible combinations to get Y in all three columns
      * (0, 1, and 2). Each combination is combined into a String seperated by a dash and
      * added to the ArrayList. Then, if no combinations were found, meaning the size of the
      * ArrayList == 0, then return false. Next, two arrays listOfPrices and listOfRows 
      * are created and a for loop iterates over all combinations in the ArrayList, adding
      * each price into the array and the rows corresponding to those prices are added to 
      * listOfRows as a String seperated by a comma. Then, this method calls 
      * minFinder with both arrays. Then, deleteFromDataBase is called to update the database
      * and then this method returns true.
      */
    private boolean algorithmToCreateOrderForElse() {
        ArrayList<String> combinations = new ArrayList<String>();
        int yChecker1 = loopMethod(0);
        int yChecker2 = loopMethod(1);
        int yChecker3 = loopMethod(2);

        if (yChecker1 < this.quantityStored || yChecker2 < this.quantityStored || yChecker3 < this.quantityStored) {
                return false;
            }

        if (yChecker1 == this.quantityStored && yChecker2 == this.quantityStored && yChecker3 == this.quantityStored) {
            sumAllRows(super.furnitureChosen,super.typeChosen);
            deleteAllRows(super.furnitureChosen, super.typeChosen);
            this.done = true;
            return true;
        }

        for (int i = 0; i < storage.length; i++) {
            if (!storage[i][0].equals("-1")) {
                for (int j = 0; j < storage.length; j++) {
                    if (!storage[j][1].equals("-1")) {
                        for (int k = 0; k < storage.length; k++) {
                            if (!storage[k][2].equals("-1")) {
                                combinations.add(i + "-" + j + "-" + k);
                            }
                        }
                    }
                }
            }
        }

        if (combinations.size() == 0) {
            return false;
        }

        int[] listOfPrices = new int[combinations.size()];
        String[] listOfRows = new String[combinations.size()];
        String combo;
        int positionOfDash;
        int positionOfDash2;
        int callRow1;
        int callRow2;
        int callRow3;
        for (int i = 0; i < combinations.size(); i++) {
            combo = combinations.get(i);
            positionOfDash = combo.indexOf('-');
            positionOfDash2 = combo.lastIndexOf('-');
            callRow1 = Integer.parseInt(combo.substring(0, positionOfDash));
            callRow2 = Integer.parseInt(combo.substring(positionOfDash + 1, positionOfDash2));
            callRow3 = Integer.parseInt(combo.substring(positionOfDash2 + 1, combo.length()));
            // If all three rows are the same.
            if (callRow1 == callRow3 && callRow1 == callRow2) {
                listOfPrices[i] = Integer.parseInt(storage[callRow1][storage[callRow1].length - 1]);
                listOfRows[i] = String.format("%d", callRow1);
            } 
            // If two rows are the same.
            else if (callRow1 == callRow2) {
                listOfPrices[i] = Integer.parseInt(storage[callRow1][storage[callRow1].length - 1])
                        + Integer.parseInt(storage[callRow3][storage[callRow3].length - 1]);
                listOfRows[i] = String.format("%d,%d", callRow1, callRow3);
            } else if (callRow1 == callRow3) {
                listOfPrices[i] = Integer.parseInt(storage[callRow1][storage[callRow1].length - 1])
                        + Integer.parseInt(storage[callRow2][storage[callRow2].length - 1]);
                listOfRows[i] = String.format("%d,%d", callRow1, callRow2);
            } else if (callRow2 == callRow3) {
                listOfPrices[i] = Integer.parseInt(storage[callRow2][storage[callRow2].length - 1])
                        + Integer.parseInt(storage[callRow1][storage[callRow1].length - 1]);
                listOfRows[i] = String.format("%d,%d", callRow1, callRow2);
            } 
            // If none of the rows are the same as the others.
            else {
                listOfPrices[i] = Integer.parseInt(storage[callRow1][storage[callRow1].length - 1])
                        + Integer.parseInt(storage[callRow2][storage[callRow2].length - 1])
                        + Integer.parseInt(storage[callRow3][storage[callRow3].length - 1]);
                listOfRows[i] = String.format("%d,%d,%d", callRow1, callRow2, callRow3);
            }
        }

        minFinder(listOfPrices, listOfRows);

        deleteFromDataBase(this.codes);

        return true;
    }

    /*
     * The following void method takes two arrays as parameters, an integer array named 
     * listOfPrices and a String array named listOfRows. These arrays are used in the 
     * algorithms to obtain the value and index of lowest prices. The listOfPrices array 
     * is used in the for loop to keep iterating over the length of the array, storing the 
     * price in each cell into an int called lowest if and only if it is less than the already
     * stored price. The corresponding index of the lowest price is also stored and updated conditionally
     * in an int called rowToAdd. Then, these values of the lowest price and the corresponding index are 
     * stored into the member fields rowToAdd and lowestPrice. Then, the obtained index is used to 
     * store the corresponding rows by using this rowToAdd variable as the index for the listOfRows array,
     * and finally, this String gets passed into getCodes.
     */
    private void minFinder(int [] listOfPrices, String [] listOfRows) {
        int lowest = listOfPrices[0];
        int rows = 0;

        for (int i = 0; i < listOfPrices.length; i++) {
            if (listOfPrices[i] < lowest) {
                lowest = listOfPrices[i];
                rows = i;
            }
        }

        setRowToAdd(rows);
        lowestPrice = lowest;

        lowestPriceCell = listOfRows[getRowToAdd()];
        getCodes(lowestPriceCell);
    }

    /**
     * The following method is used in conjuction with the algorithmToCreateOrderForChair(), 
     * algorithmToCreateOrderForLamp() and algorithmToCreateOrderForElse() methods.
     * The integer that this method takes as a parameter specifies the column that is mentioned.
     * The for loop that is used in this method iterates a variable over the number of 
     * rows of the 2D array, storage, and checks for anything except '-1' in the 2D array. 
     * Everytime it sees anything other than a '-1' in the specified column, over all the rows, 
     * it updates a counter called numOfY, which is the number of Y's in the column specified 
     * and finally returns numOfY.
     */
    protected int loopMethod(int col) {
        int numOfY = 0;
        for (int i = 0; i < storage.length; i++) {
            if (!storage[i][col].equals("-1")) {
                numOfY++;
            }
        }
        return numOfY;
    }

    /**
     * This overloaded constructor is to test the entire algorithm in JUNIT testing by passing 
     * in the inputs as Strings instead of command prompt inputs.
     */
    public DataBaseManipulator(String furniture, String type, int a, String url, String username, String password, boolean fullTest)
    {
        super(furniture, type, a);

        this.URL = url;
        this.USERNAME = username;
        this.PASSWORD = password;
        // Initialize the connection to the database.
        initializeConnection();

        // Get the quantity requested from InputReader.
        int numOfItemsRequested = super.quantity;

        // Create the 2D array by calling this method.
        create2DArray();

        boolean status = false;

        int priceStore = 0;

        // If "lamp" was selected as the furniture, keep running the algorithm
        // until either the whole order is fulfilled, or the algorithm is unable
        // to complete the full order. And on every run, add the lowest price to 
        // obtain the total for the order.
        if (super.furnitureChosen.equals("lamp")) {
            for (int i = 0; i < numOfItemsRequested; i++)
            {
                status = algorithmToCreateOrderForLamp();
                this.quantityStored--;
                if (status == false)
                  break;  
                if (this.done == true)
                {
                    priceStore += getLowestPrice();
                    break;
                }
                priceStore += getLowestPrice();
            }
        }

        setLowestPrice(priceStore);

        // If "chair" was selected as the furniture, keep running the algorithm
        // until either the whole order is fulfilled, or the algorithm is unable
        // to complete the full order. And on every run, add the lowest price to 
        // obtain the total for the order.
        if (super.furnitureChosen.equals("chair")) {
            for (int i = 0; i < numOfItemsRequested; i++)
            {
                status = algorithmToCreateOrderForChair();
                this.quantityStored--;
                if (status == false)
                  break;  
                if (this.done == true)
                {
                    priceStore += getLowestPrice();
                    break;
                }
                priceStore += getLowestPrice();
            }
        }

        setLowestPrice(priceStore);

        // If "desk" or "filing" (they both call upon the algorithmToCreateOrderForElse) 
        // was selected as the furniture, keep running the algorithm
        // until either the whole order is fulfilled, or the algorithm is unable
        // to complete the full order. And on every run, add the lowest price to 
        // obtain the total for the order.
        if (super.furnitureChosen.equals("desk") || super.furnitureChosen.equals("filing")) {
            for (int i = 0; i < numOfItemsRequested; i++)
            {
                status = algorithmToCreateOrderForElse();
                this.quantityStored--;
                if (status == false)
                  break;  
                if (this.done == true)
                {
                    priceStore += getLowestPrice();
                    break;
                }
                priceStore += getLowestPrice();
            }
        }

        setLowestPrice(priceStore);

        OrderForm(this.codes, this.furnitureChosen, this.typeChosen, this.quantity);

        // If the for loops above were unable, at any point, to create
        // the complete order.
        if (status == false)
        {
            // General print statements.
            System.out.println("\nUnable to create the order as there are not enough materials in stock to do so!");
            System.out.println("Here are a list of manufacturers that may have the needed items: \n");
            // If the furniture selected was a desk, print the appropriate manufacturers.
            if (super.furnitureChosen.equals("desk"))
            {
                for (int i = 0; i < MANUDESKS.length; i++)
                {
                    System.out.println(i+1 + ". " + MANUDESKS[i]);
                }
            }
            // If the furniture selected was a filing, print the appropriate manufacturers.
            if (super.furnitureChosen.equals("filing"))
            {
                for (int i = 0; i < MANUFILINGS.length; i++)
                {
                    System.out.println(i+1 + ". " + MANUFILINGS[i]);
                }
            }
            // If the furniture selected was a chair, print the appropriate manufacturers.
            if (super.furnitureChosen.equals("chair"))
            {
                for (int i = 0; i < MANUCHAIRS.length; i++)
                {
                    System.out.println(i+1 + ". " + MANUCHAIRS[i]);
                }
            }
            // If the furniture selected was a lamp, print the appropriate manufacturers.
            if (super.furnitureChosen.equals("lamp"))
            {
                for (int i = 0; i < MANULAMP.length; i++)
                {
                    System.out.println(i+i + ". " + MANULAMP[i]);
                }
            }
            try
            {
                resetOrderForm();
            }
            catch (Exception e)
            {
                System.out.println("Something went wrong!");
                System.exit(1);
            }
            // Terminate the program.
            System.exit(1);
        }
    }

    /** 
     * This overloaded constructor just sets the values of the member fields, used in JUNIT
     * tests for things other than the algorithm.
     */
   public DataBaseManipulator(String furniture, String type,int quantity, String url, String username, String password)
   {
       super(furniture, type, quantity);
       this.furnitureChosen = furniture;
       this.typeChosen = type;
       this.URL = url;
       this.USERNAME = username;
       this.PASSWORD = password;
       this.quantity = quantity;
       setQuantityStored(quantity);
   }

   /** 
    * Getter for URL
    */
   protected String getURL()
   {
       return this.URL;
   }
   /** 
    * Getter for USERNAME
    */
   protected String getUSERNAME()
   {
       return this.USERNAME;
   }
   /** 
    * Getter for PASSWORD
    */
   protected String getPASSWORD()
   {
       return this.PASSWORD;
   }
   /** 
    * Getter for dataBaseConnection
    */
   protected Connection getDataBaseConnection()
   {
       return this.dataBaseConnection;
   }
   /** 
    * Getter for lowestPrice
    */
   protected int getLowestPrice()
   {
       return this.lowestPrice;
   }
   /** 
    * Getter for rowsAffected
    */
   protected int getRowsAffected()
   {
       return this.rowsAffected;
   }
   /** 
    * Getter for rowToAdd
    */
   protected int getRowToAdd()
   {
       return this.rowToAdd;
   }
   /** 
    * Getter for quantityStored
    */
   protected int getQuantityStored()
   {
       return this.quantityStored;
   }
   /** 
    * Setter for lowestPrice
    */
   protected void setLowestPrice(int lowestPriceToAdd)
   {
        this.lowestPrice = lowestPriceToAdd;
   }
   /** 
    * Setter for rowToAdd
    */
   protected void setRowToAdd(int row)
   {
       this.rowToAdd = row;
   }
   /** 
    * Setter for quantityStored
    */
   protected void setQuantityStored(int store)
   {
       this.quantityStored = store;
   }
}