package surat;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import kepegawaian.DlgCariDokter;
import kepegawaian.DlgCariDokter2;
import simrskhanza.DlgCariPoli;
import simrskhanza.DlgCariPoli2;
import fungsi.validasi;

/**
 *
 * @author dosen
 */
public class SuratKontrolUmum extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private PreparedStatement ps,ps2;
    private ResultSet rs,rs2;
    private int i=0,kuota=0;
    private DlgCariDokter dokter=new DlgCariDokter(null,false);
    private DlgCariDokter2 dokter2=new DlgCariDokter2(null,false);
    private DlgCariPoli poli=new DlgCariPoli(null,false);
    private DlgCariPoli2 poli2=new DlgCariPoli2(null,false);
    private String URUTNOREG="",status="",kdpoli="",nmpoli="",noantri="",aktifjadwal="",bln_angka = "", bln_romawi = "";
    
    

    /** Creates new form DlgPemberianInfus
     * @param parent
     * @param modal */
    public SuratKontrolUmum(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        tabMode=new DefaultTableModel(null,new Object[]{
                "Tahun","No. Surat","No.RM","Nama Pasien","Diagnosa","Terapi","Diet","Perawatan Luka",
                "Perawatan NGT/ Kateter","Lain-Lain","Hasil Pemeriksaan Tertunda","Periksa Kembali","Tanggal Surat","No.Antrian","No.Reg","Kode Dokter","Nama Dokter",
                "Kode Poli","Nama Poli","Status"
            }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbObat.setModel(tabMode);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbObat.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 20; i++) {
            TableColumn column = tbObat.getColumnModel().getColumn(i);
            if(i==0){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==1){
                column.setPreferredWidth(150);
            }else if(i==2){
                column.setPreferredWidth(80);
            }else if(i==3){
                column.setPreferredWidth(150);
            }else if(i==4){
                column.setPreferredWidth(150);
            }else if(i==5){
                column.setPreferredWidth(150);
            }else if(i==6){
                column.setPreferredWidth(150);
            }else if(i==7){
                column.setPreferredWidth(150);
            }else if(i==8){
                column.setPreferredWidth(150);
            }else if(i==9){
                column.setPreferredWidth(150);
            }else if(i==10){
                column.setPreferredWidth(82);
            }else if(i==11){
                column.setPreferredWidth(75);
            }else if(i==12){
                column.setPreferredWidth(52);
            }else if(i==13){
                column.setPreferredWidth(45);
            }else if(i==9){
                column.setPreferredWidth(150);
            }else if(i==14){
                column.setPreferredWidth(82);
            }else if(i==15){
                column.setPreferredWidth(75);
            }else if(i==16){
                column.setPreferredWidth(52);
            }else if(i==17){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==18){
                column.setPreferredWidth(150);
            }else if(i==19){
                column.setMinWidth(0);
                column.setMaxWidth(0);           
            }
        }
        tbObat.setDefaultRenderer(Object.class, new WarnaTable());
        
        
        TNoRM.setDocument(new batasInput((byte)15).getKata(TNoRM));
        TCari.setDocument(new batasInput((byte)100).getKata(TCari));
        Diagnosa.setDocument(new batasInput((int)50).getKata(Diagnosa));
     // Terapi.setDocument(new batasInput((int)100).getKata(Terapi));
        Diet.setDocument(new batasInput((int)100).getKata(Diet));
        Ngt.setDocument(new batasInput((int)100).getKata(Ngt));
        Luka.setDocument(new batasInput((int)100).getKata(Luka));
        Lain.setDocument(new batasInput((int)100).getKata(Lain));
        Tunda.setDocument(new batasInput((int)100).getKata(Tunda));
        NoReg.setDocument(new batasInput((byte)6).getKata(NoReg));
        KdDokter.setDocument(new batasInput((byte)20).getKata(KdDokter));
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
        
        dokter.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {;}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(dokter.getTable().getSelectedRow()!= -1){                    
                    KdDokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),0).toString());
                    NmDokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                    isNomer();
                }
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
        
        dokter2.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {;}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(dokter2.getTable().getSelectedRow()!= -1){                    
                    KdDokter.setText(dokter2.getTable().getValueAt(dokter2.getTable().getSelectedRow(),0).toString());
                    NmDokter.setText(dokter2.getTable().getValueAt(dokter2.getTable().getSelectedRow(),1).toString());
                    if(aktifjadwal.equals("aktif")){
                        kuota=Integer.parseInt(dokter2.getTable().getValueAt(dokter2.getTable().getSelectedRow(),13).toString());
                    }
                    isNomer();                        
                }      
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
        
        poli.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(poli.getTable().getSelectedRow()!= -1){                    
                    KdPoli.setText(poli.getTable().getValueAt(poli.getTable().getSelectedRow(),0).toString());
                    NmPoli.setText(poli.getTable().getValueAt(poli.getTable().getSelectedRow(),1).toString());
                    isNomer();
                }   
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
        
        poli2.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(poli2.getTable().getSelectedRow()!= -1){                    
                    KdPoli.setText(poli2.getTable().getValueAt(poli2.getTable().getSelectedRow(),0).toString());
                    NmPoli.setText(poli2.getTable().getValueAt(poli2.getTable().getSelectedRow(),1).toString());
                    isNomer();
                }    
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
            aktifjadwal=koneksiDB.JADWALDOKTERDIREGISTRASI();
            URUTNOREG=koneksiDB.URUTNOREG();
        } catch (Exception ex) {
            aktifjadwal="";
            URUTNOREG="";
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        MnCetak = new javax.swing.JMenuItem();
        MnCetak1 = new javax.swing.JMenuItem();
        jLabel20 = new widget.Label();
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
        BtnAll = new widget.Button();
        BtnKeluar = new widget.Button();
        panelGlass10 = new widget.panelisi();
        jLabel6 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        jLabel7 = new widget.Label();
        LCount = new widget.Label();
        panelCari = new widget.panelisi();
        R1 = new widget.RadioButton();
        R2 = new widget.RadioButton();
        DTPCari1 = new widget.Tanggal();
        jLabel22 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        R3 = new widget.RadioButton();
        DTPCari3 = new widget.Tanggal();
        jLabel25 = new widget.Label();
        DTPCari4 = new widget.Tanggal();
        PanelInput = new javax.swing.JPanel();
        ChkInput = new widget.CekBox();
        FormInput = new widget.PanelBiasa();
        jLabel4 = new widget.Label();
        TNoRM = new widget.TextBox();
        jLabel9 = new widget.Label();
        NmDokter = new widget.TextBox();
        TPasien = new widget.TextBox();
        TanggalSurat = new widget.Tanggal();
        Status = new widget.ComboBox();
        jLabel10 = new widget.Label();
        KdDokter = new widget.TextBox();
        BtnDokter = new widget.Button();
        jLabel37 = new widget.Label();
        jLabel11 = new widget.Label();
        KdPoli = new widget.TextBox();
        NmPoli = new widget.TextBox();
        BtnPoli = new widget.Button();
        jLabel5 = new widget.Label();
        TanggalPeriksa = new widget.Tanggal();
        jLabel15 = new widget.Label();
        NoAntrian = new widget.TextBox();
        Diagnosa = new widget.TextBox();
        jLabel16 = new widget.Label();
        jLabel17 = new widget.Label();
        NoReg = new widget.TextBox();
        jLabel18 = new widget.Label();
        jLabel19 = new widget.Label();
        scrollPane2 = new widget.ScrollPane();
        Terapi = new widget.TextArea();
        jLabel21 = new widget.Label();
        NoSurat = new widget.TextBox();
        jLabel12 = new widget.Label();
        Diet = new widget.TextBox();
        jLabel13 = new widget.Label();
        Luka = new widget.TextBox();
        Ngt = new widget.TextBox();
        jLabel23 = new widget.Label();
        jLabel24 = new widget.Label();
        Lain = new widget.TextBox();
        jLabel26 = new widget.Label();
        Tunda = new widget.TextBox();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        MnCetak.setBackground(new java.awt.Color(255, 255, 254));
        MnCetak.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnCetak.setForeground(new java.awt.Color(50, 50, 50));
        MnCetak.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnCetak.setText("Cetak Surat Kontrol");
        MnCetak.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MnCetak.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        MnCetak.setName("MnCetak"); // NOI18N
        MnCetak.setPreferredSize(new java.awt.Dimension(180, 26));
        MnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnCetakActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnCetak);

        MnCetak1.setBackground(new java.awt.Color(255, 255, 254));
        MnCetak1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnCetak1.setForeground(new java.awt.Color(50, 50, 50));
        MnCetak1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnCetak1.setText("Cetak Surat Kontrol FKTP");
        MnCetak1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MnCetak1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        MnCetak1.setName("MnCetak1"); // NOI18N
        MnCetak1.setPreferredSize(new java.awt.Dimension(180, 26));
        MnCetak1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnCetak1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnCetak1);

        jLabel20.setText("No.Antrian :");
        jLabel20.setName("jLabel20"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[Data Surat Kontrol ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        tbObat.setAutoCreateRowSorter(true);
        tbObat.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbObat.setComponentPopupMenu(jPopupMenu1);
        tbObat.setName("tbObat"); // NOI18N
        tbObat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbObatMouseClicked(evt);
            }
        });
        tbObat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbObatKeyReleased(evt);
            }
        });
        Scroll.setViewportView(tbObat);

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(44, 144));
        jPanel3.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(55, 55));
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

        jPanel3.add(panelGlass8, java.awt.BorderLayout.PAGE_END);

        panelGlass10.setName("panelGlass10"); // NOI18N
        panelGlass10.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass10.add(jLabel6);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(450, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass10.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('2');
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
        panelGlass10.add(BtnCari);

        jLabel7.setText("Record :");
        jLabel7.setName("jLabel7"); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(65, 23));
        panelGlass10.add(jLabel7);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(50, 23));
        panelGlass10.add(LCount);

        jPanel3.add(panelGlass10, java.awt.BorderLayout.CENTER);

        panelCari.setName("panelCari"); // NOI18N
        panelCari.setPreferredSize(new java.awt.Dimension(44, 43));
        panelCari.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 9));

        R1.setBackground(new java.awt.Color(240, 250, 230));
        R1.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        buttonGroup1.add(R1);
        R1.setSelected(true);
        R1.setText("Menunggu");
        R1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R1.setName("R1"); // NOI18N
        R1.setPreferredSize(new java.awt.Dimension(85, 23));
        panelCari.add(R1);

        R2.setBackground(new java.awt.Color(240, 250, 230));
        R2.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        buttonGroup1.add(R2);
        R2.setText("Tanggal :");
        R2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R2.setName("R2"); // NOI18N
        R2.setPreferredSize(new java.awt.Dimension(90, 23));
        panelCari.add(R2);

        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "10-01-2022" }));
        DTPCari1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1.setName("DTPCari1"); // NOI18N
        DTPCari1.setOpaque(false);
        DTPCari1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                DTPCari1ItemStateChanged(evt);
            }
        });
        DTPCari1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DTPCari1KeyPressed(evt);
            }
        });
        panelCari.add(DTPCari1);

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("s.d");
        jLabel22.setName("jLabel22"); // NOI18N
        jLabel22.setPreferredSize(new java.awt.Dimension(25, 23));
        panelCari.add(jLabel22);

        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "10-01-2022" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setName("DTPCari2"); // NOI18N
        DTPCari2.setOpaque(false);
        DTPCari2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DTPCari2KeyPressed(evt);
            }
        });
        panelCari.add(DTPCari2);

        R3.setBackground(new java.awt.Color(240, 250, 230));
        R3.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        buttonGroup1.add(R3);
        R3.setText("Selesai :");
        R3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R3.setName("R3"); // NOI18N
        R3.setPreferredSize(new java.awt.Dimension(85, 23));
        panelCari.add(R3);

        DTPCari3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "10-01-2022" }));
        DTPCari3.setDisplayFormat("dd-MM-yyyy");
        DTPCari3.setName("DTPCari3"); // NOI18N
        DTPCari3.setOpaque(false);
        DTPCari3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                DTPCari3ItemStateChanged(evt);
            }
        });
        DTPCari3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DTPCari3KeyPressed(evt);
            }
        });
        panelCari.add(DTPCari3);

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("s.d");
        jLabel25.setName("jLabel25"); // NOI18N
        jLabel25.setPreferredSize(new java.awt.Dimension(25, 23));
        panelCari.add(jLabel25);

        DTPCari4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "10-01-2022" }));
        DTPCari4.setDisplayFormat("dd-MM-yyyy");
        DTPCari4.setName("DTPCari4"); // NOI18N
        DTPCari4.setOpaque(false);
        DTPCari4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                DTPCari4ItemStateChanged(evt);
            }
        });
        DTPCari4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DTPCari4KeyPressed(evt);
            }
        });
        panelCari.add(DTPCari4);

        jPanel3.add(panelCari, java.awt.BorderLayout.PAGE_START);

        internalFrame1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setPreferredSize(new java.awt.Dimension(192, 216));
        PanelInput.setLayout(new java.awt.BorderLayout(1, 1));

        ChkInput.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setMnemonic('M');
        ChkInput.setText(".: Input Data");
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

        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(190, 107));
        FormInput.setLayout(null);

        jLabel4.setText("Pasien :");
        jLabel4.setName("jLabel4"); // NOI18N
        FormInput.add(jLabel4);
        jLabel4.setBounds(0, 40, 92, 23);

        TNoRM.setEditable(false);
        TNoRM.setHighlighter(null);
        TNoRM.setName("TNoRM"); // NOI18N
        TNoRM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRMKeyPressed(evt);
            }
        });
        FormInput.add(TNoRM);
        TNoRM.setBounds(100, 40, 87, 23);

        jLabel9.setText("Dokter :");
        jLabel9.setName("jLabel9"); // NOI18N
        FormInput.add(jLabel9);
        jLabel9.setBounds(390, 250, 92, 23);

        NmDokter.setEditable(false);
        NmDokter.setHighlighter(null);
        NmDokter.setName("NmDokter"); // NOI18N
        FormInput.add(NmDokter);
        NmDokter.setBounds(580, 250, 160, 23);

        TPasien.setEditable(false);
        TPasien.setHighlighter(null);
        TPasien.setName("TPasien"); // NOI18N
        FormInput.add(TPasien);
        TPasien.setBounds(190, 40, 190, 23);

        TanggalSurat.setForeground(new java.awt.Color(50, 70, 50));
        TanggalSurat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "10-01-2022 07:48:49" }));
        TanggalSurat.setDisplayFormat("dd-MM-yyyy HH:mm:ss");
        TanggalSurat.setDoubleBuffered(true);
        TanggalSurat.setName("TanggalSurat"); // NOI18N
        TanggalSurat.setOpaque(false);
        TanggalSurat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TanggalSuratKeyPressed(evt);
            }
        });
        FormInput.add(TanggalSurat);
        TanggalSurat.setBounds(610, 10, 132, 23);

        Status.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Menunggu", "Sudah Periksa", "Batal Periksa" }));
        Status.setName("Status"); // NOI18N
        Status.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                StatusKeyPressed(evt);
            }
        });
        FormInput.add(Status);
        Status.setBounds(620, 220, 130, 23);

        jLabel10.setText("Tanggal Kunjungan Terakhir :");
        jLabel10.setName("jLabel10"); // NOI18N
        FormInput.add(jLabel10);
        jLabel10.setBounds(435, 10, 170, 23);

        KdDokter.setEditable(false);
        KdDokter.setHighlighter(null);
        KdDokter.setName("KdDokter"); // NOI18N
        FormInput.add(KdDokter);
        KdDokter.setBounds(490, 250, 87, 23);

        BtnDokter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnDokter.setMnemonic('X');
        BtnDokter.setToolTipText("Alt+X");
        BtnDokter.setName("BtnDokter"); // NOI18N
        BtnDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDokterActionPerformed(evt);
            }
        });
        BtnDokter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnDokterKeyPressed(evt);
            }
        });
        FormInput.add(BtnDokter);
        BtnDokter.setBounds(740, 250, 28, 23);

        jLabel37.setText("Status :");
        jLabel37.setName("jLabel37"); // NOI18N
        FormInput.add(jLabel37);
        jLabel37.setBounds(560, 220, 60, 23);

        jLabel11.setText("Unit/Poli :");
        jLabel11.setName("jLabel11"); // NOI18N
        FormInput.add(jLabel11);
        jLabel11.setBounds(10, 250, 95, 23);

        KdPoli.setEditable(false);
        KdPoli.setHighlighter(null);
        KdPoli.setName("KdPoli"); // NOI18N
        FormInput.add(KdPoli);
        KdPoli.setBounds(110, 250, 70, 23);

        NmPoli.setEditable(false);
        NmPoli.setHighlighter(null);
        NmPoli.setName("NmPoli"); // NOI18N
        FormInput.add(NmPoli);
        NmPoli.setBounds(180, 250, 165, 23);

        BtnPoli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnPoli.setMnemonic('X');
        BtnPoli.setToolTipText("Alt+X");
        BtnPoli.setName("BtnPoli"); // NOI18N
        BtnPoli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPoliActionPerformed(evt);
            }
        });
        BtnPoli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPoliKeyPressed(evt);
            }
        });
        FormInput.add(BtnPoli);
        BtnPoli.setBounds(350, 250, 28, 23);

        jLabel5.setText("Diet:");
        jLabel5.setName("jLabel5"); // NOI18N
        FormInput.add(jLabel5);
        jLabel5.setBounds(0, 120, 90, 23);

        TanggalPeriksa.setForeground(new java.awt.Color(50, 70, 50));
        TanggalPeriksa.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "10-01-2022 07:48:49" }));
        TanggalPeriksa.setDisplayFormat("dd-MM-yyyy HH:mm:ss");
        TanggalPeriksa.setName("TanggalPeriksa"); // NOI18N
        TanggalPeriksa.setOpaque(false);
        TanggalPeriksa.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                TanggalPeriksaItemStateChanged(evt);
            }
        });
        TanggalPeriksa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TanggalPeriksaKeyPressed(evt);
            }
        });
        FormInput.add(TanggalPeriksa);
        TanggalPeriksa.setBounds(110, 220, 132, 23);

        jLabel15.setText("No. Surat VClaim:");
        jLabel15.setName("jLabel15"); // NOI18N
        FormInput.add(jLabel15);
        jLabel15.setBounds(0, 10, 90, 23);

        NoAntrian.setHighlighter(null);
        NoAntrian.setName("NoAntrian"); // NOI18N
        NoAntrian.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NoAntrianKeyPressed(evt);
            }
        });
        FormInput.add(NoAntrian);
        NoAntrian.setBounds(320, 220, 70, 23);

        Diagnosa.setHighlighter(null);
        Diagnosa.setName("Diagnosa"); // NOI18N
        Diagnosa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DiagnosaKeyPressed(evt);
            }
        });
        FormInput.add(Diagnosa);
        Diagnosa.setBounds(100, 70, 279, 23);

        jLabel16.setText("Diagnosa :");
        jLabel16.setName("jLabel16"); // NOI18N
        FormInput.add(jLabel16);
        jLabel16.setBounds(0, 70, 92, 23);

        jLabel17.setText("Terapi :");
        jLabel17.setName("jLabel17"); // NOI18N
        FormInput.add(jLabel17);
        jLabel17.setBounds(375, 40, 95, 23);

        NoReg.setHighlighter(null);
        NoReg.setName("NoReg"); // NOI18N
        NoReg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NoRegActionPerformed(evt);
            }
        });
        NoReg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NoRegKeyPressed(evt);
            }
        });
        FormInput.add(NoReg);
        NoReg.setBounds(490, 220, 70, 23);

        jLabel18.setText("No.Reg :");
        jLabel18.setName("jLabel18"); // NOI18N
        FormInput.add(jLabel18);
        jLabel18.setBounds(390, 220, 95, 23);

        jLabel19.setText("Tanggal Kontrol :");
        jLabel19.setName("jLabel19"); // NOI18N
        FormInput.add(jLabel19);
        jLabel19.setBounds(10, 220, 92, 23);

        scrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane2.setName("scrollPane2"); // NOI18N

        Terapi.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Terapi.setColumns(20);
        Terapi.setRows(5);
        Terapi.setName("Terapi"); // NOI18N
        Terapi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TerapiKeyPressed(evt);
            }
        });
        scrollPane2.setViewportView(Terapi);

        FormInput.add(scrollPane2);
        scrollPane2.setBounds(480, 40, 360, 70);

        jLabel21.setText("No.Antrian :");
        jLabel21.setName("jLabel21"); // NOI18N
        FormInput.add(jLabel21);
        jLabel21.setBounds(250, 220, 60, 23);

        NoSurat.setName("NoSurat"); // NOI18N
        NoSurat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NoSuratActionPerformed(evt);
            }
        });
        FormInput.add(NoSurat);
        NoSurat.setBounds(100, 10, 280, 24);

        jLabel12.setText("INSTRUKSI SELAMA DIRUMAH:");
        jLabel12.setName("jLabel12"); // NOI18N
        FormInput.add(jLabel12);
        jLabel12.setBounds(0, 100, 160, 23);

        Diet.setName("Diet"); // NOI18N
        FormInput.add(Diet);
        Diet.setBounds(100, 120, 280, 24);

        jLabel13.setText("Perawatan Luka:");
        jLabel13.setName("jLabel13"); // NOI18N
        FormInput.add(jLabel13);
        jLabel13.setBounds(410, 120, 90, 23);

        Luka.setName("Luka"); // NOI18N
        FormInput.add(Luka);
        Luka.setBounds(510, 120, 280, 24);

        Ngt.setName("Ngt"); // NOI18N
        FormInput.add(Ngt);
        Ngt.setBounds(100, 150, 280, 24);

        jLabel23.setText("Perawatan NGT: ");
        jLabel23.setName("jLabel23"); // NOI18N
        FormInput.add(jLabel23);
        jLabel23.setBounds(0, 150, 90, 23);

        jLabel24.setText("Lain-lain:");
        jLabel24.setName("jLabel24"); // NOI18N
        FormInput.add(jLabel24);
        jLabel24.setBounds(410, 150, 90, 23);

        Lain.setName("Lain"); // NOI18N
        FormInput.add(Lain);
        Lain.setBounds(510, 150, 280, 24);

        jLabel26.setText("Hasil Pemeriksaan tertunda PA/Kultur/Lainnya:");
        jLabel26.setName("jLabel26"); // NOI18N
        FormInput.add(jLabel26);
        jLabel26.setBounds(30, 180, 250, 23);

        Tunda.setName("Tunda"); // NOI18N
        FormInput.add(Tunda);
        Tunda.setBounds(290, 180, 280, 24);

        PanelInput.add(FormInput, java.awt.BorderLayout.CENTER);

        internalFrame1.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TNoRMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRMKeyPressed
        Valid.pindah(evt,Diagnosa,KdDokter);
        
}//GEN-LAST:event_TNoRMKeyPressed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(TNoRM.getText().trim().equals("")||TPasien.getText().trim().equals("")){
            Valid.textKosong(TNoRM,"pasien");
        }else if(NmDokter.getText().trim().equals("")||KdDokter.getText().trim().equals("")){
            Valid.textKosong(KdDokter,"Dokter");
        }else if(NmPoli.getText().trim().equals("")||NmPoli.getText().trim().equals("")){
            Valid.textKosong(KdPoli,"Poli");
        }else if(NoAntrian.getText().trim().equals("")){
            Valid.textKosong(NoAntrian,"No.Antrian");
        }else if(NoSurat.getText().trim().equals("")){
            Valid.textKosong(NoSurat,"No.Antrian");
        }else if(NoReg.getText().trim().equals("")){
            Valid.textKosong(NoReg,"No.Antri");
        }else if(Terapi.getText().trim().equals("")){
            Valid.textKosong(Terapi,"Terapi");
        }else if(Diagnosa.getText().trim().equals("")){
            Valid.textKosong(Diagnosa,"Diagnosa");
        }else if(Diet.getText().trim().equals("")){
            Valid.textKosong(Diet,"Diet");
        }else if(Ngt.getText().trim().equals("")){
            Valid.textKosong(Ngt,"Perawatan NGT/Kateter/Lainnya");
        }else if(Luka.getText().trim().equals("")){
            Valid.textKosong(Luka,"Perawatan Luka");
        }else if(Lain.getText().trim().equals("")){
            Valid.textKosong(Lain,"Perawatan Lainnya");
        }else if(Diagnosa.getText().trim().equals("")){
            Valid.textKosong(Diagnosa,"Diagnosa");
        }else{
             if(akses.getkode().equals("Admin Utama")){
                isBooking();
            }else{
                if(aktifjadwal.equals("aktif")){
                    if(Sequel.cariInteger("select count(no_rkm_medis) from booking_registrasi where kd_dokter='"+KdDokter.getText()+"' and tanggal_periksa='"+Valid.SetTgl(TanggalPeriksa.getSelectedItem()+"")+"' ")>=kuota){
                        JOptionPane.showMessageDialog(null,"Eiiits, Kuota registrasi penuh..!!!");
                        TCari.requestFocus();
                    }else{
                        isBooking();
                    }                    
                }else{
                    isBooking();
                } 
            }            
        }
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnSimpanActionPerformed(null);
        }else{
            Valid.pindah(evt,NoReg,BtnBatal);
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
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
            TanggalPeriksa.requestFocus();
        }else if(TPasien.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, Gagal menghapus. Pilih dulu data yang mau dihapus.\nKlik data pada table untuk memilih...!!!!");
        }else if(!(TPasien.getText().trim().equals(""))){
            if(tbObat.getSelectedRow()!= -1){
                if(Sequel.queryu2tf("delete from surat_kontrol_umum where no_surat=?",1,new String[]{
                    tbObat.getValueAt(tbObat.getSelectedRow(),1).toString()
                })==true){
                    Sequel.queryu2("delete from booking_registrasi where no_rkm_medis=? and tanggal_periksa=?",2,new String[]{
                        tbObat.getValueAt(tbObat.getSelectedRow(),1).toString(),tbObat.getValueAt(tbObat.getSelectedRow(),9).toString()
                    });
                    tampil();
                    emptTeks();
                }                        
            }else{
                JOptionPane.showMessageDialog(null,"Maaf, Silahkan anda pilih terlebih dulu data yang mau anda hapus...\n Klik data pada table untuk memilih data...!!!!");
            }  
        }
}//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnHapusActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnBatal, BtnPrint);
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
            param.put("logo",Sequel.cariGambar("select logo from setting")); 
            
            Sequel.queryu("truncate table temporary_booking_registrasi");
            for(i=0;i<tabMode.getRowCount();i++){ 
             Sequel.menyimpan("temporary_booking_registrasi","'0','"+
                   tabMode.getValueAt(i,0).toString()+"','"+
                   tabMode.getValueAt(i,1).toString()+"','"+
                   tabMode.getValueAt(i,2).toString()+"','"+
                   tabMode.getValueAt(i,3).toString()+"','"+
                   tabMode.getValueAt(i,4).toString()+"','"+
                   tabMode.getValueAt(i,5).toString()+"','"+
                   tabMode.getValueAt(i,6).toString()+"','"+
                   tabMode.getValueAt(i,7).toString()+"','"+
                   tabMode.getValueAt(i,8).toString()+"','"+
                   tabMode.getValueAt(i,9).toString()+"','"+
                   tabMode.getValueAt(i,10).toString()+"','"+
                   tabMode.getValueAt(i,11).toString()+"','"+
                   tabMode.getValueAt(i,12).toString()+"','"+
                   tabMode.getValueAt(i,13).toString()+"','"+
                   tabMode.getValueAt(i,14).toString()+"','"+                  
                   tabMode.getValueAt(i,15).toString()+"','','','','','','','','','','','','','','','','','','','','',''","Rekap Nota Pembayaran");
             }
                Valid.MyReport("rptSKDPBPJS.jasper","report","::[ Laporan Daftar SKDP BPJS ]::",param);
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

