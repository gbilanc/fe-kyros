/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gibisoft.kyros2fe.ui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import net.gibisoft.kyros2fe.entities.TabDocume;

/**
 *
 * @author giampaolo
 */
public class DocumeRenderer extends JLabel
        implements ListCellRenderer<TabDocume> {
    protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

    DocumeRenderer() {

    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends TabDocume> jlist, TabDocume item, int index,
            boolean isSelected, boolean cellHasFocus) {
        setText("<html>" + item.toString() + "</html>");
        setOpaque(isSelected);
        setBackground(isSelected ? Color.ORANGE : Color.WHITE);
        return this;
    }

}
