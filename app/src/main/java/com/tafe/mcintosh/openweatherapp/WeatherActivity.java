package com.tafe.mcintosh.openweatherapp;

import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class WeatherActivity extends AppCompatActivity {

    Typeface weatherFont;

    private TextView cityField;
    private TextView updateField;
    private TextView detailsField;
    private TextView currentTemperatureField;
    private TextView weatherIcon;

    Handler handler;

    public WeatherActivity(){
        handler = new Handler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weather.ttf");

        updateWeatherData(new CityPreference(this).getCity());

        cityField = (TextView) findViewById(R.id.city_field);
        updateField = (TextView) findViewById(R.id.updated_field);
        detailsField = (TextView) findViewById(R.id.details_field);
        currentTemperatureField = (TextView) findViewById(R.id.current_temperature_field);
        weatherIcon = (TextView) findViewById(R.id.weather_icon);

        weatherIcon.setTypeface(weatherFont);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.weather, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.change_city){
            Log.d("menu", "Change city was pressed");
        }
        return false;
        // return super.onOptionsItemSelected(item);
    }

    private void updateWeatherData(final String city) {
        new Thread(){
            public void run(){
                final JSONObject json = RemoteFetch.getJSON(WeatherActivity.this, city);
                if (json == null){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(
                                    WeatherActivity.this,
                                    R.string.place_not_found,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            detailsField.setText(json.toString());
                            weatherIcon.setText(R.string.weather_sunny);

                        }
                    });
                }
            }

        }.start();
    }
}
