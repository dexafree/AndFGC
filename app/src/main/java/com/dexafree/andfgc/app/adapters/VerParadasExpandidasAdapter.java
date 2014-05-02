package com.dexafree.andfgc.app.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.beans.Parada;
import com.dexafree.andfgc.app.beans.Transbord;
import com.dexafree.andfgc.app.utils.Logger;
import org.w3c.dom.Text;

public class VerParadasExpandidasAdapter extends BaseAdapter {

    private ArrayList<Transbord> transbords;
    private ArrayList<Parada> parades = new ArrayList<Parada>();
    private LayoutInflater inflater;
    private Transbord transbordActual;
    private Context mContext;
    private static int convertViewCounter = 0;

    static class ViewHolder {
        LinearLayout layout;
        TextView nombreParada;
        ImageView iconoLinea;
        ImageView icono;
    }

    public VerParadasExpandidasAdapter(Context c, ArrayList<Transbord> children) {
        this.transbords = children;

        for(int i=0;i<children.size();i++){
            for(int j=0;j<children.get(i).getParades().size();j++){
                parades.add(children.get(i).getParades().get(j));
            }
        }
        this.mContext = c;
        this.inflater  = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int childPosition, View convertView, ViewGroup viewGroup) {

        ViewHolder holder;



        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.search_result_paradas_list_item, null);
            convertViewCounter++;

            holder.nombreParada = (TextView) convertView.findViewById(R.id.nombre_parada_expanded);
            holder.iconoLinea = (ImageView) convertView.findViewById(R.id.icono_linea);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        Parada paradaActual = parades.get(childPosition);
        holder.nombreParada.setText(paradaActual.getNom());
        int iconoLinea = -1;
        Logger.d("PARADA ACTUAL: "+childPosition, paradaActual.getLinia());
        if(paradaActual.getLinia().equalsIgnoreCase("R6")) iconoLinea = R.drawable.r6;
        else if(paradaActual.getLinia().equalsIgnoreCase("R60")) iconoLinea = R.drawable.r60;
        else if(paradaActual.getLinia().equalsIgnoreCase("R5")) iconoLinea = R.drawable.r5;
        else if(paradaActual.getLinia().equalsIgnoreCase("R50")) iconoLinea = R.drawable.r50;
        else if(paradaActual.getLinia().equalsIgnoreCase("L8")) iconoLinea = R.drawable.l8;
        else if(paradaActual.getLinia().equalsIgnoreCase("S1")) iconoLinea = R.drawable.s1;
        else if(paradaActual.getLinia().equalsIgnoreCase("S2")) iconoLinea = R.drawable.s2;
        else if(paradaActual.getLinia().equalsIgnoreCase("S4")) iconoLinea = R.drawable.s4;
        else if(paradaActual.getLinia().equalsIgnoreCase("S6")) iconoLinea = R.drawable.s8;
        else if(paradaActual.getLinia().equalsIgnoreCase("S33")) iconoLinea = R.drawable.s33;
        else iconoLinea = R.drawable.fgclogo;


        holder.iconoLinea.setImageResource(iconoLinea);


        return convertView;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return parades.get(i);
    }

    @Override
    public int getCount() {
        return parades.size();
    }
}