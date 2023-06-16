package org.arobase.client;

import com.sun.net.httpserver.HttpServer;
import org.arobase.serveur.ServiceBD;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public static void main(String[] args) {

        if(args.length < 2){
            System.err.println("Usage: java Client <host> <port>");
            System.exit(1);
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        // annuaire
        Registry registry;

        try {
            registry = LocateRegistry.getRegistry(host, port);
        } catch (RemoteException e) {
            throw new RuntimeException("Erreur de connexion à l'annuaire");
        }

        try {
            ServiceBD serviceBD = (ServiceBD) registry.lookup("baseDeDonnee");

            HttpServer server = HttpServer.create(new InetSocketAddress(80), 0);
            server.createContext("/donnees", new DonneeHandler(serviceBD));
            server.createContext("/reserver", new ReserverHandler(serviceBD));
            server.setExecutor(null);
            server.start();

        } catch (AccessException e) {
            throw new RuntimeException("Erreur d'accès à l'annuaire");
        } catch (RemoteException e) {
            throw new RuntimeException("Erreur de connexion à l'annuaire");
        } catch (NotBoundException e) {
            throw new RuntimeException("Le service n'est pas connu dans l'annuaire");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
