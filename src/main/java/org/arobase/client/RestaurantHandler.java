package org.arobase.client;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.arobase.bd.ServiceBD;

import java.io.IOException;
import java.io.OutputStream;

public class RestaurantHandler implements HttpHandler {

    private final ServiceBD serviceBD;

    public RestaurantHandler(ServiceBD serviceBD) {
        this.serviceBD = serviceBD;
    }

    @Override
    public void handle(HttpExchange t){

        new Thread(() -> {

            try {

                // Set CORS headers
                Headers headers = t.getResponseHeaders();
                headers.add("Access-Control-Allow-Origin", "*");
                headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
                headers.add("Access-Control-Allow-Headers", "Content-Type, Authorization");
                headers.add("Content-Type", "application/json");

                System.out.println("TEST");
                String response = serviceBD.getRestaurants().toJSONString();
                System.out.println("MORT");
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();

            } catch (IOException e){
                e.printStackTrace();
            }

        }).start();
    }

}
