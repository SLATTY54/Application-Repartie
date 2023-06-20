package org.arobase.client;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.arobase.enseignements.ServiceEnseignementSup;
import org.arobase.serveur.ServiceServeur;

import java.io.*;
import java.rmi.ConnectException;
import java.rmi.RemoteException;

public class EnsSupHandler implements HttpHandler {

    private final ServiceServeur serviceServeur;

    public EnsSupHandler(ServiceServeur serviceServeur) {
        this.serviceServeur = serviceServeur;
    }

    @Override
    public void handle(HttpExchange t) {

        new Thread(() -> {

            System.out.println("Proxy > Requete pour /enseignements recue");

            try {
                // Set CORS headers
                Headers headers = t.getResponseHeaders();
                headers.add("Access-Control-Allow-Origin", "*");
                headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
                headers.add("Access-Control-Allow-Headers", "Content-Type, Authorization");
                headers.add("Content-Type", "application/json");

                ServiceEnseignementSup serviceEnseignementSup = serviceServeur.getEnsSup();

                if (serviceEnseignementSup == null) {
                    System.out.println("Proxy > Requete pour /enseignements annulee, service EnsSup non disponible");
                    return;
                }

                String response = serviceEnseignementSup.getEnseignementsSup();

                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
                System.out.println("Proxy > Requete pour /enseignements terminee");

            } catch (ConnectException e) {

                try {
                    System.out.println("Proxy > Requete pour /enseignements annulee, service non disponible");
                    serviceServeur.supprimerEnsSup();
                } catch (RemoteException ex) {
                    try {
                        System.out.println("Proxy > Erreur lors de la suppression du service EnsSup");
                        serviceServeur.isAlive();
                    } catch (RemoteException exc) {
                        System.out.println("Proxy > Le serveur est hors ligne, arret du client");
                        System.exit(1);
                    }

                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }).start();
    }

}
