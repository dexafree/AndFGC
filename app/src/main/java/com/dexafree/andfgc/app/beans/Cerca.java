package com.dexafree.andfgc.app.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


public class Cerca implements Parcelable {

    private List<Opcio> opcions;
    private String paradaInici;
    private String paradaFi;
    private String paradaIniciAbr;
    private String paradaFiAbr;
    private int liniaCercada;

    public int getLiniaCercada() {
        return liniaCercada;
    }

    public void setLiniaCercada(int liniaCercada) {
        this.liniaCercada = liniaCercada;
    }

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

    public String getParadaIniciAbr() {
        return paradaIniciAbr;
    }

    public void setParadaIniciAbr(String paradaIniciAbr) {
        this.paradaIniciAbr = paradaIniciAbr;
    }

    public String getParadaFiAbr() {
        return paradaFiAbr;
    }

    public void setParadaFiAbr(String paradaFiAbr) {
        this.paradaFiAbr = paradaFiAbr;
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
        parcel.writeString(paradaIniciAbr);
        parcel.writeString(paradaFiAbr);
        parcel.writeInt(liniaCercada);
    }

    public Cerca(){

    }

    public Cerca(Parcel parcel){
        opcions = new ArrayList<Opcio>();
        parcel.readList(opcions, getClass().getClassLoader());
        paradaInici = parcel.readString();
        paradaFi = parcel.readString();
        paradaIniciAbr = parcel.readString();
        paradaFiAbr = parcel.readString();
        liniaCercada = parcel.readInt();
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
