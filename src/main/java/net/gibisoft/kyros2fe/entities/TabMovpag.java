/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gibisoft.kyros2fe.entities;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.gibisoft.kyros2fe.ui.Statics;

/**
 *
 * @author gbila
 */
public class TabMovpag {

    public String modopag;
    public LocalDate datascad;
    public BigDecimal importo;

    public TabMovpag(ResultSet rset) throws SQLException {
        this.modopag = rset.getString(1);
        this.datascad = rset.getDate(2).toLocalDate();
        this.importo = rset.getBigDecimal(3);
    }

    public static ArrayList<TabMovpag> lista(int progre) {
        ArrayList<TabMovpag> result = new ArrayList();
        try (Connection conn = Statics.getDbConn()) {
            String query = "SELECT TIPOPAG,DATACOMP,IMPORTO FROM MOVCASSA "
                    + "WHERE PROGDOC = ? ORDER BY DATACOMP;";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, progre);
                try (ResultSet rset = stmt.executeQuery()) {
                    while (rset.next()) {
                        result.add(new TabMovpag(rset));
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(TabMovpag.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
