package com.ffermi.eventapp.model;

/**
 * source: https://openweathermap.org/weather-conditions
 * Group 2xx: Thunderstorm
 * Group 3xx: Drizzle
 * Group 5xx: Rain
 * Group 6xx: Snow
 * Group 7xx: Atmosphere
 * Group 800: Clear
 * Group 80x: Clouds
 * Group 90x: Extreme
 */

public final class WeatherCode {

    public static final int THUNDERSTORM = 200;
    public static final int DRIZZLE = 300;
    public static final int RAIN = 500;
    public static final int SNOW = 600;
    public static final int CLEAR_SKY = 800;
    public static final int CLOUDS = 801;
    public static final int EXTREME = 900;

    public static boolean isThunderstorm(int code) {
        return (code > THUNDERSTORM && code < (THUNDERSTORM + 99));
    }

    public static boolean isDrizzle(int code) {
        return (code > DRIZZLE && code < (DRIZZLE + 99));
    }

    public static boolean isRain(int code) {
        return (code > RAIN && code < (RAIN + 99));
    }

    public static boolean isSnow(int code) {
        return (code > SNOW && code < (SNOW + 99));
    }

    public static boolean isClearSky(int code) {
        return code == CLEAR_SKY;
    }

    public static boolean isClouds(int code) {
        return (code > CLOUDS && code < (CLOUDS + 99));
    }

    public static boolean isExtreme(int code) {
        return (code > EXTREME && code < (EXTREME + 99));
    }

}
