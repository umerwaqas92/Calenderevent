package com.vovoapps.calenderevent.Utills;

import com.vovoapps.calenderevent.models.User_Event2;

import java.util.ArrayList;

public class Utills {
    public static int get_userEvent_Counts(ArrayList<User_Event2> events, User_Event2 event2){
        int count=0;
        for (User_Event2 event21:events) {
            if(event2.isEqal(event21)){
                count++;
            }
        }
        return count;
    }
}
