package com.vovoapps.calenderevent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.Toast;

import com.vovoapps.calenderevent.models.Event;
import com.vovoapps.calenderevent.models.Event_Repeat;
import com.vovoapps.calenderevent.models.Holyday;
import com.vovoapps.calenderevent.models.User;
import com.vovoapps.calenderevent.models.User_Event;
import com.vovoapps.calenderevent.models.User_Event2;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Database_Handler extends SQLiteOpenHelper {


    private Context context;

    public Database_Handler(Context context) {
        super(context, CONTSANTS.Databse.DB_NAME, null, 6);
        this.context = context;

    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create_table_users = "CREATE TABLE users (user_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,user_name TEXT,user_email TEXT ,user_pic TEXT,reg_time INTEGER);";

        String create_table_event_holidays = "CREATE TABLE event_holidays (event_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,event_title TEXT,event_desc TEXT,event_date INTEGER );";

        String create_table_event_type = "CREATE TABLE event_type (event_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,event_title TEXT,event_desc TEXT,event_color INTEGER,event_hours INTEGER,event_price INTEGER);";
        String create_table_event_user = "CREATE TABLE user_event (event_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,event_type TEXT,event_user INTEGER,event_date INTEGER,time INTEGER);";
        String create_table_user_pic = "CREATE TABLE user_pic (id ,pic BLOB);";
        String create_table_event_repeat = "CREATE TABLE event_repeat (id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,event_id INTEGER,event_repeat_time INTEGER,user INTEGER,event_end INTEGER,time INTEGER );";

        sqLiteDatabase.execSQL(create_table_users);
        sqLiteDatabase.execSQL(create_table_event_user);
        sqLiteDatabase.execSQL(create_table_event_type);
        sqLiteDatabase.execSQL(create_table_event_holidays);
        sqLiteDatabase.execSQL(create_table_user_pic);
        sqLiteDatabase.execSQL(create_table_event_repeat);


    }

    private void toast(String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public boolean put_image(Bitmap bitmap, int id) {
        ContentValues values = new ContentValues();
        byte[] image_bye = getBitmapAsByteArray(bitmap);

        values.put("pic", image_bye);
        values.put("id", id);
//        try {
        getWritableDatabase().insert("user_pic", null, values);
        return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
    }

    public ArrayList<Event_Repeat> get_all_repeat_event() {
        ArrayList<Event_Repeat> eventRepeats = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query("event_repeat", new String[]{"id", "event_id", "user", "event_repeat_time", "event_end", "time"}, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() == 0) {
                return eventRepeats;
            }
            cursor.moveToFirst();
            do {
                Event_Repeat eventRepeat = new Event_Repeat(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getLong(3), cursor.getLong(5), cursor.getInt(4));
                eventRepeats.add(eventRepeat);
            } while (cursor.moveToNext());

        }
//        toast("total repeats"+eventRepeats.size());
        return eventRepeats;
    }

    public boolean put_repeatness(int usr, int event, int duration_day, int end_after) {
        Long time = System.currentTimeMillis();
        ContentValues values = new ContentValues();
        values.put("user", usr);
        values.put("event_id", event);
        values.put("event_repeat_time", duration_day);
        values.put("event_end", end_after);
        values.put("time", time);

        long rslt = getWritableDatabase().insert("event_repeat", null, values);
        close_connection();
        toast("rpeat ddded" + rslt);
        if (rslt == 0) {
            return false;
        } else {
            return true;
        }


    }

    public boolean update_image(Bitmap bitmap, int id) {
        ContentValues values = new ContentValues();
        byte[] image_bye = getBitmapAsByteArray(bitmap);
        values.put("pic", image_bye);
        int rslt = getWritableDatabase().update("user_pic", values, " id=? ", new String[]{Integer.toString(id)});
        toast(id + "image updated" + rslt);

        return true;
    }

    public boolean put_holidays(Holyday holyday) {
        ContentValues values = new ContentValues();
//        values.put("event_id",holyday.getId());
        values.put("event_title", holyday.getTitle());
        values.put("event_desc", holyday.getDesc());
        values.put("event_date", holyday.getDate());


        try {
            getWritableDatabase().insert("user_event", null, values);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            toast(e.toString());
            return false;
        }
    }


    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();

        String coulms[] = {"user_id", "user_name", "user_email", "user_pic"};
        Cursor cursor
                = getReadableDatabase().query("users", coulms, "", null, null, null, "reg_time DESC", null);

        if (cursor != null) {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            do {
                users.add(new User(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(0)));
            } while (cursor.moveToNext());

            return users;
        } else {
            return null;
        }

    }

    public boolean putUser_event(User_Event user_event) {
        ContentValues values = new ContentValues();
        long time = System.currentTimeMillis();

//        values.put("event_id",user_event.getId());
        values.put("event_type", user_event.getType());
        values.put("event_user", user_event.getUser());
        values.put("event_date", user_event.getDate());
        values.put("time", time);


        try {
            getWritableDatabase().insert("user_event", null, values);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            toast(e.toString());
            return false;
        }
    }

    public boolean is_event_type_exists(Event event) {

        Cursor cursor = getReadableDatabase().query(CONTSANTS.Databse.Tables.Event_Type, new String[]{"event_title"}, "event_color=?",
                new String[]{event.getEvent_color() + ""}, null, null, null
        );
        cursor.moveToFirst();
//        toast(cursor.getCount()+" count ");
        if (cursor.getCount() != 0) {
            return true;
        } else {
            return false;
        }
    }


    public int put_event_type(Event event) {
        ContentValues values = new ContentValues();
        values.put("event_title", event.getTitle());
        values.put("event_desc", event.getTitle());
        values.put("event_color", event.getEvent_color());
        values.put("event_hours", event.getHour());
        values.put("event_price", event.getMax_event_price());
        if (is_event_type_exists(event)) {
            return 2;
        }


        try {
            long rslt = getWritableDatabase().insert("event_type", null, values);
            if (rslt > 0) {
                return 0;

            } else {
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            toast(e.toString());
            return 3;
        }
    }


    public ArrayList<User_Event2> get_all_userEVENT(int user) {
        ArrayList<User_Event2> events = new ArrayList<>();

        String q = "SELECT event_id,(SELECT event_title from event_type where event_id=event_type) as title ,(SELECT event_color from event_type where event_id=event_type) as color ,event_date,time,(SELECT event_hours from event_type where event_id=event_type) as event_hours,(SELECT event_price from event_type where event_id=event_type) as event_price from user_event where event_user=" + user;
        Cursor cursor = getReadableDatabase().rawQuery(q, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                User_Event2 event = new User_Event2(cursor.getInt(0),
                        cursor.getString(1), cursor.getInt(2),
                        cursor.getLong(3), cursor.getLong(4), cursor.getInt(5), cursor.getInt(6));
                events.add(event);
            } while (cursor.moveToNext());

            return events;
        }
        return events;
    }

    public ArrayList<Event> getEvents() {
        ArrayList<Event> events = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(CONTSANTS.Databse.Tables.Event_Type, new String[]{"event_id", "event_title", "event_desc", "event_color", "event_hours", "event_price"}, "", null, null, null, null
        );

        if (cursor != null) {
            cursor.moveToFirst();
            do {
                Event event = new Event(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5));
                events.add(event);
            } while (cursor.moveToNext());

            return events;
        }
        return events;
    }


    public boolean put_user_event(User_Event user_event) {
        Long time = System.currentTimeMillis();
        ContentValues values = new ContentValues();
//        values.put("event_id",user_event.getId());
        values.put("event_type", user_event.getType());
        values.put("event_user", user_event.getUser());
        values.put("event_date", user_event.getDate());
        values.put("time", time);


        try {
            getWritableDatabase().insert("user_event", null, values);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            toast(e.toString());
            return false;
        }
    }


    public void close_connection() {
        this.close();
    }

    public int get_user_id(User user) {
        String[] coluns = {"user_id"};
        String[] selectionAurg = {user.getUsr_name(), user.getUsr_email()};

        Cursor cursor = getReadableDatabase().query("users", coluns, "user_name =? AND user_email=?", selectionAurg, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            try {
                return cursor.getInt(0);
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }

    public int get_event_id(User_Event event) {
        String[] coluns = {"event_id"};
        String[] selectionAurg = {event.getType() + "", event.getDate() + "", event.getUser() + ""};

        Cursor cursor = getReadableDatabase().query("user_event", coluns, "event_type =? AND event_date=? AND event_user=?", selectionAurg, null, null, null);
        if (cursor != null) {

            cursor.moveToFirst();
            try {
                return cursor.getInt(0);
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }


    public Bitmap getuser_pic(User user) {
        int i = get_user_id(user);

        String qu = "select pic from user_pic where id=" + i;
        Cursor cur = getReadableDatabase().rawQuery(qu, null);
//        toast("curos is null 0");

        if (cur.moveToFirst()) {
//            toast("curos is null 1");

            byte[] imgByte = cur.getBlob(0);
            cur.close();
            return getImage(imgByte);
        }
        if (cur != null && !cur.isClosed()) {
            cur.close();
        }

//        toast("curos is null 6");
        return null;
    }

    public boolean update_user(final User user, final Bitmap bitmap) {
        ContentValues values = new ContentValues();
        values.put("user_name", user.getUsr_name());
        values.put("user_email", user.getUsr_email());

        try {
            final int rslt = getWritableDatabase().update("users", values, " user_id=? ", new String[]{Integer.toString(user.getUsr_id())});
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    update_image(bitmap, user.getUsr_id());
//                    toast(rslt+user.getUsr_name()+user.getUsr_email()+"updating user"+user.getUsr_id());

                }
            });

            return true;
        } catch (Exception e) {
            toast(e.toString());
            e.printStackTrace();
            return false;
        }

    }

    public boolean delete_user(User user) {
        int rslt = getWritableDatabase().delete("user_pic", " id = ? ", new String[]{user.getUsr_id() + ""});
        int rslt2 = getWritableDatabase().delete("users", " user_id = ? ", new String[]{user.getUsr_id() + ""});
        toast(rslt + "deleted" + rslt2);
        return true;
    }

    public boolean put_user(User user, Bitmap img_bitmap) {
        Long time = System.currentTimeMillis();
//        if(get_user_id(user)!=-1){
//            return false;
//        }
        ContentValues values = new ContentValues();
        values.put("user_name", user.getUsr_name());
        values.put("user_pic", user.getUsr_pic());
        values.put("user_email", user.getUsr_email());
        values.put("reg_time", time);

//        try {
        getWritableDatabase().insert("users", null, values);
        put_image(img_bitmap, get_user_id(user));
        toast("user added");
        return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
    }
}
