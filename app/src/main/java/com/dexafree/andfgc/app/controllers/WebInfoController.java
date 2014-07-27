package com.dexafree.andfgc.app.controllers;

import android.content.Context;
import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.events.AlertSearchFinishedEvent;
import com.dexafree.andfgc.app.events.BusProvider;
import com.dexafree.andfgc.app.events.StatusSearchFinishedEvent;
import com.dexafree.andfgc.app.utils.Logger;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import org.joda.time.DateTime;
import java.util.ArrayList;
import java.util.Locale;


public class WebInfoController {

    Context mContext;

    public WebInfoController(Context c){
        this.mContext = c;
    }

    /*
     * Loads the web page information
     * If mode is a 1, it loads the status messages
     * If mode is a 2, it loads the alerts list
     */
    private void load(final int mode){
        String localLang = Locale.getDefault().getLanguage();
        String language;
        if(localLang.equalsIgnoreCase("es"))language = "esp";
        else if(localLang.equalsIgnoreCase("en")) language = "eng";
        else language = "cat";

        Logger.d("LOCALLANG", localLang);
        Logger.d("LANG", language);

        Ion.with(mContext, "http://fgc.cat/includes/ultimaHoraServer.asp")
                .setBodyParameter("lang", language)
                .setBodyParameter("mode", mode+"")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        ArrayList<String> statusMessages = new ArrayList<String>();
                        ArrayList<String> statusDates = new ArrayList<String>();

                        JsonArray avisos = result.get("avisos").getAsJsonArray();

                        for(int i=0;i<avisos.size();i++){
                            JsonObject avis = avisos.get(i).getAsJsonObject();
                            String titol = avis.get("titul").getAsString();
                            String data = avis.get("data").getAsString();
                            statusMessages.add(titol);
                            statusDates.add(data);
                        }
                        if(statusMessages.size() == 0){
                            statusDates.clear();
                            statusMessages.add(mContext.getString(R.string.normal_service));
                            DateTime now = DateTime.now();
                            int dayInt = now.getDayOfMonth();
                            int monthInt = now.getMonthOfYear();
                            String day;
                            String month;
                            String year = now.getYear()+"";

                            if(dayInt < 10) day = "0"+dayInt;
                            else day = ""+dayInt;

                            if(monthInt < 10) month = "0"+monthInt;
                            else month = ""+monthInt;

                            statusDates.add(day+"/"+month+"/"+year);
                        }

                        if(mode==1) BusProvider.getInstance().post(new StatusSearchFinishedEvent(statusMessages, statusDates));
                        else BusProvider.getInstance().post(new AlertSearchFinishedEvent(statusMessages, statusDates));
                    }
                });
    }

    // Starts the status loading
    public void getStatusText(){
        load(1);
    }

    // Starts the alerts loading
    public void getAlertsText(){
        load(2);
    }

}
