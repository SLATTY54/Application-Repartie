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

            return finalJSON;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void reserver(int idRestaurant, Date dateResa, String nom, String prenom, int guests, String phoneNumber) throws RemoteException {
        try {
            Connection connection = DBConnection.createSession();


            PreparedStatement preparedStatement = connection.prepareStatement("select * from Reservations where restaurant_id = ? and reservationTime = ?");
            preparedStatement.setInt(1, idRestaurant);
            preparedStatement.setDate(2, dateResa);

            if (preparedStatement.execute()) {
                throw new RuntimeException("La réservation existe déjà");
            } else {
                String requete = "INSERT INTO reservation (nom, prenom, guests, phoneNumber) VALUES ('" + nom + "', '" + prenom + "', " + guests + ", '" + phoneNumber + "');";

                PreparedStatement preparedStatement1 = connection.prepareStatement(requete);
                preparedStatement1.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void ajoutRestaurant(String name, int nbPlace, String address, double latitude, double longitude) throws RemoteException {
        try {
            Connection connection = DBConnection.createSession();

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Restaurants VALUES (?,?,?,?,?)");
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, nbPlace);
            preparedStatement.setString(3, address);
            preparedStatement.setDouble(4, latitude);
            preparedStatement.setDouble(5, longitude);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
