package com.example.eventapp;

public class NotifItem {
    
    private String notifTitle;
    private String notifContent;
    private String status;
    private String value;
    private String type;

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getNotifContent() {
        return notifContent;
    }

    public String getNotifTitle() {
        return notifTitle;
    }

    public String getStatus() {
        return status;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setNotifContent(String notifContent) {
        this.notifContent = notifContent;
    }

    public void setNotifTitle(String notifTitle) {
        this.notifTitle = notifTitle;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setType(String type) {
        this.type = type;
    }
}
