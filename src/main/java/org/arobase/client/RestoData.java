package org.arobase.client;

import org.json.simple.JSONObject;

import java.io.Serializable;

public class RestoData implements Serializable {

    private String name;
    private int nbPlace;
    private String address;
    private double latitude;
    private double longitude;

    public RestoData(String name, int nbPlace, String address, double latitude, double longitude) {
        this.name = name;
        this.nbPlace = nbPlace;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public int getNbPlace() {
        return nbPlace;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public static RestoData fromJSON(JSONObject jsonObject) {
        String nom = (String) jsonObject.get("name");
        String address = (String) jsonObject.get("address");
        long nbPlaces = (Long) jsonObject.get("nbPlaces");
        double latitude = (Double) jsonObject.get("latitude");
        double longitude = (Double) jsonObject.get("longitude");

        return new RestoData(nom, Math.toIntExact(nbPlaces), address, latitude, longitude);
    }

}
