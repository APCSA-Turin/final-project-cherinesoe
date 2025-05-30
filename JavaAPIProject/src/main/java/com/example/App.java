package com.example;
import org.json.JSONObject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import org.json.JSONArray;
import org.json.JSONException;

public class App {
    private static int numArtistsGenerated = 0;
    private static String currentArtist = "";
    private static ArrayList<String> songs = new ArrayList<String>();
    private static ArrayList<String> playlist = FileHandler.readFile();
    private static String playlistString = "Current Playlist:\n";
    private static String oldPlaylist = "Current Playlist:\n";

    public static void main(String[] args) {
        try {
            // pull data from api
         
            String dataString = API.getData("https://api.openweathermap.org/data/2.5/weather?lat=40.6891&lon=-73.9767&appid=92b8873d7d12db160086c4d529c9eedf&units=imperial");
            JSONObject data = new JSONObject(dataString);
            JSONObject main = data.getJSONObject("main");
            JSONObject sys = data.getJSONObject("sys");
            JSONObject weather = (data.getJSONArray("weather")).getJSONObject(0);
            double temp = main.getDouble("temp");
            double feelsLike = main.getDouble("feels_like");
            int humidity = main.getInt("humidity");
            String desc = weather.getString("description");
            String location = "Location: " + data.getString("name") + ", " + sys.getString("country");
            // get time in EST
            int hr = LocalTime.now(ZoneId.of("America/New_York")).getHour();

            // used for determining features on gui
            boolean hot = (feelsLike > 75);
            boolean morning = (hr < 12);

            JFrame f = new JFrame("Playlist Maker"); //creating instance of JFrame
            JPanel p = new JPanel(); // creating panel for GUI
            // set up grid layout
            GridBagLayout layout = new GridBagLayout();
            GridBagConstraints con = new GridBagConstraints();
            p.setLayout(layout);

            for (int i = 0; i < playlist.size(); i++) {
                oldPlaylist += playlist.get(i) + "\n";
            }
            
            // initializing components for swing
            JLabel printInfo = new JLabel("<html>" + location + "<br/>" +
                                        "Temp: " + temp + " F, Feels Like: " + feelsLike + " F<br/>" +
                                        "Humidity: " + humidity + "<br/>" +
                                        "Description: " + desc + "</html>", SwingConstants.LEFT);
            printInfo.setFont(new Font("Sans Serif", Font.PLAIN, 20));
            JButton addButton = new JButton("Add Song to Playlist");//a button to add song when pressed
            addButton.setFont(new Font("Sans Serif", Font.PLAIN, 20));
            JButton newButton = new JButton("Generate Artist's Top Tracks");//a button to do a new artist
            newButton.setFont(new Font("Sans Serif", Font.PLAIN, 20));
            JTextField input = new JTextField(); //input text 
            input.setFont(new Font("Sans Serif", Font.PLAIN, 24));
            JTextArea output = new JTextArea(oldPlaylist + "(" + playlist.size() + " songs)");//where the playlist will output 
            output.setFont(new Font("Sans Serif", Font.PLAIN, 20));
            output.setEditable(false);

            // frame attributes
            f.setSize(800, 800);
            Container c = f.getContentPane();
            c.setBackground(Color.WHITE);
            f.setVisible(true);//making the frame visible  

            // adds different components to panel and panel to frame
            // p.add(printInfo, BorderLayout.PAGE_START);
            // p.add(input, BorderLayout.CENTER);
            // p.add(addButton, BorderLayout.EAST);
            // p.add(newButton, BorderLayout.WEST);
            // p.add(output, BorderLayout.PAGE_END);
            // f.add(p);

            // setup weather information on grid layout
            con.fill = GridBagConstraints.HORIZONTAL;
            con.gridx = 0;
            con.gridy = 0;
            con.gridwidth = 2;
            p.add(printInfo, con);

            // setup input on grid layout
            con.fill = GridBagConstraints.HORIZONTAL;
            con.gridx = 0;
            con.gridy = 1;
            con.gridwidth = 2;
            p.add(input, con);

            // setup button to generate new artist tracks on grid layout
            con.gridx = 0;
            con.gridy = 2;
            con.gridwidth = 1;
            p.add(newButton, con);

            // setup button to add song to playlist on grid layout
            con.gridx = 1;
            con.gridy = 2;
            con.gridwidth = 1;
            p.add(addButton, con);

            // setup output on grid layout
            con.fill = GridBagConstraints.HORIZONTAL;
            con.gridx = 0;
            con.gridy = 3;
            con.gridwidth = 2;
            p.add(output,con);

            c.add(p, BorderLayout.CENTER);

            // TESTING PRINTING DATA
            // System.out.println(location);
            // System.out.println("Temp: " + temp + " F, Feels Like: " + feelsLike + " F");
            // if (hot) {
            //     System.out.println("It's hot outside! Don't forget to drink water.");
            // } else {
            //     System.out.println("It's chilly outside! Don't forget to wear a sweater.");
            // }
            // System.out.println("Humidity: " + humidity);
            // System.out.println("Description: " + desc);
            // if (desc.contains("cloud")) {
            //     p.setBackground(Color.gray);
            // } else if (desc.contains("rain")) {
            //     p.setBackground(Color.blue);
            // } else if (desc.contains("sun")) {
            //     p.setBackground(Color.yellow);
            // }
            // System.out.print("Time of Day: ");
            // if (morning) {
            //     System.out.println("AM");
            // } else {
            //     System.out.println("PM");
            // }

            // checks if button is clicked to generate new artist
            newButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    currentArtist = input.getText().trim();
                    if (!currentArtist.isEmpty()) {
                        numArtistsGenerated++;
                        try {
                            // outputs generated tracks
                            songs = Spotify.listOfTracks(currentArtist);
                            String songsString = numArtistsGenerated + " artists generated.\n" + currentArtist + "'s Top 10 Tracks:\n";
                            for (int i = 0; i < songs.size(); i++) {
                                songsString += (i + 1) + ". "  + songs.get(i) + "\n";
                            }
                            output.setText(songsString);
                            input.setText("");
                        } catch (JSONException | IOException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        output.setText("Please enter a valid music artist.");
                        input.setText("");
                    }
                }
            });

            // check if button is clicked to add song to playlist
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int option = Integer.parseInt(input.getText().trim());
                    if (option > 0 && option < 11) { //if option is not an integer
                        playlist.add(songs.get(option - 1) + " by " + currentArtist);
                        playlist = FileHandler.sortData(playlist);
                        for (int i = 0; i < playlist.size(); i++) {
                            playlistString += playlist.get(i) + "\n";
                        }
                        output.setText(playlistString + "(" + (playlist.size()) + " songs)");
                        input.setText("");
                    } else {
                        output.setText("Please enter a number from 1 - 10.");
                        input.setText("");
                    }
                }
            });

            f.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    String data = "";
                    for (int i = 0; i < playlist.size(); i++) {
                        data += playlist.get(i)+"\n";
                    }
                    FileHandler.saveData(data);
                }
            });

        } catch (Exception e) {

        }
        
    }
}
