package com.dexafree.andfgc.app.events;

import com.dexafree.andfgc.app.beans.Timetable;

import java.io.File;

public class DownloadFinishedEvent {

    private String filename;
    private File file;
    private Timetable timetable;

    public DownloadFinishedEvent(){}

    public DownloadFinishedEvent(String filename, File file, Timetable timetable) {
        this.filename = filename;
        this.file = file;
        this.timetable = timetable;
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

    public Timetable getTimetable() {
        return timetable;
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }
}
