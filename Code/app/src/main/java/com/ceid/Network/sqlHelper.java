package com.ceid.Network;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class sqlHelper {

    private static final String username = "root";
    private static final String password = "Nikolis2002\"";

    public static ArrayList<Object[]> execute_query(String query, Object... parameters) {
        ArrayList<Object[]> result_list = null;
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        try {
            con = DBconnection.getCon(username, password);
            statement = con.prepareStatement(query);

            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i] == null) {
                    statement.setObject(i + 1, null);
                } else {
                    statement.setObject(i + 1, parameters[i]);
                }
            }

            result = statement.executeQuery();

            result_list = result_to_list(result);

        } catch (SQLException e) {
            e.fillInStackTrace();
        } finally {
            DBconnection.closeCon(con, statement, result);
        }
        return result_list;
    }

    public static ArrayList<Object[]> execute_function(String function, Object... parameters) {
        ArrayList<Object[]> result_list = null;
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        try {
            con = DBconnection.getCon(username, password);

            StringBuilder query = new StringBuilder("CALL " + function + "(");

            if (parameters.length > 0) {
                // Append the first parameter placeholder
                query.append("?");

                // Append placeholders for additional parameters, if any
                for (int i = 1; i < parameters.length; i++) {
                    query.append(", ?");
                }
            }

            query.append(")");
            statement = con.prepareStatement(query.toString());

            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i] == null) {
                    statement.setObject(i + 1, null);
                } else {
                    statement.setObject(i + 1, parameters[i]);
                }
            }
            statement.execute();
            result = statement.getResultSet();

            result_list = result_to_list(result);

        } catch (SQLException e) {
            e.fillInStackTrace();
        } finally {
            DBconnection.closeCon(con, statement, result);
        }
        return result_list;
    }

    public static ArrayList<Object[]> result_to_list(ResultSet result) throws SQLException {
        ArrayList<Object[]> result_list = new ArrayList<>();
        ResultSetMetaData data = result.getMetaData();
        int column_count = data.getColumnCount();

        // Store column names
        Object[] columns = new Object[column_count];
        for (int i = 0; i < column_count; i++) {
            columns[i] = data.getColumnName(i + 1);
        }
        result_list.add(columns);

        // Check if the result set is empty
        if (!result.next()) {
            return result_list;
        }

        // Iterate over rows
        do {
            Object[] row = new Object[column_count];
            for (int i = 0; i < column_count; i++) {
                row[i] = result.getObject(i + 1);
            }
            result_list.add(row);
        } while (result.next());

        return result_list;
    }

}
