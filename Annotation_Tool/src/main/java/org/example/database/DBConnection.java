package org.example.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {
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

