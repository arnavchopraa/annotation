package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        // url is of the form: "jdbc:subprotocolName:subnameURL" last name is for the schema of the db
        String dbURL = "jdbc:mysql://localhost:3306/annotation";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(dbURL, "root", "Universitate!1");
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select * from annotations");

            while(resultSet.next()) {
                System.out.println(resultSet.getString("codeContent"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}