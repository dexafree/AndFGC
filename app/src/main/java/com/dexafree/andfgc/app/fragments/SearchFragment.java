package com.dexafree.andfgc.app.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import com.dexafree.andfgc.app.utils.Logger;
import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.doomonafireball.betterpickers.datepicker.DatePickerBuilder;
import com.doomonafireball.betterpickers.datepicker.DatePickerDialogFragment;
import com.doomonafireball.betterpickers.timepicker.TimePickerBuilder;
import com.doomonafireball.betterpickers.timepicker.TimePickerDialogFragment;
import org.joda.time.DateTime;

import java.util.Calendar;

/**
 * Created by Carlos on 28/04/2014.
 */
public class SearchFragment extends Fragment {

    public static final String DEPARTURE_SELECTED_STRING = "COM.DEXAFREE.ANDFGC.DEPARTURE_STATION_SELECTED";
    public static final String ARRIVAL_SELECTED_STRING = "COM.DEXAFREE.ANDFGC.ARRIVAL_STATION_SELECTED";
    public static final String NOMBRE_PARADA = "NOMBRE_PARADA";

    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";

    private BroadcastReceiver departureStationSelected;
    private BroadcastReceiver arrivalStationSelected;

    private AlertDialog dialog;

    private TextView departureStation;
    private TextView arrivalStation;

    private TextView departureHour;
    private Context mContext;

    private String fecha;
    private String hora;
    private String minutos;
    private String fechaHora;

    private MyDateSetHandler dsh;

    private CalendarDatePickerDialog dpb;
    private TimePickerBuilder tpb;

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

        departureHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dpb.show(getChildFragmentManager(), FRAG_TAG_DATE_PICKER );
            }
        });

        return v;
    }

    private void setup(View v){
        mContext = getActivity();
        departureStation = (TextView)v.findViewById(R.id.directions_startpoint_textbox);
        arrivalStation = (TextView)v.findViewById(R.id.directions_endpoint_textbox);
        departureHour = (TextView)v.findViewById(R.id.departure_hour);

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


        DateTime now = DateTime.now();

        dsh = new MyDateSetHandler();

        dpb = new CalendarDatePickerDialog()
                .newInstance(dsh, now.getYear(), now.getMonthOfYear() -1, now.getDayOfMonth());

        tpb = new TimePickerBuilder()
                .setFragmentManager(getChildFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                .addTimePickerDialogHandler(new MyHourSetHandler());

    }

    private void showStationDialog(final String broadcast){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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

    private class MyDateSetHandler implements CalendarDatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(CalendarDatePickerDialog dialog, int year, int month, int day){
            Logger.d("year", year+"");
            Logger.d("month", month+"");
            Logger.d("day", day+"");
            fecha = day+"/"+month+"/"+year;
            showTimeDialog();
        }
    }

    private void showTimeDialog(){
        tpb.show();
    }

    private class MyHourSetHandler implements TimePickerDialogFragment.TimePickerDialogHandler {
        @Override
        public void onDialogTimeSet(int cosa, int hours, int minutes){
            hora = hours+"";
            if(minutes < 10) minutos = "0"+minutes;
            else minutos = minutes+"";

            fechaHora = fecha + " " + hora + ":" + minutos;
            departureHour.setText(fechaHora);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(departureStationSelected);
        mContext.unregisterReceiver(arrivalStationSelected);
    }

    @Override
    public void onResume() {
        // Example of reattaching to the fragment
        super.onResume();
        CalendarDatePickerDialog calendarDatePickerDialog = (CalendarDatePickerDialog) getChildFragmentManager()
                .findFragmentByTag(FRAG_TAG_DATE_PICKER);
        if (calendarDatePickerDialog != null) {
            if(dsh == null) dsh = new MyDateSetHandler();
            calendarDatePickerDialog.setOnDateSetListener(dsh);
        }
    }
}
