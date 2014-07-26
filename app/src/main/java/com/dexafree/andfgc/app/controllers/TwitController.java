package com.dexafree.andfgc.app.controllers;

import android.content.Context;
import android.widget.Toast;

import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.beans.Twit;
import com.dexafree.andfgc.app.events.BusProvider;
import com.dexafree.andfgc.app.events.ErrorEvent;
import com.dexafree.andfgc.app.events.TweetListLoadedEvent;
import com.dexafree.andfgc.app.utils.Logger;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/**
 * Created by Carlos on 07/07/14.
 */
public class TwitController {

    public static void getLastTweets(final Context context){

        Ion.with(context, "https://www.kimonolabs.com/api/2qvr1jng?apikey=f3c016707fe6e0c44c29c59a3f6cf9be")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if(!result.isJsonNull()) {
                            ArrayList<Twit> tweetList = new ArrayList<Twit>();

                            JsonArray collection = result.get("results").getAsJsonObject().get("collection1").getAsJsonArray();

                            for (int i = 0; i < collection.size(); i++) {

                                JsonObject object = collection.get(i).getAsJsonObject();

                                String message = object.get("message").getAsString();
                                String time = object.get("time").getAsString();
                                String link = object.get("link").getAsString();
                                String account = object.get("account").getAsString();
                                JsonObject image = object.get("image").getAsJsonObject();

                                String avatarUrl = image.get("src").getAsString();
                                String alt = image.get("alt").getAsString();

                                tweetList.add(new Twit(message, time, link, account, avatarUrl, alt));
                            }

                            BusProvider.getInstance().post(new TweetListLoadedEvent(tweetList));
                        } else {
                            BusProvider.getInstance().post(new ErrorEvent());
                        }

                    }
                });

    }

}
