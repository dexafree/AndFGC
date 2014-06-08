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
import com.dexafree.andfgc.app.events.BusProvider;
import com.dexafree.andfgc.app.events.SearchFinishedEvent;
import com.dexafree.andfgc.app.utils.Logger;
import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.doomonafireball.betterpickers.datepicker.DatePickerBuilder;
import com.doomonafireball.betterpickers.datepicker.DatePickerDialogFragment;
import com.doomonafireball.betterpickers.timepicker.TimePickerBuilder;
import com.doomonafireball.betterpickers.timepicker.TimePickerDialogFragment;
import com.squareup.otto.Subscribe;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Carlos on 28/04/2014.
 */
public class SearchFragment extends Fragment {

    public static final String FROM_MAP = "FROM_MAP";
    public static final String FROM_HERE = "FROM_HERE";

    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";

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

    private final String[] linies = new String[]{
            "Barcelona - Vallès",
            "Llobregat - Anoia",
            "Lleida - La Pobla de Segur"};

    private int linia = -1;
    private int horaInt;
    private int minutosInt;

    private MyDateSetHandler dsh;

    private CalendarDatePickerDialog dpb;
    private TimePickerBuilder tpb;

    @Subscribe
    public void onSearchFinished(SearchFinishedEvent event){
        pDialog.dismiss();
        mainActivity.showSearchResults(event.getCerca(), fecha);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.directions_input_panel, null);
        setup(v);

        Bundle args = getArguments();

        if(args != null) {
            if (args.getBoolean(FROM_MAP)) {
                Parada p = args.getParcelable("PARADA");
                int lineTemp = Integer.parseInt(p.getLinia());
                lineSelected(lineTemp, linies[lineTemp]);

                if (args.getBoolean(FROM_HERE)) {
                    origen = p.getAbreviatura();
                    isDepartureStationSelected = true;
                } else {
                    desti = p.getAbreviatura();
                    isArrivalStationSelected = true;
                }

                setValues();
            }
        }

        if(savedInstanceState != null){
            loadValues(savedInstanceState);
            setValues();
        }

        setListeners();

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

        DateTime now = DateTime.now();

        dsh = new MyDateSetHandler();

        dpb = CalendarDatePickerDialog
                .newInstance(dsh, now.getYear(), now.getMonthOfYear() -1, now.getDayOfMonth());

        tpb = new TimePickerBuilder()
                .setFragmentManager(getChildFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                .addTimePickerDialogHandler(new MyHourSetHandler());
    }

    private void loadValues(Bundle savedState){
        isTimeArrival = savedState.getBoolean("isTimeArrival");
        isDepartureStationSelected = savedState.getBoolean("isDepartureStationSelected");
        isArrivalStationSelected = savedState.getBoolean("isArrivalStationSelected");
        isHourSelected = savedState.getBoolean("isHourSelected");


        fecha = savedState.getString("fecha");
        fechaHora = savedState.getString("fechaHora");
        minutos = savedState.getString("minutos");
        fechaHora = savedState.getString("fechaHora");
        origen = savedState.getString("origen");
        desti = savedState.getString("desti");

        linia = savedState.getInt("linia");
        horaInt = savedState.getInt("horaInt");
        minutosInt = savedState.getInt("minutosInt");
    }

    private void setValues(){
        checkBox.setChecked(isTimeArrival);
        if(isDepartureStationSelected)
            departureStation.setText(ParadaController.getParadaFromAbreviatura(mContext,origen).getNom());

        if(isArrivalStationSelected)
            arrivalStation.setText(ParadaController.getParadaFromAbreviatura(mContext,desti).getNom());

        if(isHourSelected)
            departureHour.setText(fechaHora);

        if(linia != -1){
            lineSelect.setText(linies[linia-1]);
            enableText(departureStation);
            enableText(arrivalStation);
        }

        enableSearchButton();

    }

    private void setListeners(){

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
                else showStationDialog(false);
            }
        });

        arrivalStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(linia == -1) Toast.makeText(mContext, R.string.select_line_first, Toast.LENGTH_SHORT).show();
                else showStationDialog(true);
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

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isTimeArrival = b;
            }
        });
    }

    private void enableText(TextView tv){
        tv.setTextColor(mContext.getResources().getColor(R.color.primary_grey));
        tv.setClickable(true);
    }

    private void showLineDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.selecciona_linia);

        builder.setItems(linies, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                lineSelected(i+1, linies[i]);
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    private void lineSelected(int numLinia, String nomLinia){
        linia = numLinia;
        enableText(departureStation);
        enableText(arrivalStation);
        lineSelect.setText(nomLinia);
        isDepartureStationSelected = false;
        isArrivalStationSelected = false;
        departureStation.setText(R.string.start_station);
        arrivalStation.setText(R.string.end_station);
        origen = null;
        desti = null;
    }

    private void stationSelected(boolean isArrival, String nomParada, String abreviatura){
        if(isArrival){
            desti = abreviatura;
            arrivalStation.setText(nomParada);
            isArrivalStationSelected = true;
            enableText(arrivalStation);

        } else {
            origen = abreviatura;
            departureStation.setText(nomParada);
            isDepartureStationSelected = true;
            enableText(departureStation);
        }

        enableSearchButton();
    }

    private void enableSearchButton(){
        if(isHourSelected && isDepartureStationSelected && isArrivalStationSelected){
            buscarText.setTextColor(mContext.getResources().getColor(R.color.primary_grey));
        }
    }

    private void showStationDialog(final boolean isArrival){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.selecciona_parada);
        //TODO: INTENTAR OPTIMIZAR TENER ESTAS DOS VARIABLES
        final String[] parades = ParadaController.getParadesFromLiniaAsStringArray(mContext, linia);
        final ArrayList<Parada> paradesArrayList = ParadaController.getParadesFromLiniaAsArrayList(mContext, linia);
        builder.setItems(parades, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String parada = parades[i];
                String abreviatura = paradesArrayList.get(i).getAbreviatura();
                stationSelected(isArrival, parada, abreviatura);
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    private void showTimeDialog(){
        tpb.show();
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
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
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
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isTimeArrival", isTimeArrival);
        outState.putBoolean("isDepartureStationSelected", isDepartureStationSelected);
        outState.putBoolean("isArrivalStationSelected", isArrivalStationSelected);
        outState.putBoolean("isHourSelected", isHourSelected);

        outState.putString("fecha", fecha);
        outState.putString("fechaHora", fechaHora);
        outState.putString("minutos", minutos);
        outState.putString("fechaHora", fechaHora);
        outState.putString("origen", origen);
        outState.putString("desti", desti);

        outState.putInt("linia", linia);
        outState.putInt("horaInt", horaInt);
        outState.putInt("minutosInt", minutosInt);

    }
}
