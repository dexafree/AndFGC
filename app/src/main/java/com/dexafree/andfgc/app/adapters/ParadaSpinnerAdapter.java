package com.dexafree.andfgc.app.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.beans.Parada;

import java.util.ArrayList;

public class ParadaSpinnerAdapter extends BaseAdapter {

    static class ViewHolder {
        TextView nomParada;
    }

    public Context c;

    private AlertDialog alertDialog;


    private static final String TAG = "CustomAdapter";
    private static int convertViewCounter = 0;

    private ArrayList<Parada> paradesList = new ArrayList<Parada>();
    private LayoutInflater inflater = null;

    public ParadaSpinnerAdapter(Context c, ArrayList<Parada> p){
        //Log.v(TAG, "Constructing CustomAdapter");

        this.c = c;
        this.paradesList = p;
        inflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount(){
        //Log.v(TAG, "in getCount()");
        return paradesList.size();
    }

    @Override
    public Object getItem(int position){
        //Log.v(TAG, "in getItem() for position " + position);
        return paradesList.get(position);
    }

    @Override
    public long getItemId(int position){
        //Log.v(TAG, "in getItemId() for position " + position);
        return position;
    }

    @Override
    public int getViewTypeCount(){
        //Log.v(TAG, "in getViewTypeCount()");
        return 1;
    }

    @Override
    public int getItemViewType(int position){
        //Log.v(TAG, "in getItemViewType() for position " + position);
        return 0;
    }

    @Override
    public void notifyDataSetChanged(){
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        ViewHolder holder;

        //Log.v(TAG, "in getView for position " + position + ", convertView is "
        //      + ((convertView == null) ? "null" : "being recycled"));

        if (convertView == null){
            convertView = inflater.inflate(R.layout.spinner_item_parada, null);

            convertViewCounter++;
            //Log.v(TAG, convertViewCounter + " convertViews have been created");

            holder = new ViewHolder();

            holder.nomParada = (TextView) convertView.findViewById(R.id.nom_parada);

            convertView.setTag(holder);

        } else{
            holder = (ViewHolder) convertView.getTag();
        }

        String nomParada = paradesList.get(position).getNom();

        holder.nomParada.setText(nomParada);
        holder.nomParada.setTypeface(null, Typeface.BOLD);


        return convertView;
    }
}