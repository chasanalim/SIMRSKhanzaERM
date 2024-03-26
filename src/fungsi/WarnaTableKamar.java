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
public class WarnaTableKamar extends DefaultTableCellRenderer {
    public int kolom;
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (row % 2 == 1){
            component.setBackground(new Color(245,255,250));
        }else{
            component.setBackground(new Color(255,255,255));
        } 
        if (table.getValueAt(row, 6).toString().equals("ISI")){
            component.setBackground(new Color(195, 240, 195));
        }
//        if (table.getValueAt(row, 6).toString().equals("ISI")&&table.getValueAt(row, 7).toString().contains(",")){
//            component.setBackground(new Color(250, 142, 162));
//        }
        return component;
    }

}
