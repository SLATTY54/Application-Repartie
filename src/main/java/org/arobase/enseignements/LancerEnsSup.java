package org.arobase.enseignements;

import org.arobase.Service;

import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LancerEnsSup implements Service {

    @Override
    public void demarrer(String[] args) {

        if (args.length < 2) {
            System.err.println("Usage: java LancerEnsSup <host> <port>");
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

            EnseignementSup enseignementSup = new EnseignementSup();
            ServiceEnseignementSup ens = (ServiceEnseignementSup) UnicastRemoteObject.exportObject(enseignementSup, 0);

            registry.rebind("enseignementSup", ens);

        } catch (AccessException e) {
            throw new RuntimeException("Erreur d'accès à l'annuaire");
        } catch (RemoteException e) {
            throw new RuntimeException("Erreur de connexion à l'annuaire");
        }

    }

}
