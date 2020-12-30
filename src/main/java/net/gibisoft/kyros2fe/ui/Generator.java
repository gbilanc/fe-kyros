/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gibisoft.kyros2fe.ui;

import fte.Context;
import fte.xmlv121.DatiPagamentoType;
import fte.xmlv121.DatiTrasmissioneType;
import fte.xmlv121.FatturaElettronicaBodyType;
import fte.xmlv121.FatturaElettronicaType;
import fte.xmlv121.RappresentanteFiscaleType;
import fte.xmlv121.TerzoIntermediarioSoggettoEmittenteType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;
import net.gibisoft.kyros2fe.entities.TabAnagra;
import net.gibisoft.kyros2fe.entities.TabDitta;
import net.gibisoft.kyros2fe.entities.TabDocume;
import net.gibisoft.kyros2fe.entities.TabMoviva;
import net.gibisoft.kyros2fe.entities.TabMovmag;
import static net.gibisoft.kyros2fe.ui.Statics.OF;
import static net.gibisoft.kyros2fe.ui.Statics.getDocumenti;
import org.xml.sax.SAXException;

/**
 *
 * @author giampaolo
 */
public class Generator extends SwingWorker<Void, TabDocume> {

    private final TabDitta ditta1 = new TabDitta();
    private final MessageHandler handler;
    private TabDocume docume1;
    private Marshaller marshaller;
    private Validator validator;
    private final List<TabDocume> lista;
    private final List<TabDocume> result;
    private final boolean nosdipec;

