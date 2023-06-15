package org.arobase.serveur;

import org.json.simple.JSONObject;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Date;

public interface ServiceBD extends Remote {

    public JSONObject getDonnees() throws RemoteException;


    public void reserver(int IdRestaurant, Date dateResa, String nom, String prenom, int guests, String phoneNumber) throws RemoteException;


}
