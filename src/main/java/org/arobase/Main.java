package org.arobase;

import org.arobase.bd.LancerBd;
import org.arobase.client.Client;
import org.arobase.enseignements.LancerEnsSup;
import org.arobase.serveur.LancerServeur;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        Service serviceChoisi = null;

        if (args.length < 1) {
            System.err.println("Veuillez choisir un service ('client', 'serveur', 'service-bd', 'service-enssup')");
            System.exit(1);
        }

        switch (args[0]) {
            case "client" -> serviceChoisi = new Client();
            case "serveur" -> serviceChoisi = new LancerServeur();
            case "service-bd" -> serviceChoisi = new LancerBd();
            case "service-enssup" -> serviceChoisi = new LancerEnsSup();

        }

        if (serviceChoisi == null) {
            System.err.println("Veuillez choisir un service ('client', 'serveur', 'service-bd', 'service-enssup')");
            System.exit(1);
        }

        serviceChoisi.demarrer(Arrays.copyOfRange(args, 1, args.length));

    }

}