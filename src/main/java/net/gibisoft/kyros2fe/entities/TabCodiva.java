/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gibisoft.kyros2fe.entities;

import fte.xmlv121.NaturaType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.gibisoft.kyros2fe.ui.Statics;

/**
 *
 * @author giampaolo
 */
public class TabCodiva {

    public String codice;
    public String descri;
    public Double aliquota;
    public Double percdet;
    public String natura;

    public TabCodiva() {
        codice = "";
        descri = "";
        aliquota = 0.0;
        percdet = 100.0;
        natura = "";
    }

    public TabCodiva(String codice) {
        this();
        try (Connection conn = Statics.getDbConn()) {
            String query = "SELECT * FROM ALIQIVA WHERE CODEIVA=?;";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, codice);
                try (ResultSet rset = stmt.executeQuery()) {
                    if (rset.next()) {
                        this.codice = rset.getString("CODEIVA");
                        this.descri = rset.getString("DESCIVA");
                        this.aliquota = rset.getDouble("ALIQIVA");
                        this.percdet = 100.0;
                        this.natura = rset.getString("NATURA_DF");
                    } else {
                        this.codice = codice;
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(TabCodiva.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public NaturaType naturaType() {
        return NaturaType.fromValue(natura);
    }
}
