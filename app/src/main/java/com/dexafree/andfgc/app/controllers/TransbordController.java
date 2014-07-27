package com.dexafree.andfgc.app.controllers;

import com.dexafree.andfgc.app.beans.Parada;
import com.dexafree.andfgc.app.beans.Transbord;

import java.util.ArrayList;

public class TransbordController {

    /*
     * Returns all the stations from a Transbord object
     */
    public static ArrayList<Parada> getAllParadesFromTransbords(ArrayList<Transbord> transbords){
        ArrayList<Parada> parades = new ArrayList<Parada>();

        for (int i = 0; i < transbords.size(); i++) {
            for (int j = 0; j < transbords.get(i).getParades().size(); j++) {
                parades.add(transbords.get(i).getParades().get(j));
            }
        }


        return parades;
    }
}
