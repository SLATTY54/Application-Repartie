package org.arobase.client;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.arobase.bd.ServiceBD;
import org.arobase.serveur.ServiceServeur;

import java.io.IOException;
import java.io.OutputStream;
import java.rmi.ConnectException;
import java.rmi.RemoteException;

public class RestaurantHandler implements HttpHandler {

    private final ServiceServeur serviceServeur;

    public RestaurantHandler(ServiceServeur serviceServeur) {
        this.serviceServeur = serviceServeur;
    }

    @Override
    public void handle(HttpExchange t) {

        new Thread(() -> {

            System.out.println("Proxy > Requete pour /restaurants recue");

            try {

                // Set CORS headers
                Headers headers = t.getResponseHeaders();
                headers.add("Access-Control-Allow-Origin", "*");
                headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
                headers.add("Access-Control-Allow-Headers", "Content-Type, Authorization");
                headers.add("Content-Type", "application/json");

                ServiceBD serviceBD = serviceServeur.getBD();

                if (serviceBD == null) {
                    System.out.println("Proxy > Requete pour /restaurants annulee, service BD non disponible");
                    return;
                }

                String response = serviceBD.getRestaurants().toJSONString();
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();

                System.out.println("Proxy > Requete pour /restaurants terminee");

            } catch (ConnectException e) {

                try {
                    System.out.println("Proxy > Requete pour /restaurants annulee, service non disponible");
                    serviceServeur.supprimerBD();
                } catch (RemoteException ex) {
                    try {
                        System.out.println("Proxy > Erreur lors de la suppression du service BD");
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
