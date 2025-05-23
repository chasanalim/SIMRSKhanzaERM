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
public class WarnaTableRanap extends DefaultTableCellRenderer {
    public int kolom;
    public int jnsbayar= 6,statbayar = 20;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (row % 2 == 1){
            component.setBackground(new Color(255,255,255));
        }else{
            component.setBackground(new Color(255,255,255));
        }
        
        if (table.getValueAt(row, jnsbayar).toString().equals("BPJS KESEHATAN")){
            component.setBackground(new Color(160, 230, 180)); 
        }

        if (table.getValueAt(row, jnsbayar).toString().contains("(Asuransi)")){
            component.setBackground(new Color(224, 252, 130));
        }

        if (table.getValueAt(row, 24).toString().contains("Kelas")){
            component.setBackground(new Color(255,200,100));
            
        }

        if (table.getValueAt(row, 23).toString().contains(".")){
            component.setBackground(new Color(255,153,204));
        }

       
       
        
        if (table.getValueAt(row, 29).toString().equals("Tidak Aman")){
            component.setBackground(new Color(255, 105, 105));
//            component.setForeground(new Color(255,255,255)); // merah pink
        }
        
        if (table.getValueAt(row, statbayar).toString().equals("Sudah Bayar")){
            component.setBackground(new Color(129, 217, 252));
        }
        
//        if (column == 2){
//            component.setBackground(new Color(240, 80, 80));//putih keunguan
////            component.setForeground(new Color(250, 142, 162)); // merah pink
//            if(!table.getValueAt(row,29).toString().equals("Tidak Aman")){
//                component.setBackground(new Color(245,255,250));//putih keijoan
//
//            }
//        }else{
//            component.setForeground(new Color(70,70,70)); // abu2
//        }
        
        return component;
    }

}
