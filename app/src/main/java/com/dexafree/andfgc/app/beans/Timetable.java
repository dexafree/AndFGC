package com.dexafree.andfgc.app.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.dexafree.andfgc.app.utils.Logger;

/**
 * Created by Carlos on 26/05/2014.
 */
public class Timetable implements Parcelable {

    private String name;
    private String linea;
    private String url;


    public Timetable(){}

    public Timetable(String name, String url){
        this.name = name;
        String temp = name.replaceAll("[\\s_\\./]","-");
        this.linea = temp;
        String tempUrl = url.replace(" ", "%20");
        this.url = tempUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        String[] splits = name.split(" ");
        this.linea = splits[0];
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        String tempUrl = url.replace(" ", "%20");
        this.url = tempUrl;
    }

    public String getLinea(){
        return this.linea;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.linea);
        dest.writeString(this.url);
    }

    private Timetable(Parcel in) {
        this.name = in.readString();
        this.linea = in.readString();
        this.url = in.readString();
    }

    public static Creator<Timetable> CREATOR = new Creator<Timetable>() {
        public Timetable createFromParcel(Parcel source) {
            return new Timetable(source);
        }

        public Timetable[] newArray(int size) {
            return new Timetable[size];
        }
    };
}
