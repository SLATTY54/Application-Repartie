package org.arobase.serveur;

import org.arobase.client.ReservationData;
import org.json.simple.JSONObject;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServiceBD extends Remote {

    public JSONObject getRestaurants() throws RemoteException;

    public boolean reserver(ReservationData reservationData) throws RemoteException;

    public void ajoutRestaurant(String name, int nbPlace, String address, double latitude, double longitude) throws RemoteException;

}
