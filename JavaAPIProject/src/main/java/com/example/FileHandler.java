package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class FileHandler {

    // returns ArrayList of Strings read from the file where data is being saved
    public static ArrayList<String> readFile() {
        ArrayList<String> playlist = new ArrayList<String>();
        String line = "";
        try (BufferedReader reader = new BufferedReader(new FileReader("final-project-cherinesoe-main/JavaAPIProject/src/main/java/com/example/playlists.txt"))) {
            while ((line = reader.readLine()) != null) {
                playlist.add(line);
            }
            return playlist;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return playlist;
    }

    // saves data to file
    public static void saveData(String data) {
        try (FileWriter writer = new FileWriter("final-project-cherinesoe-main/JavaAPIProject/src/main/java/com/example/playlists.txt")) {
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> sortData(ArrayList<String> playlist) {
        Collections.sort(playlist);
        return playlist;
    }

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader("final-project-cherinesoe-main/JavaAPIProject/src/main/java/com/example/playlists.txt"))) {
            // String line;
            // while ((line = reader.readLine()) != null) {
            //     System.out.println(line);  // Print each line to the console
            // }
            ArrayList<String> playlist = FileHandler.readFile();
            playlist = FileHandler.sortData(playlist);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
