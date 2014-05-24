package com.dexafree.andfgc.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.dexafree.andfgc.app.R;

import java.util.ArrayList;

/**
 * Created by Carlos on 18/05/2014.
 */
public class ServiceStatusAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;

    private ArrayList<String> status;
    private ArrayList<String> dates;



    static class ViewHolder{
    	TextView date;
        TextView message;
    }

    public ServiceStatusAdapter(Context c, ArrayList<String> status, ArrayList<String> dates){
        this.mContext = c;
        this.inflater = LayoutInflater.from(c);
        this.status = status;
        this.dates = dates;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup){
    	ViewHolder holder;
    	
    	if(convertView == null){
    		holder = new ViewHolder();
    		convertView = inflater.inflate(R.layout.status_item, null);
    		bindViews(holder, convertView);
    		convertView.setTag(holder);
    	} else {
    		holder = (ViewHolder) convertView.getTag(); 
    	}
    	
    	setViewsContent(holder, position);
    	
    	return convertView;
    }
    
    private void bindViews(ViewHolder holder, View convertView){
    	holder.date = (TextView)convertView.findViewById(R.id.status_date);
        holder.message = (TextView)convertView.findViewById(R.id.status_message);
    }
    
    private void setViewsContent(ViewHolder holder, int position){
        holder.date.setText(dates.get(position));
        holder.message.setText(status.get(position));
    }
    
    @Override
    public int getCount() {
    	return status.size();
    }
    
    @Override
    public Object getItem(int i){
    	return status.get(i);
    }
    
    @Override
    public long getItemId(int i){
    	return 0;
    }
}
