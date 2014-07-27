package com.dexafree.andfgc.app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;


public class Checkers {

    public static boolean hasInternet(Context context){
        ConnectivityManager conMg = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo inf = conMg.getActiveNetworkInfo();

        return inf != null && inf.isConnected() && inf.isAvailable();
    }

    public static boolean isLowerThanIcs(){
        return Build.VERSION.SDK_INT <= 10;
    }

    public static boolean isKitKatOrHigher(){
        return Build.VERSION.SDK_INT >= 19;
    }

}
