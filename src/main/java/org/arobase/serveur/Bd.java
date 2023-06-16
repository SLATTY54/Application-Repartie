package org.arobase.serveur;

import org.json.simple.JSONObject;

import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Bd implements ServiceBD {

    @Override
    public JSONObject getRestaurants() throws RemoteException {
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

            System.out.println("BDD > Récupération de la liste des restaurants");

            return finalJSON;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean reserver(int idRestaurant, Date dateResa, String nom, String prenom, int guests, String phoneNumber) throws RemoteException {
        try {
            Connection connection = DBConnection.createSession();


            PreparedStatement testReservation = connection.prepareStatement("SELECT * FROM Reservations WHERE restaurant_id = ? AND reservationTime = ?");
            testReservation.setInt(1, idRestaurant);
            testReservation.setDate(2, dateResa);

            ResultSet resultSet = testReservation.executeQuery();

            if (resultSet.next()) {

                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `Reservations`(`restaurant_id`, `name`, `surname`, `guests`, `phoneNumber`, `reservationTime`) VALUES (?,?,?,?,?,?)");
                preparedStatement.setInt(1, idRestaurant);
                preparedStatement.setString(2, nom);
                preparedStatement.setString(3, prenom);
                preparedStatement.setInt(4, guests);
                preparedStatement.setString(5, phoneNumber);
                preparedStatement.setDate(6, dateResa);

                preparedStatement.executeUpdate();
                return true;

            } else {
                System.out.println("BDD > Echec d'une réservation car un client a déjà reservé à ce restaurant pour la date donnée");
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void ajoutRestaurant(String name, int nbPlace, String address, double latitude, double longitude) throws RemoteException {
        try {
            Connection connection = DBConnection.createSession();

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Restaurants(`name`, `nbPlace`, `address`, `latitude`, `longitude`) VALUES (?,?,?,?,?)");
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, nbPlace);
            preparedStatement.setString(3, address);
            preparedStatement.setDouble(4, latitude);
            preparedStatement.setDouble(5, longitude);
            preparedStatement.executeUpdate();

            System.out.println("BDD > Ajout d'un nouveau restaurant");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
