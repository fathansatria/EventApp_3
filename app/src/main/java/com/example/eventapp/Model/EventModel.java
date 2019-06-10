package com.example.eventapp.Model;

import java.util.Date;

public class EventModel {

    private String eventName;
    private int eventId;
    private String eventPlace = null;
    private String description= null;

    //setter
//    public void setEventDate(Date eventDate) {
//        this.eventDate = eventDate;
//    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventPlace(String eventPlace) {
        this.eventPlace = eventPlace;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    //getter
    public String getEventName() {
        return eventName;
    }

//    public Date getEventDate() {
//        return eventDate;
//    }

    public String getEventPlace() {
        return eventPlace;
    }

    public String getDescription() {
        return description;
    }

    public int getEventId() {
        return eventId;
    }
}
