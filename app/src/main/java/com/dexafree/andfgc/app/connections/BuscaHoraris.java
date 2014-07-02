package com.dexafree.andfgc.app.connections;


import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.beans.Cerca;
import com.dexafree.andfgc.app.beans.Opcio;
import com.dexafree.andfgc.app.beans.Parada;
import com.dexafree.andfgc.app.beans.Transbord;
import com.dexafree.andfgc.app.controllers.ParadaController;
import com.dexafree.andfgc.app.controllers.TransbordController;
import com.dexafree.andfgc.app.events.BusProvider;
import com.dexafree.andfgc.app.events.SearchFinishedEvent;
import com.dexafree.andfgc.app.utils.Checkers;
import com.dexafree.andfgc.app.utils.Logger;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Collections;

public class BuscaHoraris {

    public final static String SEARCH_COMPLETED = "COM.DEXAFREE.ANDFGC.CERCA_FINALITZADA";

    private int linea;
    private String lineaAsString;
    private String origen;
    private String desti;
    private String tipus;
    private String dia;
    private int hores;
    private String horesAsString;
    private int minuts;
    private String minutsAsString;
    private Context mContext;


    public BuscaHoraris(int linea, String origen, String desti, String tipus, String dia, int hores, int minuts, Context context){
        this.linea = linea;
        this.lineaAsString = linea+"";
        this.origen = origen;
        this.desti = desti;
        this.tipus = tipus;
        this.dia = dia;
        this.hores = hores;
        this.minuts = minuts;
        this.mContext = context;

        if(hores < 10){
            this.horesAsString = "0"+hores;
        } else {
            this.horesAsString = hores+"";
        }

        if(minuts < 10){
            this.minutsAsString = "0"+minuts;
        } else {
            this.minutsAsString = minuts+"";
        }

    }

    public BuscaHoraris(){

    }

    public void setContext(Context context){
        this.mContext = context;
    }

    public Context getContext(){
        return mContext;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
        this.lineaAsString = linea+"";
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDesti() {
        return desti;
    }

    public void setDesti(String desti) {
        this.desti = desti;
    }

    public String getTipus() {
        return tipus;
    }

    public void setTipus(String tipus) {
        this.tipus = tipus;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public int getHores() {
        return hores;
    }

    public void setHores(int hores) {
        if(hores < 10){
            this.horesAsString = "0"+hores;
        } else {
            this.horesAsString = hores+"";
        }
    }

    public int getMinuts() {
        return minuts;
    }

    public void setMinuts(int minuts) {
        if(minuts < 10){
            this.minutsAsString = "0"+minuts;
        } else {
            this.minutsAsString = minuts+"";
        }
    }

    private boolean isOptionValid(Opcio opcio){

        return !(opcio.getLinia().equalsIgnoreCase("NO"));
    }

    public void cercar(){
        if(Checkers.hasInternet(mContext)){
            Ion.with(mContext)
                    .load("POST", "http://www.fgc.cat/cercador/cerca.asp")
                    .setHeader("Content-Type", "application/x-www-form-urlencoded")
                    .setBodyParameter("liniasel", lineaAsString)
                    .setBodyParameter("estacio_origen", origen)
                    .setBodyParameter("estacio_desti", desti)
                    .setBodyParameter("tipus", tipus)
                    .setBodyParameter("dia", dia)
                    .setBodyParameter("horas", horesAsString)
                    .setBodyParameter("minutos", minutsAsString)
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        @Override
                        public void onCompleted(Exception e, JsonArray result) {
                            try {
                                Cerca c;

                                ArrayList<Opcio> opcions = new ArrayList<Opcio>();
                                JsonArray main = result.get(0).getAsJsonArray().get(0).getAsJsonArray();
                                String paradaInici = "";
                                String paradaFi = "";

                                for (int i = 0; i < main.size(); i++) {

                                    JsonArray transbords = main.get(i).getAsJsonArray();
                                    ArrayList<Transbord> transbordsList = new ArrayList<Transbord>();
                                    String linia = "";
                                    String sortida = "";
                                    String arribada = "";

                                    for (int k = 0; k < transbords.size(); k++) {

                                        JsonObject mainObject = transbords.get(k).getAsJsonObject();
                                        linia = mainObject.get("linia").getAsString();
                                        sortida = mainObject.get("sortida").getAsString();
                                        arribada = mainObject.get("arribada").getAsString();
                                        JsonArray parades = mainObject.get("estacions").getAsJsonArray();
                                        ArrayList<Parada> estacions = new ArrayList<Parada>();

                                        for (int z = 0; z < parades.size(); z++) {
                                            Parada p = ParadaController.getParadaFromAbreviatura(mContext, parades.get(z).getAsString());
                                            p.setLinia(linia);
                                            estacions.add(p);
                                        }

                                        Transbord t = new Transbord(linia, sortida, arribada, estacions);
                                        transbordsList.add(t);
                                    }
                                    Opcio op = new Opcio();
                                    op.setLinia(linia);

                                    if(tipus.equalsIgnoreCase("A")) Collections.reverse(transbordsList);

                                    sortida = transbordsList.get(0).getSortida();

                                    arribada = transbordsList.get(transbordsList.size()-1).getArribada();

                                    op.setHoraSortida(sortida);
                                    op.setHoraArribada(arribada);
                                    op.setTransbords(transbordsList);
                                    if(isOptionValid(op)) opcions.add(op);
                                }

                                c = new Cerca();


                                ArrayList<Transbord> t = opcions
                                        .get(0)
                                        .getTransbords();


                                ArrayList<Parada> pa = TransbordController.getAllParadesFromTransbords(t);
                                paradaInici = pa.get(0).getNom();
                                paradaFi = pa.get(pa.size()-1).getNom();

                                c.setOpcions(opcions);
                                c.setParadaInici(paradaInici);
                                c.setParadaFi(paradaFi);
                                c.setLiniaCercada(linea);
                                c.setParadaIniciAbr(origen);
                                c.setParadaFiAbr(desti);


                                BusProvider.getInstance().post(new SearchFinishedEvent(c));
                            } catch (ClassCastException exception){
                                JsonObject object = result.getAsJsonObject();
                                String error = object.get("path").getAsString();

                                Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        } else {
            Toast.makeText(mContext, R.string.no_internet, Toast.LENGTH_SHORT).show();
        }
    }



}
