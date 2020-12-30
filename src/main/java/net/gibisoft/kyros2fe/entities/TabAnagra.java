/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gibisoft.kyros2fe.entities;

import fte.xmlv121.CessionarioCommittenteType;
import fte.xmlv121.ContattiType;
import fte.xmlv121.DatiAnagraficiVettoreType;
import fte.xmlv121.FormatoTrasmissioneType;
import fte.xmlv121.IndirizzoType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.gibisoft.kyros2fe.ui.Statics;
import static net.gibisoft.kyros2fe.ui.Statics.OF;
import static net.gibisoft.kyros2fe.ui.Statics.getMessage;

/**
 *
 * @author giampaolo
 */
public final class TabAnagra {

    public String CODEANA;
    public String TIPOANA;
    public String DESCANA;
    public String INDIANA;
    public String CAPANA;
    public String COMUANA;
    public String LOCAANA;
    public String PROVANA;
    public String TEL1ANA;
    public String TEL2ANA;
    public String TEL3ANA;
    public String TEL4ANA;
    public String COFIANA;
    public String PAIVANA;
    public String IDPAESE;

    public TabAnagra() {
        this.CODEANA = "";
        this.TIPOANA = "";
        this.DESCANA = "";
        this.INDIANA = "";
        this.CAPANA = "";
        this.COMUANA = "";
        this.LOCAANA = "";
        this.PROVANA = "";
        this.TEL1ANA = "";
        this.TEL2ANA = "";
        this.TEL3ANA = "";
        this.TEL4ANA = "";
        this.COFIANA = "";
        this.PAIVANA = "";
        this.IDPAESE = "IT";
    }

