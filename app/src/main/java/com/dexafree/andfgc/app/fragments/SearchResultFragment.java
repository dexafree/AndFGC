package com.dexafree.andfgc.app.fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.beans.Cerca;
import com.dexafree.andfgc.app.beans.Opcio;
import com.dexafree.andfgc.app.connections.BuscaHoraris;

/**
 * Created by Carlos on 28/04/2014.
 */
public class SearchResultFragment extends Fragment {

    private Context mContext;

    private TextView departureStation;
    private TextView arrivalStation;
    private TextView departureHour;
    private TextView arrivalHour;


    private Cerca c;

    private BroadcastReceiver searchFinishedReceiver;

    private ProgressDialog dialog;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog = new ProgressDialog(mContext);
        dialog.setMessage(getString(R.string.searching_message));
        dialog.setTitle(getString(R.string.searching_title));
        dialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_result_panel, null);
        mContext = getActivity();
        setup(v);
        mContext.registerReceiver(searchFinishedReceiver, new IntentFilter(BuscaHoraris.SEARCH_COMPLETED));
        return v;
    }

    private void setup(View v){

        departureStation = (TextView)v.findViewById(R.id.directions_startpoint_textbox);
        arrivalStation = (TextView)v.findViewById(R.id.directions_endpoint_textbox);
        departureHour = (TextView)v.findViewById(R.id.departure_hour);
        arrivalHour = (TextView)v.findViewById(R.id.arrival_hour);




        searchFinishedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                c = intent.getExtras().getParcelable("CERCA");
                Opcio op = c.getFromOptions(0);
                departureStation.setText(op.getSortida());
                arrivalStation.setText(op.getArribada());
                fillLayout();
                dialog.dismiss();
            }
        };
    }

    private void fillLayout(){

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(searchFinishedReceiver);
    }
}
