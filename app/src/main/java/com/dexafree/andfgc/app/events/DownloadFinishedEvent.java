package com.dexafree.andfgc.app.events;

/**
 * Created by Carlos on 26/05/2014.
 */
public class DownloadFinishedEvent {

    private String filename;

    public DownloadFinishedEvent(){}

    public DownloadFinishedEvent(String filename){
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

}
