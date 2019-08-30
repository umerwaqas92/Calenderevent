package com.vovoapps.calenderevent.landscape_calender;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.vovoapps.calenderevent.R;
import com.vovoapps.calenderevent.landscape_calender.Header_Adopter;


import java.util.ArrayList;

public class Landscap_Calender extends AppCompatActivity {


    RecyclerView header_rec;
    ArrayList<Header_Adopter.Header_Item> headerItems=new ArrayList<>();
    Header_Adopter header_adopter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landscap__calender);
        header_rec=(RecyclerView)findViewById(R.id.landscap_header_cell_recylerVeiw);
        context=this;
        header_adopter=new Header_Adopter(headerItems,this);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false);
        header_rec.setLayoutManager(linearLayoutManager);
        header_rec.setAdapter(header_adopter);

    fill_adopter();

        //  __________
        // | | | | | |
        // | | | | | |
        // | | | | | |
    }


    public void fill_adopter(){
        int days=30;
        for (int i=0;i<days;i++){
            Header_Adopter.Header_Item header_item=new Header_Adopter.Header_Item("day ",i);

            headerItems.add(header_item);

        }

        header_adopter.notifyDataSetChanged();
    }
}