private void BtnDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDokterActionPerformed
    if(aktifjadwal.equals("aktif")){
        if(akses.getkode().equals("Admin Utama")){
            dokter.isCek();        
            dokter.TCari.requestFocus();
            dokter.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
            dokter.setLocationRelativeTo(internalFrame1);
            dokter.setVisible(true);
        }else{
            dokter2.setPoli(NmPoli.getText());
            dokter2.isCek();  
            dokter2.SetHari(TanggalPeriksa.getDate());   
            dokter2.tampil();
            dokter2.TCari.requestFocus();
            dokter2.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
            dokter2.setLocationRelativeTo(internalFrame1);
            dokter2.setVisible(true);
        }                
    }else{
        dokter.isCek();        
        dokter.TCari.requestFocus();
        dokter.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setVisible(true);
    }
}//GEN-LAST:event_BtnDokterActionPerformed

private void BtnDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnDokterKeyPressed
    if(evt.getKeyCode()==KeyEvent.VK_SPACE){
        BtnDokterActionPerformed(null);
    }else{
        Valid.pindah(evt,Diet,BtnPoli);
    }        
}//GEN-LAST:event_BtnDokterKeyPressed

private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
  isForm();                
}//GEN-LAST:event_ChkInputActionPerformed

    private void DTPCari1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DTPCari1KeyPressed

    }//GEN-LAST:event_DTPCari1KeyPressed

    private void DTPCari2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DTPCari2KeyPressed
        R2.setSelected(true);
    }//GEN-LAST:event_DTPCari2KeyPressed

    private void DTPCari3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DTPCari3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_DTPCari3KeyPressed

    private void DTPCari4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DTPCari4KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_DTPCari4KeyPressed

    private void BtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditActionPerformed
        if(TNoRM.getText().trim().equals("")||TPasien.getText().trim().equals("")){
            Valid.textKosong(TNoRM,"pasien");
        }else if(NmDokter.getText().trim().equals("")||KdDokter.getText().trim().equals("")){
            Valid.textKosong(KdDokter,"Dokter");
        }else if(NmPoli.getText().trim().equals("")||NmPoli.getText().trim().equals("")){
            Valid.textKosong(KdPoli,"Poli");
        }else if(NoAntrian.getText().trim().equals("")){
            Valid.textKosong(NoAntrian,"No.Antrian");
        }else if(NoSurat.getText().trim().equals("")){
            Valid.textKosong(NoSurat,"No.Surat");
        }else if(NoReg.getText().trim().equals("")){
            Valid.textKosong(NoReg,"No.Antri");
        }else if(Terapi.getText().trim().equals("")){
            Valid.textKosong(Terapi,"Terapi");
        }else if(Diagnosa.getText().trim().equals("")){
            Valid.textKosong(Diagnosa,"Diagnosa");
        }else if(Diet.getText().trim().equals("")){
            Valid.textKosong(Diet,"Diet");
        }else if(Ngt.getText().trim().equals("")){
            Valid.textKosong(Ngt,"Perawatan NGT/Kateter/Lainnya");
        }else if(Luka.getText().trim().equals("")){
            Valid.textKosong(Luka,"Perawatan Luka");
        }else if(Lain.getText().trim().equals("")){
            Valid.textKosong(Lain,"Perawatan Lainnya");
        }else{
            if(tbObat.getSelectedRow()!= -1){
                if(Sequel.mengedittf("surat_kontrol_umum","tahun=? and no_antrian=?","tahun=?,no_surat=?,no_rkm_medis=?,diagnosa=?,terapi=?,diet=?,ngt=?,luka=?,lain=?,tunda=?,tanggal_datang=?,tanggal_rujukan=?,no_antrian=?,kd_dokter=?,status=?",17,new String[]{
                        TanggalPeriksa.getSelectedItem().toString().substring(6,10),NoSurat.getText(),TNoRM.getText(),Diagnosa.getText(),Terapi.getText(),
                        Diet.getText(),Ngt.getText(),Luka.getText(),Lain.getText(),Tunda.getText(),Valid.SetTgl(TanggalPeriksa.getSelectedItem()+"")+" "+TanggalPeriksa.getSelectedItem().toString().substring(11,19),
                        Valid.SetTgl(TanggalSurat.getSelectedItem()+"")+" "+TanggalSurat.getSelectedItem().toString().substring(11,19),NoAntrian.getText(),KdDokter.getText(),Status.getSelectedItem().toString(),
                        tbObat.getValueAt(tbObat.getSelectedRow(),0).toString(),tbObat.getValueAt(tbObat.getSelectedRow(),13).toString()
                  })==true){
                    Sequel.mengedit3("booking_registrasi","no_rkm_medis=? and tanggal_periksa=?","tanggal_booking=?,no_rkm_medis=?,tanggal_periksa=?,kd_dokter=?,kd_poli=?,no_reg=?",8,new String[]{
                         Valid.SetTgl(TanggalPeriksa.getSelectedItem()+""),TNoRM.getText(),
                         Valid.SetTgl(TanggalPeriksa.getSelectedItem()+""),KdDokter.getText(),
                         KdPoli.getText(),NoReg.getText(),tbObat.getValueAt(tbObat.getSelectedRow(),1).toString(),
                         tbObat.getValueAt(tbObat.getSelectedRow(),9).toString()
                    });
                    emptTeks();
                    tampil();
                }
            }else{
                JOptionPane.showMessageDialog(null,"Maaf, Silahkan anda pilih terlebih dulu data yang mau anda ganti...\n Klik data pada table untuk memilih data...!!!!");
            }                
        }
    }//GEN-LAST:event_BtnEditActionPerformed

    private void BtnEditKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnEditKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnEditActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnHapus, BtnKeluar);
        }
    }//GEN-LAST:event_BtnEditKeyPressed

    private void StatusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StatusKeyPressed
        Valid.pindah(evt,TanggalPeriksa,Diagnosa);
    }//GEN-LAST:event_StatusKeyPressed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        tampil();
    }//GEN-LAST:event_formWindowOpened

    private void DTPCari1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_DTPCari1ItemStateChanged
        R2.setSelected(true);
    }//GEN-LAST:event_DTPCari1ItemStateChanged

    private void DTPCari3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_DTPCari3ItemStateChanged
        R3.setSelected(true);
    }//GEN-LAST:event_DTPCari3ItemStateChanged

    private void DTPCari4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_DTPCari4ItemStateChanged
        R3.setSelected(true);
    }//GEN-LAST:event_DTPCari4ItemStateChanged

    private void NoAntrianKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NoAntrianKeyPressed
        Valid.pindah(evt,TanggalPeriksa,NoReg);
    }//GEN-LAST:event_NoAntrianKeyPressed

    private void DiagnosaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiagnosaKeyPressed
        Valid.pindah(evt,Status,Terapi);
    }//GEN-LAST:event_DiagnosaKeyPressed

    private void BtnPoliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPoliKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnPoliActionPerformed(null);
        }else{
            Valid.pindah(evt,BtnDokter,TanggalPeriksa);
        }
    }//GEN-LAST:event_BtnPoliKeyPressed

    private void BtnPoliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPoliActionPerformed
        if(aktifjadwal.equals("aktif")){
            if(akses.getkode().equals("Admin Utama")){
                poli.isCek();        
                poli.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
                poli.setLocationRelativeTo(internalFrame1);
                poli.setVisible(true);
            }else{
                poli2.isCek();                     
                poli2.SetHari(TanggalPeriksa.getDate());
                poli2.tampil(); 
                poli2.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
                poli2.setLocationRelativeTo(internalFrame1);
                poli2.setVisible(true);
            }                
        }else{
            poli.isCek();        
            poli.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
            poli.setLocationRelativeTo(internalFrame1);
            poli.setVisible(true);
        }
    }//GEN-LAST:event_BtnPoliActionPerformed

    private void NoRegKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NoRegKeyPressed
        Valid.pindah(evt,NoAntrian,BtnSimpan);
    }//GEN-LAST:event_NoRegKeyPressed

    private void MnCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnCetakActionPerformed
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
            TanggalPeriksa.requestFocus();
        }else if(TPasien.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, Gagal menghapus. Pilih dulu data yang mau dihapus.\nKlik data pada table untuk memilih...!!!!");
        }else if(!(TPasien.getText().trim().equals(""))){
            if(tbObat.getSelectedRow()!= -1){
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                Map<String, Object> param = new HashMap<>();  
                param.put("namars",akses.getnamars());
                param.put("alamatrs",akses.getalamatrs());
                param.put("kotars",akses.getkabupatenrs());
                param.put("propinsirs",akses.getpropinsirs());
                param.put("kontakrs",akses.getkontakrs());
                param.put("emailrs",akses.getemailrs());   
                param.put("logo",Sequel.cariGambar("select logo from setting")); 
               // param.put("nama",TPasien.getText());
                param.put("tgl_lahir",Sequel.cariIsi("select DATE_FORMAT(tgl_lahir,'%d-%m-%Y') from pasien where no_rkm_medis=?",TNoRM.getText()));
               // param.put("no_rm",TNoRM.getText());
               // param.put("diagnosa",Diagnosa.getText());
                Sequel.queryu("truncate table temporary_booking_registrasi");                
                Sequel.menyimpan("temporary_booking_registrasi","'0','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),0).toString()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),1).toString()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),2).toString()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),3).toString()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),4).toString()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),5).toString()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),6).toString()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),7).toString()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),8).toString()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),9).toString()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),10).toString()+"','"+
                    TanggalPeriksa.getSelectedItem()+"','"+
                    TanggalSurat.getSelectedItem()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),13).toString()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),14).toString()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),15).toString()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),16).toString()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),17).toString()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),18).toString()+"','"+    
                    tabMode.getValueAt(tbObat.getSelectedRow(),19).toString()+"','','','','','','','','','','','','','','','','',''","Rekap Nota Pembayaran");

                   Valid.MyReport("rptSuratKontrolUmumRanap.jasper","report","::[ Surat Kontrol Umum Ranap ]::",param); 
