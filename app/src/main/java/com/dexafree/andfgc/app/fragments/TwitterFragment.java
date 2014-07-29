package com.dexafree.andfgc.app.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.adapters.TwitAdapter;
import com.dexafree.andfgc.app.beans.Twit;
import com.dexafree.andfgc.app.controllers.TwitController;
import com.dexafree.andfgc.app.events.BusProvider;
import com.dexafree.andfgc.app.events.ErrorEvent;
import com.dexafree.andfgc.app.events.TweetListLoadedEvent;
import com.dexafree.andfgc.app.utils.Logger;
import com.quentindommerc.superlistview.SuperListview;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;


public class TwitterFragment extends Fragment {

    public static final String TWEET_LIST = "TWEET_LIST";

    private Context mContext;
    private View mView;

    private SuperListview mListView;
    private ProgressDialog dialog;

    private ArrayList<Twit> mTweetList;


    @Subscribe
    public void onTweetListLoaded(TweetListLoadedEvent event){
        mTweetList = event.getTwitList();
        if(dialog.isShowing()) dialog.dismiss();
        setContent();
    }

    @Subscribe
    public void onErrorOccured(ErrorEvent event){
        Toast.makeText(mContext, R.string.error, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sample_refreshlistview, null);
        mView = v;

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
        mListView = (SuperListview)v.findViewById(R.id.listView);


        mListView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mTweetList.clear();
                TwitController.getLastTweets(mContext);
            }
        });

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
