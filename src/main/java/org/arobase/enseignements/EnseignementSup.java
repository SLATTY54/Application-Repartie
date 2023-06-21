package org.arobase.enseignements;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.rmi.RemoteException;

public class EnseignementSup implements ServiceEnseignementSup {

    @Override
    public String getEnseignementsSup() throws RemoteException {
        try {

            System.out.println("Service ENS-SUP > Recuperation des donnees de data-gouv.fr");

            HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).followRedirects(HttpClient.Redirect.NORMAL)
                    .proxy(ProxySelector.of(new InetSocketAddress("www-cache.iutnc.univ-lorraine.fr", 3128))).build();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://www.data.gouv.fr/fr/datasets/r/5fb6d2e3-609c-481d-9104-350e9ca134fa")).build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();

            if (statusCode == 200) {
                String responseBody = response.body();
            } else {
                System.out.println("Request failed with status code: " + statusCode);
            }

            System.out.println("Service ENS-SUP > Donnees recuperes avec succes, envoi du resultat vers le client");

            return response.body();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
