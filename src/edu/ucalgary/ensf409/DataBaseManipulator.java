package edu.ucalgary.ensf409;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

public class DataBaseManipulator extends InputReader {
    public static void main(String[] args) throws IOException {
        /*
         * String url = "jdbc:mysql://localhost/inventory"; String username = "scm";
         * String password = "ensf409"; DataBaseManipulator obj2 = new
         * DataBaseManipulator(url, username, password);
         */
    }

    private final String URL;
    private final String USERNAME;
    private final String PASSWORD;
    private Connection dataBaseConnection;
    private String[] manuChairs = { "Office Furnishings", "Chairs R Us", "Furniture Goods", "Fine Office Supplies" };
    private String[] manuDesks = { "Academic Desks", "Office Furnsishings, Furniture Goods", "Fine Office Supplies" };
    private String[] manuFilings = { "Office Furnishings", "Furniture Goods", "Fine Office Supplies" };
    private String[] manuLamp = { "Office Furnishings", "Furniture Goods", "Fine Office Supplies" };
    private int[][] storage;
    private int rowToAdd;
    private int lowestPrice;

    public DataBaseManipulator(String url, String username, String password) {
        this.URL = url;
        this.USERNAME = username;
        this.PASSWORD = password;
        System.out.println(this.URL + this.USERNAME + this.PASSWORD);
        initializeConnection();

        create2DArray();

        if (super.furnitureChosen.equals("lamp")) {
            algorithmToCreateOrderForLamp();
        }

        if (super.furnitureChosen.equals("chair")) {
            algorithmToCreateOrderForChair();
        }

        if (super.furnitureChosen.equals("desk") || super.furnitureChosen.equals("filing")) {
            algorithmToCreateOrderForElse();
        }
    }

    private void sumAllRows()
    {
        
    }
    private void deleteAllRows(String furnitureName, String type)
    {
        PreparedStatement myStmt;
        try
        {
            String query = "DELETE FROM " + furnitureName + " WHERE Type = ?";
            myStmt = dataBaseConnection.prepareStatement(query);

            myStmt.setString(1, type);

            int rowCount = myStmt.executeUpdate();
            System.out.println("Rows Affected: " + rowCount);
        }

        catch(SQLException e)
        {
            System.out.println("Unable to delete all rows of type " + type);
            System.exit(1);
        }
    }
    /*private void deleteFromDataBase(int row1, int row2, int row3, int row4)
    {
        try
        {
            String query = "DELETE FROM inventory WHERE " + furnitureChosen + " = ?";
            PreparedStatement myStmt = dataBaseConnection.prepareStatement(query);

            if(row1 != -1)
            {
                myStmt.setInt(1, row1);
            }
            
            if(row2 != -1)
            {
                myStmt.setInt(1, row2);
            }

            if(row3 != -1)
            {
                myStmt.setInt(1, row3);
            }

            if(row4 != -1)
            {
                myStmt.setInt(1, row4);
            }
        }
        catch(SQLException e)
        {

        }
    }*/

    private void create2DArray() {
        int numOfCols = 0;
        String[] furnitureParts;

        if (super.furnitureChosen.equals("chair")) {
            System.out.println("Chair");
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

        storage = new int[numOfRows][numOfCols];

        try {
            int i = 0;
            newstmt = this.dataBaseConnection.createStatement();
            ResultSet results = newstmt.executeQuery("SELECT * FROM " + super.furnitureChosen);
            while (results.next()) {
                if (results.getString("Type").equals(super.typeChosen)) {
                    for (int j = 0; j < furnitureParts.length; j++) {

                        if (results.getString(furnitureParts[j]).equals("Y") && j < furnitureParts.length - 1
                                && results.getString("Type").equals(super.typeChosen)) {
                            storage[i][j] = 1;
                        } else if (j == furnitureParts.length - 1
                                && results.getString("Type").equals(super.typeChosen)) {
                            storage[i][j] = Integer.parseInt(results.getString(furnitureParts[j]));
                        } else {
                            if (results.getString("Type").equals(super.typeChosen)) {
                                storage[i][j] = 0;
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

        if (yChecker1 == super.quantity && yChecker2 == super.quantity && yChecker3 == super.quantity
                && yChecker4 == super.quantity) {
            sumAllRows();
            deleteAllRows("chair", super.typeChosen);
            return true;
        }

        for (int i = 0; i < storage.length; i++) {
            if (storage[i][0] == 1) {
                for (int j = 0; j < storage.length; j++) {
                    if (storage[j][1] == 1) {
                        for (int k = 0; k < storage.length; k++) {
                            if (storage[k][2] == 1) {
                                for (int l = 0; l < storage.length; l++) {
                                    if (storage[l][3] == 1) {
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
        return true;
    }

    private boolean algorithmToCreateOrderForLamp() {
        int yChecker1 = loopMethod(0);
        int yChecker2 = loopMethod(1);

        if (yChecker1 == super.quantity && yChecker2 == super.quantity) {
            sumAllRows();
            deleteAllRows("lamp", super.typeChosen);
        }

        ArrayList<String> combinations = new ArrayList<String>();
        for (int i = 0; i < storage.length; i++) {
            if (storage[i][0] == 1) {
                for (int j = 0; j < storage.length; j++) {
                    if (storage[j][1] == 1) {
                        combinations.add(i + "-" + j);
                    }
                }
            }
        }

        int[] listOfPrices = new int[combinations.size()];
        String combo;
        int positionOfDash;
        int row1;
        int row2;
        for (int i = 0; i < combinations.size(); i++) {
            combo = combinations.get(i);
            positionOfDash = combo.indexOf('-');
            row1 = Integer.parseInt(combo.substring(0, positionOfDash));
            row2 = Integer.parseInt(combo.substring(positionOfDash + 1, combo.length()));
            if (row1 == row2) {
                listOfPrices[i] = storage[row1][storage[row1].length - 1];
            } else {
                listOfPrices[i] = storage[row1][storage[row1].length - 1] + storage[row2][storage[row2].length - 1];
            }
        }

        minFinder(listOfPrices);

        String temp = combinations.get(this.rowToAdd);
        positionOfDash = temp.indexOf('-');
        row1 = Integer.parseInt(temp.substring(0, positionOfDash));
        row2 = Integer.parseInt(temp.substring(positionOfDash + 1, temp.length()));

        //deleteFromDatabase(row1, row2, -1, -1);

        if (combinations.size() == 0) {
            return false;
        }
        return true;
    }

    private boolean algorithmToCreateOrderForElse() {
        ArrayList<String> combinations = new ArrayList<String>();
        int yChecker1 = loopMethod(0);
        int yChecker2 = loopMethod(1);
        int yChecker3 = loopMethod(2);

        if (yChecker1 == super.quantity && yChecker2 == super.quantity && yChecker3 == super.quantity) {
            sumAllRows();
            deleteAllRows(super.furnitureChosen, super.typeChosen);
            return true;
        }

        for (int i = 0; i < storage.length; i++) {
            if (storage[i][0] == 1) {
                for (int j = 0; j < storage.length; j++) {
                    if (storage[j][1] == 1) {
                        for (int k = 0; k < storage.length; k++) {
                            if (storage[k][2] == 1) {
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
        return true;
    }

    private void minFinder(int[] listOfPrices) {
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
    }

    private int loopMethod(int col) {
        int numOfY = 0;
        for (int i = 0; i < storage.length; i++) {
            if (storage[i][col] == 1) {
                numOfY++;
            }
        }
        return numOfY;
    }
}
