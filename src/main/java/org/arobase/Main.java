package org.arobase;

import org.arobase.client.Client;
import org.arobase.serveur.LancerServeur;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        Service serviceChoisi = null;

        if (args.length < 1) {
            System.err.println("Veuillez choisir un service ('client', 'serveur'");
            System.exit(1);
        }

        switch (args[0]) {
            case "client" -> serviceChoisi = new Client();
            case "serveur" -> serviceChoisi = new LancerServeur();
        }

        if (serviceChoisi == null) {
            System.err.println("Veuillez choisir un service ('client', 'serveur'");
            System.exit(1);
        }

        serviceChoisi.demarrer(Arrays.copyOfRange(args, 1, args.length));

    }

}