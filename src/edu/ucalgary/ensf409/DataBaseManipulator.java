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
    protected String[][] storage;
    private int rowToAdd;
    protected int lowestPrice;
    private String lowestPriceCell;
    protected String[] codes;
    private int codeCellCounter = 0;

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
        System.out.println(this.lowestPrice);
    }

    private void sumAllRows() {

    }

    private void deleteAllRows(String furnitureName, String type) {
        PreparedStatement myStmt;
        try {
            String query = "DELETE FROM " + furnitureName + " WHERE Type = ?";
            myStmt = dataBaseConnection.prepareStatement(query);

            myStmt.setString(1, type);

            int rowCount = myStmt.executeUpdate();
            System.out.println("Rows Affected: " + rowCount);
        }

        catch (SQLException e) {
            System.out.println("Unable to delete all rows of type " + type);
            System.exit(1);
        }
    }

    private void deleteFromDataBase(int callRow1, int callRow2, int callRow3, int callRow4) {
        try {
            String query = "DELETE FROM inventory WHERE " + furnitureChosen + " = ?";
            PreparedStatement myStmt = dataBaseConnection.prepareStatement(query);

            if (callRow1 != -1) {
                myStmt.setInt(1, callRow1);
            }

            if (callRow2 != -1) {
                myStmt.setInt(1, callRow2);
            }

            if (callRow3 != -1) {
                myStmt.setInt(1, callRow3);
            }

            if (callRow4 != -1) {
                myStmt.setInt(1, callRow4);
            }
        } catch (SQLException e) {

        }
    }

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

        if (yChecker1 == super.quantity && yChecker2 == super.quantity && yChecker3 == super.quantity
                && yChecker4 == super.quantity) {
            sumAllRows();
            //deleteAllRows("chair", super.typeChosen);
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
                    listOfRows[i] = String.format("%,%d,%d", callRow1, callRow3, callRow4);
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

        return true;

    }

    private void getCodes(String lowestPriceCell) {
        System.out.println("lowestPriceCell is: " + lowestPriceCell);
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
                System.out.println("adding : " + Integer.parseInt(concatenator));
                rowCells[cell] = Integer.parseInt(concatenator);
                cell++;
                concatenator = "";
            }
            //System.out.println("cncatenot : " + concatenator);
        }

        System.out.println("rowCells is: ");
        for(int i = 0; i < rowCells.length; i++)
        {
            System.out.print("| " + rowCells[i] + " ");
        }
        System.out.println();

        int cellInsert = 0;
        for (int i = 0; i < rowCells.length; i++) {
            // System.out.println("GETS HERE " + i);
            for (int j = 0; j < storage[rowCells[i]].length; j++) {
                // System.out.println("GETS HERE Loop 2: " + j);
                char[] charArray2 = storage[rowCells[i]][j].toCharArray();
                // System.out.println(storage[rowCells[i] - 1][j]);
                if (Character.isLetter(charArray2[0])) {
                    //System.out.println("cellinsert is: " + cellInsert);
                    codeInserts[cellInsert] = storage[rowCells[i]][j];
                    cellInsert++;
                    break;
                }
            }
        }

        codes = new String[charArray.length - commas];
        for (int goThrough = 0; goThrough < codes.length; goThrough++) {
            codes[goThrough] = codeInserts[goThrough];
        }

    }

    private boolean algorithmToCreateOrderForLamp() {
        int yChecker1 = loopMethod(0);
        int yChecker2 = loopMethod(1);

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

        for(int i = 0; i < listOfRows.length; i++)
        {
            System.out.print("| " + listOfRows[i] + " ");
        }
        System.out.println();

        minFinder(listOfPrices, listOfRows);

        String temp = combinations.get(this.rowToAdd);
        positionOfDash = temp.indexOf('-');
        callRow1 = Integer.parseInt(temp.substring(0, positionOfDash));
        callRow2 = Integer.parseInt(temp.substring(positionOfDash + 1, temp.length()));

        // deleteFromDataBase(callRow1, callRow2, -1, -1);

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
            //deleteAllRows(super.furnitureChosen, super.typeChosen);
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

        if (combinations.size() == 0) {
            return false;
        }
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
