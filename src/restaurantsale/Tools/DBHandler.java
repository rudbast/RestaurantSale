/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restaurantsale.Tools;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author rudolf
 */
public class DBHandler {
    public static final String HOST = "jdbc:mysql://127.0.0.1:3306/";
    public static final String DB_NAME = "resto_sale";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";
    public static final String URL = HOST + DB_NAME + "?user=" + USERNAME + "&password=" + PASSWORD;

    public static void execNonQuery(String sqlQuery){
        // execute query without returning any query result
        java.sql.Connection conn = null;
        Statement stm = null;
        try {
            // connection/statement/resultset declaration
            conn = DriverManager.getConnection(DBHandler.URL);
            stm = conn.createStatement();
            stm.executeUpdate(sqlQuery);
            //System.out.println(rs + " row(s) affected.");
        }
        catch (SQLException e){
            System.out.println(e.toString());
        }
        finally {
            try {
                // close existing connection/statement/resultset
                stm.close();
                conn.close();
            } catch (SQLException e){
                System.out.println(e.toString());
            }
        }
    }

    public static String getField(String sqlQuery) {
        // execute query returning single column value
        java.sql.Connection conn = null;
        Statement stm = null;
        ResultSet rs = null;
        String result = "";
        try {
            // connection/statement/resultset declaration
            conn = DriverManager.getConnection(DBHandler.URL);
            stm = conn.createStatement();
            rs = stm.executeQuery(sqlQuery);
            // read query result
            while(rs.next()){
                result = rs.getString(1);
            }
        }
        catch (SQLException e){
            System.out.println(e.toString());
        }
        finally {
            try {
                // close existing connection/statement/resultset
                rs.close();
                stm.close();
                conn.close();
            } catch (SQLException e){
                System.out.println(e.toString());
            }
        }
        return result;
    }

    public static String getTable(String sqlQuery, int TYPE) {
//        System.out.println(sqlQuery);
        String result = "";
        java.sql.Connection conn = null;
        Statement stm = null;
        ResultSet rs = null;
        int i = 0;

        try {
            // connection/statement/resultset declaration 
            conn = DriverManager.getConnection(DBHandler.URL);
            stm = conn.createStatement();
            rs = stm.executeQuery(sqlQuery);

            while(rs.next()) {
                if(i > 0) result += ":";
                switch(TYPE){
                    case 0: // MENU / SEARCH
                        result += rs.getString(1) + "." + rs.getString(2);
                        break;
                    case 1: // ORDER (returning order queues) / CHECK (check existing order on current table)
                        result += rs.getString(1) + "." + rs.getString(2) + "." + rs.getString(3);
                        break;
                    case 2:
                        result += rs.getString(1) + "." + rs.getString(2) + "." + rs.getString(3) + "." + rs.getString(4);
                }
                ++i;
            }
        }
        catch (SQLException e){
            System.out.println(e.toString());
        }
        finally {
            try {
                // close existing connection/statement/resultset
                rs.close();
                stm.close();
                conn.close();
            } catch (SQLException e){
                System.out.println(e.toString());
            }
        }
        return result;
    }
}
