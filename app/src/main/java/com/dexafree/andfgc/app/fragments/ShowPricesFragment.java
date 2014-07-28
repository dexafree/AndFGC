package com.dexafree.andfgc.app.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.adapters.TarifesAdapter;
import com.dexafree.andfgc.app.beans.Tarifa;
import com.dexafree.andfgc.app.controllers.TarifesController;
import com.dexafree.andfgc.app.utils.Logger;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ShowPricesFragment extends Fragment {

    public final static String DEPARTURE_STATION = "DEPARTURE_STATION";
    public final static String ARRIVAL_STATION = "ARRIVAL_STATION";
    public final static String LINE = "LINE";
    public final static String ZONES = "ZONES";

    private final static String STANDARD_TARIFES = "STANDARD_TARIFES";
    private final static String FNUM_TARIFES = "FNUM_TARIFES";
    private final static String DISCAP_TARIFES = "DISCAP_TARIFES";
    private final static String PENSION_TARIFES = "PENSION_TARIFES";

    private Context mContext;

    private String departure;
    private String arrival;
    private int line;
    private int zones;

    private LinearLayout standardPricesList;
    private LinearLayout fnumPricesList;
    private LinearLayout discapPricesList;
    private LinearLayout pensionPricesList;

    private CardView fnumCardView;
    private CardView discapCardView;
    private CardView pensionCardView;

    private TextView standardText;
    private TextView fnumText;
    private TextView discapText;
    private TextView pensionText;

    private ArrayList<Tarifa> standardTarifes;
    private ArrayList<Tarifa> fnumTarifes;
    private ArrayList<Tarifa> discapTarifes;
    private ArrayList<Tarifa> pensionTarifes;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.more_prices_layout, null);

        mContext = getActivity();

        bindViews(v);

        if(savedInstanceState == null){

            departure = getArguments().getString(DEPARTURE_STATION);
            arrival = getArguments().getString(ARRIVAL_STATION);
            line = getArguments().getInt(LINE);
            zones = getArguments().getInt(ZONES);

            standardTarifes = TarifesController.getTarifaStandard(mContext, line, zones);

            if(line != 3) {
                fnumTarifes = TarifesController.getTarifaFamiliaNombrosa(mContext, zones);
                discapTarifes = TarifesController.getTarifaDiscapacitat(mContext, zones);
                pensionTarifes = TarifesController.getTarifaPensionista(mContext, zones);
            }

        } else {
            loadValues(savedInstanceState);
        }

        setContent();

        return v;
    }

    private void bindViews(View v){
        standardPricesList = (LinearLayout)v.findViewById(R.id.standard_listview);
        fnumPricesList = (LinearLayout)v.findViewById(R.id.fnum_listview);
        discapPricesList = (LinearLayout)v.findViewById(R.id.discap_listview);
        pensionPricesList = (LinearLayout)v.findViewById(R.id.pension_listview);

        standardText = (TextView)v.findViewById(R.id.stand_text);
        fnumText = (TextView)v.findViewById(R.id.fnum_text);
        discapText = (TextView)v.findViewById(R.id.discap_text);
        pensionText = (TextView)v.findViewById(R.id.pension_text);

        fnumCardView = (CardView)v.findViewById(R.id.fnum_cardview);
        discapCardView = (CardView)v.findViewById(R.id.discap_cardview);
        pensionCardView = (CardView)v.findViewById(R.id.pension_cardview);
    }

    private void loadValues(Bundle savedState){
        standardTarifes = savedState.getParcelableArrayList(STANDARD_TARIFES);
        fnumTarifes = savedState.getParcelableArrayList(FNUM_TARIFES);
        discapTarifes = savedState.getParcelableArrayList(DISCAP_TARIFES);
        pensionTarifes = savedState.getParcelableArrayList(PENSION_TARIFES);

        departure = savedState.getString(DEPARTURE_STATION);
        arrival = savedState.getString(ARRIVAL_STATION);
        line = savedState.getInt(LINE);
        zones = savedState.getInt(ZONES);
    }

    private void setContent(){

        String zonesString = " ("+zones +" "+ mContext.getString(R.string.zones)+")";

        if(line == 3){
            fnumCardView.setVisibility(View.GONE);
            discapCardView.setVisibility(View.GONE);
            pensionCardView.setVisibility(View.GONE);

        } else {
            fnumPricesList.addView(generateList(fnumTarifes));
            fnumText.setText(mContext.getString(R.string.fnumerosa)+zonesString);
            discapPricesList.addView(generateList(discapTarifes));
            discapText.setText(mContext.getString(R.string.discapacitat)+zonesString);
            pensionPricesList.addView(generateList(pensionTarifes));
            pensionText.setText(mContext.getString(R.string.pensionista)+zonesString);
        }

        standardPricesList.addView(generateList(standardTarifes));
        standardText.setText(mContext.getString(R.string.standard)+zonesString);

    }

    private View generateList(ArrayList<Tarifa> tarifes){
        LinearLayout allTheTarifes = new LinearLayout(mContext);
        allTheTarifes.setOrientation(LinearLayout.VERTICAL);

        for(int i=0;i<tarifes.size();i++){
            allTheTarifes.addView(generateTarifaList(tarifes.get(i)));
        }

        return allTheTarifes;
    }

    private View generateTarifaList(Tarifa tarifa){
        View v = View.inflate(mContext, R.layout.tarifes_item, null);

        double preu = tarifa.getPreu();

        DecimalFormat df = new DecimalFormat("#.##");
        String preuString = df.format(preu);

        ((TextView)v.findViewById(R.id.concept_text)).setText(tarifa.getConcepte());
        ((TextView)v.findViewById(R.id.price_text)).setText(preuString+" €");

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STANDARD_TARIFES, standardTarifes);
        outState.putParcelableArrayList(FNUM_TARIFES, fnumTarifes);
        outState.putParcelableArrayList(DISCAP_TARIFES, discapTarifes);
        outState.putParcelableArrayList(PENSION_TARIFES, pensionTarifes);

        outState.putString(DEPARTURE_STATION, departure);
        outState.putString(ARRIVAL_STATION, arrival);
        outState.putInt(LINE, line);
        outState.putInt(ZONES, zones);
    }
}
