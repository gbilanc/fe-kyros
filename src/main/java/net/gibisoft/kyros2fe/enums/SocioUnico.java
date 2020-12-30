/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gibisoft.kyros2fe.enums;

import fte.xmlv121.SocioUnicoType;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author giampaolo
 */
public enum SocioUnico {
    SU("UNIPERSONALE"),
    SM("PLURIPERSONALE");

    final String descri;

    private SocioUnico(String descri) {
        this.descri = descri;
    }

    public String getDescri() {
        return descri;
    }
    
    public SocioUnicoType getType(){
        return SocioUnicoType.fromValue(this.name());
    }

    @Override
    public String toString() {
        return this.descri;
    }

    public static DefaultComboBoxModel<SocioUnico> getComboBoxModel() {
        return new DefaultComboBoxModel<>(SocioUnico.values());
    }
}
