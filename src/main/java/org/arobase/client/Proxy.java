package org.arobase.client;

import com.sun.net.httpserver.HttpServer;
import org.arobase.serveur.ServiceBD;
import org.arobase.serveur.ServiceEnseignementSup;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Proxy {

    private final ServiceBD serviceBD;
    private final ServiceEnseignementSup ensSup;

    public Proxy(ServiceBD serviceBD, ServiceEnseignementSup ensSup) {
        this.serviceBD = serviceBD;
        this.ensSup = ensSup;
    }

    public void createHttpServer(int port) {

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/restaurants", new RestaurantHandler(serviceBD));
            server.createContext("/reservation", new ReservationHandler(serviceBD));
            server.createContext("/enseignements", new EnsSupHandler(ensSup));
            server.createContext("/ajouterResto", new AjouterRestoHandler(serviceBD));
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
