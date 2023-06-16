package org.arobase.client;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.arobase.serveur.ServiceBD;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ReserverHandler implements HttpHandler {

    private final ServiceBD serviceBD;

    public ReserverHandler(ServiceBD serviceBD) {
        this.serviceBD = serviceBD;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        InputStream is = t.getRequestBody();

        System.out.println(convert(is));
        String response = "This is the response";
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public String convert(InputStream is) throws IOException {

        int bufferSize = 1024;
        char[] buffer = new char[bufferSize];
        StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(is, StandardCharsets.UTF_8);
        for (int numRead; (numRead = in.read(buffer, 0, buffer.length)) > 0; ) {
            out.append(buffer, 0, numRead);
        }
        return out.toString();

    }

}
