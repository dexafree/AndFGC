package com.dexafree.andfgc.app.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dexafree.andfgc.app.beans.Cerca;


public class MainFragment extends Fragment {

    private BroadcastReceiver rec;
    private Context mContext;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity.getApplicationContext();

        rec = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Cerca c = intent.getExtras().getParcelable("CERCA");
                onSearchFinished(c);
            }
        };

        mContext.registerReceiver(rec, new IntentFilter("COM.DEXAFREE.ANDFGC.CERCA_FINALITZADA"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);




        return v;
    }

    private void onSearchFinished(Cerca c){

    }
}
