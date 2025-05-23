/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DlgResepObat.java
 *
 * Created on 31 Mei 10, 11:27:40
 */

package inventory;
import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import simrskhanza.DlgInputResepPulang;


/**
 *
 * @author perpustakaan
 */
public final class DlgResepPulang extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private PreparedStatement ps,ps2,psbarang,psracikan;
    private ResultSet rs,rs2,rsracikan;
    public DlgInputResepPulang inputresep=new DlgInputResepPulang(null,false);
    private double jumlahtotal=0, total=0;
    private riwayatobat Trackobat=new riwayatobat();
    private String aktifkanbatch="no",bangsal="",lokasistok="",rincianobat="",finger="" ;
    private int i=0;

    /** Creates new form DlgResepObat 
     *@param parent
     *@param modal*/
    public DlgResepPulang(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(8,1);
        setSize(628,674);        
        tabMode=new DefaultTableModel(null,new Object[]{
            "No.Rawat","Tgl.Resep","Jam","Pasien","Obat","Jml","Harga","Total","Dosis","No.Batch","No.Faktur"}){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
             Class[] types = new Class[] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                java.lang.Object.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, 
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbResep.setModel(tabMode);

        tbResep.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbResep.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 11; i++) {
            TableColumn column = tbResep.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(110);
            }else if(i==1){
                column.setPreferredWidth(70);
            }else if(i==2){
                column.setPreferredWidth(60);
            }else if(i==3){
                column.setPreferredWidth(180);
            }else if(i==4){
                column.setPreferredWidth(230);
            }else if(i==5){
                column.setPreferredWidth(30);
            }else if(i==6){
                column.setPreferredWidth(70);
            }else if(i==7){
                column.setPreferredWidth(80);
            }else if(i==8){
                column.setPreferredWidth(100);
            }else if(i==9){
                column.setPreferredWidth(70);
            }else if(i==10){
                column.setPreferredWidth(100);
            }
        }
        tbResep.setDefaultRenderer(Object.class, new WarnaTable());

        TNoRw.setDocument(new batasInput((byte)17).getKata(TNoRw));
        KdBarang.setDocument(new batasInput((byte)15).getKata(KdBarang));
        TCari.setDocument(new batasInput((byte)100).getKata(TCari));
        if(koneksiDB.CARICEPAT().equals("aktif")){
            TCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
            });
        }  
        ChkInput.setSelected(false);
        isForm();
        
        inputresep.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(inputresep.getTable().getSelectedRow()!= -1){                   
                    KdBarang.setText(inputresep.getTable().getValueAt(inputresep.getTable().getSelectedRow(),1).toString());
                    NmBarang.setText(inputresep.getTable().getValueAt(inputresep.getTable().getSelectedRow(),2).toString());
                    Satuan.setText(inputresep.getTable().getValueAt(inputresep.getTable().getSelectedRow(),3).toString());
                    tampil();
                }    
                KdBarang.requestFocus();
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        try {
            aktifkanbatch = koneksiDB.AKTIFKANBATCHOBAT();
        } catch (Exception e) {
            System.out.println("E : "+e);
            aktifkanbatch = "no";
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        ppResepObat = new javax.swing.JMenuItem();
        ppResepObat1 = new javax.swing.JMenuItem();
        ppResepObat2 = new javax.swing.JMenuItem();
        ppLabelDataObat = new javax.swing.JMenuItem();
        ppLembarObat1 = new javax.swing.JMenuItem();
        internalFrame1 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbResep = new widget.Table();
        jPanel3 = new javax.swing.JPanel();
        panelGlass8 = new widget.panelisi();
        BtnHapus = new widget.Button();
        BtnPrint = new widget.Button();
        BtnAll = new widget.Button();
        jLabel7 = new widget.Label();
        LCount = new widget.Label();
        jLabel8 = new widget.Label();
        LCount1 = new widget.Label();
        BtnKeluar = new widget.Button();
        panelGlass9 = new widget.panelisi();
        jLabel19 = new widget.Label();
        DTPCari1 = new widget.Tanggal();
        jLabel21 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        jLabel6 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        PanelInput = new javax.swing.JPanel();
        FormInput = new widget.PanelBiasa();
        TNoRw = new widget.TextBox();
        TPasien = new widget.TextBox();
        KdBarang = new widget.TextBox();
        NmBarang = new widget.TextBox();
        jLabel3 = new widget.Label();
        jLabel13 = new widget.Label();
        btnBarang = new widget.Button();
        Satuan = new widget.TextBox();
        TNoRM = new widget.TextBox();
        ChkInput = new widget.CekBox();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        ppResepObat.setBackground(new java.awt.Color(255, 255, 254));
        ppResepObat.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppResepObat.setForeground(new java.awt.Color(50, 50, 50));
        ppResepObat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppResepObat.setText("Cetak Aturan Pakai Model 1");
        ppResepObat.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppResepObat.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppResepObat.setName("ppResepObat"); // NOI18N
        ppResepObat.setPreferredSize(new java.awt.Dimension(190, 25));
        ppResepObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppResepObatActionPerformed(evt);
            }
        });
        jPopupMenu1.add(ppResepObat);

        ppResepObat1.setBackground(new java.awt.Color(255, 255, 254));
        ppResepObat1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppResepObat1.setForeground(new java.awt.Color(50, 50, 50));
        ppResepObat1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppResepObat1.setText("Cetak Aturan Pakai Model 2");
        ppResepObat1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppResepObat1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppResepObat1.setName("ppResepObat1"); // NOI18N
        ppResepObat1.setPreferredSize(new java.awt.Dimension(190, 25));
        ppResepObat1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppResepObat1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(ppResepObat1);

        ppResepObat2.setBackground(new java.awt.Color(255, 255, 254));
        ppResepObat2.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppResepObat2.setForeground(new java.awt.Color(50, 50, 50));
        ppResepObat2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppResepObat2.setText("Cetak Aturan Pakai Model 3");
        ppResepObat2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppResepObat2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppResepObat2.setName("ppResepObat2"); // NOI18N
        ppResepObat2.setPreferredSize(new java.awt.Dimension(190, 25));
        ppResepObat2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppResepObat2ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(ppResepObat2);

        ppLabelDataObat.setBackground(new java.awt.Color(255, 255, 254));
        ppLabelDataObat.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppLabelDataObat.setForeground(new java.awt.Color(50, 50, 50));
        ppLabelDataObat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppLabelDataObat.setText("Label Daftar Obat");
        ppLabelDataObat.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppLabelDataObat.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppLabelDataObat.setName("ppLabelDataObat"); // NOI18N
        ppLabelDataObat.setPreferredSize(new java.awt.Dimension(225, 25));
        ppLabelDataObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppLabelDataObatActionPerformed(evt);
            }
        });
        jPopupMenu1.add(ppLabelDataObat);

        ppLembarObat1.setBackground(new java.awt.Color(255, 255, 254));
        ppLembarObat1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppLembarObat1.setForeground(new java.awt.Color(50, 50, 50));
        ppLembarObat1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppLembarObat1.setText("Lembar Pemberian Obat 3");
        ppLembarObat1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppLembarObat1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppLembarObat1.setName("ppLembarObat1"); // NOI18N
        ppLembarObat1.setPreferredSize(new java.awt.Dimension(225, 25));
        ppLembarObat1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppLembarObat1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(ppLembarObat1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Resep Pulang ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setComponentPopupMenu(jPopupMenu1);
        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        tbResep.setAutoCreateRowSorter(true);
        tbResep.setComponentPopupMenu(jPopupMenu1);
        tbResep.setName("tbResep"); // NOI18N
        tbResep.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbResepMouseClicked(evt);
            }
        });
        tbResep.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbResepKeyPressed(evt);
            }
        });
        Scroll.setViewportView(tbResep);

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(44, 100));
        jPanel3.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        BtnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        BtnHapus.setMnemonic('H');
        BtnHapus.setText("Hapus");
        BtnHapus.setToolTipText("Alt+H");
        BtnHapus.setName("BtnHapus"); // NOI18N
        BtnHapus.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnHapusActionPerformed(evt);
            }
        });
        BtnHapus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnHapusKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnHapus);

        BtnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        BtnPrint.setMnemonic('T');
        BtnPrint.setText("Cetak");
        BtnPrint.setToolTipText("Alt+T");
        BtnPrint.setName("BtnPrint"); // NOI18N
        BtnPrint.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrintActionPerformed(evt);
            }
        });
        BtnPrint.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPrintKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnPrint);

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('M');
        BtnAll.setText("Semua");
        BtnAll.setToolTipText("Alt+M");
        BtnAll.setName("BtnAll"); // NOI18N
        BtnAll.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllActionPerformed(evt);
            }
        });
        BtnAll.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnAll);

        jLabel7.setText("Record :");
        jLabel7.setName("jLabel7"); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(55, 30));
        panelGlass8.add(jLabel7);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(52, 30));
        panelGlass8.add(LCount);

        jLabel8.setText("Jml. Total :");
        jLabel8.setName("jLabel8"); // NOI18N
        jLabel8.setPreferredSize(new java.awt.Dimension(60, 30));
        panelGlass8.add(jLabel8);

        LCount1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount1.setText("0");
        LCount1.setName("LCount1"); // NOI18N
        LCount1.setPreferredSize(new java.awt.Dimension(120, 30));
        panelGlass8.add(LCount1);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('K');
        BtnKeluar.setText("Keluar");
        BtnKeluar.setToolTipText("Alt+K");
        BtnKeluar.setName("BtnKeluar"); // NOI18N
        BtnKeluar.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluarActionPerformed(evt);
            }
        });
        BtnKeluar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluarKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnKeluar);

        jPanel3.add(panelGlass8, java.awt.BorderLayout.CENTER);

        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel19.setText("Tgl.Resep :");
        jLabel19.setName("jLabel19"); // NOI18N
        jLabel19.setPreferredSize(new java.awt.Dimension(67, 23));
        panelGlass9.add(jLabel19);

        DTPCari1.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "17-05-2023" }));
        DTPCari1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1.setName("DTPCari1"); // NOI18N
        DTPCari1.setOpaque(false);
        DTPCari1.setPreferredSize(new java.awt.Dimension(95, 23));
        panelGlass9.add(DTPCari1);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("s.d.");
        jLabel21.setName("jLabel21"); // NOI18N
        jLabel21.setPreferredSize(new java.awt.Dimension(23, 23));
        panelGlass9.add(jLabel21);

        DTPCari2.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "17-05-2023" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setName("DTPCari2"); // NOI18N
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(95, 23));
        panelGlass9.add(DTPCari2);

        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass9.add(jLabel6);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(300, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass9.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('4');
        BtnCari.setToolTipText("Alt+4");
        BtnCari.setName("BtnCari"); // NOI18N
        BtnCari.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariActionPerformed(evt);
            }
        });
        BtnCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariKeyPressed(evt);
            }
        });
        panelGlass9.add(BtnCari);

        jPanel3.add(panelGlass9, java.awt.BorderLayout.PAGE_START);

        internalFrame1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setPreferredSize(new java.awt.Dimension(400, 96));
        PanelInput.setLayout(new java.awt.BorderLayout(1, 1));

        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(440, 107));
        FormInput.setLayout(null);

        TNoRw.setHighlighter(null);
        TNoRw.setName("TNoRw"); // NOI18N
        TNoRw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRwKeyPressed(evt);
            }
        });
        FormInput.add(TNoRw);
        TNoRw.setBounds(98, 10, 120, 23);

        TPasien.setEditable(false);
        TPasien.setHighlighter(null);
        TPasien.setName("TPasien"); // NOI18N
        FormInput.add(TPasien);
        TPasien.setBounds(313, 10, 410, 23);

        KdBarang.setEditable(false);
        KdBarang.setHighlighter(null);
        KdBarang.setName("KdBarang"); // NOI18N
        FormInput.add(KdBarang);
        KdBarang.setBounds(98, 40, 120, 23);

        NmBarang.setEditable(false);
        NmBarang.setHighlighter(null);
        NmBarang.setName("NmBarang"); // NOI18N
        FormInput.add(NmBarang);
        NmBarang.setBounds(220, 40, 351, 23);

        jLabel3.setText("No.Rawat :");
        jLabel3.setName("jLabel3"); // NOI18N
        FormInput.add(jLabel3);
        jLabel3.setBounds(0, 10, 95, 23);

        jLabel13.setText("Obat/Barang :");
        jLabel13.setName("jLabel13"); // NOI18N
        FormInput.add(jLabel13);
        jLabel13.setBounds(0, 40, 95, 23);

        btnBarang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnBarang.setMnemonic('3');
        btnBarang.setToolTipText("Alt+3");
        btnBarang.setName("btnBarang"); // NOI18N
        btnBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBarangActionPerformed(evt);
            }
        });
        btnBarang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnBarangKeyPressed(evt);
            }
        });
        FormInput.add(btnBarang);
        btnBarang.setBounds(695, 40, 28, 23);

        Satuan.setEditable(false);
        Satuan.setHighlighter(null);
        Satuan.setName("Satuan"); // NOI18N
        FormInput.add(Satuan);
        Satuan.setBounds(573, 40, 120, 23);

        TNoRM.setEditable(false);
        TNoRM.setHighlighter(null);
        TNoRM.setName("TNoRM"); // NOI18N
        FormInput.add(TNoRM);
        TNoRM.setBounds(220, 10, 91, 23);

        PanelInput.add(FormInput, java.awt.BorderLayout.CENTER);

        ChkInput.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setMnemonic('I');
        ChkInput.setText(".: Input Data");
        ChkInput.setToolTipText("Alt+I");
        ChkInput.setBorderPainted(true);
        ChkInput.setBorderPaintedFlat(true);
        ChkInput.setFocusable(false);
        ChkInput.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkInput.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkInput.setName("ChkInput"); // NOI18N
        ChkInput.setPreferredSize(new java.awt.Dimension(192, 20));
        ChkInput.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkInputActionPerformed(evt);
            }
        });
        PanelInput.add(ChkInput, java.awt.BorderLayout.PAGE_END);

        internalFrame1.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        if(tabMode.getRowCount()==0){
             JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
             TNoRw.requestFocus();
        }else if(TPasien.getText().trim().equals("")){
             JOptionPane.showMessageDialog(null,"Maaf, Gagal menghapus. Pilih dulu data yang mau dihapus.\nKlik data pada table untuk memilih...!!!!");
        }else if(!(TPasien.getText().trim().equals(""))){
            if(Sequel.cariRegistrasi(TNoRw.getText())>0){
                JOptionPane.showMessageDialog(rootPane,"Data billing sudah terverifikasi, data tidak boleh dihapus.\nSilahkan hubungi bagian kasir/keuangan ..!!");
                TCari.requestFocus();
            }else{
                i=tbResep.getSelectedRow();
                if(Sequel.queryu2tf("delete from resep_pulang where no_rawat=? and kode_brng=? and no_batch=? and no_faktur=? and tanggal=? and jam=?",6,new String[]{
                    tbResep.getValueAt(i,0).toString(),KdBarang.getText(),tbResep.getValueAt(i,9).toString(),tbResep.getValueAt(i,10).toString(),tbResep.getValueAt(i,1).toString(),tbResep.getValueAt(i,2).toString()
                })==true){
                    if(aktifkanbatch.equals("yes")){
                        Sequel.mengedit3("data_batch","no_batch=? and kode_brng=? and no_faktur=?","sisa=sisa+?",4,new String[]{
                            ""+tbResep.getValueAt(i,5).toString(),tabMode.getValueAt(i,9).toString(),KdBarang.getText(),tabMode.getValueAt(i,10).toString()
                        });
                        Trackobat.catatRiwayat(KdBarang.getText(),Valid.SetAngka(tbResep.getValueAt(i,5).toString()),0,"Resep Pulang",akses.getkode(),akses.getkdbangsal(),"Hapus",tbResep.getValueAt(i,9).toString(),tbResep.getValueAt(i,10).toString(),TNoRw.getText()+" "+TPasien.getText());
                        Sequel.menyimpan("gudangbarang","'"+KdBarang.getText()+"','"+akses.getkdbangsal()+"','"+tbResep.getValueAt(i,5).toString()+"','"+tbResep.getValueAt(i,9).toString()+"','"+tbResep.getValueAt(i,10).toString()+"'", 
                                     "stok=stok+'"+tbResep.getValueAt(i,5).toString()+"'","kode_brng='"+KdBarang.getText()+"' and kd_bangsal='"+akses.getkdbangsal()+"' and no_batch='"+tbResep.getValueAt(i,9).toString()+"' and no_faktur='"+tbResep.getValueAt(i,10).toString()+"'");    
                    }else{
                        Trackobat.catatRiwayat(KdBarang.getText(),Valid.SetAngka(tbResep.getValueAt(i,5).toString()),0,"Resep Pulang",akses.getkode(),akses.getkdbangsal(),"Hapus","","",TNoRw.getText()+" "+TPasien.getText());
                        Sequel.menyimpan("gudangbarang","'"+KdBarang.getText()+"','"+akses.getkdbangsal()+"','"+tbResep.getValueAt(i,5).toString()+"','',''", 
                                     "stok=stok+'"+tbResep.getValueAt(i,5).toString()+"'","kode_brng='"+KdBarang.getText()+"' and kd_bangsal='"+akses.getkdbangsal()+"' and no_batch='' and no_faktur=''");    
                    }
                    tampil();
                }
            }                
        }

}//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnHapusActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnCari,BtnPrint);
        }
}//GEN-LAST:event_BtnHapusKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            dispose();
        }else{Valid.pindah(evt,BtnPrint,TCari);}
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        BtnCariActionPerformed(evt);
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
            TCari.requestFocus();
        }else if(tabMode.getRowCount()!=0){
            Sequel.queryu("delete from temporary where temp37='"+akses.getalamatip()+"'");
            
            for(int i=0;i<tabMode.getRowCount();i++){  
                Sequel.menyimpan("temporary","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?",38,new String[]{
                    ""+i,tabMode.getValueAt(i,0).toString(),tabMode.getValueAt(i,1).toString(),tabMode.getValueAt(i,2).toString(),
                    tabMode.getValueAt(i,3).toString(),tabMode.getValueAt(i,4).toString(),tabMode.getValueAt(i,5).toString(),
                    Valid.SetAngka(Double.parseDouble(tabMode.getValueAt(i,6).toString())),Valid.SetAngka(Double.parseDouble(tabMode.getValueAt(i,7).toString())),
                    tabMode.getValueAt(i,8).toString(),tabMode.getValueAt(i,9).toString(),tabMode.getValueAt(i,10).toString(),"","","","","","","","","","",
                    "","","","","","","","","","","","","","","",akses.getalamatip()
                });
            }
            
            
            Map<String, Object> param = new HashMap<>();  
                param.put("namars",akses.getnamars());
                param.put("alamatrs",akses.getalamatrs());
                param.put("kotars",akses.getkabupatenrs());
                param.put("propinsirs",akses.getpropinsirs());
                param.put("kontakrs",akses.getkontakrs());
                param.put("emailrs",akses.getemailrs());   
                param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
            Valid.MyReportqry("rptResepPulang.jasper","report","::[ Data Resep Pulang ]::","select * from temporary where temporary.temp37='"+akses.getalamatip()+"' order by temporary.no",param);
        }
        this.setCursor(Cursor.getDefaultCursor());
}//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnPrintActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnHapus, BtnKeluar);
        }
}//GEN-LAST:event_BtnPrintKeyPressed

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
         if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCariActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCari.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar.requestFocus();
        }
}//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        tampil();
}//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCariActionPerformed(null);
        }else{
            Valid.pindah(evt, TCari, BtnAll);
        }
}//GEN-LAST:event_BtnCariKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        tampil();        
}//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            tampil();
            TCari.setText("");
        }else{
            Valid.pindah(evt, BtnCari, NmBarang);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void tbResepMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbResepMouseClicked
        if(tabMode.getRowCount()!=0){
            try {
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
}//GEN-LAST:event_tbResepMouseClicked

    private void tbResepKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbResepKeyPressed
        if(tabMode.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
}//GEN-LAST:event_tbResepKeyPressed

private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
  isForm();                
}//GEN-LAST:event_ChkInputActionPerformed

    private void btnBarangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnBarangKeyPressed
        Valid.pindah(evt,KdBarang,BtnCari);
    }//GEN-LAST:event_btnBarangKeyPressed

    private void btnBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBarangActionPerformed
        if(Sequel.cariRegistrasi(TNoRw.getText())>0){
                JOptionPane.showMessageDialog(rootPane,"Data billing sudah terverifikasi, data tidak boleh dihapus.\nSilahkan hubungi bagian kasir/keuangan ..!!");
                TCari.requestFocus();
        }else{
            bangsal=Sequel.cariIsi(
                "select bangsal.kd_bangsal from bangsal inner join kamar inner join kamar_inap "+
                "on bangsal.kd_bangsal=kamar.kd_bangsal and kamar.kd_kamar=kamar_inap.kd_kamar "+
                "where kamar_inap.no_rawat=? and kamar_inap.stts_pulang='-' order by STR_TO_DATE(concat(kamar_inap.tgl_masuk,' ',jam_masuk),'%Y-%m-%d %H:%i:%s') desc limit 1",TNoRw.getText());
            lokasistok=Sequel.cariIsi("select kd_depo from set_depo_ranap where kd_bangsal=?",Sequel.cariIsi("select kd_bangsal from kamar where kd_kamar=?",bangsal));
            if(lokasistok.equals("")){
                if(Sequel.cariIsi("select asal_stok from set_lokasi").equals("Gunakan Stok Bangsal")){
                    lokasistok=bangsal;
                }else{
                    lokasistok=Sequel.cariIsi("select kd_bangsal from set_lokasi");
                }
            }
            akses.setkdbangsal(lokasistok);
            inputresep.setNoRm(TNoRw.getText(),TNoRM.getText(),TPasien.getText(),"-",DTPCari1.getSelectedItem().toString(),Sequel.cariIsi("select current_time()"));
            inputresep.tampil();
            inputresep.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
            inputresep.setLocationRelativeTo(internalFrame1);
            inputresep.setVisible(true);
        }
    }//GEN-LAST:event_btnBarangActionPerformed

    private void TNoRwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRwKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            Sequel.cariIsi("select reg_periksa.no_rkm_medis from reg_periksa where reg_periksa.no_rawat=? ",TNoRM,TNoRw.getText());
            Sequel.cariIsi("select pasien.nm_pasien from pasien pasien.no_rkm_medis=? ",TPasien,TNoRM.getText());
        }else if(evt.getKeyCode()==KeyEvent.VK_DOWN){
            TCari.requestFocus();
        }else{
            Valid.pindah(evt,TCari,btnBarang);
        }
    }//GEN-LAST:event_TNoRwKeyPressed

    private void ppResepObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppResepObatActionPerformed
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
            TCari.requestFocus();
        }else if(tbResep.getSelectedRow()<= -1){
            JOptionPane.showMessageDialog(null,"Maaf, Silahkan pilih data..!!");
        }else{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            Map<String, Object> param = new HashMap<>();  
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());
            param.put("penanggung",Sequel.cariIsi("select png_jawab from penjab where kd_pj=?",Sequel.cariIsi("select kd_pj from reg_periksa where no_rawat=?",TNoRw.getText())));
            param.put("peresep",Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?",Sequel.cariIsi("select kd_dokter from permintaan_resep_pulang where no_rawat=?",TNoRw.getText())));
            param.put("noresep",Sequel.cariIsi("select no_permintaan from permintaan_resep_pulang where no_rawat=?",TNoRw.getText()));
            

            if(Sequel.cariInteger(
                    "select count(*) from resep_pulang where resep_pulang.no_rawat=? and resep_pulang.dosis<>''",TNoRw.getText())>0){
                param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
                Valid.MyReportqry("rptItemResepPulang.jasper","report","::[ Aturan Pakai Obat ]::",
                    "SELECT resep_pulang.tanggal,permintaan_resep_pulang.no_permintaan,resep_pulang.no_rawat,permintaan_resep_pulang.tgl_permintaan, "+
                    "permintaan_resep_pulang.jam,reg_periksa.no_rkm_medis,pasien.nm_pasien, "+
                    "pasien.no_ktp,pasien.jk,pasien.tgl_lahir,reg_periksa.umurdaftar,reg_periksa.sttsumur,databarang.nama_brng, "+
                    "resep_pulang.dosis,resep_pulang.jml_barang,kodesatuan.satuan "+
                    "FROM resep_pulang INNER JOIN reg_periksa ON resep_pulang.no_rawat = reg_periksa.no_rawat "+
                    "INNER JOIN databarang ON resep_pulang.kode_brng = databarang.kode_brng INNER JOIN kodesatuan ON databarang.kode_sat = kodesatuan.kode_sat "+
                    "INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis INNER JOIN permintaan_resep_pulang ON permintaan_resep_pulang.no_rawat = resep_pulang.no_rawat "+
                    "where resep_pulang.no_rawat='"+TNoRw.getText()+"' and resep_pulang.dosis<>''",param);
            }  
            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_ppResepObatActionPerformed

    private void ppResepObat1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppResepObat1ActionPerformed
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
            TCari.requestFocus();
        }else if(tbResep.getSelectedRow()<= -1){
            JOptionPane.showMessageDialog(null,"Maaf, Silahkan pilih data..!!");
        }else{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            Map<String, Object> param = new HashMap<>();
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());
            if(Sequel.cariInteger(
                    "select count(*) from resep_pulang where resep_pulang.no_rawat=? and resep_pulang.dosis<>''",TNoRw.getText())>0){
                param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
                Valid.MyReportqry("rptItemResepPulang2.jasper","report","::[ Aturan Pakai Obat ]::",
                    "select resep_pulang.no_rawat,resep_pulang.tanggal,jenis.nama,"+
                    "reg_periksa.no_rkm_medis,pasien.nm_pasien,databarang.nama_brng,"+
                    "resep_pulang.dosis,resep_pulang.jml_barang,kodesatuan.satuan "+
                    "from resep_pulang inner join reg_periksa on resep_pulang.no_rawat=reg_periksa.no_rawat "+
                    "inner join databarang on resep_pulang.kode_brng=databarang.kode_brng "+
                    "inner join kodesatuan on databarang.kode_sat=kodesatuan.kode_sat "+
                    "inner join jenis on databarang.kdjns= jenis.kdjns "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "where resep_pulang.no_rawat='"+TNoRw.getText()+"' and resep_pulang.dosis<>''",param);
            } 
            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_ppResepObat1ActionPerformed

    private void ppResepObat2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppResepObat2ActionPerformed
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
            TCari.requestFocus();
        }else if(tbResep.getSelectedRow()<= -1){
            JOptionPane.showMessageDialog(null,"Maaf, Silahkan pilih data..!!");
        }else{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            Map<String, Object> param = new HashMap<>();
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());
            if(Sequel.cariInteger(
                    "select count(*) from resep_pulang where resep_pulang.no_rawat=? and resep_pulang.dosis<>''",TNoRw.getText())>0){
                param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
                Valid.MyReportqry("rptItemResepPulang3.jasper","report","::[ Aturan Pakai Obat ]::",
                    "select resep_pulang.no_rawat,resep_pulang.tanggal, "+
                    "reg_periksa.no_rkm_medis,pasien.nm_pasien,databarang.nama_brng,"+
                    "resep_pulang.dosis,resep_pulang.jml_barang,kodesatuan.satuan "+
                    "from resep_pulang inner join reg_periksa on resep_pulang.no_rawat=reg_periksa.no_rawat "+
                    "inner join databarang on resep_pulang.kode_brng=databarang.kode_brng "+
                    "inner join kodesatuan on databarang.kode_sat=kodesatuan.kode_sat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "where resep_pulang.no_rawat='"+TNoRw.getText()+"' and resep_pulang.dosis<>''",param);
            } 
            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_ppResepObat2ActionPerformed

    private void ppLabelDataObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppLabelDataObatActionPerformed
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
            TNoRw.requestFocus();
        }else if(TPasien.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, Klik No Resep untuk mencetak aturan pakai...!!!!");
        }else if(!(TPasien.getText().trim().equals(""))){
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            Sequel.queryu("delete from temporary_resep where temp37='"+akses.getalamatip()+"'");
            try {
                i=0;
                ps=koneksi.prepareStatement(
                    "select resep_pulang.tanggal,resep_pulang.jam,databarang.kode_sat,resep_pulang.kode_brng, "+
                    "resep_pulang.jml_barang,resep_pulang.total,databarang.nama_brng,resep_pulang.no_rawat,resep_pulang.dosis "+
                    "FROM resep_pulang INNER JOIN databarang ON resep_pulang.kode_brng = databarang.kode_brng "+
                    "where resep_pulang.no_rawat=?");
                try {
                    ps.setString(1,TNoRw.getText());
                    rs=ps.executeQuery();
                    while(rs.next()){
                        Sequel.menyimpan("temporary_resep","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?",38,new String[]{
                            ""+i,rs.getString("nama_brng"),rs.getString("dosis"),rs.getString("jml_barang"),Valid.SetAngka(rs.getDouble("total")),"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",akses.getalamatip()
                        });
                        i++;
                    }
                } catch (Exception e) {
                    System.out.println("Notif 2 : "+e);
                } finally{
                    if(rs!=null){
                        rs.close();
                    }
                    if(ps!=null){
                        ps.close();
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            }

            Map<String, Object> param = new HashMap<>();
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("emailrs",akses.getemailrs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("penanggung",Sequel.cariIsi("select png_jawab from penjab where kd_pj=?",Sequel.cariIsi("select kd_pj from reg_periksa where no_rawat=?",TNoRw.getText())));
            param.put("propinsirs",akses.getpropinsirs());
            param.put("tanggal",Sequel.cariIsi("select tgl_permintaan from permintaan_resep_pulang where no_rawat=?",TNoRw.getText()));
            param.put("norawat",TNoRw.getText());
            param.put("pasien",TPasien.getText());
            param.put("norm",TNoRM.getText());
            param.put("peresep",Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?",Sequel.cariIsi("select kd_dokter from permintaan_resep_pulang where no_rawat=?",TNoRw.getText())));
            param.put("noresep",Sequel.cariIsi("select no_permintaan from permintaan_resep_pulang where no_rawat=?",TNoRw.getText()));
            param.put("poli",Sequel.cariIsi("select poliklinik.nm_poli from poliklinik where poliklinik.kd_poli=?",Sequel.cariIsi("select kd_poli from reg_periksa where no_rawat=?",TNoRw.getText())));
//            param.put("jam",cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem());
            param.put("logo",Sequel.cariGambar("select setting.logo from setting"));

            Valid.MyReportqry("rptLabelDaftarObat.jasper","report","::[ Label Daftar Obat ]::","select * from temporary_resep where temporary_resep.temp37='"+akses.getalamatip()+"' order by temporary_resep.no",param);
            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_ppLabelDataObatActionPerformed

    private void ppLembarObat1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppLembarObat1ActionPerformed
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
            TNoRw.requestFocus();
        }else if(TPasien.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, Klik No Resep untuk mencetak aturan pakai...!!!!");
        }else if(!(TPasien.getText().trim().equals(""))){
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            Sequel.queryu("delete from temporary_resep where temp37='"+akses.getalamatip()+"'");
            try {
                i=0;
                ps=koneksi.prepareStatement(
                    "SELECT databarang.nama_brng,resep_pulang.dosis,resep_pulang.jml_barang,kodesatuan.satuan "+
                    "FROM permintaan_resep_pulang INNER JOIN reg_periksa INNER JOIN databarang "+
                    "INNER JOIN resep_pulang INNER JOIN kodesatuan ON permintaan_resep_pulang.no_rawat = reg_periksa.no_rawat "+
                    "AND resep_pulang.kode_brng = databarang.kode_brng AND permintaan_resep_pulang.no_rawat = resep_pulang.no_rawat "+
                    "AND kodesatuan.kode_sat = databarang.kode_sat "+
                    "where permintaan_resep_pulang.no_rawat=? ");
                try {
                    ps.setString(1,TNoRw.getText());
                    rs=ps.executeQuery();
                    while(rs.next()){
                        Sequel.menyimpan("temporary_resep","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?",38,new String[]{
                            ""+i,rs.getString("nama_brng"),rs.getString("dosis"),rs.getString("jml_barang"),rs.getString("satuan"),"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",akses.getalamatip()
                        });
                        i++;
                    }
                } catch (Exception e) {
                    System.out.println("Notif 1 : "+e);
                } finally{
                    if(rs!=null){
                        rs.close();
                    }
                    if(ps!=null){
                        ps.close();
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            }

            Map<String, Object> param = new HashMap<>();
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("emailrs",akses.getemailrs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("poli",Sequel.cariIsi("select poliklinik.nm_poli from poliklinik inner join reg_periksa on poliklinik.kd_poli= reg_periksa.kd_poli where reg_periksa.no_rawat=?",Sequel.cariIsi("select no_rawat from reg_periksa where no_rawat=?",TNoRw.getText())));
            param.put("tgl_lahir",Sequel.cariIsi("select pasien.tgl_lahir from pasien inner join reg_periksa on pasien.no_rkm_medis=reg_periksa.no_rkm_medis where reg_periksa.no_rawat=?",Sequel.cariIsi("select no_rawat from reg_periksa where no_rawat=?",TNoRw.getText())));
            param.put("no_ktp",Sequel.cariIsi("select pasien.no_ktp from pasien inner join reg_periksa on pasien.no_rkm_medis=reg_periksa.no_rkm_medis where reg_periksa.no_rawat=?",Sequel.cariIsi("select no_rawat from reg_periksa where no_rawat=?",TNoRw.getText())));
            param.put("penanggung",Sequel.cariIsi("select penjab.png_jawab from penjab where penjab.kd_pj=?",Sequel.cariIsi("select reg_periksa.kd_pj from reg_periksa where reg_periksa.no_rawat=?",TNoRw.getText())));
            param.put("propinsirs",akses.getpropinsirs());
            param.put("tanggal",Sequel.cariIsi("select tgl_permintaan from permintaan_resep_pulang where no_rawat=?",TNoRw.getText()));
            param.put("norawat",TNoRw.getText());
            param.put("pasien",TPasien.getText());
            param.put("norm",TNoRM.getText());
            param.put("peresep",Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?",Sequel.cariIsi("select kd_dokter from permintaan_resep_pulang where no_rawat=?",TNoRw.getText())));
            if(akses.getkode().equals("Admin Utama")){
                param.put("diserahkanoleh","Petugas Farmasi");
            }else{
                param.put("diserahkanoleh",Sequel.cariIsi("select petugas.petugas from petugas where petugas.nip=?",akses.getkode()));
            }

            param.put("noresep",Sequel.cariIsi("select no_permintaan from permintaan_resep_pulang where no_rawat=?",TNoRw.getText()));
//            finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",KdDokter.getText());
//            param.put("finger","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+NmDokter.getText()+"\nID "+(finger.equals("")?KdDokter.getText():finger)+"\n"+DTPBeri.getSelectedItem());
//            param.put("jam",cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem());
//            param.put("logo",Sequel.cariGambar("select setting.logo from setting"));
//            param.put("photo","http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/penyerahanresep/"+Sequel.cariIsi("select bukti_penyerahan_resep_obat.photo from bukti_penyerahan_resep_obat where bukti_penyerahan_resep_obat.no_resep=?",NoResep.getText()));

            Valid.MyReportqry("rptLembarObat2.jasper","report","::[ Lembar Pemberian Obat ]::","select * from temporary_resep where temporary_resep.temp37='"+akses.getalamatip()+"' order by temporary_resep.no",param);
            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_ppLembarObat1ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgResepPulang dialog = new DlgResepPulang(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.Button BtnAll;
    private widget.Button BtnCari;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnPrint;
    private widget.CekBox ChkInput;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.PanelBiasa FormInput;
    private widget.TextBox KdBarang;
    private widget.Label LCount;
    private widget.Label LCount1;
    private widget.TextBox NmBarang;
    private javax.swing.JPanel PanelInput;
    private widget.TextBox Satuan;
    private widget.ScrollPane Scroll;
    private widget.TextBox TCari;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.Button btnBarang;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel13;
    private widget.Label jLabel19;
    private widget.Label jLabel21;
    private widget.Label jLabel3;
    private widget.Label jLabel6;
    private widget.Label jLabel7;
    private widget.Label jLabel8;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private javax.swing.JMenuItem ppLabelDataObat;
    private javax.swing.JMenuItem ppLembarObat1;
    private javax.swing.JMenuItem ppResepObat;
    private javax.swing.JMenuItem ppResepObat1;
    private javax.swing.JMenuItem ppResepObat2;
    private widget.Table tbResep;
    // End of variables declaration//GEN-END:variables

    public void tampil() {
        Valid.tabelKosong(tabMode);
        try{      
            ps=koneksi.prepareStatement("select resep_pulang.no_rawat,resep_pulang.tanggal,resep_pulang.jam,concat(reg_periksa.no_rkm_medis,' ',pasien.nm_pasien),"+
                    "concat(resep_pulang.kode_brng,' ',databarang.nama_brng),resep_pulang.jml_barang,resep_pulang.harga,resep_pulang.total,resep_pulang.dosis, "+
                    "resep_pulang.no_batch,resep_pulang.no_faktur from resep_pulang inner join reg_periksa inner join pasien inner join databarang "+
                    "on reg_periksa.no_rawat=resep_pulang.no_rawat and reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "and databarang.kode_brng=resep_pulang.kode_brng where "+
                    "resep_pulang.tanggal between ? and ? and reg_periksa.no_rkm_medis like ? or "+
                    "resep_pulang.tanggal between ? and ? and pasien.nm_pasien like ? or "+
                    "resep_pulang.tanggal between ? and ? and resep_pulang.kode_brng like ? or "+
                    "resep_pulang.tanggal between ? and ? and resep_pulang.no_rawat like ? or "+
                    "resep_pulang.tanggal between ? and ? and resep_pulang.no_batch like ? or "+
                    "resep_pulang.tanggal between ? and ? and resep_pulang.no_faktur like ? or "+
                    "resep_pulang.tanggal between ? and ? and databarang.nama_brng like ? "+
                    "order by resep_pulang.tanggal");
            try {
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps.setString(3,"%"+TCari.getText().trim()+"%");
                ps.setString(4,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(5,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps.setString(6,"%"+TCari.getText().trim()+"%");
                ps.setString(7,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(8,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps.setString(9,"%"+TCari.getText().trim()+"%");
                ps.setString(10,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(11,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps.setString(12,"%"+TCari.getText().trim()+"%");
                ps.setString(13,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(14,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps.setString(15,"%"+TCari.getText().trim()+"%");
                ps.setString(16,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(17,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps.setString(18,"%"+TCari.getText().trim()+"%");
                ps.setString(19,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(20,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps.setString(21,"%"+TCari.getText().trim()+"%");
                rs=ps.executeQuery();
                jumlahtotal=0;
                while(rs.next()){
                    tabMode.addRow(new Object[]{
                        rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getDouble(6),rs.getDouble(7),rs.getDouble(8),rs.getString(9),rs.getString(10),rs.getString(11)
                    });
                    jumlahtotal=jumlahtotal+rs.getDouble("total");
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
            LCount.setText(""+tabMode.getRowCount());
            LCount1.setText(""+Valid.SetAngka(jumlahtotal));
        }catch(SQLException e){
            System.out.println("Notifikasi : "+e);
        }        
    }

    private void getData() {
        if(tbResep.getSelectedRow()!= -1){
            TNoRw.setText(tbResep.getValueAt(tbResep.getSelectedRow(),0).toString()); 
            TPasien.setText(tbResep.getValueAt(tbResep.getSelectedRow(),3).toString()); 
            Sequel.cariIsi("select kode_brng from databarang where concat(kode_brng,' ',nama_brng)=?",KdBarang,tbResep.getValueAt(tbResep.getSelectedRow(),4).toString());
            Sequel.cariIsi("select nama_brng from databarang where concat(kode_brng,' ',nama_brng)=?",NmBarang,tbResep.getValueAt(tbResep.getSelectedRow(),4).toString());
            Sequel.cariIsi("select kode_sat from databarang where concat(kode_brng,' ',nama_brng)=?",Satuan,tbResep.getValueAt(tbResep.getSelectedRow(),4).toString());
         }
    }
   
    public void setNoRm(String norwt,String norm,String pasien,Date tgl1,Date tgl2) {
        TNoRw.setText(norwt);
        TNoRM.setText(norm);
        TPasien.setText(pasien);
        Sequel.cariIsi("select concat(pasien.no_rkm_medis,' ',pasien.nm_pasien) from reg_periksa inner join pasien "+
                    " on reg_periksa.no_rkm_medis=pasien.no_rkm_medis where reg_periksa.no_rawat=? ",TPasien,TNoRw.getText());
        TCari.setText(norwt);     
        DTPCari1.setDate(tgl1);
        DTPCari2.setDate(tgl2);   
        ChkInput.setSelected(true);
        isForm();
    }
    
    private void isForm(){
        if(ChkInput.isSelected()==true){
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH,96));
            FormInput.setVisible(true);      
            ChkInput.setVisible(true);
        }else if(ChkInput.isSelected()==false){           
            ChkInput.setVisible(false);            
            PanelInput.setPreferredSize(new Dimension(WIDTH,20));
            FormInput.setVisible(false);      
            ChkInput.setVisible(true);
        }
    }
    
    public void isCek(){
        btnBarang.setEnabled(akses.getresep_pulang());
        BtnHapus.setEnabled(akses.getresep_pulang());
        BtnPrint.setEnabled(akses.getresep_pulang());
    }
}
