package com.dexafree.andfgc.app.beans;

import android.os.Parcel;
import android.os.Parcelable;


public class Opcio implements Parcelable {

    private String linia;
    private String sortida;
    private String arribada;
    private String[] estacions;

    public Opcio(){

    }

    public Opcio(Parcel parcel){
        this.linia = parcel.readString();
        this.sortida = parcel.readString();
        this.arribada = parcel.readString();
        estacions = parcel.createStringArray();
    }

    public Opcio(String linia, String sortida, String arribada, String[] estacions){
        this.linia = linia;
        this.sortida = sortida;
        this.arribada = arribada;
        this.estacions = estacions;
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

    public String[] getEstacions() {
        return estacions;
    }

    public void setEstacions(String[] estacions) {
        this.estacions = estacions;
    }

    public String getLinia() {
        return linia;
    }

    public void setLinia(String linia) {
        this.linia = linia;
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
        parcel.writeStringArray(estacions);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Opcio createFromParcel(Parcel in) {
            return new Opcio(in);
        }

        public Opcio[] newArray(int size) {
            return new Opcio[size];
        }
    };
}
