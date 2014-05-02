package com.dexafree.andfgc.app.beans;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import com.dexafree.andfgc.app.controllers.ParadaController;

import java.util.ArrayList;


public class Opcio implements Parcelable {

    private String linia;
    private String horaSortida;
    private String horaArribada;
    private ArrayList<Transbord> transbords;

    public Opcio(){

    }

    public Opcio(Parcel parcel){
        this.linia = parcel.readString();
        this.horaSortida = parcel.readString();
        this.horaArribada = parcel.readString();
        transbords = new ArrayList<Transbord>();
        parcel.readList(transbords, getClass().getClassLoader());
    }

    public Opcio(String linia, String sortida, String arribada, ArrayList<Transbord> transbords){
        this.linia = linia;
        this.horaSortida = sortida;
        this.horaArribada = arribada;
        this.transbords = transbords;
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

    public ArrayList<Transbord> getTransbords() {
        return transbords;
    }

    public void setTransbords(ArrayList<Transbord> transbords) {
        this.transbords = transbords;
    }

    public String getLinia() {
        return linia;
    }

    public void setLinia(String linia) {
        this.linia = linia;
    }

    public Parada getPrimeraParada(Context context){
        Parada p;
        p = ParadaController.getParadaFromAbreviatura(context, transbords.get(0).getParades().get(0).getAbreviatura());
        return p;
    }

    public Parada getUltimaParada(Context context){
        Parada p;
        ArrayList<Parada> totalParades = transbords.get(transbords.size()-1).getParades();
        p = ParadaController.getParadaFromAbreviatura(context, totalParades.get(totalParades.size()-1).getAbreviatura());
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
        parcel.writeList(transbords);
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
