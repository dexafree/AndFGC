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

public class VerParadasExpandidasAdapter extends BaseExpandableListAdapter {

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
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.boton_ver_paradas, null);
            holder = new ViewHolder();
            holder.layout = (LinearLayout)convertView.findViewById(R.id.routeoptions_button);
            holder.nombreParada = (TextView)convertView.findViewById(R.id.ver_paradas_text);
            holder.icono = (ImageView)convertView.findViewById(R.id.icono_tren);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nombreParada.setClickable(false);
        holder.icono.setClickable(false);
        holder.layout.setClickable(false);
        holder.nombreParada.setText(mContext.getString(R.string.ver_paradas));





        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int size = 0;
        for(int i=0;i<transbords.size();i++){
            size += transbords.get(i).getParades().size();
        }
        return size;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}