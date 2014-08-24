package com.dexafree.andfgc.app.controllers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class TimetablesController {

    private static HashMap<Timetable, Boolean> downloadedMap;


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
        i.putExtra(DownloadService.TIMETABLE_OBJECT, t);
        context.startService(i);
    }

    public static boolean isTimetableDownloaded(Timetable t){

        if(downloadedMap == null) downloadedMap = new HashMap<Timetable, Boolean>();

        if(downloadedMap.containsKey(t)){
            return downloadedMap.get(t);
        } else {
            String path = Environment.getExternalStorageDirectory().getPath()+"/FGC/"+t.getLinea()+".pdf";
            File timetable = new File(path);

            boolean exists = timetable.exists();

            downloadedMap.put(t, exists);

            return exists;
        }

    }

    public static void newTimetableDownloaded(Timetable t){
        if(downloadedMap == null) downloadedMap = new HashMap<Timetable, Boolean>();

        Iterator<Timetable> it = downloadedMap.keySet().iterator();

        Timetable tempTimetable;

        do{
            tempTimetable = it.next();
        }while(it.hasNext() && !tempTimetable.getLinea().equalsIgnoreCase(t.getLinea()));

        downloadedMap.remove(tempTimetable);
        downloadedMap.put(tempTimetable, true);

    }

    public static Intent getIntentFromTimetable(Timetable t){

        String path = Environment.getExternalStorageDirectory().getPath()+"/FGC/"+t.getLinea()+".pdf";
        File file = new File(path);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return intent;
    }
}
