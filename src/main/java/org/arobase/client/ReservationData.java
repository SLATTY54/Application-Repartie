package org.arobase.client;

import org.json.simple.JSONObject;

import java.io.Serializable;

public class ReservationData implements Serializable {

    private int restaurantId;
    private String name;
    private String surname;
    private int guests;
    private String phoneNumber;
    private String reservationTime;

    public ReservationData(int restaurantId, String name, String surname, int guests, String phoneNumber, String reservationTime) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.surname = surname;
        this.guests = guests;
        this.phoneNumber = phoneNumber;
        this.reservationTime = reservationTime;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getGuests() {
        return guests;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getReservationTime() {
        return reservationTime;
    }

    @Override
    public String toString() {
        return "ReservationData{" +
                "restaurantId=" + restaurantId +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", guests=" + guests +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", reservationTime='" + reservationTime + '\'' +
                '}';
    }

    public static ReservationData fromJSON(JSONObject jsonObject) {
        long id = (Long) jsonObject.get("restaurant_id");
        String nom = (String) jsonObject.get("name");
        String prenom = (String) jsonObject.get("surname");
        long guest = (Long) jsonObject.get("guests");
        String tel = (String) jsonObject.get("phoneNumber");
        String date = (String) jsonObject.get("reservationTime");

        return new ReservationData(Math.toIntExact(id), nom, prenom, Math.toIntExact(guest), tel, date);
    }

}
