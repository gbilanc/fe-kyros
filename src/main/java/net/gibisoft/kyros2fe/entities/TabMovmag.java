/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gibisoft.kyros2fe.entities;

import fte.xmlv121.CodiceArticoloType;
import fte.xmlv121.DettaglioLineeType;
import fte.xmlv121.ScontoMaggiorazioneType;
import fte.xmlv121.TipoScontoMaggiorazioneType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.gibisoft.kyros2fe.ui.Statics;
import static net.gibisoft.kyros2fe.ui.Statics.OF;

/**
 *
 * @author giampaolo
 */
public final class TabMovmag {

    private final String codeart;
    private final String descart;
    private final String unimis;
    private final BigDecimal quanti;
    private final BigDecimal prezzo;
    private final String codeiva;
    private final BigDecimal aliqiva;
    private final BigDecimal sconto1;
    private final BigDecimal sconto2;

    public TabMovmag(ResultSet rset) throws SQLException {
        this.codeart = rset.getString("CODEART");
        this.descart = rset.getString("DESCART");
        this.unimis = rset.getString("UNIMIS");
        this.quanti = rset.getBigDecimal("QUANART");
        this.prezzo = rset.getBigDecimal("PREZART");
        this.codeiva = rset.getString("CODEIVA");
        this.aliqiva = rset.getBigDecimal("PERCIVA");
        this.sconto1 = BigDecimal.ZERO;
        this.sconto2 = BigDecimal.ZERO;
    }

    public BigDecimal importo() {
        BigDecimal importo = quanti.multiply(prezzo);
        if (sconto1.compareTo(BigDecimal.ZERO) > 0) {
            importo.subtract(importo.multiply(sconto1.multiply(new BigDecimal(0.01))));
        }
        if (sconto2.compareTo(BigDecimal.ZERO) > 0) {
            importo.subtract(importo.multiply(sconto2.multiply(new BigDecimal(0.01))));
        }
        return importo;
    }

    public static ArrayList<TabMovmag> lista(int progre) {
        ArrayList<TabMovmag> result = new ArrayList();
        try (Connection conn = Statics.getDbConn()) {
            String query = "SELECT * FROM DOCUMERIG WHERE PROGDOC = ? ORDER BY RIGADOC;";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, progre);
                try (ResultSet rset = stmt.executeQuery()) {
                    while (rset.next()) {
                        result.add(new TabMovmag(rset));
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(TabMovmag.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String toString() {
        return descart;
    }

    public DettaglioLineeType dettaglio(int riga) {
        DettaglioLineeType item = OF.createDettaglioLineeType();
        item.setNumeroLinea(riga);
        CodiceArticoloType codart = OF.createCodiceArticoloType();
        codart.setCodiceTipo("Codice Prestazione");
        codart.setCodiceValore(codeart.trim());
        item.getCodiceArticolo().add(codart);
        item.setDescrizione(this.toString());
        item.setQuantita(quanti.setScale(4, RoundingMode.HALF_UP));
        item.setUnitaMisura(unimis.isEmpty() ? "NUM" : unimis);
        item.setPrezzoUnitario(prezzo.setScale(4, RoundingMode.HALF_UP));
        if (sconto1.compareTo(BigDecimal.ZERO) > 0) {
            ScontoMaggiorazioneType scomag = OF.createScontoMaggiorazioneType();
            scomag.setTipo(TipoScontoMaggiorazioneType.SC);
            scomag.setPercentuale(sconto1.setScale(2, RoundingMode.HALF_UP));
        }
        if (sconto2.compareTo(BigDecimal.ZERO) > 0) {
            ScontoMaggiorazioneType scomag = OF.createScontoMaggiorazioneType();
            scomag.setTipo(TipoScontoMaggiorazioneType.SC);
            scomag.setPercentuale(sconto2.setScale(2, RoundingMode.HALF_UP));
        }
        item.setPrezzoTotale(importo().setScale(2, RoundingMode.HALF_UP));
        item.setAliquotaIVA(aliqiva.setScale(2));
        if (aliqiva.compareTo(BigDecimal.ZERO) == 0) {
            TabCodiva codiva = new TabCodiva(codeiva);
            item.setNatura(codiva.naturaType());
        }
        return item;
    }

}
