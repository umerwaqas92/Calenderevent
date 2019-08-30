package com.vovoapps.calenderevent.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.vovoapps.calenderevent.Adopters.Adopter_Event_list;
import com.vovoapps.calenderevent.Database_Handler;
import com.vovoapps.calenderevent.Interfaces.Event_Listtiner;
import com.vovoapps.calenderevent.R;
import com.vovoapps.calenderevent.Utills.MyActivity;
import com.vovoapps.calenderevent.models.Event;

import java.util.ArrayList;

public class Add_user_event extends MyActivity {



    Database_Handler database_handler;
     Context context;
     ArrayList<Event> events=new ArrayList<>();
     RecyclerView recyclerView;
     Adopter_Event_list adopter_event_list;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user_event);
        context=this;
        database_handler=new Database_Handler(this);
        recyclerView=(RecyclerView)findViewById(R.id.add_user_event_recylerView);
        adopter_event_list=new Adopter_Event_list(events,context,database_handler);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adopter_event_list);





        adopter_event_list.setClicked_listiner(new Event_Listtiner() {
            @Override
            public void edit(Event event, int pos) {

            }

            @Override
            public void delete(Event event, int pos) {

            }

            @Override
            public void clicked(Event event, int pos) {

            }
        });

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                events.addAll(database_handler.getEvents());
                toast(events.size()+" total size");
                adopter_event_list.notifyDataSetChanged();
            }
        });

    }
}
