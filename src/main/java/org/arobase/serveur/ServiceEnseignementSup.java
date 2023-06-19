package org.arobase.serveur;

import org.json.simple.JSONObject;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServiceEnseignementSup extends Remote {

    String getEnseignementsSup() throws RemoteException;


}
