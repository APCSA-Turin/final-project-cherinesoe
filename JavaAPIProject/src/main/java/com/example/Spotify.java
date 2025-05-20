package com.example;
import java.net.*;
import java.io.*;
import java.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Spotify {
    public static String getAccessToken() throws IOException {
        String auth = "c1c9f7b769a041c29f8b3fa7ada719f2:d66a3ead91664f479ae6494081e8447c";
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        /*endpoint is a url (string) that you get from an API website*/
        URL url = new URL("https://accounts.spotify.com/api/token");
        /*connect to the URL*/
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", "Basic " + encodedAuth);

        String body = "grant_type=client_credentials";
        connection.getOutputStream().write(body.getBytes());

        BufferedReader buff = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;//variable to store text, line by line
        /*A string builder is similar to a string object but faster for larger strings, 
        you can concatenate to it and build a larger string. Loop through the buffer 
        (read line by line). Add it to the stringbuilder */
        StringBuilder content = new StringBuilder();
        while ((inputLine = buff.readLine()) != null) {
            content.append(inputLine);
        }
        buff.close();

        String json = content.toString();
        return json;
    }

    public static String getArtistID(String name) throws JSONException, IOException {
        JSONObject obj = new JSONObject(getAccessToken());
        String token = obj.getString("access_token");

        URL url = new URL("https://api.spotify.com/v1/search?q=" + URLEncoder.encode(name, "UTF-8") + "&type=artist");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", "Bearer " + token);

        BufferedReader buff = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;//variable to store text, line by line
        /*A string builder is similar to a string object but faster for larger strings, 
        you can concatenate to it and build a larger string. Loop through the buffer 
        (read line by line). Add it to the stringbuilder */
        StringBuilder content = new StringBuilder();
        while ((inputLine = buff.readLine()) != null) {
            content.append(inputLine);
        }
        buff.close();

        String json = content.toString();
        JSONObject main = new JSONObject(json);
        main = main.getJSONObject("artists");
       
        JSONArray items = main.getJSONArray("items");
        JSONObject firstItem = (JSONObject) items.get(0);
        String id = firstItem.getString("id");

        return id;
    }

    public static String getArtist(String id) throws JSONException, IOException {
        JSONObject obj = new JSONObject(getAccessToken());
        String token = obj.getString("access_token");

        URL url = new URL("https://api.spotify.com/v1/artists/" + URLEncoder.encode(id, "UTF-8"));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", "Bearer " + token);

        BufferedReader buff = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;//variable to store text, line by line
        /*A string builder is similar to a string object but faster for larger strings, 
        you can concatenate to it and build a larger string. Loop through the buffer 
        (read line by line). Add it to the stringbuilder */
        StringBuilder content = new StringBuilder();
        while ((inputLine = buff.readLine()) != null) {
            content.append(inputLine);
        }
        buff.close();

        return content.toString();
    }

    public static String getTopTracks(String id) throws JSONException, IOException {
        JSONObject obj = new JSONObject(getAccessToken());
        String token = obj.getString("access_token");

        URL url = new URL("https://api.spotify.com/v1/artists/" + URLEncoder.encode(id, "UTF-8") + "/top-tracks");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", "Bearer " + token);

        BufferedReader buff = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;//variable to store text, line by line
        /*A string builder is similar to a string object but faster for larger strings, 
        you can concatenate to it and build a larger string. Loop through the buffer 
        (read line by line). Add it to the stringbuilder */
        StringBuilder content = new StringBuilder();
        while ((inputLine = buff.readLine()) != null) {
            content.append(inputLine);
        }
        buff.close();

        return content.toString();
    }

    // public static String getCurrentPlaying () throws JSONException, IOException {
    //     JSONObject obj = new JSONObject(getAccessToken());
    //     String token = obj.getString("access_token");

    //     URL url = new URL("https://api.spotify.com/v1/me/player/currently-playing");
    //     HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    //     connection.setRequestProperty("Authorization", "Bearer " + token);

    //     BufferedReader buff = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    //     String inputLine;//variable to store text, line by line
    //     /*A string builder is similar to a string object but faster for larger strings, 
    //     you can concatenate to it and build a larger string. Loop through the buffer 
    //     (read line by line). Add it to the stringbuilder */
    //     StringBuilder content = new StringBuilder();
    //     while ((inputLine = buff.readLine()) != null) {
    //         content.append(inputLine);
    //     }
    //     buff.close();

    //     return content.toString();
    // }   

    public static void main(String[] args) throws IOException {
        String name = "Ariana Grande";
        //System.out.println(Spotify.getAccessToken());
        String id = Spotify.getArtistID(name);
        // System.out.println(Spotify.getArtist(id));
        // System.out.println(Spotify.getTopTracks(id));
        System.out.println(name + "'s Top 10 Tracks:");
        int length = (new JSONObject(Spotify.getTopTracks(id)).getJSONArray("tracks")).length();
        for (int i = 0; i < length; i++) {
            String track = (new JSONObject(Spotify.getTopTracks(id)).getJSONArray("tracks").getJSONObject(i).getString("name"));
            System.out.println(track);
        }
        // System.out.println(Spotify.getCurrentPlaying());
    }
}
