package bridging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fungsi.akses;
import fungsi.koneksiDB;
import fungsi.sekuel;
import java.awt.HeadlessException;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Properties;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import javax.swing.JOptionPane;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class BridgingWA {
    private static final Properties prop = new Properties();
    private sekuel Sequel=new sekuel();
    private String Key,pass , url , token ,requestJson, urlApi = "" , sender = "" , number ="" , message = "" , reurn = "";
    private HttpHeaders headers ;
    private HttpEntity requestEntity;
    private ObjectMapper mapper = new ObjectMapper();
    private JsonNode root;
    private JsonNode nameNode;
    private JsonNode response;


    public String getHmac() {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashInBytes = md.digest(pass.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
            Key=sb.toString();
        } catch (Exception ex) {
            System.out.println("Notifikasi : "+ex);
        }
	return Key;
    }


    public RestTemplate getRest() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("SSL");
        javax.net.ssl.TrustManager[] trustManagers= {
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {return null;}
                public void checkServerTrusted(X509Certificate[] arg0, String arg1)throws CertificateException {}
                public void checkClientTrusted(X509Certificate[] arg0, String arg1)throws CertificateException {}
            }
        };
        sslContext.init(null,trustManagers , new SecureRandom());
        SSLSocketFactory sslFactory=new SSLSocketFactory(sslContext,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        Scheme scheme=new Scheme("https",443,sslFactory);
        HttpComponentsClientHttpRequestFactory factory=new HttpComponentsClientHttpRequestFactory();
        factory.getHttpClient().getConnectionManager().getSchemeRegistry().register(scheme);
        return new RestTemplate(factory);
    }

    public String sendWa(String no_rkm_medis, String nama, String tanggal, String poli, String tanggal_booking, String jam_booking, String no_reg){
        try {
            message = "Mengingatkan kembali kepada saudara "+nama+" dengan No.RM "+no_rkm_medis+", berdasarkan booking pada tanggal "+tanggal_booking+" "+jam_booking+" dengan tujuan pemeriksaan di "+poli+" pada tanggal "+tanggal+" agar bisa datang dengan nomor antrian "+no_reg+". Customer Service "+akses.getnamars();
            number = Sequel.cariIsi("SELECT no_tlp FROM pasien WHERE no_rkm_medis = "+no_rkm_medis);
            token = Sequel.cariIsi("SELECT value FROM mlite_settings WHERE module='wagateway' AND field = 'token'");
            urlApi = Sequel.cariIsi("SELECT value FROM mlite_settings WHERE module='wagateway' AND field = 'server'");
            sender = Sequel.cariIsi("SELECT value FROM mlite_settings WHERE module='wagateway' AND field = 'phonenumber'");
            url = urlApi+"/wagateway/kirimpesan";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
            map.add("type", "text");
            map.add("sender", sender);
            map.add("number", number);
            map.add("api_key", token);
            map.add("message", message);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

            ResponseEntity<String> response = new RestTemplate().postForEntity( url, request , String.class );
            root = mapper.readTree(response.getBody());
            System.out.println(root);
            if(root.path("status").asText().equals("true")){
                //JOptionPane.showMessageDialog(null,root.path("msg").asText());
                reurn = root.path("msg").asText();
            }else {
                JOptionPane.showMessageDialog(null,root.path("msg").asText());
            }
        } catch (Exception ex) {
            System.out.println("Notifikasi : "+ex);
            System.out.println(url);
            if(ex.toString().contains("UnknownHostException")){
                JOptionPane.showMessageDialog(null,"Koneksi ke server mLITE.id terputus...!");
            }
        }
        return token;
    }

    public String sendWaUTD(String no_rkm_medis, String nama, String tanggal){
        try {
            message = "Mengingatkan kepada saudara "+nama+", untuk jadwal donor darah pada tanggal "+tanggal+". Terima kasih. Customer Service "+akses.getnamars();
            number = Sequel.cariIsi("SELECT no_tlp FROM pasien WHERE no_rkm_medis = "+no_rkm_medis);
            token = "";
            urlApi = "http://192.168.0.2:10000";
            sender = "628115167345";
            url = urlApi+"/wagateway/kirimpesan";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
            map.add("type", "text");
            map.add("sender", sender);
            map.add("number", number);
            map.add("api_key", token);
            map.add("message", message);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

            ResponseEntity<String> response = new RestTemplate().postForEntity( url, request , String.class );
            root = mapper.readTree(response.getBody());
            System.out.println(root);
            if(root.path("status").asText().equals("true")){
                //JOptionPane.showMessageDialog(null,root.path("msg").asText());
                reurn = root.path("msg").asText();
            }else {
                JOptionPane.showMessageDialog(null,root.path("msg").asText());
            }
        } catch (Exception ex) {
            System.out.println("Notifikasi : "+ex);
            System.out.println(url);
            if(ex.toString().contains("UnknownHostException")){
                JOptionPane.showMessageDialog(null,"Koneksi ke server mLITE.id terputus...!");
            }
        }
        return token;
    }

}
