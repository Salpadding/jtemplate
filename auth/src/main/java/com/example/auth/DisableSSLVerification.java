package com.example.auth;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class DisableSSLVerification {
    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException {
        // Create a TrustManager that trusts all certificates
        TrustManager[] trustAllCertificates = new TrustManager[]{
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    // No check
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    // No check
                }
            }
        };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCertificates, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Optionally set default hostname verifier to allow insecure hostnames
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

        // Your code to make SSL connections here...
    }
}
