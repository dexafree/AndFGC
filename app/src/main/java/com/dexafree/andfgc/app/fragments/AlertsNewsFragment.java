package com.dexafree.andfgc.app.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.adapters.ServiceStatusAdapter;
import com.dexafree.andfgc.app.controllers.WebInfoController;
import com.dexafree.andfgc.app.events.AlertSearchFinishedEvent;
import com.dexafree.andfgc.app.events.BusProvider;
import com.dexafree.andfgc.app.events.StatusSearchFinishedEvent;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class AlertsNewsFragment extends Fragment {

    private WebInfoController info;

    private Context mContext;

    private ListView statusListView;
    private ListView alertsListView;

    private ProgressDialog dialog;

    private ArrayList<String> statusMessages;
    private ArrayList<String> statusDates;

    private ArrayList<String> alertMessages;
    private ArrayList<String> alertDates;

    private boolean alertsLoaded = false;
    private boolean statusLoaded = false;

    @Subscribe
    public void onStatusSearchFinished(StatusSearchFinishedEvent event){
        statusLoaded = true;
        statusMessages = event.getMessages();
        statusDates = event.getDates();
        dismissDialog();
        setContents();
    }

    @Subscribe
    public void onAlertSearchFinished(AlertSearchFinishedEvent event){
        alertsLoaded = true;
        alertMessages = event.getMessages();
        alertDates = event.getDates();
        dismissDialog();
        setContents();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.status_alerts_layout, null);
        this.mContext = getActivity();
        dialog = new ProgressDialog(mContext);
        bindViews(v);
        info = new WebInfoController(mContext);
        if(savedInstanceState == null){
            dialog = new ProgressDialog(mContext);
            dialog.show();
            info.getStatusText();
            info.getAlertsText();
        }else {
            loadSavedInstance(savedInstanceState);
            setContents();
        }
        return v;
    }

    private void dismissDialog(){
        if(alertsLoaded && statusLoaded) dialog.dismiss();
    }

    private void setContents(){
        if(statusLoaded && alertsLoaded) {
            ServiceStatusAdapter statusAdapter = new ServiceStatusAdapter(mContext, statusMessages, statusDates);
            statusListView.setAdapter(statusAdapter);
            ServiceStatusAdapter alertsAdapter = new ServiceStatusAdapter(mContext, alertMessages, alertDates);
            alertsListView.setAdapter(alertsAdapter);
        }
    }

    private void loadSavedInstance(Bundle savedState){
        statusMessages = savedState.getStringArrayList("STATUSMESSAGES");
        statusDates = savedState.getStringArrayList("STATUSDATES");
        alertMessages = savedState.getStringArrayList("ALERTMESSAGES");
        alertDates = savedState.getStringArrayList("ALERTDATES");
        statusLoaded = savedState.getBoolean("STATUSLOADED");
        alertsLoaded = savedState.getBoolean("ALERTSLOADED");
    }

    private void bindViews(View v){
        statusListView = (ListView) v.findViewById(R.id.status_listview);
        alertsListView = (ListView) v.findViewById(R.id.alerts_listview);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("STATUSMESSAGES", statusMessages);
        outState.putStringArrayList("STATUSDATES", statusDates);
        outState.putStringArrayList("ALERTMESSAGES", alertMessages);
        outState.putStringArrayList("ALERTDATES", alertDates);
        outState.putBoolean("STATUSLOADED", statusLoaded);
        outState.putBoolean("ALERTSLOADED", alertsLoaded);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

}
