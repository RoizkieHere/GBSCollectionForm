package com.example.gbscollectionform;

import android.annotation.SuppressLint;

import com.android.volley.toolbox.HurlStack;

import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

public class CustomHurlStack extends HurlStack {

    @SuppressLint("CustomX509TrustManager")
    @Override
    protected HttpsURLConnection createConnection(java.net.URL url) throws java.io.IOException {
        HttpsURLConnection connection = (HttpsURLConnection) super.createConnection(url);
        try {
            // Disabling SSL verification
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new javax.net.ssl.TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        @SuppressLint("TrustAllX509TrustManager")
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }
                        @SuppressLint("TrustAllX509TrustManager")
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            }, new java.security.SecureRandom());
            connection.setSSLSocketFactory(sslContext.getSocketFactory());
            connection.setHostnameVerifier((hostname, session) -> true); // Bypass hostname verification
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}
