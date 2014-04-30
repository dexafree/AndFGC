package com.dexafree.andfgc.app.beans;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.dexafree.andfgc.app.databases.DataBaseHelper;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Carlos on 27/04/2014.
 */
public class Parada  {

    private String nom;
    private String abreviatura;

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


}
