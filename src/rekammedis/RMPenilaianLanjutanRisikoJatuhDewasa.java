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
public final class RMPenilaianLanjutanRisikoJatuhDewasa extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private PreparedStatement ps;
    private ResultSet rs;
    private int i=0;    
    private DlgCariPetugas petugas=new DlgCariPetugas(null,false);
    private String finger="";
    /** Creates new form DlgRujuk
     * @param parent
     * @param modal */
    public RMPenilaianLanjutanRisikoJatuhDewasa(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(8,1);
        setSize(628,674);

        tabMode=new DefaultTableModel(null,new Object[]{
            "No.Rawat","No.R.M.","Nama Pasien","Tgl.Lahir","JK","Tanggal","Skala Morse 1","N.M. 1",
            "Skala Morse 2","N.M. 2","Skala Morse 3","N.M. 3","Skala Morse 4","N.M. 4","Skala Morse 5","N.M. 5",
            "Skala Morse 6","N.M. 6","Total","Hasil Skrining","Saran","NIP","Petugas"
        }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbObat.setModel(tabMode);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbObat.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 23; i++) {
            TableColumn column = tbObat.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(105);
            }else if(i==1){
                column.setPreferredWidth(65);
            }else if(i==2){
                column.setPreferredWidth(160);
            }else if(i==3){
                column.setPreferredWidth(65);
            }else if(i==4){
                column.setPreferredWidth(25);
            }else if(i==5){
                column.setPreferredWidth(115);
            }else if(i==6){
                column.setPreferredWidth(75);
            }else if(i==7){
                column.setPreferredWidth(40);
            }else if(i==8){
                column.setPreferredWidth(75);
            }else if(i==9){
                column.setPreferredWidth(40);
            }else if(i==10){
                column.setPreferredWidth(75);
            }else if(i==11){
                column.setPreferredWidth(40);
            }else if(i==12){
                column.setPreferredWidth(75);
            }else if(i==13){
                column.setPreferredWidth(40);
            }else if(i==14){
                column.setPreferredWidth(75);
            }else if(i==15){
                column.setPreferredWidth(40);
            }else if(i==16){
                column.setPreferredWidth(75);
            }else if(i==17){
                column.setPreferredWidth(40);
            }else if(i==18){
                column.setPreferredWidth(40);
            }else if(i==19){
                column.setPreferredWidth(200);
            }else if(i==20){
                column.setPreferredWidth(200);
            }else if(i==21){
                column.setPreferredWidth(85);
            }else if(i==22){
                column.setPreferredWidth(150);
            }
        }
        tbObat.setDefaultRenderer(Object.class, new WarnaTable());

        TNoRw.setDocument(new batasInput((byte)17).getKata(TNoRw));
        NIP.setDocument(new batasInput((byte)20).getKata(NIP));
        HasilSkrining.setDocument(new batasInput((int)200).getKata(HasilSkrining));
        Saran.setDocument(new batasInput((int)200).getKata(Saran));
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
        MnPenilaianLanjutanRisikoJatuh = new javax.swing.JMenuItem();
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
        jLabel57 = new widget.Label();
        jLabel217 = new widget.Label();
        jLabel219 = new widget.Label();
        jLabel218 = new widget.Label();
        NilaiResiko1 = new widget.TextBox();
        jLabel220 = new widget.Label();
        jLabel221 = new widget.Label();
        SkalaResiko2 = new widget.ComboBox();
        jLabel222 = new widget.Label();
        NilaiResiko2 = new widget.TextBox();
        jLabel223 = new widget.Label();
        jLabel224 = new widget.Label();
        SkalaResiko3 = new widget.ComboBox();
        jLabel225 = new widget.Label();
        NilaiResiko3 = new widget.TextBox();
        jLabel226 = new widget.Label();
        jLabel227 = new widget.Label();
        SkalaResiko4 = new widget.ComboBox();
        jLabel228 = new widget.Label();
        NilaiResiko4 = new widget.TextBox();
        jLabel229 = new widget.Label();
        jLabel230 = new widget.Label();
        SkalaResiko5 = new widget.ComboBox();
        jLabel231 = new widget.Label();
        NilaiResiko5 = new widget.TextBox();
        jLabel232 = new widget.Label();
        jLabel233 = new widget.Label();
        SkalaResiko6 = new widget.ComboBox();
        jLabel234 = new widget.Label();
        NilaiResiko6 = new widget.TextBox();
        TingkatResiko = new widget.Label();
        jLabel235 = new widget.Label();
        NilaiResikoTotal = new widget.TextBox();
        jLabel30 = new widget.Label();
        scrollPane1 = new widget.ScrollPane();
        HasilSkrining = new widget.TextArea();
        jLabel31 = new widget.Label();
        scrollPane2 = new widget.ScrollPane();
        Saran = new widget.TextArea();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel58 = new widget.Label();
        jLabel32 = new widget.Label();
        jLabel236 = new widget.Label();
        jLabel237 = new widget.Label();
        jLabel238 = new widget.Label();
        jLabel239 = new widget.Label();
        jLabel240 = new widget.Label();
        jLabel241 = new widget.Label();
        jLabel242 = new widget.Label();
        jLabel243 = new widget.Label();
        jLabel244 = new widget.Label();
        jLabel245 = new widget.Label();
        jLabel246 = new widget.Label();
        jLabel247 = new widget.Label();
        jLabel33 = new widget.Label();
        jLabel248 = new widget.Label();
        jLabel249 = new widget.Label();
        jLabel250 = new widget.Label();
        jLabel251 = new widget.Label();
        jLabel34 = new widget.Label();
        jLabel253 = new widget.Label();
        jLabel255 = new widget.Label();
        jLabel252 = new widget.Label();
        jLabel254 = new widget.Label();
        jLabel256 = new widget.Label();
        jLabel257 = new widget.Label();
        SkalaResiko1 = new widget.ComboBox();
        SkalaResiko9 = new widget.ComboBox();
        SkalaResiko30 = new widget.ComboBox();
        SkalaResiko31 = new widget.ComboBox();
        SkalaResiko32 = new widget.ComboBox();
        SkalaResiko33 = new widget.ComboBox();
        SkalaResiko34 = new widget.ComboBox();
        SkalaResiko35 = new widget.ComboBox();
        SkalaResiko36 = new widget.ComboBox();
        SkalaResiko37 = new widget.ComboBox();
        SkalaResiko38 = new widget.ComboBox();
        SkalaResiko39 = new widget.ComboBox();
        SkalaResiko40 = new widget.ComboBox();
        SkalaResiko41 = new widget.ComboBox();
        SkalaResiko42 = new widget.ComboBox();
        SkalaResiko43 = new widget.ComboBox();
        SkalaResiko44 = new widget.ComboBox();
        SkalaResiko45 = new widget.ComboBox();
        SkalaResiko46 = new widget.ComboBox();
        SkalaResiko47 = new widget.ComboBox();
        SkalaResiko48 = new widget.ComboBox();
        SkalaResiko49 = new widget.ComboBox();
        SkalaResiko50 = new widget.ComboBox();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        MnPenilaianLanjutanRisikoJatuh.setBackground(new java.awt.Color(255, 255, 254));
        MnPenilaianLanjutanRisikoJatuh.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnPenilaianLanjutanRisikoJatuh.setForeground(new java.awt.Color(50, 50, 50));
        MnPenilaianLanjutanRisikoJatuh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnPenilaianLanjutanRisikoJatuh.setText("Formulir Penilaian Lanjutan Risiko Jatuh Dewasa");
        MnPenilaianLanjutanRisikoJatuh.setName("MnPenilaianLanjutanRisikoJatuh"); // NOI18N
        MnPenilaianLanjutanRisikoJatuh.setPreferredSize(new java.awt.Dimension(290, 26));
        MnPenilaianLanjutanRisikoJatuh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnPenilaianLanjutanRisikoJatuhActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnPenilaianLanjutanRisikoJatuh);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Penilaian Lanjutan Risiko Jatuh Pasien Dewasa (MORSE FALL) ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);
        Scroll.setPreferredSize(new java.awt.Dimension(452, 200));

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
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "19-04-2023" }));
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
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "19-04-2023" }));
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
        PanelInput.setPreferredSize(new java.awt.Dimension(192, 466));
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
        FormInput.setBorder(null);
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(100, 810));
        FormInput.setLayout(null);

        jLabel4.setText("No.Rawat :");
        jLabel4.setName("jLabel4"); // NOI18N
        FormInput.add(jLabel4);
        jLabel4.setBounds(0, 10, 80, 23);

        TNoRw.setHighlighter(null);
        TNoRw.setName("TNoRw"); // NOI18N
        TNoRw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRwKeyPressed(evt);
            }
        });
        FormInput.add(TNoRw);
        TNoRw.setBounds(84, 10, 136, 23);

        TPasien.setEditable(false);
        TPasien.setHighlighter(null);
        TPasien.setName("TPasien"); // NOI18N
        TPasien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TPasienKeyPressed(evt);
            }
        });
        FormInput.add(TPasien);
        TPasien.setBounds(336, 10, 285, 23);

        Tanggal.setForeground(new java.awt.Color(50, 70, 50));
        Tanggal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "19-04-2023" }));
        Tanggal.setDisplayFormat("dd-MM-yyyy");
        Tanggal.setName("Tanggal"); // NOI18N
        Tanggal.setOpaque(false);
        Tanggal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TanggalKeyPressed(evt);
            }
        });
        FormInput.add(Tanggal);
        Tanggal.setBounds(84, 40, 90, 23);

        TNoRM.setEditable(false);
        TNoRM.setHighlighter(null);
        TNoRM.setName("TNoRM"); // NOI18N
        TNoRM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRMKeyPressed(evt);
            }
        });
        FormInput.add(TNoRM);
        TNoRM.setBounds(222, 10, 112, 23);

        jLabel16.setText("Tanggal :");
        jLabel16.setName("jLabel16"); // NOI18N
        jLabel16.setVerifyInputWhenFocusTarget(false);
        FormInput.add(jLabel16);
        jLabel16.setBounds(0, 40, 80, 23);

        Jam.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        Jam.setName("Jam"); // NOI18N
        Jam.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JamKeyPressed(evt);
            }
        });
        FormInput.add(Jam);
        Jam.setBounds(178, 40, 62, 23);

        Menit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        Menit.setName("Menit"); // NOI18N
        Menit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MenitKeyPressed(evt);
            }
        });
        FormInput.add(Menit);
        Menit.setBounds(243, 40, 62, 23);

        Detik.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        Detik.setName("Detik"); // NOI18N
        Detik.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DetikKeyPressed(evt);
            }
        });
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
        NIP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NIPKeyPressed(evt);
            }
        });
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
        btnPetugas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPetugasActionPerformed(evt);
            }
        });
        btnPetugas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPetugasKeyPressed(evt);
            }
        });
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

        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel57.setText("Intervensi Pencegahan Pasien Jatuh :");
        jLabel57.setName("jLabel57"); // NOI18N
        FormInput.add(jLabel57);
        jLabel57.setBounds(20, 450, 300, 23);

        jLabel217.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel217.setText("2. Tempatkan bel panggilan dalam jangkauan tangan pasien");
        jLabel217.setName("jLabel217"); // NOI18N
        FormInput.add(jLabel217);
        jLabel217.setBounds(30, 510, 310, 23);

        jLabel219.setText("Skala :");
        jLabel219.setName("jLabel219"); // NOI18N
        FormInput.add(jLabel219);
        jLabel219.setBounds(280, 90, 80, 23);

        jLabel218.setText("Nilai :");
        jLabel218.setName("jLabel218"); // NOI18N
        FormInput.add(jLabel218);
        jLabel218.setBounds(655, 90, 70, 23);

        NilaiResiko1.setEditable(false);
        NilaiResiko1.setFocusTraversalPolicyProvider(true);
        NilaiResiko1.setName("NilaiResiko1"); // NOI18N
        FormInput.add(NilaiResiko1);
        NilaiResiko1.setBounds(729, 90, 60, 23);

        jLabel220.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel220.setText("2. Diagnosis Sekunder ( Lebih dari 1  Diagnosis Medis)");
        jLabel220.setName("jLabel220"); // NOI18N
        FormInput.add(jLabel220);
        jLabel220.setBounds(34, 120, 260, 23);

        jLabel221.setText("Skala :");
        jLabel221.setName("jLabel221"); // NOI18N
        FormInput.add(jLabel221);
        jLabel221.setBounds(280, 120, 80, 23);

        SkalaResiko2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        SkalaResiko2.setName("SkalaResiko2"); // NOI18N
        SkalaResiko2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko2ItemStateChanged(evt);
            }
        });
        SkalaResiko2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko2KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko2);
        SkalaResiko2.setBounds(364, 120, 280, 23);

        jLabel222.setText("Nilai :");
        jLabel222.setName("jLabel222"); // NOI18N
        FormInput.add(jLabel222);
        jLabel222.setBounds(655, 120, 70, 23);

        NilaiResiko2.setEditable(false);
        NilaiResiko2.setFocusTraversalPolicyProvider(true);
        NilaiResiko2.setName("NilaiResiko2"); // NOI18N
        FormInput.add(NilaiResiko2);
        NilaiResiko2.setBounds(729, 120, 60, 23);

        jLabel223.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel223.setText("3. Alat Bantu");
        jLabel223.setName("jLabel223"); // NOI18N
        FormInput.add(jLabel223);
        jLabel223.setBounds(34, 150, 260, 23);

        jLabel224.setText("Skala :");
        jLabel224.setName("jLabel224"); // NOI18N
        FormInput.add(jLabel224);
        jLabel224.setBounds(280, 150, 80, 23);

        SkalaResiko3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada, Tirah Baring/bed rest/imobilisasi", "Kruk, tongkat, tripod, walker", "Benda disekitar; dinding, kursi roda" }));
        SkalaResiko3.setName("SkalaResiko3"); // NOI18N
        SkalaResiko3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko3ItemStateChanged(evt);
            }
        });
        SkalaResiko3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko3KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko3);
        SkalaResiko3.setBounds(364, 150, 280, 23);

        jLabel225.setText("Nilai :");
        jLabel225.setName("jLabel225"); // NOI18N
        FormInput.add(jLabel225);
        jLabel225.setBounds(655, 150, 70, 23);

        NilaiResiko3.setEditable(false);
        NilaiResiko3.setFocusTraversalPolicyProvider(true);
        NilaiResiko3.setName("NilaiResiko3"); // NOI18N
        FormInput.add(NilaiResiko3);
        NilaiResiko3.setBounds(729, 150, 60, 23);

        jLabel226.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel226.setText("4. Medikasi");
        jLabel226.setName("jLabel226"); // NOI18N
        FormInput.add(jLabel226);
        jLabel226.setBounds(34, 180, 260, 23);

        jLabel227.setText("Skala :");
        jLabel227.setName("jLabel227"); // NOI18N
        FormInput.add(jLabel227);
        jLabel227.setBounds(280, 180, 80, 23);

        SkalaResiko4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "heparin, obat sedatif, post operasi 24 jam terakhir", "Terpasang Infus" }));
        SkalaResiko4.setName("SkalaResiko4"); // NOI18N
        SkalaResiko4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko4ItemStateChanged(evt);
            }
        });
        SkalaResiko4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko4KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko4);
        SkalaResiko4.setBounds(364, 180, 280, 23);

        jLabel228.setText("Nilai :");
        jLabel228.setName("jLabel228"); // NOI18N
        FormInput.add(jLabel228);
        jLabel228.setBounds(655, 180, 70, 23);

        NilaiResiko4.setEditable(false);
        NilaiResiko4.setFocusTraversalPolicyProvider(true);
        NilaiResiko4.setName("NilaiResiko4"); // NOI18N
        FormInput.add(NilaiResiko4);
        NilaiResiko4.setBounds(729, 180, 60, 23);

        jLabel229.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel229.setText("5. Gaya Berjalan");
        jLabel229.setName("jLabel229"); // NOI18N
        FormInput.add(jLabel229);
        jLabel229.setBounds(34, 210, 260, 23);

        jLabel230.setText("Skala :");
        jLabel230.setName("jLabel230"); // NOI18N
        FormInput.add(jLabel230);
        jLabel230.setBounds(280, 210, 80, 23);

        SkalaResiko5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Lemah (tidak bertenaga)", "Terganggu (tidak normal/pincang/diseret)" }));
        SkalaResiko5.setName("SkalaResiko5"); // NOI18N
        SkalaResiko5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko5ItemStateChanged(evt);
            }
        });
        SkalaResiko5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko5KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko5);
        SkalaResiko5.setBounds(364, 210, 280, 23);

        jLabel231.setText("Nilai :");
        jLabel231.setName("jLabel231"); // NOI18N
        FormInput.add(jLabel231);
        jLabel231.setBounds(655, 210, 70, 23);

        NilaiResiko5.setEditable(false);
        NilaiResiko5.setFocusTraversalPolicyProvider(true);
        NilaiResiko5.setName("NilaiResiko5"); // NOI18N
        FormInput.add(NilaiResiko5);
        NilaiResiko5.setBounds(729, 210, 60, 23);

        jLabel232.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel232.setText("6. Status Mental");
        jLabel232.setName("jLabel232"); // NOI18N
        FormInput.add(jLabel232);
        jLabel232.setBounds(34, 240, 260, 23);

        jLabel233.setText("Skala :");
        jLabel233.setName("jLabel233"); // NOI18N
        FormInput.add(jLabel233);
        jLabel233.setBounds(280, 240, 80, 23);

        SkalaResiko6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Menyadari kemampuan", "Lupa/pelupa" }));
        SkalaResiko6.setName("SkalaResiko6"); // NOI18N
        SkalaResiko6.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko6ItemStateChanged(evt);
            }
        });
        SkalaResiko6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko6KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko6);
        SkalaResiko6.setBounds(364, 240, 280, 23);

        jLabel234.setText("Nilai :");
        jLabel234.setName("jLabel234"); // NOI18N
        FormInput.add(jLabel234);
        jLabel234.setBounds(655, 240, 70, 23);

        NilaiResiko6.setEditable(false);
        NilaiResiko6.setFocusTraversalPolicyProvider(true);
        NilaiResiko6.setName("NilaiResiko6"); // NOI18N
        FormInput.add(NilaiResiko6);
        NilaiResiko6.setBounds(729, 240, 60, 23);

        TingkatResiko.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        TingkatResiko.setText("Tingkat Resiko : Risiko Rendah (skala 0-24), Tindakan : Intervensi setiap  shift.");
        TingkatResiko.setToolTipText("");
        TingkatResiko.setName("TingkatResiko"); // NOI18N
        FormInput.add(TingkatResiko);
        TingkatResiko.setBounds(34, 270, 640, 23);

        jLabel235.setText("Total :");
        jLabel235.setName("jLabel235"); // NOI18N
        FormInput.add(jLabel235);
        jLabel235.setBounds(655, 270, 70, 23);

        NilaiResikoTotal.setEditable(false);
        NilaiResikoTotal.setFocusTraversalPolicyProvider(true);
        NilaiResikoTotal.setName("NilaiResikoTotal"); // NOI18N
        FormInput.add(NilaiResikoTotal);
        NilaiResikoTotal.setBounds(729, 270, 60, 23);

        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel30.setText("Resiko Jatuh Tinggi :");
        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel30.setName("jLabel30"); // NOI18N
        FormInput.add(jLabel30);
        jLabel30.setBounds(30, 470, 130, 23);

        scrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane1.setName("scrollPane1"); // NOI18N

        HasilSkrining.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        HasilSkrining.setColumns(20);
        HasilSkrining.setRows(5);
        HasilSkrining.setName("HasilSkrining"); // NOI18N
        HasilSkrining.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HasilSkriningKeyPressed(evt);
            }
        });
        scrollPane1.setViewportView(HasilSkrining);

        FormInput.add(scrollPane1);
        scrollPane1.setBounds(34, 320, 755, 43);

        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel31.setText("Saran :");
        jLabel31.setName("jLabel31"); // NOI18N
        FormInput.add(jLabel31);
        jLabel31.setBounds(14, 370, 80, 23);

        scrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane2.setName("scrollPane2"); // NOI18N

        Saran.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Saran.setColumns(20);
        Saran.setRows(5);
        Saran.setName("Saran"); // NOI18N
        Saran.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SaranKeyPressed(evt);
            }
        });
        scrollPane2.setViewportView(Saran);

        FormInput.add(scrollPane2);
        scrollPane2.setBounds(34, 390, 755, 43);

        jSeparator2.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator2.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator2.setName("jSeparator2"); // NOI18N
        FormInput.add(jSeparator2);
        jSeparator2.setBounds(0, 70, 810, 1);

        jSeparator3.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator3.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator3.setName("jSeparator3"); // NOI18N
        FormInput.add(jSeparator3);
        jSeparator3.setBounds(0, 300, 810, 1);

        jSeparator4.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator4.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator4.setName("jSeparator4"); // NOI18N
        FormInput.add(jSeparator4);
        jSeparator4.setBounds(0, 370, 810, 1);

        jSeparator5.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator5.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator5.setName("jSeparator5"); // NOI18N
        FormInput.add(jSeparator5);
        jSeparator5.setBounds(0, 440, 950, 2);

        jLabel58.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel58.setText("Skala Morse :");
        jLabel58.setName("jLabel58"); // NOI18N
        FormInput.add(jLabel58);
        jLabel58.setBounds(14, 70, 80, 23);

        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel32.setText("Hasil Skrining :");
        jLabel32.setName("jLabel32"); // NOI18N
        FormInput.add(jLabel32);
        jLabel32.setBounds(14, 300, 80, 23);

        jLabel236.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel236.setText("1. Riwayat Jatuh (1 Tahun Terakhir)");
        jLabel236.setName("jLabel236"); // NOI18N
        FormInput.add(jLabel236);
        jLabel236.setBounds(34, 90, 260, 23);

        jLabel237.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel237.setText("1. Sarankan untuk minta bantuan");
        jLabel237.setName("jLabel237"); // NOI18N
        FormInput.add(jLabel237);
        jLabel237.setBounds(30, 490, 310, 23);

        jLabel238.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel238.setText("4. Pastikan tempat tidur dalam posisi rendah atau roda terkunci");
        jLabel238.setName("jLabel238"); // NOI18N
        FormInput.add(jLabel238);
        jLabel238.setBounds(30, 550, 310, 23);

        jLabel239.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel239.setText("3. Tempatkan benda-benda milik pasien di dekat pasien");
        jLabel239.setName("jLabel239"); // NOI18N
        FormInput.add(jLabel239);
        jLabel239.setBounds(30, 530, 310, 23);

        jLabel240.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel240.setText("5. Pastikan celana panjang atau sarung pasien siatas mata kaki");
        jLabel240.setName("jLabel240"); // NOI18N
        FormInput.add(jLabel240);
        jLabel240.setBounds(30, 570, 310, 23);

        jLabel241.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel241.setText("6. Bantu pasien saat transfer / ambulasi / berpindah");
        jLabel241.setName("jLabel241"); // NOI18N
        FormInput.add(jLabel241);
        jLabel241.setBounds(30, 590, 310, 23);

        jLabel242.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel242.setText("12. Berikan orientasi ruangan sekitar kepada pasien / penunggu pasien ");
        jLabel242.setName("jLabel242"); // NOI18N
        FormInput.add(jLabel242);
        jLabel242.setBounds(470, 590, 360, 23);

        jLabel243.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel243.setText("7. Pasangkan penganman sisi tempat tidur");
        jLabel243.setName("jLabel243"); // NOI18N
        FormInput.add(jLabel243);
        jLabel243.setBounds(470, 490, 260, 23);

        jLabel244.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel244.setText("8. Pastikan label pasien resiko jatuh terpasang di gelang pasien, RM, dan bed pasien");
        jLabel244.setName("jLabel244"); // NOI18N
        FormInput.add(jLabel244);
        jLabel244.setBounds(470, 510, 460, 23);

        jLabel245.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel245.setText("9. Tawarkan ke pasien untuk ke toiilet setiap 4 jam ");
        jLabel245.setName("jLabel245"); // NOI18N
        FormInput.add(jLabel245);
        jLabel245.setBounds(470, 530, 260, 23);

        jLabel246.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel246.setText("10. Pasangkan tali pengaman bila perlu");
        jLabel246.setName("jLabel246"); // NOI18N
        FormInput.add(jLabel246);
        jLabel246.setBounds(470, 550, 260, 23);

        jLabel247.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel247.setText("11. Beritahukan efek dari obat / anestesi kepasa pasien / keluarga");
        jLabel247.setName("jLabel247"); // NOI18N
        FormInput.add(jLabel247);
        jLabel247.setBounds(470, 570, 260, 23);

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel33.setText("Resiko Jatuh Sedang :");
        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel33.setName("jLabel33"); // NOI18N
        FormInput.add(jLabel33);
        jLabel33.setBounds(30, 620, 130, 23);

        jLabel248.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel248.setText("4. Pastikan tempat tidur dalam posisi rendah atau roda terkunci");
        jLabel248.setName("jLabel248"); // NOI18N
        FormInput.add(jLabel248);
        jLabel248.setBounds(30, 700, 330, 23);

        jLabel249.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel249.setText("3. Tempatkan benda-benda milik pasien di dekat pasien");
        jLabel249.setName("jLabel249"); // NOI18N
        FormInput.add(jLabel249);
        jLabel249.setBounds(30, 680, 310, 23);

        jLabel250.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel250.setText("2. Tempatkan bel panggilan dalam jangkauan tangan pasien");
        jLabel250.setName("jLabel250"); // NOI18N
        FormInput.add(jLabel250);
        jLabel250.setBounds(30, 660, 320, 23);

        jLabel251.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel251.setText("1. Sarankan untuk minta bantuan");
        jLabel251.setName("jLabel251"); // NOI18N
        FormInput.add(jLabel251);
        jLabel251.setBounds(30, 640, 260, 23);

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel34.setText("Resiko Jatuh Rendah :");
        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel34.setName("jLabel34"); // NOI18N
        FormInput.add(jLabel34);
        jLabel34.setBounds(30, 730, 130, 23);

        jLabel253.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel253.setText("1. Monitor kondisi umum pasien dan tanda vital / 8 jam");
        jLabel253.setName("jLabel253"); // NOI18N
        FormInput.add(jLabel253);
        jLabel253.setBounds(30, 750, 260, 23);

        jLabel255.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel255.setText("2. Pastikan pengaman tempat tidur selalu tertutup saat pasien tidur ");
        jLabel255.setName("jLabel255"); // NOI18N
        FormInput.add(jLabel255);
        jLabel255.setBounds(30, 770, 340, 23);

        jLabel252.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel252.setText("8. Pastikan label pasien resiko jatuh terpasang di gelang pasien, RM, dan bed pasien");
        jLabel252.setName("jLabel252"); // NOI18N
        FormInput.add(jLabel252);
        jLabel252.setBounds(470, 700, 440, 23);

        jLabel254.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel254.setText("5. Pastikan celana panjang atau sarung pasien siatas mata kaki");
        jLabel254.setName("jLabel254"); // NOI18N
        FormInput.add(jLabel254);
        jLabel254.setBounds(470, 640, 260, 23);

        jLabel256.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel256.setText("6. Bantu pasien saat transfer / ambulasi / berpindah");
        jLabel256.setName("jLabel256"); // NOI18N
        FormInput.add(jLabel256);
        jLabel256.setBounds(470, 660, 260, 23);

        jLabel257.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel257.setText("7. Pasangkan penganman sisi tempat tidur");
        jLabel257.setName("jLabel257"); // NOI18N
        FormInput.add(jLabel257);
        jLabel257.setBounds(470, 680, 260, 23);

        SkalaResiko1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        SkalaResiko1.setName("SkalaResiko1"); // NOI18N
        SkalaResiko1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko1ItemStateChanged(evt);
            }
        });
        SkalaResiko1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko1KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko1);
        SkalaResiko1.setBounds(364, 90, 280, 23);

        SkalaResiko9.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        SkalaResiko9.setName("SkalaResiko9"); // NOI18N
        SkalaResiko9.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko9ItemStateChanged(evt);
            }
        });
        SkalaResiko9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko9KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko9);
        SkalaResiko9.setBounds(360, 510, 60, 18);

        SkalaResiko30.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        SkalaResiko30.setName("SkalaResiko30"); // NOI18N
        SkalaResiko30.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko30ItemStateChanged(evt);
            }
        });
        SkalaResiko30.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko30KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko30);
        SkalaResiko30.setBounds(360, 490, 60, 18);

        SkalaResiko31.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        SkalaResiko31.setName("SkalaResiko31"); // NOI18N
        SkalaResiko31.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko31ItemStateChanged(evt);
            }
        });
        SkalaResiko31.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko31KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko31);
        SkalaResiko31.setBounds(360, 530, 60, 18);

        SkalaResiko32.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        SkalaResiko32.setName("SkalaResiko32"); // NOI18N
        SkalaResiko32.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko32ItemStateChanged(evt);
            }
        });
        SkalaResiko32.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko32KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko32);
        SkalaResiko32.setBounds(360, 550, 60, 18);

        SkalaResiko33.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        SkalaResiko33.setName("SkalaResiko33"); // NOI18N
        SkalaResiko33.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko33ItemStateChanged(evt);
            }
        });
        SkalaResiko33.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko33KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko33);
        SkalaResiko33.setBounds(360, 570, 60, 18);

        SkalaResiko34.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        SkalaResiko34.setName("SkalaResiko34"); // NOI18N
        SkalaResiko34.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko34ItemStateChanged(evt);
            }
        });
        SkalaResiko34.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko34KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko34);
        SkalaResiko34.setBounds(360, 590, 60, 18);

        SkalaResiko35.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        SkalaResiko35.setName("SkalaResiko35"); // NOI18N
        SkalaResiko35.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko35ItemStateChanged(evt);
            }
        });
        SkalaResiko35.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko35KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko35);
        SkalaResiko35.setBounds(880, 590, 60, 18);

        SkalaResiko36.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        SkalaResiko36.setName("SkalaResiko36"); // NOI18N
        SkalaResiko36.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko36ItemStateChanged(evt);
            }
        });
        SkalaResiko36.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko36KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko36);
        SkalaResiko36.setBounds(880, 570, 60, 18);

        SkalaResiko37.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        SkalaResiko37.setName("SkalaResiko37"); // NOI18N
        SkalaResiko37.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko37ItemStateChanged(evt);
            }
        });
        SkalaResiko37.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko37KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko37);
        SkalaResiko37.setBounds(880, 490, 60, 18);

        SkalaResiko38.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        SkalaResiko38.setName("SkalaResiko38"); // NOI18N
        SkalaResiko38.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko38ItemStateChanged(evt);
            }
        });
        SkalaResiko38.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko38KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko38);
        SkalaResiko38.setBounds(880, 510, 60, 18);

        SkalaResiko39.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        SkalaResiko39.setName("SkalaResiko39"); // NOI18N
        SkalaResiko39.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko39ItemStateChanged(evt);
            }
        });
        SkalaResiko39.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko39KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko39);
        SkalaResiko39.setBounds(880, 530, 60, 18);

        SkalaResiko40.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        SkalaResiko40.setName("SkalaResiko40"); // NOI18N
        SkalaResiko40.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko40ItemStateChanged(evt);
            }
        });
        SkalaResiko40.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko40KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko40);
        SkalaResiko40.setBounds(880, 550, 60, 18);

        SkalaResiko41.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        SkalaResiko41.setName("SkalaResiko41"); // NOI18N
        SkalaResiko41.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko41ItemStateChanged(evt);
            }
        });
        SkalaResiko41.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko41KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko41);
        SkalaResiko41.setBounds(360, 700, 60, 18);

        SkalaResiko42.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        SkalaResiko42.setName("SkalaResiko42"); // NOI18N
        SkalaResiko42.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko42ItemStateChanged(evt);
            }
        });
        SkalaResiko42.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko42KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko42);
        SkalaResiko42.setBounds(360, 640, 60, 18);

        SkalaResiko43.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        SkalaResiko43.setName("SkalaResiko43"); // NOI18N
        SkalaResiko43.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko43ItemStateChanged(evt);
            }
        });
        SkalaResiko43.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko43KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko43);
        SkalaResiko43.setBounds(360, 660, 60, 18);

        SkalaResiko44.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        SkalaResiko44.setName("SkalaResiko44"); // NOI18N
        SkalaResiko44.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko44ItemStateChanged(evt);
            }
        });
        SkalaResiko44.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko44KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko44);
        SkalaResiko44.setBounds(360, 680, 60, 18);

        SkalaResiko45.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        SkalaResiko45.setName("SkalaResiko45"); // NOI18N
        SkalaResiko45.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko45ItemStateChanged(evt);
            }
        });
        SkalaResiko45.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko45KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko45);
        SkalaResiko45.setBounds(880, 700, 60, 18);

        SkalaResiko46.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        SkalaResiko46.setName("SkalaResiko46"); // NOI18N
        SkalaResiko46.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko46ItemStateChanged(evt);
            }
        });
        SkalaResiko46.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko46KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko46);
        SkalaResiko46.setBounds(880, 640, 60, 18);

        SkalaResiko47.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        SkalaResiko47.setName("SkalaResiko47"); // NOI18N
        SkalaResiko47.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko47ItemStateChanged(evt);
            }
        });
        SkalaResiko47.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko47KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko47);
        SkalaResiko47.setBounds(880, 660, 60, 18);

        SkalaResiko48.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        SkalaResiko48.setName("SkalaResiko48"); // NOI18N
        SkalaResiko48.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko48ItemStateChanged(evt);
            }
        });
        SkalaResiko48.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko48KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko48);
        SkalaResiko48.setBounds(880, 680, 60, 18);

        SkalaResiko49.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        SkalaResiko49.setName("SkalaResiko49"); // NOI18N
        SkalaResiko49.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko49ItemStateChanged(evt);
            }
        });
        SkalaResiko49.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko49KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko49);
        SkalaResiko49.setBounds(360, 770, 60, 18);

        SkalaResiko50.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        SkalaResiko50.setName("SkalaResiko50"); // NOI18N
        SkalaResiko50.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko50ItemStateChanged(evt);
            }
        });
        SkalaResiko50.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko50KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko50);
        SkalaResiko50.setBounds(360, 750, 60, 18);

        scrollInput.setViewportView(FormInput);

        PanelInput.add(scrollInput, java.awt.BorderLayout.CENTER);

        internalFrame1.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TNoRwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRwKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isRawat();
            isPsien();
        }else{            
            Valid.pindah(evt,TCari,Tanggal);
        }
}//GEN-LAST:event_TNoRwKeyPressed

    private void TPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TPasienKeyPressed
        Valid.pindah(evt,TCari,BtnSimpan);
}//GEN-LAST:event_TPasienKeyPressed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(TNoRw.getText().trim().equals("")||TPasien.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"pasien");
        }else if(NIP.getText().trim().equals("")||NamaPetugas.getText().trim().equals("")){
            Valid.textKosong(NIP,"Petugas");
        }else if(HasilSkrining.getText().trim().equals("")){
            Valid.textKosong(HasilSkrining,"Hasil Skrining");
        }else if(Saran.getText().trim().equals("")){
            Valid.textKosong(Saran,"Saran");
        }else{
            if(Sequel.menyimpantf("penilaian_lanjutan_resiko_jatuh_dewasa","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?","Data",18,new String[]{
                TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Jam.getSelectedItem()+":"+Menit.getSelectedItem()+":"+Detik.getSelectedItem(),
                SkalaResiko1.getSelectedItem().toString(),NilaiResiko1.getText(),SkalaResiko2.getSelectedItem().toString(),NilaiResiko2.getText(),
                SkalaResiko3.getSelectedItem().toString(),NilaiResiko3.getText(),SkalaResiko4.getSelectedItem().toString(),NilaiResiko4.getText(), 
                SkalaResiko5.getSelectedItem().toString(),NilaiResiko5.getText(),SkalaResiko6.getSelectedItem().toString(),NilaiResiko6.getText(),
                NilaiResikoTotal.getText(),HasilSkrining.getText(),Saran.getText(),NIP.getText()
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
            Valid.pindah(evt,Saran,BtnBatal);
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
                if(NIP.getText().equals(tbObat.getValueAt(tbObat.getSelectedRow(),21).toString())){
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
        }else if(HasilSkrining.getText().trim().equals("")){
            Valid.textKosong(HasilSkrining,"Hasil Skrining");
        }else if(Saran.getText().trim().equals("")){
            Valid.textKosong(Saran,"Saran");
        }else{
            if(tbObat.getSelectedRow()>-1){
                if(akses.getkode().equals("Admin Utama")){
                    ganti();
                }else{
                    if(NIP.getText().equals(tbObat.getValueAt(tbObat.getSelectedRow(),21).toString())){
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
                Valid.MyReportqry("rptLanjutanRisikoJatuhDewasa.jasper","report","::[ Data Penilaian Lanjutan Risiko Jatuh Dewasa ]::",
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.jk,pasien.tgl_lahir,penilaian_lanjutan_resiko_jatuh_dewasa.tanggal,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_skala1,penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_nilai1,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_skala2,penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_nilai2,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_skala3,penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_nilai3,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_skala4,penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_nilai4,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_skala5,penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_nilai5,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_skala6,penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_nilai6,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_totalnilai,penilaian_lanjutan_resiko_jatuh_dewasa.hasil_skrining,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.saran,penilaian_lanjutan_resiko_jatuh_dewasa.nip,petugas.nama "+
                    "from penilaian_lanjutan_resiko_jatuh_dewasa inner join reg_periksa on penilaian_lanjutan_resiko_jatuh_dewasa.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join petugas on penilaian_lanjutan_resiko_jatuh_dewasa.nip=petugas.nip where "+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.tanggal between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59' "+
                    "order by penilaian_lanjutan_resiko_jatuh_dewasa.tanggal",param);
            }else{
                Valid.MyReportqry("rptLanjutanRisikoJatuhDewasa.jasper","report","::[ Data Penilaian Lanjutan Risiko Jatuh Dewasa ]::",
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.jk,pasien.tgl_lahir,penilaian_lanjutan_resiko_jatuh_dewasa.tanggal,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_skala1,penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_nilai1,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_skala2,penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_nilai2,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_skala3,penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_nilai3,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_skala4,penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_nilai4,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_skala5,penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_nilai5,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_skala6,penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_nilai6,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_totalnilai,penilaian_lanjutan_resiko_jatuh_dewasa.hasil_skrining,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.saran,penilaian_lanjutan_resiko_jatuh_dewasa.nip,petugas.nama "+
                    "from penilaian_lanjutan_resiko_jatuh_dewasa inner join reg_periksa on penilaian_lanjutan_resiko_jatuh_dewasa.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join petugas on penilaian_lanjutan_resiko_jatuh_dewasa.nip=petugas.nip where "+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.tanggal between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59' and "+
                    "(reg_periksa.no_rawat like '%"+TCari.getText().trim()+"%' or pasien.no_rkm_medis like '%"+TCari.getText().trim()+"%' or pasien.nm_pasien like '%"+TCari.getText().trim()+"%' "+
                    "or penilaian_lanjutan_resiko_jatuh_dewasa.nip like '%"+TCari.getText().trim()+"%' or petugas.nama like '%"+TCari.getText().trim()+"%') "+
                    "order by penilaian_lanjutan_resiko_jatuh_dewasa.tanggal ",param);
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

    private void TanggalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TanggalKeyPressed
        Valid.pindah(evt,TCari,Jam);
}//GEN-LAST:event_TanggalKeyPressed

    private void TNoRMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRMKeyPressed
        // Valid.pindah(evt, TNm, BtnSimpan);
}//GEN-LAST:event_TNoRMKeyPressed

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

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

    private void JamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JamKeyPressed
        Valid.pindah(evt,Tanggal,Menit);
    }//GEN-LAST:event_JamKeyPressed

    private void MenitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MenitKeyPressed
        Valid.pindah(evt,Jam,Detik);
    }//GEN-LAST:event_MenitKeyPressed

    private void DetikKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DetikKeyPressed
        Valid.pindah(evt,Menit,btnPetugas);
    }//GEN-LAST:event_DetikKeyPressed

    private void NIPKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NIPKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            Sequel.cariIsi("select petugas.nama from petugas where petugas.nip=?",NamaPetugas,NIP.getText());
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            Detik.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            //GCS.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            btnPetugasActionPerformed(null);
        }
    }//GEN-LAST:event_NIPKeyPressed

    private void btnPetugasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPetugasActionPerformed
        petugas.emptTeks();
        petugas.isCek();
        petugas.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        petugas.setLocationRelativeTo(internalFrame1);
        petugas.setVisible(true);
    }//GEN-LAST:event_btnPetugasActionPerformed

    private void btnPetugasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPetugasKeyPressed
        //Valid.pindah(evt,Detik,GCS);
    }//GEN-LAST:event_btnPetugasKeyPressed

    private void MnPenilaianLanjutanRisikoJatuhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnPenilaianLanjutanRisikoJatuhActionPerformed
        if(tbObat.getSelectedRow()>-1){
            Map<String, Object> param = new HashMap<>();
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());   
            param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
            finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",tbObat.getValueAt(tbObat.getSelectedRow(),21).toString());
            param.put("finger","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+tbObat.getValueAt(tbObat.getSelectedRow(),22).toString()+"\nID "+(finger.equals("")?tbObat.getValueAt(tbObat.getSelectedRow(),21).toString():finger)+"\n"+Tanggal.getSelectedItem());
            Valid.MyReportqry("rptFormulirPenilaianLanjutanRisikoJatuhDewasa.jasper","report","::[ Formulir Penilaian Lanjutan Risiko Jatuh Dewasa ]::",
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.jk,pasien.tgl_lahir,penilaian_lanjutan_resiko_jatuh_dewasa.tanggal,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_skala1,penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_nilai1,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_skala2,penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_nilai2,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_skala3,penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_nilai3,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_skala4,penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_nilai4,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_skala5,penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_nilai5,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_skala6,penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_nilai6,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_totalnilai,penilaian_lanjutan_resiko_jatuh_dewasa.hasil_skrining,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.saran,penilaian_lanjutan_resiko_jatuh_dewasa.nip,petugas.nama "+
                    "from penilaian_lanjutan_resiko_jatuh_dewasa inner join reg_periksa on penilaian_lanjutan_resiko_jatuh_dewasa.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join petugas on penilaian_lanjutan_resiko_jatuh_dewasa.nip=petugas.nip where reg_periksa.no_rawat='"+tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()+"'",param);
        }
    }//GEN-LAST:event_MnPenilaianLanjutanRisikoJatuhActionPerformed

    private void SkalaResiko2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko2ItemStateChanged
        if(SkalaResiko2.getSelectedIndex()==0){
            NilaiResiko2.setText("0");
        }else{
            NilaiResiko2.setText("15");
        }
        isTotalResikoJatuh();
    }//GEN-LAST:event_SkalaResiko2ItemStateChanged

    private void SkalaResiko2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko2KeyPressed
        Valid.pindah(evt,SkalaResiko1,SkalaResiko3);
    }//GEN-LAST:event_SkalaResiko2KeyPressed

    private void SkalaResiko3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko3ItemStateChanged
        if(SkalaResiko3.getSelectedIndex()==0){
            NilaiResiko3.setText("0");
        }else if(SkalaResiko3.getSelectedIndex()==1){
            NilaiResiko3.setText("15");
        }else{
            NilaiResiko3.setText("30");
        }
        isTotalResikoJatuh();
    }//GEN-LAST:event_SkalaResiko3ItemStateChanged

    private void SkalaResiko3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko3KeyPressed
        Valid.pindah(evt,SkalaResiko2,SkalaResiko4);
    }//GEN-LAST:event_SkalaResiko3KeyPressed

    private void SkalaResiko4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko4ItemStateChanged
        if(SkalaResiko4.getSelectedIndex()==0){
            NilaiResiko4.setText("20");
        }else{
            NilaiResiko4.setText("20");
        }
        isTotalResikoJatuh();
    }//GEN-LAST:event_SkalaResiko4ItemStateChanged

    private void SkalaResiko4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko4KeyPressed
        Valid.pindah(evt,SkalaResiko3,SkalaResiko5);
    }//GEN-LAST:event_SkalaResiko4KeyPressed

    private void SkalaResiko5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko5ItemStateChanged
        if(SkalaResiko5.getSelectedIndex()==0){
            NilaiResiko5.setText("0");
        }else if(SkalaResiko5.getSelectedIndex()==1){
            NilaiResiko5.setText("10");
        }else{
            NilaiResiko5.setText("20");
        }
        isTotalResikoJatuh();
    }//GEN-LAST:event_SkalaResiko5ItemStateChanged

    private void SkalaResiko5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko5KeyPressed
        Valid.pindah(evt,SkalaResiko4,SkalaResiko6);
    }//GEN-LAST:event_SkalaResiko5KeyPressed

    private void SkalaResiko6ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko6ItemStateChanged
        if(SkalaResiko6.getSelectedIndex()==0){
            NilaiResiko6.setText("0");
        }else{
            NilaiResiko6.setText("15");
        }
        isTotalResikoJatuh();
    }//GEN-LAST:event_SkalaResiko6ItemStateChanged

    private void SkalaResiko6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko6KeyPressed
        Valid.pindah(evt,SkalaResiko5,HasilSkrining);
    }//GEN-LAST:event_SkalaResiko6KeyPressed

    private void HasilSkriningKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HasilSkriningKeyPressed
        Valid.pindah2(evt,SkalaResiko6,Saran);
    }//GEN-LAST:event_HasilSkriningKeyPressed

    private void SaranKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SaranKeyPressed
        Valid.pindah2(evt,HasilSkrining,BtnSimpan);
    }//GEN-LAST:event_SaranKeyPressed

    private void SkalaResiko1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko1ItemStateChanged
        if(SkalaResiko1.getSelectedIndex()==0){
            NilaiResiko1.setText("0");
        }else{
            NilaiResiko1.setText("25");
        }
        isTotalResikoJatuh();
    }//GEN-LAST:event_SkalaResiko1ItemStateChanged

    private void SkalaResiko1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko1KeyPressed

    private void SkalaResiko9ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko9ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko9ItemStateChanged

    private void SkalaResiko9KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko9KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko9KeyPressed

    private void SkalaResiko30ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko30ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko30ItemStateChanged

    private void SkalaResiko30KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko30KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko30KeyPressed

    private void SkalaResiko31ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko31ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko31ItemStateChanged

    private void SkalaResiko31KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko31KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko31KeyPressed

    private void SkalaResiko32ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko32ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko32ItemStateChanged

    private void SkalaResiko32KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko32KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko32KeyPressed

    private void SkalaResiko33ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko33ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko33ItemStateChanged

    private void SkalaResiko33KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko33KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko33KeyPressed

    private void SkalaResiko34ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko34ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko34ItemStateChanged

    private void SkalaResiko34KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko34KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko34KeyPressed

    private void SkalaResiko35ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko35ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko35ItemStateChanged

    private void SkalaResiko35KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko35KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko35KeyPressed

    private void SkalaResiko36ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko36ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko36ItemStateChanged

    private void SkalaResiko36KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko36KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko36KeyPressed

    private void SkalaResiko37ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko37ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko37ItemStateChanged

    private void SkalaResiko37KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko37KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko37KeyPressed

    private void SkalaResiko38ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko38ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko38ItemStateChanged

    private void SkalaResiko38KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko38KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko38KeyPressed

    private void SkalaResiko39ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko39ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko39ItemStateChanged

    private void SkalaResiko39KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko39KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko39KeyPressed

    private void SkalaResiko40ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko40ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko40ItemStateChanged

    private void SkalaResiko40KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko40KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko40KeyPressed

    private void SkalaResiko41ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko41ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko41ItemStateChanged

    private void SkalaResiko41KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko41KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko41KeyPressed

    private void SkalaResiko42ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko42ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko42ItemStateChanged

    private void SkalaResiko42KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko42KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko42KeyPressed

    private void SkalaResiko43ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko43ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko43ItemStateChanged

    private void SkalaResiko43KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko43KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko43KeyPressed

    private void SkalaResiko44ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko44ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko44ItemStateChanged

    private void SkalaResiko44KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko44KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko44KeyPressed

    private void SkalaResiko45ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko45ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko45ItemStateChanged

    private void SkalaResiko45KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko45KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko45KeyPressed

    private void SkalaResiko46ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko46ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko46ItemStateChanged

    private void SkalaResiko46KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko46KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko46KeyPressed

    private void SkalaResiko47ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko47ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko47ItemStateChanged

    private void SkalaResiko47KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko47KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko47KeyPressed

    private void SkalaResiko48ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko48ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko48ItemStateChanged

    private void SkalaResiko48KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko48KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko48KeyPressed

    private void SkalaResiko49ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko49ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko49ItemStateChanged

    private void SkalaResiko49KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko49KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko49KeyPressed

    private void SkalaResiko50ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko50ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko50ItemStateChanged

    private void SkalaResiko50KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko50KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SkalaResiko50KeyPressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMPenilaianLanjutanRisikoJatuhDewasa dialog = new RMPenilaianLanjutanRisikoJatuhDewasa(new javax.swing.JFrame(), true);
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
    private widget.TextArea HasilSkrining;
    private widget.ComboBox Jam;
    private widget.Label LCount;
    private widget.ComboBox Menit;
    private javax.swing.JMenuItem MnPenilaianLanjutanRisikoJatuh;
    private widget.TextBox NIP;
    private widget.TextBox NamaPetugas;
    private widget.TextBox NilaiResiko1;
    private widget.TextBox NilaiResiko2;
    private widget.TextBox NilaiResiko3;
    private widget.TextBox NilaiResiko4;
    private widget.TextBox NilaiResiko5;
    private widget.TextBox NilaiResiko6;
    private widget.TextBox NilaiResikoTotal;
    private javax.swing.JPanel PanelInput;
    private widget.TextArea Saran;
    private widget.ScrollPane Scroll;
    private widget.ComboBox SkalaResiko1;
    private widget.ComboBox SkalaResiko2;
    private widget.ComboBox SkalaResiko3;
    private widget.ComboBox SkalaResiko30;
    private widget.ComboBox SkalaResiko31;
    private widget.ComboBox SkalaResiko32;
    private widget.ComboBox SkalaResiko33;
    private widget.ComboBox SkalaResiko34;
    private widget.ComboBox SkalaResiko35;
    private widget.ComboBox SkalaResiko36;
    private widget.ComboBox SkalaResiko37;
    private widget.ComboBox SkalaResiko38;
    private widget.ComboBox SkalaResiko39;
    private widget.ComboBox SkalaResiko4;
    private widget.ComboBox SkalaResiko40;
    private widget.ComboBox SkalaResiko41;
    private widget.ComboBox SkalaResiko42;
    private widget.ComboBox SkalaResiko43;
    private widget.ComboBox SkalaResiko44;
    private widget.ComboBox SkalaResiko45;
    private widget.ComboBox SkalaResiko46;
    private widget.ComboBox SkalaResiko47;
    private widget.ComboBox SkalaResiko48;
    private widget.ComboBox SkalaResiko49;
    private widget.ComboBox SkalaResiko5;
    private widget.ComboBox SkalaResiko50;
    private widget.ComboBox SkalaResiko6;
    private widget.ComboBox SkalaResiko9;
    private widget.TextBox TCari;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.Tanggal Tanggal;
    private widget.TextBox TglLahir;
    private widget.Label TingkatResiko;
    private widget.Button btnPetugas;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel16;
    private widget.Label jLabel18;
    private widget.Label jLabel19;
    private widget.Label jLabel21;
    private widget.Label jLabel217;
    private widget.Label jLabel218;
    private widget.Label jLabel219;
    private widget.Label jLabel220;
    private widget.Label jLabel221;
    private widget.Label jLabel222;
    private widget.Label jLabel223;
    private widget.Label jLabel224;
    private widget.Label jLabel225;
    private widget.Label jLabel226;
    private widget.Label jLabel227;
    private widget.Label jLabel228;
    private widget.Label jLabel229;
    private widget.Label jLabel230;
    private widget.Label jLabel231;
    private widget.Label jLabel232;
    private widget.Label jLabel233;
    private widget.Label jLabel234;
    private widget.Label jLabel235;
    private widget.Label jLabel236;
    private widget.Label jLabel237;
    private widget.Label jLabel238;
    private widget.Label jLabel239;
    private widget.Label jLabel240;
    private widget.Label jLabel241;
    private widget.Label jLabel242;
    private widget.Label jLabel243;
    private widget.Label jLabel244;
    private widget.Label jLabel245;
    private widget.Label jLabel246;
    private widget.Label jLabel247;
    private widget.Label jLabel248;
    private widget.Label jLabel249;
    private widget.Label jLabel250;
    private widget.Label jLabel251;
    private widget.Label jLabel252;
    private widget.Label jLabel253;
    private widget.Label jLabel254;
    private widget.Label jLabel255;
    private widget.Label jLabel256;
    private widget.Label jLabel257;
    private widget.Label jLabel30;
    private widget.Label jLabel31;
    private widget.Label jLabel32;
    private widget.Label jLabel33;
    private widget.Label jLabel34;
    private widget.Label jLabel4;
    private widget.Label jLabel57;
    private widget.Label jLabel58;
    private widget.Label jLabel6;
    private widget.Label jLabel7;
    private widget.Label jLabel8;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.ScrollPane scrollInput;
    private widget.ScrollPane scrollPane1;
    private widget.ScrollPane scrollPane2;
    private widget.Table tbObat;
    // End of variables declaration//GEN-END:variables
    
    public void tampil() {
        Valid.tabelKosong(tabMode);
        try{
            if(TCari.getText().toString().trim().equals("")){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.jk,pasien.tgl_lahir,penilaian_lanjutan_resiko_jatuh_dewasa.tanggal,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_skala1,penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_nilai1,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_skala2,penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_nilai2,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_skala3,penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_nilai3,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_skala4,penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_nilai4,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_skala5,penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_nilai5,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_skala6,penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_nilai6,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_totalnilai,penilaian_lanjutan_resiko_jatuh_dewasa.hasil_skrining,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.saran,penilaian_lanjutan_resiko_jatuh_dewasa.nip,petugas.nama "+
                    "from penilaian_lanjutan_resiko_jatuh_dewasa inner join reg_periksa on penilaian_lanjutan_resiko_jatuh_dewasa.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join petugas on penilaian_lanjutan_resiko_jatuh_dewasa.nip=petugas.nip where "+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.tanggal between ? and ? order by penilaian_lanjutan_resiko_jatuh_dewasa.tanggal");
            }else{
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.jk,pasien.tgl_lahir,penilaian_lanjutan_resiko_jatuh_dewasa.tanggal,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_skala1,penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_nilai1,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_skala2,penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_nilai2,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_skala3,penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_nilai3,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_skala4,penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_nilai4,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_skala5,penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_nilai5,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_skala6,penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_nilai6,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.penilaian_jatuhmorse_totalnilai,penilaian_lanjutan_resiko_jatuh_dewasa.hasil_skrining,"+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.saran,penilaian_lanjutan_resiko_jatuh_dewasa.nip,petugas.nama "+
                    "from penilaian_lanjutan_resiko_jatuh_dewasa inner join reg_periksa on penilaian_lanjutan_resiko_jatuh_dewasa.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join petugas on penilaian_lanjutan_resiko_jatuh_dewasa.nip=petugas.nip where "+
                    "penilaian_lanjutan_resiko_jatuh_dewasa.tanggal between ? and ? and (reg_periksa.no_rawat like ? or pasien.no_rkm_medis like ? or pasien.nm_pasien like ? or penilaian_lanjutan_resiko_jatuh_dewasa.nip like ? or petugas.nama like ?) "+
                    "order by penilaian_lanjutan_resiko_jatuh_dewasa.tanggal ");
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
                        rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),rs.getString("tgl_lahir"),rs.getString("jk"),rs.getString("tanggal"),
                        rs.getString("penilaian_jatuhmorse_skala1"),rs.getString("penilaian_jatuhmorse_nilai1"),rs.getString("penilaian_jatuhmorse_skala2"),rs.getString("penilaian_jatuhmorse_nilai2"),
                        rs.getString("penilaian_jatuhmorse_skala3"),rs.getString("penilaian_jatuhmorse_nilai3"),rs.getString("penilaian_jatuhmorse_skala4"),rs.getString("penilaian_jatuhmorse_nilai4"),
                        rs.getString("penilaian_jatuhmorse_skala5"),rs.getString("penilaian_jatuhmorse_nilai5"),rs.getString("penilaian_jatuhmorse_skala6"),rs.getString("penilaian_jatuhmorse_nilai6"),
                        rs.getString("penilaian_jatuhmorse_totalnilai"),rs.getString("hasil_skrining"),rs.getString("saran"),rs.getString("nip"),rs.getString("nama")
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
        Tanggal.setDate(new Date());
        SkalaResiko1.setSelectedIndex(0);
        NilaiResiko1.setText("0");
        SkalaResiko2.setSelectedIndex(0);
        NilaiResiko2.setText("0");
        SkalaResiko3.setSelectedIndex(0);
        NilaiResiko3.setText("0");
        SkalaResiko4.setSelectedIndex(0);
        NilaiResiko4.setText("0");
        SkalaResiko5.setSelectedIndex(0);
        NilaiResiko5.setText("0");
        SkalaResiko6.setSelectedIndex(0);
        NilaiResiko6.setText("0");
        NilaiResikoTotal.setText("0");
        Saran.setText("");
        HasilSkrining.setText("");
        SkalaResiko1.requestFocus();
    } 

    private void getData() {
        if(tbObat.getSelectedRow()!= -1){
            TNoRw.setText(tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()); 
            TNoRM.setText(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString());
            TPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(),2).toString());
            TglLahir.setText(tbObat.getValueAt(tbObat.getSelectedRow(),3).toString());
            SkalaResiko1.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),6).toString());
            NilaiResiko1.setText(tbObat.getValueAt(tbObat.getSelectedRow(),7).toString());
            SkalaResiko2.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),8).toString());
            NilaiResiko2.setText(tbObat.getValueAt(tbObat.getSelectedRow(),9).toString());
            SkalaResiko3.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),10).toString());
            NilaiResiko3.setText(tbObat.getValueAt(tbObat.getSelectedRow(),11).toString());
            SkalaResiko4.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),12).toString());
            NilaiResiko4.setText(tbObat.getValueAt(tbObat.getSelectedRow(),13).toString());
            SkalaResiko5.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),14).toString());
            NilaiResiko5.setText(tbObat.getValueAt(tbObat.getSelectedRow(),15).toString());
            SkalaResiko6.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),16).toString());
            NilaiResiko6.setText(tbObat.getValueAt(tbObat.getSelectedRow(),17).toString());
            NilaiResikoTotal.setText(tbObat.getValueAt(tbObat.getSelectedRow(),18).toString());
            HasilSkrining.setText(tbObat.getValueAt(tbObat.getSelectedRow(),19).toString());
            Saran.setText(tbObat.getValueAt(tbObat.getSelectedRow(),20).toString());
            Jam.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),5).toString().substring(11,13));
            Menit.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),5).toString().substring(14,16));
            Detik.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),5).toString().substring(17,19));
            Valid.SetTgl(Tanggal,tbObat.getValueAt(tbObat.getSelectedRow(),5).toString());
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
            if(internalFrame1.getHeight()>638){
                ChkInput.setVisible(false);
                PanelInput.setPreferredSize(new Dimension(WIDTH,666));
                FormInput.setVisible(true);      
                ChkInput.setVisible(true);
            }else{
                ChkInput.setVisible(false);
                PanelInput.setPreferredSize(new Dimension(WIDTH,internalFrame1.getHeight()-172));
                FormInput.setVisible(true);      
                ChkInput.setVisible(true);
            }
        }else if(ChkInput.isSelected()==false){           
            ChkInput.setVisible(false);            
            PanelInput.setPreferredSize(new Dimension(WIDTH,20));
            FormInput.setVisible(false);      
            ChkInput.setVisible(true);
        }
    }
    
    public void isCek(){
        BtnSimpan.setEnabled(akses.getpenilaian_lanjutan_resiko_jatuh_dewasa());
        BtnHapus.setEnabled(akses.getpenilaian_lanjutan_resiko_jatuh_dewasa());
        BtnEdit.setEnabled(akses.getpenilaian_lanjutan_resiko_jatuh_dewasa());
        BtnPrint.setEnabled(akses.getpenilaian_lanjutan_resiko_jatuh_dewasa()); 
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
        Sequel.mengedit("penilaian_lanjutan_resiko_jatuh_dewasa","tanggal=? and no_rawat=?","no_rawat=?,tanggal=?,penilaian_jatuhmorse_skala1=?,penilaian_jatuhmorse_nilai1=?,"+
            "penilaian_jatuhmorse_skala2=?,penilaian_jatuhmorse_nilai2=?,penilaian_jatuhmorse_skala3=?,penilaian_jatuhmorse_nilai3=?,penilaian_jatuhmorse_skala4=?,"+
            "penilaian_jatuhmorse_nilai4=?,penilaian_jatuhmorse_skala5=?,penilaian_jatuhmorse_nilai5=?,penilaian_jatuhmorse_skala6=?,penilaian_jatuhmorse_nilai6=?,"+
            "penilaian_jatuhmorse_totalnilai=?,hasil_skrining=?,saran=?,nip=?",20,new String[]{
            TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Jam.getSelectedItem()+":"+Menit.getSelectedItem()+":"+Detik.getSelectedItem(),
            SkalaResiko1.getSelectedItem().toString(),NilaiResiko1.getText(),SkalaResiko2.getSelectedItem().toString(),NilaiResiko2.getText(),
            SkalaResiko3.getSelectedItem().toString(),NilaiResiko3.getText(),SkalaResiko4.getSelectedItem().toString(),NilaiResiko4.getText(), 
            SkalaResiko5.getSelectedItem().toString(),NilaiResiko5.getText(),SkalaResiko6.getSelectedItem().toString(),NilaiResiko6.getText(),
            NilaiResikoTotal.getText(),HasilSkrining.getText(),Saran.getText(),NIP.getText(),tbObat.getValueAt(tbObat.getSelectedRow(),5).toString(),
            tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()
        });
        if(tabMode.getRowCount()!=0){tampil();}
        emptTeks();
    }

    private void hapus() {
        if(Sequel.queryu2tf("delete from penilaian_lanjutan_resiko_jatuh_dewasa where tanggal=? and no_rawat=?",2,new String[]{
            tbObat.getValueAt(tbObat.getSelectedRow(),5).toString(),tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()
        })==true){
            tampil();
            emptTeks();
        }else{
            JOptionPane.showMessageDialog(null,"Gagal menghapus..!!");
        }
    }
    
    private void isTotalResikoJatuh(){
        try {
            NilaiResikoTotal.setText((Integer.parseInt(NilaiResiko1.getText())+Integer.parseInt(NilaiResiko2.getText())+Integer.parseInt(NilaiResiko3.getText())+Integer.parseInt(NilaiResiko4.getText())+Integer.parseInt(NilaiResiko5.getText())+Integer.parseInt(NilaiResiko6.getText()))+"");
            if(Integer.parseInt(NilaiResikoTotal.getText())<25){
                TingkatResiko.setText("Tingkat Resiko : Risiko Rendah (skala 0-24), Tindakan : Intervensi setiap  shift");
            }else if(Integer.parseInt(NilaiResikoTotal.getText())<45){
                TingkatResiko.setText("Tingkat Resiko : Risiko Sedang (skala 25-44), Tindakan : Intervensi setiap 6 jam");
            }else if(Integer.parseInt(NilaiResikoTotal.getText())>=45){
                TingkatResiko.setText("Tingkat Resiko : Risiko Tinggi (skala > 45), Tindakan : Intervensi setiap 4 jam");
            }
        } catch (Exception e) {
            NilaiResikoTotal.setText("0");
            TingkatResiko.setText("Tingkat Resiko : Risiko Rendah (skala 0-24), Tindakan : Intervensi setiap  shift");
        }
    }
}
