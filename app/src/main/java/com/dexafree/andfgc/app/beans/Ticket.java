package com.dexafree.andfgc.app.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Ticket implements Parcelable {

    private String name;
    private ArrayList<Property> properties;
    private String description;

    public Ticket(){}

    public Ticket(String name, ArrayList<Property> properties, String description){
        this.name = name;
        this.properties = properties;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Property> getProperties() {
        return properties;
    }

    public void setProperties(ArrayList<Property> properties) {
        this.properties = properties;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.properties);
        dest.writeString(this.name);
        dest.writeString(this.description);
    }

    private Ticket(Parcel in) {
        in.readList(properties, getClass().getClassLoader());
        this.name = in.readString();
        this.description = in.readString();
    }

    public static Parcelable.Creator<Ticket> CREATOR = new Parcelable.Creator<Ticket>() {
        public Ticket createFromParcel(Parcel source) {
            return new Ticket(source);
        }

        public Ticket[] newArray(int size) {
            return new Ticket[size];
        }
    };
}
