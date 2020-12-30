/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gibisoft.kyros2fe.enums;

import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author giampaolo
 */
public enum RegimeFiscale {
    RF01("Regime ordinario"),
    RF02("Regime dei contribuenti minimi (art. 1,c.96-117, L. 244/2007)"),
    //  RF03("Regime delle nuove iniziative produttive (art. 13, L. 388/2000)";
    RF04("Agricoltura e attività connesse e pesca (artt. 34 e 34-bis, D.P.R. 633/1972)"),
    RF05("Vendita sali e tabacchi (art. 74, c.1, D.P.R. 633/1972)"),
    RF06("Commercio dei fiammiferi (art. 74, c.1, D.P.R. 633/1972"),
    RF07("Editoria (art. 74, c.1, D.P.R. 633/1972)"),
    RF08("Gestione di servizi di telefonia pubblica (art. 74, c.1, D.P.R. 633/1972)"),
    RF09("Rivendita di documenti di trasporto pubblico e di sosta (art. 74, c.1, D.P.R. 633/1972)"),
    RF10("Intrattenimenti, giochi e altre attività di cui alla tariffa allegata al D.P.R. 640/72 (art. 74, c.6, D.P.R. 633/1972)"),
    RF11("Agenzie di viaggi e turismo (art. 74-ter, D.P.R. 633/1972)"),
    RF12("Agriturismo (art. 5, c.2, L. 413/1991)"),
    RF13("Vendite a domicilio (art. 25-bis, c.6, D.P.R. 600/1973)"),
    RF14("Rivendita di beni usati, di oggetti d’arte, d’antiquariato o da collezione (art.36, D.L. 41/1995)"),
    RF15("Agenzie di vendite all’asta di oggetti d’arte, antiquariato o da collezione (art. 40-bis, D.L. 41/1995)"),
    RF16("IVA per cassa P.A. (art. 6, c.5, D.P.R. 633/1972)"),
    RF17("IVA per cassa (art. 32-bis, D.L. 83/2012)"),
    RF18("Altro"),
    RF19("Regime forfettario");

    final String descri;

    private RegimeFiscale(String descri) {
        this.descri = descri;
    }

    public String getDescri() {
        return descri;
    }

    @Override
    public String toString() {
        return this.descri;
    }

    public static DefaultComboBoxModel<RegimeFiscale> getComboBoxModel() {
        return new DefaultComboBoxModel<>(RegimeFiscale.values());
    }
}
