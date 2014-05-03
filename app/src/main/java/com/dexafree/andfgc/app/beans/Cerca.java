package com.dexafree.andfgc.app.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


public class Cerca implements Parcelable {

    private List<Opcio> opcions;
    private String paradaInici;
    private String paradaFi;

    public List<Opcio> getOpcions() {
        return opcions;
    }

    public void setOpcions(List<Opcio> opcions) {
        this.opcions = opcions;
    }

    public String getParadaInici() {
        return paradaInici;
    }

    public void setParadaInici(String paradaInici) {
        this.paradaInici = paradaInici;
    }

    public String getParadaFi() {
        return paradaFi;
    }

    public void setParadaFi(String paradaFi) {
        this.paradaFi = paradaFi;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(opcions);
        parcel.writeString(paradaInici);
        parcel.writeString(paradaFi);
    }

    public Cerca(){

    }

    public Cerca(Parcel parcel){
        opcions = new ArrayList<Opcio>();
        parcel.readList(opcions, getClass().getClassLoader());
        paradaInici = parcel.readString();
        paradaFi = parcel.readString();
    }

    public void setOpcions(ArrayList<Opcio> opcions){
        this.opcions = opcions;
    }

    public void addToOptions(Opcio op){
        opcions.add(op);
    }

    public Opcio getFromOptions(int pos){
        return opcions.get(pos);
    }

    public static final Parcelable.Creator<Cerca> CREATOR = new Parcelable.Creator<Cerca>() {
        public Cerca createFromParcel(Parcel in) {
            return new Cerca(in);
        }

        public Cerca[] newArray(int size) {
            return new Cerca[size];
        }
    };

}
