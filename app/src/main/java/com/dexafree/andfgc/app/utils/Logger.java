package com.dexafree.andfgc.app.utils;

import android.util.Log;

/**
 * Created by Carlos on 28/04/2014.
 */
public class Logger {

    public static void d(String tag, String message){
        if(Constants.DEBUG){
            Log.d(tag, message);
        }
    }

}
