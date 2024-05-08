package org.example.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {
    private static Connection connection = null;
    /**
     * This method establishes the connection to a MySQL database.
     *
     * @return an object of type connection
     */
    private DBConnection() {
        try {
            connection = DriverManager.getConnection(, "root", "Universitate!1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        if (connection == null) {
            synchronized (DBConnection.class) {
                if (connection == null) {
                    new DBConnection();
                }
            }
        }
        return connection;
    }

    /**
     * This method executes a query on a database.
     *
     * @param query the query to be executed
     * @param connection the connection to a database
     * @return a list of results in the form of strings
     */
    public static List<String> queryExecution(String query, Connection connection) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            List<String> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(resultSet.getString(1));
            }
            return result;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}

