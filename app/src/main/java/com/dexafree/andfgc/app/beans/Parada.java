package com.dexafree.andfgc.app.beans;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.dexafree.andfgc.app.databases.DataBaseHelper;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Carlos on 27/04/2014.
 */
public class Parada implements Parcelable {

    private String nom;
    private String abreviatura;
    private String linia;

    public String getLinia() {
        return linia;
    }

    public void setLinia(String linia) {
        this.linia = linia;
    }

    public Parada(Parcel parcel){
        this.nom = parcel.readString();
        this.abreviatura = parcel.readString();
        this.linia = parcel.readString();
    }

    public Parada(){

    }

    public Parada(String nom, String abreviatura){
        this.nom = nom;
        this.abreviatura = abreviatura;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nom);
        parcel.writeString(abreviatura);
        parcel.writeString(linia);

    }

    public static final Parcelable.Creator<Parada> CREATOR = new Parcelable.Creator<Parada>() {
        public Parada createFromParcel(Parcel in) {
            return new Parada(in);
        }

        public Parada[] newArray(int size) {
            return new Parada[size];
        }
    };
}