    public TabAnagra(String codice) {
        this();
        try (Connection conn = Statics.getDbConn()) {
            String query = "SELECT * FROM ANAGRA WHERE CODEANA=?;";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, codice);
                try (ResultSet rset = stmt.executeQuery()) {
                    if (rset.next()) {
                        this.CODEANA = rset.getString("CODEANA");
                        this.TIPOANA = rset.getString("TIPOANA");
                        this.DESCANA = rset.getString("DESCANA").trim();
                        this.INDIANA = rset.getString("INDIANA").trim();
                        this.CAPANA = rset.getString("CAPANA").trim();
                        this.COMUANA = rset.getString("COMUANA").trim();
                        this.PROVANA = rset.getString("PROVANA").trim();
                        this.TEL1ANA = rset.getString("TEL1ANA").trim();
                        this.TEL2ANA = rset.getString("TEL2ANA").trim();
                        this.TEL3ANA = rset.getString("TEL3ANA").trim();
                        this.TEL4ANA = rset.getString("TEL4ANA").trim();
                        this.COFIANA = rset.getString("COFIANA").trim();
                        this.PAIVANA = rset.getString("PAIVANA").trim();
                        this.IDPAESE = "IT";
                    } else {
                        this.CODEANA = codice;
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(TabAnagra.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", CODEANA, DESCANA);
    }

    public String getCodesdi() {
        return (TEL4ANA.length() == 7) ? TEL4ANA : "0000000";

    }

    public String getEmailpec() {
        return (TEL4ANA.length() > 7) ? TEL4ANA : "";
    }

    public boolean isItaliano() {
        return IDPAESE.equals("IT");
    }

    public String verificaDatiTrasmissione(boolean senzasdi) {
        if (isItaliano()) {
            if (PAIVANA.isEmpty()) {
                return "OK"; // privato consumatore
            } else { // soggetto Iva 
                if (!getCodesdi().equals("0000000")) {
                    return "OK"; // codice SDI presente
                }
                if (!getEmailpec().isEmpty()) {
                    return "OK"; // emailpec presente
                }
                if (senzasdi) { // assenza sdi e pec ammessa
                    return "OK";
                }
                return String.format(getMessage("M004"), this);
            }
        } else { // soggetto estero
            if (getCodesdi().equals("XXXXXXX")) {
                return "OK";
            } else {
                return String.format(getMessage("M006"), this);
            }
        }
    }

    public String verificaDatiAnagrafici() {
        if (DESCANA.isEmpty()) {
            return String.format(getMessage("M003"), this);
        }
        if (INDIANA.isEmpty()) {
            return String.format(getMessage("M007"), this);
        }
        if (CAPANA.isEmpty()) {
            return String.format(getMessage("M008"), this);
        }
        if (COMUANA.isEmpty()) {
            return String.format(getMessage("M009"), this);
        }
        if (isItaliano()) {
            if (COFIANA.isEmpty()) {
                return String.format(getMessage("M001"), this);
            }
        } else { // soggetto estero
            if (PAIVANA.isEmpty()) {
                return String.format(getMessage("M002"), this);
            }
        }
        return "OK";
    }

    public FormatoTrasmissioneType getFormatoTrasmissioneType() {
        return FormatoTrasmissioneType.FPR_12;
    }

    public CessionarioCommittenteType getCessionarioCommittente() {
        CessionarioCommittenteType item = OF.createCessionarioCommittenteType();
        item.setDatiAnagrafici(OF.createDatiAnagraficiCessionarioType());
        if (!PAIVANA.isEmpty()) {
            item.getDatiAnagrafici().setIdFiscaleIVA(OF.createIdFiscaleType());
            item.getDatiAnagrafici().getIdFiscaleIVA().setIdPaese("IT");
            item.getDatiAnagrafici().getIdFiscaleIVA().setIdCodice(PAIVANA);
        }
        if (!COFIANA.isEmpty()) {
            item.getDatiAnagrafici().setCodiceFiscale(COFIANA);
        }
        item.getDatiAnagrafici().setAnagrafica(OF.createAnagraficaType());
        if ("S".equals(TIPOANA)) {
            String[] chunks = DESCANA.split("\\s+", 2);
            item.getDatiAnagrafici().getAnagrafica().setCognome(chunks[0]);
            item.getDatiAnagrafici().getAnagrafica().setNome(chunks[1]);
        } else {
            item.getDatiAnagrafici().getAnagrafica().setDenominazione(DESCANA);
        }
        item.setSede(getIndirizzo());
//        item.setStabileOrganizzazione(this.getStabileOrganizzazione());
//        item.setRappresentanteFiscale(this.getRappresentanteFiscaleCessionario());
        return item;
    }

    private IndirizzoType getIndirizzo() {
        IndirizzoType item = OF.createIndirizzoType();
        item.setIndirizzo(INDIANA);
        item.setCAP(CAPANA);
        item.setComune(COMUANA);
        item.setProvincia(PROVANA);
        item.setNazione("IT");
        return item;
    }

    private ContattiType getContatti() {
        ContattiType item = OF.createContattiType();
        if (!TEL4ANA.isEmpty()) {
            item.setEmail(TEL4ANA);
        }
        if (!TEL1ANA.isEmpty()) {
            item.setTelefono(TEL1ANA);
        }
        return item;
    }

    public DatiAnagraficiVettoreType getDatiAnagraficiVettore() {
        DatiAnagraficiVettoreType item = OF.createDatiAnagraficiVettoreType();
        if (!PAIVANA.isEmpty()) {
            item.setIdFiscaleIVA(OF.createIdFiscaleType());
            item.getIdFiscaleIVA().setIdPaese("IT");
            item.getIdFiscaleIVA().setIdCodice(PAIVANA);
        }
        if (!COFIANA.isEmpty()) {
            item.setCodiceFiscale(COFIANA);
        }
        item.setAnagrafica(OF.createAnagraficaType());
        if (!DESCANA.contains("*")) {
            item.getAnagrafica().setDenominazione(DESCANA);
        } else {
            String[] chunks = DESCANA.split("\\*");
            item.getAnagrafica().setCognome(chunks[0]);
            item.getAnagrafica().setNome(chunks[1]);
        }
//        item.setNumeroLicenzaGuida("");
        return item;
    }

    public IndirizzoType getIndirizzoResa() {
        IndirizzoType item = OF.createIndirizzoType();
        item.setIndirizzo(INDIANA);
        item.setCAP(CAPANA);
        item.setComune(COMUANA);
        item.setProvincia(PROVANA);
        item.setNazione("IT");
        return item;
    }

}
