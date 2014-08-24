package com.dexafree.andfgc.app.fragments;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.adapters.TimetablesAdapter;
import com.dexafree.andfgc.app.beans.Timetable;
import com.dexafree.andfgc.app.controllers.TimetablesController;
import com.dexafree.andfgc.app.events.BusProvider;
import com.dexafree.andfgc.app.events.DownloadFinishedEvent;
import com.dexafree.andfgc.app.events.TimetablesLoadedEvent;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Carlos on 26/05/2014.
 */
public class DownloadTimetablesFragment extends Fragment {

    private final static String TIMETABLES_NAME = "TIMETABLES";

    private Context mContext;
    private ProgressDialog dialog;
    private ArrayList<Timetable> mTimetables;
    private ListView timetableList;

    @Subscribe
    public void onDownloadFinished(DownloadFinishedEvent event){
        Toast.makeText(mContext, getString(R.string.timetable_downloaded)+event.getFilename(), Toast.LENGTH_SHORT).show();

        TimetablesController.newTimetableDownloaded(event.getTimetable());
        refreshListView();

        File file = event.getFile();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e){
            Toast.makeText(mContext, R.string.activity_pdf_not_found, Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void onTimetablesLoaded(TimetablesLoadedEvent event){
        mTimetables = event.getTimetablesList();
        dialog.dismiss();
        setTimetables();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sample_listview, null);
        this.mContext = getActivity();
        bindViews(v);

        if(savedInstanceState == null){
            dialog = new ProgressDialog(mContext);
            //INICIALIZAMOS VALORES DIALOG
            dialog.setTitle(R.string.loading_timetables);
            dialog.setMessage(getString(R.string.please_wait));
            dialog.show();
            TimetablesController.loadTables(mContext);
        } else {
            loadValues(savedInstanceState);
            setTimetables();
        }

        return v;
    }

    private void loadValues(Bundle savedState){
        mTimetables = savedState.getParcelableArrayList(TIMETABLES_NAME);
    }

    private void bindViews(View v){
        timetableList = (ListView)v.findViewById(R.id.listView);
    }

    private void refreshListView(){
        timetableList.setAdapter(new TimetablesAdapter(mContext, mTimetables));
    }

    private void setTimetables(){
        TimetablesAdapter adapter = new TimetablesAdapter(mContext, mTimetables);
        timetableList.setAdapter(adapter);
        timetableList.setDivider(null);
        timetableList.setDividerHeight(10);
        timetableList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(TimetablesController.isTimetableDownloaded(mTimetables.get(i))){
                    Intent intent = TimetablesController.getIntentFromTimetable(mTimetables.get(i));
                    try{
                        startActivity(intent);
                    } catch (ActivityNotFoundException e){
                        Toast.makeText(mContext, R.string.activity_pdf_not_found, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    TimetablesController.downloadTimetable(mTimetables.get(i), mContext);
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(TIMETABLES_NAME, mTimetables);
    }
}
