package com.dexafree.andfgc.app.events;

import com.dexafree.andfgc.app.beans.Cerca;

/**
 * Created by Carlos on 26/05/2014.
 */
public class SearchFinishedEvent {

    private Cerca cerca;

    public SearchFinishedEvent(Cerca c){
        this.cerca = c;
    }

    public Cerca getCerca() {
        return cerca;
    }

    public void setCerca(Cerca c) {
        this.cerca = c;
    }

}
