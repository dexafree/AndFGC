package com.dexafree.andfgc.app.events;

import java.util.ArrayList;

/**
 * Created by Carlos on 23/05/2014.
 */
public class AlertSearchFinishedEvent {

    private ArrayList<String> messages;
    private ArrayList<String> dates;

    public AlertSearchFinishedEvent(ArrayList<String> messages, ArrayList<String> dates){
        this.messages = messages;
        this.dates = dates;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<String> messages) {
        this.messages = messages;
    }

    public ArrayList<String> getDates() {
        return dates;
    }

    public void setDates(ArrayList<String> dates) {
        this.dates = dates;
    }


}
