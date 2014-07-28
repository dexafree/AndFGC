package com.dexafree.andfgc.app.controllers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dexafree.andfgc.app.beans.Coordenada;
import com.dexafree.andfgc.app.beans.Parada;
import com.dexafree.andfgc.app.databases.DataBaseHelper;
import com.dexafree.andfgc.app.utils.Logger;

import java.util.ArrayList;

public class ParadaController {

    /*
     * Returns a SQLiteDatabase Instance
     */
    private static SQLiteDatabase getDb(Context context) {
        DataBaseHelper dbHelper = new DataBaseHelper(context);
        SQLiteDatabase db = dbHelper.getDataBase();
        if (db == null) {
            db = getDb(context);
        }
        return db;
    }

    /*
     * Returns all the station names in a line as a String[]
     */
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


            } while(cursor.moveToNext());
        }

        db.close();

        String[] paradesArray = parades.toArray(new String[parades.size()]);

        return paradesArray;
    }

    /*
     * Returns all the stations in a line
     */
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

    /*
     * Returns all the stations in the database
     */
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

    /*
     * Returns all the station names in the database as a String[]
     */
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

    /*
     * Returns all the station abreviations in the database as a String[]
     */
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

    /*
     * Returns a Parada instance created from its abreviation
     */
    public static Parada getParadaFromAbreviatura(Context c, String abreviatura){
        SQLiteDatabase db = getDb(c);
        String sqlSeq = "SELECT * FROM parades WHERE ABREVIACIO = '"+abreviatura+"'";


        Cursor cursor = db.rawQuery(sqlSeq, null);

        String nom = "";
        boolean hasBeenFound = false;
        if(cursor.moveToFirst()){
            do {
                nom = cursor.getString(cursor.getColumnIndex("COMPLET"));
                hasBeenFound = true;
            } while(cursor.moveToNext());
        }

        db.close();

        if(!hasBeenFound) nom = abreviatura;


        Parada p = new Parada(nom, abreviatura);

        return p;
    }

    /*
     * Returns the coordinates of all the stations of the db
     */
    public static ArrayList<Coordenada> getAllCoords(Context c){
        ArrayList<Coordenada> coords = new ArrayList<Coordenada>();

        SQLiteDatabase db = getDb(c);
        String sqlSeq = "SELECT * FROM coordenadas";

        Cursor cursor = db.rawQuery(sqlSeq, null);

        if(cursor.moveToFirst()){
            do{
                String nom = cursor.getString(cursor.getColumnIndex("PARADA"));
                String abreviacio = cursor.getString(cursor.getColumnIndex("ABREVIACIO"));
                double x = cursor.getDouble(cursor.getColumnIndex("X"));
                double y = cursor.getDouble(cursor.getColumnIndex("Y"));
                Coordenada coord = new Coordenada(nom, abreviacio, x, y);
                coords.add(coord);
            }while(cursor.moveToNext());
        }

        db.close();

        return coords;
    }

    /*
     * Returns the coordinates from a specific station
     */
    public static Coordenada getCoordenadaFromParada(Context c, Parada p){
        Coordenada coord = new Coordenada();

        String abreviacioParada = p.getAbreviatura();

        SQLiteDatabase db = getDb(c);
        String sqlSeq = "SELECT * FROM coordenadas WHERE ABREVIACIO = '"+abreviacioParada+"'";

        Cursor cursor = db.rawQuery(sqlSeq, null);

        if(cursor.moveToFirst()){
            String nom = cursor.getString(cursor.getColumnIndex("PARADA"));
            String abreviacio = cursor.getString(cursor.getColumnIndex("ABREVIACIO"));
            double x = cursor.getDouble(cursor.getColumnIndex("X"));
            double y = cursor.getDouble(cursor.getColumnIndex("Y"));
            coord.setNom(nom);
            coord.setAbreviacio(abreviacio);
            coord.setX(x);
            coord.setY(y);
        }

        db.close();

        return coord;
    }

    /*
     * Returns a Parada from the information of the Coordenada object received
     */
    public static Parada getParadaFromCoordenada(Context c, Coordenada coord){
        Parada p = new Parada();

        String abreviacioParada = coord.getAbreviacio();

        SQLiteDatabase db = getDb(c);
        String sqlSeq = "SELECT * FROM parades WHERE ABREVIACIO = '"+abreviacioParada+"'";

        Cursor cursor = db.rawQuery(sqlSeq, null);

        if(cursor.moveToFirst()){
            do{
                String nom = cursor.getString(cursor.getColumnIndex("COMPLET"));
                int linia  = cursor.getInt(cursor.getColumnIndex("LINIA"));
                p.setAbreviatura(abreviacioParada);
                p.setLinia(linia+"");
                p.setNom(nom);
            }while(cursor.moveToNext());
        }

        db.close();

        return p;
    }

    public static int getZonesFromParades(Context c, Parada origen, Parada desti){

        String abrOrigen = origen.getAbreviatura();
        String abrDesti = desti.getAbreviatura();

        return getZonesFromAbreviatures(c, abrOrigen, abrDesti);

    }


    public static int getZonesFromAbreviatures(Context c, String origen, String desti){

        SQLiteDatabase db = getDb(c);

        String sqlSeq = "SELECT ABREVIACIO, ZONA FROM parades WHERE ("
                +"ABREVIACIO = '"+origen+"'"
                +" OR "
                +"ABREVIACIO = '"+desti+"');";

        Cursor cursor = db.rawQuery(sqlSeq, null);

        int numZones = 0;

        int i = 0;
        int[] zones = new int[2];

        if(cursor.moveToFirst()){
            do{
                zones[i] = cursor.getInt(cursor.getColumnIndex("ZONA"));
                i++;
            } while(cursor.moveToNext());
        }

        numZones = Math.abs(zones[0] - zones[1]);

        return numZones + 1;

    }


}
