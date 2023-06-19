package org.arobase.serveur;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.rmi.RemoteException;

public class EnseignementSup implements ServiceEnseignementSup {

    @Override
    public String getEnseignementsSup() throws RemoteException {
        try {

            HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).followRedirects(HttpClient.Redirect.NORMAL).build();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://www.data.gouv.fr/fr/datasets/r/5fb6d2e3-609c-481d-9104-350e9ca134fa")).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("ENS-SUP > Récuperation des donnees d'ens superieur");

            return response.body();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
