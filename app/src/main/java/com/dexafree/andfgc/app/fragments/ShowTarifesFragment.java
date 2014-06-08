package com.dexafree.andfgc.app.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.beans.Ticket;
import com.dexafree.andfgc.app.connections.GetTarifes;
import com.dexafree.andfgc.app.events.BusProvider;
import com.dexafree.andfgc.app.events.TarifesSearchFinishedEvent;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

/**
 * Created by Carlos on 06/06/14.
 */
public class ShowTarifesFragment extends Fragment {

    private final String TICKETS = "TICKETS";

    private Context mContext;
    private ProgressDialog dialog;

    private ArrayList<Ticket> ticketsList;

    @Subscribe
    public void onTarifesSearchFinished(TarifesSearchFinishedEvent event){
        ticketsList = event.getTickets();
        //setContent();
        dialog.dismiss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_fragment_layout, null); // CAMBIAR, SOLO PARA QUE NO PETE
        this.mContext = getActivity();
        //bindViews(v);
        if(savedInstanceState == null){
            dialog = new ProgressDialog(mContext);
            dialog.setTitle(getString(R.string.downloading_tickets));
            dialog.setMessage(getString(R.string.please_wait));
            dialog.show();
            GetTarifes.getTarifes(mContext);
        } else {
            loadValues(savedInstanceState);
            //setContent();
        }
        return v;
    }

    public void loadValues(Bundle savedState){
        ticketsList = savedState.getParcelableArrayList(TICKETS);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(TICKETS, ticketsList);
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
