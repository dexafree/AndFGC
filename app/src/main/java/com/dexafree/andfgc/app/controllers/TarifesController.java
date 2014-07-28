package com.dexafree.andfgc.app.controllers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.beans.Property;
import com.dexafree.andfgc.app.beans.Tarifa;
import com.dexafree.andfgc.app.beans.Ticket;
import com.dexafree.andfgc.app.databases.DataBaseHelper;
import com.dexafree.andfgc.app.events.BusProvider;
import com.dexafree.andfgc.app.events.ErrorEvent;
import com.dexafree.andfgc.app.events.TarifesSearchFinishedEvent;
import com.dexafree.andfgc.app.utils.Logger;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Locale;

public class TarifesController {

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

    public static void getTarifes(final Context c){
        String language;
        String localLang = Locale.getDefault().getLanguage();

        if(localLang.equalsIgnoreCase("es")){
            language = "esp";
            Logger.d("ESPAÑOL", "ENTRADO");
        }else if(localLang.equalsIgnoreCase("en")){
            language = "eng";
        }else{
            language = "cat";
        }

        Logger.d("LOCALLANG", localLang);
        Logger.d("LANGUAGE", language);

        Ion.with(c)
           .load("http://www.kimonolabs.com/api/aypr1ui0?apikey=f3c016707fe6e0c44c29c59a3f6cf9be&kimpath1="+language)
           .asJsonObject()
           .setCallback(new FutureCallback<JsonObject>() {
               @Override
               public void onCompleted(Exception e, JsonObject result) {
                   Logger.d("RESULT", result.toString());
                   try {
                       JsonObject results = result.get("results").getAsJsonObject();

                       JsonArray collection = results.get("collection1").getAsJsonArray();
                       ArrayList<Ticket> ticketsList = new ArrayList<Ticket>();
                       for (int i = 0; i < collection.size(); i++) {
                           JsonObject ticketObject = collection.get(i).getAsJsonObject();
                           Logger.d("OBJECT", ticketObject.toString());
                           if (isValid(ticketObject)) {
                               String ticketName = ticketObject.get("title").getAsString();
                               JsonArray propertiesArray = ticketObject.get("property").getAsJsonArray();
                               JsonArray zone1 = ticketObject.get("zone1").getAsJsonArray();
                               JsonArray zone2 = ticketObject.get("zone2").getAsJsonArray();
                               JsonArray zone3 = ticketObject.get("zone3").getAsJsonArray();
                               JsonArray zone4 = ticketObject.get("zone4").getAsJsonArray();
                               JsonArray zone5 = ticketObject.get("zone5").getAsJsonArray();
                               JsonArray zone6 = ticketObject.get("zone6").getAsJsonArray();
                               String description;

                               description = getDescription(ticketObject);

                               ArrayList<Property> properties = new ArrayList<Property>();
                               for (int j = 0; j < propertiesArray.size(); j++) {
                                   ArrayList<String> values = new ArrayList<String>();
                                   values.add(zone1.get(j).getAsString());
                                   values.add(zone2.get(j).getAsString());
                                   values.add(zone3.get(j).getAsString());
                                   values.add(zone4.get(j).getAsString());
                                   values.add(zone5.get(j).getAsString());
                                   values.add(zone6.get(j).getAsString());

                                   String propertyName = propertiesArray.get(j).getAsString();
                                   Property property = new Property(propertyName, values);
                                   properties.add(property);
                               }

                               Ticket ticket = new Ticket(ticketName, properties, description);
                               ticketsList.add(ticket);
                           }

                       }
                       BusProvider.getInstance().post(new TarifesSearchFinishedEvent(ticketsList));
                   } catch (NullPointerException exc){
                       exc.printStackTrace();
                       BusProvider.getInstance().post(new ErrorEvent());
                   }
               }
           });
    }

    private static boolean isValid(JsonObject object){
        try{
            return object.get("zone1").getAsJsonArray().size() > 0;
        } catch(IllegalStateException e){
            Logger.d("TICKET", object.toString());
            e.printStackTrace();

        }
        return false;

    }

    private static String getDescription(JsonObject ticketObject){
        String description = "";
        try{
            description = ticketObject.get("description").getAsString();
        } catch (UnsupportedOperationException e){
            e.printStackTrace();
        } catch (IllegalStateException e){
            e.printStackTrace();
        }

        return description;
    }

    private static ArrayList<Tarifa> baseGetTarifa(Context c, String table, int zones){

        ArrayList<Tarifa> tarifes = new ArrayList<Tarifa>();

        SQLiteDatabase db = getDb(c);


        String sqlSeq = "SELECT * FROM "+table;

        Cursor cursor = db.rawQuery(sqlSeq, null);

        if(cursor.moveToFirst()){
            do {
                String concepte = cursor.getString(cursor.getColumnIndex("concepte"));
                float preu = cursor.getFloat(cursor.getColumnIndex("zona"+zones));

                Tarifa t = new Tarifa(concepte, preu);
                tarifes.add(t);
            } while(cursor.moveToNext());
        }
        db.close();

        return tarifes;

    }

    public static double getPreuNormal(Context c, int linia, int zones){

        double preu = 0;

        SQLiteDatabase db = getDb(c);

        if(linia == 3){
            String sqlSeq = "SELECT * FROM preus_lleida";

            Cursor cursor = db.rawQuery(sqlSeq, null);

            if(cursor.moveToFirst()){

                preu = cursor.getFloat(cursor.getColumnIndex("zona"+zones));

            }

        } else {
            String sqlSeq = "SELECT * FROM preus_standard";

            Cursor cursor = db.rawQuery(sqlSeq, null);

            if(cursor.moveToFirst()){

                preu = cursor.getFloat(cursor.getColumnIndex("zona"+zones));

            }
        }
        db.close();

        return preu;
    }

    public static ArrayList<Tarifa> getTarifaStandard(Context c, int linia, int zones){

        ArrayList<Tarifa> tarifes;

        SQLiteDatabase db = getDb(c);

        if(linia == 3){
            tarifes = new ArrayList<Tarifa>();
            String sqlSeq = "SELECT * FROM preus_lleida";

            Cursor cursor = db.rawQuery(sqlSeq, null);

            if(cursor.moveToFirst()){
                String concepte = cursor.getString(cursor.getColumnIndex("concepte"));
                double preu = cursor.getDouble(cursor.getColumnIndex("zona"+zones));

                Tarifa t = new Tarifa(concepte, preu);
                tarifes.add(t);
            }
        } else {

            tarifes = baseGetTarifa(c, "preus_standard", zones);
        }
        db.close();

        return tarifes;

    }

    public static ArrayList<Tarifa> getTarifaDiscapacitat(Context c, int zones){

        return baseGetTarifa(c, "preus_discapacitat", zones);

    }

    public static ArrayList<Tarifa> getTarifaFamiliaNombrosa(Context c, int zones){

        return baseGetTarifa(c, "preus_familia_nombrosa", zones);

    }
    public static ArrayList<Tarifa> getTarifaPensionista(Context c, int zones){

        return baseGetTarifa(c, "preus_pensionista", zones);

    }
}
