package com.maorgil.hospitalappointmentsystem.beans;

import com.maorgil.hospitalappointmentsystem.DBHandler;

import javax.faces.bean.ManagedBean;
import java.util.List;

@ManagedBean(name = "adminBean")
public class AdminBean {
    private String query;
    private String result;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getResult() {
        return result;
    }

    public void execute() {
        result = execute(query);
    }

    private static String execute(String query) {
        DBHandler dbHandler = DBHandler.getInstance();

        try {
            if (isSelectQuery(query)) {
                // Execute query and retrieve result list
                List<Object[]> resultList = dbHandler.executeUserSelectQuery(query);

                // Convert result list to string
                StringBuilder sb = new StringBuilder();
                for (Object[] result : resultList) {
                    for (Object obj : result) {
                        sb.append(obj != null ? obj.toString() : "null").append("\t"); // Use tab as separator
                    }
                    sb.append("\n"); // Use newline to separate rows
                }

                return sb.toString();
            } else {
                // For non-SELECT query, execute update and retrieve number of rows affected
                int numRowsAffected = dbHandler.executeAlterQuery(query);
                return "rows affected (" + numRowsAffected + ")";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    // Helper method to check if query is SELECT query
    private static boolean isSelectQuery(String sqlQuery) {
        String queryType = sqlQuery.trim().substring(0, 6).toUpperCase();
        return "SELECT".equals(queryType);
    }
}
