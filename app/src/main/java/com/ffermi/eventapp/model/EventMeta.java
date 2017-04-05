package com.ffermi.eventapp.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class EventMeta {

    private String eventId;
    private String description;
    private String hashtag;
    private WeatherConstraints weatherContraint;

    public EventMeta() {
    }

    public EventMeta(String description, String hashtag, WeatherConstraints weatherContraint) {
        this.description = description;
        this.hashtag = hashtag;
        this.weatherContraint = weatherContraint;
    }
    @Exclude
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public WeatherConstraints getWeatherContraint() {
        return weatherContraint;
    }

    public void setWeatherContraint(WeatherConstraints weatherContraint) {
        this.weatherContraint = weatherContraint;
    }
}
