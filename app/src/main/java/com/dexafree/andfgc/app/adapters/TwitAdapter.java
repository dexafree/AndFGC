package com.dexafree.andfgc.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.beans.Twit;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class TwitAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<Twit> mTweetList;


    public TwitAdapter(Context context, ArrayList<Twit> tweetList){
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        mTweetList = tweetList;

    }


    static class ViewHolder{
    	TextView account;
        TextView time;
        TextView message;
        ImageView image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup){
    	ViewHolder holder;

    	if(convertView == null){
    		holder = new ViewHolder();
    		convertView = inflater.inflate(R.layout.twit_layout, null);
    		bindViews(holder, convertView);
    		convertView.setTag(holder);

    	} else {
    		holder = (ViewHolder) convertView.getTag();

        }

    	setViewsContent(holder, position);

    	return convertView;
    }

    private void bindViews(ViewHolder holder, View convertView){
        holder.account = (TextView)convertView.findViewById(R.id.account_name);
        holder.time = (TextView)convertView.findViewById(R.id.time);
        holder.message = (TextView)convertView.findViewById(R.id.message);
        holder.image = (ImageView)convertView.findViewById(R.id.account_image);
    }

    private void setViewsContent(ViewHolder holder, int position){
        holder.account.setText(mTweetList.get(position).getAccount());
        holder.time.setText(mContext.getString(R.string.time_ago)+" "+mTweetList.get(position).getTime());
        holder.message.setText(mTweetList.get(position).getMessage());

        String url = mTweetList.get(position).getAvatarUrl().replace("normal", "bigger");


        //If the author of the twit is FGC, it will show the stored logo (network saving)
        if(!mTweetList.get(position).getAlt().equalsIgnoreCase("FGC")) displayImage(url, holder.image);
        else holder.image.setImageResource(R.drawable.fgclogo);
    }

    /*
     * Loads the image asynchronously and shows it at the imageview once downloaded
     */
    private void displayImage(String url, ImageView image){
        Ion.with(image)
                .placeholder(R.drawable.fgclogo)
                .error(R.drawable.fgclogo)
                .load(url);
    }

    @Override
    public int getCount() {
    	return mTweetList.size();
    }

    @Override
    public Object getItem(int i){
    	return mTweetList.get(i);
    }

    @Override
    public long getItemId(int id){
    	return 0;
    }
}
