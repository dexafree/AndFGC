package com.dexafree.andfgc.app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.adapters.VerParadasExpandidasAdapter;
import com.dexafree.andfgc.app.beans.Cerca;
import com.dexafree.andfgc.app.beans.Opcio;
import com.dexafree.andfgc.app.beans.Parada;
import com.dexafree.andfgc.app.beans.Transbord;
import com.dexafree.andfgc.app.controllers.ParadaController;
import com.dexafree.andfgc.app.utils.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Carlos on 28/04/2014.
 */
public class SearchResultFragment extends Fragment {

    private Context mContext;

    private TextView departureStation;
    private TextView arrivalStation;
    private TextView departureHour;
    private TextView arrivalHour;

    private String dataBuscada;

    private ListView slv;


    private Cerca c;

    public static final SearchResultFragment newInstance(Cerca c, String data){
        SearchResultFragment f = new SearchResultFragment();
        f.setCerca(c);
        f.setDataBuscada(data);
        return f;
    }

    private void setCerca(Cerca c){
        this.c = c;
    }

    private void setDataBuscada(String data){
        this.dataBuscada = data;
    }

    private String preparaHora(String data){
        String[] array = data.split(" ");

        String hora = dataBuscada+" "+array[1];

        return hora;
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
        slv = (ListView)v.findViewById(R.id.lista_paradas);

        Opcio op = c.getFromOptions(0);

        Logger.d("PARADAIniciFragment", c.getParadaInici());
        Logger.d("PARADAFiFragment", c.getParadaFi());
        departureStation.setText(ParadaController.getParadaFromAbreviatura(mContext, c.getParadaInici()).getNom());
        arrivalStation.setText(ParadaController.getParadaFromAbreviatura(mContext, c.getParadaFi()).getNom());
        departureHour.setText(preparaHora(op.getHoraSortida()));
        arrivalHour.setText(preparaHora(op.getHoraArribada()));


        ArrayList<Transbord> transbords = op.getTransbords();

        VerParadasExpandidasAdapter adapter = new VerParadasExpandidasAdapter(mContext, transbords);

        slv.setClickable(true);
        slv.setAdapter(adapter);
    }
}
