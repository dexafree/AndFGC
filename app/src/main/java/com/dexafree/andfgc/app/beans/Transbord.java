package com.dexafree.andfgc.app.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Carlos on 02/05/2014.
 */
public class Transbord implements Parcelable {

    private String linia;
    private String sortida;
    private String arribada;
    private ArrayList<Parada> parades;

    public Transbord(String linia, String sortida, String arribada, ArrayList<Parada> parades) {
        this.linia = linia;
        this.sortida = sortida;
        this.arribada = arribada;
        this.parades = parades;
    }



    public Transbord(){}

    public Transbord(Parcel parcel){
        this.linia = parcel.readString();
        this.sortida = parcel.readString();
        this.arribada = parcel.readString();
        parades = new ArrayList<Parada>();
        parcel.readList(parades, getClass().getClassLoader());
    }

    public String getLinia() {
        return linia;
    }

    public void setLinia(String linia) {
        this.linia = linia;
    }

    public String getSortida() {
        return sortida;
    }

    public void setSortida(String sortida) {
        this.sortida = sortida;
    }

    public String getArribada() {
        return arribada;
    }

    public void setArribada(String arribada) {
        this.arribada = arribada;
    }

    public ArrayList<Parada> getParades() {
        return parades;
    }

    public void setParades(ArrayList<Parada> parades) {
        this.parades = parades;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(linia);
        parcel.writeString(sortida);
        parcel.writeString(arribada);
        parcel.writeList(parades);
    }

    public static final Parcelable.Creator<Transbord> CREATOR = new Parcelable.Creator<Transbord>() {
        public Transbord createFromParcel(Parcel in) {
            return new Transbord(in);
        }

        public Transbord[] newArray(int size) {
            return new Transbord[size];
        }
    };
}
