package com.example;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Spotify {
    // generates access token from spotify web api
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

    // generates an artist's id based on name
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

    // gets information about artist based on id
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

    // gets artist's top tracks based on id
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

    //     URL url = new URL("https://api.spotify.com/v1/me/player/currently-playing?market=US");
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
    
    // gets an ArrayList of artist's top tracks based on name
    public static ArrayList<String> listOfTracks(String artist) throws JSONException, IOException {
        ArrayList<String> songs = new ArrayList<String>();
        String id = Spotify.getArtistID(artist);


        int length = (new JSONObject(Spotify.getTopTracks(id)).getJSONArray("tracks")).length();
        for (int i = 0; i < length; i++) {
            String track = (new JSONObject(Spotify.getTopTracks(id)).getJSONArray("tracks").getJSONObject(i).getString("name"));
            songs.add(track);
        }
        return songs;
    }

    public static ArrayList<String> getAlbumURLs(String id) throws JSONException, IOException {
        ArrayList<String> albumURLs = new ArrayList<String>();

        int length = (new JSONObject(Spotify.getTopTracks(id)).getJSONArray("tracks")).length();
        for (int i = 0; i < length; i++) {
            String url = (new JSONObject(Spotify.getTopTracks(id)).getJSONArray("tracks").
                        getJSONObject(0).getJSONObject("album").getJSONArray("images").
                        getJSONObject(2).getString("url"));            
            albumURLs.add(url);
        }
        return albumURLs;
    }

    // testing methods in Spotify class 
    public static void main(String[] args) throws IOException {
        // Scanner scan = new Scanner(System.in);
        // ArrayList<String> playlist = new ArrayList<String>();

        // String name = "Ariana Grande";
        // System.out.println(name + "'s Top 10 Tracks:");
        // ArrayList<String> songs = listOfTracks(name);

        // int option = 0;
        // while (option != -1) {
        //     for (int i = 0; i < songs.size(); i++) {
        //         System.out.println((i + 1) + ": " + songs.get(i));
        //     }
        //     System.out.print("What song would you like to add to your playlist? (-1 to quit): ");
        //     option = scan.nextInt();
        //     if (option != -1) {
        //         playlist.add(songs.get(option - 1));
        //     }            
        //     System.out.println(playlist);
        // }

        String name = "Ariana Grande";
        String id = getArtistID(name);
        System.out.println(getAlbumURLs(id));
    }
}
