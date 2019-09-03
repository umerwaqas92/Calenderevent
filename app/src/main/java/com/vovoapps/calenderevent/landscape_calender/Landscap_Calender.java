package com.vovoapps.calenderevent.landscape_calender;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vovoapps.calenderevent.Database_Handler;
import com.vovoapps.calenderevent.R;
import com.vovoapps.calenderevent.Utills.Utills;
import com.vovoapps.calenderevent.activities.Add_event_type;
import com.vovoapps.calenderevent.models.User;
import com.vovoapps.calenderevent.models.User_Event2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Landscap_Calender extends AppCompatActivity {


    RecyclerView header_rec;
    ArrayList<Header_Adopter.Header_Item> headerItems = new ArrayList<>();
    Header_Adopter header_adopter;
    Context context;

    TableLayout tableLayout;


    ArrayList<User_Event2> events = new ArrayList<>();
    ArrayList<User> users = new ArrayList<>();


    Database_Handler database_handler;

    ArrayList<User_Event2> userEventsUnique = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landscap__calender);

        database_handler = new Database_Handler(this);

        tableLayout = findViewById(R.id.tableLayout);

        GridLayout gridLayout;


        // Create a new table row.
        TableRow headerRow = new TableRow(this);

        // Set new table row layout parameters.
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);

        headerRow.setLayoutParams(layoutParams);

        for (int i = 0; i < 32; i++) {
            TextView textView = new TextView(this);
            textView.setPadding(12, 12, 12, 12);
            if (i == 0) {
                textView.setText("Users\n ");
                textView.setBackgroundColor(Color.RED);
                textView.setTextColor(Color.WHITE);
                headerRow.addView(textView, 0);
            } else {
                textView.setBackgroundColor(Color.DKGRAY);
                textView.setTextColor(Color.WHITE);
                textView.setText("Date\n" + i);
                headerRow.addView(textView, i);
            }


        }


        tableLayout.addView(headerRow);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    users.clear();
                    users.addAll(database_handler.getAllUsers());


                    for (int k = 0; k < users.size(); k++) {


                        User user = users.get(k);

                        events.clear();
                        events.addAll(database_handler.get_all_userEVENT(user.getUsr_id()));
                        filter_user_events();
                        TableRow newRow = new TableRow(Landscap_Calender.this);

                        // Set new table row layout parameters.
                        TableRow.LayoutParams newParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);

                        newRow.setLayoutParams(newParams);

                        newRow.setClickable(true);
                        newRow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(Landscap_Calender.this, Add_event_type.class));
                            }
                        });

                      /*  TextView sample = (TextView) newRow.getChildAt(2);
                        String result=sample.getText().toString();*/


                        Log.e("LandScap-EventsSize", "" + events.size());


                        for (int j=0;j<32;j++){
//                            User_Event2 event = events.get(i);

                            for (int i = 0; i < events.size(); i++) {

                                User_Event2 event = events.get(i);
                                Calendar calendar=Calendar.getInstance();
                                calendar.setTimeInMillis(event.getEvent_date());


//                                Calendar calendar = Calendar.getInstance();
//                                Date date = new Date();
//                                date.setTime(event.getEvent_date());
//                                calendar.setTime(date);

                                Log.e("LandScap-EventDate", "" + event.getEvent_date());
                                Log.e("LandScap-EventCalendar", "" + calendar.get(Calendar.DATE));
                                Log.e("LandScap-EventIndex", "" + i);


                                if (i == 0 &&j==0) {
                                    ImageView imageView = new ImageView(Landscap_Calender.this);
                                    imageView.setPadding(16, 16, 16, 16);
                                    imageView.setImageBitmap(database_handler.getuser_pic(user));
                                    newRow.addView(imageView);

                                }else if (i == 0 ) {
                                    TextView rowTextView = new TextView(Landscap_Calender.this);
                                    rowTextView.setPadding(16, 16, 16, 16);
                                    rowTextView.setText("");
                                    newRow.addView(rowTextView);

                                }

                                    if(calendar.get(Calendar.DAY_OF_MONTH)==j){

                                        TextView rowTextView = new TextView(Landscap_Calender.this);
                                        rowTextView.setPadding(16, 16, 16, 16);
                                        rowTextView.setBackgroundColor(event.getColor());
                                        rowTextView.setTextColor(Color.BLACK);
                                        Log.e("EVENT_NAME",event.getTitle()+i);
                                        Log.e("EVENT_NAME",event.getId()+", "+i);
                                        rowTextView.setText("" + event.getTitle()+i);
                                        newRow.addView(rowTextView);



                           /*     for (int j = 1; j < 32; j++) {

                                    if (j == calendar.get(Calendar.DATE)) {


                                    }else{
                                        TextView rowTextView = new TextView(Landscap_Calender.this);
                                        rowTextView.setPadding(16, 16, 16, 16);
                                        rowTextView.setBackgroundColor(Color.WHITE);
                                        rowTextView.setTextColor(Color.WHITE);
                                        newRow.addView(rowTextView, i);
                                    }
                                }*/
                                }
                            /*
                            for (int j = 0; j < 32; j++) {

                            }
*/

                            }



                        }




                        tableLayout.addView(newRow);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
//                toast(events.size()+" total size");
            }
        });


        header_rec = (RecyclerView) findViewById(R.id.landscap_header_cell_recylerVeiw);
        context = this;
        header_adopter = new Header_Adopter(headerItems, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        header_rec.setLayoutManager(linearLayoutManager);
        // header_rec.setAdapter(header_adopter);

        //   fill_adopter();

        //  __________
        // | | | | | |
        // | | | | | |
        // | | | | | |
    }


    public void fill_adopter() {
        int days = 30;
        for (int i = 0; i < days; i++) {
            Header_Adopter.Header_Item header_item = new Header_Adopter.Header_Item("day ", i);

            headerItems.add(header_item);

        }

        header_adopter.notifyDataSetChanged();
    }


    public void filter_user_events() {
        userEventsUnique.clear();
        for (User_Event2 event2 : events) {
            if (Utills.get_userEvent_Counts(userEventsUnique, event2) == 0) {
                int total_count = Utills.get_userEvent_Counts(events, event2);
                event2.setCount(total_count);
                userEventsUnique.add(event2);
            }
        }

    }
}
