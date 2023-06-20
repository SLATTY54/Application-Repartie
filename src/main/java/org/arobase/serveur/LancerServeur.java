package org.arobase.serveur;

import org.arobase.Service;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LancerServeur implements Service {

    @Override
    public void demarrer(String[] args) {

        try {

            Registry registry = LocateRegistry.createRegistry(1099);

            System.out.println("Serveur lancé");

        } catch (RemoteException e) {
            System.out.println("Création de la référence distante échoué");
        }

    }
}
