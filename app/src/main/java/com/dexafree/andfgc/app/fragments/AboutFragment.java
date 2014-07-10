package com.dexafree.andfgc.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;

import com.dexafree.andfgc.app.R;

public class AboutFragment extends PreferenceFragment {

    public static final String OTTO = "https://github.com/square/otto";
    public static final String ION = "https://github.com/koush/ion";
    public static final String GNAVDRAWER = "https://github.com/Arasthel/GoogleNavigationDrawerMenu";
    public static final String GSON = "https://code.google.com/p/google-gson/";
    public static final String BETTERPICKERS = "https://github.com/derekbrameyer/android-betterpickers";
    public static final String JSOUP = "http://jsoup.org";
    public static final String JODATIME = "http://www.joda.org/joda-time/";
    public static final String DEXAFREE = "https://github.com/dexafree";

    private Context mContext;

    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        addPreferencesFromResource(R.xml.about);

        mContext = getActivity();

        findPreference("otto").setOnPreferenceClickListener(new Listener(OTTO));
        findPreference("ion").setOnPreferenceClickListener(new Listener(ION));
        findPreference("gnavdrawer").setOnPreferenceClickListener(new Listener(GNAVDRAWER));
        findPreference("gson").setOnPreferenceClickListener(new Listener(GSON));
        findPreference("betterpickers").setOnPreferenceClickListener(new Listener(BETTERPICKERS));
        findPreference("jsoup").setOnPreferenceClickListener(new Listener(JSOUP));
        findPreference("joda").setOnPreferenceClickListener(new Listener(JODATIME));
        findPreference("dexafree").setOnPreferenceClickListener(new Listener(DEXAFREE));
        findPreference("version").setSummary(getVersionString());

    }

    private String getVersionString(){
        String versionString;
        try {
            versionString = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;

        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            versionString = "1.0";
        }

        return versionString;

    }

    private class Listener implements Preference.OnPreferenceClickListener {

        private String url;

        public Listener(String url){ this.url = url;}

        @Override
        public boolean onPreferenceClick(Preference preference) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
            return false;
        }
    }

}
