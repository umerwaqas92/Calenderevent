package com.vovoapps.calenderevent.models;

public class Holyday {
 int id;
 String title;
 String desc;
 long date;

    public Holyday(int id, String title, String desc, long date) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
