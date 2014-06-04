package com.dexafree.andfgc.app.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.adapters.TimetablesAdapter;
import com.dexafree.andfgc.app.beans.Timetable;
import com.dexafree.andfgc.app.connections.GetTimetables;
import com.dexafree.andfgc.app.events.BusProvider;
import com.dexafree.andfgc.app.events.DownloadFinishedEvent;
import com.dexafree.andfgc.app.utils.Logger;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

/**
 * Created by Carlos on 26/05/2014.
 */
public class DownloadTimetablesFragment extends Fragment {

    private final static String TIMETABLES_NAME = "TIMETABLES";

    private Context mContext;
    private ProgressDialog dialog;
    private ArrayList<Timetable> timetables;
    private ListView timetableList;

    @Subscribe
    public void onDownloadFinished(DownloadFinishedEvent event){
        Toast.makeText(mContext, getString(R.string.timetable_downloaded)+event.getFilename(), Toast.LENGTH_SHORT).show();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sample_listview, null);
        this.mContext = getActivity();
        bindViews(v);

        if(savedInstanceState == null){
            new LoadTimetables().execute();
        } else {
            loadValues(savedInstanceState);
            setTimetables();
        }

        return v;
    }

    private void loadValues(Bundle savedState){
        timetables = savedState.getParcelableArrayList(TIMETABLES_NAME);
    }

    private void bindViews(View v){
        timetableList = (ListView)v.findViewById(R.id.listView);
    }

    private class LoadTimetables extends AsyncTask<String, Float, Integer>{

        private ArrayList<Timetable> tables;

        protected void onPreExecute() {
            dialog = new ProgressDialog(mContext);
            //INICIALIZAMOS VALORES DIALOG
            dialog.setTitle(R.string.loading_timetables);
            dialog.setMessage(getString(R.string.please_wait));
            dialog.show(); //Mostramos el dialogo antes de comenzar
        }

        protected Integer doInBackground(String... params) {

            tables = GetTimetables.loadTables();

            return 0;
        }

        protected void onPostExecute(Integer bytes) {
            timetables = tables;
            dialog.dismiss();
            setTimetables();
        }
    }

    private void setTimetables(){
        TimetablesAdapter adapter = new TimetablesAdapter(mContext, timetables);
        timetableList.setAdapter(adapter);
        timetableList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GetTimetables.downloadTimetable(timetables.get(i), mContext);
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
        outState.putParcelableArrayList(TIMETABLES_NAME, timetables);
    }
}
