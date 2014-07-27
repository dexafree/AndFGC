package com.dexafree.andfgc.app.events;

import java.io.File;

public class DownloadFinishedEvent {

    private String filename;
    private File file;

    public DownloadFinishedEvent(){}

    public DownloadFinishedEvent(String filename, File file){
        this.filename = filename;
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

}
