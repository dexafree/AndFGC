package com.dexafree.andfgc.app.connections;

import android.content.Context;

import com.dexafree.andfgc.app.beans.Property;
import com.dexafree.andfgc.app.beans.Ticket;
import com.dexafree.andfgc.app.events.BusProvider;
import com.dexafree.andfgc.app.events.TarifesSearchFinishedEvent;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Carlos on 06/06/14.
 */
public class GetTarifes {

    public static void getTarifes(Context c){
        Ion.with(c)
           .load("http://www.kimonolabs.com/api/aypr1ui0?apikey=f3c016707fe6e0c44c29c59a3f6cf9be")
           .asJsonObject()
           .setCallback(new FutureCallback<JsonObject>() {
               @Override
               public void onCompleted(Exception e, JsonObject result) {
                   JsonObject results = result.get("results").getAsJsonObject();
                   JsonArray collection = results.get("collection1").getAsJsonArray();
                   ArrayList<Ticket> ticketsList = new ArrayList<Ticket>();
                   for(int i=0;i<collection.size();i++){
                       JsonObject ticketObject = collection.get(i).getAsJsonObject();
                        if(isValid(ticketObject)){
                            String ticketName = ticketObject.get("title").getAsString();

                            JsonArray propertiesArray = ticketObject.get("property").getAsJsonArray();
                            JsonArray zone1 = ticketObject.get("zone1").getAsJsonArray();
                            JsonArray zone2 = ticketObject.get("zone2").getAsJsonArray();
                            JsonArray zone3 = ticketObject.get("zone3").getAsJsonArray();
                            JsonArray zone4 = ticketObject.get("zone4").getAsJsonArray();
                            JsonArray zone5 = ticketObject.get("zone5").getAsJsonArray();
                            JsonArray zone6 = ticketObject.get("zone6").getAsJsonArray();
                            ArrayList<Property> properties = new ArrayList<Property>();
                            for(int j=0;j<propertiesArray.size();j++){
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

                            Ticket ticket = new Ticket(ticketName, properties);
                            ticketsList.add(ticket);
                        }

                   }
                   BusProvider.getInstance().post(new TarifesSearchFinishedEvent(ticketsList));
               }
           });
    }

    private static boolean isValid(JsonObject object){
        return object.get("zone1").getAsJsonArray().size() > 0;
    }
}
