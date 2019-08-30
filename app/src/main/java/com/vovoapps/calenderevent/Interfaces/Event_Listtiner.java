package com.vovoapps.calenderevent.Interfaces;

import com.vovoapps.calenderevent.models.Event;
import com.vovoapps.calenderevent.models.User;

public interface Event_Listtiner {
    public void edit(Event event, int pos);
    public void delete(Event event, int pos);
    public void clicked(Event event, int pos);
}
