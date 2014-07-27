package com.dexafree.andfgc.app.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.beans.Ticket;
import com.dexafree.andfgc.app.controllers.TarifesController;
import com.dexafree.andfgc.app.events.BusProvider;
import com.dexafree.andfgc.app.events.ErrorEvent;
import com.dexafree.andfgc.app.events.TarifesSearchFinishedEvent;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;


public class ShowTarifesFragment extends Fragment {

    private final String TICKETS = "TICKETS";

    private Context mContext;
    private ProgressDialog dialog;
    private View mView;

    private ArrayList<Ticket> ticketsList;

    @Subscribe
    public void onTarifesSearchFinished(TarifesSearchFinishedEvent event){
        ticketsList = event.getTickets();
        dialog.dismiss();

        setViewContent();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent event){
        dialog.dismiss();
        Toast.makeText(mContext, getString(R.string.download_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tarifas_layout, null);

        this.mContext = getActivity();
        mView = v;

        if(savedInstanceState == null){
            dialog = new ProgressDialog(mContext);
            dialog.setTitle(getString(R.string.downloading_tickets));
            dialog.setMessage(getString(R.string.please_wait));
            dialog.show();
            TarifesController.getTarifes(mContext);
        } else {
            loadValues(savedInstanceState);
            setViewContent();
        }
        return v;
    }

    private void setViewContent(){
        ((LinearLayout)mView.findViewById(R.id.mainLayout)).addView(generateCards());
    }

    private void loadValues(Bundle savedState){
        ticketsList = savedState.getParcelableArrayList(TICKETS);
    }

    private View generateCards(){
        LinearLayout allTheTickets = new LinearLayout(mContext);
        allTheTickets.setOrientation(LinearLayout.VERTICAL);
        for(int i=0;i<ticketsList.size();i++){
            allTheTickets.addView(generateCard(i));
        }

        return allTheTickets;
    }

    private View generateCard(final int position){
        View v = View.inflate(mContext, R.layout.tarifa_item_layout, null);

        ((TextView)v.findViewById(R.id.ticketTitle)).setText(ticketsList.get(position).getName());
        ((LinearLayout)v.findViewById(R.id.tarifaContentLayout)).addView(createTable(position));

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoDialog(position);
            }
        });

        return v;
    }

    private View createTable(int position){

        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);

        int numFilas = ticketsList.get(position).getProperties().size();

        layout.addView(generateHeaderView());

        for(int i=0;i<numFilas;i++) {

            View v = View.inflate(mContext, R.layout.tarifas_row_item_layout, null);
            ((TextView) v.findViewById(R.id.sectionTitle)).setText(ticketsList.get(position).getProperties().get(i).getName());
            ((TextView) v.findViewById(R.id.zone1)).setText(ticketsList.get(position).getProperties().get(i).getValues().get(0));
            ((TextView) v.findViewById(R.id.zone2)).setText(ticketsList.get(position).getProperties().get(i).getValues().get(1));
            ((TextView) v.findViewById(R.id.zone3)).setText(ticketsList.get(position).getProperties().get(i).getValues().get(2));
            ((TextView) v.findViewById(R.id.zone4)).setText(ticketsList.get(position).getProperties().get(i).getValues().get(3));
            ((TextView) v.findViewById(R.id.zone5)).setText(ticketsList.get(position).getProperties().get(i).getValues().get(4));
            ((TextView) v.findViewById(R.id.zone6)).setText(ticketsList.get(position).getProperties().get(i).getValues().get(5));
            layout.addView(v);
        }

        return layout;
    }

    private View generateHeaderView(){
        View headerView = View.inflate(mContext,R.layout.tarifas_row_item_layout, null);
        ((TextView) headerView.findViewById(R.id.sectionTitle)).setText("ZONA");
        ((TextView) headerView.findViewById(R.id.zone1)).setText("1");
        ((TextView) headerView.findViewById(R.id.zone2)).setText("2");
        ((TextView) headerView.findViewById(R.id.zone3)).setText("3");
        ((TextView) headerView.findViewById(R.id.zone4)).setText("4");
        ((TextView) headerView.findViewById(R.id.zone5)).setText("5");
        ((TextView) headerView.findViewById(R.id.zone6)).setText("6");
        return headerView;
    }

    private void showInfoDialog(int position){

        Ticket ticket = ticketsList.get(position);

        AlertDialog dialog;

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(ticket.getName());
        builder.setMessage(ticket.getDescription());
        dialog = builder.create();

        dialog.setCustomTitle(generateDialogHeaderView(ticket.getName()));

        dialog.show();

        int dividerId = dialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);

        View divider = dialog.findViewById(dividerId);

        divider.setBackgroundColor(mContext.getResources().getColor(R.color.green_ticket));


    }

    private View generateDialogHeaderView(String title){

        LayoutInflater inflater = LayoutInflater.from(mContext);

        View header = inflater.inflate(R.layout.dialog_header, null);

        TextView titleTextView = (TextView)header.findViewById(R.id.dialog_title);

        titleTextView.setText(title);

        return header;

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
