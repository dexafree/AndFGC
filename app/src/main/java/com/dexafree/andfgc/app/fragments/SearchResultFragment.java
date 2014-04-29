package com.dexafree.andfgc.app.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.beans.Cerca;
import com.dexafree.andfgc.app.beans.Opcio;
import com.dexafree.andfgc.app.beans.Parada;

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

    private ProgressDialog dialog;

    public static final SearchResultFragment newInstance(Cerca c){
        SearchResultFragment f = new SearchResultFragment();
        f.setCerca(c);
        return f;
    }

    private void setCerca(Cerca c){
        this.c = c;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_result_panel, null);
        mContext = getActivity();
        setup(v);
        return v;
    }

    private void setup(View v){

        departureStation = (TextView)v.findViewById(R.id.directions_startpoint_textbox);
        arrivalStation = (TextView)v.findViewById(R.id.directions_endpoint_textbox);
        departureHour = (TextView)v.findViewById(R.id.departure_hour);
        arrivalHour = (TextView)v.findViewById(R.id.arrival_hour);

        Opcio op = c.getFromOptions(0);
        Parada departureStationStop = op.getPrimeraParada(mContext);
        Parada arrivalStationStop = op.getUltimaParada(mContext);
        departureStation.setText(departureStationStop.getNom());
        arrivalStation.setText(arrivalStationStop.getNom());

    }
}
