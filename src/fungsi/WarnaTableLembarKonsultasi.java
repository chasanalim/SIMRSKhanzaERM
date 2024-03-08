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
public class WarnaTableLembarKonsultasi extends DefaultTableCellRenderer {
    public int kolom;
    public int jnsbayar= 6,statbayar = 20;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//        if (row % 2 == 1){
//            component.setBackground(new Color(255,255,255));
//        }else{
//            component.setBackground(new Color(255,255,255));
//        }
        
        if (table.getValueAt(row, 12) ==  null || table.getValueAt(row, 13) ==  null){
            component.setBackground(new Color(250, 142, 162)); 
        }

//        else if (table.getValueAt(row, 13).equals("")){
//            component.setBackground(new Color(250, 142, 162));
//        }
        
        else{
            component.setBackground(new Color(160, 230, 180));
        }

        
        
        return component;
    }

}
