package com.dexafree.andfgc.app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import com.dexafree.andfgc.app.MainActivity;
import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.adapters.AlternativesAdapter;
import com.dexafree.andfgc.app.beans.Cerca;
import com.dexafree.andfgc.app.beans.Opcio;
import com.quentindommerc.superlistview.SuperGridview;

import java.util.ArrayList;

/**
 * Created by Carlos on 04/05/2014.
 */
public class AlternativeOptionsFragment extends Fragment {

    private Context mContext;

    private String primeraParada;
    private String ultimaParada;

    private String dataBuscada;
    private Cerca mCerca;

    private ArrayList<Opcio> opcions;

    private SuperGridview sgv;
    private TextView firstStationTV;
    private TextView lastStationTV;


    public void setUltimaParada(String ultimaParada) {
        this.ultimaParada = ultimaParada;
    }

    public void setPrimeraParada(String primeraParada) {
        this.primeraParada = primeraParada;
    }

    private void setOpcions(ArrayList<Opcio> opts){
        this.opcions = opts;
    }

    private void setDataBuscada(String data){
        this.dataBuscada = data;
    }

    private void setCerca(Cerca c){
        this.mCerca = c;
    }

    public static final AlternativeOptionsFragment newInstance(Cerca c, String dataBuscada, String primeraParada, String ultimaParada){

        AlternativeOptionsFragment f = new AlternativeOptionsFragment();
        ArrayList<Opcio> options = new ArrayList<Opcio>();

        for(int i=1;i<c.getOpcions().size();i++){
            options.add(c.getOpcions().get(i));
        }
        f.setCerca(c);
        f.setOpcions(options);
        f.setPrimeraParada(primeraParada);
        f.setUltimaParada(ultimaParada);
        f.setDataBuscada(dataBuscada);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.show_alternatives,null);
        this.mContext = getActivity();
        setup(v);
        if(savedInstanceState != null) loadValues(savedInstanceState);
        setValues();
        return v;
    }

    private void loadValues(Bundle savedState){
        primeraParada = savedState.getString("PRIMERAPARADA");
        ultimaParada = savedState.getString("ULTIMAPARADA");
        dataBuscada = savedState.getString("DATABUSCADA");
        mCerca = savedState.getParcelable("CERCA");
        opcions = savedState.getParcelableArrayList("OPCIONS");
    }

    private void setValues(){
        firstStationTV.setText(primeraParada);
        lastStationTV.setText(ultimaParada);

        AlternativesAdapter adapter = new AlternativesAdapter(mContext, opcions, dataBuscada);
        sgv.setAdapter(adapter);

        sgv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SearchResultFragment f = SearchResultFragment.newInstanceFromOptions(opcions.get(i),dataBuscada, mCerca);
                ((MainActivity)mContext).changeFragment(f);
            }
        });

    }

    private void setup(View v){
        sgv = (SuperGridview)v.findViewById(R.id.super_grid_view);
        firstStationTV = (TextView)v.findViewById(R.id.directions_startpoint_textbox);
        lastStationTV = (TextView)v.findViewById(R.id.directions_endpoint_textbox);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("PRIMERAPARADA", primeraParada);
        outState.putString("ULTIMAPARADA", ultimaParada);
        outState.putString("DATABUSCADA", dataBuscada);
        outState.putParcelable("CERCA", mCerca);
        outState.putParcelableArrayList("OPCIONS", opcions);
    }
}
