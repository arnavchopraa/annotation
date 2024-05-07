package org.example;

import org.example.database.DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        testFunctionality();
    }

    /**
     * This method tests the retrieval of the annotations from the MySQL database.
     */
    public static void testFunctionality() {
        Connection connection = DBConnection.startConnection("jdbc:mysql://localhost:3306/annotation");
        StringBuilder sb = new StringBuilder();
        sb.append("select codeContent from annotations where id = ");
        Scanner scanner = new Scanner(System.in);
        sb.append("'");
        sb.append(scanner.nextLine());
        sb.append("'");
        System.out.println(DBConnection.queryExecution(sb.toString(), connection));
    }
}