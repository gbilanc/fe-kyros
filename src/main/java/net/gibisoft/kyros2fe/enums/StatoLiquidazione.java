/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gibisoft.kyros2fe.enums;

import fte.xmlv121.StatoLiquidazioneType;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author giampaolo
 */
public enum StatoLiquidazione {
    LS("IN LIQUIDAZIONE"),
    LN("NO LIQUIDAZIONE");

    final String descri;

    private StatoLiquidazione(String descri) {
        this.descri = descri;
    }

    public String getDescri() {
        return descri;
    }

    @Override
    public String toString() {
        return this.descri;
    }

    public StatoLiquidazioneType getType() {
        return StatoLiquidazioneType.fromValue(this.name());
    }

    public static DefaultComboBoxModel<StatoLiquidazione> getComboBoxModel() {
        return new DefaultComboBoxModel<>(StatoLiquidazione.values());
    }
}
