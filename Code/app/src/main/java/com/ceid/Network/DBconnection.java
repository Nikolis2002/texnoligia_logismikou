package com.ceid.Network;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBconnection {
    private static final String URL="jdbc:mysql://localhost:3306/app_database";

    public static Connection getCon(String username,String password) throws SQLException{
        return DriverManager.getConnection(URL,username,password);
    }

    public static void closeCon(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
    }
}
