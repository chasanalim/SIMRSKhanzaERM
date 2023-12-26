/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DlgLhtBiaya.java
 *
 * Created on 12 Jul 10, 16:21:34
 */

package rekammedis;

import fungsi.WarnaTable;
import fungsi.akses;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.HyperlinkEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import simrskhanza.DlgCariPasien;

/**
 *
 * @author windiarto
 */
public final class RMBerkasKlaim extends javax.swing.JDialog {    
    private validasi Valid=new validasi();    
    private final sekuel Sequel=new sekuel();
    private final DefaultTableModel tabModeRegistrasi;
    private PreparedStatement ps,ps2;
    private ResultSet rs,rs2,rs3,rs4,rs5;
    private Connection koneksi=koneksiDB.condb();
    private int i=0,urut=0,w=0,s=0,urutdpjp=0;
    private double biayaperawatan=0;
    private String kddpjp="",dpjp="",dokterrujukan="",polirujukan="",keputusan="",ke1="",ke2="",ke3="",ke4="",ke5="",ke6="",file="";
    private StringBuilder htmlContent;
    private HttpClient http = new HttpClient();
    private GetMethod get;
    private DlgCariPasien pasien=new DlgCariPasien(null,true);

    /** Creates new form DlgLhtBiaya
     * @param parent
     * @param modal */
    public RMBerkasKlaim(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(8,1);
        setSize(885,674);
        
        tabModeRegistrasi=new DefaultTableModel(null,new Object[]{
                "No.","No.Rawat","Tanggal","Jam","Kd.Dokter","Dokter Dituju/DPJP","Umur","Poliklinik/Kamar","Jenis Bayar"
            }){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbRegistrasi.setModel(tabModeRegistrasi);

        tbRegistrasi.setPreferredScrollableViewportSize(new Dimension(800,800));
        tbRegistrasi.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 9; i++) {
            TableColumn column = tbRegistrasi.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(25);
            }else if(i==1){
                column.setPreferredWidth(110);
            }else if(i==2){
                column.setPreferredWidth(70);
            }else if(i==3){
                column.setPreferredWidth(60);
            }else if(i==4){
                column.setPreferredWidth(70);
            }else if(i==5){
                column.setPreferredWidth(250);   
            }else if(i==6){
                column.setPreferredWidth(40);
            }else if(i==7){
                column.setPreferredWidth(200);
            }else if(i==8){
                column.setPreferredWidth(110);
            }
        }
        tbRegistrasi.setDefaultRenderer(Object.class, new WarnaTable());
        
        NoRM.setDocument(new batasInput((byte)20).getKata(NoRM));
        NoRawat.setDocument(new batasInput((byte)20).getKata(NoRawat));
        
        pasien.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(pasien.getTable().getSelectedRow()!= -1){                   
                    NoRM.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),0).toString());
                    NmPasien.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),1).toString());
                    Jk.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),3).toString());
                    TempatLahir.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),4).toString());
                    TanggalLahir.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),5).toString());
                    IbuKandung.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),6).toString());
                    Alamat.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),7).toString());
                    GD.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),8).toString());
                    StatusNikah.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),10).toString());
                    Agama.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),11).toString());
                    Pendidikan.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),15).toString());
                    Bahasa.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),26).toString());
                    CacatFisik.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),32).toString());
                }    
                NoRM.requestFocus();
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
        
        pasien.getTable().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_SPACE){
                    pasien.dispose();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        
        HTMLEditorKit kit = new HTMLEditorKit();
        LoadHTMLRiwayatPerawatan.setEditorKit(kit);
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule(".isi td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}.isi a{text-decoration:none;color:#8b9b95;padding:0 0 0 0px;font-family: Tahoma;font-size: 8.5px;border: white;}");
        Document doc = kit.createDefaultDocument();
        LoadHTMLRiwayatPerawatan.setDocument(doc);
        LoadHTMLRiwayatPerawatan.setEditable(false);
        LoadHTMLRiwayatPerawatan.addHyperlinkListener(e -> {
            if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
              Desktop desktop = Desktop.getDesktop();
              try {
                desktop.browse(e.getURL().toURI());
              } catch (Exception ex) {
                ex.printStackTrace();
              }
            }
        });

        LoadHTMLRiwayatPerawatanRanap.setEditorKit(kit);
        LoadHTMLRiwayatPerawatanRanap.setDocument(doc);
        LoadHTMLRiwayatPerawatanRanap.setEditable(false);
        LoadHTMLRiwayatPerawatanRanap.addHyperlinkListener(e -> {
            if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
              Desktop desktop = Desktop.getDesktop();
              try {
                desktop.browse(e.getURL().toURI());
              } catch (Exception ex) {
                ex.printStackTrace();
              }
            }
        });
        ChkAccor.setSelected(false);
        isMenu();
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
        Pekerjaan = new widget.TextBox();
        internalFrame1 = new widget.InternalFrame();
        panelGlass5 = new widget.panelisi();
        R1 = new widget.RadioButton();
        R2 = new widget.RadioButton();
        R3 = new widget.RadioButton();
        Tgl1 = new widget.Tanggal();
        label18 = new widget.Label();
        Tgl2 = new widget.Tanggal();
        R4 = new widget.RadioButton();
        NoRawat = new widget.TextBox();
        BtnCari1 = new widget.Button();
        label19 = new widget.Label();
        BtnPrint = new widget.Button();
        BtnKeluar = new widget.Button();
        TabRawat = new javax.swing.JTabbedPane();
        Scroll1 = new widget.ScrollPane();
        tbRegistrasi = new widget.Table();
        internalFrame2 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        LoadHTMLRiwayatPerawatan = new widget.editorpane();
        PanelAccor = new widget.PanelBiasa();
        ChkAccor = new widget.CekBox();
        ScrollMenu = new widget.ScrollPane();
        FormMenu = new widget.PanelBiasa();
        chkSemua = new widget.CekBox();
        chkSEPRajal = new widget.CekBox();
        chkSBPK = new widget.CekBox();
        chkSBPKIGD = new widget.CekBox();
        chkFormRehabMedis = new widget.CekBox();
        chkProgramTerapi = new widget.CekBox();
        chkPemeriksaanLaborat = new widget.CekBox();
        chkPemeriksaanRadiologi = new widget.CekBox();
        chkBilling = new widget.CekBox();
        internalFrame3 = new widget.InternalFrame();
        Scroll2 = new widget.ScrollPane();
        LoadHTMLRiwayatPerawatanRanap = new widget.editorpane();
        PanelAccor1 = new widget.PanelBiasa();
        ChkAccor1 = new widget.CekBox();
        ScrollMenu1 = new widget.ScrollPane();
        FormMenu1 = new widget.PanelBiasa();
        chkSemuaRanap = new widget.CekBox();
        chkSEPRanap = new widget.CekBox();
        chkSupri = new widget.CekBox();
        chkPemeriksaanLaboratRanap = new widget.CekBox();
        chkPemeriksaanRadiologiRanap = new widget.CekBox();
        chkResume = new widget.CekBox();
        chkBillingRanap = new widget.CekBox();
        PanelInput = new javax.swing.JPanel();
        ChkInput = new widget.CekBox();
        FormInput = new widget.panelisi();
        label17 = new widget.Label();
        NoRM = new widget.TextBox();
        NmPasien = new widget.TextBox();
        BtnPasien = new widget.Button();
        label20 = new widget.Label();
        Jk = new widget.TextBox();
        label21 = new widget.Label();
        TempatLahir = new widget.TextBox();
        label22 = new widget.Label();
        Alamat = new widget.TextBox();
        label23 = new widget.Label();
        GD = new widget.TextBox();
        label24 = new widget.Label();
        IbuKandung = new widget.TextBox();
        TanggalLahir = new widget.TextBox();
        label25 = new widget.Label();
        Agama = new widget.TextBox();
        StatusNikah = new widget.TextBox();
        label26 = new widget.Label();
        Pendidikan = new widget.TextBox();
        label27 = new widget.Label();
        label28 = new widget.Label();
        Bahasa = new widget.TextBox();
        label29 = new widget.Label();
        CacatFisik = new widget.TextBox();

        Pekerjaan.setEditable(false);
        Pekerjaan.setName("Pekerjaan"); // NOI18N
        Pekerjaan.setPreferredSize(new java.awt.Dimension(100, 23));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Data Berkas Klaim BPJS ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass5.setName("panelGlass5"); // NOI18N
        panelGlass5.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        R1.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        buttonGroup1.add(R1);
        R1.setSelected(true);
        R1.setText("2 Riwayat Terakhir");
        R1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R1.setName("R1"); // NOI18N
        R1.setPreferredSize(new java.awt.Dimension(120, 23));
        panelGlass5.add(R1);

        R2.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        buttonGroup1.add(R2);
        R2.setText("Semua Riwayat");
        R2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R2.setName("R2"); // NOI18N
        R2.setPreferredSize(new java.awt.Dimension(104, 23));
        panelGlass5.add(R2);

        R3.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        buttonGroup1.add(R3);
        R3.setText("Tanggal :");
        R3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R3.setName("R3"); // NOI18N
        R3.setPreferredSize(new java.awt.Dimension(75, 23));
        panelGlass5.add(R3);

        Tgl1.setDisplayFormat("dd-MM-yyyy");
        Tgl1.setName("Tgl1"); // NOI18N
        Tgl1.setPreferredSize(new java.awt.Dimension(90, 23));
        Tgl1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Tgl1KeyPressed(evt);
            }
        });
        panelGlass5.add(Tgl1);

        label18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label18.setText("s.d.");
        label18.setName("label18"); // NOI18N
        label18.setPreferredSize(new java.awt.Dimension(25, 23));
        panelGlass5.add(label18);

        Tgl2.setDisplayFormat("dd-MM-yyyy");
        Tgl2.setName("Tgl2"); // NOI18N
        Tgl2.setPreferredSize(new java.awt.Dimension(90, 23));
        Tgl2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Tgl2KeyPressed(evt);
            }
        });
        panelGlass5.add(Tgl2);

        R4.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        buttonGroup1.add(R4);
        R4.setText("Nomor :");
        R4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R4.setName("R4"); // NOI18N
        R4.setPreferredSize(new java.awt.Dimension(67, 23));
        panelGlass5.add(R4);

        NoRawat.setName("NoRawat"); // NOI18N
        NoRawat.setPreferredSize(new java.awt.Dimension(135, 23));
        NoRawat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NoRawatKeyPressed(evt);
            }
        });
        panelGlass5.add(NoRawat);

        BtnCari1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari1.setMnemonic('2');
        BtnCari1.setToolTipText("Alt+2");
        BtnCari1.setName("BtnCari1"); // NOI18N
        BtnCari1.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCari1ActionPerformed(evt);
            }
        });
        panelGlass5.add(BtnCari1);

        label19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label19.setName("label19"); // NOI18N
        label19.setPreferredSize(new java.awt.Dimension(15, 23));
        panelGlass5.add(label19);

        BtnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        BtnPrint.setMnemonic('T');
        BtnPrint.setToolTipText("Alt+T");
        BtnPrint.setName("BtnPrint"); // NOI18N
        BtnPrint.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrintActionPerformed(evt);
            }
        });
        panelGlass5.add(BtnPrint);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('K');
        BtnKeluar.setToolTipText("Alt+K");
        BtnKeluar.setName("BtnKeluar"); // NOI18N
        BtnKeluar.setPreferredSize(new java.awt.Dimension(28, 23));
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
        panelGlass5.add(BtnKeluar);

        internalFrame1.add(panelGlass5, java.awt.BorderLayout.PAGE_END);

        TabRawat.setBackground(new java.awt.Color(255, 255, 254));
        TabRawat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(241, 246, 236)));
        TabRawat.setForeground(new java.awt.Color(50, 50, 50));
        TabRawat.setName("TabRawat"); // NOI18N
        TabRawat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabRawatMouseClicked(evt);
            }
        });

        Scroll1.setName("Scroll1"); // NOI18N
        Scroll1.setOpaque(true);

        tbRegistrasi.setName("tbRegistrasi"); // NOI18N
        Scroll1.setViewportView(tbRegistrasi);

        TabRawat.addTab("Riwayat Kunjungan", Scroll1);

        internalFrame2.setBackground(new java.awt.Color(235, 255, 235));
        internalFrame2.setBorder(null);
        internalFrame2.setName("internalFrame2"); // NOI18N
        internalFrame2.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        LoadHTMLRiwayatPerawatan.setBorder(null);
        LoadHTMLRiwayatPerawatan.setName("LoadHTMLRiwayatPerawatan"); // NOI18N
        Scroll.setViewportView(LoadHTMLRiwayatPerawatan);

        internalFrame2.add(Scroll, java.awt.BorderLayout.CENTER);

        PanelAccor.setBackground(new java.awt.Color(255, 255, 255));
        PanelAccor.setName("PanelAccor"); // NOI18N
        PanelAccor.setPreferredSize(new java.awt.Dimension(275, 43));
        PanelAccor.setLayout(new java.awt.BorderLayout());

        ChkAccor.setBackground(new java.awt.Color(255, 250, 248));
        ChkAccor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(250, 255, 248)));
        ChkAccor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kanan.png"))); // NOI18N
        ChkAccor.setFocusable(false);
        ChkAccor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkAccor.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChkAccor.setName("ChkAccor"); // NOI18N
        ChkAccor.setPreferredSize(new java.awt.Dimension(15, 20));
        ChkAccor.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kanan.png"))); // NOI18N
        ChkAccor.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kiri.png"))); // NOI18N
        ChkAccor.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kiri.png"))); // NOI18N
        ChkAccor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkAccorActionPerformed(evt);
            }
        });
        PanelAccor.add(ChkAccor, java.awt.BorderLayout.EAST);

        ScrollMenu.setBorder(null);
        ScrollMenu.setName("ScrollMenu"); // NOI18N
        ScrollMenu.setOpaque(true);
        ScrollMenu.setPreferredSize(new java.awt.Dimension(255, 1197));

        FormMenu.setBackground(new java.awt.Color(255, 255, 255));
        FormMenu.setBorder(null);
        FormMenu.setName("FormMenu"); // NOI18N
        FormMenu.setPreferredSize(new java.awt.Dimension(255, 1817));
        FormMenu.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 1));

        chkSemua.setBackground(new java.awt.Color(255, 204, 255));
        chkSemua.setSelected(true);
        chkSemua.setText("Semua");
        chkSemua.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkSemua.setName("chkSemua"); // NOI18N
        chkSemua.setPreferredSize(new java.awt.Dimension(245, 22));
        chkSemua.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkSemuaItemStateChanged(evt);
            }
        });
        FormMenu.add(chkSemua);

        chkSEPRajal.setSelected(true);
        chkSEPRajal.setText("SEP Rawat Jalan");
        chkSEPRajal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkSEPRajal.setName("chkSEPRajal"); // NOI18N
        chkSEPRajal.setOpaque(false);
        chkSEPRajal.setPreferredSize(new java.awt.Dimension(245, 22));
        chkSEPRajal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSEPRajalActionPerformed(evt);
            }
        });
        FormMenu.add(chkSEPRajal);

        chkSBPK.setSelected(true);
        chkSBPK.setText("SBPK");
        chkSBPK.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkSBPK.setName("chkSBPK"); // NOI18N
        chkSBPK.setOpaque(false);
        chkSBPK.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkSBPK);

        chkSBPKIGD.setSelected(true);
        chkSBPKIGD.setText("SBPK IGD");
        chkSBPKIGD.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkSBPKIGD.setName("chkSBPKIGD"); // NOI18N
        chkSBPKIGD.setOpaque(false);
        chkSBPKIGD.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkSBPKIGD);

        chkFormRehabMedis.setSelected(true);
        chkFormRehabMedis.setText("Formulir Rehabilitasi Medis");
        chkFormRehabMedis.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkFormRehabMedis.setName("chkFormRehabMedis"); // NOI18N
        chkFormRehabMedis.setOpaque(false);
        chkFormRehabMedis.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkFormRehabMedis);

        chkProgramTerapi.setSelected(true);
        chkProgramTerapi.setText("Program Terapi");
        chkProgramTerapi.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkProgramTerapi.setName("chkProgramTerapi"); // NOI18N
        chkProgramTerapi.setOpaque(false);
        chkProgramTerapi.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkProgramTerapi);

        chkPemeriksaanLaborat.setSelected(true);
        chkPemeriksaanLaborat.setText("Pemeriksaan Laborat");
        chkPemeriksaanLaborat.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkPemeriksaanLaborat.setName("chkPemeriksaanLaborat"); // NOI18N
        chkPemeriksaanLaborat.setOpaque(false);
        chkPemeriksaanLaborat.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkPemeriksaanLaborat);

        chkPemeriksaanRadiologi.setSelected(true);
        chkPemeriksaanRadiologi.setText("Pemeriksaan Radiologi");
        chkPemeriksaanRadiologi.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkPemeriksaanRadiologi.setName("chkPemeriksaanRadiologi"); // NOI18N
        chkPemeriksaanRadiologi.setOpaque(false);
        chkPemeriksaanRadiologi.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkPemeriksaanRadiologi);

        chkBilling.setSelected(true);
        chkBilling.setText("Billing");
        chkBilling.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkBilling.setName("chkBilling"); // NOI18N
        chkBilling.setOpaque(false);
        chkBilling.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkBilling);

        ScrollMenu.setViewportView(FormMenu);

        PanelAccor.add(ScrollMenu, java.awt.BorderLayout.CENTER);

        internalFrame2.add(PanelAccor, java.awt.BorderLayout.WEST);

        TabRawat.addTab("Berkas Klaim Rawat Jalan", internalFrame2);

        internalFrame3.setBackground(new java.awt.Color(235, 255, 235));
        internalFrame3.setBorder(null);
        internalFrame3.setName("internalFrame3"); // NOI18N
        internalFrame3.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        Scroll2.setName("Scroll2"); // NOI18N
        Scroll2.setOpaque(true);

        LoadHTMLRiwayatPerawatanRanap.setBorder(null);
        LoadHTMLRiwayatPerawatanRanap.setName("LoadHTMLRiwayatPerawatanRanap"); // NOI18N
        Scroll2.setViewportView(LoadHTMLRiwayatPerawatanRanap);

        internalFrame3.add(Scroll2, java.awt.BorderLayout.CENTER);

        PanelAccor1.setBackground(new java.awt.Color(255, 255, 255));
        PanelAccor1.setName("PanelAccor1"); // NOI18N
        PanelAccor1.setPreferredSize(new java.awt.Dimension(275, 43));
        PanelAccor1.setLayout(new java.awt.BorderLayout());

        ChkAccor1.setBackground(new java.awt.Color(255, 250, 248));
        ChkAccor1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(250, 255, 248)));
        ChkAccor1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kanan.png"))); // NOI18N
        ChkAccor1.setFocusable(false);
        ChkAccor1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkAccor1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChkAccor1.setName("ChkAccor1"); // NOI18N
        ChkAccor1.setPreferredSize(new java.awt.Dimension(15, 20));
        ChkAccor1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kanan.png"))); // NOI18N
        ChkAccor1.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kiri.png"))); // NOI18N
        ChkAccor1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kiri.png"))); // NOI18N
        ChkAccor1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkAccor1ActionPerformed(evt);
            }
        });
        PanelAccor1.add(ChkAccor1, java.awt.BorderLayout.EAST);

        ScrollMenu1.setBorder(null);
        ScrollMenu1.setName("ScrollMenu1"); // NOI18N
        ScrollMenu1.setOpaque(true);
        ScrollMenu1.setPreferredSize(new java.awt.Dimension(255, 1197));

        FormMenu1.setBackground(new java.awt.Color(255, 255, 255));
        FormMenu1.setBorder(null);
        FormMenu1.setName("FormMenu1"); // NOI18N
        FormMenu1.setPreferredSize(new java.awt.Dimension(255, 1817));
        FormMenu1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 1));

        chkSemuaRanap.setBackground(new java.awt.Color(255, 204, 255));
        chkSemuaRanap.setSelected(true);
        chkSemuaRanap.setText("Semua");
        chkSemuaRanap.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkSemuaRanap.setName("chkSemuaRanap"); // NOI18N
        chkSemuaRanap.setPreferredSize(new java.awt.Dimension(245, 22));
        chkSemuaRanap.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkSemuaRanapItemStateChanged(evt);
            }
        });
        FormMenu1.add(chkSemuaRanap);

        chkSEPRanap.setSelected(true);
        chkSEPRanap.setText("SEP Rawat Inap");
        chkSEPRanap.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkSEPRanap.setName("chkSEPRanap"); // NOI18N
        chkSEPRanap.setOpaque(false);
        chkSEPRanap.setPreferredSize(new java.awt.Dimension(245, 22));
        chkSEPRanap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSEPRanapActionPerformed(evt);
            }
        });
        FormMenu1.add(chkSEPRanap);

        chkSupri.setSelected(true);
        chkSupri.setText("Surat Perintah Inap");
        chkSupri.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkSupri.setName("chkSupri"); // NOI18N
        chkSupri.setOpaque(false);
        chkSupri.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu1.add(chkSupri);

        chkPemeriksaanLaboratRanap.setSelected(true);
        chkPemeriksaanLaboratRanap.setText("Pemeriksaan Laborat");
        chkPemeriksaanLaboratRanap.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkPemeriksaanLaboratRanap.setName("chkPemeriksaanLaboratRanap"); // NOI18N
        chkPemeriksaanLaboratRanap.setOpaque(false);
        chkPemeriksaanLaboratRanap.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu1.add(chkPemeriksaanLaboratRanap);

        chkPemeriksaanRadiologiRanap.setSelected(true);
        chkPemeriksaanRadiologiRanap.setText("Pemeriksaan Radiologi");
        chkPemeriksaanRadiologiRanap.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkPemeriksaanRadiologiRanap.setName("chkPemeriksaanRadiologiRanap"); // NOI18N
        chkPemeriksaanRadiologiRanap.setOpaque(false);
        chkPemeriksaanRadiologiRanap.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu1.add(chkPemeriksaanRadiologiRanap);

        chkResume.setSelected(true);
        chkResume.setText("Resume");
        chkResume.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkResume.setName("chkResume"); // NOI18N
        chkResume.setOpaque(false);
        chkResume.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu1.add(chkResume);

        chkBillingRanap.setSelected(true);
        chkBillingRanap.setText("Billing");
        chkBillingRanap.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkBillingRanap.setName("chkBillingRanap"); // NOI18N
        chkBillingRanap.setOpaque(false);
        chkBillingRanap.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu1.add(chkBillingRanap);

        ScrollMenu1.setViewportView(FormMenu1);

        PanelAccor1.add(ScrollMenu1, java.awt.BorderLayout.CENTER);

        internalFrame3.add(PanelAccor1, java.awt.BorderLayout.WEST);

        TabRawat.addTab("Berkas Klaim Rawat Inap", internalFrame3);

        internalFrame1.add(TabRawat, java.awt.BorderLayout.CENTER);

        PanelInput.setBackground(new java.awt.Color(255, 255, 255));
        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setLayout(new java.awt.BorderLayout(1, 1));

        ChkInput.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setMnemonic('M');
        ChkInput.setSelected(true);
        ChkInput.setText(".: Tampilkan/Sembunyikan Data Pasien");
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
        FormInput.setPreferredSize(new java.awt.Dimension(100, 104));
        FormInput.setLayout(null);

        label17.setText("Pasien :");
        label17.setName("label17"); // NOI18N
        label17.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label17);
        label17.setBounds(5, 10, 55, 23);

        NoRM.setName("NoRM"); // NOI18N
        NoRM.setPreferredSize(new java.awt.Dimension(100, 23));
        NoRM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NoRMKeyPressed(evt);
            }
        });
        FormInput.add(NoRM);
        NoRM.setBounds(64, 10, 100, 23);

        NmPasien.setEditable(false);
        NmPasien.setName("NmPasien"); // NOI18N
        NmPasien.setPreferredSize(new java.awt.Dimension(220, 23));
        FormInput.add(NmPasien);
        NmPasien.setBounds(167, 10, 220, 23);

        BtnPasien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnPasien.setMnemonic('3');
        BtnPasien.setToolTipText("Alt+3");
        BtnPasien.setName("BtnPasien"); // NOI18N
        BtnPasien.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPasienActionPerformed(evt);
            }
        });
        BtnPasien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPasienKeyPressed(evt);
            }
        });
        FormInput.add(BtnPasien);
        BtnPasien.setBounds(390, 10, 28, 23);

        label20.setText("J.K. :");
        label20.setName("label20"); // NOI18N
        label20.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label20);
        label20.setBounds(436, 10, 30, 23);

        Jk.setEditable(false);
        Jk.setName("Jk"); // NOI18N
        Jk.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(Jk);
        Jk.setBounds(470, 10, 40, 23);

        label21.setText("Tempat & Tgl.Lahir :");
        label21.setName("label21"); // NOI18N
        label21.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label21);
        label21.setBounds(523, 10, 110, 23);

        TempatLahir.setEditable(false);
        TempatLahir.setName("TempatLahir"); // NOI18N
        TempatLahir.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(TempatLahir);
        TempatLahir.setBounds(637, 10, 140, 23);

        label22.setText("Alamat :");
        label22.setName("label22"); // NOI18N
        label22.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label22);
        label22.setBounds(5, 40, 55, 23);

        Alamat.setEditable(false);
        Alamat.setName("Alamat"); // NOI18N
        Alamat.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(Alamat);
        Alamat.setBounds(64, 40, 354, 23);

        label23.setText("G.D. :");
        label23.setName("label23"); // NOI18N
        label23.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label23);
        label23.setBounds(436, 40, 30, 23);

        GD.setEditable(false);
        GD.setName("GD"); // NOI18N
        GD.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(GD);
        GD.setBounds(470, 40, 40, 23);

        label24.setText("Nama Ibu Kandung :");
        label24.setName("label24"); // NOI18N
        label24.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label24);
        label24.setBounds(523, 40, 110, 23);

        IbuKandung.setEditable(false);
        IbuKandung.setName("IbuKandung"); // NOI18N
        IbuKandung.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(IbuKandung);
        IbuKandung.setBounds(637, 40, 225, 23);

        TanggalLahir.setEditable(false);
        TanggalLahir.setName("TanggalLahir"); // NOI18N
        TanggalLahir.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(TanggalLahir);
        TanggalLahir.setBounds(779, 10, 83, 23);

        label25.setText("Agama :");
        label25.setName("label25"); // NOI18N
        label25.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label25);
        label25.setBounds(5, 70, 55, 23);

        Agama.setEditable(false);
        Agama.setName("Agama"); // NOI18N
        Agama.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(Agama);
        Agama.setBounds(64, 70, 100, 23);

        StatusNikah.setEditable(false);
        StatusNikah.setName("StatusNikah"); // NOI18N
        StatusNikah.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(StatusNikah);
        StatusNikah.setBounds(245, 70, 100, 23);

        label26.setText("Stts.Nikah :");
        label26.setName("label26"); // NOI18N
        label26.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label26);
        label26.setBounds(176, 70, 65, 23);

        Pendidikan.setEditable(false);
        Pendidikan.setName("Pendidikan"); // NOI18N
        Pendidikan.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(Pendidikan);
        Pendidikan.setBounds(429, 70, 80, 23);

        label27.setText("Pendidikan :");
        label27.setName("label27"); // NOI18N
        label27.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label27);
        label27.setBounds(355, 70, 70, 23);

        label28.setText("Bahasa :");
        label28.setName("label28"); // NOI18N
        label28.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label28);
        label28.setBounds(520, 70, 50, 23);

        Bahasa.setEditable(false);
        Bahasa.setName("Bahasa"); // NOI18N
        Bahasa.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(Bahasa);
        Bahasa.setBounds(574, 70, 100, 23);

        label29.setText("Cacat Fisik :");
        label29.setName("label29"); // NOI18N
        label29.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label29);
        label29.setBounds(683, 70, 70, 23);

        CacatFisik.setEditable(false);
        CacatFisik.setName("CacatFisik"); // NOI18N
        CacatFisik.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(CacatFisik);
        CacatFisik.setBounds(757, 70, 105, 23);

        PanelInput.add(FormInput, java.awt.BorderLayout.CENTER);

        internalFrame1.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);
        internalFrame1.getAccessibleContext().setAccessibleName("::[ Data Berkas Klaim Pasien ]::");
        internalFrame1.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            dispose();
        }else{Valid.pindah(evt,Tgl1,NoRM);}
}//GEN-LAST:event_BtnKeluarKeyPressed

