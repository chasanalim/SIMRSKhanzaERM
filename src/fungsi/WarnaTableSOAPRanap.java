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
public class WarnaTableSOAPRanap extends DefaultTableCellRenderer {
    public int kolom;
    public int dokter = 8;
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (row % 2 == 1){
            component.setBackground(new Color(225,240,230));
//            component.setForeground(new Color(40,40,40));
        }else{
            component.setBackground(new Color(255,255,255));
//            component.setForeground(new Color(40,40,40));
        } 
        
        if (table.getValueAt(row, dokter).toString().contains("Dokter")){
            component.setBackground(new Color(120, 250, 140));
//            component.setForeground(new Color(245, 240, 240));
        }
        return component;
    }

}
