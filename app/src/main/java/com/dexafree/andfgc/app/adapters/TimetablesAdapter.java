package com.dexafree.andfgc.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.beans.Timetable;

import java.util.ArrayList;

public class TimetablesAdapter extends BaseAdapter {

    private ArrayList<Timetable> timetables;
    private Context mContext;
    private LayoutInflater inflater;

    public TimetablesAdapter(Context c, ArrayList<Timetable> timetables){
        this.mContext = c;
        this.inflater = LayoutInflater.from(c);
        this.timetables = timetables;

    }

    static class ViewHolder{
    	TextView name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup){
    	ViewHolder holder;

    	if(convertView == null){
    		holder = new ViewHolder();
    		convertView = inflater.inflate(R.layout.timetables_item, null);
    		bindViews(holder, convertView);
    		convertView.setTag(holder);
    	} else {
    		holder = (ViewHolder) convertView.getTag();
    	}

    	setViewsContent(holder, position);

    	return convertView;
    }

    private void bindViews(ViewHolder holder, View convertView){
    	holder.name = (TextView)convertView.findViewById(R.id.timetable_name);
    }

    private void setViewsContent(ViewHolder holder, int position){
        holder.name.setText(timetables.get(position).getName());
    }

    @Override
    public int getCount() {
    	return timetables.size();
    }

    @Override
    public Object getItem(int i){
    	return timetables.get(i);
    }

    @Override
    public long getItemId(int i){
    	return 0;
    }

}
