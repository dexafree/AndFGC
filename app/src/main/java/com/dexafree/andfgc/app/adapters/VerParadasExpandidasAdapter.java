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
import org.w3c.dom.Text;

public class VerParadasExpandidasAdapter extends BaseExpandableListAdapter {

    private ArrayList<Parada> parades;
    private LayoutInflater inflater;
    private Parada child;
    private Context mContext;
    private static int convertViewCounter = 0;

    static class ViewHolder {
        LinearLayout layout;
        TextView nombreParada;
        ImageView icono;
    }

    public VerParadasExpandidasAdapter(Context c, ArrayList<Parada> children) {
        this.parades = children;
        this.mContext = c;
        this.inflater  = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ViewHolder holder;

        child = parades.get(childPosition);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.search_result_paradas_list_item, null);
            convertViewCounter++;

            holder.nombreParada = (TextView) convertView.findViewById(R.id.nombre_parada_expanded);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nombreParada.setText(child.getNom());

        /*convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, child.getNom(),
                        Toast.LENGTH_SHORT).show();
            }
        });*/

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
        return parades.size();
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