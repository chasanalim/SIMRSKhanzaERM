/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fungsi;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Owner
 */
public class WarnaTableIGD extends DefaultTableCellRenderer {
    public int kolom;
    public int statrawat = 19;
    public int statbayar = 23;
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (row % 2 == 1){
            component.setBackground(new Color(245,255,250));
        }else{
            component.setBackground(new Color(255,255,255));
        } 
        if (table.getValueAt(row, 21).toString().equals("Ranap")){
            component.setBackground(new Color(120, 250, 140));
        }
        return component;
    }

}
