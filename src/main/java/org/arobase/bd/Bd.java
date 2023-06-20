package org.arobase.bd;

import org.arobase.client.ReservationData;
import org.arobase.client.RestoData;
import org.json.simple.JSONObject;

import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Bd implements ServiceBD {

    @Override
    public JSONObject getRestaurants() throws RemoteException {
        System.out.println("BDD > Tentative de recuperation de la liste des restaurants");
        try {
            Connection connection = DBConnection.createSession();

            String requete = "SELECT * FROM Restaurants";
            PreparedStatement preparedStatement = connection.prepareStatement(requete);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<JSONObject> list = new ArrayList<>();

            while (resultSet.next()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", resultSet.getInt("id"));
                jsonObject.put("name", resultSet.getString("name"));
                jsonObject.put("nbPlace", resultSet.getString("nbPlace"));
                jsonObject.put("address", resultSet.getString("address"));
                jsonObject.put("latitude", resultSet.getDouble("latitude"));
                jsonObject.put("longitude", resultSet.getDouble("longitude"));

                list.add(jsonObject);
            }

            JSONObject finalJSON = new JSONObject();
            finalJSON.put("restaurants", list);

            System.out.println("BDD > Recuperation des restos effectuees, envoi vers le client");

            return finalJSON;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean reserver(ReservationData reservationData) throws RemoteException {

        System.out.println("BDD > Tentative de reservation pour " + reservationData.getName());

        try {
            Connection connection = DBConnection.createSession();


            PreparedStatement testReservation = connection.prepareStatement("SELECT * FROM Reservations WHERE restaurant_id = ? AND reservationTime = ?");
            testReservation.setInt(1, reservationData.getRestaurantId());
            testReservation.setDate(2, Date.valueOf(reservationData.getReservationTime()));

            ResultSet resultSet = testReservation.executeQuery();

            if (!resultSet.next()) {

                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Reservations(`restaurant_id`, `name`, `surname`, `guests`, `phoneNumber`, `reservationTime`) VALUES (?,?,?,?,?,?)");
                preparedStatement.setInt(1, reservationData.getRestaurantId());
                preparedStatement.setString(2, reservationData.getName());
                preparedStatement.setString(3, reservationData.getSurname());
                preparedStatement.setInt(4, reservationData.getGuests());
                preparedStatement.setString(5, reservationData.getPhoneNumber());
                preparedStatement.setDate(6, Date.valueOf(reservationData.getReservationTime()));

                preparedStatement.executeUpdate();

                System.out.println("BDD > Reservation effectuee pour " + reservationData.getName());
                return true;

            } else {
                System.out.println("BDD > Echec d'une reservation car un client a deja reserve à ce restaurant pour la date donnee");
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int ajoutRestaurant(RestoData restoData) throws RemoteException {

        System.out.println("BDD > Demande d'ajout d'un nouveau restaurant");

        try {
            Connection connection = DBConnection.createSession();

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Restaurants(`name`, `nbPlace`, `address`, `latitude`, `longitude`) VALUES (?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, restoData.getName());
            preparedStatement.setInt(2, restoData.getNbPlace());
            preparedStatement.setString(3, restoData.getAddress());
            preparedStatement.setDouble(4, restoData.getLatitude());
            preparedStatement.setDouble(5, restoData.getLongitude());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            int generatedId = -1;
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1);
            }

            System.out.println("BDD > Restaurant ajoutee avec succes");
            return generatedId;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
