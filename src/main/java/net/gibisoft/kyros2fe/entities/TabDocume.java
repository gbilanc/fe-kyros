/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gibisoft.kyros2fe.entities;

import fte.xmlv121.BolloVirtualeType;
import fte.xmlv121.CausalePagamentoType;
import fte.xmlv121.CondizioniPagamentoType;
import fte.xmlv121.DatiBolloType;
import fte.xmlv121.DatiCassaPrevidenzialeType;
import fte.xmlv121.DatiDDTType;
import fte.xmlv121.DatiGeneraliDocumentoType;
import fte.xmlv121.DatiRitenutaType;
import fte.xmlv121.DettaglioPagamentoType;
import fte.xmlv121.ModalitaPagamentoType;
import fte.xmlv121.NaturaType;
import fte.xmlv121.RitenutaType;
import fte.xmlv121.TipoCassaType;
import fte.xmlv121.TipoRitenutaType;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import net.gibisoft.kyros2fe.ui.Statics;
import static net.gibisoft.kyros2fe.ui.Statics.DMY;
import static net.gibisoft.kyros2fe.ui.Statics.OF;
import static net.gibisoft.kyros2fe.ui.Statics.toXMLGregorianCalendar;

/**
 *
 * @author giampaolo
 */
public class TabDocume {

    public int PROGDOC;
    public String TIPODOC;
    public int NUMEREG;
    public LocalDate DATAREG;
    public String NUMEDOC;
    public LocalDate DATADOC;
    public String CODEANA;
    public BigDecimal IMPOARTI;
    public BigDecimal PERCINPS;
    public BigDecimal IMPOINPS;
    public BigDecimal TOTAINPS;
    public BigDecimal PERCIVA;
    public BigDecimal IMPOIVA;
    public BigDecimal TOTAIVA;
    public BigDecimal PERCRITE;
    public BigDecimal IMPORITE;
    public BigDecimal TOTARITE;
    public String NOTEDOC;
    public String filename;
    public String errormsg;

    private final BigDecimal limiteBollo = new BigDecimal(77.47);
    private final BigDecimal impoBollo = new BigDecimal(2.0);

    public TabDocume() {
        PROGDOC = 0;
        TIPODOC = "";
        NUMEREG = 0;
        DATAREG = LocalDate.now();
        NUMEDOC = "";
        DATADOC = LocalDate.now();
        CODEANA = "";
        IMPOARTI = BigDecimal.ZERO;
        PERCINPS = BigDecimal.ZERO;
        IMPOINPS = BigDecimal.ZERO;
        TOTAINPS = BigDecimal.ZERO;
        PERCIVA = BigDecimal.ZERO;
        IMPOIVA = BigDecimal.ZERO;
        TOTAIVA = BigDecimal.ZERO;
        PERCRITE = BigDecimal.ZERO;
        IMPORITE = BigDecimal.ZERO;
        TOTARITE = BigDecimal.ZERO;
        NOTEDOC = "";
        filename = "";
        errormsg = "";
    }

    public TabDocume(ResultSet rset) throws SQLException {
        PROGDOC = rset.getInt("PROGDOC");
        TIPODOC = rset.getString("TIPODOC");
        NUMEREG = rset.getInt("NUMEREG");
        DATAREG = rset.getDate("DATAREG").toLocalDate();
        NUMEDOC = rset.getString("NUMEDOC");
        DATADOC = rset.getDate("DATADOC").toLocalDate();
        CODEANA = rset.getString("CODEANA");
        IMPOARTI = rset.getBigDecimal("IMPOARTI");
        PERCINPS = rset.getBigDecimal("PERCINPS");
        IMPOINPS = rset.getBigDecimal("IMPOINPS");
        TOTAINPS = rset.getBigDecimal("TOTAINPS");
        PERCIVA = rset.getBigDecimal("PERCIVA");
        IMPOIVA = rset.getBigDecimal("IMPOIVA");
        TOTAIVA = rset.getBigDecimal("TOTAIVA");
        PERCRITE = rset.getBigDecimal("PERCRITE");
        IMPORITE = rset.getBigDecimal("IMPORITE");
        TOTARITE = rset.getBigDecimal("TOTARITE");
        NOTEDOC = rset.getString("NOTEDOC");
    }

    private String details() {
        return String.format("%s numero %s del %s cliente %s",
                tipodoc().toString(), NUMEDOC.trim(),
                DATADOC.format(DMY), clifor().toString());
    }

    @Override
    public String toString() {
        if (errormsg.equals("OK")) {
            return String.format(Statics.getMessage("MASK0"), details(), filename);
        } else {
            return String.format(Statics.getMessage("MASK1"), details(), errormsg);
        }
    }

