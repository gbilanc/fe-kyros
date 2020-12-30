/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gibisoft.kyros2fe.entities;

import fte.xmlv121.CedentePrestatoreType;
import fte.xmlv121.ContattiTrasmittenteType;
import fte.xmlv121.ContattiType;
import fte.xmlv121.IndirizzoType;
import fte.xmlv121.IscrizioneREAType;
import fte.xmlv121.RappresentanteFiscaleType;
import fte.xmlv121.RegimeFiscaleType;
import fte.xmlv121.SocioUnicoType;
import fte.xmlv121.SoggettoEmittenteType;
import fte.xmlv121.StatoLiquidazioneType;
import fte.xmlv121.TerzoIntermediarioSoggettoEmittenteType;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.gibisoft.kyros2fe.ui.Statics;
import static net.gibisoft.kyros2fe.ui.Statics.OF;

/**
 *
 * @author giampaolo
 */
public final class TabDitta implements Serializable {

    private final static long serialVersionUID = 1;
    public String IdPaese;
    public String CodiceFiscale;
    public String PartitaIva;
    public String Denominazione;
    public String Cognome;
    public String Nome;
    public String Sesso;
    public String Datanas;
    public String Comunas;
    public String Provnas;
    public String Titolo;
    public String CodEORI;
    public String Telefono;
    public String Telefax;
    public String Email;
    public String RegimeFiscale;
    public String SoggettoEmittente;
    public IndirizzoType sede;
    public IndirizzoType stabileOrganizzazione;
    public IscrizioneREAType iscrizioneRea;
    public RappresentanteFiscaleType rappresentanteFiscale;
    public TerzoIntermediarioSoggettoEmittenteType terzoIntermediario;

    public TabDitta() {
        IdPaese = "";
        CodiceFiscale = "";
        PartitaIva = "";
        Denominazione = "";
        Cognome = "";
        Nome = "";
        Sesso = "";
        Datanas = "";
        Comunas = "";
        Provnas = "";
        Titolo = "";
        CodEORI = "";
        Telefono = "";
        Telefax = "";
        Email = "";
        RegimeFiscale = "RF01";
        SoggettoEmittente = "CC";
        sede = null;
        stabileOrganizzazione = null;
        iscrizioneRea = null;
        rappresentanteFiscale = null;
        terzoIntermediario = null;
        load("001");
    }

    private void load(String codice) {
        try (Connection conn = Statics.getDbConn()) {
            String query = "SELECT * FROM AZIENDE WHERE CODEAZI=?;";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, codice);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                IdPaese = "IT";
                CodiceFiscale = rs.getString("COFIAZI");
                PartitaIva = rs.getString("PAIVAZI");
                getDenominazione(rs.getString("DESCAZI"));
                Sesso = rs.getString("SESSO");
                Comunas = rs.getString("COMUNAS");
                Provnas = rs.getString("PROVNAS");
                Datanas = rs.getString("DATANAS");
                Telefono = rs.getString("TEL1AZI");
                Email = rs.getString("TEL4AZI");
                sede = OF.createIndirizzoType();
                sede.setIndirizzo(rs.getString("INDIAZI"));
                sede.setCAP(rs.getString("CAPAZI"));
                sede.setComune(rs.getString("COMUAZI"));
                sede.setProvincia(rs.getString("PROVAZI"));
                sede.setNazione("IT");
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(TabDitta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getDenominazione(String descazi) {
        if (!descazi.contains("*")) {
            Denominazione = descazi;
        } else {
            String[] chunks = descazi.split("\\*");
            Cognome = chunks[0];
            Nome = chunks[1];
        }
    }

    public CedentePrestatoreType getCedentePrestatore() {
        CedentePrestatoreType item = OF.createCedentePrestatoreType();
        item.setDatiAnagrafici(OF.createDatiAnagraficiCedenteType());
        if (!PartitaIva.isEmpty()) {
            item.getDatiAnagrafici().setIdFiscaleIVA(OF.createIdFiscaleType());
            item.getDatiAnagrafici().getIdFiscaleIVA().setIdPaese(IdPaese);
            item.getDatiAnagrafici().getIdFiscaleIVA().setIdCodice(PartitaIva);
        }
        if (!CodiceFiscale.isEmpty()) {
            item.getDatiAnagrafici().setCodiceFiscale(CodiceFiscale);
        }
        item.getDatiAnagrafici().setAnagrafica(OF.createAnagraficaType());
        if (!Denominazione.isEmpty()) {
            item.getDatiAnagrafici().getAnagrafica().setDenominazione(Denominazione);
        } else {
            item.getDatiAnagrafici().getAnagrafica().setCognome(Cognome);
            item.getDatiAnagrafici().getAnagrafica().setNome(Nome);
        }
//        item.getDatiAnagrafici().setAlboProfessionale("");
//        item.getDatiAnagrafici().setProvinciaAlbo("");
//        item.getDatiAnagrafici().setNumeroIscrizioneAlbo("");
//        item.getDatiAnagrafici().setDataIscrizioneAlbo(getXMLGregorianCalendar(new Date()));

        item.getDatiAnagrafici().setRegimeFiscale(RegimeFiscaleType.fromValue(RegimeFiscale));
        item.setSede(sede);
//        if (!stabileOrganizzazione.getIndirizzo().isEmpty()) {
//            item.setStabileOrganizzazione(stabileOrganizzazione);
//        }
//        if (!iscrizioneRea.getNumeroREA().isEmpty()) {
//            item.setIscrizioneREA(iscrizioneRea);
//        }
        item.setContatti(getContatti());
        return item;
    }

    public TerzoIntermediarioSoggettoEmittenteType getTerzoIntermediario() {
        return terzoIntermediario;
    }

    public RappresentanteFiscaleType getRappresentanteFiscale() {
        return rappresentanteFiscale;
    }

    public ContattiTrasmittenteType getContattiTrasmittente() {
        ContattiTrasmittenteType item = OF.createContattiTrasmittenteType();
        if (!Telefono.isEmpty()) {
            item.setTelefono(Telefono);
        }
        if (!Email.isEmpty()) {
            item.setEmail(Email);
        }
        return item;
    }

    private ContattiType getContatti() {
        ContattiType item = OF.createContattiType();
        if (!Telefono.isEmpty()) {
            item.setTelefono(Telefono);
        }
        if (!Telefax.isEmpty()) {
            item.setFax(Telefax);
        }
        if (!Email.isEmpty()) {
            item.setEmail(Email);
        }
        return item;
    }

    public IndirizzoType getSede() {
        if (sede == null) {
            sede = OF.createIndirizzoType();
        }
        return sede;
    }

    public IndirizzoType getStabileOrganizzazione() {
        return stabileOrganizzazione;
    }

    public IscrizioneREAType getIscrizioneREA() {
        return iscrizioneRea;
    }

    public RegimeFiscaleType regimeFiscaleType() {
        return RegimeFiscaleType.fromValue(RegimeFiscale);
    }

    public SoggettoEmittenteType soggettoEmittenteType() {
        return SoggettoEmittenteType.fromValue(SoggettoEmittente);
    }

    public SocioUnicoType socioUnicoType() {
        return iscrizioneRea.getSocioUnico();
    }

    public StatoLiquidazioneType statoLiquidazioneType() {
        return iscrizioneRea.getStatoLiquidazione();
    }

}
