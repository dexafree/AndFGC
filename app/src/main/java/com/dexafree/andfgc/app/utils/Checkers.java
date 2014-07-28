package com.dexafree.andfgc.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;


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

    public static boolean hasAppBeenUpdated(Context context){

        try {

            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

            int lastVersion = sp.getInt(Constants.LAST_VERSION, 1);
            int currentVersion = pInfo.versionCode;

            if(lastVersion != currentVersion){
                return false;
            } else {
                sp.edit().putInt(Constants.LAST_VERSION, currentVersion).commit();
                return true;
            }

        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }

        return true;
    }
}
