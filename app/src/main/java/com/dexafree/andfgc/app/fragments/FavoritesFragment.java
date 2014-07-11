package com.dexafree.andfgc.app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dexafree.andfgc.app.MainActivity;
import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.adapters.FavoritosAdapter;
import com.dexafree.andfgc.app.beans.Favorito;
import com.dexafree.andfgc.app.controllers.FavoritosController;
import com.dexafree.andfgc.app.events.BusProvider;
import com.dexafree.andfgc.app.events.FavoriteDeletedEvent;
import com.dexafree.andfgc.app.utils.Logger;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

/**
 * Created by Carlos on 02/07/14.
 */
public class FavoritesFragment extends Fragment {

    private Context mContext;

    private ListView mListView;
    private RelativeLayout emptyView;

    private ArrayList<Favorito> favoritosList;

   @Subscribe
   public void onFavoriteDeleted(FavoriteDeletedEvent event){

       Toast.makeText(mContext, R.string.favorite_deleted, Toast.LENGTH_SHORT).show();

       favoritosList = FavoritosController.getAllFavoritos(mContext);

       FavoritosAdapter adapter = new FavoritosAdapter(mContext, favoritosList);

       mListView.setAdapter(adapter);


   }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.listview_with_emptyview, null);


        mContext = getActivity();

        bindViews(v);
        if(savedInstanceState == null){
            loadFavoritos();
        } else {
            loadSaved(savedInstanceState);
        }

        setView();



        return v;
    }

    private void bindViews(View v){
        mListView = (ListView) v.findViewById(R.id.listView);

        mListView.setDivider(null);
        mListView.setDividerHeight(5);

        emptyView = (RelativeLayout)v.findViewById(R.id.empty);

    }

    private void loadFavoritos(){
        favoritosList = FavoritosController.getAllFavoritos(mContext);
    }

    private void loadSaved(Bundle savedState){
        favoritosList = savedState.getParcelableArrayList("FAVORITOS");
    }

    private void setView(){

        if(favoritosList.size() > 0){
            emptyView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            FavoritosAdapter adapter = new FavoritosAdapter(mContext, favoritosList);

            mListView.setAdapter(adapter);



            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Fragment f = new SearchFragment();
                    Bundle args = new Bundle();
                    args.putBoolean(SearchFragment.FROM_FAVORITO, true);
                    args.putParcelable("FAVORITO", favoritosList.get(i));
                    f.setArguments(args);

                    ((MainActivity)mContext).changeFragment(f, 0);

                }
            });

        } else {
            mListView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);

        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("FAVORITOS", favoritosList);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }
}
