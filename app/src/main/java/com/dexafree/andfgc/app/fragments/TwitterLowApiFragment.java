package com.dexafree.andfgc.app.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.adapters.TwitAdapter;
import com.dexafree.andfgc.app.beans.Twit;
import com.dexafree.andfgc.app.controllers.TwitController;
import com.dexafree.andfgc.app.events.BusProvider;
import com.dexafree.andfgc.app.events.ErrorEvent;
import com.dexafree.andfgc.app.events.TweetListLoadedEvent;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;


public class TwitterLowApiFragment extends Fragment {

    public static final String TWEET_LIST = "TWEET_LIST";

    private Context mContext;

    private ListView mListView;
    private ProgressDialog dialog;

    private ArrayList<Twit> mTweetList;


    @Subscribe
    public void onTweetListLoaded(TweetListLoadedEvent event){
        mTweetList = event.getTwitList();
        dialog.dismiss();
        setContent();
    }

    @Subscribe
    public void onErrorOccured(ErrorEvent event){
        Toast.makeText(mContext, R.string.error, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sample_listview, null);

        mContext = getActivity();

        bindViews(v);

        if(savedInstanceState == null){
            dialog = new ProgressDialog(mContext);
            dialog.setTitle(R.string.please_wait);
            dialog.show();
            TwitController.getLastTweets(mContext);
        } else {
            loadSavedState(savedInstanceState);
            setContent();
        }

        return v;
    }

    private void bindViews(View v){
        mListView = (ListView)v.findViewById(R.id.listView);

    }

    private void loadSavedState(Bundle savedState){
        mTweetList = savedState.getParcelableArrayList(TWEET_LIST);
    }

    private void setContent(){

        TwitAdapter adapter = new TwitAdapter(mContext, mTweetList);

        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mTweetList.get(i).getLink()));
                startActivity(browserIntent);
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){

        inflater.inflate(R.menu.twitter_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_refresh:
                dialog = new ProgressDialog(mContext);
                dialog.setTitle(R.string.please_wait);
                dialog.show();
                TwitController.getLastTweets(mContext);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(TWEET_LIST, mTweetList);
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
