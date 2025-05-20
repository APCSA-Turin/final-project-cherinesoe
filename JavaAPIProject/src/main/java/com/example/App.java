package com.example;
import org.json.JSONObject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import org.json.JSONArray;

public class App {
    public static void main(String[] args) {
        JFrame f=new JFrame();//creating instance of JFrame    
        JButton b=new JButton("click");//creating instance of JButton    
        b.setBounds(130,100,100, 40);//x axis, y axis, width, height    
        f.add(b);//adding button in JFrame    
        f.setSize(400,500);//400 width and 500 height    
        f.setLayout(null);//using no layout managers    
        f.setVisible(true);//making the frame visible    
        try {
            String dataString = API.getData("https://api.openweathermap.org/data/2.5/weather?lat=40.6891&lon=-73.9767&appid=92b8873d7d12db160086c4d529c9eedf&units=imperial");
            JSONObject data = new JSONObject(dataString);
            System.out.println(data);
            JSONObject main = data.getJSONObject("main");
            JSONObject sys = data.getJSONObject("sys");
            JSONObject weather = (data.getJSONArray("weather")).getJSONObject(0);
            double temp = main.getDouble("temp");
            double feelsLike = main.getDouble("feels_like");
            int humidity = main.getInt("humidity");
            String desc = weather.getString("description");
            String country = sys.getString("country");
            String location = data.getString("name");

            boolean hot = (feelsLike > 75);

            // int time = ((data.getInt("dt")) % 86400);
            // String printingTime = (time ) + ":";
            // time -= (data.getInt("dt") - (5 * 3600));
            System.out.println();
            System.out.println("Location: " + location + ", " + country);
            System.out.println("Temp: " + temp + " F, Feels Like: " + feelsLike + " F");
            if (hot) {
                System.out.println("It's hot outside! Don't forget to drink water.");
            } else {
                System.out.println("It's chilly outside! Don't forget to wear a sweater.");
            }
            System.out.println("Humidity: " + humidity);
            System.out.println("Description: " + desc);
            // System.out.println(printingTime);

        } catch (Exception e) {

        }
    }
}
