package com.dexafree.andfgc.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.beans.Opcio;

import java.util.ArrayList;

/**
 * Created by Carlos on 04/05/2014.
 */
public class AlternativesAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Opcio> opcions;
    private LayoutInflater inflater;
    private String dataBuscada;

    static class ViewHolder{
        TextView horaSortida;
        TextView horaArribada;
    }

    public AlternativesAdapter(Context c, ArrayList<Opcio> opts, String data){
        this.mContext = c;
        this.opcions = opts;
        this.inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dataBuscada = data;
    }

    @Override
    public View getView(int childPosition, View convertView, ViewGroup viewGroup) {

        ViewHolder holder;
        Opcio op = opcions.get(childPosition);
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.alternatives_list_item, null);
            holder.horaSortida = (TextView)convertView.findViewById(R.id.departure_hour);
            holder.horaArribada = (TextView)convertView.findViewById(R.id.arrival_hour);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        String[] splits = op.getHoraSortida().split(" ");
        String horaSortida = dataBuscada+" "+splits[1];
        holder.horaSortida.setText(horaSortida);
        splits = op.getHoraArribada().split(" ");
        String horaArribada = dataBuscada+" "+splits[1];
        holder.horaArribada.setText(horaArribada);

        return convertView;

    }

    @Override
    public int getCount() {
        return opcions.size();
    }

    @Override
    public Object getItem(int i) {
        return opcions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


}
