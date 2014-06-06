package com.dexafree.andfgc.app.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Carlos on 06/06/14.
 */
public class Ticket implements Parcelable {

    private String name;
    private ArrayList<Property> properties;

    public Ticket(){}

    public Ticket(String name, ArrayList<Property> properties){
        this.name = name;
        this.properties = properties;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.properties);
        dest.writeString(this.name);
    }

    private Ticket(Parcel in) {
        this.properties = (ArrayList<Property>) in.readSerializable();
        this.name = in.readString();
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
