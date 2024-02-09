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
    public int aturan;
    public int statrawat = 24;
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (row % 2 == 1){
            component.setBackground(new Color(245,255,250));
        }else{
            component.setBackground(new Color(255,255,255));
        }
        
        if (table.getValueAt(row, 4).toString().equals("BPJS KESEHATAN")){
            component.setBackground(new Color(160, 230, 180)); 
        }
        
        if (table.getValueAt(row, 4).toString().contains("(Asuransi)")){
            component.setBackground(new Color(224, 252, 130));
        }
        
        if (table.getValueAt(row, 24).toString().contains("Tidak")){
            component.setBackground(new Color(250, 142, 162));
        }
        
        if (column == kolom){
            component.setBackground(new Color(210,210,235));
            component.setForeground(new Color(250, 142, 162));
            if(!table.getValueAt(row,kolom).toString().equals("")){
                component.setBackground(new Color(245,255,250));
                component.setForeground(new Color(55,55,175));
            }
        }else if (column == aturan){
            component.setBackground(new Color(210,210,235));
            component.setForeground(new Color(250, 142, 162));
            if(!table.getValueAt(row,aturan).toString().equals("0")){
                component.setBackground(new Color(245,255,250));
                component.setForeground(new Color(55,55,175));
            }
        }else{
            component.setForeground(new Color(70,70,70));
        }
        return component;
    }

}
