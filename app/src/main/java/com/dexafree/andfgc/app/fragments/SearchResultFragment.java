package com.dexafree.andfgc.app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.adapters.VerParadasExpandidasAdapter;
import com.dexafree.andfgc.app.beans.Cerca;
import com.dexafree.andfgc.app.beans.Opcio;
import com.dexafree.andfgc.app.beans.Parada;
import com.dexafree.andfgc.app.beans.Transbord;
import com.dexafree.andfgc.app.controllers.ParadaController;
import com.dexafree.andfgc.app.controllers.TransbordController;
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

    private LinearLayout paradasLayout;


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
        setup(v, inflater);
        return v;
    }

    private void setup(View v, LayoutInflater inflater){
        departureStation = (TextView)v.findViewById(R.id.directions_startpoint_textbox);
        arrivalStation = (TextView)v.findViewById(R.id.directions_endpoint_textbox);
        departureHour = (TextView)v.findViewById(R.id.departure_hour);
        arrivalHour = (TextView)v.findViewById(R.id.arrival_hour);
        paradasLayout = (LinearLayout)v.findViewById(R.id.stations_layout);

        Opcio op = c.getFromOptions(0);
        ArrayList<Transbord> transbords = op.getTransbords();;
        ArrayList<Parada> parades = TransbordController.getAllParadesFromTransbords(transbords);

        Logger.d("PARADAIniciFragment", c.getParadaInici());
        Logger.d("PARADAFiFragment", c.getParadaFi());
        departureStation.setText(parades.get(0).getNom());
        arrivalStation.setText(parades.get(parades.size()-1).getNom());
        departureHour.setText(preparaHora(op.getHoraSortida()));
        arrivalHour.setText(preparaHora(op.getHoraArribada()));




        View rowView;
        for(int i=0;i<parades.size();i++){
            rowView = getParadaView(parades.get(i), inflater);
            paradasLayout.addView(rowView);
        }

    }

    private View getParadaView(Parada paradaActual, LayoutInflater inflater){
        View v = inflater.inflate(R.layout.search_result_paradas_list_item, null);
        ImageView linia = (ImageView)v.findViewById(R.id.icono_linea);
        TextView nom = (TextView)v.findViewById(R.id.nombre_parada_expanded);


        nom.setText(paradaActual.getNom());
        Logger.d("LINIA", paradaActual.getLinia());
        int iconoLinea;
        if(paradaActual.getLinia().equalsIgnoreCase("R6")) iconoLinea = R.drawable.r6;
        else if(paradaActual.getLinia().equalsIgnoreCase("R60")) iconoLinea = R.drawable.r60;
        else if(paradaActual.getLinia().equalsIgnoreCase("R5")) iconoLinea = R.drawable.r5;
        else if(paradaActual.getLinia().equalsIgnoreCase("R50")) iconoLinea = R.drawable.r50;
        else if(paradaActual.getLinia().equalsIgnoreCase("L8")) iconoLinea = R.drawable.l8;
        else if(paradaActual.getLinia().equalsIgnoreCase("S1")) iconoLinea = R.drawable.s1;
        else if(paradaActual.getLinia().equalsIgnoreCase("S2")) iconoLinea = R.drawable.s2;
        else if(paradaActual.getLinia().equalsIgnoreCase("S4")) iconoLinea = R.drawable.s4;
        else if(paradaActual.getLinia().equalsIgnoreCase("S8")) iconoLinea = R.drawable.s8;
        else if(paradaActual.getLinia().equalsIgnoreCase("S33")) iconoLinea = R.drawable.s33;
        else iconoLinea = R.drawable.fgclogo;
        linia.setImageResource(iconoLinea);

        return v;

    }

}
