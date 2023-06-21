package org.arobase.serveur;

import org.arobase.bd.ServiceBD;
import org.arobase.enseignements.ServiceEnseignementSup;

import java.rmi.RemoteException;

public class Serveur implements ServiceServeur {

    private ServiceBD serviceBD;
    private ServiceEnseignementSup serviceEnseignementSup;

    @Override
    public void enregistrerBD(ServiceBD serviceBD) throws RemoteException {
        System.out.println("Serveur > Enregistrement du service BDD");
        this.serviceBD = serviceBD;
    }

    @Override
    public void supprimerBD() throws RemoteException {
        System.out.println("Serveur > Suppression du service BDD");
        this.serviceBD = null;
    }

    @Override
    public ServiceBD getBD() throws RemoteException {
        return serviceBD;
    }

    @Override
    public void enregisterEnsSup(ServiceEnseignementSup serviceEnseignementSup) throws RemoteException {
        System.out.println("Serveur > Enregistrement du service enseignement superieur");
        this.serviceEnseignementSup = serviceEnseignementSup;
    }

    @Override
    public void supprimerEnsSup() throws RemoteException {
        System.out.println("Serveur > Suppression du service enseignement superieur");
        this.serviceEnseignementSup = null;
    }

    @Override
    public ServiceEnseignementSup getEnsSup() throws RemoteException {
        return serviceEnseignementSup;
    }

    @Override
    public boolean isAlive() throws RemoteException {
        return true;
    }

}
