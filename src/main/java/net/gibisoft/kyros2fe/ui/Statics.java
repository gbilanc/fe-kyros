/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gibisoft.kyros2fe.ui;

import fte.xmlv121.ObjectFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import net.gibisoft.kyros2fe.entities.TabDocume;

/**
 *
 * @author giampaolo
 */
public final class Statics {

    private static Statics INSTANCE = null;
    private static final Preferences PREF = Preferences.userRoot().node("KYROS2");
    public static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public String DBDRIVER;
    public String DATABASE;
    public String PATHXML;
    public final static ObjectFactory OF = new ObjectFactory();

    private Statics() {
        DBDRIVER = PREF.get("DBDRIVER", "org.firebirdsql.jdbc.FBDriver");
        DATABASE = PREF.get("DATABASE", "jdbc:firebirdsql:KYROS2");
        PATHXML = PREF.get("PATHXML", "C:\\KYROS2\\FattureElettroniche");
        savePreferences();
    }

    public void savePreferences() {
        try {
            PREF.put("DBDRIVER", DBDRIVER);
            PREF.put("DATABASE", DATABASE);
            PREF.put("PATHXML", PATHXML);
            PREF.flush();
        } catch (BackingStoreException ex) {
            Logger.getLogger(Statics.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Statics getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Statics();
        }
        return INSTANCE;
    }

    public static Connection getDbConn() throws ClassNotFoundException, SQLException {
        if (INSTANCE == null) {
            INSTANCE = new Statics();
        }
        Class.forName(INSTANCE.DBDRIVER);
        Properties properties = new Properties();
        properties.setProperty("user", "SYSDBA");
        properties.setProperty("password", "masterkey");
        properties.setProperty("encoding", "UNICODE_FSS");
        return DriverManager.getConnection(INSTANCE.DATABASE, properties);
    }

    public static XMLGregorianCalendar toXMLGregorianCalendar(LocalDate data) {
        try {
            GregorianCalendar gcal = GregorianCalendar.from(data.atStartOfDay(ZoneId.systemDefault()));
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(TabDocume.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static XMLGregorianCalendar toXMLGregorianCalendar(Date value) {
        XMLGregorianCalendar xmlDate = null;
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(value);
        try {
            xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
            xmlDate.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(Statics.class.getName()).log(Level.SEVERE, null, ex);
        }
        return xmlDate;
    }

    public static List<TabDocume> getDocumenti(LocalDate data1, LocalDate data2, Long prog1, Long prog2) {
        List<TabDocume> result = new ArrayList();
        String query = "SELECT * FROM DOCUMENTI T1 "
                + "INNER JOIN TAB_TIPODOCUME T2 ON T1.TIPODOC=T2.CODICE "
                + "WHERE T1.CODEAZI = ? AND T2.TIPODOC_DF <> 'TD00' "
                + "AND T1.PROGDOC BETWEEN ? AND ? "
                + "AND T1.DATAREG BETWEEN ? AND ? "
                + "ORDER BY T1.DATAREG, T1.PROGDOC;";
        try (Connection conn = Statics.getDbConn()) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, "001");
                stmt.setLong(2, prog1);
                stmt.setLong(3, prog2);
                stmt.setDate(4, java.sql.Date.valueOf(data1));
                stmt.setDate(5, java.sql.Date.valueOf(data2));
                try (ResultSet rset = stmt.executeQuery()) {
                    while (rset.next()) {
                        result.add(new TabDocume(rset));
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(TabDocume.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static String getMessage(String code) {
        return ResourceBundle.getBundle("Messages", Locale.getDefault()).getString(code);
    }
}
