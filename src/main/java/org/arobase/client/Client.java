package org.arobase.client;

import org.arobase.Service;
import org.arobase.serveur.ServiceBD;
import org.arobase.serveur.ServiceEnseignementSup;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client implements Service {

    @Override
    public void demarrer(String[] args) {

        if (args.length < 2) {
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
            ServiceEnseignementSup ensSup = (ServiceEnseignementSup) registry.lookup("enseignementSup");

            Proxy proxy = new Proxy(serviceBD, ensSup);
            proxy.createHttpServer(80);
            System.out.println("Proxy lancé");

        } catch (AccessException e) {
            throw new RuntimeException("Erreur d'accès à l'annuaire");
        } catch (RemoteException e) {
            throw new RuntimeException("Erreur de connexion à l'annuaire");
        } catch (NotBoundException e) {
            throw new RuntimeException("Le service n'est pas connu dans l'annuaire");
        }


    }

}
