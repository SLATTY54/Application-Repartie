package org.arobase.serveur;

import org.arobase.bd.ServiceBD;
import org.arobase.enseignements.ServiceEnseignementSup;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServiceServeur extends Remote {

    void enregistrerBD(ServiceBD serviceBD) throws RemoteException;

    ServiceBD getBD() throws RemoteException;

    void enregisterEnsSup(ServiceEnseignementSup serviceEnseignementSup) throws RemoteException;

    ServiceEnseignementSup getEnsSup() throws RemoteException;

}
