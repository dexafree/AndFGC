package com.dexafree.andfgc.app.beans;

import android.os.Parcel;
import android.os.Parcelable;

public class Favorito implements Parcelable {

    private String origen;
    private String desti;
    private String title;
    private int id;
    private int linia;



    public Favorito() {
    }

    public Favorito(String origen, String desti, String title, int id, int linia) {
        this.origen = origen;
        this.desti = desti;
        this.title = title;
        this.id = id;
        this.linia = linia;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDesti() {
        return desti;
    }

    public void setDesti(String desti) {
        this.desti = desti;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLinia() {
        return linia;
    }

    public void setLinia(int linia) {
        this.linia = linia;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.origen);
        dest.writeString(this.desti);
        dest.writeString(this.title);
        dest.writeInt(this.id);
        dest.writeInt(this.linia);
    }

    private Favorito(Parcel in) {
        this.origen = in.readString();
        this.desti = in.readString();
        this.title = in.readString();
        this.id = in.readInt();
        this.linia = in.readInt();
    }

    public static final Creator<Favorito> CREATOR = new Creator<Favorito>() {
        public Favorito createFromParcel(Parcel source) {
            return new Favorito(source);
        }

        public Favorito[] newArray(int size) {
            return new Favorito[size];
        }
    };
}
