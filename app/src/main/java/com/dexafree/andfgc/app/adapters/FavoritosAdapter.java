package com.dexafree.andfgc.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.beans.Favorito;
import com.dexafree.andfgc.app.controllers.FavoritosController;
import com.dexafree.andfgc.app.controllers.ParadaController;
import com.dexafree.andfgc.app.events.BusProvider;
import com.dexafree.andfgc.app.events.FavoriteDeletedEvent;

import java.util.ArrayList;

/**
 * Created by Carlos on 02/07/14.
 */
public class FavoritosAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Favorito> favoritosList;
    private LayoutInflater inflater;

    public FavoritosAdapter(Context context, ArrayList<Favorito> favoritosList){
        this.mContext = context;
        this.favoritosList = favoritosList;
        inflater = LayoutInflater.from(context);
    }

    static class ViewHolder{

        TextView title;
        TextView from;
        TextView to;
        ImageButton deleteButton;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup){
    	ViewHolder holder;

    	if(convertView == null){
    		holder = new ViewHolder();
    		convertView = inflater.inflate(R.layout.favorito_item, null);
    		bindViews(holder, convertView);
    		convertView.setTag(holder);
    	} else {
    		holder = (ViewHolder) convertView.getTag();
    	}

    	setViewsContent(holder, position);

    	return convertView;
    }

    private void bindViews(ViewHolder holder, View convertView){

        holder.from = (TextView)convertView.findViewById(R.id.start_station);
        holder.to = (TextView)convertView.findViewById(R.id.end_station);
        holder.title = (TextView)convertView.findViewById(R.id.favTitleText);
        holder.deleteButton = (ImageButton)convertView.findViewById(R.id.deleteButton);

    }

    private void setViewsContent(ViewHolder holder, final int position){

        String fromText = ParadaController.getParadaFromAbreviatura(mContext, favoritosList.get(position).getOrigen()).getNom();
        String destinationText = ParadaController.getParadaFromAbreviatura(mContext, favoritosList.get(position).getDesti()).getNom();

        holder.from.setText(fromText);
        holder.to.setText(destinationText);
        holder.title.setText(favoritosList.get(position).getTitle());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavoritosController.deleteFavoritoFromId(mContext, favoritosList.get(position).getId());
                BusProvider.getInstance().post(new FavoriteDeletedEvent());

            }
        });



    }

    @Override
    public int getCount() {
    	return favoritosList.size();
    }

    @Override
    public Object getItem(int i){
    	return favoritosList.get(i);
    }

    @Override
    public long getItemId(int id){
    	return 0;
    }
}
