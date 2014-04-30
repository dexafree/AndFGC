package com.dexafree.andfgc.app.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.adapters.ParadaSpinnerAdapter;
import com.dexafree.andfgc.app.beans.Cerca;
import com.dexafree.andfgc.app.beans.Parada;
import com.dexafree.andfgc.app.controllers.ParadaController;

import java.util.ArrayList;


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
        View v = inflater.inflate(R.layout.search_fragment_layout, container, false);
        setHasOptionsMenu(true);

        Spinner spinnerLinia = (Spinner)v.findViewById(R.id.spinner_linia);
        final Spinner spinnerParada = (Spinner)v.findViewById(R.id.spinner_parada);
        spinnerParada.setVisibility(View.GONE);

        final String[] linies = new String[]{"1", "2", "3"};

        ArrayAdapter<String> adaptadorLinias =
                new ArrayAdapter<String>(mContext, R.layout.spinner_item, linies);


        spinnerLinia.setAdapter(adaptadorLinias);

        String prompt = getString(R.string.selecciona_linia);

        spinnerLinia.setPrompt(prompt);

        spinnerLinia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0) {
                    int linia = Integer.parseInt(linies[i]);
                    ArrayList<Parada> parades = ParadaController.getParadesFromLiniaAsArrayList(mContext, linia);


                    ParadaSpinnerAdapter adaptadorParadas = new ParadaSpinnerAdapter(mContext, parades);

                    spinnerParada.setAdapter(adaptadorParadas);
                    spinnerParada.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return v;
    }

    private void onSearchFinished(Cerca c){

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(rec);
    }
}
