package org.arobase.client;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.arobase.serveur.ServiceBD;
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

        try {

            Headers headers = t.getResponseHeaders();
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            headers.add("Access-Control-Allow-Headers", "Content-Type, Authorization");
            headers.add("Content-Type", "application/json");

            if(!t.getRequestMethod().equals("POST")){

                String response = "<h1>You must use this URL with a POST method!</h1>";
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
                return;
            }

            InputStream is = t.getRequestBody();

            JSONObject jsonObject = inputStreamToJSON(is);
            RestoData restoData = RestoData.fromJSON(jsonObject);
            System.out.println(restoData);

            boolean value = serviceBD.ajoutRestaurant(restoData);

            String response = String.valueOf(value);
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

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
