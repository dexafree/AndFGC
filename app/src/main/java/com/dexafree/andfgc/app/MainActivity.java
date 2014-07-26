package com.dexafree.andfgc.app;

import android.app.AlertDialog;
import android.content.*;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import com.dexafree.andfgc.app.beans.Cerca;
import com.dexafree.andfgc.app.databases.DataBaseHelper;
import com.dexafree.andfgc.app.fragments.AboutFragment;
import com.dexafree.andfgc.app.fragments.AlertsNewsFragment;
import com.dexafree.andfgc.app.fragments.DownloadTimetablesFragment;
import com.dexafree.andfgc.app.fragments.FavoritesFragment;
import com.dexafree.andfgc.app.fragments.SearchFragment;
import com.dexafree.andfgc.app.fragments.SearchResultFragment;
import com.dexafree.andfgc.app.fragments.ShowMapFragment;
import com.dexafree.andfgc.app.fragments.ShowTarifesFragment;
import com.dexafree.andfgc.app.fragments.TwitterFragment;
import com.dexafree.andfgc.app.fragments.TwitterLowApiFragment;
import com.dexafree.andfgc.app.fragments.WelcomeFragment;
import com.dexafree.andfgc.app.utils.Checkers;
import com.dexafree.andfgc.app.utils.Constants;
import com.dexafree.andfgc.app.utils.Logger;

import org.arasthel.googlenavdrawermenu.views.GoogleNavigationDrawer;

public class MainActivity extends ActionBarActivity {

    private final static String POSITION = "POSITION";

    private ActionBarDrawerToggle drawerToggle;
    private GoogleNavigationDrawer mDrawer;

    private int mPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        firstTime();

        /*
         * We get the GoogleNavigationDrawer object
         * in order to allow further method usage
         */
        mDrawer = (GoogleNavigationDrawer) findViewById(R.id.navigation_drawer_container);

        /*
         * We get the drawerToggle object order to
         * allow showing the NavigationDrawer icon
         */
        drawerToggle = new ActionBarDrawerToggle(this,
                mDrawer,
                R.drawable.ic_navigation_drawer,
                R.string.app_name,
                R.string.app_name);

        mDrawer.setDrawerListener(drawerToggle); //Attach the DrawerListener



        mDrawer.setOnNavigationSectionSelected(new GoogleNavigationDrawer.OnNavigationSectionSelected() {
            @Override
            public void onSectionSelected(View view, int i, long l) {
                selectOption(i);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        if(savedInstanceState == null) {

            Fragment f;

            if(showWelcome())
                f = new WelcomeFragment();
            else
                f = new SearchFragment();


            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.content_layout, f)
                    .commit();

            mPosition = -1;

        } else {

            mPosition = savedInstanceState.getInt(POSITION);

        }


    }

    public void showSearchResults(Cerca cerca, String dataBuscada){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_layout, SearchResultFragment.newInstance(cerca, dataBuscada))
                .addToBackStack(null)
                .commit();
    }

    public void changeFragment(Fragment f){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_layout, f)
                .addToBackStack(null)
                .commit();
    }

    public void changeFragmentWithoutAddingToStack(Fragment f){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_layout, f)
                .commit();
    }

    public void changeFragment(Fragment f, int option){

        mDrawer.check(option);
        mPosition = option;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_layout, f)
                .commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_settings).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
         * Declare the behaviour of clicking at the
         * application icon, opening and closing the drawer
         */
        if(item.getItemId() == android.R.id.home) {
            if(mDrawer != null) {
                if(mDrawer.isDrawerMenuOpen()) {
                    mDrawer.closeDrawerMenu();
                } else {
                    mDrawer.openDrawerMenu();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectOption(int position){

        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        boolean mustChange = false;

        Fragment f = new WelcomeFragment();

        if(position != mPosition) {
            switch (position) {
                case 0:
                    f = new SearchFragment();
                    mustChange = true;
                    mPosition = position;
                    break;
                case 1:
                    f = new FavoritesFragment();
                    mustChange = true;
                    mPosition = position;
                    break;
                case 2:
                    if(Checkers.hasInternet(this)) {
                        f = new AlertsNewsFragment();
                        mustChange = true;
                        mPosition = position;
                    } else {
                        showInternetDialog();
                    }
                    break;
                case 3:
                    if(Checkers.hasInternet(this)) {
                        f = new DownloadTimetablesFragment();
                        mustChange = true;
                        mPosition = position;
                    } else {
                        showInternetDialog();
                    }
                    break;
                case 4:
                    if(Checkers.hasInternet(this)) {
                        f = new ShowTarifesFragment();
                        mustChange = true;
                        mPosition = position;
                    } else {
                        showInternetDialog();
                    }
                    break;
                case 5:
                    if(Checkers.hasInternet(this)) {
                        f = new ShowMapFragment();
                        mustChange = true;
                        mPosition = position;
                    } else {
                        showInternetDialog();
                    }
                    break;
                case 6:
                    if(Checkers.hasInternet(this)) {
                        f = Checkers.isLowerThanIcs() ? new TwitterLowApiFragment() : new TwitterFragment();
                        mustChange = true;
                        mPosition = position;
                    } else {
                        showInternetDialog();
                    }
                    break;
                case 7:
                    f = new AboutFragment();
                    mustChange = true;
                    mPosition = position;
                    break;
                default:
                    f = new WelcomeFragment();
                    break;
            }

            if(mustChange) {
                FragmentManager fm = getSupportFragmentManager();


                fm.beginTransaction()
                        .replace(R.id.content_layout, f)
                        .commit();
            }
        }

    }

    private void firstTime(){
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
        if(p.getBoolean("first_time", true)){
            DataBaseHelper dbH = new DataBaseHelper(this);
            dbH.copyDB("parades.sqlite");
            p.edit().putBoolean("first_time", false).commit();
        }
    }

    public void changeNavDrawerSelection(int position){
        mDrawer.check(position);
    }

    private void showInternetDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error);
        builder.setMessage(R.string.no_internet_alert);
        builder.create().show();
    }

    private boolean showWelcome(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        return sp.getBoolean(Constants.SHOW_WELCOME, true);

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, mPosition);
    }


}
