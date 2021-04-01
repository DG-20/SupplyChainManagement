package edu.ucalgary.ensf409;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

public class DataBaseManipulator extends InputReader {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost/inventory";
        String username = "scm";
        String password = "ensf409";
        DataBaseManipulator obj2 = new DataBaseManipulator(url, username, password);
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

    public DataBaseManipulator(String url, String username, String password) {
        this.URL = url;
        this.USERNAME = username;
        this.PASSWORD = password;
        System.out.println(this.URL + this.USERNAME + this.PASSWORD);
        initializeConnection();

        create2DArray();

        algorithmToCreateOrderForLamp();
        algorithmToCreateOrderForElse();

        
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

    private boolean algorithmToCreateOrderForLamp() {
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

        int [] listOfPrices = new int [combinations.size()];
        String combo;
        int positionOfDash;
        int row1;
        int row2;
        for (int i = 0; i < combinations.size(); i++) {
            combo = combinations.get(i);
            positionOfDash = combo.indexOf('-');
            row1 = Integer.parseInt(combo.substring(0, positionOfDash));
            row2 = Integer.parseInt(combo.substring(positionOfDash + 1, combo.length()));
            listOfPrices[i] = storage[row1][storage[row1].length - 1] + storage[row2][storage[row2].length - 1];
        }
        

        return true;
    }

    private boolean algorithmToCreateOrderForElse() {
        ArrayList<String> combinations = new ArrayList<String>();
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

        Iterator<String> obj = combinations.iterator();
        while (obj.hasNext())
            System.out.println(obj.next());
        return true;
    }



}
