/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gibisoft.kyros2fe.entities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author giampaolo
 */
public final class dbHelper {

    private final static String DBDRIVER = "org.firebirdsql.jdbc.FBDriver";
    private final static String DATABASE = "jdbc:firebirdsql:KYROS2";

    private static dbHelper instance = null;

    public static Connection getDbConn() {
        if (instance == null) {
            instance = new dbHelper();
        }
        try {
            Class.forName(DBDRIVER);
            Properties connectionProperties = new Properties();
            connectionProperties.setProperty("user", "SYSDBA");
            connectionProperties.setProperty("password", "masterkey");
            connectionProperties.setProperty("encoding", "UTF8");
            return DriverManager.getConnection(DATABASE, connectionProperties);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(dbHelper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
