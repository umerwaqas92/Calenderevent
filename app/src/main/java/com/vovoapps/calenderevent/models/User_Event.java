package com.vovoapps.calenderevent.models;

public class User_Event {
    int id;
    int type;
    int user;
    long date;

    public User_Event(int id, int type, int user, long date) {
        this.id = id;
        this.type = type;
        this.user = user;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public int getUser() {
        return user;
    }

    public long getDate() {
        return date;
    }
}
