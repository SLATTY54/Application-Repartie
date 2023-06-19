package org.arobase.client;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.arobase.serveur.ServiceBD;

import java.io.IOException;
import java.io.OutputStream;

public class RestaurantHandler implements HttpHandler {

    private final ServiceBD serviceBD;

    public RestaurantHandler(ServiceBD serviceBD) {
        this.serviceBD = serviceBD;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        // Set CORS headers
        Headers headers = t.getResponseHeaders();
        headers.add("Access-Control-Allow-Origin", "http://localhost");
        headers.add("Access-Control-Allow-Methods", "GET");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Content-Type", "application/json");

        String response = serviceBD.getRestaurants().toJSONString();
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
