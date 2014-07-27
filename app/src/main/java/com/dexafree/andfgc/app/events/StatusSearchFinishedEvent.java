package com.dexafree.andfgc.app.events;

import java.util.ArrayList;

public class StatusSearchFinishedEvent {

    private ArrayList<String> messages;
    private ArrayList<String> dates;

    public StatusSearchFinishedEvent(ArrayList<String> messages, ArrayList<String> dates){
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
