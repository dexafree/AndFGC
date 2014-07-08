package com.dexafree.andfgc.app.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.dexafree.andfgc.app.MainActivity;
import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.beans.Coordenada;
import com.dexafree.andfgc.app.beans.Parada;
import com.dexafree.andfgc.app.controllers.ParadaController;
import com.dexafree.andfgc.app.utils.Logger;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Carlos on 04/06/14.
 */
public class ShowMapFragment extends SupportMapFragment{

    private GoogleMap mMap;
    private Context mContext;
    private ArrayList<Coordenada> coordenadas;

    private Map<Marker, Coordenada> enlaces = new HashMap<Marker, Coordenada>();

    private final float ZOOM = 9.820687f;
    private final double LAT = 41.516600362876126;
    private final double LON = 1.8991706147789955;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMap = getMap();
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(LAT, LON), ZOOM);
        mMap.moveCamera(update);
        this.mContext = getActivity();
        setMarkers();

    }

    private void setMarkers(){
        coordenadas = ParadaController.getAllCoords(mContext);

        for(int i=0;i<coordenadas.size();i++){
            Coordenada coord = coordenadas.get(i);

            MarkerOptions opts = new MarkerOptions();
            opts.position(new LatLng(coord.getX(), coord.getY()));
            opts.title(coord.getNom());


            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {

                    final Coordenada pressed = enlaces.get(marker);

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    String[] fromToHere = new String[]{
                            getString(R.string.from_here),
                            getString(R.string.to_here)

                    };
                    builder.setItems(fromToHere, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            showSearch(pressed, i==0);
                        }
                    });

                    builder.create().show();
                }
            });

            Marker m = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(coord.getX(), coord.getY()))
                    .title(coord.getNom()));
            enlaces.put(m, coord);
        }
    }

    private void showSearch(Coordenada pressed, boolean fromHere){
        Fragment f = new SearchFragment();
        Parada p = ParadaController.getParadaFromCoordenada(mContext, pressed);
        Bundle b = new Bundle();
        b.putParcelable("PARADA", p);
        b.putBoolean(SearchFragment.FROM_MAP, true);
        b.putBoolean(SearchFragment.FROM_HERE, fromHere);
        f.setArguments(b);
        ((MainActivity)mContext).changeFragment(f, 0);
    }
}
