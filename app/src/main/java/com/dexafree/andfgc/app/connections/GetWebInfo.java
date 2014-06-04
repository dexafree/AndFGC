package com.dexafree.andfgc.app.connections;

import android.content.Context;
import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.events.AlertSearchFinishedEvent;
import com.dexafree.andfgc.app.events.BusProvider;
import com.dexafree.andfgc.app.events.StatusSearchFinishedEvent;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import org.joda.time.DateTime;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Carlos on 18/05/2014.
 */
public class GetWebInfo {

    Context mContext;

    public GetWebInfo(Context c){
        this.mContext = c;
    }


    private void load(final int mode){
        String localLang = Locale.getDefault().getDisplayLanguage();
        String language;
        if(localLang.equalsIgnoreCase("es"))language = "esp";
        else if(localLang.equalsIgnoreCase("en")) language = "eng";
        else language = "cat";


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
                            statusDates.add(now.getDayOfMonth()+"/"+now.getMonthOfYear()+"/"+now.getYear());
                        }

                        if(mode==1) BusProvider.getInstance().post(new StatusSearchFinishedEvent(statusMessages, statusDates));
                        else BusProvider.getInstance().post(new AlertSearchFinishedEvent(statusMessages, statusDates));
                    }
                });
    }

    public void getStatusText(){
        load(1);
    }


    public void getAlertsText(){
        load(2);
    }

}