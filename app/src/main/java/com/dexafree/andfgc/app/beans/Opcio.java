package com.dexafree.andfgc.app.beans;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import com.dexafree.andfgc.app.controllers.ParadaController;


public class Opcio implements Parcelable {

    private String linia;
    private String horaSortida;
    private String horaArribada;
    private String[] estacions;

    public Opcio(){

    }

    public Opcio(Parcel parcel){
        this.linia = parcel.readString();
        this.horaSortida = parcel.readString();
        this.horaArribada = parcel.readString();
        estacions = parcel.createStringArray();
    }

    public Opcio(String linia, String sortida, String arribada, String[] estacions){
        this.linia = linia;
        this.horaSortida = sortida;
        this.horaArribada = arribada;
        this.estacions = estacions;
    }

    public String getHoraSortida() {
        return horaSortida;
    }

    public void setHoraSortida(String horaSortida) {
        this.horaSortida = horaSortida;
    }

    public String getHoraArribada() {
        return horaArribada;
    }

    public void setHoraArribada(String horaArribada) {
        this.horaArribada = horaArribada;
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

    public Parada getPrimeraParada(Context context){
        Parada p;
        p = ParadaController.getParadaFromAbreviatura(context, estacions[0]);
        return p;
    }

    public Parada getUltimaParada(Context context){
        Parada p;
        p = ParadaController.getParadaFromAbreviatura(context, estacions[estacions.length-1]);
        return p;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(linia);
        parcel.writeString(horaSortida);
        parcel.writeString(horaArribada);
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
