/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gibisoft.kyros2fe.enums;

import fte.xmlv121.SoggettoEmittenteType;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author giampaolo
 */
public enum SoggettoEmittente {
    CC("CEDENTE/PRESTATORE"),
    TZ("TERZO INTERMEDIARIO");

    final String descri;

    private SoggettoEmittente(String descri) {
        this.descri = descri;
    }

    public String getDescri() {
        return descri;
    }

    @Override
    public String toString() {
        return this.descri;
    }

    public SoggettoEmittenteType getType() {
        return SoggettoEmittenteType.fromValue(this.name());
    }

    public static DefaultComboBoxModel<SoggettoEmittente> getComboBoxModel() {
        return new DefaultComboBoxModel<>(SoggettoEmittente.values());
    }
}
