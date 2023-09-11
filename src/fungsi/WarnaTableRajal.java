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
public class WarnaTableRajal extends DefaultTableCellRenderer {
    public int kolom;
    public int statrawat = 10;
    public int statbayar = 15;
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (row % 2 == 1){
            component.setBackground(new Color(245,255,250));
        }else{
            component.setBackground(new Color(255,255,255));
        } 
        
        if (table.getValueAt(row, statrawat).toString().equals("Batal")){
            component.setBackground(new Color(155, 155, 155));
        }
        else if (table.getValueAt(row, statbayar).toString().equals("Sudah Bayar")){
            component.setBackground(new Color(130, 215, 250));
        }
        else if (table.getValueAt(row, statrawat).toString().equals("Berkas Terkirim")){
            component.setBackground(new Color(120, 250, 140));
        }
        else if (table.getValueAt(row, statrawat).toString().equals("Berkas Diterima")){
            component.setBackground(new Color(250, 155, 200));
        }
        else if (table.getValueAt(row, statrawat).toString().equals("Sudah")){
            component.setBackground(new Color(220,115,215));
        }
        return component;
    }

}
