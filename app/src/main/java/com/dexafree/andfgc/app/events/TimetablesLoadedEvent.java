package com.dexafree.andfgc.app.events;

import com.dexafree.andfgc.app.beans.Timetable;

import java.util.ArrayList;

/**
 * Created by Carlos on 08/07/14.
 */
public class TimetablesLoadedEvent {

    private ArrayList<Timetable> timetablesList;

    public TimetablesLoadedEvent(){}

    public TimetablesLoadedEvent(ArrayList<Timetable> timetablesList) {
        this.timetablesList = timetablesList;
    }

    public ArrayList<Timetable> getTimetablesList() {
        return timetablesList;
    }
}