    public Generator(MessageHandler handler,
            LocalDate data1, LocalDate data2,
            Long prog1, Long prog2, boolean nosdipec) {
        try {
            marshaller = Context.getJAXBContext().createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            validator = Context.getXsdSchema().newValidator();
        } catch (JAXBException ex) {
            Logger.getLogger(Generator.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.handler = handler;
        this.nosdipec = nosdipec;
        lista = getDocumenti(data1, data2, prog1, prog2);
        result = new ArrayList<>();
    }

    private DatiTrasmissioneType getDatiTrasmissione(TabAnagra cliente) {
        DatiTrasmissioneType datitrasmissione = OF.createDatiTrasmissioneType();
        datitrasmissione.setIdTrasmittente(OF.createIdFiscaleType());
        datitrasmissione.getIdTrasmittente().setIdPaese(ditta1.IdPaese);
        datitrasmissione.getIdTrasmittente().setIdCodice(ditta1.CodiceFiscale);
        datitrasmissione.setProgressivoInvio(String.format("%05X", docume1.PROGDOC));
        datitrasmissione.setFormatoTrasmissione(cliente.getFormatoTrasmissioneType());
        datitrasmissione.setCodiceDestinatario(cliente.getCodesdi());
        datitrasmissione.setContattiTrasmittente(ditta1.getContattiTrasmittente());
        if (!cliente.getEmailpec().isEmpty()) {
            datitrasmissione.setPECDestinatario(cliente.getEmailpec());
        }
        return datitrasmissione;
    }

    private FatturaElettronicaBodyType getFatturaElettronicaBody() {

        FatturaElettronicaBodyType feBody = OF.createFatturaElettronicaBodyType();
        feBody.setDatiGenerali(OF.createDatiGeneraliType());
        feBody.getDatiGenerali().setDatiGeneraliDocumento(docume1.getDatiGeneraliDocumento());
        feBody.setDatiBeniServizi(OF.createDatiBeniServiziType());
        ArrayList<TabMovmag> movmag = docume1.movmag();
        for (int riga = 0; riga < movmag.size(); riga++) {
            feBody.getDatiBeniServizi().getDettaglioLinee().add(movmag.get(riga).dettaglio(riga + 1));
        }
        ArrayList<TabMoviva> moviva = docume1.moviva();
        moviva.forEach(item -> {
            feBody.getDatiBeniServizi().getDatiRiepilogo().add(item.dettaglio());
        });

        DatiPagamentoType pagamenti = OF.createDatiPagamentoType();
        pagamenti.setCondizioniPagamento(docume1.condizioniPagamentoType());
        pagamenti.getDettaglioPagamento().addAll(docume1.dettaglioPagamentoType());
        feBody.getDatiPagamento().add(pagamenti);
        return feBody;
    }

    private void generaFTE(TabDocume docume) {
        docume1 = docume;
        TabAnagra cliente = docume1.clifor();
        docume.errormsg = cliente.verificaDatiTrasmissione(nosdipec);
        if (!docume.errormsg.equals("OK")) {
            result.add(docume);
            return;
        }
        docume.errormsg = cliente.verificaDatiAnagrafici();
        if (!docume.errormsg.equals("OK")) {
            result.add(docume);
            return;
        }
        FatturaElettronicaType fte = OF.createFatturaElettronicaType();
        fte.setVersione(cliente.getFormatoTrasmissioneType());
        fte.setFatturaElettronicaHeader(OF.createFatturaElettronicaHeaderType());
        fte.getFatturaElettronicaHeader().setDatiTrasmissione(getDatiTrasmissione(cliente));
        fte.getFatturaElettronicaHeader().setCedentePrestatore(ditta1.getCedentePrestatore());
        RappresentanteFiscaleType rf = ditta1.getRappresentanteFiscale();
        if (rf != null) {
            fte.getFatturaElettronicaHeader().setRappresentanteFiscale(rf);
        }
        fte.getFatturaElettronicaHeader().setCessionarioCommittente(cliente.getCessionarioCommittente());
        TerzoIntermediarioSoggettoEmittenteType ti = ditta1.getTerzoIntermediario();
        if (ti != null) {
            fte.getFatturaElettronicaHeader().setTerzoIntermediarioOSoggettoEmittente(ti);
            fte.getFatturaElettronicaHeader().setSoggettoEmittente(ditta1.soggettoEmittenteType());
        }
        fte.getFatturaElettronicaBody().add(getFatturaElettronicaBody());

        String pattern = cliente.IDPAESE.equals("IT") ? "%s%s_%05X.xml" : "%s%s_DF_%05X.xml";
        docume1.filename = String.format(pattern,
                ditta1.IdPaese, ditta1.CodiceFiscale, docume1.PROGDOC);
        if (generaXml(fte, docume1.filename)) {
            result.add(docume);
        } else {
            result.add(docume);
        }

    }

    private boolean generaXml(FatturaElettronicaType fte, String filename) {
        try {
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(fte, stringWriter);
            if (verifica(stringWriter.toString())) {
                File file = new File(Statics.getInstance().PATHXML, filename);
                try {
                    try ( Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8")) {
//                String escapedXml = escapeXml(stringWriter.toString());
                        writer.write(stringWriter.toString());
                    }
                    return true;
                } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                    docume1.errormsg = ex.toString();
                    return false;
                } catch (IOException ex) {
                    docume1.errormsg = ex.toString();
                    return false;
                }
            } else {// verifica xsd fallita;
                return false;
            }
        } catch (JAXBException ex) {
            docume1.errormsg = ex.toString();
            return false;
        }

    }

    private boolean verifica(String xmlString) {
        try {
            StringReader reader = new StringReader(xmlString);
            validator.validate(new StreamSource(reader));
            return true;
        } catch (SAXException | IOException ex) {
            docume1.errormsg = ex.toString();
            return false;
        }
    }

    @Override
    protected Void doInBackground() throws Exception {
        lista.forEach((docume) -> {
            generaFTE(docume);
            publish(docume);
        });
        return null;
    }

    @Override
    protected void process(List<TabDocume> docs) {
        handler.updateList(result);
//        handler.addItem(docs.get(0));
    }
    
    @Override
    protected void done() {
        handler.showMessage(Statics.getMessage("M012"));
    }

}
