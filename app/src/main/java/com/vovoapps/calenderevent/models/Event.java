package com.vovoapps.calenderevent.models;

public class Event {
    int id;
    String title;
    String desc;
    int event_color;
    int hour;
    int max_event_price;

    public int getHour() {
        return hour;
    }

    public int getMax_event_price() {
        return max_event_price;
    }

    public Event(int id, String title, String desc, int event_color, int hour, int max_event_price) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.event_color = event_color;
        this.hour = hour;
        this.max_event_price = max_event_price;
    }

    public int getEvent_color() {
        return event_color;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }
}
