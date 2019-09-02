package com.vovoapps.calenderevent;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;
import com.stacktips.view.CalendarListener;
import com.stacktips.view.CustomCalendarView;
import com.stacktips.view.DayDecorator;
import com.stacktips.view.DayView;
import com.vovoapps.calenderevent.Adopters.Adopter_Event_list;
import com.vovoapps.calenderevent.Adopters.Adopter_USer_Event_list;
import com.vovoapps.calenderevent.Interfaces.Event_Listtiner;
import com.vovoapps.calenderevent.Utills.MyActivity;
import com.vovoapps.calenderevent.Utills.Utills;
import com.vovoapps.calenderevent.models.Event;
import com.vovoapps.calenderevent.models.User;
import com.vovoapps.calenderevent.models.User_Event;
import com.vovoapps.calenderevent.models.User_Event2;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends MyActivity {

    BottomSheetBehavior add_event_bottom_behaviour;
    BottomSheetBehavior add_event_type_bottom_behaviour;
    Context context;
    ArrayList<Event> events = new ArrayList<>();
    RecyclerView recyclerView;
    Adopter_Event_list adopter_event_list;
    CustomCalendarView calendarView;
    Calendar currentCalendar;
    Date selectedDate;
    int selected_Color = -1;
    int user_id = 1;
    boolean isForstime = true;
    ArrayList<User_Event2> userEvents = new ArrayList<>();
    ArrayList<User_Event2> userEventsUnique = new ArrayList<>();
    Database_Handler database_handler;
    int chosen_color = -1;
    RecyclerView recyclerView_user_list;
    Adopter_USer_Event_list adopterUSerEventList;
    User currentUser;
    int repeat_selection = 0;


    public void back_mainactivity(View view) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);
        update_current_date();
        currentCalendar = Calendar.getInstance(Locale.getDefault());
        recyclerView_user_list = (RecyclerView) findViewById(R.id.event_details_recyleView);
        if (getIntent() != null) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    user_id = getIntent().getIntExtra("user_id", -1);
                    String name = getIntent().getStringExtra("user_name");
                    String eamil = getIntent().getStringExtra("user_email");
                    currentUser = new User(name, eamil, "", user_id);
                    ((ImageView) findViewById(R.id.imageButton_search)).setImageBitmap(database_handler.getuser_pic(currentUser));
                    ((TextView) findViewById(R.id.edt_search_txt)).setText(name.toUpperCase());
                }
            });
        }


        context = this;
//        CalendarView calendarView=(CalendarView)findViewById(R.id.calendarView);
        database_handler = new Database_Handler(MainActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.add_user_event_recylerView);
        adopter_event_list = new Adopter_Event_list(events, context, database_handler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adopter_event_list);
        adopterUSerEventList = new Adopter_USer_Event_list(userEventsUnique, context, database_handler);
        recyclerView_user_list.setLayoutManager(linearLayoutManager2);
        recyclerView_user_list.setAdapter(adopterUSerEventList);


        load_db_event();


        adopter_event_list.setClicked_listiner(new Event_Listtiner() {
            @Override
            public void edit(Event event, int pos) {

            }

            @Override
            public void delete(Event event, int pos) {

            }

            @Override
            public void clicked(Event event, int pos) {

                User_Event user_event = new User_Event(-1, event.getId(), user_id, selectedDate.getTime());
                if (database_handler.put_user_event(user_event)) {
                    add_event_bottom_behaviour.setState(BottomSheetBehavior.STATE_HIDDEN);
                    if (repeat_selection != 0) {
                        int evvent_id = database_handler.get_event_id(user_event);

                        if (database_handler.put_repeatness(user_id, user_event.getType(), repeat_selection, 0)) {
                            toast(repeat_selection + "Event id" + event);
                            repeat_selection = 0;

                        }
                    }
                    snake_bar_make("Event is added");
                    load_db_event();
                }


            }
        });

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    events.addAll(database_handler.getEvents());
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                toast(events.size()+" total size");
                adopter_event_list.notifyDataSetChanged();
            }
        });

        add_event_bottom_behaviour = BottomSheetBehavior.from(((ConstraintLayout) findViewById(R.id.add_user_event_constrainlayout)));
        add_event_type_bottom_behaviour = BottomSheetBehavior.from(((ConstraintLayout) findViewById(R.id.bottome_ad_newUser)));
        add_event_bottom_behaviour.setHideable(true);
        add_event_type_bottom_behaviour.setHideable(true);
        add_event_bottom_behaviour.setState(BottomSheetBehavior.STATE_HIDDEN);

        add_event_type_bottom_behaviour.setState(BottomSheetBehavior.STATE_HIDDEN);
