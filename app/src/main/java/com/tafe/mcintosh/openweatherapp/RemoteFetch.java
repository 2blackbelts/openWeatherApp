package com.tafe.mcintosh.openweatherapp;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RemoteFetch {

    private static final String OPEN_WEATHER_MAP_API = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";

    public static JSONObject getJSON(Context context, String city){
        try {
            // make the url
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, city));
            Log.d("json", String.valueOf(url));

            // get URL
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.addRequestProperty("x-api-key",context.getString(R.string.open_weather_maps_app_id));
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            // Read through results and process to json String
            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // if not successful then return null
            if(data.getInt("cod") != 200){
                return null;
            }
            return data;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
       return null;
    }
}
