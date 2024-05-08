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
    public static Connection getConnection() {
        if(connection == null) {
            System.out.println("con null");
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/annotation", "root", "Universitate!1");
            } catch (Exception e) {
                e.printStackTrace();
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
            System.out.println(connection);
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

