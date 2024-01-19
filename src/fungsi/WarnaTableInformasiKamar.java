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
public class WarnaTableInformasiKamar extends DefaultTableCellRenderer {
    public int kolom;
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (row % 2 == 1){
            component.setBackground(new Color(245,255,250));
        }else{
            component.setBackground(new Color(255,255,255));
        } 
        if (table.getValueAt(row, 3).toString().contains("Pria")){
            component.setBackground(new Color(195, 240, 195));
        }if (table.getValueAt(row, 3).toString().contains("Wanita")){
            component.setBackground(new Color(195, 240, 195));
        }
        if (table.getValueAt(row, 1).toString().contains("Kosong")){
            component.setBackground(new Color(237, 151, 149));
        }
        return component;
    }

}
