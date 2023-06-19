package org.arobase.serveur;

import org.arobase.Service;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;

public class LancerServeur implements Service {

    @Override
    public void demarrer(String[] args) {

        try {
            DBConnection.initializeDatabase("kizyow", "kizyow");
            Bd bd = new Bd();
            EnseignementSup enseignementSup = new EnseignementSup();

            ServiceBD rd = (ServiceBD) UnicastRemoteObject.exportObject(bd, 0);
            ServiceEnseignementSup ens = (ServiceEnseignementSup) UnicastRemoteObject.exportObject(enseignementSup, 0);

            Registry registry = LocateRegistry.createRegistry(1099);

            registry.rebind("baseDeDonnee", rd);
            registry.rebind("enseignementSup", ens);

            System.out.println("Serveur lancé");

        } catch (RemoteException e) {
            System.out.println("Création de la référence distante échoué");
        }

    }
}