//        add_event_bottom_behaviour.setPeekHeight(1);
        //customize the dialog however you want


        calendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                selectedDate = date;


                add_event_bottom_behaviour.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                ((Button) findViewById(R.id.add_new_event_type)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        add_event_type_bottom_behaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                        add_event_bottom_behaviour.setState(BottomSheetBehavior.STATE_HIDDEN);


                    }
                });

                ((Button) findViewById(R.id.chose_event_color)).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(final View view) {
                        final ColorPicker cp = new ColorPicker(MainActivity.this, new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
                        cp.setCancelable(false);


                        cp.show();
                        cp.setCallback(new ColorPickerCallback() {
                            @Override
                            public void onColorChosen(int color) {
                                view.setBackgroundColor(color);
                                chosen_color = color;
                                cp.dismiss();
                            }
                        });
                        return false;
                    }
                });

                ((Button) findViewById(R.id.chose_event_color)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {

                        int color = android.graphics.Color.argb(100, new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
                        view.setBackgroundColor(color);
                        chosen_color = color;

                        final ColorPicker cp = new ColorPicker(MainActivity.this, new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
                        cp.setCancelable(false);

//                        cp.show();
                        cp.setCallback(new ColorPickerCallback() {
                            @Override
                            public void onColorChosen(int color) {
                                view.setBackgroundColor(color);
                                chosen_color = color;
                                cp.dismiss();
                            }
                        });
//                        ColorChooserDialog dialog = new ColorChooserDialog(context);
//                        dialog.setCancelable(false);
//                        dialog.setTitle("Select Event Color");
//                        dialog.setColorListener(new ColorListener() {
//                            @Override
//                            public void OnColorClick(View v, int color) {
//                                //do whatever you want to with the values
//                                view.setBackgroundColor(color);
//                                chosen_color=color;
//                            }
//                        });
//                        dialog.show();


                    }
                });

                ((Button) findViewById(R.id.ad_new_user_done)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText edt_title = (EditText) findViewById(R.id.edt_event_title);
                        final EditText edt_desc = (EditText) findViewById(R.id.edt_event_desc);
                        final EditText edt_hors = (EditText) findViewById(R.id.edt_event_hour);
                        final EditText edt_price = (EditText) findViewById(R.id.edt_event_hour_price);
                        if (edt_title.getText().toString().isEmpty()) {
                            snake_bar_make("Please enter event title");

                            return;
                        }
                        if (chosen_color == -1) {

                            snake_bar_make("Please chose a color for this event type");
                            return;
                        }
                        final Event event = new Event(-1, edt_title.getText().toString(), edt_desc.getText().toString().isEmpty() ? "" : edt_desc.getText().toString(), chosen_color, Integer.parseInt(edt_hors.getText().toString()), Integer.parseInt(edt_price.getText().toString()));
                        int rslt = database_handler.put_event_type(event);
                        switch (rslt) {
                            case 0:
                                toast("Event added");
                                add_event_type_bottom_behaviour.setState(BottomSheetBehavior.STATE_HIDDEN);
                                new Handler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        events.clear();
                                        events.addAll(database_handler.getEvents());
                                        adopter_event_list.notifyDataSetChanged();
                                        filter_user_events();
                                    }
                                });
                                break;
                            case 2:
                                toast("Event already exists");
                                break;
                            case 3:
                                toast("Something went wrong");

                                break;
                        }
                    }
                });

                List<DayDecorator> decorators = new ArrayList<>();
                decorators.add(new DisabledColorDecorator());
                calendarView.setDecorators(decorators);

