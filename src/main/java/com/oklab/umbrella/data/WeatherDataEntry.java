package com.oklab.umbrella.data;

/**
 * Created by olgakuklina on 2017-04-08.
 */

public class WeatherDataEntry {

    private final double lon;
    private final double lat;
    private final double temp;
    private final double pressure;
    private final int humidity;
    private final double speed;
    private final int deg;
    private final int rain3h;
    private final int cloudiness;
    private final String name;

    public WeatherDataEntry(double lon, double lat, double temp, double pressure, int humidity, double speed, int deg, int rain3h, int cloudiness, String name) {
        this.lon = lon;
        this.lat = lat;
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.speed = speed;
        this.deg = deg;
        this.rain3h = rain3h;
        this.cloudiness = cloudiness;
        this.name = name;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    public double getTemp() {
        return temp;
    }

    public double getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getSpeed() {
        return speed;
    }

    public int getDeg() {
        return deg;
    }

    public int getRain3h() {
        return rain3h;
    }

    public int getCloudiness() {
        return cloudiness;
    }

    public String getName() {
        return name;
    }
}
