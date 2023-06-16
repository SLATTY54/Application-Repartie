package org.arobase.serveur;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;

public class LancerServeur {
    public static void main(String[] args) {

        try{
            DBConnection.initializeDatabase("", "perrot54u");
            Bd bd = new Bd();

            ServiceBD rd = (ServiceBd) UnicastRemoteObject.exportObject(bd, 0);

            Registry registry = LocateRegistry.createRegistry(1099);

            registry.rebind("baseDeDonnee", rd);

            System.out.println("Serveur lancé");

        }catch (RemoteException e){
            System.out.println("Création de la référence distante échouer");
        }catch (ServerNotActiveException ex) {
            System.out.println("Serveur non active");
        }

    }
}
