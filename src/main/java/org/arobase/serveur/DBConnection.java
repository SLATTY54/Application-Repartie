package org.arobase.serveur;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static String serverName = "webetu.iutnc.univ-lorraine.fr";
    private static String portNumber = "3306";
    private static String dbName = "perrot54u";
    private static String username;
    private static String password;
    private static final String JDBC_URL = "jdbc:mysql://" + serverName + ":" + portNumber + "/" + dbName + "?autoReconnect=true";

    /**
     * Initialiser la base de donnée avec le login/password
     * @param usr
     * @param pwd
     */
    public static void initializeDatabase(String usr, String pwd){
        username = usr;
        password = pwd;
    }

    /**
     * @return Une session de DBConnection (en mode transactionnel)
     */
    public static Connection createSession() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, username, password);
    }

}