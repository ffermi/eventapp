package com.ffermi.eventapp.model;

public class WeatherConstraints {

    private boolean thunderstorm;
    private boolean drizzle;
    private boolean rain;
    private boolean snow;
    private boolean clearSky;
    private boolean clouds;
    private boolean extreme;

    public WeatherConstraints() {
    }

    public WeatherConstraints(boolean thunderstorm, boolean drizzle, boolean rain, boolean snow,
                              boolean clearSky, boolean clouds, boolean extreme) {
        this.thunderstorm = thunderstorm;
        this.drizzle = drizzle;
        this.rain = rain;
        this.snow = snow;
        this.clearSky = clearSky;
        this.clouds = clouds;
        this.extreme = extreme;
    }

    public boolean isThunderstorm() {
        return thunderstorm;
    }

    public void setThunderstorm(boolean thunderstorm) {
        this.thunderstorm = thunderstorm;
    }

    public boolean isDrizzle() {
        return drizzle;
    }

    public void setDrizzle(boolean drizzle) {
        this.drizzle = drizzle;
    }

    public boolean isRain() {
        return rain;
    }

    public void setRain(boolean rain) {
        this.rain = rain;
    }

    public boolean isSnow() {
        return snow;
    }

    public void setSnow(boolean snow) {
        this.snow = snow;
    }

    public boolean isClearSky() {
        return clearSky;
    }

    public void setClearSky(boolean clearSky) {
        this.clearSky = clearSky;
    }

    public boolean isClouds() {
        return clouds;
    }

    public void setClouds(boolean clouds) {
        this.clouds = clouds;
    }

    public boolean isExtreme() {
        return extreme;
    }

    public void setExtreme(boolean extreme) {
        this.extreme = extreme;
    }
}
