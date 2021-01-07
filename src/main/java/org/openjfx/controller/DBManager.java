package org.openjfx.controller;

import java.sql.*;

public class DBManager {

    private static Connection connection = getConnection();

    /**
     * Constructor
     */
    public DBManager(){

    }

    /**
     * Creates a connection to the database file.
     * @return
     */
    public static Connection getConnection(){

        try{
            return DriverManager.getConnection("jdbc:sqlite:src/main/database/app.db");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
