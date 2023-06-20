package org.arobase.client;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.arobase.bd.ServiceBD;
import org.arobase.serveur.ServiceServeur;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.rmi.ConnectException;
import java.rmi.RemoteException;

public class ReservationHandler implements HttpHandler {

    private final ServiceServeur serviceServeur;

    public ReservationHandler(ServiceServeur serviceServeur) {
        this.serviceServeur = serviceServeur;
    }

    @Override
    public void handle(HttpExchange t) {

        new Thread(() -> {

            /**
             * FORMAT POUR LES DONNÉES
             * {
             *     "restaurant_id": 1,
             *     "name": "PERROT",
             *     "surname": "Alexandre",
             *     "guests": 3,
             *     "phoneNumber": "0625005194",
             *     "reservationTime": "2023-06-17",
             * }
             */

            System.out.println("Proxy > Requete pour /reservation recue");

            try {

                Headers headers = t.getResponseHeaders();
                headers.add("Access-Control-Allow-Origin", "*");
                headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
                headers.add("Access-Control-Allow-Headers", "Content-Type, Authorization");

                if (!t.getRequestMethod().equals("POST")) {
                    headers.add("Content-Type", "text/html");

                    String response = "<h1>You must use this URL with a POST method!</h1>";
                    t.sendResponseHeaders(200, response.length());
                    OutputStream os = t.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                    System.out.println("Proxy > Echec pour /reservation, requete faite en GET et non POST");
                    return;
                } else {
                    headers.add("Content-Type", "application/json");
                }

                InputStream is = t.getRequestBody();

                JSONObject jsonObject = inputStreamToJSON(is);
                ReservationData reservationData = ReservationData.fromJSON(jsonObject);

                boolean value = false;

                ServiceBD serviceBD = serviceServeur.getBD();

                if (serviceBD == null) {
                    System.out.println("Proxy > Requete pour /reservation annulee, service BD non disponible, valeur par defaut retournee");
                } else {
                    value = serviceBD.reserver(reservationData);
                }

                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("status", value);

                String response = jsonObject1.toJSONString();
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
                System.out.println("Proxy > Requete pour /reservation terminee");

            } catch (ConnectException e) {

                try {
                    System.out.println("Proxy > Requete pour /reservation annulee, service non disponible");
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

    public JSONObject inputStreamToJSON(InputStream is) throws IOException {

        int bufferSize = 1024;
        char[] buffer = new char[bufferSize];
        StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(is, StandardCharsets.UTF_8);
        for (int numRead; (numRead = in.read(buffer, 0, buffer.length)) > 0; ) {
            out.append(buffer, 0, numRead);
        }

        try {
            JSONParser parser = new JSONParser();
            return (JSONObject) parser.parse(out.toString());
        } catch (ParseException e) {
            System.err.println("JSON parsing request failed!");
            throw new RuntimeException(e);
        }

    }

}
