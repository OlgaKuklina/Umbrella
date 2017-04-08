package com.oklab.umbrella.parsers;

import android.util.Log;

import com.oklab.umbrella.data.WeatherDataEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olgakuklina on 2017-04-08.
 */

public class WeatherDataParser {


    private static final String TAG = WeatherDataParser.class.getSimpleName();

    public WeatherDataEntry parse(JSONObject jsonObject) throws JSONException {
        WeatherDataEntry entry = parseItem(jsonObject);
        return entry;
    }

    private WeatherDataEntry parseItem(JSONObject object) throws JSONException {
        if (object == null) {
            return null;
        }
        Log.v(TAG, "object = " + object);

        String name = " ";
        if (!object.getString("name").isEmpty()) {
            name = object.getString("name");
        }
        JSONObject   coord = object.getJSONObject("coord");
        double lat = coord.getDouble("lat");
        double lon = coord.getDouble("lon");
        JSONObject weather = object.getJSONObject("weather");
        double temp = weather.getDouble("temp");
        double pressure = weather.getDouble("pressure");
        int humidity = weather.getInt("humidity");
        JSONObject wind = object.getJSONObject("wind");
        double speed = wind.getDouble("speed");
        int deg = wind.getInt("deg");
        JSONObject rain = object.getJSONObject("rain");
        int rain3h = rain.getInt("3h");
        JSONObject clouds = object.getJSONObject("clouds");
        int cloudiness = clouds.getInt("cloudiness");
        name = object.getString("name");
        return new WeatherDataEntry(lon, lat, temp, pressure, humidity, speed, deg, rain3h, cloudiness, name);
    }
}