//                 Valid.MyReportqry("rptSuratKontrolUmumRanap.jasper","report","::[ Surat Kontrol Umum Ranap ]::",
//                              "" +
//                              " " +
//                              " " +
//                              " "+
//                              " "+
//                              "",param);
                this.setCursor(Cursor.getDefaultCursor());
            }else{
                JOptionPane.showMessageDialog(null,"Maaf, Silahkan anda pilih terlebih dulu data yang mau anda hapus...\n Klik data pada table untuk memilih data...!!!!");
            }  
        }
    }//GEN-LAST:event_MnCetakActionPerformed

    private void tbObatKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbObatKeyReleased
        if(tabMode.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbObatKeyReleased

    private void TerapiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TerapiKeyPressed
      
    }//GEN-LAST:event_TerapiKeyPressed

    private void NoRegActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NoRegActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NoRegActionPerformed

    private void NoSuratActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NoSuratActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NoSuratActionPerformed

    private void TanggalSuratKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TanggalSuratKeyPressed
        Valid.pindah(evt,TCari,Status);
    }//GEN-LAST:event_TanggalSuratKeyPressed

    private void TanggalPeriksaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TanggalPeriksaKeyPressed
        Valid.pindah(evt,BtnPoli,NoAntrian);
    }//GEN-LAST:event_TanggalPeriksaKeyPressed

    private void TanggalPeriksaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_TanggalPeriksaItemStateChanged
        try {
            isNomer();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_TanggalPeriksaItemStateChanged

    private void MnCetak1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnCetak1ActionPerformed
       if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
            TanggalPeriksa.requestFocus();
        }else if(TPasien.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, Gagal menghapus. Pilih dulu data yang mau dihapus.\nKlik data pada table untuk memilih...!!!!");
        }else if(!(TPasien.getText().trim().equals(""))){
            if(tbObat.getSelectedRow()!= -1){
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                Map<String, Object> param = new HashMap<>();  
                param.put("namars",akses.getnamars());
                param.put("alamatrs",akses.getalamatrs());
                param.put("kotars",akses.getkabupatenrs());
                param.put("propinsirs",akses.getpropinsirs());
                param.put("kontakrs",akses.getkontakrs());
                param.put("emailrs",akses.getemailrs());   
                param.put("logo",Sequel.cariGambar("select logo from setting")); 
               // param.put("nama",TPasien.getText());
                param.put("tgl_lahir",Sequel.cariIsi("select DATE_FORMAT(tgl_lahir,'%d-%m-%Y') from pasien where no_rkm_medis=?",TNoRM.getText()));
               // param.put("no_rm",TNoRM.getText());
               // param.put("diagnosa",Diagnosa.getText());
                Sequel.queryu("truncate table temporary_booking_registrasi");                
                Sequel.menyimpan("temporary_booking_registrasi","'0','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),0).toString()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),1).toString()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),2).toString()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),3).toString()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),4).toString()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),5).toString()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),6).toString()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),7).toString()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),8).toString()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),9).toString()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),10).toString()+"','"+
                    TanggalPeriksa.getSelectedItem()+"','"+
                    TanggalSurat.getSelectedItem()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),13).toString()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),14).toString()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),15).toString()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),16).toString()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),17).toString()+"','"+
                    tabMode.getValueAt(tbObat.getSelectedRow(),18).toString()+"','"+    
                    tabMode.getValueAt(tbObat.getSelectedRow(),19).toString()+"','','','','','','','','','','','','','','','','',''","Rekap Nota Pembayaran");

                   Valid.MyReport("rptSuratKontrolUmumRanap1.jasper","report","::[ Surat Kontrol Umum Ranap ]::",param); 
