package com.vovoapps.calenderevent.Services;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.vovoapps.calenderevent.Database_Handler;
import com.vovoapps.calenderevent.R;
import com.vovoapps.calenderevent.activities.Users_List;
import com.vovoapps.calenderevent.models.Event_Repeat;
import com.vovoapps.calenderevent.models.User_Event;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class Service_AI extends Service {
    Database_Handler database_handler;
    ArrayList<Event_Repeat> event_repeats;

    public Service_AI() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        database_handler=new Database_Handler(this);
            process();

    }

    public void  process(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                sendNotification_pause("","",45.5f);
                event_repeats=database_handler.get_all_repeat_event();
                Log.e("SERVICEAI","total laodeed repeat events"+event_repeats.size());
                long current_time=System.currentTimeMillis();



                for (Event_Repeat eventRepeat:event_repeats) {
                    if(eventRepeat.getRepeat_time()==1){
                        ///for faily updates
                        long duration=current_time-eventRepeat.getTime();

                        Date date=new Date();
                        date.setTime(current_time);
                        Date date1=new Date();
                        date1.setTime(eventRepeat.getTime());

                        if(duration%(1*86400000)>0){
                            log("event shoud occurs usr"+eventRepeat.getUsr() +" event_type"+eventRepeat.getEvent_id());
                            database_handler.putUser_event(new User_Event(-1,eventRepeat.getEvent_id(),eventRepeat.getUsr(),current_time));
                        }
                    }
                }
            }
        });
    }

    protected void log(String s){
        Log.e("SERVICEAI",s);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    process();
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }



    public void sendNotification_pause(String title, String duration, float distance) {

        RemoteViews notificationLayout = null;

//        notificationLayout= new RemoteViews(getPackageName(), R.layout.custom_notification_location);


        Intent intent = new Intent(getApplicationContext(), Service_AI.class);
//        intent.setAction(ACTION_FORWARD);
        PendingIntent pendIntentapp_oopen = PendingIntent.getActivity(this, 0, new Intent(this, Users_List.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT), 0);
//


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CANNEL01")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(false)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .setOnlyAlertOnce(true)
//                .addAction(new NotificationCompat.Action(R.drawable.ic_close_black_24dp,"End",pending_intent_end))
                .setContentIntent(pendIntentapp_oopen);


        builder.setOngoing(true);

//
//
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            @SuppressLint("WrongConstant") NotificationChannel channel = new NotificationChannel("CANNEL01", "Location", importance);
            channel.setDescription("All the different interval tones");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            startForeground(101,builder.build());
//            notificationManager.notify(101, builder.build());

        }else {
            startForeground(101,builder.build());

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//            notificationManager.notify(101, builder.build());
        }
//        startForeground(0,builder.build());





// notificationId is a unique int for each notification that you must define
    }

}
