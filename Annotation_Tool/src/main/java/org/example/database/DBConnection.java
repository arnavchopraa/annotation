package org.example.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {
    /**
     * This method establishes the connection to a MySQL database.
     *
     * @param URL the URL of the database to connect to
     * @return an object of type connection
     */
    public static Connection startConnection(String URL) {
        // url is of the form: "jdbc:subprotocolName:subnameURL" last name is for the schema of the db
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, "root", "Universitate!1");
        } catch (Exception e) {
            System.out.println(e);
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

