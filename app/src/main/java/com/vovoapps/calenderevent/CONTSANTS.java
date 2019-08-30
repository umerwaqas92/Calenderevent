package com.vovoapps.calenderevent;

public @interface CONTSANTS {

    public @interface REPEATS{
        final static int WEEKLY=7;
        final static int DAILY=1;
        final static int MONTHLY=30;
        final static int YERYL=30*12;


    }
    public @interface Databse{
        final String DB_NAME="calender_data.db";
        public @interface Tables{

            final String Event_Type="event_type";
            final String Event_Holidays="event_holidays";
            final String Users_Event="user_event";
            final String Users="users";

        }
    }

}

