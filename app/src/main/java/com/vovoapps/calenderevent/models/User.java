package com.vovoapps.calenderevent.models;

import android.graphics.Bitmap;

import com.vovoapps.calenderevent.Database_Handler;

public class User {
    String usr_name;
    String usr_email;
    String usr_pic;
    int usr_id;





    public void setUsr_name(String usr_name) {
        this.usr_name = usr_name;
    }

    public void setUsr_email(String usr_email) {
        this.usr_email = usr_email;
    }


    public User(String usr_name, String usr_email, String usr_pic, int usr_id) {
        this.usr_name = usr_name;
        this.usr_email = usr_email;
        this.usr_pic = usr_pic;
        this.usr_id = usr_id;
    }

    public String getUsr_pic() {
        return usr_pic;
    }

    public String getUsr_name() {
        return usr_name;
    }

    public String getUsr_email() {
        return usr_email;
    }


    public int getUsr_id() {
        return usr_id;
    }
}
