package org.arobase.serveur;

import org.arobase.Service;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LancerServeur implements Service {

    @Override
    public void demarrer(String[] args) {

        try {

            Serveur serveur = new Serveur();

            ServiceServeur serviceServeur = (ServiceServeur) UnicastRemoteObject.exportObject(serveur, 0);

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("serveur", serviceServeur);

            System.out.println("Serveur lancé");

        } catch (RemoteException e) {
            System.out.println("Création de la référence distante échoué");
        }

    }
}
