package org.example.database;

import java.sql.*;

public class DBConnection {
    private static Connection connection = null;
    /**
     * This method establishes the connection to a MySQL database.
     *
     * @return an object of type connection
     */
    public static Connection getConnection() {
        if(connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/annotation", "root", "Universitate!1");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

}

