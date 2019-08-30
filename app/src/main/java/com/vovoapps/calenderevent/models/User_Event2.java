package com.vovoapps.calenderevent.models;

public class User_Event2 {
    int id;
    String title;
    int color;
    long event_date;
    long time;

    int count=0;
    int hours;
    int max_pay;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getHours() {
        return hours;
    }

    public int getMax_pay() {
        return max_pay;
    }

    public User_Event2(int id, String title, int color, long event_date, long time, int hours, int max_pay) {
        this.id = id;
        this.title = title;
        this.color = color;
        this.event_date = event_date;
        this.time = time;
        this.hours = hours;
        this.max_pay = max_pay;
    }

    public boolean isEqal(User_Event2 event2){
        if(getTitle().equalsIgnoreCase(event2.getTitle()) && getColor()==event2.getColor()){
            return true;
        }else {
            return false;
        }
    }
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getColor() {
        return color;
    }

    public long getEvent_date() {
        return event_date;
    }

    public long getTime() {
        return time;
    }
}