private void NoRMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NoRMKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isPasien();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            isPasien();
            BtnKeluar.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            BtnPasienActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            isPasien();
            BtnPrint.requestFocus();
        }
}//GEN-LAST:event_NoRMKeyPressed

private void BtnPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPasienActionPerformed
    if(akses.getpasien()==true){
        pasien.isCek();
        pasien.emptTeks();
        pasien.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        pasien.setLocationRelativeTo(internalFrame1);
        pasien.setVisible(true);
    }   
}//GEN-LAST:event_BtnPasienActionPerformed

private void BtnPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPasienKeyPressed
    //Valid.pindah(evt,Tgl2,TKd);
}//GEN-LAST:event_BtnPasienKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        if(NoRM.getText().trim().equals("")||NmPasien.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Pasien masih kosong...!!!");
        }else{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            switch (TabRawat.getSelectedIndex()) {
                case 0:
                    if(tabModeRegistrasi.getRowCount()==0){
                        JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
                    }else if(tabModeRegistrasi.getRowCount()!=0){
                        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));        
                        Sequel.queryu("truncate table temporary_resume");

                        for(int i=0;i<tabModeRegistrasi.getRowCount();i++){  
                            Sequel.menyimpan("temporary_resume","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?",38,new String[]{
                                "0",tabModeRegistrasi.getValueAt(i,0).toString(),tabModeRegistrasi.getValueAt(i,1).toString(),tabModeRegistrasi.getValueAt(i,2).toString(),
                                tabModeRegistrasi.getValueAt(i,3).toString(),tabModeRegistrasi.getValueAt(i,4).toString(),tabModeRegistrasi.getValueAt(i,5).toString(),
                                tabModeRegistrasi.getValueAt(i,6).toString(),tabModeRegistrasi.getValueAt(i,7).toString(),tabModeRegistrasi.getValueAt(i,8).toString(),
                                "","","","","","","","","","","","","","","","","","","","","","","","","","","","",""
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
                        Valid.MyReport2("rptRiwayatRegistrasi.jasper","report","::[ Riwayat Registrasi ]::",param);
                        this.setCursor(Cursor.getDefaultCursor());
                    }
                    break;
                case 1:
                    panggilLaporan(LoadHTMLRiwayatPerawatan.getText()); 
                    break;
                case 2:
                    panggilLaporan(LoadHTMLRiwayatPerawatanRanap.getText()); 
                    break;
                default:
                    break;
            }
            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_BtnPrintActionPerformed

    private void Tgl1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Tgl1KeyPressed
        Valid.pindah(evt, BtnKeluar, Tgl2);
    }//GEN-LAST:event_Tgl1KeyPressed

    private void Tgl2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Tgl2KeyPressed
        Valid.pindah(evt, Tgl1,NoRM);
    }//GEN-LAST:event_Tgl2KeyPressed

    private void BtnCari1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCari1ActionPerformed
        if(NoRM.getText().trim().equals("")||NmPasien.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Pasien masih kosong...!!!");
        }else{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            switch (TabRawat.getSelectedIndex()) {
                case 0:
                    tampilKunjungan();
                    break;
                case 1:
                    tampilPerawatan();
                    break;
                case 2:
                    tampilPerawatanRanap();
                    break;
                default:
                    break;
            }
            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_BtnCari1ActionPerformed

    private void TabRawatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawatMouseClicked
        ChkAccor.setVisible(false);
        PanelAccor.setPreferredSize(new Dimension(225,HEIGHT));
        FormMenu.setVisible(true); 
        ChkAccor.setVisible(true);
        BtnCari1ActionPerformed(null);
        
    }//GEN-LAST:event_TabRawatMouseClicked

    private void ChkAccorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkAccorActionPerformed
        isMenu();
    }//GEN-LAST:event_ChkAccorActionPerformed

    private void chkSemuaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkSemuaItemStateChanged
        if(chkSemua.isSelected()==true){
//            chkKlaimRajal.setSelected(true);
//            chkKlaimRanap.setSelected(true);
            chkSEPRajal.setSelected(true);

            chkSBPK.setSelected(true);
            chkSBPKIGD.setSelected(true);
            chkFormRehabMedis.setSelected(true);
            chkProgramTerapi.setSelected(true);
            chkPemeriksaanRadiologi.setSelected(true);
            chkPemeriksaanLaborat.setSelected(true);
            chkResume.setSelected(true);
            chkBilling.setSelected(true);
            
        }else{
            chkSEPRajal.setSelected(false);

            chkSBPK.setSelected(false);
            chkSBPKIGD.setSelected(false);
            chkFormRehabMedis.setSelected(false);
            chkProgramTerapi.setSelected(false);
            chkPemeriksaanRadiologi.setSelected(false);
            chkPemeriksaanLaborat.setSelected(false);
            chkResume.setSelected(false);
            chkBilling.setSelected(false);

        }
    }//GEN-LAST:event_chkSemuaItemStateChanged

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

    private void NoRawatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NoRawatKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCari1ActionPerformed(null);
        }
    }//GEN-LAST:event_NoRawatKeyPressed

    private void chkSEPRajalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSEPRajalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkSEPRajalActionPerformed

    private void ChkAccor1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkAccor1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ChkAccor1ActionPerformed

    private void chkSemuaRanapItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkSemuaRanapItemStateChanged
         if(chkSemuaRanap.isSelected()==true){
            chkSEPRanap.setSelected(true);
            chkPemeriksaanRadiologiRanap.setSelected(true);
            chkPemeriksaanLaboratRanap.setSelected(true);
            chkResume.setSelected(true);
            chkBillingRanap.setSelected(true);
            
        }else{
            chkSEPRanap.setSelected(false);
            chkPemeriksaanRadiologiRanap.setSelected(false);
            chkPemeriksaanLaboratRanap.setSelected(false);
            chkResume.setSelected(false);
            chkBillingRanap.setSelected(false);

        }
    }//GEN-LAST:event_chkSemuaRanapItemStateChanged

    private void chkSEPRanapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSEPRanapActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkSEPRanapActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMBerkasKlaim dialog = new RMBerkasKlaim(new javax.swing.JFrame(), true);
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
    private widget.TextBox Agama;
    private widget.TextBox Alamat;
    private widget.TextBox Bahasa;
    private widget.Button BtnCari1;
    private widget.Button BtnKeluar;
    private widget.Button BtnPasien;
    private widget.Button BtnPrint;
    private widget.TextBox CacatFisik;
    private widget.CekBox ChkAccor;
    private widget.CekBox ChkAccor1;
    private widget.CekBox ChkInput;
    private widget.panelisi FormInput;
    private widget.PanelBiasa FormMenu;
    private widget.PanelBiasa FormMenu1;
    private widget.TextBox GD;
    private widget.TextBox IbuKandung;
    private widget.TextBox Jk;
    private widget.editorpane LoadHTMLRiwayatPerawatan;
    private widget.editorpane LoadHTMLRiwayatPerawatanRanap;
    private widget.TextBox NmPasien;
    private widget.TextBox NoRM;
    private widget.TextBox NoRawat;
    private widget.PanelBiasa PanelAccor;
    private widget.PanelBiasa PanelAccor1;
    private javax.swing.JPanel PanelInput;
    private widget.TextBox Pekerjaan;
    private widget.TextBox Pendidikan;
    private widget.RadioButton R1;
    private widget.RadioButton R2;
    private widget.RadioButton R3;
    private widget.RadioButton R4;
    private widget.ScrollPane Scroll;
    private widget.ScrollPane Scroll1;
    private widget.ScrollPane Scroll2;
    private widget.ScrollPane ScrollMenu;
    private widget.ScrollPane ScrollMenu1;
    private widget.TextBox StatusNikah;
    private javax.swing.JTabbedPane TabRawat;
    private widget.TextBox TanggalLahir;
    private widget.TextBox TempatLahir;
    private widget.Tanggal Tgl1;
    private widget.Tanggal Tgl2;
    private javax.swing.ButtonGroup buttonGroup1;
    private widget.CekBox chkBilling;
    private widget.CekBox chkBillingRanap;
    private widget.CekBox chkFormRehabMedis;
    private widget.CekBox chkPemeriksaanLaborat;
    private widget.CekBox chkPemeriksaanLaboratRanap;
    private widget.CekBox chkPemeriksaanRadiologi;
    private widget.CekBox chkPemeriksaanRadiologiRanap;
    private widget.CekBox chkProgramTerapi;
    private widget.CekBox chkResume;
    private widget.CekBox chkSBPK;
    private widget.CekBox chkSBPKIGD;
    private widget.CekBox chkSEPRajal;
    private widget.CekBox chkSEPRanap;
    private widget.CekBox chkSemua;
    private widget.CekBox chkSemuaRanap;
    private widget.CekBox chkSupri;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame2;
    private widget.InternalFrame internalFrame3;
    private widget.Label label17;
    private widget.Label label18;
    private widget.Label label19;
    private widget.Label label20;
    private widget.Label label21;
    private widget.Label label22;
    private widget.Label label23;
    private widget.Label label24;
    private widget.Label label25;
    private widget.Label label26;
    private widget.Label label27;
    private widget.Label label28;
    private widget.Label label29;
    private widget.panelisi panelGlass5;
    private widget.Table tbRegistrasi;
    // End of variables declaration//GEN-END:variables

    public void setNoRm(String norm,String nama,String norawat) {
        NoRM.setText(norm);
        NmPasien.setText(nama);
        NoRawat.setText(norawat);
        R4.setSelected(true);
        isPasien();
        BtnCari1ActionPerformed(null);
    }

    private void isPasien() {
        try{
            ps=koneksi.prepareStatement(
                    "select pasien.no_rkm_medis,pasien.nm_pasien,pasien.jk,pasien.tmp_lahir,pasien.tgl_lahir,pasien.agama,"+
                    "bahasa_pasien.nama_bahasa,cacat_fisik.nama_cacat,pasien.gol_darah,pasien.nm_ibu,pasien.stts_nikah,pasien.pnd, "+
                    "concat(pasien.alamat,', ',kelurahan.nm_kel,', ',kecamatan.nm_kec,', ',kabupaten.nm_kab) as alamat,pasien.pekerjaan "+
                    "from pasien inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "+
                    "inner join cacat_fisik on cacat_fisik.id=pasien.cacat_fisik "+
                    "inner join kelurahan on pasien.kd_kel=kelurahan.kd_kel "+
                    "inner join kecamatan on pasien.kd_kec=kecamatan.kd_kec "+
                    "inner join kabupaten on pasien.kd_kab=kabupaten.kd_kab "+
                    "where pasien.no_rkm_medis=?");
            try {
                ps.setString(1,NoRM.getText().trim());
                rs=ps.executeQuery();
                if(rs.next()){
                    NoRM.setText(rs.getString("no_rkm_medis"));
                    NmPasien.setText(rs.getString("nm_pasien"));
                    Jk.setText(rs.getString("jk"));
                    TempatLahir.setText(rs.getString("tmp_lahir"));
                    TanggalLahir.setText(rs.getString("tgl_lahir"));
                    Alamat.setText(rs.getString("alamat"));
                    GD.setText(rs.getString("gol_darah"));
                    IbuKandung.setText(rs.getString("nm_ibu"));
                    Agama.setText(rs.getString("agama"));
                    StatusNikah.setText(rs.getString("stts_nikah"));
                    Pendidikan.setText(rs.getString("pnd"));
                    Bahasa.setText(rs.getString("nama_bahasa"));
                    CacatFisik.setText(rs.getString("nama_cacat"));
                    Pekerjaan.setText(rs.getString("pekerjaan"));
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
    }
    
    private void tampilKunjungan() {
        Valid.tabelKosong(tabModeRegistrasi);
        try{   
            if(R1.isSelected()==true){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.status_lanjut,"+
                    "reg_periksa.kd_dokter,dokter.nm_dokter,reg_periksa.umurdaftar,reg_periksa.sttsumur,"+
                    "poliklinik.kd_poli,poliklinik.nm_poli,penjab.png_jawab from reg_periksa inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter "+
                    "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli inner join penjab on reg_periksa.kd_pj=penjab.kd_pj  "+
                    "where reg_periksa.stts<>'Batal' and reg_periksa.no_rkm_medis=? order by reg_periksa.tgl_registrasi desc limit 3");
            }else if(R2.isSelected()==true){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.status_lanjut,"+
                    "reg_periksa.kd_dokter,dokter.nm_dokter,reg_periksa.umurdaftar,reg_periksa.sttsumur,"+
                    "poliklinik.kd_poli,poliklinik.nm_poli,penjab.png_jawab from reg_periksa inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter "+
                    "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli inner join penjab  on reg_periksa.kd_pj=penjab.kd_pj  "+
                    "where reg_periksa.stts<>'Batal' and reg_periksa.no_rkm_medis=? order by reg_periksa.tgl_registrasi");
            }else if(R3.isSelected()==true){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.status_lanjut,"+
                    "reg_periksa.kd_dokter,dokter.nm_dokter,reg_periksa.umurdaftar,reg_periksa.sttsumur,"+
                    "poliklinik.kd_poli,poliklinik.nm_poli,penjab.png_jawab from reg_periksa inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter "+
                    "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli inner join penjab on reg_periksa.kd_pj=penjab.kd_pj  "+
                    "where reg_periksa.stts<>'Batal' and reg_periksa.no_rkm_medis=? and "+
                    "reg_periksa.tgl_registrasi between ? and ? order by reg_periksa.tgl_registrasi");
            }else if(R4.isSelected()==true){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.status_lanjut,"+
                    "reg_periksa.kd_dokter,dokter.nm_dokter,reg_periksa.umurdaftar,reg_periksa.sttsumur,"+
                    "poliklinik.kd_poli,poliklinik.nm_poli,penjab.png_jawab from reg_periksa inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter "+
                    "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli inner join penjab on reg_periksa.kd_pj=penjab.kd_pj  "+
                    "where reg_periksa.stts<>'Batal' and reg_periksa.no_rkm_medis=? and reg_periksa.no_rawat=?");
            }
            
            try {
                i=0;
                if(R1.isSelected()==true){
                    ps.setString(1,NoRM.getText().trim());
                }else if(R2.isSelected()==true){
                    ps.setString(1,NoRM.getText().trim());
                }else if(R3.isSelected()==true){
                    ps.setString(1,NoRM.getText().trim());
                    ps.setString(2,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                    ps.setString(3,Valid.SetTgl(Tgl2.getSelectedItem()+""));
                }else if(R4.isSelected()==true){
                    ps.setString(1,NoRM.getText().trim());
                    ps.setString(2,NoRawat.getText().trim());
                }                     
                rs=ps.executeQuery();
                while(rs.next()){
                    i++;
                    tabModeRegistrasi.addRow(new String[]{
                        i+"",rs.getString("no_rawat"),rs.getString("tgl_registrasi"),rs.getString("jam_reg"),
                        rs.getString("kd_dokter"),rs.getString("nm_dokter"),rs.getString("umurdaftar")+" "+rs.getString("sttsumur"),
                        rs.getString("kd_poli")+" "+rs.getString("nm_poli"),rs.getString("png_jawab")
                    });
                    ps2=koneksi.prepareStatement(
                            "select rujukan_internal_poli.kd_dokter,dokter.nm_dokter,"+
                            "rujukan_internal_poli.kd_poli,poliklinik.nm_poli from rujukan_internal_poli "+
                            "inner join dokter inner join poliklinik on rujukan_internal_poli.kd_dokter=dokter.kd_dokter "+
                            "and rujukan_internal_poli.kd_poli=poliklinik.kd_poli where rujukan_internal_poli.no_rawat=?");
                    try {
                        ps2.setString(1,rs.getString("no_rawat"));
                        rs2=ps2.executeQuery();
                        while(rs2.next()){                            
                            tabModeRegistrasi.addRow(new String[]{
                                "",rs.getString("no_rawat"),rs.getString("tgl_registrasi"),"",
                                rs2.getString("kd_dokter"),rs2.getString("nm_dokter"),rs.getString("umurdaftar")+" "+rs.getString("sttsumur"),
                                rs2.getString("kd_poli")+" "+rs2.getString("nm_poli"),rs.getString("png_jawab")
                            });
                        }
                    } catch (Exception e) {
                        System.out.println("Notifikasi 3 : "+e);
                    } finally{
                        if(rs2!=null){
                            rs2.close();
                        }
                        if(ps2!=null){
                            ps2.close();
                        }
                    }  
                    kddpjp="";
                    dpjp="";
                    if(rs.getString("status_lanjut").equals("Ranap")){
                        kddpjp=Sequel.cariIsi("select dpjp_ranap.kd_dokter from dpjp_ranap where dpjp_ranap.no_rawat=?",rs.getString("no_rawat"));
                        if(!kddpjp.equals("")){
                            dpjp=Sequel.cariIsi("select dokter.nm_dokter from dokter where dokter.kd_dokter=?",kddpjp);
                        }else{
                            kddpjp=rs.getString("kd_dokter");
                            dpjp=rs.getString("nm_dokter");
                        }
                    }                        
                    ps2=koneksi.prepareStatement(
                            "select kamar_inap.tgl_masuk,kamar_inap.jam_masuk,kamar_inap.kd_kamar,bangsal.nm_bangsal "+
                            "from kamar_inap inner join kamar on kamar_inap.kd_kamar=kamar.kd_kamar inner join bangsal  "+
                            "on kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=?");
                    try {
                        ps2.setString(1,rs.getString("no_rawat"));
                        rs2=ps2.executeQuery();
                        while(rs2.next()){                            
                            tabModeRegistrasi.addRow(new String[]{
                                "",rs.getString("no_rawat"),rs2.getString("tgl_masuk"),rs2.getString("jam_masuk"),
                                kddpjp,dpjp,rs.getString("umurdaftar")+" "+rs.getString("sttsumur"),
                                rs2.getString("kd_kamar")+" "+rs2.getString("nm_bangsal"),rs.getString("png_jawab")
                            });
                        }
                    } catch (Exception e) {
                        System.out.println("Notifikasi 2 : "+e);
                    } finally{
                        if(rs2!=null){
                            rs2.close();
                        }
                        if(ps2!=null){
                            ps2.close();
                        }
                    } 
                }
            } catch (Exception e) {
                System.out.println("Notifikasi 1 : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }                
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
    }
    
    private void isMenu(){
        if(ChkAccor.isSelected()==true){
            ChkAccor.setVisible(false);
            PanelAccor.setPreferredSize(new Dimension(225,HEIGHT));
            FormMenu.setVisible(true); 
            ChkAccor.setVisible(true);
        }else if(ChkAccor.isSelected()==false){  
            ChkAccor.setVisible(false);
            PanelAccor.setPreferredSize(new Dimension(15,HEIGHT));
            FormMenu.setVisible(false);    
            ChkAccor.setVisible(true);
        }
    }

    private void tampilPerawatan() {
        try{   
            htmlContent = new StringBuilder();
            if(R1.isSelected()==true){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_reg,reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.jam_reg,"+
                    "reg_periksa.kd_dokter,dokter.nm_dokter,poliklinik.nm_poli,reg_periksa.p_jawab,reg_periksa.almt_pj,"+
                    "reg_periksa.hubunganpj,reg_periksa.biaya_reg,reg_periksa.status_lanjut,penjab.png_jawab "+
                    "from reg_periksa inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter "+
                    "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                    "where reg_periksa.stts<>'Batal' and reg_periksa.no_rkm_medis=? order by reg_periksa.tgl_registrasi desc limit 3");
            }else if(R2.isSelected()==true){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_reg,reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.jam_reg,"+
                    "reg_periksa.kd_dokter,dokter.nm_dokter,poliklinik.nm_poli,reg_periksa.p_jawab,reg_periksa.almt_pj,"+
                    "reg_periksa.hubunganpj,reg_periksa.biaya_reg,reg_periksa.status_lanjut,penjab.png_jawab "+
                    "from reg_periksa inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter "+
                    "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                    "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                    "where reg_periksa.stts<>'Batal' and reg_periksa.no_rkm_medis=? order by reg_periksa.tgl_registrasi");
            }else if(R3.isSelected()==true){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_reg,reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.jam_reg,"+
                    "reg_periksa.kd_dokter,dokter.nm_dokter,poliklinik.nm_poli,reg_periksa.p_jawab,reg_periksa.almt_pj,"+
                    "reg_periksa.hubunganpj,reg_periksa.biaya_reg,reg_periksa.status_lanjut,penjab.png_jawab "+
                    "from reg_periksa inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter "+
                    "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                    "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                    "where reg_periksa.stts<>'Batal' and reg_periksa.no_rkm_medis=? and "+
                    "reg_periksa.tgl_registrasi between ? and ? order by reg_periksa.tgl_registrasi");
            }else if(R4.isSelected()==true){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_reg,reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.jam_reg,"+
                    "reg_periksa.kd_dokter,dokter.nm_dokter,poliklinik.nm_poli,reg_periksa.p_jawab,reg_periksa.almt_pj,"+
                    "reg_periksa.hubunganpj,reg_periksa.biaya_reg,reg_periksa.status_lanjut,penjab.png_jawab "+
                    "from reg_periksa inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter "+
                    "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                    "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                    "where reg_periksa.stts<>'Batal' and reg_periksa.no_rkm_medis=? and reg_periksa.no_rawat=?");
            }
            
            try {
                i=0;
                if(R1.isSelected()==true){
                    ps.setString(1,NoRM.getText().trim());
                }else if(R2.isSelected()==true){
                    ps.setString(1,NoRM.getText().trim());
                }else if(R3.isSelected()==true){
                    ps.setString(1,NoRM.getText().trim());
                    ps.setString(2,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                    ps.setString(3,Valid.SetTgl(Tgl2.getSelectedItem()+""));
                }else if(R4.isSelected()==true){
                    ps.setString(1,NoRM.getText().trim());
                    ps.setString(2,NoRawat.getText().trim());
                }            
                urut=1;
                rs=ps.executeQuery();
                while(rs.next()){
                    try {
                        dokterrujukan="";
                        polirujukan="";
                        rs2=koneksi.prepareStatement(
                            "select poliklinik.nm_poli,dokter.nm_dokter from rujukan_internal_poli "+
                            "inner join poliklinik on rujukan_internal_poli.kd_poli=poliklinik.kd_poli "+
                            "inner join dokter on rujukan_internal_poli.kd_dokter=dokter.kd_dokter "+
                            "where no_rawat='"+rs.getString("no_rawat")+"'").executeQuery();
                        while(rs2.next()){
                            polirujukan=polirujukan+", "+rs2.getString("nm_poli");
                            dokterrujukan=dokterrujukan+", "+rs2.getString("nm_dokter");
                        }
                    } catch (Exception e) {
                        System.out.println("Notif : "+e);
                    } finally{
                        if(rs2!=null){
                            rs2.close();
                        }
                    }   
                    
                    //menampilkan SEP Rajal
                    menampilkanSEPRajal(rs.getString("no_rawat"));
                    
                    //menampilkan SBPK
                    menampilkanSBPK(rs.getString("no_rawat"));
                    
                     //menampilkan SBPK IGD Ralan
                    menampilkanSBPKIGD(rs.getString("no_rawat"));
                                     
                    //menampilkan Form Rehab Medis
                    menampilkanFormRehabMedis(rs.getString("no_rawat"));
                    
                    //menampilkan Program Terapi
                    menampilkanProgramTerapi(rs.getString("no_rawat"));
                    
                    //menampilkan Billing
                    menampilkanBilling(rs.getString("no_rawat"));
                    
                    //menampilkan Radiologi
                    menampilkanRadiologi(rs.getString("no_rawat"));

                    //menampilkan Lab
                    menampilkanLaboratorium(rs.getString("no_rawat"));
                    

    
                    //menampilkan berkas digital
//                    menampilkanBerkasDigital(rs.getString("no_rawat"));
                    
                    biayaperawatan=rs.getDouble("biaya_reg");
                    //biaya administrasi
                    htmlContent.append(

                             "</table>"
                    );
                 
                    htmlContent.append(
                        "<tr class='isi'><td></td><td colspan='3' align='right'>&nbsp;</tr>"
                    );
                    
                }
                
                LoadHTMLRiwayatPerawatan.setText(
                    "<html>"+
                      "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                       htmlContent.toString()+
                      "</table>"+
                    "</html>");
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }                
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
    }
    
    private void tampilPerawatanRanap() {
        try{   
            htmlContent = new StringBuilder();
            if(R1.isSelected()==true){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_reg,reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.jam_reg,"+
                    "reg_periksa.kd_dokter,dokter.nm_dokter,poliklinik.nm_poli,reg_periksa.p_jawab,reg_periksa.almt_pj,"+
                    "reg_periksa.hubunganpj,reg_periksa.biaya_reg,reg_periksa.status_lanjut,penjab.png_jawab "+
                    "from reg_periksa inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter "+
                    "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                    "where reg_periksa.stts<>'Batal' and reg_periksa.no_rkm_medis=? order by reg_periksa.tgl_registrasi desc limit 3");
            }else if(R2.isSelected()==true){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_reg,reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.jam_reg,"+
                    "reg_periksa.kd_dokter,dokter.nm_dokter,poliklinik.nm_poli,reg_periksa.p_jawab,reg_periksa.almt_pj,"+
                    "reg_periksa.hubunganpj,reg_periksa.biaya_reg,reg_periksa.status_lanjut,penjab.png_jawab "+
                    "from reg_periksa inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter "+
                    "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                    "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                    "where reg_periksa.stts<>'Batal' and reg_periksa.no_rkm_medis=? order by reg_periksa.tgl_registrasi");
            }else if(R3.isSelected()==true){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_reg,reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.jam_reg,"+
                    "reg_periksa.kd_dokter,dokter.nm_dokter,poliklinik.nm_poli,reg_periksa.p_jawab,reg_periksa.almt_pj,"+
                    "reg_periksa.hubunganpj,reg_periksa.biaya_reg,reg_periksa.status_lanjut,penjab.png_jawab "+
                    "from reg_periksa inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter "+
                    "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                    "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                    "where reg_periksa.stts<>'Batal' and reg_periksa.no_rkm_medis=? and "+
                    "reg_periksa.tgl_registrasi between ? and ? order by reg_periksa.tgl_registrasi");
            }else if(R4.isSelected()==true){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_reg,reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.jam_reg,"+
                    "reg_periksa.kd_dokter,dokter.nm_dokter,poliklinik.nm_poli,reg_periksa.p_jawab,reg_periksa.almt_pj,"+
                    "reg_periksa.hubunganpj,reg_periksa.biaya_reg,reg_periksa.status_lanjut,penjab.png_jawab "+
                    "from reg_periksa inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter "+
                    "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                    "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                    "where reg_periksa.stts<>'Batal' and reg_periksa.no_rkm_medis=? and reg_periksa.no_rawat=?");
            }
            
            try {
                i=0;
                if(R1.isSelected()==true){
                    ps.setString(1,NoRM.getText().trim());
                }else if(R2.isSelected()==true){
                    ps.setString(1,NoRM.getText().trim());
                }else if(R3.isSelected()==true){
                    ps.setString(1,NoRM.getText().trim());
                    ps.setString(2,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                    ps.setString(3,Valid.SetTgl(Tgl2.getSelectedItem()+""));
                }else if(R4.isSelected()==true){
                    ps.setString(1,NoRM.getText().trim());
                    ps.setString(2,NoRawat.getText().trim());
                }            
                urut=1;
                rs=ps.executeQuery();
                while(rs.next()){
                    try {
                        dokterrujukan="";
                        polirujukan="";
                        rs2=koneksi.prepareStatement(
                            "select poliklinik.nm_poli,dokter.nm_dokter from rujukan_internal_poli "+
                            "inner join poliklinik on rujukan_internal_poli.kd_poli=poliklinik.kd_poli "+
                            "inner join dokter on rujukan_internal_poli.kd_dokter=dokter.kd_dokter "+
                            "where no_rawat='"+rs.getString("no_rawat")+"'").executeQuery();
                        while(rs2.next()){
                            polirujukan=polirujukan+", "+rs2.getString("nm_poli");
                            dokterrujukan=dokterrujukan+", "+rs2.getString("nm_dokter");
                        }
                    } catch (Exception e) {
                        System.out.println("Notif : "+e);
                    } finally{
                        if(rs2!=null){
                            rs2.close();
                        }
                    }   
                    
                    //menampilkan SEP Rajal
                    menampilkanSEPRanap(rs.getString("no_rawat"));
                    
                    //menampilkan Supri
                    menampilkanSupri(rs.getString("no_rawat"));
                    
                    //menampilkan Billing
                    menampilkanBillingRanap(rs.getString("no_rawat"));
                    
                    //menampilkan Radiologi
                    menampilkanRadiologi(rs.getString("no_rawat"));

                    //menampilkan Lab
                    menampilkanLaboratorium(rs.getString("no_rawat"));
                    

                    //menampilkan berkas digital
//                    menampilkanBerkasDigital(rs.getString("no_rawat"));
                    
                    biayaperawatan=rs.getDouble("biaya_reg");
                    //biaya administrasi
                    htmlContent.append(

                             "</table>"
                    );
                 
                    htmlContent.append(
                        "<tr class='isi'><td></td><td colspan='3' align='right'>&nbsp;</tr>"
                    );
                    
                }
                
                LoadHTMLRiwayatPerawatanRanap.setText(
                    "<html>"+
                      "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                       htmlContent.toString()+
                      "</table>"+
                    "</html>");
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }                
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
    }
    
    private void panggilLaporan(String teks) {
        try{
            File g = new File("klaim.css");            
            BufferedWriter bg = new BufferedWriter(new FileWriter(g));
            bg.write(".isi td{border:0 ;font: 10px tahoma;height:12px;background: #ffffff;color:#323232;}.isi a{text-decoration:none;color:#8b9b95;padding:0 0 0 0px;font-family: Tahoma;font-size: 10px;} .isi .sep td{border: 0;font: 10px tahoma;height:12px;border: 0;background: #ffffff;color:#323232;}  .isi .sep-bold td{border: 0;font: 10px tahoma;height:12px; font-weight:bold;border: 0;background: #ffffff;color:#323232;} .isi .sbpk td{border: 1px solid #080808;font: 10px tahoma;height:12px;background: #ffffff;color:#323232;} .isi .sbpk-table td{border-right: 1px solid #080808; border-left: 1px solid #080808; font: 10px tahoma;height:12px;background: #ffffff;color:#323232;} .isi .sepjudul td{border: 0;font: 12px tahoma;height:12.5px;border: 0;background: #ffffff;color:#323232;} .isi .sep_note p{border: 0;padding-bottom:-2px; font: 6px tahoma;height:8px;border: 0;background: #ffffff;color:#323232;} .isi .ttd p{border: 0;padding-bottom:-2px; font-size: 10px tahoma;height:10px;border: 0;background: #ffffff;color:#323232;} .berkas {margin-bottom:20px;} ");
            bg.close();

            File f = new File("berkasklaim.html");            
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write(
                 teks.replaceAll("<head>","<head><link href=\"klaim.css\" rel=\"stylesheet\" type=\"text/css\" />").
                      replaceAll("<body>",
                                 "<body>"
                                            
                      )
            );  
            bw.close();
            Desktop.getDesktop().browse(f.toURI());
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }   
    }
    
    private void isForm(){
        if(ChkInput.isSelected()==true){
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH,126));
            FormInput.setVisible(true);      
            ChkInput.setVisible(true);
        }else if(ChkInput.isSelected()==false){           
            ChkInput.setVisible(false);            
            PanelInput.setPreferredSize(new Dimension(WIDTH,20));
            FormInput.setVisible(false);      
            ChkInput.setVisible(true);
        }
    }

    private void menampilkanSEPRajal(String norawat) {
        try {
            if(chkSEPRajal.isSelected()==true){
                try {
                    rs2=koneksi.prepareStatement(
                        "SELECT bridging_sep.no_sep,bridging_sep.no_rawat,bridging_sep.nomr,bridging_sep.nama_pasien,bridging_sep.tglsep,bridging_sep.tglrujukan,bridging_sep.no_rujukan,bridging_sep.kdppkrujukan,bridging_sep.nmppkrujukan, "+
                        "bridging_sep.kdppkpelayanan,bridging_sep.nmppkpelayanan,IF( bridging_sep.jnspelayanan = '1', '1. Ranap', '2. Ralan' ) AS jnspelayanan,bridging_sep.catatan,bridging_sep.diagawal,bridging_sep.nmdiagnosaawal, "+
                        "bridging_sep.kdpolitujuan,bridging_sep.nmpolitujuan,IF(bridging_sep.klsrawat = '1','1. Kelas 1',IF( bridging_sep.klsrawat = '2', '2. Kelas 2', '3. Kelas 3' )) AS klsrawat,bridging_sep.klsnaik,bridging_sep.pembiayaan, "+
                        "bridging_sep.pjnaikkelas,bridging_sep.lakalantas,bridging_sep.USER,bridging_sep.tanggal_lahir,bridging_sep.peserta,bridging_sep.jkel,bridging_sep.no_kartu,bridging_sep.tglpulang,bridging_sep.asal_rujukan,bridging_sep.eksekutif, "+
                        "bridging_sep.cob,bridging_sep.notelep,bridging_sep.katarak,bridging_sep.tglkkl,bridging_sep.keterangankkl,bridging_sep.suplesi,bridging_sep.no_sep_suplesi,bridging_sep.kdprop,bridging_sep.nmprop,bridging_sep.kdkab, "+
                        "bridging_sep.nmkab,bridging_sep.kdkec,bridging_sep.nmkec,bridging_sep.noskdp,bridging_sep.kddpjp,bridging_sep.nmdpdjp,IF (bridging_sep.tujuankunjungan = '0','Konsultasi dokter(pertama)','Kunjungan Kontrol(ulangan)') as tujuankunjungan,bridging_sep.flagprosedur,bridging_sep.penunjang,bridging_sep.asesmenpelayanan, "+
                        "bridging_sep.kddpjplayanan,bridging_sep.nmdpjplayanan "+
                        "FROM bridging_sep where bridging_sep.jnspelayanan =2 and  bridging_sep.no_rawat ='"+norawat+"'").executeQuery();
                    if(rs2.next()){
                        htmlContent.append(
                          "<tr class='isi'>"+ 
                            "<td valign='top' width='2%'></td>"+        
                            "<td valign='top' width='98%'>"+
                              "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"
                        );
                        rs2.beforeFirst();
                        
                        
                        w=1;
                        while(rs2.next()){
                            get = new GetMethod("http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/penggajian/generateqrcode.php?kodedokter="+rs2.getString("nomr").replace(" ","_"));
                            http.executeMethod(get);
                            
                            htmlContent.append(
                                
                                 "<tr class='sepjudul'>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' align='center'><img alt='SEP Rajal' src='http://192.168.1.236/webapp/berkasrawat/pages/upload/bpjs.jpg' width='100%' height='40'/></td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='47%' style='font-size:12px;' id='title'>SURAT ELIGIBITAS PESERTA RSU BANYUMANIK 2 </td>"+
                                    "<td valign='top' border='0' width='3%'> </td>"+
                                    "<td valign='top' border='0' width='21%'></td>"+
                                 "</tr>"+
//                                 "<tr class='sepjudul'>"+
//                                    "<td valign='top' border='0' width='20%'></td>"+
//                                    "<td valign='top' border='0' width='2%'></td>"+
//                                    "<td valign='top' border='0' width='20%' style='font-size:10px;' id='title'></td>"+
//                                    "<td valign='top' border='0' width='2%'></td>"+
//                                    "<td valign='top' border='0' width='35%'></td>"+
//                                    "<td valign='top' border='0' width='21%'></td>"+
//                                 "</tr>"+
                              "</table>" +
                              "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'>No. SEP</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("no_sep")+"</td>"+
                                    "<td valign='top' border='0' width='2%' ></td>"+
                                    "<td valign='top' border='0' width='12%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='32%'></td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'>Tgl. SEP</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("tglsep")+"</td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'>Peserta</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%' align='left'>"+rs2.getString("peserta")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'>No. Kartu</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("no_kartu")+"( MR : "+rs2.getString("nomr")+" )"+"</td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'>Jns. Rawat</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%' align='left'>"+rs2.getString("jnspelayanan")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'>Nama Peserta</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("nama_pasien")+"</td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'>Jns. Kunjungan</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%' align='left'>"+rs2.getString("tujuankunjungan")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'>Tgl. Lahir</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("tanggal_lahir")+"</td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='32%'></td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'>No. Telepon</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("notelep")+"</td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'>Poli Perujuk</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%' align='left'>"+"-"+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'>Sub/Spesialis</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("nmpolitujuan")+"</td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'>Kls. Hak</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%' align='left'>"+rs2.getString("klsrawat")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'>Dokter</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("nmdpdjp")+"</td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'>Kls. Rawat</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%' align='left'>"+rs2.getString("klsnaik")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'>Faskes Perujuk</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("nmppkrujukan")+"</td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'>Penjamin</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%' align='left'>"+rs2.getString("pembiayaan")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'>Diagnosa Awal</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("nmdiagnosaawal")+"</td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='32%' align='left'></td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'>Catatan</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("catatan")+"</td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='32%' align='left'></td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='32%' align='left'></td>"+
                                 "</tr>"+
                             "</table>"+
                             "<table width='100%' border='0' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                 "<tr>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td class='sep_note' valign='top' border='0' style='font-size:6px;' width='58%'>"+
                                    "<div> "+
                                    "<p>*Saya menyetujui BPJS Kesehatan untuk : </p>"+
                                    "<p>a. membuka dan atau menggunakan informasi medis Pasien untuk keperluan administrasi, pembayaran asuransi atau jaminan pembiayaan kesehatan</p>"+
                                    "<p>b. memberikan akses informasi medis atau riwayat pelayanan kepada dokter/tenaga medis pada RSU BANYUMANIK 2 untuk kepentingan pemeliharaan kesehatan, pengobatan, penyembuhan, dan perawatan Pasien</p>"+
                                    "<p>*Saya mengetahui dan memahami : </p>"+
                                    "<p>a. Rumah Sakit dapat melakukan koordinasi dengan PT Jasa Raharja / PT Taspen / PT ASABRI / BPJS Ketenagakerjaan atau Penjamin lainnya, jika Peserta merupakan pasien yang mengalami kecelakaan lalulintas dan / atau kecelakaan kerja</p>"+
                                    "<p>b. SEP bukan sebagai bukti penjaminan peserta </p>"+
                                    "<p>** Dengan tampilnya luaran SEP elektronik ini merupakan hasil validasi terhadap eligibilitas Pasien secara elektronik (validasi finger print atau biometrik / sistem validasi lain)</p>"+
                                    "<p>dan selanjutnya Pasien dapat mengakses pelayanan kesehatan rujukan sesuai ketentuan berlaku. Kebenaran dan keaslian atas informasi data Pasien menjadi tanggung jawab penuh FKRTL </p>"+
                                    "</div>"+
                                            
                                    "</td>"+
                                    "<td valign='top' border='0' style='font-size:10px;' width='10%'>"+
                                    "<td class='ttd' valign='top' border='0' style='font-size:10px;' width='30%'>"+
                                    "<div> "+
                                    "<p> Persetujuan </p>"+
                                    "<p> Pasien/Keluarga Pasien </p>"+
                                    "<img width='70' height='70' src='http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/penggajian/temp/"+rs2.getString("nomr")+".png'/>"+
                                    "<p>"+rs2.getString("nama_pasien")+"</p>"+
                                    
                                    "</div>"+
                                            
                                    "</td>"+
                                 "</tr>"+
                                "</table>" + // awal batas garis
                                "<table width='100%' border='0'  align='center' height='30' cellspacing='50' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>"+
                                 "</table>" +
                                "<table width='100%' border='0' align='center' height='30' cellspacing='50' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" +
                                  "</table>" +
                                "<table width='100%' border='0' align='center' height='30' cellspacing='50' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" +
                                  "</table>" +
                                "<table width='100%' border='0' align='center' height='30' cellspacing='50' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" +
                                  "</table>" +
                                "<table width='100%' border='0' align='center' height='30' cellspacing='20' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" +
                                 "</table>" +
                                "<table width='100%' border='0' style='border-bottom:3px solid #87b5ed;' align='center' height='30' cellspacing='40' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" 
                                // akhir batas garis
                            );                                     
                            w++;
                        }
                        htmlContent.append(
                             "</table>" +
                            "</td>"+
                          "</tr>"    
                        );
                    }
                } catch (Exception e) {
                    System.out.println("Notifikasi : "+e);
                } finally{
                    if(rs2!=null){
                        rs2.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Hemodialisa : "+e);
        }
    }
    
    private void menampilkanSEPRanap(String norawat) {
        try {
            if(chkSEPRanap.isSelected()==true){
                try {
                    rs2=koneksi.prepareStatement(
                        "SELECT bridging_sep.no_sep,bridging_sep.no_rawat,bridging_sep.nomr,bridging_sep.nama_pasien,bridging_sep.tglsep,bridging_sep.tglrujukan,bridging_sep.no_rujukan,bridging_sep.kdppkrujukan,bridging_sep.nmppkrujukan, "+
                        "bridging_sep.kdppkpelayanan,bridging_sep.nmppkpelayanan,IF( bridging_sep.jnspelayanan = '1', '1. Ranap', '2. Ralan' ) AS jnspelayanan,bridging_sep.catatan,bridging_sep.diagawal,bridging_sep.nmdiagnosaawal, "+
                        "bridging_sep.kdpolitujuan,bridging_sep.nmpolitujuan,IF(bridging_sep.klsrawat = '1','1. Kelas 1',IF( bridging_sep.klsrawat = '2', '2. Kelas 2', '3. Kelas 3' )) AS klsrawat,bridging_sep.klsnaik,bridging_sep.pembiayaan, "+
                        "bridging_sep.pjnaikkelas,bridging_sep.lakalantas,bridging_sep.USER,bridging_sep.tanggal_lahir,bridging_sep.peserta,bridging_sep.jkel,bridging_sep.no_kartu,bridging_sep.tglpulang,bridging_sep.asal_rujukan,bridging_sep.eksekutif, "+
                        "bridging_sep.cob,bridging_sep.notelep,bridging_sep.katarak,bridging_sep.tglkkl,bridging_sep.keterangankkl,bridging_sep.suplesi,bridging_sep.no_sep_suplesi,bridging_sep.kdprop,bridging_sep.nmprop,bridging_sep.kdkab, "+
                        "bridging_sep.nmkab,bridging_sep.kdkec,bridging_sep.nmkec,bridging_sep.noskdp,bridging_sep.kddpjp,bridging_sep.nmdpdjp,IF (bridging_sep.tujuankunjungan = '0','Konsultasi dokter(pertama)','Kunjungan Kontrol(ulangan)') as tujuankunjungan,bridging_sep.flagprosedur,bridging_sep.penunjang,bridging_sep.asesmenpelayanan, "+
                        "bridging_sep.kddpjplayanan,bridging_sep.nmdpjplayanan "+
                        "FROM bridging_sep where bridging_sep.jnspelayanan =1 and  bridging_sep.no_rawat ='"+norawat+"'").executeQuery();
                    if(rs2.next()){
                        htmlContent.append(
                          "<tr class='isi'>"+ 
                            "<td valign='top' width='2%'></td>"+        
                            "<td valign='top' width='98%'>"+
                              "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"
                        );
                        rs2.beforeFirst();
                        w=1;
                        while(rs2.next()){
                            
                            get = new GetMethod("http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/penggajian/generateqrcode.php?kodedokter="+rs2.getString("nomr").replace(" ","_"));
                            http.executeMethod(get);
                            
                            htmlContent.append(
                                 
                                 "<tr class='sepjudul'>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' align='center'><img alt='SEP Rajal' src='http://192.168.1.236/webapp/berkasrawat/pages/upload/bpjs.jpg' width='100%' height='35'/></td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='27%' style='font-size:12px;' id='title'>SURAT ELIGIBITAS PESERTA RSU BANYUMANIK 2 </td>"+
                                    "<td valign='top' border='0' width='23%'> </td>"+
                                    "<td valign='top' border='0' width='21%'></td>"+
                                 "</tr>"+
//                                 "<tr class='sep'>"+
//                                    "<td valign='top' border='0' width='25%'></td>"+
//                                    "<td valign='top' border='0' width='2%'></td>"+
//                                    "<td valign='top' border='0' width='25%'></td>"+
//                                    "<td valign='top' border='0' width='2%'></td>"+
//                                    "<td valign='top' border='0' width='25%'></td>"+
//                                    "<td valign='top' border='0' width='21%'></td>"+
//                                 "</tr>"+
                              "</table>" +
                              "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'>No. SEP</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("no_sep")+"</td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='32%''></td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'>Tgl. SEP</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("tglsep")+"</td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'>Peserta</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%'' align='left'>"+rs2.getString("peserta")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'>No. Kartu</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("no_kartu")+"( MR : "+rs2.getString("nomr")+" )"+"</td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'>Jns. Rawat</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%'' align='left'>"+rs2.getString("jnspelayanan")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'>Nama Peserta</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("nama_pasien")+"</td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'>Jns. Kunjungan</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%'' align='left'>"+rs2.getString("tujuankunjungan")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'>Tgl. Lahir</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("tanggal_lahir")+"</td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='32%''></td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'>No. Telepon</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("notelep")+"</td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'>Poli Perujuk</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%'' align='left'>"+"-"+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'>Sub/Spesialis</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("nmpolitujuan")+"</td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'>Kls. Hak</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%'' align='left'>"+rs2.getString("klsrawat")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'>Dokter</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("nmdpdjp")+"</td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'>Kls. Rawat</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%'' align='left'>"+rs2.getString("klsnaik")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'>Faskes Perujuk</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("nmppkrujukan")+"</td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'>Penjamin</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%'' align='left'>"+rs2.getString("pembiayaan")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'>Diagnosa Awal</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("nmdiagnosaawal")+"</td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='32%'' align='left'></td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'>Catatan</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("catatan")+"</td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='32%'' align='left'></td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='32%'' align='left'></td>"+
                                 "</tr>"+
                             "</table>"+
                             "<table width='100%' border='0' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                 "<tr>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td class='sep_note' valign='top' border='0' style='font-size:6px;' width='58%'>"+
                                    "<div> "+
                                    "<p>*Saya menyetujui BPJS Kesehatan untuk : </p>"+
                                    "<p>a. membuka dan atau menggunakan informasi medis Pasien untuk keperluan administrasi, pembayaran asuransi atau jaminan pembiayaan kesehatan</p>"+
                                    "<p>b. memberikan akses informasi medis atau riwayat pelayanan kepada dokter/tenaga medis pada RSU BANYUMANIK 2 untuk kepentingan pemeliharaan kesehatan, pengobatan, penyembuhan, dan perawatan Pasien</p>"+
                                    "<p>*Saya mengetahui dan memahami : </p>"+
                                    "<p>a. Rumah Sakit dapat melakukan koordinasi dengan PT Jasa Raharja / PT Taspen / PT ASABRI / BPJS Ketenagakerjaan atau Penjamin lainnya, jika Peserta merupakan pasien yang mengalami kecelakaan lalulintas dan / atau kecelakaan kerja</p>"+
                                    "<p>b. SEP bukan sebagai bukti penjaminan peserta </p>"+
                                    "<p>** Dengan tampilnya luaran SEP elektronik ini merupakan hasil validasi terhadap eligibilitas Pasien secara elektronik (validasi finger print atau biometrik / sistem validasi lain)</p>"+
                                    "<p>dan selanjutnya Pasien dapat mengakses pelayanan kesehatan rujukan sesuai ketentuan berlaku. Kebenaran dan keaslian atas informasi data Pasien menjadi tanggung jawab penuh FKRTL </p>"+
                                    "</div>"+
                                            
                                    "</td>"+
                                    "<td valign='top' border='0' style='font-size:10px;' width='10%'>"+
                                    "<td class='ttd' valign='top' border='0' style='font-size:10px;' width='30%'>"+
                                    "<div> "+
                                    "<p> Persetujuan </p>"+
                                    "<p> Pasien/Keluarga Pasien </p>"+
                                    "<img width='70' height='70' src='http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/penggajian/temp/"+rs2.getString("nomr")+".png'/>"+
                                    "<p>"+rs2.getString("nama_pasien")+"</p>"+
                                    
                                    "</div>"+
                                            
                                    "</td>"+
                                 "</tr>"+
                                "</table>" + // awal batas garis
                                "<table width='100%' border='0'  align='center' height='30' cellspacing='50' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>"+
                                 "</table>" +
                                "<table width='100%' border='0' align='center' height='30' cellspacing='50' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" +
                                  "</table>" +
                                "<table width='100%' border='0' align='center' height='30' cellspacing='50' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" +
                                  "</table>" +
                                "<table width='100%' border='0' align='center' height='30' cellspacing='50' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" +
                                  "</table>" +
                                "<table width='100%' border='0' align='center' height='30' cellspacing='20' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" +
                                 "</table>" +
                                "<table width='100%' border='0' style='border-bottom:3px solid #87b5ed;' align='center' height='30' cellspacing='50' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" 
                            );                                     
                            w++;
                        }
                        htmlContent.append(
                             "</table>" +
                            "</td>"+
                          "</tr>");
                    }
                } catch (Exception e) {
                    System.out.println("Notifikasi : "+e);
                } finally{
                    if(rs2!=null){
                        rs2.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Hemodialisa : "+e);
        }
    }
    
    private void menampilkanSBPK(String norawat) {
        try {
            if(chkSBPK.isSelected()==true){
                try {
                    rs2=koneksi.prepareStatement(
                        "SELECT bridging_sep.no_sep,bridging_sep.no_rawat,bridging_sep.nomr,bridging_sep.nama_pasien,bridging_sep.tglsep,bridging_sep.tanggal_lahir,bridging_sep.jkel,bridging_sep.no_kartu, reg_periksa.kd_dokter, dokter.nm_dokter,pemeriksaan_ralan.penilaian,pemeriksaan_ralan.rtl,pemeriksaan_ralan.pemeriksaan, "+
                        "IF ( bridging_sep.tujuankunjungan = '0', 'Konsultasi dokter(pertama)', 'Kunjungan Kontrol(ulangan)' ) AS tujuankunjungan,pemeriksaan_ralan.keluhan,concat('suhu : ',pemeriksaan_ralan.suhu_tubuh,', TD : ',pemeriksaan_ralan.tensi,', Nadi : ',pemeriksaan_ralan.nadi) as fisik "+
                        "FROM bridging_sep inner join pemeriksaan_ralan on bridging_sep.no_rawat=pemeriksaan_ralan.no_rawat INNER JOIN reg_periksa on reg_periksa.no_rawat=bridging_sep.no_rawat INNER JOIN dokter on dokter.kd_dokter=reg_periksa.kd_dokter WHERE bridging_sep.jnspelayanan = 2 AND bridging_sep.no_rawat ='"+norawat+"'").executeQuery();
                    if(rs2.next()){
                        htmlContent.append(
                          "<tr class='isi'>"+ 
                            "<td valign='top' width='2%'></td>"+        
                            "<td valign='top' width='98%'>"+   
                              "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='5' class='tbl_form'>"+
                                  "<tr class='isi' padding='0'>"+
                                    "<td colspan='7' padding='0'>"+
                                        "<table width='100%' bgcolor='#ffffff' align='left' style='border-bottom: 2px solid;' class='tbl_form' cellspacing='0' cellpadding='0'>"+
                                            "<tr>"+
                                                "<td  width='5%'>"+
                                                    "<img width='45' height='45' src='http://192.168.1.236/webapp/berkasrawat/pages/upload/logo.png' style='margin-left:50px;'/>"+
                                                "</td>"+
                                                "<td width='75%'>"+
                                                    "<center>"+
                                                        "<font color='000000' size='3'  face='Tahoma'>RUMAH SAKIT UMUM BANYUMANIK 2</font><br>"+
                                                        "<font color='000000' size='1'  face='Tahoma'>"+
                                                            "Jl. Perintis Kemerdekaan no.57, Banyumanik, Kota Semarang, Jawa Tengah<br>"+
                                                            "(024) 74665525, email: rsubanyumanik2@gmail.com <br>"+
                                                        "</font> "+
                                                    "</center>"+
                                                "</td>"+
                                                "<td  width='20%'>"+
                                                   
                                                "</td>"+
                                            "</tr>"+
                                        "</table>"+
                                    "</td>"+
                                    "</tr>"
                        );
                        rs2.beforeFirst();
                        w=1;
                        while(rs2.next()){
                            
                            htmlContent.append(
                                 
                              "<tr class='sepjudul' cellspacing='5'>"+
                                  
                                    "<td valign='top' border='0' align='center' width='80%' style='font-size:12px;' id='title'>SURAT BUKTI PELAYANAN KESEHATAN (SBPK)</td>"+
                            
                                 "</tr>"+
                              "</table>" +
                              "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'>No.Kartu BPJS </td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("no_kartu")+"</td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'>No. SEP</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%' align='left'>"+rs2.getString("no_sep")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'>No. Rekam Medis</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("nomr")+"</td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'>Jenis Kelamin</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%' align='left'>"+rs2.getString("jkel")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'>Nama Pasien</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("nama_pasien")+"</td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'>Tgl Masuk RS</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%' align='left'>"+rs2.getString("tglsep")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'>Tanggal Lahir</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("tanggal_lahir")+"</td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'>Berat Badan</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%' align='left'></td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'>(Khusus Bayi)</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='32%'></td>"+
                                 "</tr>"+
                                "</table>" +
                                            
                                "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='11%'>Datang Untuk </td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='66%' align='left'>"+rs2.getString("tujuankunjungan")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='11%'>Anamnesa</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='66%' align='left'>"+rs2.getString("keluhan")+"</td>"+
                                 "</tr>"+ 
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='11%'>Pemeriksaan Fisik</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='66%' align='left'>"+rs2.getString("fisik")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='11%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='66%' align='left'>"+rs2.getString("pemeriksaan").replaceAll("(\r\n|\r|\n|\n\r)","<br>")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='11%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='66%' align='left'></td>"+
                                 "</tr>"+
                                "</table>" +
                                            
                                "<table width='90%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                 "<tr class='sbpk'>"+
                                    "<td valign='top' width='70%' align='center'>DIAGNOSA</td>"+
                                    "<td valign='top' width='30%' align='center'>KODE ICD 10</td>"+
                                 "</tr>"+
                                 "<tr class='sbpk'>"+
                                    "<td valign='top' width='70%'>Diagnosa  Utama : "+rs2.getString("penilaian")+"</td>"+
                                    "<td valign='top' width='30%'></td>"+
                                 "</tr>"+
                                            "<tr class='sbpk'>"+
                                    "<td valign='top' width='70%'>Diagnosa  Tambahan : </td>"+
                                    "<td valign='top' width='30%'></td>"+
                                 "</tr>"+
                                            "<tr class='sbpk'>"+
                                    "<td valign='top' width='70%'></td>"+
                                    "<td valign='top' width='30%'></td>"+
                                 "</tr>"+
                                "</table>" +
                                            
                                 "<table width='90%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                 "<tr class='sbpk'>"+
                                    "<td valign='top' width='70%' align='center'>PROSEDUR</td>"+
                                    "<td valign='top' width='30%' align='center'>KODE ICD 9</td>"+
                                 "</tr>"+
                                 "<tr class='sbpk'>"+
                                    "<td valign='top' width='70%'>Prosedur Utama : "+rs2.getString("rtl")+"</td>"+
                                    "<td valign='top' width='30%'></td>"+
                                 "</tr>"+
                                            "<tr class='sbpk'>"+
                                    "<td valign='top' width='70%'>Prosedur Tambahan : </td>"+
                                    "<td valign='top' width='30%'></td>"+
                                 "</tr>"+
                                            "<tr class='sbpk'>"+
                                    "<td valign='top' width='70%'></td>"+
                                    "<td valign='top' width='30%'></td>"+
                                 "</tr>"+
                                "</table>" +
                                            
                                 "<table width='100%' border='0' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                 "<tr>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td class='sep_note' valign='top' border='0' style='font-size:6px;' width='58%'>"+
                                    "</td>"+
                                    "<td valign='top' border='0' style='font-size:10px;' width='10%'>"+
                                    "<td class='ttd' valign='top' border='0' style='font-size:10px;' width='30%'>"+
                                    "<div> "+
                                    "<p> Tanda Verifikasi </p>"+
                                    "<p> DPJP/dr. Pemeriksa </p>"+
                                    "<img width='70' height='70' src='http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/penggajian/temp/"+rs2.getString("kd_dokter")+".png'/>"+
                                    "<p>"+rs2.getString("nm_dokter")+"</p>"+
                                    "</div>"+
                                            
                                    "</td>"+
                                 "</tr>"+
                                "</table>" +
                                "<table width='100%' border='0' height='30' cellspacing='20' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>"+
                                 "</table>" +
                                "<table width='100%' border='0' align='center' height='30' cellspacing='50' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" +
                                  "</table>" +
                                "<table width='100%' border='0' align='center' height='30' cellspacing='40' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" +
                                  "</table>" +
                                "<table width='100%' border='0' align='center' height='30' cellspacing='50' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" +
                                 "</table>" +
                                "<table width='100%' border='0' style='border-bottom:3px solid #87b5ed;' align='center' height='30' cellspacing='50' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>"  
                            );                                     
                            w++;
                        }
                        htmlContent.append(
                             "</table>" +
                            "</td>"+
                          "</tr>");
                    }
                } catch (Exception e) {
                    System.out.println("Notifikasi : "+e);
                } finally{
                    if(rs2!=null){
                        rs2.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif SBPK : "+e);
        }
    }
    
    private void menampilkanSBPKIGD(String norawat) {
        try {
            if(chkSBPKIGD.isSelected()==true){
                try {
                    rs2=koneksi.prepareStatement(
                        "SELECT bridging_sep.no_sep,bridging_sep.no_rawat,bridging_sep.nomr,bridging_sep.nama_pasien,bridging_sep.tglsep,bridging_sep.tanggal_lahir,bridging_sep.jkel, "+ 
                        "bridging_sep.no_kartu, reg_periksa.kd_dokter, dokter.nm_dokter, penilaian_medis_igd.keluhan_utama,penilaian_medis_igd.diagnosis,penilaian_medis_igd.ket_fisik,penilaian_medis_igd.tata, "+
                        "IF ( bridging_sep.tujuankunjungan = '0', 'Konsultasi dokter(pertama)', 'Kunjungan Kontrol(ulangan)' ) AS tujuankunjungan, "+ 
                        "concat( 'suhu : ', penilaian_medis_igd.suhu, ', TD : ', penilaian_medis_igd.td, ', Nadi : ', penilaian_medis_igd.nadi ) AS fisik  "+
                        "FROM bridging_sep INNER JOIN penilaian_medis_igd ON bridging_sep.no_rawat = penilaian_medis_igd.no_rawat INNER JOIN reg_periksa on reg_periksa.no_rawat=bridging_sep.no_rawat " + 
                        "INNER JOIN dokter on dokter.kd_dokter=reg_periksa.kd_dokter WHERE bridging_sep.jnspelayanan = 2 AND bridging_sep.no_rawat ='"+norawat+"'").executeQuery();
                    if(rs2.next()){
                        htmlContent.append(
                          "<tr class='isi'>"+ 
                            "<td valign='top' width='2%'></td>"+        
                            "<td valign='top' width='98%'>"+   
                              "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='5' class='tbl_form'>"+
                                  "<tr class='isi' padding='0'>"+
                                    "<td colspan='7' padding='0'>"+
                                        "<table width='100%' bgcolor='#ffffff' align='left' style='border-bottom: 2px solid;' class='tbl_form' cellspacing='0' cellpadding='0'>"+
                                            "<tr>"+
                                                "<td  width='5%'>"+
                                                    "<img width='45' height='45' src='http://192.168.1.236/webapp/berkasrawat/pages/upload/logo.png' style='margin-left:50px;'/>"+
                                                "</td>"+
                                                "<td width='75%'>"+
                                                    "<center>"+
                                                        "<font color='000000' size='3'  face='Tahoma'>RUMAH SAKIT UMUM BANYUMANIK 2</font><br>"+
                                                        "<font color='000000' size='1'  face='Tahoma'>"+
                                                            "Jl. Perintis Kemerdekaan no.57, Banyumanik, Kota Semarang, Jawa Tengah<br>"+
                                                            "(024) 74665525, email: rsubanyumanik2@gmail.com <br>"+
                                                        "</font> "+
                                                    "</center>"+
                                                "</td>"+
                                                "<td  width='20%'>"+
                                                   
                                                "</td>"+
                                            "</tr>"+
                                        "</table>"+
                                    "</td>"+
                                    "</tr>"
                        );
                        rs2.beforeFirst();
                        w=1;
                        while(rs2.next()){
                            
                            htmlContent.append(
                                 
                              "<tr class='sepjudul' cellspacing='5'>"+
                                  
                                    "<td valign='top' border='0' align='center' width='80%' style='font-size:12px;' id='title'>SURAT BUKTI PELAYANAN KESEHATAN (SBPK)</td>"+
                            
                                 "</tr>"+
                              "</table>" +
                              "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'>No.Kartu BPJS </td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("no_kartu")+"</td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'>No. SEP</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%' align='left'>"+rs2.getString("no_sep")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'>No. Rekam Medis</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("nomr")+"</td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'>Jenis Kelamin</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%' align='left'>"+rs2.getString("jkel")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'>Nama Pasien</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("nama_pasien")+"</td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'>Tgl Masuk RS</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%' align='left'>"+rs2.getString("tglsep")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'>Tanggal Lahir</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("tanggal_lahir")+"</td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'>Berat Badan</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%' align='left'></td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'>(Khusus Bayi)</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='32%'></td>"+
                                 "</tr>"+
                                "</table>" +
                                            
                                "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='11%'>Datang Untuk </td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='66%' align='left'>"+rs2.getString("tujuankunjungan")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='11%'>Anamnesa</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='66%' align='left'>"+rs2.getString("keluhan_utama")+"</td>"+
                                 "</tr>"+ 
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='11%'>Pemeriksaan Fisik</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='66%' align='left'>"+rs2.getString("fisik")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='11%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='66%' align='left'>"+rs2.getString("ket_fisik").replaceAll("(\r\n|\r|\n|\n\r)","<br>")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='11%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='66%' align='left'></td>"+
                                 "</tr>"+
                                "</table>" +
                                            
                                "<table width='90%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                 "<tr class='sbpk'>"+
                                    "<td valign='top' width='70%' align='center'>DIAGNOSA</td>"+
                                    "<td valign='top' width='30%' align='center'>KODE ICD 10</td>"+
                                 "</tr>"+
                                 "<tr class='sbpk'>"+
                                    "<td valign='top' width='70%'>Diagnosa  Utama : "+rs2.getString("diagnosis")+"</td>"+
                                    "<td valign='top' width='30%'></td>"+
                                 "</tr>"+
                                            "<tr class='sbpk'>"+
                                    "<td valign='top' width='70%'>Diagnosa  Tambahan : </td>"+
                                    "<td valign='top' width='30%'></td>"+
                                 "</tr>"+
                                            "<tr class='sbpk'>"+
                                    "<td valign='top' width='70%'></td>"+
                                    "<td valign='top' width='30%'></td>"+
                                 "</tr>"+
                                "</table>" +
                                            
                                 "<table width='90%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                 "<tr class='sbpk'>"+
                                    "<td valign='top' width='70%' align='center'>PROSEDUR</td>"+
                                    "<td valign='top' width='30%' align='center'>KODE ICD 9</td>"+
                                 "</tr>"+
                                 "<tr class='sbpk'>"+
                                    "<td valign='top' width='70%'>Prosedur Utama : "+rs2.getString("tata")+"</td>"+
                                    "<td valign='top' width='30%'></td>"+
                                 "</tr>"+
                                            "<tr class='sbpk'>"+
                                    "<td valign='top' width='70%'>Prosedur Tambahan : </td>"+
                                    "<td valign='top' width='30%'></td>"+
                                 "</tr>"+
                                            "<tr class='sbpk'>"+
                                    "<td valign='top' width='70%'></td>"+
                                    "<td valign='top' width='30%'></td>"+
                                 "</tr>"+
                                "</table>" +
                                            
                                 "<table width='100%' border='0' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                 "<tr>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td class='sep_note' valign='top' border='0' style='font-size:6px;' width='58%'>"+
                                    "</td>"+
                                    "<td valign='top' border='0' style='font-size:10px;' width='10%'>"+
                                    "<td class='ttd' valign='top' border='0' style='font-size:10px;' width='30%'>"+
                                    "<div> "+
                                    "<p> Tanda Verifikasi </p>"+
                                    "<p> DPJP/dr. Pemeriksa </p>"+
                                    "<img width='70' height='70' src='http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/penggajian/temp/"+rs2.getString("kd_dokter")+".png'/>"+
                                    "<p>"+rs2.getString("nm_dokter")+"</p>"+
                                    "</div>"+
                                            
                                    "</td>"+
                                 "</tr>"+
                                "</table>" +
                                "<table width='100%' border='0' height='30' cellspacing='20' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>"+
                                 "</table>" +
                                "<table width='100%' border='0' align='center' height='30' cellspacing='50' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" +
                                  "</table>" +
                                "<table width='100%' border='0' align='center' height='30' cellspacing='40' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" +
                                  "</table>" +
                                "<table width='100%' border='0' align='center' height='30' cellspacing='50' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" +
                                 "</table>" +
                                "<table width='100%' border='0' style='border-bottom:3px solid #87b5ed;' align='center' height='30' cellspacing='50' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>"  
                            );                                     
                            w++;
                        }
                        htmlContent.append(
                             "</table>" +
                            "</td>"+
                          "</tr>");
                    }
                } catch (Exception e) {
                    System.out.println("Notifikasi : "+e);
                } finally{
                    if(rs2!=null){
                        rs2.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif SBPK : "+e);
        }
    }
    
    private void menampilkanSupri(String norawat) {
        try {
            if(chkSupri.isSelected()==true){
                try {
                    rs2=koneksi.prepareStatement(
                        "select perintah_ranap.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,IF(pasien.jk='L','Laki-laki','Perempuan') as jk,reg_periksa.umurdaftar,reg_periksa.sttsumur,"+
                        "pasien.no_tlp,penjab.png_jawab,poliklinik.nm_poli,dokter.nm_dokter,perintah_ranap.tanggal,perintah_ranap.kd_kamar,kamar.kd_bangsal,"+
                        "bangsal.nm_bangsal,kamar.trf_kamar,perintah_ranap.diagnosa,perintah_ranap.catatan,perintah_ranap.kd_dpjp,perintah_ranap.nm_dpjp,reg_periksa.kd_dokter from perintah_ranap "+
                        "inner join reg_periksa on perintah_ranap.no_rawat=reg_periksa.no_rawat "+
                        "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                        "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                        "inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter "+
                        "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                        "inner join kamar on perintah_ranap.kd_kamar=kamar.kd_kamar "+
                        "inner join bangsal on kamar.kd_bangsal=bangsal.kd_bangsal "+
                        "where perintah_ranap.no_rawat ='"+norawat+"'").executeQuery();
                    if(rs2.next()){
                        htmlContent.append(
                          "<tr class='isi'>"+ 
                            "<td valign='top' width='2%'></td>"+        
                            "<td valign='top' width='98%'>"+   
                              "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='5' class='tbl_form'>"+
                                  "<tr class='isi' padding='0'>"+
                                    "<td colspan='7' padding='0'>"+
                                        "<table width='100%' bgcolor='#ffffff' align='left' style='border-bottom: 2px solid;' class='tbl_form' cellspacing='0' cellpadding='0'>"+
                                            "<tr>"+
                                                "<td  width='5%'>"+
                                                    "<img width='45' height='45' src='http://192.168.1.236/webapp/berkasrawat/pages/upload/logo.png' style='margin-left:50px;'/>"+
                                                "</td>"+
                                                "<td width='75%'>"+
                                                    "<center>"+
                                                        "<font color='000000' size='3'  face='Tahoma'>RUMAH SAKIT UMUM BANYUMANIK 2</font><br>"+
                                                        "<font color='000000' size='1'  face='Tahoma'>"+
                                                            "Jl. Perintis Kemerdekaan no.57, Banyumanik, Kota Semarang, Jawa Tengah<br>"+
                                                            "(024) 74665525, email: rsubanyumanik2@gmail.com <br>"+
                                                        "</font> "+
                                                    "</center>"+
                                                "</td>"+
                                                "<td  width='20%'>"+
                                                   
                                                "</td>"+
                                            "</tr>"+
                                        "</table>"+
                                    "</td>"+
                                    "</tr>"
                        );
                        rs2.beforeFirst();
                        w=1;
                        while(rs2.next()){
                            
                            htmlContent.append(
                              "<tr class='sepjudul' cellspacing='5'>"+
                                  
                                    "<td valign='top' border='0' align='center' width='80%' style='font-size:12px;' id='title'>SURAT PERINTAH INAP</td>"+
                            
                                 "</tr>"+
                              "</table>" +
                              "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>Dikirim dari unit </td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='49%' align='left'>"+rs2.getString("nm_poli")+"</td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='left'></td>"+
                                 "</tr>"+
                                      "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>No. Rekam Medis </td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='49%' align='left'>"+rs2.getString("no_rkm_medis")+"</td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='left'></td>"+
                                 "</tr>"+
                                      "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>Nama Pasien</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='49%' align='left'>"+rs2.getString("nm_pasien")+"</td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='left'></td>"+
                                 "</tr>"+
                                      "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>Jenis Kelamin</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='49%' align='left'>"+rs2.getString("jk")+"</td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='left'></td>"+
                                 "</tr>"+
                                      "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>Umur</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='49%' align='left'>"+rs2.getString("umurdaftar")+" "+rs2.getString("sttsumur")+"</td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='left'></td>"+
                                 "</tr>"+
                                      "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>Ruang Perawatan</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='49%' align='left'>"+rs2.getString("nm_bangsal")+"</td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='left'></td>"+
                                 "</tr>"+
                                      "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>Diagnosa Kerja </td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='49%' align='left'>"+rs2.getString("diagnosa")+"</td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='left'></td>"+
                                 "</tr>"+
                                 "</tr>"+
                                      "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>Indikasi Rawat Inap </td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='49%' align='left'>"+rs2.getString("catatan")+"</td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='left'></td>"+
                                 "</tr>"+
                                 "</tr>"+
                                      "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>DPJP</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='49%' align='left'>"+rs2.getString("nm_dpjp")+"</td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='left'></td>"+
                                 "</tr>"+
                                 "</tr>"+
                                      "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>Cara Bayar/Asuransi </td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='49%' align='left'>"+rs2.getString("png_jawab")+"</td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='left'></td>"+
                                 "</tr>"+
                                "</table>" +   
                                           
                                 "<table width='100%' border='0' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                 "<tr>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td class='sep_note' valign='top' border='0' style='font-size:6px;' width='58%'>"+
                                    "</td>"+
                                    "<td valign='top' border='0' style='font-size:10px;' width='10%'>"+
                                    "<td class='ttd' valign='top' border='0' style='font-size:10px;' width='30%'>"+
                                    "<div> "+
                                    "<p> Semarang, " +rs2.getString("tanggal")+" </p>"+
                                    "<p> Tanda Verifikasi </p>"+
                                    "<p> Dokter Jaga IGD / Poliklinik </p>"+
                                    "<img width='70' height='70' src='http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/penggajian/temp/"+rs2.getString("kd_dokter")+".png'/>"+
                                    "<p>"+rs2.getString("nm_dokter")+"</p>"+
                                    "</div>"+
                                    "</td>"+
                                 "</tr>"+
                                "</table>" +
                               
                                "<table width='100%' border='0' height='30' cellspacing='20' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>"+
                                 "</table>" +
                                "<table width='100%' border='0' align='center' height='30' cellspacing='50' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" +
                                  "</table>" +
                                 "<table width='100%' border='0' align='center' height='30' cellspacing='50' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" +
                                  "</table>" +
                                "<table width='100%' border='0' align='center' height='30' cellspacing='40' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" +
                                  "</table>" +
                                "<table width='100%' border='0' align='center' height='30' cellspacing='50' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" +
                                 "</table>" +
                                "<table width='100%' border='0' style='border-bottom:3px solid #87b5ed;' align='center' height='50' cellspacing='50' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>"  
                            );                                     
                            w++;
                        }
                        htmlContent.append(
                             "</table>" +
                            "</td>"+
                          "</tr>");
                    }
                } catch (Exception e) {
                    System.out.println("Notifikasi : "+e);
                } finally{
                    if(rs2!=null){
                        rs2.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif SBPK : "+e);
        }
    }
    
    private void menampilkanFormRehabMedis(String norawat) {
        try {
            if(chkFormRehabMedis.isSelected()==true){
                try {
                    rs2=koneksi.prepareStatement(
                        "SELECT reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.tgl_lahir,reg_periksa.almt_pj,reg_periksa.kd_dokter,dokter.nm_dokter,form_fisioterapi.tgl_perawatan,form_fisioterapi.jam_rawat,form_fisioterapi.anamnesa,"+
                        "form_fisioterapi.anjuran,form_fisioterapi.diagnosa,form_fisioterapi.tindakan,form_fisioterapi.evaluasi,form_fisioterapi.suspek,form_fisioterapi.ket_suspek "+
                        "FROM pasien INNER JOIN reg_periksa ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis INNER JOIN form_fisioterapi ON form_fisioterapi.no_rawat = reg_periksa.no_rawat INNER JOIN dokter on dokter.kd_dokter=reg_periksa.kd_dokter "+
                        "WHERE form_fisioterapi.no_rawat='"+norawat+"'").executeQuery();
                    if(rs2.next()){
                        htmlContent.append(
                          "<tr class='isi'>"+ 
                            "<td valign='top' width='2%'></td>"+        
                            "<td valign='top' width='98%'>"+   
                              "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='5' class='tbl_form'>"+
                                  "<tr class='isi' padding='0'>"+
                                    "<td colspan='7' padding='0'>"+
                                        "<table width='100%' bgcolor='#ffffff' align='left' style='border-bottom: 2px solid;' class='tbl_form' cellspacing='0' cellpadding='0'>"+
                                            "<tr>"+
                                                "<td  width='5%'>"+
                                                    "<img width='45' height='45' src='http://192.168.1.236/webapp/berkasrawat/pages/upload/logo.png' style='margin-left:50px;'/>"+
                                                "</td>"+
                                                "<td width='75%'>"+
                                                    "<center>"+
                                                        "<font color='000000' size='3'  face='Tahoma'>RUMAH SAKIT UMUM BANYUMANIK 2</font><br>"+
                                                        "<font color='000000' size='1'  face='Tahoma'>"+
                                                            "Jl. Perintis Kemerdekaan no.57, Banyumanik, Kota Semarang, Jawa Tengah<br>"+
                                                            "(024) 74665525, email: rsubanyumanik2@gmail.com <br>"+
                                                        "</font> "+
                                                    "</center>"+
                                                "</td>"+
                                                "<td  width='20%'>"+
                                                   
                                                "</td>"+
                                            "</tr>"+
                                        "</table>"+
                                    "</td>"+
                                    "</tr>"
                        );
                        rs2.beforeFirst();
                        w=1;
                        while(rs2.next()){
                            htmlContent.append(
                                 
                                 "<tr class='sepjudul' cellspacing='5'>"+
      
                                    "<td valign='top' border='0' width='100%' align='center' style='font-size:12px; font-weight:bold;' id='title'>FORMULIR KLAIM RAWAT JALAN</td>"+
                                 "</tr>"+
                                 "<tr class='sepjudul' cellspacing='5'>"+
   
                                     "<td valign='top' border='0' width='100%' align='center' style='font-size:12px; font-weight:bold;' id='title'>LAYANAN KEDOKTERAN FISIK DAN REHABILITASI</td>"+

                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='32%'></td>"+
                                 "</tr>"+
                              "</table>" +
                              "<table width='90%' border='2px solid black' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='28%'>1. IDENTITAS PASIEN/PESERTA </td>"+
                                    "<td valign='top' border='0' width='1%'></td>"+
                                    "<td valign='top' border='0' width='69%'></td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='28%'>No Rekam Medis</td>"+
                                    "<td valign='top' border='0' width='1%'>:</td>"+
                                    "<td valign='top' border='0' width='69%'>"+rs2.getString("no_rkm_medis")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='28%'>Nama Pasien </td>"+
                                    "<td valign='top' border='0' width='1%'>:</td>"+
                                    "<td valign='top' border='0' width='69%'>"+rs2.getString("nm_pasien")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='28%'>Tanggal Lahir </td>"+
                                    "<td valign='top' border='0' width='1%'>:</td>"+
                                    "<td valign='top' border='0' width='69%'>"+rs2.getString("tgl_lahir")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='28%'>Alamat </td>"+
                                    "<td valign='top' border='0' width='1%'>:</td>"+
                                    "<td valign='top' border='0' width='69%'>"+rs2.getString("almt_pj")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='28%'>Rujukan dari Dokter</td>"+
                                    "<td valign='top' border='0' width='1%'>:</td>"+
                                    "<td valign='top' border='0' width='69%'>"+rs2.getString("nm_dokter")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='28%'>Tanggal Rujukan</td>"+
                                    "<td valign='top' border='0' width='1%'>:</td>"+
                                    "<td valign='top' border='0' width='69%'></td>"+
                                 "</tr>"+ 
                                "</table>" +
                                
                                "<table width='90%' border='2px solid black' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='28%'>2. DIISI OLEH DOKTER Sp. KFR </td>"+
                                    "<td valign='top' border='0' width='1%'></td>"+
                                    "<td valign='top' border='0' width='69%'></td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='28%'>Tanggal Pelayanan</td>"+
                                    "<td valign='top' border='0' width='1%'>:</td>"+
                                    "<td valign='top' border='0' width='69%'>"+rs2.getString("tgl_perawatan")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='28%'>Tanggal ke DPJP Pengirim</td>"+
                                    "<td valign='top' border='0' width='1%'>:</td>"+
                                    "<td valign='top' border='0' width='69%'>"+rs2.getString("tgl_perawatan")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='28%'>Anamnesa </td>"+
                                    "<td valign='top' border='0' width='1%'>:</td>"+
                                    "<td valign='top' border='0' width='69%'>"+rs2.getString("anamnesa")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='28%'>Diagnosis (ICD 10)</td>"+
                                    "<td valign='top' border='0' width='1%'>:</td>"+
                                    "<td valign='top' border='0' width='69%'>"+rs2.getString("diagnosa")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='28%'>Tindakan (ICD 9 CM)</td>"+
                                    "<td valign='top' border='0' width='1%'>:</td>"+
                                    "<td valign='top' border='0' width='69%'>"+rs2.getString("tindakan")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='28%'>Anjuran</td>"+
                                    "<td valign='top' border='0' width='1%'>:</td>"+
                                    "<td valign='top' border='0' width='69%'>"+rs2.getString("anjuran")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='28%'>Evaluasi</td>"+
                                    "<td valign='top' border='0' width='1%'>:</td>"+
                                    "<td valign='top' border='0' width='69%'>"+rs2.getString("evaluasi")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='28%'>Suspek Penyakit Akibat Kerja"+
                                    "<td valign='top' border='0' width='1%'>:</td>"+
                                    "<td valign='top' border='0' width='69%'>"+rs2.getString("suspek")+", "+rs2.getString("ket_suspek")+"</td>"+
                                 "</tr>"+
                                "</table>" +
                                            
                                 "<table width='100%' border='0' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                  "<tr>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td class='sep_note' valign='top' border='0' style='font-size:6px;' width='58%'>"+
                                    "</td>"+
                                    "<td valign='top' border='0' style='font-size:10px;' width='10%'>"+
                                    "<td class='ttd' valign='top' border='0' style='font-size:10px;' width='30%'>"+
                                    "</td>"+
                                 "</tr>"+
                                 "<tr>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td class='sep_note' valign='top' border='0' style='font-size:6px;' width='58%'>"+
                                    "</td>"+
                                    "<td valign='top' border='0' style='font-size:10px;' width='10%'>"+
                                    "<td class='ttd' valign='top' border='0' style='font-size:10px;' width='30%'>"+
                                    "<div> "+
                                    "<p> Tanda Verifikasi </p>"+
                                    "<p> Dokter Sp.KFR</p>"+
                                    "<img width='70' height='70' src='http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/penggajian/temp/"+rs2.getString("kd_dokter")+".png'/>"+
                                    "<p>"+rs2.getString("nm_dokter")+"</p>"+
                                    "</div>"+
                                            
                                    "</td>"+
                                 "</tr>"+
                                "</table>" +
                                "<table width='100%' border='0' height='30' cellspacing='10' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>"+
                                 "</table>" +
                                "<table width='100%' border='0' align='center' height='30' cellspacing='50' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" +
                                  "</table>" +
                                "<table width='100%' border='0' align='center' height='30' cellspacing='50' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" +
                                  "</table>" +
                                "<table width='100%' border='0' align='center' height='30' cellspacing='30' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" +
                                 "</table>" +
                                "<table width='100%' border='0' style='border-bottom:3px solid #87b5ed;' align='center' height='30' cellspacing='30' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>"  
                            );                                     
                            w++;
                        }
                        htmlContent.append(
                             "</table>" +
                            "</td>"+
                          "</tr>");
                    }
                } catch (Exception e) {
                    System.out.println("Notifikasi : "+e);
                } finally{
                    if(rs2!=null){
                        rs2.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif SBPK : "+e);
        }
    }
    
    private void menampilkanProgramTerapi(String norawat) {
        try {
            if(chkProgramTerapi.isSelected()==true){
                try {
                    rs2=koneksi.prepareStatement(
                        "SELECT reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.tgl_lahir,reg_periksa.almt_pj,reg_periksa.kd_dokter,dokter.nm_dokter, "+
                        "petugas.nama,program_terapi.tgl_perawatan,program_terapi.diagnosa,program_terapi.terapi,program_terapi.program,program_terapi.kd_dokter as ke_dokter_program,program_terapi.nip "+
                        "FROM pasien INNER JOIN reg_periksa ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis INNER JOIN program_terapi ON program_terapi.no_rawat = reg_periksa.no_rawat INNER JOIN petugas ON petugas.nip = program_terapi.nip "+
                        "INNER JOIN dokter on dokter.kd_dokter= reg_periksa.kd_dokter WHERE program_terapi.no_rawat ='"+norawat+"'").executeQuery();
                    if(rs2.next()){
                        htmlContent.append(
                          "<tr class='isi'>"+ 
                            "<td valign='top' width='2%'></td>"+        
                            "<td valign='top' width='98%'>"+   
                              "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='5' class='tbl_form'>"+
                                  "<tr class='isi' padding='0'>"+
                                    "<td colspan='7' padding='0'>"+
                                        "<table width='100%' bgcolor='#ffffff' align='left' style='border-bottom: 2px solid;' class='tbl_form' cellspacing='0' cellpadding='0'>"+
                                            "<tr>"+
                                                "<td  width='5%'>"+
                                                    "<img width='45' height='45' src='http://192.168.1.236/webapp/berkasrawat/pages/upload/logo.png' style='margin-left:50px;'/>"+
                                                "</td>"+
                                                "<td width='75%'>"+
                                                    "<center>"+
                                                        "<font color='000000' size='3'  face='Tahoma'>RUMAH SAKIT UMUM BANYUMANIK 2</font><br>"+
                                                        "<font color='000000' size='1'  face='Tahoma'>"+
                                                            "Jl. Perintis Kemerdekaan no.57, Banyumanik, Kota Semarang, Jawa Tengah<br>"+
                                                            "(024) 74665525, email: rsubanyumanik2@gmail.com <br>"+
                                                        "</font> "+
                                                    "</center>"+
                                                "</td>"+
                                                "<td  width='20%' border='1px solid black'>"+
//                                                   "<font color='000000' size='1'  face='Tahoma'>No. RM      : "+rs2.getString("no_rkm_medis")+" </font><br>"+
//                                                   "<font color='000000' size='1'  face='Tahoma'>Nama : "+rs2.getString("nm_pasien")+" </font><br>"+
//                                                   "<font color='000000' size='1'  face='Tahoma'>Tgl. Lahir  : "+rs2.getString("tgl_lahir")+" </font><br>"+
                                                "</td>"+
                                            "</tr>"+
                                        "</table>"+
                                    "</td>"+
                                    "</tr>"
                        );
                        rs2.beforeFirst();
                        w=1;
                        while(rs2.next()){
                            
                             get = new GetMethod("http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/penggajian/generateqrcodepetugas.php?nip="+rs2.getString("nip").replace(" ","_"));
                            http.executeMethod(get);
                            
                            htmlContent.append(
                                 
                                 "<tr class='sepjudul' cellspacing='5'>"+
      
                                    "<td valign='top' border='0' width='100%' align='center' style='font-size:12px; font-weight:bold;' id='title'>PROGRAM TERAPI</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='32%'></td>"+
                                 "</tr>"+
                              "</table>" +
                              "<table width='90%' border='1' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='28%'>IDENTITAS PASIEN/PESERTA </td>"+
                                    "<td valign='top' border='0' width='1%'></td>"+
                                    "<td valign='top' border='0' width='69%'></td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='28%'>No Rekam Medis</td>"+
                                    "<td valign='top' border='0' width='1%'>:</td>"+
                                    "<td valign='top' border='0' width='69%'>"+rs2.getString("no_rkm_medis")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='28%'>Nama Pasien </td>"+
                                    "<td valign='top' border='0' width='1%'>:</td>"+
                                    "<td valign='top' border='0' width='69%'>"+rs2.getString("nm_pasien")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='28%'>Tanggal Lahir </td>"+
                                    "<td valign='top' border='0' width='1%'>:</td>"+
                                    "<td valign='top' border='0' width='69%'>"+rs2.getString("tgl_lahir")+"</td>"+
                                 "</tr>"+
                                "</table>" +
                                
                                "<table width='100%' border='0' align='center' height='30' cellspacing='5' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>"+
                                 "</table>" +
                                            
                                "<table width='90%' border='1' align='center' height='30' cellspacing='0' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='28%'>DIAGNOSA</td>"+
                                    "<td valign='top' border='0' width='1%'>:</td>"+
                                    "<td valign='top' border='0' width='69%'>"+rs2.getString("diagnosa")+"</td>"+
                                 "</tr>"+
                                 "</table>" +
                                
                                 "<table width='90%' border='1' align='center' height='30' cellspacing='0' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='28%'>PERMINTAAN TERAPI</td>"+
                                    "<td valign='top' border='0' width='1%'>:</td>"+
                                    "<td valign='top' border='0' width='69%'>"+rs2.getString("terapi")+"</td>"+
                                 "</tr>"+
                                 "</table>" +
                                            
                                "<table width='100%' border='0' align='center' height='30' cellspacing='5' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>"+
                                 "</table>" +
                                            
                                 "<table width='90%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                 "<tr class='sbpk'>"+
                                    "<td valign='top' width='40%' align='center'>PROGRAM</td>"+
                                    "<td valign='top' width='25%' align='center'>TANGGAL</td>"+
                                    "<td valign='top' width='35%' align='center'>TANDA VERIFIKASI</td>"+
                                 "</tr>"+
                                 "<tr class='sbpk'>"+
                                   "<td valign='top' width='40%' align='left'>"+rs2.getString("program")+"</td>"+
                                    "<td valign='top' width='25%' align='center'>"+rs2.getString("tgl_perawatan")+"</td>"+
                                    "<td valign='top' width='35%' align='center'>"+
                                    "<div> "+
                                    "<img width='70' height='70' src='http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/penggajian/temp/"+rs2.getString("nip")+".png'/>"+
                                    "<p>"+rs2.getString("nama")+"</p>"+
                                    "</div>"+
                                     "</td>"+
                                 "</tr>"+
                                    "<tr class='sbpk'>"+
                                   "<td valign='top' width='40%' align='center'></td>"+
                                    "<td valign='top' width='25%' align='center'></td>"+
                                    "<td valign='top' width='35%' align='center'></td>"+
                                 "</tr>"+
                                    "<tr class='sbpk'>"+
                                    "<td valign='top' width='40%' align='center'></td>"+
                                    "<td valign='top' width='25%' align='center'></td>"+
                                    "<td valign='top' width='35%' align='center'></td>"+
                                 "</tr>"+
                                "</table>" +
                                            
                                 "<table width='100%' border='0' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                  "<tr>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td class='sep_note' valign='top' border='0' style='font-size:6px;' width='58%'>"+
                                    "</td>"+
                                    "<td valign='top' border='0' style='font-size:10px;' width='10%'>"+
                                    "<td class='ttd' valign='top' border='0' style='font-size:10px;' width='30%'>"+
                                    "</td>"+
                                 "</tr>"+
                                 "<tr>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td class='sep_note' valign='top' border='0' style='font-size:6px;' width='58%'>"+
                                    "</td>"+
                                    "<td valign='top' border='0' style='font-size:10px;' width='10%'>"+
                                    "<td class='ttd' valign='top' border='0' style='font-size:10px;' width='30%'>"+
                                    "<div> "+
                                    "<p> Tanda Verifikasi </p>"+
                                    "<p> Dokter Sp.KFR</p>"+
                                    "<img width='70' height='70' src='http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/penggajian/temp/"+rs2.getString("kd_dokter")+".png'/>"+
                                    "<p>"+rs2.getString("nm_dokter")+"</p>"+
                                    "</div>"+             
                                    "</td>"+
                                 "</tr>"+
                                "</table>" +
                                
                                "<table width='100%' border='0' align='center' height='30' cellspacing='40' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>"+
                                "</table>" +
                                 "<table width='100%' border='0' align='center' height='30' cellspacing='40' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>"+
                                "</table>" +
                                "<table width='100%' border='0' align='center' height='30' cellspacing='30' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>"+
                                "</table>" +  
                                "<table width='100%' border='0' style='border-bottom:3px solid #87b5ed;' align='center' height='40' cellspacing='20' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>"
//                                 
                            );                                     
                            w++;
                        }
                        htmlContent.append(
                             "</table>" +
                            "</td>"+
                          "</tr>");
                    }
                } catch (Exception e) {
                    System.out.println("Notifikasi : "+e);
                } finally{
                    if(rs2!=null){
                        rs2.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif SBPK : "+e);
        }
    }
    
    private void menampilkanRadiologi(String norawat) {
        try {
            if((chkPemeriksaanRadiologi.isSelected()==true)||(chkPemeriksaanRadiologiRanap.isSelected()==true)){
                try {
                    rs2=koneksi.prepareStatement(
                        "SELECT reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.tgl_lahir,reg_periksa.almt_pj,jns_perawatan_radiologi.nm_perawatan,hasil_radiologi.hasil,periksa_radiologi.tgl_periksa, "+
                        "periksa_radiologi.jam,poliklinik.nm_poli,reg_periksa.kd_dokter,dokter.nm_dokter,petugas.nip,petugas.nama,pegawai.nama as dokter_perujuk "+
                        "FROM pasien INNER JOIN reg_periksa ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis INNER JOIN periksa_radiologi ON periksa_radiologi.no_rawat = reg_periksa.no_rawat "+     
                        "INNER JOIN petugas ON petugas.nip = periksa_radiologi.nip INNER JOIN dokter ON dokter.kd_dokter = periksa_radiologi.kd_dokter INNER JOIN jns_perawatan_radiologi on periksa_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "+   
                        "INNER JOIN hasil_radiologi on periksa_radiologi.no_rawat=hasil_radiologi.no_rawat INNER JOIN poliklinik on poliklinik.kd_poli=reg_periksa.kd_poli INNER JOIN pegawai on periksa_radiologi.dokter_perujuk=pegawai.nik "+
                        "WHERE periksa_radiologi.no_rawat ='"+norawat+"' group by hasil_radiologi.hasil").executeQuery();
                    if(rs2.next()){
                        rs2.beforeFirst();
                        w=1;
                        while(rs2.next()){
                            
                            get = new GetMethod("http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/penggajian/generateqrcodepetugas.php?nip="+rs2.getString("nip").replace(" ","_"));
                            http.executeMethod(get);
                            
                            htmlContent.append(
                             "<tr class='isi'>"+
                             "<td valign='top' width='2%'></td>"+        
                              "<td valign='top' width='98%'>"+   
                              "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='5' class='tbl_form'>"+
                                  "<tr class='isi' padding='0'>"+
                                    "<td colspan='7' padding='0'>"+
                                        "<table width='100%' bgcolor='#ffffff' align='left' style='border-bottom: 2px solid;' class='tbl_form' cellspacing='0' cellpadding='0'>"+
                                            "<tr>"+
                                                "<td  width='5%'>"+
                                                    "<img width='45' height='45' src='http://192.168.1.236/webapp/berkasrawat/pages/upload/logo.png' style='margin-left:50px;'/>"+
                                                "</td>"+
                                                "<td width='75%'>"+
                                                    "<center>"+
                                                        "<font color='000000' size='3'  face='Tahoma'>RUMAH SAKIT UMUM BANYUMANIK 2</font><br>"+
                                                        "<font color='000000' size='1'  face='Tahoma'>"+
                                                            "Jl. Perintis Kemerdekaan no.57, Banyumanik, Kota Semarang, Jawa Tengah<br>"+
                                                            "(024) 74665525, email: rsubanyumanik2@gmail.com <br>"+
                                                        "</font> "+
                                                    "</center>"+
                                                "</td>"+
                                                "<td  width='20%' border='1px solid black'>"+
//                                                   "<font color='000000' size='1'  face='Tahoma'>No. RM      : "+rs2.getString("no_rkm_medis")+" </font><br>"+
//                                                   "<font color='000000' size='1'  face='Tahoma'>Nama : "+rs2.getString("nm_pasien")+" </font><br>"+
//                                                   "<font color='000000' size='1'  face='Tahoma'>Tgl. Lahir  : "+rs2.getString("tgl_lahir")+" </font><br>"+
                                                "</td>"+
                                            "</tr>"+
                                        "</table>"+
                                    "</td>"+
                                    "</tr>"+
                                 "<tr class='sepjudul' cellspacing='5'>"+
                                    "<td valign='top' border='0' width='100%' align='center' style='font-size:12px; font-weight:bold;' id='title'>HASIL PEMERIKSAAN RADIOLOGI</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='32%'></td>"+
                                 "</tr>"+
                              "</table>" +
                               "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='13%'>No. RM </td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("no_rkm_medis")+"</td>"+
                                    "<td valign='top' border='0' width='1%'></td>"+
                                    "<td valign='top' border='0' width='14%'>Penanggung Jawab</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%' align='left'>"+rs2.getString("nm_dokter")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='13%'>Nama Pasien</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("nm_pasien")+"</td>"+
                                    "<td valign='top' border='0' width='1%'></td>"+
                                    "<td valign='top' border='0' width='14%'>Dokter Pengirim</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%' align='left'>"+rs2.getString("dokter_perujuk")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='13%'>Tanggal Lahir</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("tgl_lahir")+"</td>"+
                                    "<td valign='top' border='0' width='1%'></td>"+
                                    "<td valign='top' border='0' width='14%'>Tgl. Pemeriksaan</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%' align='left'>"+rs2.getString("tgl_periksa")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='13%'>Alamat</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("almt_pj")+"</td>"+
                                    "<td valign='top' border='0' width='1%'></td>"+
                                    "<td valign='top' border='0' width='14%'>Jam Pemeriksaan</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%' align='left'>"+rs2.getString("jam")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='13%'>No. Periksa</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("no_rawat")+"</td>"+
                                    "<td valign='top' border='0' width='1%'></td>"+
                                    "<td valign='top' border='0' width='14%'>Poliklinik</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%' align='left'>"+rs2.getString("nm_poli")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='13%'>Pemeriksaan</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("nm_perawatan")+"</td>"+
                                    "<td valign='top' border='0' width='1%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='32%' align='left'></td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='13%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='1%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='32%' align='left'></td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='13%'>Hasil Pemeriksaaan</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='1%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='32%' align='left'></td>"+
                                 "</tr>"+       
                                "</table>" +
                                            
                                "<table width='100%' border='1' align='center' height='30' cellspacing='0' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='96%'>"+rs2.getString("hasil").replaceAll("(\r\n|\r|\n|\n\r)","<br>")+"<</td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                  "</tr>"+
                                 "</table>" +
                                            
                                 "<table width='90%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' width='40%' align='center'></td>"+
                                    "<td valign='top' width='20%' align='center'></td>"+
                                    "<td valign='top' width='40%' align='center'></td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' width='40%' align='center'></td>"+
                                    "<td valign='top' width='20%' align='center'></td>"+
                                    "<td valign='top' width='40%' align='center'>Tanda Verifikasi</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' width='40%' align='center'>Dokter Radiologi</td>"+
                                    "<td valign='top' width='20%' align='center'></td>"+
                                    "<td valign='top' width='40%' align='center'>Petugas Radiologi</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' width='40%' align='center'>"+
                                    "<div> "+
                                    "<img width='70' height='70' src='http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/penggajian/temp/"+rs2.getString("kd_dokter")+".png'/>"+
                                    "<p>"+rs2.getString("nm_dokter")+"</p>"+
                                    "</div>"+  
                                    "</td>"+
                                    "<td valign='top' width='20%' align='center'></td>"+
                                    "<td valign='top' width='40%' align='center'>"+
                                    "<div> "+
                                    "<img width='70' height='70' src='http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/penggajian/temp/"+rs2.getString("nip")+".png'/>"+
                                    "<p>"+rs2.getString("nama")+"</p>"+
                                    "</div>"+  
                                     "</td>"+
                                 "</tr>"+
                                "</table>" +
                                
                                "<table width='100%' border='0' align='center' height='30' cellspacing='40' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>"+
                                "</table>" +
                                 "<table width='100%' border='0' align='center' height='30' cellspacing='40' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>"+
                                "</table>" +
                                "<table width='100%' border='0' align='center' height='30' cellspacing='50' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>"+
                                "</table>" +
                                "<table width='100%' border='0' style='border-bottom:3px solid #87b5ed;' align='center' height='40' cellspacing='20' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>"+
                                "</table>" 
                            );                                     
                            w++;
                        }
                        htmlContent.append(                           
                            "</td>"+
                          "</tr>");
                    }
                } catch (Exception e) {
                    System.out.println("Notifikasi : "+e);
                } finally{
                    if(rs2!=null){
                        rs2.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Pemeriksaan Radiologi : "+e);
        }
    }
    
    private void menampilkanLaboratorium(String norawat) {
        try {
            if((chkPemeriksaanLaborat.isSelected()==true)||(chkPemeriksaanLaboratRanap.isSelected()==true)){
                try {
                    rs2=koneksi.prepareStatement(
                        "SELECT reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.tgl_lahir,reg_periksa.almt_pj,permintaan_lab.noorder,permintaan_lab.tgl_permintaan,permintaan_lab.jam_permintaan, "+
                        "detail_periksa_lab.tgl_periksa,detail_periksa_lab.jam,poliklinik.nm_poli,reg_periksa.kd_dokter,dokter.nm_dokter,petugas.nip,petugas.nama,pegawai.nama AS dokter_perujuk "+
                        "FROM pasien INNER JOIN reg_periksa ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis INNER JOIN periksa_lab ON periksa_lab.no_rawat = reg_periksa.no_rawat INNER JOIN permintaan_lab on permintaan_lab.no_rawat=reg_periksa.no_rawat "+     
                        "INNER JOIN petugas ON petugas.nip = periksa_lab.nip INNER JOIN dokter ON dokter.kd_dokter = periksa_lab.kd_dokter INNER JOIN detail_periksa_lab ON detail_periksa_lab.no_rawat = periksa_lab.no_rawat  "+   
                        "INNER JOIN poliklinik ON poliklinik.kd_poli = reg_periksa.kd_poli INNER JOIN pegawai ON periksa_lab.dokter_perujuk = pegawai.nik  "+
                        "WHERE periksa_lab.no_rawat='"+norawat+"' GROUP BY permintaan_lab.noorder").executeQuery();
                    if(rs2.next()){
                        rs2.beforeFirst();
                        w=1;
                        while(rs2.next()){
                            
                            get = new GetMethod("http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/penggajian/generateqrcodepetugas.php?nip="+rs2.getString("nip").replace(" ","_"));
                            http.executeMethod(get);
                            
                            htmlContent.append(
                             "<tr class='isi'>"+
                             "<td valign='top' width='2%'></td>"+        
                              "<td valign='top' width='98%'>"+   
                              "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='5' class='tbl_form'>"+
                                  "<tr class='isi' padding='0'>"+
                                    "<td colspan='7' padding='0'>"+
                                        "<table width='100%' bgcolor='#ffffff' align='left' style='border-bottom: 2px solid;' class='tbl_form' cellspacing='0' cellpadding='0'>"+
                                            "<tr>"+
                                                "<td  width='5%'>"+
                                                    "<img width='45' height='45' src='http://192.168.1.236/webapp/berkasrawat/pages/upload/logo.png' style='margin-left:50px;'/>"+
                                                "</td>"+
                                                "<td width='75%'>"+
                                                    "<center>"+
                                                        "<font color='000000' size='3'  face='Tahoma'>RUMAH SAKIT UMUM BANYUMANIK 2</font><br>"+
                                                        "<font color='000000' size='1'  face='Tahoma'>"+
                                                            "Jl. Perintis Kemerdekaan no.57, Banyumanik, Kota Semarang, Jawa Tengah<br>"+
                                                            "(024) 74665525, email: rsubanyumanik2@gmail.com <br>"+
                                                        "</font> "+
                                                    "</center>"+
                                                "</td>"+
                                                "<td  width='20%' border='1px solid black'>"+
//                                                   "<font color='000000' size='1'  face='Tahoma'>No. RM      : "+rs2.getString("no_rkm_medis")+" </font><br>"+
//                                                   "<font color='000000' size='1'  face='Tahoma'>Nama : "+rs2.getString("nm_pasien")+" </font><br>"+
//                                                   "<font color='000000' size='1'  face='Tahoma'>Tgl. Lahir  : "+rs2.getString("tgl_lahir")+" </font><br>"+
                                                "</td>"+
                                            "</tr>"+
                                        "</table>"+
                                    "</td>"+
                                    "</tr>"+
                                 "<tr class='sepjudul' cellspacing='5'>"+
                                    "<td valign='top' border='0' width='100%' align='center' style='font-size:12px; font-weight:bold;' id='title'>HASIL PEMERIKSAAN LABORATORIUM</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='12%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='32%'></td>"+
                                 "</tr>"+
                              "</table>" +
                                     
                               //IDENTITAS
                               "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='13%'>No. RM </td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("no_rkm_medis")+"</td>"+
                                    "<td valign='top' border='0' width='1%'></td>"+
                                    "<td valign='top' border='0' width='14%'>No. Permintaan Lab</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%' align='left'>"+rs2.getString("noorder")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='13%'>Nama Pasien</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("nm_pasien")+"</td>"+
                                    "<td valign='top' border='0' width='1%'></td>"+
                                    "<td valign='top' border='0' width='14%'>Tgl. Permintaan</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%' align='left'>"+rs2.getString("tgl_permintaan")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='13%'>Tanggal Lahir</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("tgl_lahir")+"</td>"+
                                    "<td valign='top' border='0' width='1%'></td>"+
                                    "<td valign='top' border='0' width='14%'>Jam Permintaan</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%' align='left'>"+rs2.getString("jam_permintaan")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='13%'>Alamat</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("almt_pj")+"</td>"+
                                    "<td valign='top' border='0' width='1%'></td>"+
                                    "<td valign='top' border='0' width='14%'>Tgl. Keluar Hasil</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%' align='left'>"+rs2.getString("tgl_periksa")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='13%'>No. Periksa</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("no_rawat")+"</td>"+
                                    "<td valign='top' border='0' width='1%'></td>"+
                                    "<td valign='top' border='0' width='14%'>Jam Keluar Hasil </td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%' align='left'>"+rs2.getString("jam")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='13%'>Dokter Pengirim</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='36%' align='left'>"+rs2.getString("dokter_perujuk")+"</td>"+
                                    "<td valign='top' border='0' width='1%'></td>"+
                                    "<td valign='top' border='0' width='14%'>Poliklinik </td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='32%' align='left'>"+rs2.getString("nm_poli")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='13%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='1%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='32%' align='left'></td>"+
                                 "</tr>"+
//                                 "<tr class='sep'>"+
//                                    "<td valign='top' border='0' width='2%'></td>"+
//                                    "<td valign='top' border='0' width='13%'>Hasil Pemeriksaaan</td>"+
//                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
//                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
//                                    "<td valign='top' border='0' width='1%'></td>"+
//                                    "<td valign='top' border='0' width='14%'></td>"+
//                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
//                                    "<td valign='top' border='0' width='32%' align='left'></td>"+
//                                 "</tr>"+       
                                "</table>" 
                            );
                                
                                //ISI
                                            
                                try {
                                    rs4=koneksi.prepareStatement(
                                         "select periksa_lab.tgl_periksa,periksa_lab.jam from periksa_lab where periksa_lab.kategori='PK' and periksa_lab.no_rawat='"+rs.getString("no_rawat")+"' "+
                                         "group by concat(periksa_lab.no_rawat,periksa_lab.tgl_periksa,periksa_lab.jam) order by periksa_lab.tgl_periksa,periksa_lab.jam").executeQuery();
                                    if(rs4.next()){
//                                        rs4.beforeFirst();
//                                        w=1;
//                                        while(rs4.next()){
                                            try{
                                                rs5=koneksi.prepareStatement(
                                                     "select periksa_lab.kd_jenis_prw, "+
                                                     "jns_perawatan_lab.nm_perawatan,petugas.nama,periksa_lab.biaya,periksa_lab.dokter_perujuk,dokter.nm_dokter "+
                                                     "from periksa_lab inner join jns_perawatan_lab on periksa_lab.kd_jenis_prw=jns_perawatan_lab.kd_jenis_prw "+
                                                     "inner join petugas on periksa_lab.nip=petugas.nip inner join dokter on periksa_lab.kd_dokter=dokter.kd_dokter "+
                                                     "where periksa_lab.kategori='PK' and periksa_lab.no_rawat='"+rs.getString("no_rawat")+"' "+
                                                     "and periksa_lab.tgl_periksa='"+rs4.getString("tgl_periksa")+"' and periksa_lab.jam='"+rs4.getString("jam")+"'").executeQuery();
                                                
                                                //while(rs5.next()){
                                                    if(rs5.next()){
                                                        htmlContent.append(
                                                        "<table width='100%' border='1' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                                            "<tr class='sbpk'>"+
                                                                   "<td valign='top' align='center' width='25%' bgcolor='#FFFAF8'>Pemeriksaan</td>"+
                                                                   "<td valign='top' align='center' width='20%' bgcolor='#FFFAF8'>Hasil</td>"+
                                                                   "<td valign='top' align='center' width='22%' bgcolor='#FFFAF8'>Satuan</td>"+
                                                                   "<td valign='top' align='center' width='22%' bgcolor='#FFFAF8'>Nilai Rujukan</td>"+
                                                                   "<td valign='top' align='center' width='11%' bgcolor='#FFFAF8'>Keterangan</td>"+
                                                            "</tr>"+
                                                                
                                                            "<tr class='sbpk-table'>"+
                                                               "<td valign='top' width='25%' >"+rs5.getString("nm_perawatan")+"</td>"+
                                                               "<td valign='top' width='20%' ></td>"+
                                                               "<td valign='top' width='22%' ></td>"+
                                                               "<td valign='top' width='22%' ></td>"+
                                                               "<td valign='top' width='11%' ></td>"+
                                                            "</tr>"
                                                       ); 

                                                    try {
                                                        rs3=koneksi.prepareStatement(
                                                            "select template_laboratorium.Pemeriksaan, detail_periksa_lab.nilai,"+
                                                            "template_laboratorium.satuan,detail_periksa_lab.nilai_rujukan,detail_periksa_lab.biaya_item,"+
                                                            "detail_periksa_lab.keterangan from detail_periksa_lab inner join "+
                                                            "template_laboratorium on detail_periksa_lab.id_template=template_laboratorium.id_template "+
                                                            "where detail_periksa_lab.no_rawat='"+rs.getString("no_rawat")+"' and "+
                                                            "detail_periksa_lab.kd_jenis_prw='"+rs5.getString("kd_jenis_prw")+"' and "+
                                                            "detail_periksa_lab.tgl_periksa='"+rs4.getString("tgl_periksa")+"' and "+
                                                            "detail_periksa_lab.jam='"+rs4.getString("jam")+"' order by detail_periksa_lab.kd_jenis_prw,template_laboratorium.urut ").executeQuery();
                                                        if(rs3.next()){ 
//                                                            rs3.beforeFirst();
                                                            //while(rs3.next()){
                                                            if(rs3.next()){
                                                                htmlContent.append(
                                                                    "<tr class='sbpk-table'>"+
                                                                       "<td valign='top' width='25%' >"+rs3.getString("Pemeriksaan")+"</td>"+
                                                                       "<td valign='top' align='center' width='20%' >"+rs3.getString("nilai")+"</td>"+
                                                                       "<td valign='top' align='center' width='22%' >"+rs3.getString("satuan")+"</td>"+
                                                                       "<td valign='top' align='center' width='22%' >"+rs3.getString("nilai_rujukan")+"</td>"+
                                                                       "<td valign='top' align='center' width='11%' >"+rs3.getString("keterangan")+"</td>"+
                                                 
                                                                    "</tr>"); 
                                                         
                                                            }                                               
                                                        }
                                                    } catch (Exception e) {
                                                        System.out.println("Notifikasi : "+e);
                                                    } finally{
                                                        if(rs3!=null){
                                                            rs3.close();
                                                        }
                                                    }
//                                                    s++;
                                                }  
                                                w++;
                                            } catch (Exception e) {
                                                System.out.println("Notifikasi Lab : "+e);
                                            } finally{
                                                if(rs5!=null){
                                                    rs5.close();
                                                }
                                            }
//                                        }
                                        htmlContent.append(
                                                      "</table>");
                                    }
                                } catch (Exception e) {
                                    System.out.println("Notif : "+e);
                                } finally{
                                    if(rs4!=null){
                                        rs4.close();
                                    }
                                }

                  
                               htmlContent.append(
                                 
                                 //TTD Barcode
                                 "<table width='90%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' width='40%' align='center'></td>"+
                                    "<td valign='top' width='20%' align='center'></td>"+
                                    "<td valign='top' width='40%' align='center'></td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' width='40%' align='center'></td>"+
                                    "<td valign='top' width='20%' align='center'></td>"+
                                    "<td valign='top' width='40%' align='center'>Tanda Verifikasi</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' width='40%' align='center'>Penanggunng Jawab</td>"+
                                    "<td valign='top' width='20%' align='center'></td>"+
                                    "<td valign='top' width='40%' align='center'>Petugas Laboratorium</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' width='40%' align='center'>"+
                                    "<div> "+
                                    "<img width='70' height='70' src='http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/penggajian/temp/"+rs2.getString("kd_dokter")+".png'/>"+
                                    "<p>"+rs2.getString("nm_dokter")+"</p>"+
                                    "</div>"+  
                                    "</td>"+
                                    "<td valign='top' width='20%' align='center'></td>"+
                                    "<td valign='top' width='40%' align='center'>"+
                                    "<div> "+
                                    "<img width='70' height='70' src='http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/penggajian/temp/"+rs2.getString("nip")+".png'/>"+
                                    "<p>"+rs2.getString("nama")+"</p>"+
                                    "</div>"+  
                                     "</td>"+
                                 "</tr>"+
                                "</table>" +
                                
                                "<table width='100%' border='0' align='center' height='30' cellspacing='40' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>"+
                                "</table>" +
                                 "<table width='100%' border='0' align='center' height='30' cellspacing='40' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>"+
                                "</table>" +
                                "<table width='100%' border='0' align='center' height='30' cellspacing='40' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>"+
                                "</table>" +
                                "<table width='100%' border='0' align='center' height='30' cellspacing='20' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>"+
                                "</table>" 
//                                "<table width='100%' border='0' style='border-bottom:3px solid #87b5ed;' align='center' height='40' cellspacing='20' class='tbl_form'>"+
//                                 "<tr class='sep'>"+
//                                    "<td valign='top' border='0' width='2%'></td>"+
//                                    "<td valign='top' border='0' width='14%'></td>"+
//                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
//                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
//                                    "<td valign='top' border='0' width='2%'></td>"+
//                                 "</tr>"+
//                                "</table>" 
                            );                                     
                            w++;
                        }
                        htmlContent.append(                           
                            "</td>"+
                          "</tr>");
                    }
                } catch (Exception e) {
                    System.out.println("Notifikasi : "+e);
                } finally{
                    if(rs2!=null){
                        rs2.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Pemeriksaan Laborat : "+e);
        }
    }
    
    private void menampilkanBilling(String norawat) {
        try {
            if(chkBilling.isSelected()==true){
                try {
                    rs2=koneksi.prepareStatement(
                        "SELECT nota_jalan.no_nota,poliklinik.nm_poli,nota_jalan.tanggal,nota_jalan.jam,reg_periksa.no_rkm_medis,pasien.nm_pasien,reg_periksa.almt_pj,dokter.nm_dokter,reg_periksa.biaya_reg "+
                        "FROM reg_periksa INNER JOIN nota_jalan ON reg_periksa.no_rawat = nota_jalan.no_rawat INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis INNER JOIN dokter ON dokter.kd_dokter = reg_periksa.kd_dokter "+
                        "INNER JOIN poliklinik ON reg_periksa.kd_poli = poliklinik.kd_poli WHERE reg_periksa.no_rawat='"+norawat+"'").executeQuery();
                    if(rs2.next()){
                        htmlContent.append(
                          "<tr class='isi'>"+ 
                            "<td valign='top' width='2%'></td>"+        
                            "<td valign='top' width='98%'>"+   
                              "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='5' class='tbl_form'>"+
                                  "<tr class='isi' padding='0'>"+
                                    "<td colspan='7' padding='0'>"+
                                        "<table width='100%' bgcolor='#ffffff' align='left' style='border-bottom: 2px solid;' class='tbl_form' cellspacing='0' cellpadding='0'>"+
                                            "<tr>"+
                                                "<td  width='5%'>"+
                                                    "<img width='45' height='45' src='http://192.168.1.236/webapp/berkasrawat/pages/upload/logo.png' style='margin-left:50px;'/>"+
                                                "</td>"+
                                                "<td width='75%'>"+
                                                    "<center>"+
                                                        "<font color='000000' size='3'  face='Tahoma'>RUMAH SAKIT UMUM BANYUMANIK 2</font><br>"+
                                                        "<font color='000000' size='1'  face='Tahoma'>"+
                                                            "Jl. Perintis Kemerdekaan no.57, Banyumanik, Kota Semarang, Jawa Tengah<br>"+
                                                            "(024) 74665525, email: rsubanyumanik2@gmail.com <br>"+
                                                        "</font> "+
                                                    "</center>"+
                                                "</td>"+
                                                "<td  width='20%'>"+
                                                   
                                                "</td>"+
                                            "</tr>"+
                                        "</table>"+
                                    "</td>"+
                                    "</tr>"
                        );
                        rs2.beforeFirst();
                        w=1;
                        while(rs2.next()){
                            
                            htmlContent.append(
                                 
                              "<tr class='sepjudul' cellspacing='5'>"+
                                  
                                    "<td valign='top' border='0' align='center' width='80%' style='font-size:12px;' id='title'>BILLING</td>"+
                            
                                 "</tr>"+
                              "</table>" +
                              "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>No.Nota </td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='49%' align='left'>"+rs2.getString("no_nota")+"</td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='left'></td>"+
                                 "</tr>"+
                                      "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>Unit/Instalasi </td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='49%' align='left'>"+rs2.getString("nm_poli")+"</td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='left'></td>"+
                                 "</tr>"+
                                      "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>Tanggal & Jam </td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='49%' align='left'>"+rs2.getString("tanggal")+" "+rs2.getString("jam")+"</td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='left'></td>"+
                                 "</tr>"+
                                      "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>No.RM </td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='49%' align='left'>"+rs2.getString("no_rkm_medis")+"</td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='left'></td>"+
                                 "</tr>"+
                                      "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>Nama Pasien</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='49%' align='left'>"+rs2.getString("nm_pasien")+"</td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='left'></td>"+
                                 "</tr>"+
                                      "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>Alamat Pasien</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='49%' align='left'>"+rs2.getString("almt_pj")+"</td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='left'></td>"+
                                 "</tr>"+
                                      "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>Dokter </td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='49%' align='left'>"+rs2.getString("nm_dokter")+"</td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='left'></td>"+
                                 "</tr>"+
                                "</table>" +
                                            
                                "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>Administrasi Rekam Medis </td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='49%' align='left'></td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='right'>"+rs2.getString("biaya_reg")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>Tindakan</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='49%' align='left'></td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='right'></td>"+
                                 "</tr>"  
                                    );
                            
                                    biayaperawatan=rs.getDouble("biaya_reg");
                                 //tindakan dr
                                    try{
                                    rs3=koneksi.prepareStatement(
                                            "select rawat_jl_dr.kd_jenis_prw,jns_perawatan.nm_perawatan,dokter.nm_dokter,rawat_jl_dr.biaya_rawat, "+
                                            "rawat_jl_dr.tgl_perawatan,rawat_jl_dr.jam_rawat from rawat_jl_dr inner join jns_perawatan on rawat_jl_dr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                                            "inner join dokter on rawat_jl_dr.kd_dokter=dokter.kd_dokter where rawat_jl_dr.no_rawat='"+rs.getString("no_rawat")+"' order by rawat_jl_dr.tgl_perawatan,rawat_jl_dr.jam_rawat").executeQuery();
                                    if(rs3.next()){                                    
                                        rs3.beforeFirst();
                                        w=1;
                                        while(rs3.next()){
                                            htmlContent.append(
                                                    "<tr class='sep'>"+
                                                    "<td valign='top' border='0' width='2%'></td>"+
                                                    "<td valign='top' border='0' width='24%'></td>"+
                                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                                    "<td valign='top' border='0' width='49%' align='left'>"+rs3.getString("nm_perawatan")+"</td>"+
                                                    "<td valign='top' border='0' width='4%'>1.0</td>"+
                                                    "<td valign='top' border='0' width='20%' align='right'>"+Valid.SetAngka(rs3.getDouble("biaya_rawat"))+"</td>"+
                                                    "</tr>"
                                                 ); 
                                            w++;
                                            biayaperawatan=biayaperawatan+rs3.getDouble("biaya_rawat");
                                        }
                                       
                                    }                                
                                } catch (Exception e) {
                                    System.out.println("Notifikasi tindakan: "+e);
                                } finally{
                                    if(rs3!=null){
                                        rs3.close();
                                    }
                                }
                                   
                                //tindakan perawat   
                                try{
                                    rs3=koneksi.prepareStatement(
                                    "select rawat_jl_pr.kd_jenis_prw,jns_perawatan.nm_perawatan,petugas.nama,rawat_jl_pr.biaya_rawat, "+
                                    "rawat_jl_pr.tgl_perawatan,rawat_jl_pr.jam_rawat from rawat_jl_pr inner join jns_perawatan on rawat_jl_pr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                                    "inner join petugas on rawat_jl_pr.nip=petugas.nip where rawat_jl_pr.no_rawat='"+rs.getString("no_rawat")+"' order by rawat_jl_pr.tgl_perawatan,rawat_jl_pr.jam_rawat").executeQuery();
                                    if(rs3.next()){                                    
                                        rs3.beforeFirst();
                                        w=1;
                                        while(rs3.next()){
                                            htmlContent.append(
                                                    "<tr class='sep'>"+
                                                    "<td valign='top' border='0' width='2%'></td>"+
                                                    "<td valign='top' border='0' width='24%'></td>"+
                                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                                    "<td valign='top' border='0' width='49%' align='left'>"+rs3.getString("nm_perawatan")+"</td>"+
                                                    "<td valign='top' border='0' width='4%'>1.0</td>"+
                                                    "<td valign='top' border='0' width='20%' align='right'>"+Valid.SetAngka(rs3.getDouble("biaya_rawat"))+"</td>"+
                                                    "</tr>"
                                                 ); 
                                            w++;
                                            biayaperawatan=biayaperawatan+rs3.getDouble("biaya_rawat");
                                        }
                                       
                                    }                                
                                } catch (Exception e) {
                                    System.out.println("Notifikasi tindakan pr: "+e);
                                } finally{
                                    if(rs3!=null){
                                        rs3.close();
                                    }
                                }
                                
                                //tindakan dr + perawat   
                                try{
                                    rs3=koneksi.prepareStatement(
                                    "select rawat_jl_drpr.kd_jenis_prw,jns_perawatan.nm_perawatan,dokter.nm_dokter,petugas.nama,rawat_jl_drpr.biaya_rawat, "+
                                    "rawat_jl_drpr.tgl_perawatan,rawat_jl_drpr.jam_rawat from rawat_jl_drpr inner join jns_perawatan on rawat_jl_drpr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                                    "inner join dokter on rawat_jl_drpr.kd_dokter=dokter.kd_dokter inner join petugas on rawat_jl_drpr.nip=petugas.nip "+
                                    "where rawat_jl_drpr.no_rawat='"+rs.getString("no_rawat")+"' order by rawat_jl_drpr.tgl_perawatan,rawat_jl_drpr.jam_rawat").executeQuery();
                                    if(rs3.next()){                                    
                                        rs3.beforeFirst();
                                        w=1;
                                        while(rs3.next()){
                                            htmlContent.append(
                                                    "<tr class='sep'>"+
                                                    "<td valign='top' border='0' width='2%'></td>"+
                                                    "<td valign='top' border='0' width='24%'></td>"+
                                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                                    "<td valign='top' border='0' width='49%' align='left'>"+rs3.getString("nm_perawatan")+"</td>"+
                                                    "<td valign='top' border='0' width='4%'>1.0</td>"+
                                                    "<td valign='top' border='0' width='20%' align='right'>"+Valid.SetAngka(rs3.getDouble("biaya_rawat"))+"</td>"+
                                                    "</tr>"
                                                 ); 
                                            w++;
                                            biayaperawatan=biayaperawatan+rs3.getDouble("biaya_rawat");
                                        }
                                       
                                    }                                
                                } catch (Exception e) {
                                    System.out.println("Notifikasi tindakan dr pr: "+e);
                                } finally{
                                    if(rs3!=null){
                                        rs3.close();
                                    }
                                }
                                
                                //operasi + vk  
                                try{
                                    rs3=koneksi.prepareStatement(
                                   "select operasi.tgl_operasi,operasi.jenis_anasthesi,operasi.operator1, operasi.operator2, operasi.operator3, operasi.asisten_operator1,"+
                                    "operasi.asisten_operator2,operasi.asisten_operator3,operasi.biayaasisten_operator3, operasi.instrumen, operasi.dokter_anak, operasi.perawaat_resusitas, "+
                                    "operasi.dokter_anestesi, operasi.asisten_anestesi, operasi.asisten_anestesi2,operasi.asisten_anestesi2, operasi.bidan, operasi.bidan2, operasi.bidan3, operasi.perawat_luar, operasi.omloop,"+
                                    "operasi.omloop2,operasi.omloop3,operasi.omloop4,operasi.omloop5,operasi.dokter_pjanak,operasi.dokter_umum, "+
                                    "operasi.kode_paket,paket_operasi.nm_perawatan, operasi.biayaoperator1, operasi.biayaoperator2, operasi.biayaoperator3, "+
                                    "operasi.biayaasisten_operator1, operasi.biayaasisten_operator2, operasi.biayaasisten_operator3, operasi.biayainstrumen, "+
                                    "operasi.biayadokter_anak, operasi.biayaperawaat_resusitas, operasi.biayadokter_anestesi, "+
                                    "operasi.biayaasisten_anestesi,operasi.biayaasisten_anestesi2, operasi.biayabidan,operasi.biayabidan2,operasi.biayabidan3, operasi.biayaperawat_luar, operasi.biayaalat,"+
                                    "operasi.biayasewaok,operasi.akomodasi,operasi.bagian_rs,operasi.biaya_omloop,operasi.biaya_omloop2,operasi.biaya_omloop3,operasi.biaya_omloop4,operasi.biaya_omloop5,"+
                                    "operasi.biayasarpras,operasi.biaya_dokter_pjanak,operasi.biaya_dokter_umum,"+
                                    "(operasi.biayaoperator1+operasi.biayaoperator2+operasi.biayaoperator3+"+
                                    "operasi.biayaasisten_operator1+operasi.biayaasisten_operator2+operasi.biayaasisten_operator3+operasi.biayainstrumen+"+
                                    "operasi.biayadokter_anak+operasi.biayaperawaat_resusitas+operasi.biayadokter_anestesi+"+
                                    "operasi.biayaasisten_anestesi+operasi.biayaasisten_anestesi2+operasi.biayabidan+operasi.biayabidan2+operasi.biayabidan3+operasi.biayaperawat_luar+operasi.biayaalat+"+
                                    "operasi.biayasewaok+operasi.akomodasi+operasi.bagian_rs+operasi.biaya_omloop+operasi.biaya_omloop2+operasi.biaya_omloop3+operasi.biaya_omloop4+operasi.biaya_omloop5+"+
                                    "operasi.biayasarpras+operasi.biaya_dokter_pjanak+operasi.biaya_dokter_umum) as total "+
                                    "from operasi inner join paket_operasi on operasi.kode_paket=paket_operasi.kode_paket "+
                                    "where operasi.no_rawat='"+rs.getString("no_rawat")+"' order by operasi.tgl_operasi").executeQuery();
                                    if(rs3.next()){                                    
                                        rs3.beforeFirst();
                                        w=1;
                                        while(rs3.next()){
                                            htmlContent.append(
                                                    "<tr class='sep'>"+
                                                    "<td valign='top' border='0' width='2%'></td>"+
                                                    "<td valign='top' border='0' width='24%'></td>"+
                                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                                    "<td valign='top' border='0' width='49%' align='left'>"+rs3.getString("nm_perawatan")+"</td>"+
                                                    "<td valign='top' border='0' width='4%'>1.0</td>"+
                                                    "<td valign='top' border='0' width='20%' align='right'>"+Valid.SetAngka(rs3.getDouble("total"))+"</td>"+
                                                    "</tr>"
                                                 ); 
                                            w++;
                                            biayaperawatan=biayaperawatan+rs3.getDouble("total");
                                        }
                                       
                                    }                                
                                } catch (Exception e) {
                                    System.out.println("Notifikasi OK VK: "+e);
                                } finally{
                                    if(rs3!=null){
                                        rs3.close();
                                    }
                                }

                                 //Radologi  
                                try{
                                    rs3=koneksi.prepareStatement(
                                     "select periksa_radiologi.tgl_periksa,periksa_radiologi.jam,periksa_radiologi.kd_jenis_prw, "+
                                        "jns_perawatan_radiologi.nm_perawatan,petugas.nama,periksa_radiologi.biaya,periksa_radiologi.dokter_perujuk,"+
                                        "dokter.nm_dokter,concat("+
                                       "if(periksa_radiologi.proyeksi<>'',concat('Proyeksi : ',periksa_radiologi.proyeksi,', '),''),"+
                                       "if(periksa_radiologi.kV<>'',concat('kV : ',periksa_radiologi.kV,', '),''),"+
                                       "if(periksa_radiologi.mAS<>'',concat('mAS : ',periksa_radiologi.mAS,', '),''),"+
                                       "if(periksa_radiologi.FFD<>'',concat('FFD : ',periksa_radiologi.FFD,', '),''),"+
                                       "if(periksa_radiologi.BSF<>'',concat('BSF : ',periksa_radiologi.BSF,', '),''),"+
                                       "if(periksa_radiologi.inak<>'',concat('Inak : ',periksa_radiologi.inak,', '),''),"+
                                       "if(periksa_radiologi.jml_penyinaran<>'',concat('Jml Penyinaran : ',periksa_radiologi.jml_penyinaran,', '),''),"+
                                       "if(periksa_radiologi.dosis<>'',concat('Dosis Radiasi : ',periksa_radiologi.dosis),'')) as proyeksi "+
                                        "from periksa_radiologi inner join jns_perawatan_radiologi on periksa_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "+
                                        "inner join petugas on periksa_radiologi.nip=petugas.nip inner join dokter on periksa_radiologi.kd_dokter=dokter.kd_dokter "+
                                        "where periksa_radiologi.no_rawat='"+rs.getString("no_rawat")+"' order by periksa_radiologi.tgl_periksa,periksa_radiologi.jam").executeQuery();
                                    if(rs3.next()){                                    
                                        rs3.beforeFirst();
                                        w=1;
                                        while(rs3.next()){
                                            htmlContent.append(
                                                    "<tr class='sep'>"+
                                                    "<td valign='top' border='0' width='2%'></td>"+
                                                    "<td valign='top' border='0' width='24%'></td>"+
                                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                                    "<td valign='top' border='0' width='49%' align='left'>"+rs3.getString("nm_perawatan")+"</td>"+
                                                    "<td valign='top' border='0' width='4%'>1.0</td>"+
                                                    "<td valign='top' border='0' width='20%' align='right'>"+Valid.SetAngka(rs3.getDouble("biaya"))+"</td>"+
                                                    "</tr>"
                                                 ); 
                                            w++;
                                            biayaperawatan=biayaperawatan+rs3.getDouble("biaya");
                                        }
                                       
                                    }                                
                                } catch (Exception e) {
                                    System.out.println("Notifikasi radiologi: "+e);
                                } finally{
                                    if(rs3!=null){
                                        rs3.close();
                                    }
                                }
                                
                                
                                 //Laboratorium
//                                
//                                try{
//                                    rs3=koneksi.prepareStatement(
//                                             "select periksa_lab.kd_jenis_prw, "+
//                                             "jns_perawatan_lab.nm_perawatan,petugas.nama,periksa_lab.biaya,periksa_lab.dokter_perujuk,dokter.nm_dokter "+
//                                             "from periksa_lab inner join jns_perawatan_lab on periksa_lab.kd_jenis_prw=jns_perawatan_lab.kd_jenis_prw "+
//                                             "inner join petugas on periksa_lab.nip=petugas.nip inner join dokter on periksa_lab.kd_dokter=dokter.kd_dokter "+
//                                             "where periksa_lab.kategori='PK' and periksa_lab.no_rawat='"+rs.getString("no_rawat")+"' "+
//                                             "and periksa_lab.tgl_periksa='"+rs4.getString("tgl_periksa")+"' and periksa_lab.jam='"+rs4.getString("jam")+"'").executeQuery();
//                                    if(rs3.next()){                                    
//                                        rs3.beforeFirst();
//                                        w=1;
//                                        while(rs3.next()){
//                                            htmlContent.append(
//                                                    "<tr class='sep'>"+
//                                                    "<td valign='top' border='0' width='2%'></td>"+
//                                                    "<td valign='top' border='0' width='24%'></td>"+
//                                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
//                                                    "<td valign='top' border='0' width='49%' align='left'>"+rs3.getString("nm_perawatan")+"</td>"+
//                                                    "<td valign='top' border='0' width='4%'>1.0</td>"+
//                                                    "<td valign='top' border='0' width='20%' align='right'>"+Valid.SetAngka(rs3.getDouble("biaya"))+"</td>"+
//                                                    "</tr>"
//                                                 ); 
//                                            w++;
//                                            biayaperawatan=biayaperawatan+rs3.getDouble("biaya");
//                                        }
//                                       
//                                    }                                
//                                } catch (Exception e) {
//                                    System.out.println("Notifikasi Laboratorium: "+e);
//                                } finally{
//                                    if(rs3!=null){
//                                        rs3.close();
//                                    }
//                                }
                                
                                
                                 // Detail Laboratorium  
                                try {
                                    rs4=koneksi.prepareStatement(
                                         "select periksa_lab.tgl_periksa,periksa_lab.jam from periksa_lab where periksa_lab.kategori='PK' and periksa_lab.no_rawat='"+rs.getString("no_rawat")+"' "+
                                         "group by concat(periksa_lab.no_rawat,periksa_lab.tgl_periksa,periksa_lab.jam) order by periksa_lab.tgl_periksa,periksa_lab.jam").executeQuery();
                                    if(rs4.next()){
                                        rs4.beforeFirst();
                                        w=1;
                                        while(rs4.next()){
                                            try{
                                                rs3=koneksi.prepareStatement(
                                                     "select periksa_lab.kd_jenis_prw, "+
                                                     "jns_perawatan_lab.nm_perawatan,petugas.nama,periksa_lab.biaya,periksa_lab.dokter_perujuk,dokter.nm_dokter "+
                                                     "from periksa_lab inner join jns_perawatan_lab on periksa_lab.kd_jenis_prw=jns_perawatan_lab.kd_jenis_prw "+
                                                     "inner join petugas on periksa_lab.nip=petugas.nip inner join dokter on periksa_lab.kd_dokter=dokter.kd_dokter "+
                                                     "where periksa_lab.kategori='PK' and periksa_lab.no_rawat='"+rs.getString("no_rawat")+"' "+
                                                     "and periksa_lab.tgl_periksa='"+rs4.getString("tgl_periksa")+"' and periksa_lab.jam='"+rs4.getString("jam")+"'").executeQuery();
                                                s=1;
                                                while(rs3.next()){
                                                    if(s==1){
                                                        htmlContent.append(
                                                            "<tr class='sep'>"+
                                                            "<td valign='top' border='0' width='2%'></td>"+
                                                            "<td valign='top' border='0' width='24%'></td>"+
                                                            "<td valign='top' border='0' width='1%' align='right'></td>"+
                                                            "<td valign='top' border='0' width='49%' align='left'>"+rs3.getString("nm_perawatan")+"</td>"+
                                                            "<td valign='top' border='0' width='4%'>1.0</td>"+
                                                            "<td valign='top' border='0' width='20%' align='right'>"+Valid.SetAngka(rs3.getDouble("biaya"))+"</td>"+
                                                            "</tr>"
                                                       ); 
                                                    }else{
                                                        htmlContent.append(
                                                            "<tr class='sep'>"+
                                                            "<td valign='top' border='0' width='2%'></td>"+
                                                            "<td valign='top' border='0' width='24%'></td>"+
                                                            "<td valign='top' border='0' width='1%' align='right'></td>"+
                                                            "<td valign='top' border='0' width='49%' align='left'>"+rs3.getString("nm_perawatan")+"</td>"+
                                                            "<td valign='top' border='0' width='4%'>1.0</td>"+
                                                            "<td valign='top' border='0' width='20%' align='right'>"+Valid.SetAngka(rs3.getDouble("biaya"))+"</td>"+
                                                            "</tr>"
                                                        ); 
                                                    }

                                                    biayaperawatan=biayaperawatan+rs3.getDouble("biaya");

                                                    try {
                                                        rs5=koneksi.prepareStatement(
                                                            "select template_laboratorium.Pemeriksaan, detail_periksa_lab.nilai,"+
                                                            "template_laboratorium.satuan,detail_periksa_lab.nilai_rujukan,detail_periksa_lab.biaya_item,"+
                                                            "detail_periksa_lab.keterangan from detail_periksa_lab inner join "+
                                                            "template_laboratorium on detail_periksa_lab.id_template=template_laboratorium.id_template "+
                                                            "where detail_periksa_lab.no_rawat='"+rs.getString("no_rawat")+"' and "+
                                                            "detail_periksa_lab.kd_jenis_prw='"+rs3.getString("kd_jenis_prw")+"' and "+
                                                            "detail_periksa_lab.tgl_periksa='"+rs4.getString("tgl_periksa")+"' and "+
                                                            "detail_periksa_lab.jam='"+rs4.getString("jam")+"' order by detail_periksa_lab.kd_jenis_prw,template_laboratorium.urut ").executeQuery();
                                                        if(rs5.next()){ 
                                                            rs5.beforeFirst();
                                                            while(rs5.next()){
                                                                htmlContent.append(
                                                                    "<tr class='sep'>"+
                                                                    "<td valign='top' border='0' width='2%'></td>"+
                                                                    "<td valign='top' border='0' width='24%'></td>"+
                                                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                                                    "<td valign='top' border='0' width='49%' align='left'>"+rs5.getString("Pemeriksaan")+"</td>"+
                                                                    "<td valign='top' border='0' width='4%'>1.0</td>"+
                                                                    "<td valign='top' border='0' width='20%' align='right'>"+Valid.SetAngka(rs5.getDouble("biaya_item"))+"</td>"+
                                                                    "</tr>"
                                                                ); 
                                                                biayaperawatan=biayaperawatan+rs5.getDouble("biaya_item");
                                                            }                                               
                                                        }
                                                    } catch (Exception e) {
                                                        System.out.println("Notifikasi Detail Lab: "+e);
                                                    } finally{
                                                        if(rs5!=null){
                                                            rs5.close();
                                                        }
                                                    }
                                                    s++;
                                                }
  
                                                w++;
                                            } catch (Exception e) {
                                                System.out.println("Notifikasi Lab : "+e);
                                            } finally{
                                                if(rs3!=null){
                                                    rs3.close();
                                                }
                                            }
                                        }
//                                        htmlContent.append(
//                                                      "</table>");
                                    }
                                } catch (Exception e) {
                                    System.out.println("Notif Laboratorium: "+e);
                                } finally{
                                    if(rs4!=null){
                                        rs4.close();
                                    }
                                }
                                
                                htmlContent.append(       
                                 
                                      "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>Obat & BHP</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='49%' align='left'></td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='left'></td>"+
                                 "</tr>"
                                );
                                
                                
                                 // Obat & BHP 
                                try{
                                    rs3=koneksi.prepareStatement(
                                            "select detail_pemberian_obat.tgl_perawatan,detail_pemberian_obat.jam,databarang.kode_sat, "+
                                            "detail_pemberian_obat.kode_brng,detail_pemberian_obat.jml,detail_pemberian_obat.total,"+
                                            "databarang.nama_brng from detail_pemberian_obat inner join databarang "+
                                            "on detail_pemberian_obat.kode_brng=databarang.kode_brng "+
                                            "where detail_pemberian_obat.no_rawat='"+rs.getString("no_rawat")+"' order by detail_pemberian_obat.tgl_perawatan,detail_pemberian_obat.jam").executeQuery();
                                    if(rs3.next()){                                    
                                        rs3.beforeFirst();
                                        w=1;
                                        while(rs3.next()){
                                            htmlContent.append(
                                                    "<tr class='sep'>"+
                                                    "<td valign='top' border='0' width='2%'></td>"+
                                                    "<td valign='top' border='0' width='24%'></td>"+
                                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                                    "<td valign='top' border='0' width='49%' align='left'>"+rs3.getString("nama_brng")+"</td>"+
                                                    "<td valign='top' border='0' width='4%'>"+rs3.getDouble("jml")+"</td>"+
                                                    "<td valign='top' border='0' width='20%' align='right'>"+Valid.SetAngka(rs3.getDouble("total"))+"</td>"+
                                                    "</tr>"
                                                 ); 
                                            w++;
                                            biayaperawatan=biayaperawatan+rs3.getDouble("total");
                                        }
                                       
                                    }                                
                                } catch (Exception e) {
                                    System.out.println("Notifikasi Obat & BHP: "+e);
                                } finally{
                                    if(rs3!=null){
                                        rs3.close();
                                    }
                                }
                                
                                
                                 // Potongan Biaya 
                                try{
                                    rs3=koneksi.prepareStatement(
                                           "select nama_pengurangan, (-1*besar_pengurangan) as besar_pengurangan from pengurangan_biaya where no_rawat='"+rs.getString("no_rawat")+"' order by nama_pengurangan").executeQuery();
                                    if(rs3.next()){                                    
                                        rs3.beforeFirst();
                                        w=1;
                                        while(rs3.next()){
                                            htmlContent.append(
                                                    "<tr class='sep'>"+
                                                    "<td valign='top' border='0' width='2%'></td>"+
                                                    "<td valign='top' border='0' width='24%'>Potongan Biaya</td>"+
                                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                                    "<td valign='top' border='0' width='49%' align='left'>"+rs3.getString("nama_pengurangan")+"</td>"+
                                                    "<td valign='top' border='0' width='4%'>1.0</td>"+
                                                    "<td valign='top' border='0' width='20%' align='right'>"+Valid.SetAngka(rs3.getDouble("besar_pengurangan"))+"</td>"+
                                                    "</tr>"
                                                 ); 
                                            w++;
                                            biayaperawatan=biayaperawatan+rs3.getDouble("besar_pengurangan");
                                        }
                                       
                                    }                                
                                } catch (Exception e) {
                                    System.out.println("Notifikasi Potongan Biaya: "+e);
                                } finally{
                                    if(rs3!=null){
                                        rs3.close();
                                    }
                                }
                                
                                
                                 // Tambahan Biaya
                                try{
                                    rs3=koneksi.prepareStatement(
                                            "select nama_biaya, besar_biaya from tambahan_biaya where no_rawat='"+rs.getString("no_rawat")+"' order by nama_biaya").executeQuery();
                                    if(rs3.next()){                                    
                                        rs3.beforeFirst();
                                        w=1;
                                        while(rs3.next()){
                                            htmlContent.append(
                                                    "<tr class='sep'>"+
                                                    "<td valign='top' border='0' width='2%'></td>"+
                                                    "<td valign='top' border='0' width='24%'>Tambahan Biaya</td>"+
                                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                                    "<td valign='top' border='0' width='49%' align='left'>"+rs3.getString("nama_biaya")+"</td>"+
                                                    "<td valign='top' border='0' width='4%'>1.0</td>"+
                                                    "<td valign='top' border='0' width='20%' align='right'>"+Valid.SetAngka(rs3.getDouble("besar_biaya"))+"</td>"+
                                                    "</tr>"
                                                 ); 
                                            w++;
                                            biayaperawatan=biayaperawatan+rs3.getDouble("besar_biaya");
                                        }
                                       
                                    }                                
                                } catch (Exception e) {
                                    System.out.println("Notifikasi Tambahan Biaya: "+e);
                                } finally{
                                    if(rs3!=null){
                                        rs3.close();
                                    }
                                }
                                
                          
                                // TOTAL TAGIHAN
                                htmlContent.append(
                                    "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='49%' align='left'></td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='right' ></td>"+
                                    "</tr>"+
                                    "<tr class='sep-bold'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>TOTAL TAGIHAN</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='49%' align='left'></td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='right'>"+Valid.SetAngka(biayaperawatan)+"</td>"+
                                    "</tr>"+
                                    "<tr class='sep-bold'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>PPN</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='49%' align='left'></td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='right'>0</td>"+
                                    "</tr>"+
                                    "<tr class='sep-bold'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>TAGIHAN + PPN</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='49%' align='left'></td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='right'>"+Valid.SetAngka(biayaperawatan)+"</td>"+
                                    "</tr>"+
                                    "<tr class='sep-bold'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>EKSES</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='49%' align='left'></td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='right'>0</td>"+
                                    "</tr>"+
                                    "<tr class='sep-bold'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>SISA PIUTANG</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='49%' align='left'></td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='right'>"+Valid.SetAngka(biayaperawatan)+"</td>"+
                                    "</tr>"
                                            
                                 ); 
                                
//                               
                                 htmlContent.append(       
                                 
                                "</table>" +
                                
                                            
                                 "<table width='100%' border='0' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                 "<tr>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td class='sep_note' valign='top' border='0' style='font-size:6px;' width='58%'>"+
                                    "</td>"+
                                    "<td valign='top' border='0' style='font-size:10px;' width='10%'>"+
                                    "<td class='ttd' valign='top' border='0' style='font-size:10px;' width='30%'>"+
                                    "<div> "+
                                    "<p> Semarang, " +rs2.getString("tanggal")+" "+rs2.getString("jam")+"</p>"+
                                    "<p> Tanda Verifikasi </p>"+
                                    "<img width='70' height='70' src='http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/penggajian/temp/001.png'/>"+
                                    "<p>( Admin Kasir )</p>"+
                                    "</div>"+
                                    "</td>"+
                                 "</tr>"+
                                "</table>" +
                                "<table width='100%' border='0' align='center' height='30' cellspacing='40' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" +
                                 "</table>" +
                                 "<table width='100%' border='0' align='center' height='30' cellspacing='40' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" +
                                 "</table>" +
                                 "<table width='100%' border='0' align='center' height='30' cellspacing='40' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" +
                                 "</table>" +
                                 "<table width='100%' border='0' align='center' height='30' cellspacing='40' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" +
                                 "</table>" +
                                "<table width='100%' border='0' style='border-bottom:3px solid #87b5ed;' align='center' height='30' cellspacing='50' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>"  
                            );                                     
                            w++;
                        }
                        htmlContent.append(
                             "</table>" +
                            "</td>"+
                          "</tr>");
                    }
                } catch (Exception e) {
                    System.out.println("Notifikasi : "+e);
                } finally{
                    if(rs2!=null){
                        rs2.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Billing : "+e);
        }
    }

    private void menampilkanBillingRanap(String norawat) {
        try {
            if(chkBillingRanap.isSelected()==true){
                try {
                    rs2=koneksi.prepareStatement(
                        "SELECT nota_jalan.no_nota,poliklinik.nm_poli,nota_jalan.tanggal,nota_jalan.jam,reg_periksa.no_rkm_medis,pasien.nm_pasien,reg_periksa.almt_pj,dokter.nm_dokter,reg_periksa.biaya_reg "+
                        "FROM reg_periksa INNER JOIN nota_jalan ON reg_periksa.no_rawat = nota_jalan.no_rawat INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis INNER JOIN dokter ON dokter.kd_dokter = reg_periksa.kd_dokter "+
                        "INNER JOIN poliklinik ON reg_periksa.kd_poli = poliklinik.kd_poli WHERE reg_periksa.no_rawat='"+norawat+"'").executeQuery();
                    if(rs2.next()){
                        htmlContent.append(
                          "<tr class='isi'>"+ 
                            "<td valign='top' width='2%'></td>"+        
                            "<td valign='top' width='98%'>"+   
                              "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='5' class='tbl_form'>"+
                                  "<tr class='isi' padding='0'>"+
                                    "<td colspan='7' padding='0'>"+
                                        "<table width='100%' bgcolor='#ffffff' align='left' style='border-bottom: 2px solid;' class='tbl_form' cellspacing='0' cellpadding='0'>"+
                                            "<tr>"+
                                                "<td  width='5%'>"+
                                                    "<img width='45' height='45' src='http://192.168.1.236/webapp/berkasrawat/pages/upload/logo.png' style='margin-left:50px;'/>"+
                                                "</td>"+
                                                "<td width='75%'>"+
                                                    "<center>"+
                                                        "<font color='000000' size='3'  face='Tahoma'>RUMAH SAKIT UMUM BANYUMANIK 2</font><br>"+
                                                        "<font color='000000' size='1'  face='Tahoma'>"+
                                                            "Jl. Perintis Kemerdekaan no.57, Banyumanik, Kota Semarang, Jawa Tengah<br>"+
                                                            "(024) 74665525, email: rsubanyumanik2@gmail.com <br>"+
                                                        "</font> "+
                                                    "</center>"+
                                                "</td>"+
                                                "<td  width='20%'>"+
                                                   
                                                "</td>"+
                                            "</tr>"+
                                        "</table>"+
                                    "</td>"+
                                    "</tr>"
                        );
                        rs2.beforeFirst();
                        w=1;
                        while(rs2.next()){
                            
                            htmlContent.append(
                                 
                              "<tr class='sepjudul' cellspacing='5'>"+
                                  
                                    "<td valign='top' border='0' align='center' width='80%' style='font-size:12px;' id='title'>BILLING</td>"+
                            
                                 "</tr>"+
                              "</table>" +
                              "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>No.Nota </td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='49%' align='left'>"+rs2.getString("no_nota")+"</td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='left'></td>"+
                                 "</tr>"+
                                      "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>Unit/Instalasi </td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='49%' align='left'>"+rs2.getString("nm_poli")+"</td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='left'></td>"+
                                 "</tr>"+
                                      "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>Tanggal & Jam </td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='49%' align='left'>"+rs2.getString("tanggal")+" "+rs2.getString("jam")+"</td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='left'></td>"+
                                 "</tr>"+
                                      "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>No.RM </td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='49%' align='left'>"+rs2.getString("no_rkm_medis")+"</td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='left'></td>"+
                                 "</tr>"+
                                      "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>Nama Pasien</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='49%' align='left'>"+rs2.getString("nm_pasien")+"</td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='left'></td>"+
                                 "</tr>"+
                                      "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>Alamat Pasien</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='49%' align='left'>"+rs2.getString("almt_pj")+"</td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='left'></td>"+
                                 "</tr>"+
                                      "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>Dokter </td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='49%' align='left'>"+rs2.getString("nm_dokter")+"</td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='left'></td>"+
                                 "</tr>"+
                                "</table>" +
                                            
                                "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>Administrasi Rekam Medis </td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='49%' align='left'></td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='right'>"+rs2.getString("biaya_reg")+"</td>"+
                                 "</tr>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>Tindakan</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='49%' align='left'></td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='right'></td>"+
                                 "</tr>"  
                                    );
                            
                                    biayaperawatan=rs.getDouble("biaya_reg");
                                 //tindakan dr
                                    try{
                                    rs3=koneksi.prepareStatement(
                                            "select rawat_jl_dr.kd_jenis_prw,jns_perawatan.nm_perawatan,dokter.nm_dokter,rawat_jl_dr.biaya_rawat, "+
                                            "rawat_jl_dr.tgl_perawatan,rawat_jl_dr.jam_rawat from rawat_jl_dr inner join jns_perawatan on rawat_jl_dr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                                            "inner join dokter on rawat_jl_dr.kd_dokter=dokter.kd_dokter where rawat_jl_dr.no_rawat='"+rs.getString("no_rawat")+"' order by rawat_jl_dr.tgl_perawatan,rawat_jl_dr.jam_rawat").executeQuery();
                                    if(rs3.next()){                                    
                                        rs3.beforeFirst();
                                        w=1;
                                        while(rs3.next()){
                                            htmlContent.append(
                                                    "<tr class='sep'>"+
                                                    "<td valign='top' border='0' width='2%'></td>"+
                                                    "<td valign='top' border='0' width='24%'></td>"+
                                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                                    "<td valign='top' border='0' width='49%' align='left'>"+rs3.getString("nm_perawatan")+"</td>"+
                                                    "<td valign='top' border='0' width='4%'>1.0</td>"+
                                                    "<td valign='top' border='0' width='20%' align='right'>"+Valid.SetAngka(rs3.getDouble("biaya_rawat"))+"</td>"+
                                                    "</tr>"
                                                 ); 
                                            w++;
                                            biayaperawatan=biayaperawatan+rs3.getDouble("biaya_rawat");
                                        }
                                       
                                    }                                
                                } catch (Exception e) {
                                    System.out.println("Notifikasi tindakan: "+e);
                                } finally{
                                    if(rs3!=null){
                                        rs3.close();
                                    }
                                }
                                   
                                //tindakan perawat   
                                try{
                                    rs3=koneksi.prepareStatement(
                                    "select rawat_jl_pr.kd_jenis_prw,jns_perawatan.nm_perawatan,petugas.nama,rawat_jl_pr.biaya_rawat, "+
                                    "rawat_jl_pr.tgl_perawatan,rawat_jl_pr.jam_rawat from rawat_jl_pr inner join jns_perawatan on rawat_jl_pr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                                    "inner join petugas on rawat_jl_pr.nip=petugas.nip where rawat_jl_pr.no_rawat='"+rs.getString("no_rawat")+"' order by rawat_jl_pr.tgl_perawatan,rawat_jl_pr.jam_rawat").executeQuery();
                                    if(rs3.next()){                                    
                                        rs3.beforeFirst();
                                        w=1;
                                        while(rs3.next()){
                                            htmlContent.append(
                                                    "<tr class='sep'>"+
                                                    "<td valign='top' border='0' width='2%'></td>"+
                                                    "<td valign='top' border='0' width='24%'></td>"+
                                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                                    "<td valign='top' border='0' width='49%' align='left'>"+rs3.getString("nm_perawatan")+"</td>"+
                                                    "<td valign='top' border='0' width='4%'>1.0</td>"+
                                                    "<td valign='top' border='0' width='20%' align='right'>"+Valid.SetAngka(rs3.getDouble("biaya_rawat"))+"</td>"+
                                                    "</tr>"
                                                 ); 
                                            w++;
                                            biayaperawatan=biayaperawatan+rs3.getDouble("biaya_rawat");
                                        }
                                       
                                    }                                
                                } catch (Exception e) {
                                    System.out.println("Notifikasi tindakan pr: "+e);
                                } finally{
                                    if(rs3!=null){
                                        rs3.close();
                                    }
                                }
                                
                                //tindakan dr + perawat   
                                try{
                                    rs3=koneksi.prepareStatement(
                                    "select rawat_jl_drpr.kd_jenis_prw,jns_perawatan.nm_perawatan,dokter.nm_dokter,petugas.nama,rawat_jl_drpr.biaya_rawat, "+
                                    "rawat_jl_drpr.tgl_perawatan,rawat_jl_drpr.jam_rawat from rawat_jl_drpr inner join jns_perawatan on rawat_jl_drpr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                                    "inner join dokter on rawat_jl_drpr.kd_dokter=dokter.kd_dokter inner join petugas on rawat_jl_drpr.nip=petugas.nip "+
                                    "where rawat_jl_drpr.no_rawat='"+rs.getString("no_rawat")+"' order by rawat_jl_drpr.tgl_perawatan,rawat_jl_drpr.jam_rawat").executeQuery();
                                    if(rs3.next()){                                    
                                        rs3.beforeFirst();
                                        w=1;
                                        while(rs3.next()){
                                            htmlContent.append(
                                                    "<tr class='sep'>"+
                                                    "<td valign='top' border='0' width='2%'></td>"+
                                                    "<td valign='top' border='0' width='24%'></td>"+
                                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                                    "<td valign='top' border='0' width='49%' align='left'>"+rs3.getString("nm_perawatan")+"</td>"+
                                                    "<td valign='top' border='0' width='4%'>1.0</td>"+
                                                    "<td valign='top' border='0' width='20%' align='right'>"+Valid.SetAngka(rs3.getDouble("biaya_rawat"))+"</td>"+
                                                    "</tr>"
                                                 ); 
                                            w++;
                                            biayaperawatan=biayaperawatan+rs3.getDouble("biaya_rawat");
                                        }
                                       
                                    }                                
                                } catch (Exception e) {
                                    System.out.println("Notifikasi tindakan dr pr: "+e);
                                } finally{
                                    if(rs3!=null){
                                        rs3.close();
                                    }
                                }
                                
                                //operasi + vk  
                                try{
                                    rs3=koneksi.prepareStatement(
                                   "select operasi.tgl_operasi,operasi.jenis_anasthesi,operasi.operator1, operasi.operator2, operasi.operator3, operasi.asisten_operator1,"+
                                    "operasi.asisten_operator2,operasi.asisten_operator3,operasi.biayaasisten_operator3, operasi.instrumen, operasi.dokter_anak, operasi.perawaat_resusitas, "+
                                    "operasi.dokter_anestesi, operasi.asisten_anestesi, operasi.asisten_anestesi2,operasi.asisten_anestesi2, operasi.bidan, operasi.bidan2, operasi.bidan3, operasi.perawat_luar, operasi.omloop,"+
                                    "operasi.omloop2,operasi.omloop3,operasi.omloop4,operasi.omloop5,operasi.dokter_pjanak,operasi.dokter_umum, "+
                                    "operasi.kode_paket,paket_operasi.nm_perawatan, operasi.biayaoperator1, operasi.biayaoperator2, operasi.biayaoperator3, "+
                                    "operasi.biayaasisten_operator1, operasi.biayaasisten_operator2, operasi.biayaasisten_operator3, operasi.biayainstrumen, "+
                                    "operasi.biayadokter_anak, operasi.biayaperawaat_resusitas, operasi.biayadokter_anestesi, "+
                                    "operasi.biayaasisten_anestesi,operasi.biayaasisten_anestesi2, operasi.biayabidan,operasi.biayabidan2,operasi.biayabidan3, operasi.biayaperawat_luar, operasi.biayaalat,"+
                                    "operasi.biayasewaok,operasi.akomodasi,operasi.bagian_rs,operasi.biaya_omloop,operasi.biaya_omloop2,operasi.biaya_omloop3,operasi.biaya_omloop4,operasi.biaya_omloop5,"+
                                    "operasi.biayasarpras,operasi.biaya_dokter_pjanak,operasi.biaya_dokter_umum,"+
                                    "(operasi.biayaoperator1+operasi.biayaoperator2+operasi.biayaoperator3+"+
                                    "operasi.biayaasisten_operator1+operasi.biayaasisten_operator2+operasi.biayaasisten_operator3+operasi.biayainstrumen+"+
                                    "operasi.biayadokter_anak+operasi.biayaperawaat_resusitas+operasi.biayadokter_anestesi+"+
                                    "operasi.biayaasisten_anestesi+operasi.biayaasisten_anestesi2+operasi.biayabidan+operasi.biayabidan2+operasi.biayabidan3+operasi.biayaperawat_luar+operasi.biayaalat+"+
                                    "operasi.biayasewaok+operasi.akomodasi+operasi.bagian_rs+operasi.biaya_omloop+operasi.biaya_omloop2+operasi.biaya_omloop3+operasi.biaya_omloop4+operasi.biaya_omloop5+"+
                                    "operasi.biayasarpras+operasi.biaya_dokter_pjanak+operasi.biaya_dokter_umum) as total "+
                                    "from operasi inner join paket_operasi on operasi.kode_paket=paket_operasi.kode_paket "+
                                    "where operasi.no_rawat='"+rs.getString("no_rawat")+"' order by operasi.tgl_operasi").executeQuery();
                                    if(rs3.next()){                                    
                                        rs3.beforeFirst();
                                        w=1;
                                        while(rs3.next()){
                                            htmlContent.append(
                                                    "<tr class='sep'>"+
                                                    "<td valign='top' border='0' width='2%'></td>"+
                                                    "<td valign='top' border='0' width='24%'></td>"+
                                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                                    "<td valign='top' border='0' width='49%' align='left'>"+rs3.getString("nm_perawatan")+"</td>"+
                                                    "<td valign='top' border='0' width='4%'>1.0</td>"+
                                                    "<td valign='top' border='0' width='20%' align='right'>"+Valid.SetAngka(rs3.getDouble("total"))+"</td>"+
                                                    "</tr>"
                                                 ); 
                                            w++;
                                            biayaperawatan=biayaperawatan+rs3.getDouble("total");
                                        }
                                       
                                    }                                
                                } catch (Exception e) {
                                    System.out.println("Notifikasi OK VK: "+e);
                                } finally{
                                    if(rs3!=null){
                                        rs3.close();
                                    }
                                }

                                 //Radologi  
                                try{
                                    rs3=koneksi.prepareStatement(
                                     "select periksa_radiologi.tgl_periksa,periksa_radiologi.jam,periksa_radiologi.kd_jenis_prw, "+
                                        "jns_perawatan_radiologi.nm_perawatan,petugas.nama,periksa_radiologi.biaya,periksa_radiologi.dokter_perujuk,"+
                                        "dokter.nm_dokter,concat("+
                                       "if(periksa_radiologi.proyeksi<>'',concat('Proyeksi : ',periksa_radiologi.proyeksi,', '),''),"+
                                       "if(periksa_radiologi.kV<>'',concat('kV : ',periksa_radiologi.kV,', '),''),"+
                                       "if(periksa_radiologi.mAS<>'',concat('mAS : ',periksa_radiologi.mAS,', '),''),"+
                                       "if(periksa_radiologi.FFD<>'',concat('FFD : ',periksa_radiologi.FFD,', '),''),"+
                                       "if(periksa_radiologi.BSF<>'',concat('BSF : ',periksa_radiologi.BSF,', '),''),"+
                                       "if(periksa_radiologi.inak<>'',concat('Inak : ',periksa_radiologi.inak,', '),''),"+
                                       "if(periksa_radiologi.jml_penyinaran<>'',concat('Jml Penyinaran : ',periksa_radiologi.jml_penyinaran,', '),''),"+
                                       "if(periksa_radiologi.dosis<>'',concat('Dosis Radiasi : ',periksa_radiologi.dosis),'')) as proyeksi "+
                                        "from periksa_radiologi inner join jns_perawatan_radiologi on periksa_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "+
                                        "inner join petugas on periksa_radiologi.nip=petugas.nip inner join dokter on periksa_radiologi.kd_dokter=dokter.kd_dokter "+
                                        "where periksa_radiologi.no_rawat='"+rs.getString("no_rawat")+"' order by periksa_radiologi.tgl_periksa,periksa_radiologi.jam").executeQuery();
                                    if(rs3.next()){                                    
                                        rs3.beforeFirst();
                                        w=1;
                                        while(rs3.next()){
                                            htmlContent.append(
                                                    "<tr class='sep'>"+
                                                    "<td valign='top' border='0' width='2%'></td>"+
                                                    "<td valign='top' border='0' width='24%'></td>"+
                                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                                    "<td valign='top' border='0' width='49%' align='left'>"+rs3.getString("nm_perawatan")+"</td>"+
                                                    "<td valign='top' border='0' width='4%'>1.0</td>"+
                                                    "<td valign='top' border='0' width='20%' align='right'>"+Valid.SetAngka(rs3.getDouble("biaya"))+"</td>"+
                                                    "</tr>"
                                                 ); 
                                            w++;
                                            biayaperawatan=biayaperawatan+rs3.getDouble("biaya");
                                        }
                                       
                                    }                                
                                } catch (Exception e) {
                                    System.out.println("Notifikasi radiologi: "+e);
                                } finally{
                                    if(rs3!=null){
                                        rs3.close();
                                    }
                                }
                                
                                
                                 //Laboratorium
//                                
//                                try{
//                                    rs3=koneksi.prepareStatement(
//                                             "select periksa_lab.kd_jenis_prw, "+
//                                             "jns_perawatan_lab.nm_perawatan,petugas.nama,periksa_lab.biaya,periksa_lab.dokter_perujuk,dokter.nm_dokter "+
//                                             "from periksa_lab inner join jns_perawatan_lab on periksa_lab.kd_jenis_prw=jns_perawatan_lab.kd_jenis_prw "+
//                                             "inner join petugas on periksa_lab.nip=petugas.nip inner join dokter on periksa_lab.kd_dokter=dokter.kd_dokter "+
//                                             "where periksa_lab.kategori='PK' and periksa_lab.no_rawat='"+rs.getString("no_rawat")+"' "+
//                                             "and periksa_lab.tgl_periksa='"+rs4.getString("tgl_periksa")+"' and periksa_lab.jam='"+rs4.getString("jam")+"'").executeQuery();
//                                    if(rs3.next()){                                    
//                                        rs3.beforeFirst();
//                                        w=1;
//                                        while(rs3.next()){
//                                            htmlContent.append(
//                                                    "<tr class='sep'>"+
//                                                    "<td valign='top' border='0' width='2%'></td>"+
//                                                    "<td valign='top' border='0' width='24%'></td>"+
//                                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
//                                                    "<td valign='top' border='0' width='49%' align='left'>"+rs3.getString("nm_perawatan")+"</td>"+
//                                                    "<td valign='top' border='0' width='4%'>1.0</td>"+
//                                                    "<td valign='top' border='0' width='20%' align='right'>"+Valid.SetAngka(rs3.getDouble("biaya"))+"</td>"+
//                                                    "</tr>"
//                                                 ); 
//                                            w++;
//                                            biayaperawatan=biayaperawatan+rs3.getDouble("biaya");
//                                        }
//                                       
//                                    }                                
//                                } catch (Exception e) {
//                                    System.out.println("Notifikasi Laboratorium: "+e);
//                                } finally{
//                                    if(rs3!=null){
//                                        rs3.close();
//                                    }
//                                }
                                
                                
                                 // Detail Laboratorium  
                                try {
                                    rs4=koneksi.prepareStatement(
                                         "select periksa_lab.tgl_periksa,periksa_lab.jam from periksa_lab where periksa_lab.kategori='PK' and periksa_lab.no_rawat='"+rs.getString("no_rawat")+"' "+
                                         "group by concat(periksa_lab.no_rawat,periksa_lab.tgl_periksa,periksa_lab.jam) order by periksa_lab.tgl_periksa,periksa_lab.jam").executeQuery();
                                    if(rs4.next()){
                                        rs4.beforeFirst();
                                        w=1;
                                        while(rs4.next()){
                                            try{
                                                rs3=koneksi.prepareStatement(
                                                     "select periksa_lab.kd_jenis_prw, "+
                                                     "jns_perawatan_lab.nm_perawatan,petugas.nama,periksa_lab.biaya,periksa_lab.dokter_perujuk,dokter.nm_dokter "+
                                                     "from periksa_lab inner join jns_perawatan_lab on periksa_lab.kd_jenis_prw=jns_perawatan_lab.kd_jenis_prw "+
                                                     "inner join petugas on periksa_lab.nip=petugas.nip inner join dokter on periksa_lab.kd_dokter=dokter.kd_dokter "+
                                                     "where periksa_lab.kategori='PK' and periksa_lab.no_rawat='"+rs.getString("no_rawat")+"' "+
                                                     "and periksa_lab.tgl_periksa='"+rs4.getString("tgl_periksa")+"' and periksa_lab.jam='"+rs4.getString("jam")+"'").executeQuery();
                                                s=1;
                                                while(rs3.next()){
                                                    if(s==1){
                                                        htmlContent.append(
                                                            "<tr class='sep'>"+
                                                            "<td valign='top' border='0' width='2%'></td>"+
                                                            "<td valign='top' border='0' width='24%'></td>"+
                                                            "<td valign='top' border='0' width='1%' align='right'></td>"+
                                                            "<td valign='top' border='0' width='49%' align='left'>"+rs3.getString("nm_perawatan")+"</td>"+
                                                            "<td valign='top' border='0' width='4%'>1.0</td>"+
                                                            "<td valign='top' border='0' width='20%' align='right'>"+Valid.SetAngka(rs3.getDouble("biaya"))+"</td>"+
                                                            "</tr>"
                                                       ); 
                                                    }else{
                                                        htmlContent.append(
                                                            "<tr class='sep'>"+
                                                            "<td valign='top' border='0' width='2%'></td>"+
                                                            "<td valign='top' border='0' width='24%'></td>"+
                                                            "<td valign='top' border='0' width='1%' align='right'></td>"+
                                                            "<td valign='top' border='0' width='49%' align='left'>"+rs3.getString("nm_perawatan")+"</td>"+
                                                            "<td valign='top' border='0' width='4%'>1.0</td>"+
                                                            "<td valign='top' border='0' width='20%' align='right'>"+Valid.SetAngka(rs3.getDouble("biaya"))+"</td>"+
                                                            "</tr>"
                                                        ); 
                                                    }

                                                    biayaperawatan=biayaperawatan+rs3.getDouble("biaya");

                                                    try {
                                                        rs5=koneksi.prepareStatement(
                                                            "select template_laboratorium.Pemeriksaan, detail_periksa_lab.nilai,"+
                                                            "template_laboratorium.satuan,detail_periksa_lab.nilai_rujukan,detail_periksa_lab.biaya_item,"+
                                                            "detail_periksa_lab.keterangan from detail_periksa_lab inner join "+
                                                            "template_laboratorium on detail_periksa_lab.id_template=template_laboratorium.id_template "+
                                                            "where detail_periksa_lab.no_rawat='"+rs.getString("no_rawat")+"' and "+
                                                            "detail_periksa_lab.kd_jenis_prw='"+rs3.getString("kd_jenis_prw")+"' and "+
                                                            "detail_periksa_lab.tgl_periksa='"+rs4.getString("tgl_periksa")+"' and "+
                                                            "detail_periksa_lab.jam='"+rs4.getString("jam")+"' order by detail_periksa_lab.kd_jenis_prw,template_laboratorium.urut ").executeQuery();
                                                        if(rs5.next()){ 
                                                            rs5.beforeFirst();
                                                            while(rs5.next()){
                                                                htmlContent.append(
                                                                    "<tr class='sep'>"+
                                                                    "<td valign='top' border='0' width='2%'></td>"+
                                                                    "<td valign='top' border='0' width='24%'></td>"+
                                                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                                                    "<td valign='top' border='0' width='49%' align='left'>"+rs5.getString("Pemeriksaan")+"</td>"+
                                                                    "<td valign='top' border='0' width='4%'>1.0</td>"+
                                                                    "<td valign='top' border='0' width='20%' align='right'>"+Valid.SetAngka(rs5.getDouble("biaya_item"))+"</td>"+
                                                                    "</tr>"
                                                                ); 
                                                                biayaperawatan=biayaperawatan+rs5.getDouble("biaya_item");
                                                            }                                               
                                                        }
                                                    } catch (Exception e) {
                                                        System.out.println("Notifikasi Detail Lab: "+e);
                                                    } finally{
                                                        if(rs5!=null){
                                                            rs5.close();
                                                        }
                                                    }
                                                    s++;
                                                }
  
                                                w++;
                                            } catch (Exception e) {
                                                System.out.println("Notifikasi Lab : "+e);
                                            } finally{
                                                if(rs3!=null){
                                                    rs3.close();
                                                }
                                            }
                                        }
//                                        htmlContent.append(
//                                                      "</table>");
                                    }
                                } catch (Exception e) {
                                    System.out.println("Notif Laboratorium: "+e);
                                } finally{
                                    if(rs4!=null){
                                        rs4.close();
                                    }
                                }
                                
                                htmlContent.append(       
                                 
                                      "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>Obat & BHP</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                    "<td valign='top' border='0' width='49%' align='left'></td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='left'></td>"+
                                 "</tr>"
                                );
                                
                                
                                 // Obat & BHP 
                                try{
                                    rs3=koneksi.prepareStatement(
                                            "select detail_pemberian_obat.tgl_perawatan,detail_pemberian_obat.jam,databarang.kode_sat, "+
                                            "detail_pemberian_obat.kode_brng,detail_pemberian_obat.jml,detail_pemberian_obat.total,"+
                                            "databarang.nama_brng from detail_pemberian_obat inner join databarang "+
                                            "on detail_pemberian_obat.kode_brng=databarang.kode_brng "+
                                            "where detail_pemberian_obat.no_rawat='"+rs.getString("no_rawat")+"' order by detail_pemberian_obat.tgl_perawatan,detail_pemberian_obat.jam").executeQuery();
                                    if(rs3.next()){                                    
                                        rs3.beforeFirst();
                                        w=1;
                                        while(rs3.next()){
                                            htmlContent.append(
                                                    "<tr class='sep'>"+
                                                    "<td valign='top' border='0' width='2%'></td>"+
                                                    "<td valign='top' border='0' width='24%'></td>"+
                                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                                    "<td valign='top' border='0' width='49%' align='left'>"+rs3.getString("nama_brng")+"</td>"+
                                                    "<td valign='top' border='0' width='4%'>"+rs3.getDouble("jml")+"</td>"+
                                                    "<td valign='top' border='0' width='20%' align='right'>"+Valid.SetAngka(rs3.getDouble("total"))+"</td>"+
                                                    "</tr>"
                                                 ); 
                                            w++;
                                            biayaperawatan=biayaperawatan+rs3.getDouble("total");
                                        }
                                       
                                    }                                
                                } catch (Exception e) {
                                    System.out.println("Notifikasi Obat & BHP: "+e);
                                } finally{
                                    if(rs3!=null){
                                        rs3.close();
                                    }
                                }
                                
                                
                                 // Potongan Biaya 
                                try{
                                    rs3=koneksi.prepareStatement(
                                           "select nama_pengurangan, (-1*besar_pengurangan) as besar_pengurangan from pengurangan_biaya where no_rawat='"+rs.getString("no_rawat")+"' order by nama_pengurangan").executeQuery();
                                    if(rs3.next()){                                    
                                        rs3.beforeFirst();
                                        w=1;
                                        while(rs3.next()){
                                            htmlContent.append(
                                                    "<tr class='sep'>"+
                                                    "<td valign='top' border='0' width='2%'></td>"+
                                                    "<td valign='top' border='0' width='24%'>Potongan Biaya</td>"+
                                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                                    "<td valign='top' border='0' width='49%' align='left'>"+rs3.getString("nama_pengurangan")+"</td>"+
                                                    "<td valign='top' border='0' width='4%'>1.0</td>"+
                                                    "<td valign='top' border='0' width='20%' align='right'>"+Valid.SetAngka(rs3.getDouble("besar_pengurangan"))+"</td>"+
                                                    "</tr>"
                                                 ); 
                                            w++;
                                            biayaperawatan=biayaperawatan+rs3.getDouble("besar_pengurangan");
                                        }
                                       
                                    }                                
                                } catch (Exception e) {
                                    System.out.println("Notifikasi Potongan Biaya: "+e);
                                } finally{
                                    if(rs3!=null){
                                        rs3.close();
                                    }
                                }
                                
                                
                                 // Tambahan Biaya
                                try{
                                    rs3=koneksi.prepareStatement(
                                            "select nama_biaya, besar_biaya from tambahan_biaya where no_rawat='"+rs.getString("no_rawat")+"' order by nama_biaya").executeQuery();
                                    if(rs3.next()){                                    
                                        rs3.beforeFirst();
                                        w=1;
                                        while(rs3.next()){
                                            htmlContent.append(
                                                    "<tr class='sep'>"+
                                                    "<td valign='top' border='0' width='2%'></td>"+
                                                    "<td valign='top' border='0' width='24%'>Tambahan Biaya</td>"+
                                                    "<td valign='top' border='0' width='1%' align='right'>:</td>"+
                                                    "<td valign='top' border='0' width='49%' align='left'>"+rs3.getString("nama_biaya")+"</td>"+
                                                    "<td valign='top' border='0' width='4%'>1.0</td>"+
                                                    "<td valign='top' border='0' width='20%' align='right'>"+Valid.SetAngka(rs3.getDouble("besar_biaya"))+"</td>"+
                                                    "</tr>"
                                                 ); 
                                            w++;
                                            biayaperawatan=biayaperawatan+rs3.getDouble("besar_biaya");
                                        }
                                       
                                    }                                
                                } catch (Exception e) {
                                    System.out.println("Notifikasi Tambahan Biaya: "+e);
                                } finally{
                                    if(rs3!=null){
                                        rs3.close();
                                    }
                                }
                                
                          
                                // TOTAL TAGIHAN
                                htmlContent.append(
                                    "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='49%' align='left'></td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='right' ></td>"+
                                    "</tr>"+
                                    "<tr class='sep-bold'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>TOTAL TAGIHAN</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='49%' align='left'></td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='right'>"+Valid.SetAngka(biayaperawatan)+"</td>"+
                                    "</tr>"+
                                    "<tr class='sep-bold'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>PPN</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='49%' align='left'></td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='right'>0</td>"+
                                    "</tr>"+
                                    "<tr class='sep-bold'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>TAGIHAN + PPN</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='49%' align='left'></td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='right'>"+Valid.SetAngka(biayaperawatan)+"</td>"+
                                    "</tr>"+
                                    "<tr class='sep-bold'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>EKSES</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='49%' align='left'></td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='right'>0</td>"+
                                    "</tr>"+
                                    "<tr class='sep-bold'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='24%'>SISA PIUTANG</td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='49%' align='left'></td>"+
                                    "<td valign='top' border='0' width='4%'></td>"+
                                    "<td valign='top' border='0' width='20%' align='right'>"+Valid.SetAngka(biayaperawatan)+"</td>"+
                                    "</tr>"
                                            
                                 ); 
                                
//                               
                                 htmlContent.append(       
                                 
                                "</table>" +
                                
                                            
                                 "<table width='100%' border='0' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                 "<tr>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td class='sep_note' valign='top' border='0' style='font-size:6px;' width='58%'>"+
                                    "</td>"+
                                    "<td valign='top' border='0' style='font-size:10px;' width='10%'>"+
                                    "<td class='ttd' valign='top' border='0' style='font-size:10px;' width='30%'>"+
                                    "<div> "+
                                    "<p> Semarang, " +rs2.getString("tanggal")+" "+rs2.getString("jam")+"</p>"+
                                    "<p> Tanda Verifikasi </p>"+
                                    "<img width='70' height='70' src='http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/penggajian/temp/001.png'/>"+
                                    "<p>( Admin Kasir )</p>"+
                                    "</div>"+
                                    "</td>"+
                                 "</tr>"+
                                "</table>" +
                                "<table width='100%' border='0' align='center' height='30' cellspacing='40' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" +
                                 "</table>" +
                                 "<table width='100%' border='0' align='center' height='30' cellspacing='40' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" +
                                 "</table>" +
                                 "<table width='100%' border='0' align='center' height='30' cellspacing='40' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" +
                                 "</table>" +
                                 "<table width='100%' border='0' align='center' height='30' cellspacing='40' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>" +
                                 "</table>" +
                                "<table width='100%' border='0' style='border-bottom:3px solid #87b5ed;' align='center' height='30' cellspacing='50' class='tbl_form'>"+
                                 "<tr class='sep'>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                    "<td valign='top' border='0' width='14%'></td>"+
                                    "<td valign='top' border='0' width='1%' align='right'></td>"+
                                    "<td valign='top' border='0' width='36%' align='left'></td>"+
                                    "<td valign='top' border='0' width='2%'></td>"+
                                 "</tr>"  
                            );                                     
                            w++;
                        }
                        htmlContent.append(
                             "</table>" +
                            "</td>"+
                          "</tr>");
                    }
                } catch (Exception e) {
                    System.out.println("Notifikasi : "+e);
                } finally{
                    if(rs2!=null){
                        rs2.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Billing : "+e);
        }
    }
}

    
