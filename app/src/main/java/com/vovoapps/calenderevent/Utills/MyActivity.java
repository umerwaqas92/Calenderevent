package com.vovoapps.calenderevent.Utills;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class MyActivity extends AppCompatActivity {
    public void snake_bar_make(String s){
        Snackbar.make(getCurrentFocus(),s,Snackbar.LENGTH_SHORT).show();
    }

    public void toast(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

}
