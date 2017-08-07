/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import static com.sun.corba.se.impl.util.Utility.printStackTrace;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SayefReyadh
 */
public abstract class DatabaseConnection {

    private final String USER_NAME = "root";
    private final String PASSWORD = "";
    private final String DATABASE_NAME = "hospital_management";
    private final String CLASSFOR = "com.mysql.jdbc.Driver";
    private final String URL = "jdbc:mysql://localhost/" + DATABASE_NAME + "?useSSL=false";

    private Connection mConnection = null;

    public boolean openDatabaseConnection() {
        try {
            mConnection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            System.out.println("Successfully Connected..");
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Cannot Connect to Database");
            printStackTrace();
        }
        return false;
    }

    public boolean closeConnection() {
        if (mConnection != null) {
            try {
                mConnection.close();
                System.out.println("Connection Closed");
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return true;
    }

    public ResultSet excuteSearchQuery(String query) {
        Statement statement = null;
        ResultSet result = null;
        PreparedStatement pStatement = null;
        if (openDatabaseConnection()) {
            try {
                pStatement = mConnection.prepareStatement(query);
                result = pStatement.executeQuery();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
                closeConnection();
                return null;
            }
        }
        closeConnection();
        return result;
    }

    public boolean excuteInsertQuery(String query) {

        PreparedStatement pStatement = null;
        if (openDatabaseConnection()) {
            try {
                pStatement = mConnection.prepareStatement(query);
                int res = pStatement.executeUpdate();
                if (res > 0) {
                    closeConnection();
                    return true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        closeConnection();
        return false;
    }

    abstract public void insert();

    abstract public void update();

    abstract public void delete();

    abstract public void fetch();

}
