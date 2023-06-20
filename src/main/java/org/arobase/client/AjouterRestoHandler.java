package org.arobase.client;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.arobase.bd.ServiceBD;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class AjouterRestoHandler implements HttpHandler {

    private final ServiceBD serviceBD;

    public AjouterRestoHandler(ServiceBD serviceBD) {
        this.serviceBD = serviceBD;
    }

    @Override
    public void handle(HttpExchange t) {

        new Thread(() -> {

            /**
             * FORMAT POUR LES DONNÉES
             * {
             *     "name": "Resto",
             *     "address": "1 rue test",
             *     "nbPlace": 3,
             *     "latitude": "1.000000",
             *     "longitude": "1.000000",
             * }
             */

            System.out.println("Proxy > Requete pour /ajouterResto recue");

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
                    System.out.println("Proxy > Echec pour /ajouterResto, requete faite en GET et non POST");
                    return;
                } else {
                    headers.add("Content-Type", "application/json");
                }

                InputStream is = t.getRequestBody();

                JSONObject jsonObject = inputStreamToJSON(is);
                RestoData restoData = RestoData.fromJSON(jsonObject);
                System.out.println(restoData);

                int id = serviceBD.ajoutRestaurant(restoData);
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("restaurant_id", id);

                String response = jsonObject1.toJSONString();
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
                System.out.println("Proxy > Requete pour /ajouterResto recue");

            } catch (IOException e) {
                e.printStackTrace();
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
