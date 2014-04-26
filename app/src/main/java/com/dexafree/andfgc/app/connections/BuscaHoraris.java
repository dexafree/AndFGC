package com.dexafree.andfgc.app.connections;


import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.beans.Cerca;
import com.dexafree.andfgc.app.beans.Opcio;
import com.dexafree.andfgc.app.utils.Checkers;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class BuscaHoraris {

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

    public interface CercaFinalitzada{
        public Cerca getCerca();
    }

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

    public void cercar(){

        final Cerca c = new Cerca();

        if(Checkers.hasInternet(mContext)){
            Ion.with(mContext, "http://www.fgc.cat/cercador/cerca.asp")
                    .setBodyParameter("lineasel", lineaAsString)
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
                            JsonArray main = result.get(0).getAsJsonArray().get(0).getAsJsonArray();
                            for(int i=0;i<main.size();i++){
                                JsonObject mainObject = main.get(i).getAsJsonObject();
                                String linia = mainObject.get("linia").getAsString();
                                String sortida = mainObject.get("sortida").getAsString();
                                String arribada = mainObject.get("arribada").getAsString();
                                JsonArray parades = mainObject.get("estacions").getAsJsonArray();
                                String[] estacions = new String[parades.size()];
                                for(int j=0;j<parades.size();j++){
                                    estacions[j] = parades.get(i).getAsString();
                                }
                                Opcio o = new Opcio(linia, sortida, arribada, estacions);
                                c.addToOptions(o);
                            }

                            Intent i = new Intent();
                            i.putExtra("CERCA", c);
                            i.setAction("COM.DEXAFREE.ANDFGC.CERCA_FINALITZADA");
                            mContext.sendBroadcast(i);
                        }
                    });

        } else {
            Toast.makeText(mContext, R.string.no_internet, Toast.LENGTH_SHORT).show();
        }
    }



}
