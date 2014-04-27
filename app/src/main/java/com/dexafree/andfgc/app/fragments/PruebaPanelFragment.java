package com.dexafree.andfgc.app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.adapters.VerParadasExpandidasAdapter;
import com.dexafree.andfgc.app.beans.Parada;
import com.dexafree.andfgc.app.utils.Logger;

import java.util.ArrayList;

/**
 * Created by Carlos on 28/04/2014.
 */
public class PruebaPanelFragment extends Fragment {

    private Context mContext;
    private Logger l = new Logger();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_result_panel, null);

        ExpandableListView elv = (ExpandableListView) v.findViewById(R.id.lista_paradas);

        mContext = getActivity();

        ArrayList<Parada> parades = new ArrayList<Parada>();
        Parada p = new Parada("PRUEBA", "AA");
        for(int i=0;i<5;i++){
            parades.add(p);
        }

        VerParadasExpandidasAdapter adapter = new VerParadasExpandidasAdapter(mContext, parades);

        elv.setClickable(true);

        elv.setAdapter(adapter);


        return v;
    }
}
