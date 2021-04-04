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

        for (int l = 0; l < storage.length; l++) {
            for (int m = 0; m < storage[l].length; m++) {
                System.out.print(storage[l][m] + " ");
            }
            System.out.println();
        }

    }

    private void initializeConnection() {
        try {
            this.dataBaseConnection = DriverManager.getConnection(this.URL, this.USERNAME, this.PASSWORD);
        } catch (SQLException e) {
            System.out.println("Could not connect to the database!");
            System.exit(1);
        }
    }

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

    private boolean algorithmToCreateOrderForLamp() {
        int yChecker1 = loopMethod(0);
        int yChecker2 = loopMethod(1);

        if (yChecker1 < this.quantityStored || yChecker2 < this.quantityStored) {
                return false;
            }
        if (yChecker1 == super.quantity && yChecker2 == super.quantity) {
            sumAllRows();
            //deleteAllRows("lamp", super.typeChosen);
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

        String temp = combinations.get(this.rowToAdd);
        positionOfDash = temp.indexOf('-');
        callRow1 = Integer.parseInt(temp.substring(0, positionOfDash));
        callRow2 = Integer.parseInt(temp.substring(positionOfDash + 1, temp.length()));

        deleteFromDataBase();

        return true;
    }

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
