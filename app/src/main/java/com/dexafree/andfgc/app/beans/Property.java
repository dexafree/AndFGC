package com.dexafree.andfgc.app.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Property implements Parcelable {

    private String name;
    private ArrayList<String> values;

    public Property(){}

    public Property(String name, ArrayList<String> values){
        this.name = name;
        this.values = values;
    }

    public ArrayList<String> getValues() {
        return values;
    }

    public void setValues(ArrayList<String> values) {
        this.values = values;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeList(this.values);
    }

    private Property(Parcel in) {
        this.name = in.readString();
        in.readList(values, getClass().getClassLoader());
    }

    public static Parcelable.Creator<Property> CREATOR = new Parcelable.Creator<Property>() {
        public Property createFromParcel(Parcel source) {
            return new Property(source);
        }

        public Property[] newArray(int size) {
            return new Property[size];
        }
    };
}
