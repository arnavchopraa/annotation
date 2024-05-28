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
                connection = DriverManager.getConnection("jdbc:mysql://avnadmin:AVNS_YcLJdqH1K2QB6aSxIsN@mysql-425b8ae-pdf-parser.i.aivencloud.com:24979/defaultdb?ssl-mode=REQUIRED", "avnadmin", "AVNS_YcLJdqH1K2QB6aSxIsN");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

}

