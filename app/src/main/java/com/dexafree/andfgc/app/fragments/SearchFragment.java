package com.dexafree.andfgc.app.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.*;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.beans.Parada;
import com.dexafree.andfgc.app.connections.BuscaHoraris;
import com.dexafree.andfgc.app.utils.Logger;
import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.doomonafireball.betterpickers.datepicker.DatePickerBuilder;
import com.doomonafireball.betterpickers.datepicker.DatePickerDialogFragment;
import com.doomonafireball.betterpickers.timepicker.TimePickerBuilder;
import com.doomonafireball.betterpickers.timepicker.TimePickerDialogFragment;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Carlos on 28/04/2014.
 */
public class SearchFragment extends Fragment {

    public static final String DEPARTURE_SELECTED_STRING = "COM.DEXAFREE.ANDFGC.DEPARTURE_STATION_SELECTED";
    public static final String ARRIVAL_SELECTED_STRING = "COM.DEXAFREE.ANDFGC.ARRIVAL_STATION_SELECTED";
    public static final String LINE_SELECTED_STRING = "COM.DEXAFREE.ANDFGC.LINE_SELECTED";
    public static final String NOMBRE_PARADA = "NOMBRE_PARADA";
    public static final String NUMERO_LINEA = "NUMERO_LINEA";

    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";

    private BroadcastReceiver departureStationSelected;
    private BroadcastReceiver arrivalStationSelected;
    private BroadcastReceiver lineSelectedReceiver;

    private AlertDialog dialog;

    private TextView departureStation;
    private TextView arrivalStation;
    private TextView departureHour;
    private TextView buscarText;
    private TextView lineSelect;
    private CheckBox checkBox;
    private Context mContext;

    private boolean isHourSelected = false;
    private boolean isDepartureStationSelected = false;
    private boolean isArrivalStationSelected = false;

    private boolean isTimeDeparture = true;

    private BuscaHoraris buscaHoraris;

    private String fecha;
    private String hora;
    private String minutos;
    private String fechaHora;

    private String origen;
    private String desti;

    private int linia;
    private int horaInt;
    private int minutosInt;

    private MyDateSetHandler dsh;

    private CalendarDatePickerDialog dpb;
    private TimePickerBuilder tpb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.directions_input_panel, null);
        setup(v);

        mContext.registerReceiver(departureStationSelected, new IntentFilter(DEPARTURE_SELECTED_STRING));
        mContext.registerReceiver(arrivalStationSelected, new IntentFilter(ARRIVAL_SELECTED_STRING));
        mContext.registerReceiver(lineSelectedReceiver, new IntentFilter(LINE_SELECTED_STRING));

        lineSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLineDialog();
            }
        });

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
        lineSelect = (TextView)v.findViewById(R.id.selecciona_linea);
        buscarText = (TextView)v.findViewById(R.id.routeoptions_textbox);
        checkBox = (CheckBox)v.findViewById(R.id.salida_llegada_checkbox);

        lineSelectedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                linia = intent.getExtras().getInt(NUMERO_LINEA);
                enableText(departureStation);
                enableText(arrivalStation);
            }
        };

        departureStationSelected = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                departureStation.setText(intent.getExtras().getString(NOMBRE_PARADA));
                isDepartureStationSelected = true;
                enableSearchButton();
            }
        };

        arrivalStationSelected = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                arrivalStation.setText(intent.getExtras().getString(NOMBRE_PARADA));
                isArrivalStationSelected = true;
                enableSearchButton();
            }
        };

        buscarText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscaHoraris.cercar();
                getChildFragmentManager().beginTransaction().replace(R.id.directions_input_panel, new SearchResultFragment()).commit();
            }
        });


        DateTime now = DateTime.now();

        dsh = new MyDateSetHandler();

        dpb = new CalendarDatePickerDialog()
                .newInstance(dsh, now.getYear(), now.getMonthOfYear() -1, now.getDayOfMonth());

        tpb = new TimePickerBuilder()
                .setFragmentManager(getChildFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                .addTimePickerDialogHandler(new MyHourSetHandler());
    }

    private void enableText(TextView tv){
        tv.setTextColor(mContext.getResources().getColor(R.color.secondary_grey));
        tv.setClickable(true);
    }

    private void enableSearchButton(){
        if(isArrivalStationSelected && isDepartureStationSelected && isHourSelected){
            enableText(buscarText);
            String sortidaArribada;
            if(checkBox.isChecked()) sortidaArribada = "S";
            else sortidaArribada = "A";
            buscaHoraris = new BuscaHoraris(linia, origen, desti, sortidaArribada, fecha, horaInt, minutosInt, mContext);
        }
    }

    private void showLineDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.selecciona_linia);
        final String[] linies = new String[]{"1", "2", "3"};
        builder.setItems(linies, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String linia = linies[i];
                mContext.sendBroadcast(new Intent().setAction(LINE_SELECTED_STRING).putExtra(NUMERO_LINEA, Integer.parseInt(linia)));
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    private void showStationDialog(final String broadcast){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.selecciona_parada);
        final String[] parades = Parada.getParadesFromLiniaAsStringArray(mContext, linia);
        final ArrayList<Parada> paradesArrayList = Parada.getParadesFromLiniaAsArrayList(mContext, linia);
        builder.setItems(parades, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String parada = parades[i];
                mContext.sendBroadcast(new Intent().setAction(broadcast).putExtra(NOMBRE_PARADA, parada));
                String abreviatura = paradesArrayList.get(i).getAbreviatura();
                if(broadcast.equalsIgnoreCase(DEPARTURE_SELECTED_STRING)) origen = abreviatura;
                else if(broadcast.equalsIgnoreCase(ARRIVAL_SELECTED_STRING)) desti = abreviatura;
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    private class MyDateSetHandler implements CalendarDatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(CalendarDatePickerDialog dialog, int year, int month, int day){
            String monthString;
            String dayString;
            if((month + 1) < 10) monthString = "0" + (month + 1);
            else monthString = (month + 1)+"";

            if(day < 10) dayString = "0"+day;
            else dayString = day+"";
            fecha = dayString+"/"+monthString+"/"+year;
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

            horaInt = hours;
            minutosInt = minutes;

            fechaHora = fecha + " " + hora + ":" + minutos;
            departureHour.setText(fechaHora);

            isHourSelected = true;
            enableSearchButton();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(departureStationSelected);
        mContext.unregisterReceiver(arrivalStationSelected);
        mContext.unregisterReceiver(lineSelectedReceiver);

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
