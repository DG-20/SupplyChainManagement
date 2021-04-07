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

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class DataBaseManipulator extends InputReader {
    public static void main(String[] args) throws IOException {
         String url = "jdbc:mysql://localhost/inventory"; String username = "scm";
         String password = "ensf409"; DataBaseManipulator obj2 = new
         DataBaseManipulator(url, username, password);
         obj2.sumAllRows();
    }

    private final String URL;
    private final String USERNAME;
    private final String PASSWORD;
    private Connection dataBaseConnection;
    private String[] manuChairs = { "Office Furnishings", "Chairs R Us", "Furniture Goods", "Fine Office Supplies" };
    private String[] manuDesks = { "Academic Desks", "Office Furnsishings, Furniture Goods", "Fine Office Supplies" };
    private String[] manuFilings = { "Office Furnishings", "Furniture Goods", "Fine Office Supplies" };
    private String[] manuLamp = { "Office Furnishings", "Furniture Goods", "Fine Office Supplies" };
    protected String[][] storage;
    private int rowToAdd;
    protected int lowestPrice;
    private String lowestPriceCell;
    protected ArrayList<String> codes= new ArrayList<String>(); //USE ARRAYLIST
    private int quantityStored = super.quantity;

    public DataBaseManipulator(String url, String username, String password) {
        this.URL = url;
        this.USERNAME = username;
        this.PASSWORD = password;
        initializeConnection();

        int numOfItemsRequested = super.quantity;

        create2DArray();

        boolean status = false;

        int priceStore = 0;

        if (super.furnitureChosen.equals("lamp")) {
            for (int i = 0; i < numOfItemsRequested; i++)
            {
                status = algorithmToCreateOrderForLamp();
                this.quantityStored--;
                System.out.println(status);
                if (status == false)
                  break;  
                priceStore += this.lowestPrice;
            }
        }

        this.lowestPrice = priceStore;

        priceStore = 0;

        if (super.furnitureChosen.equals("chair")) {
            for (int i = 0; i < numOfItemsRequested; i++)
            {
                status = algorithmToCreateOrderForChair();
                this.quantityStored--;
                System.out.println(status);
                if (status == false)
                  break;  
                priceStore += this.lowestPrice;
            }
        }

        this.lowestPrice = priceStore;

        priceStore = 0;

        if (super.furnitureChosen.equals("desk") || super.furnitureChosen.equals("filing")) {
            for (int i = 0; i < numOfItemsRequested; i++)
            {
                status = algorithmToCreateOrderForElse();
                this.quantityStored--;
                System.out.println(status);
                if (status == false)
                  break;  
                priceStore += this.lowestPrice;
            }
        }

        this.lowestPrice = priceStore;

        System.out.println(this.lowestPrice);

        print2DArray();

        if (status == false)
        {
            System.out.println("\nUnable to create the order as there are not enough materials in stock to do so!");
            System.out.println("Here are a list of manufacturers that may have the needed items: \n");
            if (super.furnitureChosen.equals("desk"))
            {
                for (int i = 0; i < manuDesks.length; i++)
                {
                    System.out.println(i+1 + ". " + manuDesks[i]);
                }
            }
            if (super.furnitureChosen.equals("filing"))
            {
                for (int i = 0; i < manuFilings.length; i++)
                {
                    System.out.println(i+1 + ". " + manuFilings[i]);
                }
            }
            if (super.furnitureChosen.equals("chair"))
            {
                for (int i = 0; i < manuChairs.length; i++)
                {
                    System.out.println(i+1 + ". " + manuChairs[i]);
                }
            }
            if (super.furnitureChosen.equals("lamp"))
            {
                for (int i = 0; i < manuLamp.length; i++)
                {
                    System.out.println(i+i + ". " + manuLamp[i]);
                }
            }
            System.exit(1);
        }
    }

    private void print2DArray()
    {
        for(int i = 0; i < storage.length; i++)
        {
            for(int j = 0; j < storage[i].length; j++)
            {
                System.out.print(storage[i][j] + " ");
            }
            System.out.println();
        }
    }

    /*
     * sumAllRows is a method that total's up all of the sums of a specified furniture object and its specific sub type.
     * It makes a statement called newstmt which is used to execute a select from statement. The result of the executed
     * statement is then stored and the string contained under the "Type" column is compared to the desired type. If  
     * they match then the "Price" column of that particular result is taken in and added onto the price.
    */
    private void sumAllRows() 
    {
        Statement newstmt;
        lowestPrice = 0;
        try
        {
            newstmt = this.dataBaseConnection.createStatement();
            ResultSet results = newstmt.executeQuery("SELECT * FROM " + super.furnitureChosen); 
            while(results.next())
            {
                 if((results.getString("Type").equals(super.typeChosen)))
                {
                    
                    lowestPrice += Integer.parseInt(results.getString("Price"));
                }
            }

        }

        catch(SQLException e)
        {
            System.out.println("Unable to calculate sum of all rows");
        }
        
    }

    /*
     * deleteAllRows is a method that deletes all the rows that match the specified furniture type.
     * A query is created that holds the instruction to delete from a specified furniture name where
     * the desired type to delete is stored. 
    */
    private void deleteAllRows(String furnitureName, String type) {
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

    /*
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
    private void deleteFromDataBase() {
        PreparedStatement myStmt;
        try {
            for (int i = 0; i < this.codes.size(); i++)
            {
                String query = "DELETE FROM " + this.furnitureChosen + " WHERE ID = ?";
                myStmt = this.dataBaseConnection.prepareStatement(query);
                myStmt.setString(1, codes.get(i));
                myStmt.executeUpdate();
                System.out.println(codes.get(i));
            }
        } catch (SQLException e) {
            System.out.println("Unable to delete from the database! Something went wrong :(");
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

    /*
     * initializeConnection is a method that creates a connection from the code to the database.
     * dataBaseConnection stores the connection between the sql database and the code by taking 
     * in a url, username, and password for the host connection storing the database.
    */
    private void initializeConnection() {
        try {
            this.dataBaseConnection = DriverManager.getConnection(this.URL, this.USERNAME, this.PASSWORD);
        } catch (SQLException e) {
            System.out.println("Could not connect to the database!");
            System.exit(1);
        }
    }

    /*
     * algorithmToCreateOrderForChair is a method that performs the algorithm on chairs.
     * The method performs 4 loops on the database to check for number of Yes occurences in the table
     * and then stores the amount of occurences in an int. It then compares each check through the loop's
     * occurences. Checking to see if the number of Y's allows for an order of a particular quantity. 
     * The nested for loop's check each cell for a yes or no storing each combination of allowed occurences in i,j,k,l.
     * After storing the combinations, an array for the listOfPrices of type int and an array for the listOfRows of type String
     * are created. The array listOfPrices 
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
            sumAllRows();
            deleteAllRows("chair", super.typeChosen);
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
            if (callRow1 == callRow3 && callRow1 == callRow2 && callRow1 == callRow4) {
                listOfPrices[i] = Integer.parseInt(storage[callRow1][storage[callRow1].length - 1]);
                listOfRows[i] = String.format("%d,%d", callRow1);
            } else if (callRow1 == callRow2 && callRow1 == callRow3) {
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
            // pairs
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
                    listOfRows[i] = String.format("%,%d,%d", callRow1, callRow2, callRow3);
                } else {
                    listOfPrices[i] = Integer.parseInt(storage[callRow3][storage[callRow3].length - 1])
                            + Integer.parseInt(storage[callRow1][storage[callRow1].length - 1]);
                    listOfRows[i] = String.format("%d,%d", callRow1, callRow3);

                }
            }
            // singles

            else {
                listOfPrices[i] = Integer.parseInt(storage[callRow1][storage[callRow1].length - 1])
                        + Integer.parseInt(storage[callRow2][storage[callRow2].length - 1])
                        + Integer.parseInt(storage[callRow3][storage[callRow3].length - 1])
                        + Integer.parseInt(storage[callRow4][storage[callRow4].length - 1]);
                listOfRows[i] = String.format("%d,%d,%d,%d", callRow1, callRow2, callRow3, callRow4);
            }
        }

        minFinder(listOfPrices, listOfRows);

        deleteFromDataBase();
        return true;

    }

    /* getCodes is a private method in the DataBaseManipulator java file. It takes in a 
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
        for (int c = 0; c < charArray.length; c++)       //char charachter : charArray 
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

        //codes = new String[charArray.length - commas];
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
     * user provides an order for lamps. Initially, the base condition is checked
     * by calling upon loopMethod twice, once for each column in the Lamp table,
     * and storing them into two variables. Then, if the number of Ys in each column
     * of all the rows for the selected furniture is less than the quantity desired,
     * the complete order is not possible and thus this method returns false.
     * Then, if the total amount of Y's in each column is exactly equal to the 
     * ordered quantity, this method calls sumAllRows and then deleteAllRows to
     * delete all the rows from the database and then reutrns true.
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
            sumAllRows();
            deleteAllRows("lamp", super.typeChosen);
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
            if (callRow1 == callRow2) {
                listOfPrices[i] = Integer.parseInt(storage[callRow1][storage[callRow1].length - 1]);
                listOfRows[i] = String.format("%d", callRow1);
            } else {
                listOfPrices[i] = Integer.parseInt(storage[callRow1][storage[callRow1].length - 1])
                        + Integer.parseInt(storage[callRow2][storage[callRow2].length - 1]);
                listOfRows[i] = String.format("%d,%d", callRow1, callRow2);
            }
        }

        minFinder(listOfPrices, listOfRows);
        deleteFromDataBase();
        return true;
    }

     /*
     * algorithmToCreateOrderForElse is a method which applies an algorithm if the 
     * user provides an order for desks or filing. This method does not take in any arguments 
     * and returns a boolean based off whether or not it was successful in satisfying the order
     * Initially, the base condition is checked by calling upon loopMethod twice, once for each column 
     * in the desired desk/filing table, and storing them into two variables. Then, if the number of Ys in each column
     * of all the rows for the selected furniture is less than the quantity desired,
     * the complete order is not possible and thus this method returns false.
     * Then, if the total amount of Y's in each column is exactly equal to the 
     * ordered quantity, this method calls sumAllRows and then deleteAllRows to
     * delete all the rows from the database and then reutrns true.
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
    private boolean algorithmToCreateOrderForElse() {
        ArrayList<String> combinations = new ArrayList<String>();
        int yChecker1 = loopMethod(0);
        int yChecker2 = loopMethod(1);
        int yChecker3 = loopMethod(2);

        if (yChecker1 < this.quantityStored || yChecker2 < this.quantityStored || yChecker3 < this.quantityStored) {
                return false;
            }

        if (yChecker1 == this.quantityStored && yChecker2 == this.quantityStored && yChecker3 == this.quantityStored) {
            sumAllRows();
            deleteAllRows(super.furnitureChosen, super.typeChosen);
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
            if (callRow1 == callRow3 && callRow1 == callRow2) {
                listOfPrices[i] = Integer.parseInt(storage[callRow1][storage[callRow1].length - 1]);
                listOfRows[i] = String.format("%d", callRow1);
            } else if (callRow1 == callRow2) {
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
            } else {
                listOfPrices[i] = Integer.parseInt(storage[callRow1][storage[callRow1].length - 1])
                        + Integer.parseInt(storage[callRow2][storage[callRow2].length - 1])
                        + Integer.parseInt(storage[callRow3][storage[callRow3].length - 1]);
                listOfRows[i] = String.format("%d,%d,%d", callRow1, callRow2, callRow3);
            }
        }

        minFinder(listOfPrices, listOfRows);

        deleteFromDataBase();

        return true;
    }

    /*
     *The following void method takes two arrays as parameters, an integer array named listOfPrices and a String array named listOfRows.
     *These arrays are used in the algorithms to develop value and index of lowest prices. The listOfPrices method is used in the for loop to keep iterating, as
     *long as its length is less than the total length of array, and its value is less than the variable called lowest, after which its values gets updated into it.
     *The listOfRows array is utilized for the row index that corresponds to lowest price. This method also calls getCodes, which takes
     *lowestPriceCell:this finds the lowest price of a chosen cell, which leads to the next part of the implementation, taking place in the getCodes method.
     */

    private void minFinder(int[] listOfPrices, String[] listOfRows) {
        int lowest = listOfPrices[0];
        int rowToAdd = 0;

        for (int i = 0; i < listOfPrices.length; i++) {
            if (listOfPrices[i] < lowest) {
                lowest = listOfPrices[i];
                rowToAdd = i;
            }
        }

        this.rowToAdd = rowToAdd;
        this.lowestPrice = lowest;

        lowestPriceCell = listOfRows[this.rowToAdd];
        for (int i = 0; i < listOfRows.length; i++)
            System.out.println(listOfRows[rowToAdd]);
        

        getCodes(lowestPriceCell);
    }

    /*
     *The following method is used in conjuction with the algorithmToCreateOrderForChair(), algorithmToCreateOrderForLamp() and 
     *algorithmToCreateOrderForElse() methods. The integer that this method takes as a parameter specifies the column that is mentioned.
     *The for loop that is used in this method iterates a variable over the length of every row in the database, and checks for a '-1' in 
     *the 2D array called storage: it then updates numofY, which is the number of Y's in the column specified and finally returns numOfY.
     */
    
    private int loopMethod(int col) {
        int numOfY = 0;
        for (int i = 0; i < storage.length; i++) {
            if (!storage[i][col].equals("-1")) {
                numOfY++;
            }
        }
        return numOfY;
    }
}
