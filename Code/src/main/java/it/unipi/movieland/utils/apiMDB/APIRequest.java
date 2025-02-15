package it.unipi.movieland.utils.apiMDB;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class APIRequest {

    private final HttpClient httpClient;

    public APIRequest() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public String getIdByTitle(String type, String title, Optional<Integer> year) throws IOException, InterruptedException {
        String baseUrl = "https://api.mdblist.com/search/";
        String yearParam = (year.isPresent()) ? "&year=" + year.get() : "&";
        String apiKey = "9penwmkrxop9yv1nch91du21l";
        //creating the url and the request
        String requestUrl = String.format("%s%s?query=%s%s&apikey=%s",
                baseUrl,
                type,
                URLEncoder.encode(title, StandardCharsets.UTF_8),
                yearParam,
                apiKey
        );
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
                .header("accept", "application/json")
                .build();
        //sending the request
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200 ? response.body() : null;
    }

    public String getMoviebyId(String type, String id) throws IOException, InterruptedException {
        type = type.toLowerCase();
        String baseUrl = "https://api.mdblist.com/imdb/";
        String apiKey = "9penwmkrxop9yv1nch91du21l";

        String requestUrl = String.format("%s%s/%s?apikey=%s",
                baseUrl,
                URLEncoder.encode(type, StandardCharsets.UTF_8),
                URLEncoder.encode(id, StandardCharsets.UTF_8),
                apiKey);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
                .header("accept", "application/json")
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200 ? response.body() : null;
    }

}
