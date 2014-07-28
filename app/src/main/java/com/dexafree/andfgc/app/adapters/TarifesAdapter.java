package com.dexafree.andfgc.app.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.beans.Tarifa;
import com.dexafree.andfgc.app.utils.Logger;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TarifesAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<Tarifa> tarifesList;

    public TarifesAdapter(Context context, ArrayList<Tarifa> tarifes){
        mContext = context;
        inflater = LayoutInflater.from(context);
        tarifesList = tarifes;
    }

    static class ViewHolder{
    	private TextView concept;
        private TextView price;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup){
    	ViewHolder holder;

    	if(convertView == null){
    		holder = new ViewHolder();
    		convertView = inflater.inflate(R.layout.tarifes_item, null);
    		bindViews(holder, convertView);
    		convertView.setTag(holder);
    	} else {
    		holder = (ViewHolder) convertView.getTag();
    	}

    	setViewsContent(holder, position);

    	return convertView;
    }

    private void bindViews(ViewHolder holder, View convertView){
        holder.concept = (TextView) convertView.findViewById(R.id.concept_text);
        holder.price = (TextView) convertView.findViewById(R.id.price_text);
    }

    private void setViewsContent(ViewHolder holder, int position){
        holder.concept.setText(tarifesList.get(position).getConcepte());

        double preu = tarifesList.get(position).getPreu();

        DecimalFormat df = new DecimalFormat("#.##");
        String preuString = df.format(preu);

        holder.price.setText(preuString+" €");
    }

    @Override
    public int getCount() {
    	return tarifesList.size();
    }

    @Override
    public Object getItem(int i){
    	return tarifesList.get(i);
    }

    @Override
    public long getItemId(int id){
    	return 0;
    }

}
