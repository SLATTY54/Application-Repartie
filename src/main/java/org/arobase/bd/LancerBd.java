package org.arobase.bd;

import org.arobase.Service;

import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LancerBd implements Service {

    @Override
    public void demarrer(String[] args) {

        if (args.length < 2) {
            System.err.println("Usage: java LancerBd <host> <port>");
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

            DBConnection.initializeDatabase("perrot54u", "Alex54123*");
            Bd bd = new Bd();

            ServiceBD rd = (ServiceBD) UnicastRemoteObject.exportObject(bd, 0);

            registry.rebind("baseDeDonnee", rd);

        } catch (AccessException e) {
            throw new RuntimeException("Erreur d'accès à l'annuaire");
        } catch (RemoteException e) {
            throw new RuntimeException("Erreur de connexion à l'annuaire");
        }

    }

}
