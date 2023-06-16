package org.arobase.client;

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
        String response = serviceBD.getRestaurants().toJSONString();
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
