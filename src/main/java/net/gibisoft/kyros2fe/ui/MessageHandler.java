/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gibisoft.kyros2fe.ui;

import java.util.List;
import net.gibisoft.kyros2fe.entities.TabDocume;

/**
 *
 * @author giampaolo
 */
public interface MessageHandler {

    void showMessage(String message);
    void addItem(TabDocume item);
    void updateList(List<TabDocume> docs);
}
