package org.arobase.client;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.arobase.serveur.ServiceBD;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DonneeHandler implements HttpHandler {

    private final ServiceBD serviceBD;

    public DonneeHandler(ServiceBD serviceBD) {
        this.serviceBD = serviceBD;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        InputStream is = t.getRequestBody();
        String response = serviceBD.getDonnees().toJSONString();
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
