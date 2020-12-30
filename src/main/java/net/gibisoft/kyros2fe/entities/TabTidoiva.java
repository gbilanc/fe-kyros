/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gibisoft.kyros2fe.entities;

import fte.xmlv121.TipoDocumentoType;
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
public class TabTidoiva {

    public String CODICE;
    public String DESCRI;

    public TabTidoiva() {
        CODICE = "";
        DESCRI = "";
    }

    public TabTidoiva(String codice) {
        this();
        try (Connection conn = Statics.getDbConn()) {
            String query = "SELECT * FROM DF_TIPODOCIVA WHERE CODICE=?;";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, codice);
                try (ResultSet rset = stmt.executeQuery()) {
                    if (rset.next()) {
                        CODICE = rset.getString("CODICE");
                        DESCRI = rset.getString("DESCRI");
                    } else {
                        CODICE = codice;
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(TabTidoiva.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public TipoDocumentoType tipodocumento() {
        return TipoDocumentoType.fromValue(CODICE);
    }

}
