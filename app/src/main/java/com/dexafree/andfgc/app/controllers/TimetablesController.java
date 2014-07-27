package com.dexafree.andfgc.app.controllers;

import android.content.Context;
import android.content.Intent;
import com.dexafree.andfgc.app.beans.Timetable;
import com.dexafree.andfgc.app.events.BusProvider;
import com.dexafree.andfgc.app.events.TimetablesLoadedEvent;
import com.dexafree.andfgc.app.services.DownloadService;
import com.dexafree.andfgc.app.utils.Logger;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class TimetablesController {


    // Loads all the timetables and launches an Otto event
    public static void loadTables(Context context){

        Ion.with(context, "https://www.kimonolabs.com/api/67x3h6bc?apikey=f3c016707fe6e0c44c29c59a3f6cf9be")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        ArrayList<Timetable> timetableList = new ArrayList<Timetable>();

                        JsonArray collection = result.get("results").getAsJsonObject().get("collection1").getAsJsonArray();

                        for(int i=0;i<collection.size();i++){

                            JsonObject object = collection.get(i).getAsJsonObject().get("timetable").getAsJsonObject();

                            String name = object.get("text").getAsString();
                            String link = object.get("href").getAsString();


                            timetableList.add(new Timetable(name, link));
                        }

                        BusProvider.getInstance().post(new TimetablesLoadedEvent(timetableList));

                    }
                });
    }

    // Starts the download of a timetable
    public static void downloadTimetable(Timetable t, Context context){
        Intent i = new Intent(context, DownloadService.class);
        i.putExtra(DownloadService.FILENAME, t.getLinea());
        i.putExtra(DownloadService.URL, t.getUrl());
        i.putExtra(DownloadService.TIMETABLE, t.getName());
        context.startService(i);
    }
}