    public TabTipodoc tipodoc() {
        return new TabTipodoc(TIPODOC);
    }

    public TabAnagra clifor() {
        return new TabAnagra(CODEANA);
    }

    public ArrayList<TabMoviva> moviva() {
        return TabMoviva.lista(PROGDOC);
    }

    public ArrayList<TabMovmag> movmag() {
        return TabMovmag.lista(PROGDOC);
    }

    public ArrayList<TabMovpag> movpag() {
        return TabMovpag.lista(PROGDOC);
    }

    public BigDecimal totaleDocumento() {
        return IMPOARTI.add(TOTAINPS).add(TOTAIVA);
    }

    public DatiGeneraliDocumentoType getDatiGeneraliDocumento() {
        TabTipodoc td = new TabTipodoc(TIPODOC);
        DatiGeneraliDocumentoType item = OF.createDatiGeneraliDocumentoType();
        item.setTipoDocumento(td.tipodocumento());
        item.setDivisa("EUR");
        item.setData(toXMLGregorianCalendar(DATADOC));
        item.setNumero(NUMEDOC);
        if (IMPORITE.compareTo(BigDecimal.ZERO) > 0) {
            item.getDatiRitenuta().add(getDatiRitenuta());
        }
        if (totaleDocumento().compareTo(limiteBollo) > 0
                && TOTAIVA.compareTo(BigDecimal.ZERO) == 0) {
            item.setDatiBollo(getDatiBollo());
        }
//        item.getDatiCassaPrevidenziale().add(this.getDatiCassaPrevidenziale());
        item.setImportoTotaleDocumento(totaleDocumento().setScale(2));
//        item.setArrotondamento(BigDecimal.ZERO);
//        item.getCausale().add("descrizione causale del documento");
//        item.setArt73(Art73Type.SI);
        return item;
    }

    private DatiRitenutaType getDatiRitenuta() {
        DatiRitenutaType ritenuta = OF.createDatiRitenutaType();
        ritenuta.setTipoRitenuta(TipoRitenutaType.RT_01);
        ritenuta.setImportoRitenuta(TOTARITE.setScale(2));
        ritenuta.setAliquotaRitenuta(PERCRITE.setScale(2));
        ritenuta.setCausalePagamento(CausalePagamentoType.A);
        return ritenuta;
    }

    private DatiBolloType getDatiBollo() {
        DatiBolloType bollo = OF.createDatiBolloType();
        bollo.setBolloVirtuale(BolloVirtualeType.SI);
        bollo.setImportoBollo(impoBollo.setScale(2));
        return bollo;
    }

    private DatiCassaPrevidenzialeType getDatiCassaPrevidenziale() {
        DatiCassaPrevidenzialeType cassapre = OF.createDatiCassaPrevidenzialeType();
        cassapre.setTipoCassa(TipoCassaType.TC_01);
        cassapre.setAlCassa(BigDecimal.ZERO);
        cassapre.setImportoContributoCassa(BigDecimal.ZERO);
        cassapre.setImponibileCassa(BigDecimal.ZERO);
        cassapre.setAliquotaIVA(BigDecimal.ZERO);
        cassapre.setRitenuta(RitenutaType.SI);
        if (cassapre.getAliquotaIVA().equals(BigDecimal.ZERO)) {
            cassapre.setNatura(NaturaType.N_1);
        }
        cassapre.setRiferimentoAmministrazione("");
        return cassapre;
    }

    public DatiDDTType getDatiDDTType(int riga) {
        DatiDDTType ddttype = OF.createDatiDDTType();
        ddttype.setNumeroDDT(NUMEDOC);
        ddttype.setDataDDT(toXMLGregorianCalendar(DATADOC));
        for (TabMovmag mvm : movmag()) {
            ddttype.getRiferimentoNumeroLinea().add(riga++);
        }
        return ddttype;
    }

    public CondizioniPagamentoType condizioniPagamentoType() {
        return CondizioniPagamentoType.TP_02;
    }

    public List<DettaglioPagamentoType> dettaglioPagamentoType() {
        List<DettaglioPagamentoType> result = new ArrayList<>();
        movpag().forEach(mvp -> {
            DettaglioPagamentoType pagam = OF.createDettaglioPagamentoType();
            pagam.setModalitaPagamento(ModalitaPagamentoType.fromValue(mvp.modopag));
            pagam.setDataScadenzaPagamento(toXMLGregorianCalendar(mvp.datascad));
            pagam.setImportoPagamento(mvp.importo);
            result.add(pagam);
        });
        return result;
    }

}
