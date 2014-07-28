package com.dexafree.andfgc.app.beans;

import android.os.Parcel;
import android.os.Parcelable;

public class Coordenada implements Parcelable {

    private String nom;
    private String abreviacio;
    private double x;
    private double y;

    public Coordenada(String nom, String abreviacio, double x, double y){
        this.nom = nom;
        this.abreviacio = abreviacio;
        this.x = x;
        this.y = y;
    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAbreviacio() {
        return abreviacio;
    }

    public void setAbreviacio(String abreviacio) {
        this.abreviacio = abreviacio;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nom);
        dest.writeString(this.abreviacio);
        dest.writeDouble(this.x);
        dest.writeDouble(this.y);
    }

    public Coordenada() {
    }

    private Coordenada(Parcel in) {
        this.nom = in.readString();
        this.abreviacio = in.readString();
        this.x = in.readDouble();
        this.y = in.readDouble();
    }

    public static Parcelable.Creator<Coordenada> CREATOR = new Parcelable.Creator<Coordenada>() {
        public Coordenada createFromParcel(Parcel source) {
            return new Coordenada(source);
        }

        public Coordenada[] newArray(int size) {
            return new Coordenada[size];
        }
    };
}
