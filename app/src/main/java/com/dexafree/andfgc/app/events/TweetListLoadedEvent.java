package com.dexafree.andfgc.app.events;

import com.dexafree.andfgc.app.beans.Twit;

import java.util.ArrayList;

/**
 * Created by Carlos on 07/07/14.
 */
public class TweetListLoadedEvent {

    private ArrayList<Twit> twitList;

    public TweetListLoadedEvent(ArrayList<Twit> twitList) {
        this.twitList = twitList;
    }

    public ArrayList<Twit> getTwitList() {
        return twitList;
    }
}
