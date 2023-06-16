package org.arobase.client;

import com.sun.net.httpserver.HttpServer;
import org.arobase.serveur.ServiceBD;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Proxy {

    private final ServiceBD serviceBD;

    public Proxy(ServiceBD serviceBD) {
        this.serviceBD = serviceBD;
    }

    public void createHttpServer(int port) {

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/restaurants", new RestaurantHandler(serviceBD));
            server.createContext("/reservation", new ReservationHandler(serviceBD));
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