//                calendarView.refreshCalendar(null);
                calendarView.refreshCalendar(currentCalendar);
//                calendarView.setSelected(true);
            }


            @Override
            public void onMonthChanged(Date date) {

                update_current_date();
//                calendarView.markDayAsCurrentDay();
//                calendarView.refreshCalendar(Calendar.getInstance());


            }
        });


//        startActivity(new Intent(this, Add_user_event.class));

        if (database_handler == null) {
            toast("laodded not databse");
        }

//    database_handler.put_user(new User("waqas","vovo","asd",1));
//    database_handler.put_event(new User_Event(0,12,12,64655665));
        new Handler().post(new Runnable() {
            @Override
            public void run() {

            }
        });


//        database_handler.put_repeatness(12,12,12,1);
//        database_handler.put_repeatness(12,12,12,1);
//        database_handler.put_repeatness(12,12,12,1);
        ////===========>
        database_handler.get_all_repeat_event();
        adopter_event_list.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dilouge_repeat__selection);
//                dialog. requestWindowFeature(Window.FEATURE_NO_TITLE);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.show();

                Button button0 = (Button) dialog.findViewById(R.id.btn_dilogue_01);
                Button button1 = (Button) dialog.findViewById(R.id.btn_dilogue1);
                Button button2 = (Button) dialog.findViewById(R.id.btn_dilogue_0);
                Button button3 = (Button) dialog.findViewById(R.id.btn_dilogue2);
                Button button4 = (Button) dialog.findViewById(R.id.btn_dilogue4);

                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        int id = view.getId();
                        switch (id) {
                            case R.id.btn_dilogue_01:
                                repeat_selection = 0;
                                break;
                            case R.id.btn_dilogue_0:
                                repeat_selection = 1;

                                break;
                            case R.id.btn_dilogue1:
                                repeat_selection = 7;

                                break;
                            case R.id.btn_dilogue2:
                                repeat_selection = 30;

                                break;
                            case R.id.btn_dilogue4:
                                repeat_selection = 30 * 12;

                                break;

                        }
                    }
                };

//                if(button0==null)
//                    return;

                button0.setOnClickListener(listener);
                button1.setOnClickListener(listener);
                button2.setOnClickListener(listener);
                button3.setOnClickListener(listener);
                button4.setOnClickListener(listener);
            }
        });


    }

    public void filter_user_events() {
        userEventsUnique.clear();
        for (User_Event2 event2 : userEvents) {
            if (Utills.get_userEvent_Counts(userEventsUnique, event2) == 0) {
                int total_count = Utills.get_userEvent_Counts(userEvents, event2);
                event2.setCount(total_count);
                userEventsUnique.add(event2);
            }
        }

    }

    public void update_current_date() {
        ((TextView) findViewById(R.id.edt_search_txt_time)).setText(calendarView.get_title());
    }

    public void load_db_event() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                userEvents.clear();
                try {
                    userEvents.addAll(database_handler.get_all_userEVENT(user_id));
                    filter_user_events();
                    adopterUSerEventList.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
//                toast("got events"+userEvents.size());
                List<DayDecorator> decorators = new ArrayList<>();
                decorators.add(new DisabledColorDecorator());
                calendarView.setDecorators(decorators);
                calendarView.refreshCalendar(currentCalendar);


            }
        });

    }

    private class DisabledColorDecorator implements DayDecorator {
        @Override
        public void decorate(DayView dayView) {

            for (User_Event2 event2 : userEvents) {
                Time time = new Time(event2.getTime());
                java.sql.Date date = new java.sql.Date(event2.getEvent_date());

                Log.e("TAG", dayView.getDate().compareTo(date) + "");
                if (dayView.getDate().getDate() == date.getDate() && dayView.getDate().getMonth() == date.getMonth() && dayView.getDate().getYear() == date.getYear()) {
                    if (event2.getColor() == Color.BLACK) {
                        dayView.setTextColor(Color.WHITE);
                    }
                    dayView.setBackgroundColor(event2.getColor());

                }

            }
        }
    }


}
