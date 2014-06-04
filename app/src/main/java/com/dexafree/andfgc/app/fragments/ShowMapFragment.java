package com.dexafree.andfgc.app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.dexafree.andfgc.app.beans.Coordenada;
import com.dexafree.andfgc.app.controllers.ParadaController;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Carlos on 04/06/14.
 */
public class ShowMapFragment extends SupportMapFragment{

    private GoogleMap mMap;
    private Context mContext;

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
        ArrayList<Coordenada> coordenadas = ParadaController.getAllCoords(mContext);

        Coordenada coord;

        for(int i=0;i<coordenadas.size();i++){
            coord = coordenadas.get(i);
            mMap.addMarker(new MarkerOptions()
            .position(new LatLng(coord.getX(), coord.getY()))
            .title(coord.getNom()));
        }
    }
}
