package com.vovoapps.calenderevent.models;

public class Event_Repeat {
    int id;
    int event_id;
    int usr;
    long repeat_time;
    long time;;
    int event_end;






    public int getEvent_id() {
        return event_id;
    }

    public int getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public long getRepeat_time() {
        return repeat_time;
    }

    public int getUsr() {
        return usr;
    }

    public Event_Repeat(int id, int event_id, int usr, long repeat_time, long time, int event_end) {
        this.id = id;
        this.event_id = event_id;
        this.usr = usr;
        this.repeat_time = repeat_time;
        this.time = time;
        this.event_end = event_end;
    }
}
