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
public class Parada {

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

    private static SQLiteDatabase getDb(Context context) {
        DataBaseHelper dbHelper = new DataBaseHelper(context);
        SQLiteDatabase db = dbHelper.getDataBase();
        if (db == null) {
            db = getDb(context);
        }
        return db;
    }

    public static String[] getParadesFromLiniaAsStringArray(Context context, int linia){
        ArrayList<String> parades;

        SQLiteDatabase db = getDb(context);
        String SQLSeq = "SELECT COMPLET FROM parades WHERE ABREVIACIO IN (SELECT PARADA FROM LiniaParades WHERE linia="+linia+")";

        Cursor cursor = db.rawQuery(SQLSeq, null);

        parades = new ArrayList<String>();

        if(cursor.moveToFirst()){
            do {
                String nom = cursor.getString(cursor.getColumnIndex("COMPLET"));
                parades.add(nom);
                //String abreviatura = cursor.getString(cursor.getColumnIndex("ABREVIACIO"));

                //Parada p = new Parada(nom, abreviatura);

                //parades.add(p);

            } while(cursor.moveToNext());
        }

        db.close();

        String[] paradesArray = parades.toArray(new String[parades.size()]);

        return paradesArray;
    }

    public static ArrayList<Parada> getParadesFromLiniaAsArrayList(Context context, int linia){
        ArrayList<Parada> parades;

        SQLiteDatabase db = getDb(context);
        String SQLSeq = "SELECT COMPLET, ABREVIACIO FROM parades WHERE ABREVIACIO IN (SELECT PARADA FROM LiniaParades WHERE linia="+linia+")";

        Cursor cursor = db.rawQuery(SQLSeq, null);

        parades = new ArrayList<Parada>();

        if(cursor.moveToFirst()){
            do {
                String nom = cursor.getString(cursor.getColumnIndex("COMPLET"));
                String abreviatura = cursor.getString(cursor.getColumnIndex("ABREVIACIO"));

                Parada p = new Parada(nom, abreviatura);

                parades.add(p);

            } while(cursor.moveToNext());
        }

        db.close();

        return parades;
    }

    public static ArrayList<Parada> getParades(Context context){

        ArrayList<Parada> parades;

        SQLiteDatabase db = getDb(context);
        String SQLSeq = "SELECT * FROM parades";

        Cursor cursor = db.rawQuery(SQLSeq, null);

        parades = new ArrayList<Parada>();

        if(cursor.moveToFirst()){
            do {
                String nom = cursor.getString(cursor.getColumnIndex("COMPLET"));
                String abreviatura = cursor.getString(cursor.getColumnIndex("ABREVIACIO"));

                Parada p = new Parada(nom, abreviatura);

                parades.add(p);

            } while(cursor.moveToNext());
        }

        db.close();

        return parades;
    }

    public static String[] getNomsParades(Context context){

        ArrayList<String> nomsParadesArrayList = new ArrayList<String>();

        SQLiteDatabase db = getDb(context);

        String SQLSeq = "SELECT COMPLET FROM parades";

        Cursor cursor = db.rawQuery(SQLSeq, null);

        if(cursor.moveToFirst()){
            do {
                String nom = cursor.getString(0);

                nomsParadesArrayList.add(nom);

            } while(cursor.moveToNext());
        }

        db.close();

        String[] nomsParades = nomsParadesArrayList.toArray(new String[nomsParadesArrayList.size()]);

        return nomsParades;
    }

    public static String[] getAbreviaturesParades(Context context){

        ArrayList<String> abreviaturesParadesArrayList = new ArrayList<String>();

        SQLiteDatabase db = getDb(context);

        String SQLSeq = "SELECT ABREVIACIO FROM parades";

        Cursor cursor = db.rawQuery(SQLSeq, null);

        if(cursor.moveToFirst()){
            do {
                String nom = cursor.getString(0);

                abreviaturesParadesArrayList.add(nom);

            } while(cursor.moveToNext());
        }

        db.close();

        String[] nomsParades = abreviaturesParadesArrayList.toArray(new String[abreviaturesParadesArrayList.size()]);

        return nomsParades;
    }
}
