package org.arobase.bd;

import org.arobase.client.ReservationData;
import org.arobase.client.RestoData;
import org.json.simple.JSONObject;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServiceBD extends Remote {

    public JSONObject getRestaurants() throws RemoteException;

    public boolean reserver(ReservationData reservationData) throws RemoteException;

    public int ajoutRestaurant(RestoData restoData) throws RemoteException;

}
