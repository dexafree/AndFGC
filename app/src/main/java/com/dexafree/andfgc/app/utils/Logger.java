package com.dexafree.andfgc.app.utils;

import android.util.Log;

public class Logger {

    public static void d(String tag, String message){
        if(Constants.DEBUG){
            Log.d(tag, message);
        }
    }

}
