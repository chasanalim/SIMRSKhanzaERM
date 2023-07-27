/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rekammedis;

import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import kepegawaian.DlgCariPetugas;


/**
 *
 * @author perpustakaan
 */
public final class RMChecklistDuranteOperasi extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private PreparedStatement ps;
    private ResultSet rs;
    private int i=0;    
    private DlgCariPetugas petugas=new DlgCariPetugas(null,false);
    private String dpjp="";
    /** Creates new form DlgRujuk
     * @param parent
     * @param modal */
    public RMChecklistDuranteOperasi(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(8,1);
        setSize(628,674);

        tabMode=new DefaultTableModel(null,new Object[]{
            "No.Rawat","No.R.M.","Nama Pasien","Umur","JK","Tgl.Lahir","Tgl.Obser","Jam Obser","GCS (E,V,M)",
            "TD(mmHg)","HR(x/menit)","RR(x/menit)","Suhu(°C)","SpO2(%)","NIP","Nama Petugas"
        }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbObat.setModel(tabMode);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbObat.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 16; i++) {
            TableColumn column = tbObat.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(105);
            }else if(i==1){
                column.setPreferredWidth(65);
            }else if(i==2){
                column.setPreferredWidth(160);
            }else if(i==3){
                column.setPreferredWidth(35);
            }else if(i==4){
                column.setPreferredWidth(20);
            }else if(i==5){
                column.setPreferredWidth(65);
            }else if(i==6){
                column.setPreferredWidth(65);
            }else if(i==7){
                column.setPreferredWidth(60);
            }else if(i==8){
                column.setPreferredWidth(65);
            }else if(i==9){
                column.setPreferredWidth(65);
            }else if(i==10){
                column.setPreferredWidth(65);
            }else if(i==11){
                column.setPreferredWidth(65);
            }else if(i==12){
                column.setPreferredWidth(50);
            }else if(i==13){
                column.setPreferredWidth(55);
            }else if(i==14){
                column.setPreferredWidth(90);
            }else if(i==15){
                column.setPreferredWidth(160);
            }
        }
        tbObat.setDefaultRenderer(Object.class, new WarnaTable());

        TNoRw.setDocument(new batasInput((byte)17).getKata(TNoRw));
        NIP.setDocument(new batasInput((byte)20).getKata(NIP));
        GCS.setDocument(new batasInput((byte)10).getKata(GCS));
        TD.setDocument(new batasInput((byte)8).getKata(TD));
        HR.setDocument(new batasInput((byte)5).getKata(HR));
        RR.setDocument(new batasInput((byte)5).getKata(RR));
        Suhu.setDocument(new batasInput((byte)5).getKata(Suhu));
        SPO.setDocument(new batasInput((byte)3).getKata(SPO));
        TCari.setDocument(new batasInput((int)100).getKata(TCari));
        
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
        
        petugas.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(petugas.getTable().getSelectedRow()!= -1){                   
                    NIP.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),0).toString());
                    NamaPetugas.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),1).toString());
                }  
                NIP.requestFocus();
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
        
        ChkInput.setSelected(false);
        isForm();
        jam();
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
        MnCatatanObservasiIGD = new javax.swing.JMenuItem();
        internalFrame1 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbObat = new widget.Table();
        jPanel3 = new javax.swing.JPanel();
        panelGlass8 = new widget.panelisi();
        BtnSimpan = new widget.Button();
        BtnBatal = new widget.Button();
        BtnHapus = new widget.Button();
        BtnEdit = new widget.Button();
        BtnPrint = new widget.Button();
        jLabel7 = new widget.Label();
        LCount = new widget.Label();
        BtnKeluar = new widget.Button();
        panelGlass9 = new widget.panelisi();
        jLabel19 = new widget.Label();
        DTPCari1 = new widget.Tanggal();
        jLabel21 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        jLabel6 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        BtnAll = new widget.Button();
        PanelInput = new javax.swing.JPanel();
        ChkInput = new widget.CekBox();
        scrollInput = new widget.ScrollPane();
        FormInput = new widget.PanelBiasa();
        jLabel4 = new widget.Label();
        TNoRw = new widget.TextBox();
        TPasien = new widget.TextBox();
        Tanggal = new widget.Tanggal();
        TNoRM = new widget.TextBox();
        jLabel16 = new widget.Label();
        Jam = new widget.ComboBox();
        Menit = new widget.ComboBox();
        Detik = new widget.ComboBox();
        ChkKejadian = new widget.CekBox();
        jLabel18 = new widget.Label();
        NIP = new widget.TextBox();
        NamaPetugas = new widget.TextBox();
        btnPetugas = new widget.Button();
        jLabel8 = new widget.Label();
        TglLahir = new widget.TextBox();
        jLabel12 = new widget.Label();
        TD = new widget.TextBox();
        jLabel53 = new widget.Label();
        jLabel58 = new widget.Label();
        Kardiologi5 = new widget.ComboBox();
        jLabel54 = new widget.Label();
        jLabel59 = new widget.Label();
        Kardiologi6 = new widget.ComboBox();
        jLabel60 = new widget.Label();
        Kardiologi7 = new widget.ComboBox();
        jLabel61 = new widget.Label();
        Kardiologi8 = new widget.ComboBox();
        jLabel62 = new widget.Label();
        Kardiologi9 = new widget.ComboBox();
        jLabel63 = new widget.Label();
        Kardiologi10 = new widget.ComboBox();
        jLabel64 = new widget.Label();
        Kardiologi11 = new widget.ComboBox();
        jLabel65 = new widget.Label();
        Kardiologi12 = new widget.ComboBox();
        jLabel55 = new widget.Label();
        jLabel66 = new widget.Label();
        jLabel67 = new widget.Label();
        jLabel68 = new widget.Label();
        jLabel69 = new widget.Label();
        jLabel70 = new widget.Label();
        jLabel71 = new widget.Label();
        J1HP1 = new widget.TextBox();
        GCS = new widget.TextBox();
        HR = new widget.TextBox();
        Suhu = new widget.TextBox();
        RR = new widget.TextBox();
        SPO = new widget.TextBox();
        jLabel13 = new widget.Label();
        TD7 = new widget.TextBox();
        J2HP1 = new widget.TextBox();
        TD9 = new widget.TextBox();
        TD10 = new widget.TextBox();
        TD11 = new widget.TextBox();
        TD12 = new widget.TextBox();
        TD13 = new widget.TextBox();
        jLabel14 = new widget.Label();
        TD14 = new widget.TextBox();
        TD15 = new widget.TextBox();
        TD16 = new widget.TextBox();
        TD17 = new widget.TextBox();
        TD18 = new widget.TextBox();
        TD19 = new widget.TextBox();
        TD20 = new widget.TextBox();
        jLabel15 = new widget.Label();
        TD21 = new widget.TextBox();
        TD22 = new widget.TextBox();
        TD23 = new widget.TextBox();
        TD24 = new widget.TextBox();
        TD25 = new widget.TextBox();
        TD26 = new widget.TextBox();
        TD27 = new widget.TextBox();
        TD28 = new widget.TextBox();
        TD29 = new widget.TextBox();
        TD30 = new widget.TextBox();
        TD31 = new widget.TextBox();
        TD32 = new widget.TextBox();
        TD33 = new widget.TextBox();
        TD34 = new widget.TextBox();
        jLabel24 = new widget.Label();
        TD35 = new widget.TextBox();
        TD36 = new widget.TextBox();
        TD37 = new widget.TextBox();
        TD38 = new widget.TextBox();
        TD39 = new widget.TextBox();
        TD40 = new widget.TextBox();
        jLabel30 = new widget.Label();
        TD41 = new widget.TextBox();
        jLabel31 = new widget.Label();
        TD42 = new widget.TextBox();
        TD43 = new widget.TextBox();
        TD44 = new widget.TextBox();
        TD45 = new widget.TextBox();
        TD46 = new widget.TextBox();
        TD47 = new widget.TextBox();
        TD48 = new widget.TextBox();
        jLabel32 = new widget.Label();
        TD49 = new widget.TextBox();
        TD50 = new widget.TextBox();
        TD51 = new widget.TextBox();
        TD52 = new widget.TextBox();
        TD53 = new widget.TextBox();
        TD54 = new widget.TextBox();
        TD55 = new widget.TextBox();
        TD56 = new widget.TextBox();
        TD57 = new widget.TextBox();
        TD58 = new widget.TextBox();
        jLabel33 = new widget.Label();
        TD59 = new widget.TextBox();
        TD60 = new widget.TextBox();
        TD61 = new widget.TextBox();
        TD62 = new widget.TextBox();
        TD63 = new widget.TextBox();
        TD64 = new widget.TextBox();
        TD65 = new widget.TextBox();
        TD66 = new widget.TextBox();
        TD67 = new widget.TextBox();
        TD68 = new widget.TextBox();
        TD69 = new widget.TextBox();
        jLabel34 = new widget.Label();
        jLabel36 = new widget.Label();
        TD70 = new widget.TextBox();
        TD71 = new widget.TextBox();
        TD72 = new widget.TextBox();
        TD73 = new widget.TextBox();
        TD74 = new widget.TextBox();
        TD75 = new widget.TextBox();
        TD76 = new widget.TextBox();
        TD77 = new widget.TextBox();
        TD78 = new widget.TextBox();
        TD79 = new widget.TextBox();
        jLabel37 = new widget.Label();
        TD80 = new widget.TextBox();
        TD81 = new widget.TextBox();
        TD82 = new widget.TextBox();
        TD83 = new widget.TextBox();
        TD84 = new widget.TextBox();
        TD85 = new widget.TextBox();
        TD86 = new widget.TextBox();
        TD87 = new widget.TextBox();
        TD88 = new widget.TextBox();
        TD89 = new widget.TextBox();
        TD90 = new widget.TextBox();
        jLabel38 = new widget.Label();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        MnCatatanObservasiIGD.setBackground(new java.awt.Color(255, 255, 254));
        MnCatatanObservasiIGD.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnCatatanObservasiIGD.setForeground(new java.awt.Color(50, 50, 50));
        MnCatatanObservasiIGD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnCatatanObservasiIGD.setText("Formulir Catatan Observasi Rawat Inap");
        MnCatatanObservasiIGD.setName("MnCatatanObservasiIGD"); // NOI18N
        MnCatatanObservasiIGD.setPreferredSize(new java.awt.Dimension(260, 26));
        MnCatatanObservasiIGD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnCatatanObservasiIGDActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnCatatanObservasiIGD);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Checklist Durante Operasi ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);
        Scroll.setPreferredSize(new java.awt.Dimension(452, 30));

        tbObat.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbObat.setComponentPopupMenu(jPopupMenu1);
        tbObat.setName("tbObat"); // NOI18N
        tbObat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbObatMouseClicked(evt);
            }
        });
        tbObat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbObatKeyPressed(evt);
            }
        });
        Scroll.setViewportView(tbObat);

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(44, 100));
        jPanel3.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        BtnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpan.setMnemonic('S');
        BtnSimpan.setText("Simpan");
        BtnSimpan.setToolTipText("Alt+S");
        BtnSimpan.setName("BtnSimpan"); // NOI18N
        BtnSimpan.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpanActionPerformed(evt);
            }
        });
        BtnSimpan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnSimpanKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnSimpan);

        BtnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Cancel-2-16x16.png"))); // NOI18N
        BtnBatal.setMnemonic('B');
        BtnBatal.setText("Baru");
        BtnBatal.setToolTipText("Alt+B");
        BtnBatal.setName("BtnBatal"); // NOI18N
        BtnBatal.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBatalActionPerformed(evt);
            }
        });
        BtnBatal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnBatalKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnBatal);

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

        BtnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/inventaris.png"))); // NOI18N
        BtnEdit.setMnemonic('G');
        BtnEdit.setText("Ganti");
        BtnEdit.setToolTipText("Alt+G");
        BtnEdit.setName("BtnEdit"); // NOI18N
        BtnEdit.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEditActionPerformed(evt);
            }
        });
        BtnEdit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnEditKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnEdit);

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

        jLabel7.setText("Record :");
        jLabel7.setName("jLabel7"); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass8.add(jLabel7);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass8.add(LCount);

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

        jLabel19.setText("Tanggal :");
        jLabel19.setName("jLabel19"); // NOI18N
        jLabel19.setPreferredSize(new java.awt.Dimension(60, 23));
        panelGlass9.add(jLabel19);

        DTPCari1.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "27-07-2023" }));
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
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "27-07-2023" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setName("DTPCari2"); // NOI18N
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(95, 23));
        panelGlass9.add(DTPCari2);

        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass9.add(jLabel6);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(310, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass9.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('3');
        BtnCari.setToolTipText("Alt+3");
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

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('M');
        BtnAll.setToolTipText("Alt+M");
        BtnAll.setName("BtnAll"); // NOI18N
        BtnAll.setPreferredSize(new java.awt.Dimension(28, 23));
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
        panelGlass9.add(BtnAll);

        jPanel3.add(panelGlass9, java.awt.BorderLayout.PAGE_START);

        internalFrame1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setPreferredSize(new java.awt.Dimension(192, 420));
        PanelInput.setLayout(new java.awt.BorderLayout(1, 1));

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

        scrollInput.setName("scrollInput"); // NOI18N
        scrollInput.setPreferredSize(new java.awt.Dimension(102, 557));

        FormInput.setBackground(new java.awt.Color(250, 255, 245));
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(100, 655));
        FormInput.setLayout(null);

        jLabel4.setText("No.Rawat :");
        jLabel4.setName("jLabel4"); // NOI18N
        FormInput.add(jLabel4);
        jLabel4.setBounds(0, 10, 80, 23);

        TNoRw.setHighlighter(null);
        TNoRw.setName("TNoRw"); // NOI18N
        FormInput.add(TNoRw);
        TNoRw.setBounds(84, 10, 136, 23);

        TPasien.setEditable(false);
        TPasien.setHighlighter(null);
        TPasien.setName("TPasien"); // NOI18N
        FormInput.add(TPasien);
        TPasien.setBounds(336, 10, 285, 23);

        Tanggal.setForeground(new java.awt.Color(50, 70, 50));
        Tanggal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "27-07-2023" }));
        Tanggal.setDisplayFormat("dd-MM-yyyy");
        Tanggal.setName("Tanggal"); // NOI18N
        Tanggal.setOpaque(false);
        FormInput.add(Tanggal);
        Tanggal.setBounds(84, 40, 90, 23);

        TNoRM.setEditable(false);
        TNoRM.setHighlighter(null);
        TNoRM.setName("TNoRM"); // NOI18N
        FormInput.add(TNoRM);
        TNoRM.setBounds(222, 10, 112, 23);

        jLabel16.setText("Tanggal :");
        jLabel16.setName("jLabel16"); // NOI18N
        jLabel16.setVerifyInputWhenFocusTarget(false);
        FormInput.add(jLabel16);
        jLabel16.setBounds(0, 40, 80, 23);

        Jam.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        Jam.setName("Jam"); // NOI18N
        FormInput.add(Jam);
        Jam.setBounds(178, 40, 62, 23);

        Menit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        Menit.setName("Menit"); // NOI18N
        FormInput.add(Menit);
        Menit.setBounds(243, 40, 62, 23);

        Detik.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        Detik.setName("Detik"); // NOI18N
        FormInput.add(Detik);
        Detik.setBounds(308, 40, 62, 23);

        ChkKejadian.setBorder(null);
        ChkKejadian.setSelected(true);
        ChkKejadian.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ChkKejadian.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkKejadian.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChkKejadian.setName("ChkKejadian"); // NOI18N
        FormInput.add(ChkKejadian);
        ChkKejadian.setBounds(373, 40, 23, 23);

        jLabel18.setText("Petugas :");
        jLabel18.setName("jLabel18"); // NOI18N
        FormInput.add(jLabel18);
        jLabel18.setBounds(400, 40, 70, 23);

        NIP.setEditable(false);
        NIP.setHighlighter(null);
        NIP.setName("NIP"); // NOI18N
        FormInput.add(NIP);
        NIP.setBounds(474, 40, 94, 23);

        NamaPetugas.setEditable(false);
        NamaPetugas.setName("NamaPetugas"); // NOI18N
        FormInput.add(NamaPetugas);
        NamaPetugas.setBounds(570, 40, 187, 23);

        btnPetugas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnPetugas.setMnemonic('2');
        btnPetugas.setToolTipText("ALt+2");
        btnPetugas.setName("btnPetugas"); // NOI18N
        FormInput.add(btnPetugas);
        btnPetugas.setBounds(761, 40, 28, 23);

        jLabel8.setText("Tgl.Lahir :");
        jLabel8.setName("jLabel8"); // NOI18N
        FormInput.add(jLabel8);
        jLabel8.setBounds(625, 10, 60, 23);

        TglLahir.setHighlighter(null);
        TglLahir.setName("TglLahir"); // NOI18N
        FormInput.add(TglLahir);
        TglLahir.setBounds(689, 10, 100, 23);

        jLabel12.setText("Gause (Kassa)");
        jLabel12.setName("jLabel12"); // NOI18N
        FormInput.add(jLabel12);
        jLabel12.setBounds(30, 260, 130, 23);

        TD.setFocusTraversalPolicyProvider(true);
        TD.setName("TD"); // NOI18N
        FormInput.add(TD);
        TD.setBounds(710, 260, 100, 23);

        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel53.setText("3. Perhitungan Instrumen Dasar");
        jLabel53.setName("jLabel53"); // NOI18N
        FormInput.add(jLabel53);
        jLabel53.setBounds(10, 210, 180, 23);

        jLabel58.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel58.setText("Pengaturan Posisi Operasi :");
        jLabel58.setName("jLabel58"); // NOI18N
        FormInput.add(jLabel58);
        jLabel58.setBounds(30, 90, 210, 23);

        Kardiologi5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Supinasi", "Pronasi", "Litotomi", "Tengkulup", "Leteral Kanan/Kiri", "Sim Kanan/Kiri" }));
        Kardiologi5.setName("Kardiologi5"); // NOI18N
        FormInput.add(Kardiologi5);
        Kardiologi5.setBounds(170, 90, 200, 23);

        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel54.setText("I. Posisi Operasi ");
        jLabel54.setName("jLabel54"); // NOI18N
        FormInput.add(jLabel54);
        jLabel54.setBounds(10, 70, 180, 23);

        jLabel59.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel59.setText("Cuci Tangan Bedah :");
        jLabel59.setName("jLabel59"); // NOI18N
        FormInput.add(jLabel59);
        jLabel59.setBounds(30, 130, 170, 23);

        Kardiologi6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        Kardiologi6.setName("Kardiologi6"); // NOI18N
        Kardiologi6.setPreferredSize(new java.awt.Dimension(55, 22));
        FormInput.add(Kardiologi6);
        Kardiologi6.setBounds(150, 130, 80, 23);

        jLabel60.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel60.setText("Memakai Jas Operasi :");
        jLabel60.setName("jLabel60"); // NOI18N
        FormInput.add(jLabel60);
        jLabel60.setBounds(30, 160, 170, 23);

        Kardiologi7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        Kardiologi7.setName("Kardiologi7"); // NOI18N
        Kardiologi7.setPreferredSize(new java.awt.Dimension(55, 20));
        FormInput.add(Kardiologi7);
        Kardiologi7.setBounds(150, 160, 80, 23);

        jLabel61.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel61.setText("Memakai Handscoon Streil :");
        jLabel61.setName("jLabel61"); // NOI18N
        FormInput.add(jLabel61);
        jLabel61.setBounds(280, 130, 170, 23);

        Kardiologi8.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        Kardiologi8.setName("Kardiologi8"); // NOI18N
        Kardiologi8.setPreferredSize(new java.awt.Dimension(55, 22));
        FormInput.add(Kardiologi8);
        Kardiologi8.setBounds(460, 130, 80, 23);

        jLabel62.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel62.setText("Desinfeksi Iodine Providone 10%:");
        jLabel62.setName("jLabel62"); // NOI18N
        FormInput.add(jLabel62);
        jLabel62.setBounds(280, 160, 170, 23);

        Kardiologi9.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        Kardiologi9.setName("Kardiologi9"); // NOI18N
        Kardiologi9.setPreferredSize(new java.awt.Dimension(55, 20));
        FormInput.add(Kardiologi9);
        Kardiologi9.setBounds(460, 160, 80, 23);

        jLabel63.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel63.setText("Desinfeksi Alkohol :");
        jLabel63.setName("jLabel63"); // NOI18N
        FormInput.add(jLabel63);
        jLabel63.setBounds(600, 130, 170, 23);

        Kardiologi10.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        Kardiologi10.setName("Kardiologi10"); // NOI18N
        Kardiologi10.setPreferredSize(new java.awt.Dimension(55, 22));
        FormInput.add(Kardiologi10);
        Kardiologi10.setBounds(720, 130, 80, 23);

        jLabel64.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel64.setText("Memasang Linen Steril :");
        jLabel64.setName("jLabel64"); // NOI18N
        FormInput.add(jLabel64);
        jLabel64.setBounds(600, 160, 170, 23);

        Kardiologi11.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        Kardiologi11.setName("Kardiologi11"); // NOI18N
        Kardiologi11.setPreferredSize(new java.awt.Dimension(55, 20));
        FormInput.add(Kardiologi11);
        Kardiologi11.setBounds(720, 160, 80, 23);

        jLabel65.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel65.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel65.setText("Tambahan Selama Operasi");
        jLabel65.setName("jLabel65"); // NOI18N
        FormInput.add(jLabel65);
        jLabel65.setBounds(290, 230, 230, 23);

        Kardiologi12.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Bersih", "Terkontaminasi", "Infeksi" }));
        Kardiologi12.setName("Kardiologi12"); // NOI18N
        FormInput.add(Kardiologi12);
        Kardiologi12.setBounds(150, 190, 150, 23);

        jLabel55.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel55.setText("2. Preparasi Operasi");
        jLabel55.setName("jLabel55"); // NOI18N
        FormInput.add(jLabel55);
        jLabel55.setBounds(10, 110, 180, 23);

        jLabel66.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel66.setText("Klasifikasi Luka :");
        jLabel66.setName("jLabel66"); // NOI18N
        FormInput.add(jLabel66);
        jLabel66.setBounds(30, 190, 90, 23);

        jLabel67.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel67.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel67.setText("Jumlah");
        jLabel67.setName("jLabel67"); // NOI18N
        FormInput.add(jLabel67);
        jLabel67.setBounds(530, 230, 60, 23);

        jLabel68.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel68.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel68.setText("Hitungan Akhir");
        jLabel68.setName("jLabel68"); // NOI18N
        FormInput.add(jLabel68);
        jLabel68.setBounds(710, 230, 100, 23);

        jLabel69.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel69.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel69.setText("Jenis Item");
        jLabel69.setName("jLabel69"); // NOI18N
        FormInput.add(jLabel69);
        jLabel69.setBounds(30, 230, 140, 23);

        jLabel70.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel70.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel70.setText("Hitungan Pertama");
        jLabel70.setName("jLabel70"); // NOI18N
        FormInput.add(jLabel70);
        jLabel70.setBounds(180, 230, 100, 23);

        jLabel71.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel71.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel71.setText("Hitungan Kedua");
        jLabel71.setName("jLabel71"); // NOI18N
        FormInput.add(jLabel71);
        jLabel71.setBounds(600, 230, 100, 23);

        J1HP1.setFocusTraversalPolicyProvider(true);
        J1HP1.setName("J1HP1"); // NOI18N
        FormInput.add(J1HP1);
        J1HP1.setBounds(180, 260, 100, 23);

        GCS.setFocusTraversalPolicyProvider(true);
        GCS.setName("GCS"); // NOI18N
        FormInput.add(GCS);
        GCS.setBounds(290, 260, 70, 23);

        HR.setFocusTraversalPolicyProvider(true);
        HR.setName("HR"); // NOI18N
        FormInput.add(HR);
        HR.setBounds(370, 260, 70, 23);

        Suhu.setFocusTraversalPolicyProvider(true);
        Suhu.setName("Suhu"); // NOI18N
        FormInput.add(Suhu);
        Suhu.setBounds(450, 260, 70, 23);

        RR.setFocusTraversalPolicyProvider(true);
        RR.setName("RR"); // NOI18N
        FormInput.add(RR);
        RR.setBounds(530, 260, 60, 23);

        SPO.setFocusTraversalPolicyProvider(true);
        SPO.setName("SPO"); // NOI18N
        FormInput.add(SPO);
        SPO.setBounds(600, 260, 100, 23);

        jLabel13.setText("Pean Kecil");
        jLabel13.setName("jLabel13"); // NOI18N
        FormInput.add(jLabel13);
        jLabel13.setBounds(30, 290, 130, 23);

        TD7.setFocusTraversalPolicyProvider(true);
        TD7.setName("TD7"); // NOI18N
        FormInput.add(TD7);
        TD7.setBounds(710, 290, 100, 23);

        J2HP1.setFocusTraversalPolicyProvider(true);
        J2HP1.setName("J2HP1"); // NOI18N
        FormInput.add(J2HP1);
        J2HP1.setBounds(180, 290, 100, 23);

        TD9.setFocusTraversalPolicyProvider(true);
        TD9.setName("TD9"); // NOI18N
        FormInput.add(TD9);
        TD9.setBounds(290, 290, 70, 23);

        TD10.setFocusTraversalPolicyProvider(true);
        TD10.setName("TD10"); // NOI18N
        FormInput.add(TD10);
        TD10.setBounds(370, 290, 70, 23);

        TD11.setFocusTraversalPolicyProvider(true);
        TD11.setName("TD11"); // NOI18N
        FormInput.add(TD11);
        TD11.setBounds(450, 290, 70, 23);

        TD12.setFocusTraversalPolicyProvider(true);
        TD12.setName("TD12"); // NOI18N
        FormInput.add(TD12);
        TD12.setBounds(530, 290, 60, 23);

        TD13.setFocusTraversalPolicyProvider(true);
        TD13.setName("TD13"); // NOI18N
        FormInput.add(TD13);
        TD13.setBounds(600, 290, 100, 23);

        jLabel14.setText("Pean Besar");
        jLabel14.setName("jLabel14"); // NOI18N
        FormInput.add(jLabel14);
        jLabel14.setBounds(30, 320, 130, 23);

        TD14.setFocusTraversalPolicyProvider(true);
        TD14.setName("TD14"); // NOI18N
        FormInput.add(TD14);
        TD14.setBounds(710, 320, 100, 23);

        TD15.setFocusTraversalPolicyProvider(true);
        TD15.setName("TD15"); // NOI18N
        FormInput.add(TD15);
        TD15.setBounds(180, 320, 100, 23);

        TD16.setFocusTraversalPolicyProvider(true);
        TD16.setName("TD16"); // NOI18N
        FormInput.add(TD16);
        TD16.setBounds(290, 320, 70, 23);

        TD17.setFocusTraversalPolicyProvider(true);
        TD17.setName("TD17"); // NOI18N
        FormInput.add(TD17);
        TD17.setBounds(370, 320, 70, 23);

        TD18.setFocusTraversalPolicyProvider(true);
        TD18.setName("TD18"); // NOI18N
        FormInput.add(TD18);
        TD18.setBounds(450, 320, 70, 23);

        TD19.setFocusTraversalPolicyProvider(true);
        TD19.setName("TD19"); // NOI18N
        FormInput.add(TD19);
        TD19.setBounds(530, 320, 60, 23);

        TD20.setFocusTraversalPolicyProvider(true);
        TD20.setName("TD20"); // NOI18N
        FormInput.add(TD20);
        TD20.setBounds(600, 320, 100, 23);

        jLabel15.setText("Kocher");
        jLabel15.setName("jLabel15"); // NOI18N
        FormInput.add(jLabel15);
        jLabel15.setBounds(30, 350, 130, 23);

        TD21.setFocusTraversalPolicyProvider(true);
        TD21.setName("TD21"); // NOI18N
        FormInput.add(TD21);
        TD21.setBounds(710, 350, 100, 23);

        TD22.setFocusTraversalPolicyProvider(true);
        TD22.setName("TD22"); // NOI18N
        FormInput.add(TD22);
        TD22.setBounds(180, 350, 100, 23);

        TD23.setFocusTraversalPolicyProvider(true);
        TD23.setName("TD23"); // NOI18N
        FormInput.add(TD23);
        TD23.setBounds(290, 350, 70, 23);

        TD24.setFocusTraversalPolicyProvider(true);
        TD24.setName("TD24"); // NOI18N
        FormInput.add(TD24);
        TD24.setBounds(370, 350, 70, 23);

        TD25.setFocusTraversalPolicyProvider(true);
        TD25.setName("TD25"); // NOI18N
        FormInput.add(TD25);
        TD25.setBounds(450, 350, 70, 23);

        TD26.setFocusTraversalPolicyProvider(true);
        TD26.setName("TD26"); // NOI18N
        FormInput.add(TD26);
        TD26.setBounds(530, 350, 60, 23);

        TD27.setFocusTraversalPolicyProvider(true);
        TD27.setName("TD27"); // NOI18N
        FormInput.add(TD27);
        TD27.setBounds(600, 350, 100, 23);

        TD28.setFocusTraversalPolicyProvider(true);
        TD28.setName("TD28"); // NOI18N
        FormInput.add(TD28);
        TD28.setBounds(450, 380, 70, 23);

        TD29.setFocusTraversalPolicyProvider(true);
        TD29.setName("TD29"); // NOI18N
        FormInput.add(TD29);
        TD29.setBounds(710, 380, 100, 23);

        TD30.setFocusTraversalPolicyProvider(true);
        TD30.setName("TD30"); // NOI18N
        FormInput.add(TD30);
        TD30.setBounds(370, 380, 70, 23);

        TD31.setFocusTraversalPolicyProvider(true);
        TD31.setName("TD31"); // NOI18N
        FormInput.add(TD31);
        TD31.setBounds(530, 380, 60, 23);

        TD32.setFocusTraversalPolicyProvider(true);
        TD32.setName("TD32"); // NOI18N
        FormInput.add(TD32);
        TD32.setBounds(180, 380, 100, 23);

        TD33.setFocusTraversalPolicyProvider(true);
        TD33.setName("TD33"); // NOI18N
        FormInput.add(TD33);
        TD33.setBounds(600, 380, 100, 23);

        TD34.setFocusTraversalPolicyProvider(true);
        TD34.setName("TD34"); // NOI18N
        FormInput.add(TD34);
        TD34.setBounds(290, 380, 70, 23);

        jLabel24.setText("Pinset Anatomi");
        jLabel24.setName("jLabel24"); // NOI18N
        FormInput.add(jLabel24);
        jLabel24.setBounds(30, 380, 130, 23);

        TD35.setFocusTraversalPolicyProvider(true);
        TD35.setName("TD35"); // NOI18N
        FormInput.add(TD35);
        TD35.setBounds(180, 410, 100, 23);

        TD36.setFocusTraversalPolicyProvider(true);
        TD36.setName("TD36"); // NOI18N
        FormInput.add(TD36);
        TD36.setBounds(290, 410, 70, 23);

        TD37.setFocusTraversalPolicyProvider(true);
        TD37.setName("TD37"); // NOI18N
        FormInput.add(TD37);
        TD37.setBounds(530, 410, 60, 23);

        TD38.setFocusTraversalPolicyProvider(true);
        TD38.setName("TD38"); // NOI18N
        FormInput.add(TD38);
        TD38.setBounds(710, 410, 100, 23);

        TD39.setFocusTraversalPolicyProvider(true);
        TD39.setName("TD39"); // NOI18N
        FormInput.add(TD39);
        TD39.setBounds(450, 410, 70, 23);

        TD40.setFocusTraversalPolicyProvider(true);
        TD40.setName("TD40"); // NOI18N
        FormInput.add(TD40);
        TD40.setBounds(600, 410, 100, 23);

        jLabel30.setText("Pinset Cirurgis");
        jLabel30.setName("jLabel30"); // NOI18N
        FormInput.add(jLabel30);
        jLabel30.setBounds(30, 410, 130, 23);

        TD41.setFocusTraversalPolicyProvider(true);
        TD41.setName("TD41"); // NOI18N
        FormInput.add(TD41);
        TD41.setBounds(370, 410, 70, 23);

        jLabel31.setText("Gunting");
        jLabel31.setName("jLabel31"); // NOI18N
        FormInput.add(jLabel31);
        jLabel31.setBounds(30, 440, 130, 23);

        TD42.setFocusTraversalPolicyProvider(true);
        TD42.setName("TD42"); // NOI18N
        FormInput.add(TD42);
        TD42.setBounds(530, 440, 60, 23);

        TD43.setFocusTraversalPolicyProvider(true);
        TD43.setName("TD43"); // NOI18N
        FormInput.add(TD43);
        TD43.setBounds(450, 440, 70, 23);

        TD44.setFocusTraversalPolicyProvider(true);
        TD44.setName("TD44"); // NOI18N
        FormInput.add(TD44);
        TD44.setBounds(180, 440, 100, 23);

        TD45.setFocusTraversalPolicyProvider(true);
        TD45.setName("TD45"); // NOI18N
        FormInput.add(TD45);
        TD45.setBounds(290, 440, 70, 23);

        TD46.setFocusTraversalPolicyProvider(true);
        TD46.setName("TD46"); // NOI18N
        FormInput.add(TD46);
        TD46.setBounds(710, 440, 100, 23);

        TD47.setFocusTraversalPolicyProvider(true);
        TD47.setName("TD47"); // NOI18N
        FormInput.add(TD47);
        TD47.setBounds(370, 440, 70, 23);

        TD48.setFocusTraversalPolicyProvider(true);
        TD48.setName("TD48"); // NOI18N
        FormInput.add(TD48);
        TD48.setBounds(600, 440, 100, 23);

        jLabel32.setText("Towel Klem");
        jLabel32.setName("jLabel32"); // NOI18N
        FormInput.add(jLabel32);
        jLabel32.setBounds(30, 470, 130, 23);

        TD49.setFocusTraversalPolicyProvider(true);
        TD49.setName("TD49"); // NOI18N
        FormInput.add(TD49);
        TD49.setBounds(600, 470, 100, 23);

        TD50.setFocusTraversalPolicyProvider(true);
        TD50.setName("TD50"); // NOI18N
        FormInput.add(TD50);
        TD50.setBounds(290, 470, 70, 23);

        TD51.setFocusTraversalPolicyProvider(true);
        TD51.setName("TD51"); // NOI18N
        FormInput.add(TD51);
        TD51.setBounds(370, 470, 70, 23);

        TD52.setFocusTraversalPolicyProvider(true);
        TD52.setName("TD52"); // NOI18N
        FormInput.add(TD52);
        TD52.setBounds(180, 470, 100, 23);

        TD53.setFocusTraversalPolicyProvider(true);
        TD53.setName("TD53"); // NOI18N
        FormInput.add(TD53);
        TD53.setBounds(710, 470, 100, 23);

        TD54.setFocusTraversalPolicyProvider(true);
        TD54.setName("TD54"); // NOI18N
        FormInput.add(TD54);
        TD54.setBounds(450, 470, 70, 23);

        TD55.setFocusTraversalPolicyProvider(true);
        TD55.setName("TD55"); // NOI18N
        FormInput.add(TD55);
        TD55.setBounds(530, 470, 60, 23);

        TD56.setFocusTraversalPolicyProvider(true);
        TD56.setName("TD56"); // NOI18N
        FormInput.add(TD56);
        TD56.setBounds(600, 500, 100, 23);

        TD57.setFocusTraversalPolicyProvider(true);
        TD57.setName("TD57"); // NOI18N
        FormInput.add(TD57);
        TD57.setBounds(370, 500, 70, 23);

        TD58.setFocusTraversalPolicyProvider(true);
        TD58.setName("TD58"); // NOI18N
        FormInput.add(TD58);
        TD58.setBounds(180, 500, 100, 23);

        jLabel33.setText("Handle Mess");
        jLabel33.setName("jLabel33"); // NOI18N
        FormInput.add(jLabel33);
        jLabel33.setBounds(30, 500, 130, 23);

        TD59.setFocusTraversalPolicyProvider(true);
        TD59.setName("TD59"); // NOI18N
        FormInput.add(TD59);
        TD59.setBounds(290, 500, 70, 23);

        TD60.setFocusTraversalPolicyProvider(true);
        TD60.setName("TD60"); // NOI18N
        FormInput.add(TD60);
        TD60.setBounds(710, 500, 100, 23);

        TD61.setFocusTraversalPolicyProvider(true);
        TD61.setName("TD61"); // NOI18N
        FormInput.add(TD61);
        TD61.setBounds(450, 500, 70, 23);

        TD62.setFocusTraversalPolicyProvider(true);
        TD62.setName("TD62"); // NOI18N
        FormInput.add(TD62);
        TD62.setBounds(530, 500, 60, 23);

        TD63.setFocusTraversalPolicyProvider(true);
        TD63.setName("TD63"); // NOI18N
        FormInput.add(TD63);
        TD63.setBounds(290, 530, 70, 23);

        TD64.setFocusTraversalPolicyProvider(true);
        TD64.setName("TD64"); // NOI18N
        FormInput.add(TD64);
        TD64.setBounds(600, 530, 100, 23);

        TD65.setFocusTraversalPolicyProvider(true);
        TD65.setName("TD65"); // NOI18N
        FormInput.add(TD65);
        TD65.setBounds(370, 530, 70, 23);

        TD66.setFocusTraversalPolicyProvider(true);
        TD66.setName("TD66"); // NOI18N
        FormInput.add(TD66);
        TD66.setBounds(180, 530, 100, 23);

        TD67.setFocusTraversalPolicyProvider(true);
        TD67.setName("TD67"); // NOI18N
        FormInput.add(TD67);
        TD67.setBounds(710, 530, 100, 23);

        TD68.setFocusTraversalPolicyProvider(true);
        TD68.setName("TD68"); // NOI18N
        FormInput.add(TD68);
        TD68.setBounds(450, 530, 70, 23);

        TD69.setFocusTraversalPolicyProvider(true);
        TD69.setName("TD69"); // NOI18N
        FormInput.add(TD69);
        TD69.setBounds(530, 530, 60, 23);

        jLabel34.setText("Allis Klem");
        jLabel34.setName("jLabel34"); // NOI18N
        FormInput.add(jLabel34);
        jLabel34.setBounds(30, 530, 130, 23);

        jLabel36.setText("Needle");
        jLabel36.setName("jLabel36"); // NOI18N
        FormInput.add(jLabel36);
        jLabel36.setBounds(30, 560, 130, 23);

        TD70.setFocusTraversalPolicyProvider(true);
        TD70.setName("TD70"); // NOI18N
        FormInput.add(TD70);
        TD70.setBounds(600, 560, 100, 23);

        TD71.setFocusTraversalPolicyProvider(true);
        TD71.setName("TD71"); // NOI18N
        FormInput.add(TD71);
        TD71.setBounds(290, 560, 70, 23);

        TD72.setFocusTraversalPolicyProvider(true);
        TD72.setName("TD72"); // NOI18N
        FormInput.add(TD72);
        TD72.setBounds(370, 560, 70, 23);

        TD73.setFocusTraversalPolicyProvider(true);
        TD73.setName("TD73"); // NOI18N
        FormInput.add(TD73);
        TD73.setBounds(180, 560, 100, 23);

        TD74.setFocusTraversalPolicyProvider(true);
        TD74.setName("TD74"); // NOI18N
        FormInput.add(TD74);
        TD74.setBounds(710, 560, 100, 23);

        TD75.setFocusTraversalPolicyProvider(true);
        TD75.setName("TD75"); // NOI18N
        FormInput.add(TD75);
        TD75.setBounds(450, 560, 70, 23);

        TD76.setFocusTraversalPolicyProvider(true);
        TD76.setName("TD76"); // NOI18N
        FormInput.add(TD76);
        TD76.setBounds(530, 560, 60, 23);

        TD77.setFocusTraversalPolicyProvider(true);
        TD77.setName("TD77"); // NOI18N
        FormInput.add(TD77);
        TD77.setBounds(600, 590, 100, 23);

        TD78.setFocusTraversalPolicyProvider(true);
        TD78.setName("TD78"); // NOI18N
        FormInput.add(TD78);
        TD78.setBounds(370, 590, 70, 23);

        TD79.setFocusTraversalPolicyProvider(true);
        TD79.setName("TD79"); // NOI18N
        FormInput.add(TD79);
        TD79.setBounds(180, 590, 100, 23);

        jLabel37.setText("Langen Back");
        jLabel37.setName("jLabel37"); // NOI18N
        FormInput.add(jLabel37);
        jLabel37.setBounds(30, 590, 130, 23);

        TD80.setFocusTraversalPolicyProvider(true);
        TD80.setName("TD80"); // NOI18N
        FormInput.add(TD80);
        TD80.setBounds(290, 590, 70, 23);

        TD81.setFocusTraversalPolicyProvider(true);
        TD81.setName("TD81"); // NOI18N
        FormInput.add(TD81);
        TD81.setBounds(710, 590, 100, 23);

        TD82.setFocusTraversalPolicyProvider(true);
        TD82.setName("TD82"); // NOI18N
        FormInput.add(TD82);
        TD82.setBounds(450, 590, 70, 23);

        TD83.setFocusTraversalPolicyProvider(true);
        TD83.setName("TD83"); // NOI18N
        FormInput.add(TD83);
        TD83.setBounds(530, 590, 60, 23);

        TD84.setFocusTraversalPolicyProvider(true);
        TD84.setName("TD84"); // NOI18N
        FormInput.add(TD84);
        TD84.setBounds(290, 620, 70, 23);

        TD85.setFocusTraversalPolicyProvider(true);
        TD85.setName("TD85"); // NOI18N
        FormInput.add(TD85);
        TD85.setBounds(600, 620, 100, 23);

        TD86.setFocusTraversalPolicyProvider(true);
        TD86.setName("TD86"); // NOI18N
        FormInput.add(TD86);
        TD86.setBounds(370, 620, 70, 23);

        TD87.setFocusTraversalPolicyProvider(true);
        TD87.setName("TD87"); // NOI18N
        FormInput.add(TD87);
        TD87.setBounds(180, 620, 100, 23);

        TD88.setFocusTraversalPolicyProvider(true);
        TD88.setName("TD88"); // NOI18N
        FormInput.add(TD88);
        TD88.setBounds(710, 620, 100, 23);

        TD89.setFocusTraversalPolicyProvider(true);
        TD89.setName("TD89"); // NOI18N
        FormInput.add(TD89);
        TD89.setBounds(450, 620, 70, 23);

        TD90.setFocusTraversalPolicyProvider(true);
        TD90.setName("TD90"); // NOI18N
        FormInput.add(TD90);
        TD90.setBounds(530, 620, 60, 23);

        jLabel38.setText("Jarum");
        jLabel38.setName("jLabel38"); // NOI18N
        FormInput.add(jLabel38);
        jLabel38.setBounds(30, 620, 130, 23);

        scrollInput.setViewportView(FormInput);

        PanelInput.add(scrollInput, java.awt.BorderLayout.CENTER);

        internalFrame1.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(TNoRw.getText().trim().equals("")||TPasien.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"pasien");
        }else if(NIP.getText().trim().equals("")||NamaPetugas.getText().trim().equals("")){
            Valid.textKosong(NIP,"Petugas");
        }else{
            if(Sequel.menyimpantf("catatan_observasi_ranap","?,?,?,?,?,?,?,?,?,?","Data",10,new String[]{
                TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+""),Jam.getSelectedItem()+":"+Menit.getSelectedItem()+":"+Detik.getSelectedItem(),
                GCS.getText(),TD.getText(),HR.getText(),RR.getText(),Suhu.getText(),SPO.getText(),NIP.getText()
            })==true){
                tampil();
                emptTeks();
            }   
        }
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnSimpanActionPerformed(null);
        }else{
            Valid.pindah(evt,SPO,BtnBatal);
        }
}//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        emptTeks();
        ChkInput.setSelected(true);
        isForm(); 
}//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            emptTeks();
        }else{Valid.pindah(evt, BtnSimpan, BtnHapus);}
}//GEN-LAST:event_BtnBatalKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        if(tbObat.getSelectedRow()>-1){
            if(akses.getkode().equals("Admin Utama")){
                hapus();
            }else{
                if(NIP.getText().equals(tbObat.getValueAt(tbObat.getSelectedRow(),14).toString())){
                    hapus();
                }else{
                    JOptionPane.showMessageDialog(null,"Hanya bisa dihapus oleh petugas yang bersangkutan..!!");
                }
            }
        }else{
            JOptionPane.showMessageDialog(rootPane,"Silahkan anda pilih data terlebih dahulu..!!");
        }   
}//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnHapusActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnBatal, BtnEdit);
        }
}//GEN-LAST:event_BtnHapusKeyPressed

    private void BtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditActionPerformed
        if(TNoRw.getText().trim().equals("")||TPasien.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"pasien");
        }else if(NIP.getText().trim().equals("")||NamaPetugas.getText().trim().equals("")){
            Valid.textKosong(NIP,"Petugas");
        }else{ 
            if(tbObat.getSelectedRow()>-1){
                if(akses.getkode().equals("Admin Utama")){
                    ganti();
                }else{
                    if(NIP.getText().equals(tbObat.getValueAt(tbObat.getSelectedRow(),14).toString())){
                        ganti();
                    }else{
                        JOptionPane.showMessageDialog(null,"Hanya bisa diganti oleh petugas yang bersangkutan..!!");
                    }
                }
            }else{
                JOptionPane.showMessageDialog(rootPane,"Silahkan anda pilih data terlebih dahulu..!!");
            }
        }
}//GEN-LAST:event_BtnEditActionPerformed

    private void BtnEditKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnEditKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnEditActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnHapus, BtnPrint);
        }
}//GEN-LAST:event_BtnEditKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        petugas.dispose();
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnKeluarActionPerformed(null);
        }else{Valid.pindah(evt,BtnEdit,TCari);}
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
            BtnBatal.requestFocus();
        }else if(tabMode.getRowCount()!=0){
            Map<String, Object> param = new HashMap<>(); 
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());   
            param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
            
            if(TCari.getText().trim().equals("")){
                Valid.MyReportqry("rptDataCatatanObservasiRanap.jasper","report","::[ Data Catatan Observasi Rawat Inap ]::",
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,reg_periksa.umurdaftar,reg_periksa.sttsumur,"+
                    "pasien.jk,pasien.tgl_lahir,catatan_observasi_ranap.tgl_perawatan,catatan_observasi_ranap.jam_rawat,catatan_observasi_ranap.gcs,"+
                    "catatan_observasi_ranap.td,catatan_observasi_ranap.hr,catatan_observasi_ranap.rr,catatan_observasi_ranap.suhu,catatan_observasi_ranap.spo2,"+
                    "catatan_observasi_ranap.nip,petugas.nama from catatan_observasi_ranap inner join reg_periksa on catatan_observasi_ranap.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join petugas on catatan_observasi_ranap.nip=petugas.nip where "+
                    "catatan_observasi_ranap.tgl_perawatan between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+"' order by catatan_observasi_ranap.tgl_perawatan",param);
            }else{
                Valid.MyReportqry("rptDataCatatanObservasiRanap.jasper","report","::[ Data Catatan Observasi Rawat Inap ]::",
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,reg_periksa.umurdaftar,reg_periksa.sttsumur,"+
                    "pasien.jk,pasien.tgl_lahir,catatan_observasi_ranap.tgl_perawatan,catatan_observasi_ranap.jam_rawat,catatan_observasi_ranap.gcs,"+
                    "catatan_observasi_ranap.td,catatan_observasi_ranap.hr,catatan_observasi_ranap.rr,catatan_observasi_ranap.suhu,catatan_observasi_ranap.spo2,"+
                    "catatan_observasi_ranap.nip,petugas.nama from catatan_observasi_ranap inner join reg_periksa on catatan_observasi_ranap.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join petugas on catatan_observasi_ranap.nip=petugas.nip where "+
                    "catatan_observasi_ranap.tgl_perawatan between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+"' and "+
                    "(reg_periksa.no_rawat like '%"+TCari.getText().trim()+"%' or pasien.no_rkm_medis like '%"+TCari.getText().trim()+"%' or "+
                    "pasien.nm_pasien like '%"+TCari.getText().trim()+"%' or catatan_observasi_ranap.nip like '%"+TCari.getText().trim()+"%' or petugas.nama like '%"+TCari.getText().trim()+"%') "+
                    "order by catatan_observasi_ranap.tgl_perawatan ",param);
            }  
        }
        this.setCursor(Cursor.getDefaultCursor());
}//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnPrintActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnEdit, BtnKeluar);
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
            Valid.pindah(evt, BtnCari, TPasien);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void tbObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbObatMouseClicked
        if(tabMode.getRowCount()!=0){
            try {
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
}//GEN-LAST:event_tbObatMouseClicked

    private void tbObatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbObatKeyPressed
        if(tabMode.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
}//GEN-LAST:event_tbObatKeyPressed

    private void MnCatatanObservasiIGDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnCatatanObservasiIGDActionPerformed
        if(tbObat.getSelectedRow()>-1){
            Map<String, Object> param = new HashMap<>();
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());   
            dpjp=Sequel.cariIsi("select dokter.nm_dokter from dpjp_ranap inner join dokter on dpjp_ranap.kd_dokter=dokter.kd_dokter where dpjp_ranap.no_rawat=?",tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
            if(dpjp.equals("")){
                dpjp=Sequel.cariIsi("select dokter.nm_dokter from reg_periksa inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter where reg_periksa.no_rawat=?",tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
            }
            param.put("dpjp",dpjp);   
            param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
            Valid.MyReportqry("rptFormulirCatatanObservasiRanap.jasper","report","::[ Formulir Catatan Observasi Rawat Inap ]::",
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,reg_periksa.umurdaftar,reg_periksa.sttsumur,reg_periksa.tgl_registrasi,reg_periksa.jam_reg,"+
                    "pasien.jk,pasien.tgl_lahir,catatan_observasi_ranap.tgl_perawatan,catatan_observasi_ranap.jam_rawat,catatan_observasi_ranap.gcs,"+
                    "catatan_observasi_ranap.td,catatan_observasi_ranap.hr,catatan_observasi_ranap.rr,catatan_observasi_ranap.suhu,catatan_observasi_ranap.spo2,"+
                    "petugas.nama from catatan_observasi_ranap inner join reg_periksa on catatan_observasi_ranap.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join petugas on catatan_observasi_ranap.nip=petugas.nip where reg_periksa.no_rawat='"+tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()+"'",param);
        }
    }//GEN-LAST:event_MnCatatanObservasiIGDActionPerformed

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMChecklistDuranteOperasi dialog = new RMChecklistDuranteOperasi(new javax.swing.JFrame(), true);
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
    private widget.Button BtnBatal;
    private widget.Button BtnCari;
    private widget.Button BtnEdit;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnPrint;
    private widget.Button BtnSimpan;
    private widget.CekBox ChkInput;
    private widget.CekBox ChkKejadian;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.ComboBox Detik;
    private widget.PanelBiasa FormInput;
    private widget.TextBox GCS;
    private widget.TextBox HR;
    private widget.TextBox J1HP1;
    private widget.TextBox J2HP1;
    private widget.ComboBox Jam;
    private widget.ComboBox Kardiologi10;
    private widget.ComboBox Kardiologi11;
    private widget.ComboBox Kardiologi12;
    private widget.ComboBox Kardiologi5;
    private widget.ComboBox Kardiologi6;
    private widget.ComboBox Kardiologi7;
    private widget.ComboBox Kardiologi8;
    private widget.ComboBox Kardiologi9;
    private widget.Label LCount;
    private widget.ComboBox Menit;
    private javax.swing.JMenuItem MnCatatanObservasiIGD;
    private widget.TextBox NIP;
    private widget.TextBox NamaPetugas;
    private javax.swing.JPanel PanelInput;
    private widget.TextBox RR;
    private widget.TextBox SPO;
    private widget.ScrollPane Scroll;
    private widget.TextBox Suhu;
    private widget.TextBox TCari;
    private widget.TextBox TD;
    private widget.TextBox TD10;
    private widget.TextBox TD11;
    private widget.TextBox TD12;
    private widget.TextBox TD13;
    private widget.TextBox TD14;
    private widget.TextBox TD15;
    private widget.TextBox TD16;
    private widget.TextBox TD17;
    private widget.TextBox TD18;
    private widget.TextBox TD19;
    private widget.TextBox TD20;
    private widget.TextBox TD21;
    private widget.TextBox TD22;
    private widget.TextBox TD23;
    private widget.TextBox TD24;
    private widget.TextBox TD25;
    private widget.TextBox TD26;
    private widget.TextBox TD27;
    private widget.TextBox TD28;
    private widget.TextBox TD29;
    private widget.TextBox TD30;
    private widget.TextBox TD31;
    private widget.TextBox TD32;
    private widget.TextBox TD33;
    private widget.TextBox TD34;
    private widget.TextBox TD35;
    private widget.TextBox TD36;
    private widget.TextBox TD37;
    private widget.TextBox TD38;
    private widget.TextBox TD39;
    private widget.TextBox TD40;
    private widget.TextBox TD41;
    private widget.TextBox TD42;
    private widget.TextBox TD43;
    private widget.TextBox TD44;
    private widget.TextBox TD45;
    private widget.TextBox TD46;
    private widget.TextBox TD47;
    private widget.TextBox TD48;
    private widget.TextBox TD49;
    private widget.TextBox TD50;
    private widget.TextBox TD51;
    private widget.TextBox TD52;
    private widget.TextBox TD53;
    private widget.TextBox TD54;
    private widget.TextBox TD55;
    private widget.TextBox TD56;
    private widget.TextBox TD57;
    private widget.TextBox TD58;
    private widget.TextBox TD59;
    private widget.TextBox TD60;
    private widget.TextBox TD61;
    private widget.TextBox TD62;
    private widget.TextBox TD63;
    private widget.TextBox TD64;
    private widget.TextBox TD65;
    private widget.TextBox TD66;
    private widget.TextBox TD67;
    private widget.TextBox TD68;
    private widget.TextBox TD69;
    private widget.TextBox TD7;
    private widget.TextBox TD70;
    private widget.TextBox TD71;
    private widget.TextBox TD72;
    private widget.TextBox TD73;
    private widget.TextBox TD74;
    private widget.TextBox TD75;
    private widget.TextBox TD76;
    private widget.TextBox TD77;
    private widget.TextBox TD78;
    private widget.TextBox TD79;
    private widget.TextBox TD80;
    private widget.TextBox TD81;
    private widget.TextBox TD82;
    private widget.TextBox TD83;
    private widget.TextBox TD84;
    private widget.TextBox TD85;
    private widget.TextBox TD86;
    private widget.TextBox TD87;
    private widget.TextBox TD88;
    private widget.TextBox TD89;
    private widget.TextBox TD9;
    private widget.TextBox TD90;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.Tanggal Tanggal;
    private widget.TextBox TglLahir;
    private widget.Button btnPetugas;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel12;
    private widget.Label jLabel13;
    private widget.Label jLabel14;
    private widget.Label jLabel15;
    private widget.Label jLabel16;
    private widget.Label jLabel18;
    private widget.Label jLabel19;
    private widget.Label jLabel21;
    private widget.Label jLabel24;
    private widget.Label jLabel30;
    private widget.Label jLabel31;
    private widget.Label jLabel32;
    private widget.Label jLabel33;
    private widget.Label jLabel34;
    private widget.Label jLabel36;
    private widget.Label jLabel37;
    private widget.Label jLabel38;
    private widget.Label jLabel4;
    private widget.Label jLabel53;
    private widget.Label jLabel54;
    private widget.Label jLabel55;
    private widget.Label jLabel58;
    private widget.Label jLabel59;
    private widget.Label jLabel6;
    private widget.Label jLabel60;
    private widget.Label jLabel61;
    private widget.Label jLabel62;
    private widget.Label jLabel63;
    private widget.Label jLabel64;
    private widget.Label jLabel65;
    private widget.Label jLabel66;
    private widget.Label jLabel67;
    private widget.Label jLabel68;
    private widget.Label jLabel69;
    private widget.Label jLabel7;
    private widget.Label jLabel70;
    private widget.Label jLabel71;
    private widget.Label jLabel8;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.ScrollPane scrollInput;
    private widget.Table tbObat;
    // End of variables declaration//GEN-END:variables
    
    public void tampil() {
        Valid.tabelKosong(tabMode);
        try{
            if(TCari.getText().toString().trim().equals("")){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,reg_periksa.umurdaftar,reg_periksa.sttsumur,"+
                    "pasien.jk,pasien.tgl_lahir,catatan_observasi_ranap.tgl_perawatan,catatan_observasi_ranap.jam_rawat,catatan_observasi_ranap.gcs,"+
                    "catatan_observasi_ranap.td,catatan_observasi_ranap.hr,catatan_observasi_ranap.rr,catatan_observasi_ranap.suhu,catatan_observasi_ranap.spo2,"+
                    "catatan_observasi_ranap.nip,petugas.nama from catatan_observasi_ranap inner join reg_periksa on catatan_observasi_ranap.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join petugas on catatan_observasi_ranap.nip=petugas.nip where "+
                    "catatan_observasi_ranap.tgl_perawatan between ? and ? order by catatan_observasi_ranap.tgl_perawatan");
            }else{
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,reg_periksa.umurdaftar,reg_periksa.sttsumur,"+
                    "pasien.jk,pasien.tgl_lahir,catatan_observasi_ranap.tgl_perawatan,catatan_observasi_ranap.jam_rawat,catatan_observasi_ranap.gcs,"+
                    "catatan_observasi_ranap.td,catatan_observasi_ranap.hr,catatan_observasi_ranap.rr,catatan_observasi_ranap.suhu,catatan_observasi_ranap.spo2,"+
                    "catatan_observasi_ranap.nip,petugas.nama from catatan_observasi_ranap inner join reg_periksa on catatan_observasi_ranap.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join petugas on catatan_observasi_ranap.nip=petugas.nip where "+
                    "catatan_observasi_ranap.tgl_perawatan between ? and ? and (reg_periksa.no_rawat like ? or pasien.no_rkm_medis like ? or pasien.nm_pasien like ? or catatan_observasi_ranap.nip like ? or petugas.nama like ?) "+
                    "order by catatan_observasi_ranap.tgl_perawatan ");
            }
                
            try {
                if(TCari.getText().toString().trim().equals("")){
                    ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
                    ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
                }else{
                    ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
                    ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
                    ps.setString(3,"%"+TCari.getText()+"%");
                    ps.setString(4,"%"+TCari.getText()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                }
                    
                rs=ps.executeQuery();
                while(rs.next()){
                    tabMode.addRow(new String[]{
                        rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),
                        rs.getString("umurdaftar")+" "+rs.getString("sttsumur"),rs.getString("jk"),rs.getString("tgl_lahir"),
                        rs.getString("tgl_perawatan"),rs.getString("jam_rawat"),rs.getString("gcs"),rs.getString("td"),
                        rs.getString("hr"),rs.getString("rr"),rs.getString("suhu"),rs.getString("spo2"),rs.getString("nip"),
                        rs.getString("nama")
                    });
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
        }catch(SQLException e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+tabMode.getRowCount());
    }
    
    public void emptTeks() {
        GCS.setText("");
        TD.setText("");
        HR.setText("");
        RR.setText("");
        Suhu.setText("");
        SPO.setText("");
        Tanggal.setDate(new Date());
        GCS.requestFocus();
    } 

    private void getData() {
        if(tbObat.getSelectedRow()!= -1){
            TNoRw.setText(tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
            TNoRM.setText(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString());
            TPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(),2).toString());
            TglLahir.setText(tbObat.getValueAt(tbObat.getSelectedRow(),5).toString());
            Jam.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),7).toString().substring(0,2));
            Menit.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),7).toString().substring(3,5));
            Detik.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),7).toString().substring(6,8));
            GCS.setText(tbObat.getValueAt(tbObat.getSelectedRow(),8).toString());
            TD.setText(tbObat.getValueAt(tbObat.getSelectedRow(),9).toString());
            HR.setText(tbObat.getValueAt(tbObat.getSelectedRow(),10).toString());
            RR.setText(tbObat.getValueAt(tbObat.getSelectedRow(),11).toString());
            Suhu.setText(tbObat.getValueAt(tbObat.getSelectedRow(),12).toString());
            SPO.setText(tbObat.getValueAt(tbObat.getSelectedRow(),13).toString());
            Valid.SetTgl(Tanggal,tbObat.getValueAt(tbObat.getSelectedRow(),6).toString());  
        }
    }
    private void isRawat() {
         Sequel.cariIsi("select reg_periksa.no_rkm_medis from reg_periksa where reg_periksa.no_rawat='"+TNoRw.getText()+"' ",TNoRM);
    }

    private void isPsien() {
        Sequel.cariIsi("select pasien.nm_pasien from pasien where pasien.no_rkm_medis='"+TNoRM.getText()+"' ",TPasien);
        Sequel.cariIsi("select date_format(pasien.tgl_lahir,'%d-%m-%Y') from pasien where pasien.no_rkm_medis=? ",TglLahir,TNoRM.getText());
    }
    
    public void setNoRm(String norwt, Date tgl2) {
        TNoRw.setText(norwt);
        TCari.setText(norwt);
        Sequel.cariIsi("select reg_periksa.tgl_registrasi from reg_periksa where reg_periksa.no_rawat='"+norwt+"'", DTPCari1);
        DTPCari2.setDate(tgl2);
        isRawat();
        isPsien();
        ChkInput.setSelected(true);
        isForm();
    }
    
    private void isForm(){
        if(ChkInput.isSelected()==true){
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH,520));
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
        BtnSimpan.setEnabled(akses.getcatatan_observasi_ranap());
        BtnHapus.setEnabled(akses.getcatatan_observasi_ranap());
        BtnEdit.setEnabled(akses.getcatatan_observasi_ranap());
        BtnPrint.setEnabled(akses.getcatatan_observasi_ranap()); 
        if(akses.getjml2()>=1){
            NIP.setEditable(false);
            btnPetugas.setEnabled(false);
            NIP.setText(akses.getkode());
            Sequel.cariIsi("select petugas.nama from petugas where petugas.nip=?", NamaPetugas,NIP.getText());
            if(NamaPetugas.getText().equals("")){
                NIP.setText("");
                JOptionPane.showMessageDialog(null,"User login bukan petugas...!!");
            }
        }            
    }

    private void jam(){
        ActionListener taskPerformer = new ActionListener(){
            private int nilai_jam;
            private int nilai_menit;
            private int nilai_detik;
            public void actionPerformed(ActionEvent e) {
                String nol_jam = "";
                String nol_menit = "";
                String nol_detik = "";
                
                Date now = Calendar.getInstance().getTime();

                // Mengambil nilaj JAM, MENIT, dan DETIK Sekarang
                if(ChkKejadian.isSelected()==true){
                    nilai_jam = now.getHours();
                    nilai_menit = now.getMinutes();
                    nilai_detik = now.getSeconds();
                }else if(ChkKejadian.isSelected()==false){
                    nilai_jam =Jam.getSelectedIndex();
                    nilai_menit =Menit.getSelectedIndex();
                    nilai_detik =Detik.getSelectedIndex();
                }

                // Jika nilai JAM lebih kecil dari 10 (hanya 1 digit)
                if (nilai_jam <= 9) {
                    // Tambahkan "0" didepannya
                    nol_jam = "0";
                }
                // Jika nilai MENIT lebih kecil dari 10 (hanya 1 digit)
                if (nilai_menit <= 9) {
                    // Tambahkan "0" didepannya
                    nol_menit = "0";
                }
                // Jika nilai DETIK lebih kecil dari 10 (hanya 1 digit)
                if (nilai_detik <= 9) {
                    // Tambahkan "0" didepannya
                    nol_detik = "0";
                }
                // Membuat String JAM, MENIT, DETIK
                String jam = nol_jam + Integer.toString(nilai_jam);
                String menit = nol_menit + Integer.toString(nilai_menit);
                String detik = nol_detik + Integer.toString(nilai_detik);
                // Menampilkan pada Layar
                //tampil_jam.setText("  " + jam + " : " + menit + " : " + detik + "  ");
                Jam.setSelectedItem(jam);
                Menit.setSelectedItem(menit);
                Detik.setSelectedItem(detik);
            }
        };
        // Timer
        new Timer(1000, taskPerformer).start();
    }

    private void ganti() {
        Sequel.mengedit("catatan_observasi_ranap","tgl_perawatan=? and jam_rawat=? and no_rawat=?","no_rawat=?,tgl_perawatan=?,jam_rawat=?,gcs=?,td=?,hr=?,rr=?,suhu=?,spo2=?,nip=?",13,new String[]{
            TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+""),Jam.getSelectedItem()+":"+Menit.getSelectedItem()+":"+Detik.getSelectedItem(),
            GCS.getText(),TD.getText(),HR.getText(),RR.getText(),Suhu.getText(),SPO.getText(),NIP.getText(),tbObat.getValueAt(tbObat.getSelectedRow(),6).toString(),
            tbObat.getValueAt(tbObat.getSelectedRow(),7).toString(),tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()
        });
        if(tabMode.getRowCount()!=0){tampil();}
        emptTeks();
    }

    private void hapus() {
        if(Sequel.queryu2tf("delete from catatan_observasi_ranap where tgl_perawatan=? and jam_rawat=? and no_rawat=?",3,new String[]{
            tbObat.getValueAt(tbObat.getSelectedRow(),6).toString(),tbObat.getValueAt(tbObat.getSelectedRow(),7).toString(),tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()
        })==true){
            tampil();
            emptTeks();
        }else{
            JOptionPane.showMessageDialog(null,"Gagal menghapus..!!");
        }
    }
    
    
}
