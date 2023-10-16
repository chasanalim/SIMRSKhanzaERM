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
public class WarnaPerkiraanRanap extends DefaultTableCellRenderer {
    public int kolom;
    public int statrawat = 24;
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (row % 2 == 1){
            component.setBackground(new Color(245,255,250));
        }else{
            component.setBackground(new Color(255,255,255));
        } 
        
        if (table.getValueAt(row, 23).toString().contains("Tidak")){
            component.setBackground(new Color(247, 143, 136));
        }
        return component;
    }

}
