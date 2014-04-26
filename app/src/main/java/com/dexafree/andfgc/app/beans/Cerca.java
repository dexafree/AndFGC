package com.dexafree.andfgc.app.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


public class Cerca implements Parcelable {

    private List<Opcio> opcions;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(opcions);

    }

    public Cerca(){

    }

    public Cerca(List<Opcio> opcions) {
        this.opcions = opcions;
    }



    public List<Opcio> getOpcions(){
        return opcions;
    }

    public void setOpcions(List<Opcio> opcions){
        this.opcions = opcions;
    }

    public void addToOptions(Opcio op){
        opcions.add(op);
    }

    public Opcio getFromOptions(int pos){
        return opcions.get(pos);
    }
}
