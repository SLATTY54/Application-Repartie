package org.arobase.client;

import org.arobase.Service;
import org.arobase.serveur.ServiceServeur;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LancerProxy implements Service {

    @Override
    public void demarrer(String[] args) {

        if (args.length < 3) {
            System.err.println("Usage: java -jar app-repartie proxy <host> <port> <http_port>");
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

            ServiceServeur serviceServeur = (ServiceServeur) registry.lookup("serveur");

            Proxy proxy = new Proxy(serviceServeur);
            proxy.createHttpServer(Integer.parseInt(args[2]));

            System.out.println("Proxy > Serveur HTTPS demarre sur " + args[2]);

        } catch (AccessException e) {
            throw new RuntimeException("Erreur d'accès à l'annuaire");
        } catch (RemoteException e) {
            throw new RuntimeException("Erreur de connexion à l'annuaire");
        } catch (NotBoundException e) {
            throw new RuntimeException("Le service n'est pas connu dans l'annuaire");
        }

    }

}
