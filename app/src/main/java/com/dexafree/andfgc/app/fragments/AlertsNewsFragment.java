package com.dexafree.andfgc.app.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.adapters.ServiceStatusAdapter;
import com.dexafree.andfgc.app.connections.GetWebInfo;
import com.dexafree.andfgc.app.events.AlertSearchFinishedEvent;
import com.dexafree.andfgc.app.events.BusProvider;
import com.dexafree.andfgc.app.events.StatusSearchFinishedEvent;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

/**
 * Created by Carlos on 18/05/2014.
 */
public class AlertsNewsFragment extends Fragment {

    private GetWebInfo info;

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
        info = new GetWebInfo(mContext);
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
