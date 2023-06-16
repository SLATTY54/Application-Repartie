package org.arobase.serveur;

import org.json.simple.JSONObject;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Date;

public interface ServiceBD extends Remote {

    public JSONObject getRestaurants() throws RemoteException;

    public void reserver(int idRestaurant, Date dateResa, String nom, String prenom, int guests, String phoneNumber) throws RemoteException;

    public void ajoutRestaurant(String name, int nbPlace, String address, double latitude, double longitude) throws RemoteException;

}
