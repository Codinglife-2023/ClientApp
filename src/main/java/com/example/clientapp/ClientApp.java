package com.example.clientapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ClientApp {

    private static final String BASE_URL = "http://localhost:8080/api";

    public static void main(String[] args) {
        ClientApp clientApp = new ClientApp();
        
        try {
            // Fetch cities
            System.out.println("Attempting to fetch cities...");
            String cities = clientApp.fetchCities();
            System.out.println("Cities: " + cities);

            // Create a new city
            System.out.println("Attempting to create a city...");
            String newCity = clientApp.createCity("Los Angeles", "CA", 4000000);
            System.out.println("City created: " + newCity);

        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    public String fetchCities() throws Exception {
        URL url = new URL(BASE_URL + "/cities");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            StringBuilder response = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
            }
            return response.toString();
        } else {
            throw new Exception("GET request failed with response code " + responseCode);
        }
    }

    public String createCity(String name, String state, int population) throws Exception {
        URL url = new URL(BASE_URL + "/cities");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonInputString = String.format("{\"name\":\"%s\", \"state\":\"%s\", \"population\":%d}", name, state, population);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
            StringBuilder response = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
            }
            return response.toString();
        } else {
            throw new Exception("POST request failed with response code " + responseCode);
        }
    }
}
