package org.arobase.client;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import org.arobase.bd.ServiceBD;
import org.arobase.enseignements.ServiceEnseignementSup;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.*;
import java.security.cert.CertificateException;

public class Proxy {

    private final ServiceBD serviceBD;
    private final ServiceEnseignementSup ensSup;

    public Proxy(ServiceBD serviceBD, ServiceEnseignementSup ensSup) {
        this.serviceBD = serviceBD;
        this.ensSup = ensSup;
    }

    public void createHttpServer(int port) {

        try {
            HttpsServer server = HttpsServer.create(new InetSocketAddress(port), 0);
            SSLContext sslContext = SSLContext.getInstance("TLS");

            // initialise the keystore
            char[] password = "password".toCharArray();
            KeyStore ks = KeyStore.getInstance("JKS");
            FileInputStream fis = new FileInputStream("key.jks");
            ks.load(fis, password);

            // setup the key manager factory
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, password);

            // setup the trust manager factory
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ks);

            // setup the HTTPS context and parameters
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            server.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
                public void configure(HttpsParameters params) {
                    try {
                        // initialise the SSL context
                        SSLContext context = getSSLContext();
                        SSLEngine engine = context.createSSLEngine();
                        params.setNeedClientAuth(false);
                        params.setCipherSuites(engine.getEnabledCipherSuites());
                        params.setProtocols(engine.getEnabledProtocols());

                        // Set the SSL parameters
                        SSLParameters sslParameters = context.getSupportedSSLParameters();
                        params.setSSLParameters(sslParameters);

                    } catch (Exception ex) {
                        System.out.println("Failed to create HTTPS port");
                    }
                }
            });

            server.createContext("/restaurants", new RestaurantHandler(serviceBD));
            server.createContext("/reservation", new ReservationHandler(serviceBD));
            server.createContext("/enseignements", new EnsSupHandler(ensSup));
            server.createContext("/ajouterResto", new AjouterRestoHandler(serviceBD));
            server.setExecutor(null);
            server.start();
        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException |
                 KeyManagementException | UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        }


    }

}
