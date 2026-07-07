/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.stereotype.Service
 */
package com.zenithe.boost.sms.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.springframework.stereotype.Service;
import sun.net.www.protocol.https.Handler;

@Service
public class WebServices {
    private URL url;
    private int Cnx;
    private HttpURLConnection server;
    private String reponse;
    private InputStream reponses;
    private String[] retour;
    private SSLContext ctx;

    public int connect(String method) throws Exception {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HostnameVerifier allHostsValid = new HostnameVerifier(){

                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            this.server = (HttpURLConnection)this.url.openConnection();
            this.server.setRequestMethod(method);
            this.server.connect();
            return 1;
        }
        catch (IOException e) {
            System.err.println(e.toString());
            System.err.println("erreur de connection au service biometrie");
            return -1;
        }
    }

    public String displayResponse() throws Exception {
        String b = "";
        try {
            BufferedReader s = new BufferedReader(new InputStreamReader(this.server.getInputStream()));
            String url = this.server.getURL().toString();
            b = s.readLine();
            s.close();
            return b;
        }
        catch (Exception e) {
            System.err.println(e.toString());
            System.err.println("Code " + e.getLocalizedMessage());
            System.err.println("Impossible de lire la reponse de la passerelle 1");
            return "NOK";
        }
    }

    public InputStream displayResponses() throws Exception {
        String b = "";
        try {
            return this.server.getInputStream();
        }
        catch (Exception e) {
            System.err.println(e.toString());
            System.err.println("Impossible de lire la reponse de la passerelle 1");
            return null;
        }
    }

    public String urlBiometrie(String hostname) {
        try {
            this.url = new URL(hostname);
            this.Cnx = this.connect("GET");
            if (this.Cnx == 1) {
                this.reponse = this.displayResponse();
                return this.reponse;
            }
            return "echec";
        }
        catch (Exception exception) {
            return this.reponse;
        }
    }

    public InputStream urlBiometries(String hostname) {
        try {
            this.url = new URL(null, hostname, new Handler());
            this.Cnx = this.connect("GET");
            if (this.Cnx == 1) {
                this.reponses = this.displayResponses();
                return this.reponses;
            }
            return null;
        }
        catch (Exception exception) {
            return this.reponses;
        }
    }
}
