package com.dexafree.andfgc.app.fragments;

import android.app.AlertDialog;
import android.content.*;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.beans.Parada;

/**
 * Created by Carlos on 28/04/2014.
 */
public class SearchFragment extends Fragment {

    public static final String DEPARTURE_SELECTED_STRING = "COM.DEXAFREE.ANDFGC.DEPARTURE_STATION_SELECTED";
    public static final String ARRIVAL_SELECTED_STRING = "COM.DEXAFREE.ANDFGC.ARRIVAL_STATION_SELECTED";
    public static final String NOMBRE_PARADA = "NOMBRE_PARADA";

    private BroadcastReceiver departureStationSelected;
    private BroadcastReceiver arrivalStationSelected;
    private BroadcastReceiver departureHourSelected;

    private AlertDialog dialog;

    private TextView departureStation;
    private TextView arrivalStation;

    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.directions_input_panel, null);
        setup(v);

        mContext.registerReceiver(departureStationSelected, new IntentFilter(DEPARTURE_SELECTED_STRING));
        mContext.registerReceiver(arrivalStationSelected, new IntentFilter(ARRIVAL_SELECTED_STRING));

        departureStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStationDialog(DEPARTURE_SELECTED_STRING);
            }
        });

        arrivalStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStationDialog(ARRIVAL_SELECTED_STRING);
            }
        });

        return v;
    }

    private void setup(View v){
        mContext = getActivity();
        departureStation = (TextView)v.findViewById(R.id.directions_startpoint_textbox);
        arrivalStation = (TextView)v.findViewById(R.id.directions_endpoint_textbox);

        departureStationSelected = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                departureStation.setText(intent.getExtras().getString(NOMBRE_PARADA));
            }
        };

        arrivalStationSelected = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                arrivalStation.setText(intent.getExtras().getString(NOMBRE_PARADA));
            }
        };
    }

    private void showStationDialog(final String broadcast){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        /*LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.sample_listview, null);
        ListView paradesListView = (ListView)layout.findViewById(R.id.listView);

        final String[] parades = Parada.getParadesFromLiniaAsStringArray(mContext, 1);

        ArrayAdapter<String> adapter =
            new ArrayAdapter<String>(
                mContext,
                android.R.layout.simple_list_item_1,
                parades
            );


        paradesListView.setAdapter(adapter);

        paradesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String parada = parades[i];
                mContext.sendBroadcast(new Intent().setAction(broadcast).putExtra(NOMBRE_PARADA, parada));
                dialog.dismiss();
            }
        });

        builder.setView(layout);
        builder.setMessage(R.string.selecciona_parada);

        dialog = builder.create();
        dialog.requestWindowFeature(android.R.id.title);


        dialog.show();

        View title = dialog.findViewById(android.R.id.title);
        title.setMinimumHeight(50);*/
        builder.setTitle(R.string.selecciona_parada);
        final String[] parades = Parada.getParadesFromLiniaAsStringArray(mContext, 1);
        builder.setItems(parades, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String parada = parades[i];
                mContext.sendBroadcast(new Intent().setAction(broadcast).putExtra(NOMBRE_PARADA, parada));
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(departureStationSelected);
        mContext.unregisterReceiver(arrivalStationSelected);
    }
}
