package org.example.services;

import org.example.database.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class QueryService {
    /**
     * This method queries the database for a code associated to a piece of text
     * and returns the text.
     *
     * @param key
     * @return the text associated with the query
     */
    public String queryResults(String key) {
        StringBuilder sb = new StringBuilder();
        sb.append("select codeContent from annotations where id = ");
        sb.append("'");
        sb.append(key);
        sb.append("'");
        List<String> results = queryExecution(sb.toString());
        if(results.size() == 0)
            return key;
        return results.get(0);
    }

    /**
     * This method executes a query on a database.
     *
     * @param query the query to be executed
     * @return a list of results in the form of strings
     */
    public static List<String> queryExecution(String query) {
        try {
            Statement statement = DBConnection.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            List<String> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(resultSet.getString(1));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
