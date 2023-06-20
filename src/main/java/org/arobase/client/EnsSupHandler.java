package org.arobase.client;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.arobase.serveur.ServiceEnseignementSup;

import java.io.*;

public class EnsSupHandler implements HttpHandler {

    private final ServiceEnseignementSup serviceEnseignementSup;

    public EnsSupHandler(ServiceEnseignementSup serviceEnseignementSup) {
        this.serviceEnseignementSup = serviceEnseignementSup;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        // Set CORS headers
        Headers headers = t.getResponseHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "Content-Type, Authorization");
        headers.add("Content-Type", "application/json");

        String response = serviceEnseignementSup.getEnseignementsSup();

        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
