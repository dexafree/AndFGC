package com.dexafree.andfgc.app.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.*;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.dexafree.andfgc.app.MainActivity;
import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.beans.Cerca;
import com.dexafree.andfgc.app.beans.Parada;
import com.dexafree.andfgc.app.connections.BuscaHoraris;
import com.dexafree.andfgc.app.controllers.ParadaController;
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
    public static final String NOMBRE_LINEA = "NOMBRE_LINEA";

    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";

    private BroadcastReceiver departureStationSelected;
    private BroadcastReceiver arrivalStationSelected;
    private BroadcastReceiver lineSelectedReceiver;
    private BroadcastReceiver searchFinished;

    private AlertDialog dialog;
    private ProgressDialog pDialog;

    private TextView departureStation;
    private TextView arrivalStation;
    private TextView departureHour;
    private TextView buscarText;
    private TextView lineSelect;
    private CheckBox checkBox;

    private Context mContext;
    private MainActivity mainActivity;

    private boolean isHourSelected = false;
    private boolean isDepartureStationSelected = false;
    private boolean isArrivalStationSelected = false;

    private boolean isTimeArrival = false;

    private BuscaHoraris buscaHoraris;

    private String fecha;
    private String hora;
    private String minutos;
    private String fechaHora;

    private String origen;
    private String desti;

    private int linia = -1;
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
        mContext.registerReceiver(searchFinished, new IntentFilter(BuscaHoraris.SEARCH_COMPLETED));

        lineSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLineDialog();
            }
        });

        departureStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(linia == -1) Toast.makeText(mContext, R.string.select_line_first, Toast.LENGTH_SHORT).show();
                else showStationDialog(DEPARTURE_SELECTED_STRING);
            }
        });

        arrivalStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(linia == -1) Toast.makeText(mContext, R.string.select_line_first, Toast.LENGTH_SHORT).show();
                else showStationDialog(ARRIVAL_SELECTED_STRING);
            }
        });

        departureHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dpb.show(getChildFragmentManager(), FRAG_TAG_DATE_PICKER );
            }
        });

        buscarText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isArrivalStationSelected && isDepartureStationSelected && isHourSelected) {
                    pDialog = new ProgressDialog(mContext);
                    pDialog.setMessage(getString(R.string.searching_message));
                    pDialog.setTitle(getString(R.string.searching_title));
                    pDialog.show();
                    String sortidaArribada;
                    if(isTimeArrival) sortidaArribada = "A";
                    else sortidaArribada = "S";
                    buscaHoraris = new BuscaHoraris(linia, origen, desti, sortidaArribada, fecha, horaInt, minutosInt, mContext);
                    buscaHoraris.cercar();
                } else {
                    Toast.makeText(mContext, R.string.select_all, Toast.LENGTH_SHORT).show();
                }
            }
        });

        buscarText.setText(getString(R.string.search));

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isTimeArrival = b;
            }
        });

        return v;
    }

    private void setup(View v){
        mContext = getActivity();
        mainActivity = (MainActivity) getActivity();
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
                String nombreLinea = intent.getExtras().getString(NOMBRE_LINEA);
                enableText(departureStation);
                enableText(arrivalStation);
                lineSelect.setText(nombreLinea);
            }
        };

        departureStationSelected = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                departureStation.setText(intent.getExtras().getString(NOMBRE_PARADA));
                isDepartureStationSelected = true;
            }
        };

        arrivalStationSelected = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                arrivalStation.setText(intent.getExtras().getString(NOMBRE_PARADA));
                isArrivalStationSelected = true;
            }
        };

        searchFinished = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try{
                    pDialog.dismiss();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                Cerca c = intent.getExtras().getParcelable("CERCA");
                Logger.d("FECHA", fecha);
                mainActivity.showSearchResults(c, fecha);

            }
        };

        DateTime now = DateTime.now();

        dsh = new MyDateSetHandler();

        dpb = CalendarDatePickerDialog
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

    private void showLineDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.selecciona_linia);
        final String[] linies = new String[]{
                "Barcelona - Vallès",
                "Llobregat - Anoia",
                "Lleida - La Pobla de Segur"};
        builder.setItems(linies, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction(LINE_SELECTED_STRING);
                intent.putExtra(NUMERO_LINEA, i+1);
                intent.putExtra(NOMBRE_LINEA, linies[i]);
                mContext.sendBroadcast(intent);
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    private void showStationDialog(final String broadcast){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.selecciona_parada);
        final String[] parades = ParadaController.getParadesFromLiniaAsStringArray(mContext, linia);
        final ArrayList<Parada> paradesArrayList = ParadaController.getParadesFromLiniaAsArrayList(mContext, linia);
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
        super.onResume();
        CalendarDatePickerDialog calendarDatePickerDialog = (CalendarDatePickerDialog) getChildFragmentManager()
                .findFragmentByTag(FRAG_TAG_DATE_PICKER);
        if (calendarDatePickerDialog != null) {
            if(dsh == null) dsh = new MyDateSetHandler();
            calendarDatePickerDialog.setOnDateSetListener(dsh);
        }
    }
}
