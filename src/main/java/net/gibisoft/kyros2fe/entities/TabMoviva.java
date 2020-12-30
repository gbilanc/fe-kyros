/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gibisoft.kyros2fe.entities;

import fte.xmlv121.DatiRiepilogoType;
import fte.xmlv121.EsigibilitaIVAType;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.gibisoft.kyros2fe.ui.Statics;
import static net.gibisoft.kyros2fe.ui.Statics.OF;

/**
 *
 * @author giampaolo
 */
public class TabMoviva {

    public String codeiva;
    public BigDecimal aliqiva;
    public BigDecimal imponib;
    public BigDecimal imposta;

    public TabMoviva(ResultSet rset) throws SQLException {
        this.codeiva = rset.getString("CODEIVA");
        this.aliqiva = rset.getBigDecimal("PERCIVA");
        this.imponib = rset.getBigDecimal("TOTAARTI");
        this.imposta = rset.getBigDecimal("TOTAIVA");
    }

    public static ArrayList<TabMoviva> lista(int progre) {
        Map<String, TabMoviva> result = new HashMap<>();
//        ArrayList<dbMOVIVA> result = new ArrayList();
        try (Connection conn = Statics.getDbConn()) {
            String query = "SELECT * FROM DOCUMERIG WHERE PROGDOC = ? ORDER BY RIGADOC;";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, progre);
                try (ResultSet rset = stmt.executeQuery()) {
                    while (rset.next()) {
                        TabMoviva mm = new TabMoviva(rset);
                        if (result.containsKey(mm.codeiva)) {
                            result.get(mm.codeiva).imponib = result.get(mm.codeiva).imponib.add(mm.imponib);
                            result.get(mm.codeiva).imposta = result.get(mm.codeiva).imposta.add(mm.imposta);
                        } else {
                            result.put(mm.codeiva, mm);
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(TabMoviva.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>(result.values());
    }

    public DatiRiepilogoType dettaglio() {
        TabCodiva codiva = new TabCodiva(this.codeiva);
        DatiRiepilogoType item = OF.createDatiRiepilogoType();
        item.setAliquotaIVA(aliqiva.setScale(2));
        if (aliqiva.compareTo(BigDecimal.ZERO) == 0) {
            item.setNatura(codiva.naturaType());
            item.setRiferimentoNormativo(codiva.descri);
        }
        item.setImponibileImporto(imponib.setScale(2));
        item.setImposta(imposta.setScale(2));
        if (imposta.compareTo(BigDecimal.ZERO) > 0) {
            item.setEsigibilitaIVA(EsigibilitaIVAType.I);
        }
        return item;

    }

}
