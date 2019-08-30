package com.vovoapps.calenderevent.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import com.vovoapps.calenderevent.Database_Handler;
import com.vovoapps.calenderevent.R;
import com.vovoapps.calenderevent.Utills.MyActivity;
import com.vovoapps.calenderevent.models.Event;

public class Add_event_type extends MyActivity {

    Context context;
    int chosen_color=-1;
    Database_Handler database_handler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event_type);
        context=this;
        database_handler=new Database_Handler(context);

        ((Button)findViewById(R.id.chose_event_color)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
//                ColorChooserDialog dialog = new ColorChooserDialog(context);
//                dialog.setCancelable(false);
//                dialog.setTitle("Select Event Color");
//                dialog.setColorListener(new ColorListener() {
//                    @Override
//                    public void OnColorClick(View v, int color) {
//                        //do whatever you want to with the values
//                        view.setBackgroundColor(color);
//                        chosen_color=color;
//                    }
//                });
//                dialog.show();
            }
        });

        ((Button)findViewById(R.id.ad_new_user_done)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edt_title=(EditText)findViewById(R.id.edt_event_title);
                EditText edt_desc=(EditText)findViewById(R.id.edt_event_desc);
                if(edt_title.getText().toString().isEmpty()){
                    snake_bar_make("Please enter event title");

                    return;
                }
                if(chosen_color==-1){
                    snake_bar_make("Please chose a color for this event type");
                    return;
                }
                Event event;//new Event(-1,edt_title.getText().toString(),edt_desc.getText().toString().isEmpty()?"":edt_desc.getText().toString(),chosen_color);
               int rslt=0;//database_handler.put_event_type(event);
               switch (rslt){
                   case 0:
                       snake_bar_make("Event added");
                       break;
                   case 2:
                       snake_bar_make("Event already exists");
                       break;
                   case 3:
                       snake_bar_make("Something went wrong");

                       break;
               }




            }
        });

    }
}