//                 Valid.MyReportqry("rptSuratKontrolUmumRanap.jasper","report","::[ Surat Kontrol Umum Ranap ]::",
//                              "" +
//                              " " +
//                              " " +
//                              " "+
//                              " "+
//                              "",param);
                this.setCursor(Cursor.getDefaultCursor());
            }else{
                JOptionPane.showMessageDialog(null,"Maaf, Silahkan anda pilih terlebih dulu data yang mau anda hapus...\n Klik data pada table untuk memilih data...!!!!");
            }  
        }
    }//GEN-LAST:event_MnCetak1ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            SuratKontrolUmum dialog = new SuratKontrolUmum(new javax.swing.JFrame(), true);
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
    private widget.Button BtnDokter;
    private widget.Button BtnEdit;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnPoli;
    private widget.Button BtnPrint;
    private widget.Button BtnSimpan;
    private widget.CekBox ChkInput;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.Tanggal DTPCari3;
    private widget.Tanggal DTPCari4;
    private widget.TextBox Diagnosa;
    private widget.TextBox Diet;
    private widget.PanelBiasa FormInput;
    private widget.TextBox KdDokter;
    private widget.TextBox KdPoli;
    private widget.Label LCount;
    private widget.TextBox Lain;
    private widget.TextBox Luka;
    private javax.swing.JMenuItem MnCetak;
    private javax.swing.JMenuItem MnCetak1;
    private widget.TextBox Ngt;
    private widget.TextBox NmDokter;
    private widget.TextBox NmPoli;
    private widget.TextBox NoAntrian;
    private widget.TextBox NoReg;
    private widget.TextBox NoSurat;
    private javax.swing.JPanel PanelInput;
    private widget.RadioButton R1;
    private widget.RadioButton R2;
    private widget.RadioButton R3;
    private widget.ScrollPane Scroll;
    private widget.ComboBox Status;
    private widget.TextBox TCari;
    private widget.TextBox TNoRM;
    private widget.TextBox TPasien;
    private widget.Tanggal TanggalPeriksa;
    private widget.Tanggal TanggalSurat;
    private widget.TextArea Terapi;
    private widget.TextBox Tunda;
    private javax.swing.ButtonGroup buttonGroup1;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel10;
    private widget.Label jLabel11;
    private widget.Label jLabel12;
    private widget.Label jLabel13;
    private widget.Label jLabel15;
    private widget.Label jLabel16;
    private widget.Label jLabel17;
    private widget.Label jLabel18;
    private widget.Label jLabel19;
    private widget.Label jLabel20;
    private widget.Label jLabel21;
    private widget.Label jLabel22;
    private widget.Label jLabel23;
    private widget.Label jLabel24;
    private widget.Label jLabel25;
    private widget.Label jLabel26;
    private widget.Label jLabel37;
    private widget.Label jLabel4;
    private widget.Label jLabel5;
    private widget.Label jLabel6;
    private widget.Label jLabel7;
    private widget.Label jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private widget.panelisi panelCari;
    private widget.panelisi panelGlass10;
    private widget.panelisi panelGlass8;
    private widget.ScrollPane scrollPane2;
    private widget.Table tbObat;
    // End of variables declaration//GEN-END:variables

    private void tampil() {     
        if(R1.isSelected()==true){
            status=" surat_kontrol_umum.status='Menunggu' ";
        }else if(R2.isSelected()==true){
            status=" surat_kontrol_umum_umum.tanggal_rujukan between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+"' ";
        }else if(R3.isSelected()==true){
            status=" surat_kontrol_umum.status<>'Menunggu' and surat_kontrol_umum_umum.tanggal_rujukan between '"+Valid.SetTgl(DTPCari3.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari4.getSelectedItem()+"")+"' ";           
        }
        Valid.tabelKosong(tabMode);
        try {
            ps=koneksi.prepareStatement(
                    "select surat_kontrol_umum.tahun,surat_kontrol_umum.no_surat,surat_kontrol_umum.no_rkm_medis,pasien.nm_pasien,"+
                    "surat_kontrol_umum.diagnosa,surat_kontrol_umum.terapi,surat_kontrol_umum.diet, "+
                    "surat_kontrol_umum.luka,surat_kontrol_umum.ngt,surat_kontrol_umum.lain,surat_kontrol_umum.tunda,surat_kontrol_umum.tanggal_datang,surat_kontrol_umum.tanggal_rujukan,"+
                    "surat_kontrol_umum.no_antrian,surat_kontrol_umum.kd_dokter,dokter.nm_dokter,surat_kontrol_umum.status "+
                    "from surat_kontrol_umum inner join pasien inner join dokter on "+
                    "surat_kontrol_umum.no_rkm_medis=pasien.no_rkm_medis and surat_kontrol_umum.kd_dokter=dokter.kd_dokter "+
                    "where "+status+" and surat_kontrol_umum.no_rkm_medis like ? or "+
                    status+" and pasien.nm_pasien like ? or "+
                    status+" and surat_kontrol_umum.diagnosa like ? or "+
                    status+" and surat_kontrol_umum.terapi like ? or "+
                    status+" and surat_kontrol_umum.no_antrian like ? or "+
                    status+" and dokter.nm_dokter like ? order by surat_kontrol_umum.tanggal_rujukan,surat_kontrol_umum.no_antrian");
            try {
                ps.setString(1,"%"+TCari.getText().trim()+"%");
                ps.setString(2,"%"+TCari.getText().trim()+"%");
                ps.setString(3,"%"+TCari.getText().trim()+"%");
                ps.setString(4,"%"+TCari.getText().trim()+"%");
                ps.setString(5,"%"+TCari.getText().trim()+"%");
                ps.setString(6,"%"+TCari.getText().trim()+"%");
                rs=ps.executeQuery();
                while(rs.next()){
                    kdpoli="";nmpoli="";noantri="";
                    ps2=koneksi.prepareStatement(
                            "select booking_registrasi.kd_poli,poliklinik.nm_poli,booking_registrasi.no_reg "+
                            "from booking_registrasi inner join poliklinik on booking_registrasi.kd_poli=poliklinik.kd_poli "+
                            "where booking_registrasi.kd_dokter=? and booking_registrasi.tanggal_periksa=? and booking_registrasi.no_rkm_medis=?");
                    try {
                        ps2.setString(1,rs.getString("kd_dokter"));
                        ps2.setString(2,rs.getString("tanggal_datang"));
                        ps2.setString(3,rs.getString("no_rkm_medis"));
                        rs2=ps2.executeQuery();
                        if(rs2.next()){
                            kdpoli=rs2.getString("kd_poli");
                            nmpoli=rs2.getString("nm_poli");
                            noantri=rs2.getString("no_reg");
                        }
                    } catch (Exception e) {
                        System.out.println("Notif : "+e);
                    } finally{
                        if(rs2!=null){
                            rs2.close();
                        }
                        if(ps2!=null){
                            ps2.close();
                        }
                    }
                    tabMode.addRow(new Object[]{
                        rs.getInt("tahun"),rs.getString("no_surat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),
                        rs.getString("diagnosa"),rs.getString("terapi"),rs.getString("diet"),rs.getString("luka"),rs.getString("ngt"),                       
                        rs.getString("lain"),rs.getString("tunda"),rs.getString("tanggal_datang"),rs.getString("tanggal_rujukan"),rs.getString("no_antrian"),
                        noantri,rs.getString("kd_dokter"),rs.getString("nm_dokter"),kdpoli,
                        nmpoli,rs.getString("status")
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
        } catch (Exception e) {
            System.out.println("Notif : "+e);
        } 
        LCount.setText(""+tabMode.getRowCount());
    }


    public void emptTeks() {
        KdDokter.setText("");
        NmDokter.setText("");
        KdPoli.setText("");
        NmPoli.setText("");
        Diet.setText("");
        Luka.setText("");
        Ngt.setText("");
        Lain.setText("");
        Tunda.setText("");
        Terapi.setText("");
        Diagnosa.setText("");
        TanggalPeriksa.setDate(new Date());
        TanggalPeriksa.setDate(new Date());
        TanggalPeriksa.requestFocus();
        isNomer();
        Valid.autoNomer3("select ifnull(MAX(CONVERT(RIGHT(no_surat,3),signed)),0) from surat_kontrol_umum where tanggal_rujukan='"+Valid.SetTgl(TanggalPeriksa.getSelectedItem()+"")+"' ",
                "SKU"+TanggalPeriksa.getSelectedItem().toString().substring(6,10)+TanggalPeriksa.getSelectedItem().toString().substring(3,5)+TanggalPeriksa.getSelectedItem().toString().substring(0,2),3,NoSurat); 
        NoSurat.requestFocus();
        
    }
    
    private void isNomer(){
        switch (URUTNOREG) {
            case "poli":
                Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from booking_registrasi where kd_poli='"+KdPoli.getText()+"' and tanggal_periksa='"+Valid.SetTgl(TanggalPeriksa.getSelectedItem()+"")+"'","",3,NoReg);
                break;
            case "dokter":
                Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from booking_registrasi where kd_dokter='"+KdDokter.getText()+"' and tanggal_periksa='"+Valid.SetTgl(TanggalPeriksa.getSelectedItem()+"")+"'","",3,NoReg);
                break;
            case "dokter + poli":             
                Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from booking_registrasi where kd_dokter='"+KdDokter.getText()+"' and kd_poli='"+KdPoli.getText()+"' and tanggal_periksa='"+Valid.SetTgl(TanggalPeriksa.getSelectedItem()+"")+"'","",3,NoReg);
                break;
            default:
                Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from booking_registrasi where kd_dokter='"+KdDokter.getText()+"' and tanggal_periksa='"+Valid.SetTgl(TanggalPeriksa.getSelectedItem()+"")+"'","",3,NoReg);
                break;
        }
        //nomorSurat();
           Valid.autoNomer3("select ifnull(MAX(CONVERT(RIGHT(no_antrian,6),signed)),0) from surat_kontrol_umum where tahun='"+TanggalPeriksa.getSelectedItem().toString().substring(6,10)+"' ","",6,NoAntrian);  
           //Valid.autoNomerSuratKhusus("select ifnull(MAX(CONVERT(LEFT(no_surat,3),signed)),0) from surat_kontrol_umum where "              
           //    + "tanggal_datang like '%" + Valid.SetTgl(TanggalPeriksa.getSelectedItem() + "").substring(0, 7) + "%' ", "/SKTRL/" + bln_romawi + "/" + Valid.SetTgl(TanggalPeriksa.getSelectedItem() + "").substring(0, 4), 3, NoSurat);
           NoSurat.requestFocus();       
    }
     /* public void nomorSurat() {
        bln_angka = "";
        bln_romawi = "";
        bln_angka = Sequel.cariIsi("Select month(now())");
        if (bln_angka.equals("1")) {
            bln_romawi = "I";
        } else if (bln_angka.equals("2")) {
            bln_romawi = "II";
        } else if (bln_angka.equals("3")) {
            bln_romawi = "III";
        } else if (bln_angka.equals("4")) {
            bln_romawi = "IV";
        } else if (bln_angka.equals("5")) {
            bln_romawi = "V";
        } else if (bln_angka.equals("6")) {
            bln_romawi = "VI";
        } else if (bln_angka.equals("7")) {
            bln_romawi = "VII";
        } else if (bln_angka.equals("8")) {
            bln_romawi = "VIII";
        } else if (bln_angka.equals("9")) {
            bln_romawi = "IX";
        } else if (bln_angka.equals("10")) {
            bln_romawi = "X";
        } else if (bln_angka.equals("11")) {
            bln_romawi = "XI";
        } else if (bln_angka.equals("12")) {
            bln_romawi = "XII";
        }
       
        Valid.autoNomer3("select ifnull(MAX(CONVERT(RIGHT(no_antrian,6),signed)),0) from surat_kontrol_umum where tahun='"+TanggalPeriksa.getSelectedItem().toString().substring(6,10)+"' ","",6,NoAntrian);  
      // Valid.autoNomerSuratKhusus("select ifnull(MAX(CONVERT(LEFT(no_surat,3),signed)),0) from surat_kontrol_umum where "              
        //      + "tanggal_datang like '%" + Valid.SetTgl(TanggalPeriksa.getSelectedItem() + "").substring(0, 7) + "%' ", "/SKTRL/" + bln_romawi + "/" + Valid.SetTgl(TanggalPeriksa.getSelectedItem() + "").substring(0, 4), 3, NoSurat);
    //NoAntrian.requestFocus();     
     } */
      
    private void getData() {
        if(tbObat.getSelectedRow()!= -1){
            NoSurat.setText(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString()); 
            TNoRM.setText(tbObat.getValueAt(tbObat.getSelectedRow(),2).toString()); 
            TPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(),3).toString());
            Diagnosa.setText(tbObat.getValueAt(tbObat.getSelectedRow(),4).toString());
            Terapi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),5).toString());
            Diet.setText(tbObat.getValueAt(tbObat.getSelectedRow(),6).toString());
            Luka.setText(tbObat.getValueAt(tbObat.getSelectedRow(),7).toString());
            Ngt.setText(tbObat.getValueAt(tbObat.getSelectedRow(),8).toString());
            Lain.setText(tbObat.getValueAt(tbObat.getSelectedRow(),9).toString());
            Tunda.setText(tbObat.getValueAt(tbObat.getSelectedRow(),10).toString());
            Valid.SetTgl(TanggalPeriksa,tbObat.getValueAt(tbObat.getSelectedRow(),11).toString());
            Valid.SetTgl(TanggalSurat,tbObat.getValueAt(tbObat.getSelectedRow(),12).toString());
//            Valid.SetTgl2(TanggalSurat,tbObat.getValueAt(tbObat.getSelectedRow(),12).toString());
//            Valid.SetTgl2(TanggalPeriksa,tbObat.getValueAt(tbObat.getSelectedRow(),12).toString());
            NoAntrian.setText(tbObat.getValueAt(tbObat.getSelectedRow(),13).toString());
            NoReg.setText(tbObat.getValueAt(tbObat.getSelectedRow(),14).toString());
            KdDokter.setText(tbObat.getValueAt(tbObat.getSelectedRow(),15).toString());
            NmDokter.setText(tbObat.getValueAt(tbObat.getSelectedRow(),16).toString());
            KdPoli.setText(tbObat.getValueAt(tbObat.getSelectedRow(),17).toString());
            NmPoli.setText(tbObat.getValueAt(tbObat.getSelectedRow(),18).toString());
            Status.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),19).toString());
        }
    }
    
    public void setNoRm(String norm,String nama) {
        TNoRM.setText(norm);
        TPasien.setText(nama);
        TCari.setText(norm);
        ChkInput.setSelected(true);
     // Terapi.setText(Sequel.cariIsi("select keluhan_utama from penilaian_awal_keperawatan_ralan where no_rawat=?",TKeluhan,TNoRw.getText());
        isForm();
        tampil();
    }
    
    
    
    public void setNoRm(String norm,String nama,String kodepoli,String namapoli,String kodedokter,String namadokter) {
        TNoRM.setText(norm);
        TPasien.setText(nama);
        KdPoli.setText(kodepoli);
        NmPoli.setText(namapoli);
        KdDokter.setText(kodedokter);
        NmDokter.setText(namadokter);
        TCari.setText(norm);
        ChkInput.setSelected(true);
        isForm();
        tampil();
    }
    
    private void isForm(){
        if(ChkInput.isSelected()==true){
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH,300));
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
        BtnSimpan.setEnabled(akses.getsurat_sakit());
        BtnHapus.setEnabled(akses.getsurat_sakit());
        BtnPrint.setEnabled(akses.getsurat_sakit());
        BtnEdit.setEnabled(akses.getsurat_sakit());
    }

    public JTable getTable(){
        return tbObat;
    }
  
    

    private void isBooking(){
        if(Sequel.menyimpantf("surat_kontrol_umum","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?","Tahun dan nomor surat",15,new String[]{
             NoSurat.getText(),TanggalPeriksa.getSelectedItem().toString().substring(6,10),TNoRM.getText(),Diagnosa.getText(),Terapi.getText(),
             Diet.getText(),Luka.getText(),Ngt.getText(),Lain.getText(),Tunda.getText(),Valid.SetTgl(TanggalPeriksa.getSelectedItem()+"")+" "+TanggalPeriksa.getSelectedItem().toString().substring(11,19),
             Valid.SetTgl(TanggalSurat.getSelectedItem()+"")+" "+TanggalSurat.getSelectedItem().toString().substring(11,19),NoAntrian.getText(),KdDokter.getText(),Status.getSelectedItem().toString()
         })==true){
             Sequel.menyimpan2("booking_registrasi","?,?,?,?,?,?,?,?,?,?,?","Pasien dan Tanggal",11,new String[]{
                Valid.SetTgl(TanggalPeriksa.getSelectedItem()+""),TanggalPeriksa.getSelectedItem().toString().substring(11,19),TNoRM.getText(),
                Valid.SetTgl(TanggalPeriksa.getSelectedItem()+""),KdDokter.getText(),
                KdPoli.getText(),NoReg.getText(),Sequel.cariIsi("select kd_pj from pasien where no_rkm_medis=?",TNoRM.getText()),"0",
                Valid.SetTgl(TanggalPeriksa.getSelectedItem()+"")+" "+TanggalPeriksa.getSelectedItem().toString().substring(11,19),
                "belum"
              });
             emptTeks();
             tampil();
         } 
    }
    
}
