package edu.ucalgary.ensf409;

import java.sql.*;

public class DataBaseManipulator {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost/inventory";
        String username = "scm";
        String password = "ensf409";
        DataBaseManipulator obj = new DataBaseManipulator(url, username, password);
    }

    private final String URL;
    private final String USERNAME;
    private final String PASSWORD;
    private Connection dataBaseConnection;
    private String[] manuLamp = {"Office Furnishings", "Furniture Goods", "Fine Office Supplies"}

    public DataBaseManipulator(String url, String username, String password) {
        this.URL = url;
        this.USERNAME = username;
        this.PASSWORD = password;
        System.out.println(this.URL + this.USERNAME + this.PASSWORD);
        initializeConnection();
    }

    private void initializeConnection() {
        try {
            this.dataBaseConnection = DriverManager.getConnection(this.URL, this.USERNAME, this.PASSWORD);
        } catch (SQLException e) {
            System.out.println("Could not connect to the database!");
            System.exit(1);
        }
    }

}
