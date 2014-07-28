package com.dexafree.andfgc.app.beans;

import android.os.Parcel;
import android.os.Parcelable;

public class Tarifa implements Parcelable {

    private String concepte;
    private double preu;

    public Tarifa(){}

    public Tarifa(String concepte, double preu) {
        this.concepte = concepte;
        this.preu = preu;
    }

    public String getConcepte() {
        return concepte;
    }

    public void setConcepte(String concepte) {
        this.concepte = concepte;
    }

    public double getPreu() {
        return preu;
    }

    public void setPreu(double preu) {
        this.preu = preu;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.concepte);
        dest.writeDouble(this.preu);
    }

    private Tarifa(Parcel in) {
        this.concepte = in.readString();
        this.preu = in.readDouble();
    }

    public static final Creator<Tarifa> CREATOR = new Creator<Tarifa>() {
        public Tarifa createFromParcel(Parcel source) {
            return new Tarifa(source);
        }

        public Tarifa[] newArray(int size) {
            return new Tarifa[size];
        }
    };
}
