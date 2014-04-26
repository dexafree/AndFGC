package com.dexafree.andfgc.app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class Checkers {

    public static boolean hasInternet(Context context){
        ConnectivityManager conMg = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo inf = conMg.getActiveNetworkInfo();

        if(inf == null) return false;
        if(inf != null){
            if(!inf.isConnected()) return false;
            if(!inf.isAvailable())return false;
        }
        return true;
    }

}
