package com.dexafree.andfgc.app.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.dexafree.andfgc.app.MainActivity;
import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.beans.Cerca;
import com.dexafree.andfgc.app.beans.Favorito;
import com.dexafree.andfgc.app.beans.Opcio;
import com.dexafree.andfgc.app.beans.Parada;
import com.dexafree.andfgc.app.beans.Transbord;
import com.dexafree.andfgc.app.controllers.FavoritosController;
import com.dexafree.andfgc.app.controllers.TransbordController;

import java.util.ArrayList;

public class SearchResultFragment extends Fragment {

    private Context mContext;
    private Cerca c;
    private Opcio opcio;

    private TextView departureStation;
    private TextView arrivalStation;
    private TextView departureHour;
    private TextView arrivalHour;

    private String dataBuscada;

    private MenuItem favIcon;
    private MenuItem unFavIcon;

    private LinearLayout paradasLayout;
    private RelativeLayout moreOptionsLayout;

    private boolean fromSearch;


    public static final SearchResultFragment newInstance(Cerca c, String data){
        SearchResultFragment f = new SearchResultFragment();
        f.setCerca(c);
        f.setDataBuscada(data);
        f.setFromSearch(true);
        return f;
    }

    public static final SearchResultFragment newInstanceFromOptions(Opcio op, String data, Cerca c){
        SearchResultFragment f = new SearchResultFragment();
        f.setFromSearch(false);
        f.setOpcio(op);
        f.setDataBuscada(data);
        f.setCerca(c);
        return f;
    }

    private void setCerca(Cerca c){
        this.c = c;
    }

    private void setDataBuscada(String data){
        this.dataBuscada = data;
    }

    private void setFromSearch(boolean fromSearch){
        this.fromSearch = fromSearch;
    }

    private void setOpcio(Opcio op){
        this.opcio = op;
    }

    private String preparaHora(String data){
        String[] array = data.split(" ");

        String hora = dataBuscada+" "+array[1];

        return hora;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) loadValues(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_result_panel, null);
        mContext = getActivity();
        setup(v);
        setTexts(inflater);
        return v;
    }

    private void setup(View v){
        departureStation = (TextView)v.findViewById(R.id.directions_startpoint_textbox);
        arrivalStation = (TextView)v.findViewById(R.id.directions_endpoint_textbox);
        departureHour = (TextView)v.findViewById(R.id.departure_hour);
        arrivalHour = (TextView)v.findViewById(R.id.arrival_hour);
        paradasLayout = (LinearLayout)v.findViewById(R.id.stations_layout);
        moreOptionsLayout = (RelativeLayout)v.findViewById(R.id.more_options_layout);
    }

    private void setTexts(LayoutInflater inflater){
        Opcio op;
        if(fromSearch) op = c.getFromOptions(0);
        else op = opcio;
        ArrayList<Transbord> transbords = op.getTransbords();
        final ArrayList<Parada> parades = TransbordController.getAllParadesFromTransbords(transbords);

        departureStation.setText(parades.get(0).getNom());
        arrivalStation.setText(parades.get(parades.size()-1).getNom());
        departureHour.setText(preparaHora(op.getHoraSortida()));
        arrivalHour.setText(preparaHora(op.getHoraArribada()));

        moreOptionsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlternativeOptionsFragment fragment = AlternativeOptionsFragment.newInstance(c, dataBuscada, parades.get(0).getNom(),parades.get(parades.size()-1).getNom());
                ((MainActivity)mContext).changeFragment(fragment);
            }
        });

        View rowView;
        for(int i=0;i<parades.size();i++){
            rowView = getParadaView(parades.get(i), inflater);
            paradasLayout.addView(rowView);
        }
    }

    private void loadValues(Bundle savedState){
        c = savedState.getParcelable("CERCA");
        opcio = savedState.getParcelable("OPCIO");
        dataBuscada = savedState.getString("DATABUSCADA");
        fromSearch = savedState.getBoolean("FROMSEARCH");
    }

    private View getParadaView(Parada paradaActual, LayoutInflater inflater){
        View v = inflater.inflate(R.layout.search_result_paradas_list_item, null);
        ImageView linia = (ImageView)v.findViewById(R.id.icono_linea);
        TextView nom = (TextView)v.findViewById(R.id.nombre_parada_expanded);


        nom.setText(paradaActual.getNom());
        //Logger.d("LINIA", paradaActual.getLinia());
        int iconoLinea;

        String tLinia = paradaActual.getLinia();

        if(tLinia.equalsIgnoreCase("R6")) iconoLinea = R.drawable.r6;
        else if(tLinia.equalsIgnoreCase("R60")) iconoLinea = R.drawable.r60;
        else if(tLinia.equalsIgnoreCase("R5")) iconoLinea = R.drawable.r5;
        else if(tLinia.equalsIgnoreCase("R50")) iconoLinea = R.drawable.r50;
        else if(tLinia.equalsIgnoreCase("L8")) iconoLinea = R.drawable.l8;
        else if(tLinia.equalsIgnoreCase("S1")) iconoLinea = R.drawable.s1;
        else if(tLinia.equalsIgnoreCase("S2")) iconoLinea = R.drawable.s2;
        else if(tLinia.equalsIgnoreCase("S4")) iconoLinea = R.drawable.s4;
        else if(tLinia.equalsIgnoreCase("S8")) iconoLinea = R.drawable.s8;
        else if(tLinia.equalsIgnoreCase("S33")) iconoLinea = R.drawable.s33;
        else iconoLinea = R.drawable.fgclogo;
        linia.setImageResource(iconoLinea);

        return v;

    }

    private void showFavDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit, null);
        final EditText favNameText = (EditText)v.findViewById(R.id.fav_name);

        builder.setView(v);

        builder.setTitle(mContext.getString(R.string.save_favorite));

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(TextUtils.isEmpty(favNameText.getText())){
                    Toast.makeText(mContext, R.string.save_favorite, Toast.LENGTH_SHORT).show();
                } else {
                    Favorito f = new Favorito();
                    f.setOrigen(c.getParadaIniciAbr());
                    f.setDesti(c.getParadaFiAbr());
                    f.setLinia(c.getLiniaCercada());
                    f.setTitle(favNameText.getText().toString());
                    FavoritosController.insertFavorito(mContext, f);
                    favIcon.setVisible(false);
                    unFavIcon.setVisible(true);
                }
            }
        });

        builder.create().show();

    }

    private void deleteFav(){
        String origin = c.getParadaIniciAbr();
        String dest = c.getParadaFiAbr();

        FavoritosController.deleteFavoritoFromStations(mContext, origin, dest);
        favIcon.setVisible(true);
        unFavIcon.setVisible(false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("CERCA", c);
        outState.putParcelable("OPCIO", opcio);
        outState.putString("DATABUSCADA", dataBuscada);
        outState.putBoolean("FROMSEARCH", fromSearch);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){

        inflater.inflate(R.menu.search_result, menu);
        if (menu != null) {

            favIcon = menu.findItem(R.id.action_favorite);
            unFavIcon = menu.findItem(R.id.action_unfavorite);

            String origin = c.getParadaIniciAbr();
            String dest = c.getParadaFiAbr();

            boolean fill = false;

            if(FavoritosController.isFavoritoSaved(getActivity(), origin, dest)){
                fill = true;
            }


            favIcon.setVisible(!fill);
            unFavIcon.setVisible(fill);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_favorite:
                showFavDialog();
                return true;

            case R.id.action_unfavorite:
                deleteFav();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
