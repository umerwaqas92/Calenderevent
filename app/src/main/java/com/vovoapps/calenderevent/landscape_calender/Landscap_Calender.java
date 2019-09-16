package com.vovoapps.calenderevent.landscape_calender;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.lang.UCharacter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vovoapps.calenderevent.Database_Handler;
import com.vovoapps.calenderevent.R;
import com.vovoapps.calenderevent.Utills.Utills;
import com.vovoapps.calenderevent.activities.Add_event_type;
import com.vovoapps.calenderevent.activities.Add_user_event;
import com.vovoapps.calenderevent.models.Event;
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
    TableLayout tableLayout_header_pic;
    TableLayout tableLayout_header_days;


    ArrayList<User_Event2> events = new ArrayList<>();
    ArrayList<User> users = new ArrayList<>();


    Database_Handler database_handler;

    ArrayList<User_Event2> userEventsUnique = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landscap__calender);
        context=this;

        database_handler = new Database_Handler(this);

        tableLayout = findViewById(R.id.tableLayout);
        tableLayout_header_pic = findViewById(R.id.tableLayout_header_pic_inner);
        tableLayout_header_days = findViewById(R.id.tableLayout_header_days);

        final ScrollView scrollView=(ScrollView)findViewById(R.id.vertical_scrol);
        final ScrollView scrollView_image=(ScrollView)findViewById(R.id.tableLayout_header_pic);
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                scrollView_image.scrollTo(0,Math.round(i1*1.1f));

            }
        });



        GridLayout gridLayout;


        // Create a new table row.
        TableRow headerRow = new TableRow(this);

        // Set new table row layout parameters.
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);

        headerRow.setLayoutParams(layoutParams);

        final ArrayList<Event> events_=database_handler.getEvents();


        for (int i = 1; i < 32+events_.size(); i++) {
//            i++;
            TextView textView = new TextView(this);
            textView.setPadding(12, 0, 12, 0);

                LinearLayout linearLayout=new LinearLayout(context);
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(300,300);







//                linearLayout.setLayoutParams(params);




                textView.setBackgroundColor(Color.DKGRAY);

                View view= LayoutInflater.from(context).inflate(R.layout.landsscape_cell_date,null,false);
                LinearLayout layout=(LinearLayout) view.findViewById(R.id.landscape_cell_date_linerlayout);
                TableRow.LayoutParams params1=new TableRow.LayoutParams(200,120);
                layout.setLayoutParams(params1);
                TextView textView1=(TextView)view.findViewById(R.id.landscape_cell_date_date_txt);
                TextView header_days_txt=(TextView)view.findViewById(R.id.header_days_txt);
                textView1.setText(i+"");
                Log.e("CALENDER","day"+i);




                textView.setTextColor(Color.WHITE);
                textView.setText("Date\n" + i);

//                layout.addView(textView);

//                headerRow.addView(layout, i);



            if(i<=31) {
                headerRow.addView(layout);
            }else {
                layout.setBackgroundColor(events_.get(i-32).getEvent_color());
                textView1.setText(events_.get(i-32).getTitle());
                textView1.setTextColor(Color.WHITE);
                header_days_txt.setVisibility(View.GONE);
                headerRow.addView(layout);

            }
//


        }


        tableLayout_header_days.addView(headerRow);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    users.clear();
                    users.addAll(database_handler.getAllUsers());


                    for (int k = 0; k < users.size(); k++) {


                        final User user = users.get(k);

                        events.clear();
                        events.addAll(database_handler.get_all_userEVENT(user.getUsr_id()));
                        filter_user_events();
                        TableRow newRow = new TableRow(Landscap_Calender.this);
                        TableRow newRow_image = new TableRow(Landscap_Calender.this);


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


                        for (int j=0;j<32+events_.size();j++){
//                            User_Event2 event = events.get(i);


                            if(j<31) {
                                for (int i = 0; i < events.size(); i++) {


                                    User_Event2 event = events.get(i);

                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTimeInMillis(event.getEvent_date());
//                                Calendar calendar = Calendar.getInstance();
//                                Date date = new Date();
//                                date.setTime(event.getEvent_date());
//                                calendar.setTime(date);

                                    Log.e("LandScap-EventDate", "" + event.getEvent_date());
                                    Log.e("LandScap-EventCalendar", "" + calendar.get(Calendar.DATE));
                                    Log.e("LandScap-EventIndex", "" + i);

                                    if (i == 0 && j == 0) {
                                        ImageView imageView = new ImageView(Landscap_Calender.this);
//                                    LinearLayout.LayoutParams layoutParams1=new LinearLayout.LayoutParams(100,100);

                                        imageView.setPadding(16, 0, 16, 21);
//                                    imageView.getLayoutParams().width=50;
//                                    imageView.getLayoutParams().height=50;
//                                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                        imageView.setImageBitmap(database_handler.getuser_pic(user));
                                        newRow_image.addView(imageView);

                                    }

                                    if (calendar.get(Calendar.DAY_OF_MONTH)-1 == j) {

                                        TextView rowTextView = new TextView(Landscap_Calender.this);
                                        rowTextView.setPadding(16, 16, 16, 16);
                                        rowTextView.setBackgroundColor(event.getColor());
                                        rowTextView.setTextColor(Color.BLACK);
                                        Log.e("EVENT_NAME", event.getTitle() + i);
                                        Log.e("EVENT_NAME", event.getId() + ", " + i);
                                        rowTextView.setText("" + event.getTitle() + i);

                                        View view = LayoutInflater.from(context).inflate(R.layout.landsscape_cell_event, null, false);
                                        LinearLayout layout = (LinearLayout) view.findViewById(R.id.landscape_cell_date_linerlayout);
                                        TableRow.LayoutParams params1 = new TableRow.LayoutParams(200, 120);
                                        layout.setLayoutParams(params1);
                                        TextView textView1 = (TextView) view.findViewById(R.id.landscape_cell_date_date_txt);
                                        LinearLayout background = (LinearLayout) view.findViewById(R.id.back_ground_color);
                                        background.setBackgroundColor(event.getColor());
                                        layout.setBackgroundColor(event.getColor());
                                        textView1.setText(event.getTitle());



//                                        if (j <= 31) {
                                            newRow.addView(layout,j);
                                            break;
//                                        }


                                    }  else if(i==events.size()-1) {
                                        View view = LayoutInflater.from(context).inflate(R.layout.landsscape_cell_event, null, false);
                                        LinearLayout layout = (LinearLayout) view.findViewById(R.id.landscape_cell_date_linerlayout);
                                        TableRow.LayoutParams params1 = new TableRow.LayoutParams(200, 120);
                                        layout.setLayoutParams(params1);
//                                        layout.setBackgroundColor(Color.RED);

//                                        if(newRow.getChildCount()<31){
                                            Log.e("TABLE",user.getUsr_name()+","+j);
                                        final int finalI = k;
                                        final int finalJ = j;
                                        layout.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Log.e("TABLE", finalI +","+ finalJ);
                                                    startActivity(new Intent(Landscap_Calender.this, Add_user_event.class));
                                                }
                                            });
                                        newRow.addView(layout,j);

                                        break;
//                                        }

                                    }




                                }
                            } else if(j>31){
                                View view= LayoutInflater.from(context).inflate(R.layout.landsscape_cell_event,null,false);
                                LinearLayout layout=(LinearLayout) view.findViewById(R.id.landscape_cell_date_linerlayout);
                                TableRow.LayoutParams params1=new TableRow.LayoutParams(200,120);
                                layout.setLayoutParams(params1);
                                TextView textView1=(TextView)view.findViewById(R.id.landscape_cell_date_date_txt);
                                LinearLayout background=(LinearLayout)view.findViewById(R.id.back_ground_color);



                                background.setBackgroundColor(events_.get(j-32).getEvent_color());
                                layout.setBackgroundColor(events_.get(j-32).getEvent_color());
//                                background.setAlpha(0.5f);
                                textView1.setText(""+database_handler.user_event_count(user.getUsr_id(),events_.get(j-32).getId()));
                                textView1.setTextColor(Color.WHITE);
                                newRow.addView(layout);



                            }



                        }





                        Log.e("TABLE","Row "+k+" count "+newRow.getChildCount());
                        tableLayout.addView(newRow);
                        tableLayout_header_pic.addView(newRow_image);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TABLE",e.toString());
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
