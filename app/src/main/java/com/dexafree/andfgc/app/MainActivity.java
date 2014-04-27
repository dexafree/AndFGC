package com.dexafree.andfgc.app;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import com.dexafree.andfgc.app.databases.DataBaseHelper;
import com.dexafree.andfgc.app.fragments.MainFragment;
import com.dexafree.andfgc.app.fragments.PruebaPanelFragment;
import org.arasthel.googlenavdrawermenu.views.GoogleNavigationDrawer;

public class MainActivity extends ActionBarActivity {

    private ActionBarDrawerToggle drawerToggle;
    private GoogleNavigationDrawer mDrawer;

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
                R.drawable.ic_launcher,
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

        /*Fragment f = new MainFragment();

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.content_layout, f)
                .commit();*/
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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
        Fragment f;
        switch(position){
            case 0:
                f = new MainFragment();
                break;
            case 1:
                f = new PruebaPanelFragment();
                break;
            default:
                f = new MainFragment();
                break;
        }

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.content_layout, f)
                .commit();
    }

    private void firstTime(){
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
        if(p.getBoolean("first_time", true)){
            DataBaseHelper dbH = new DataBaseHelper(this);
            dbH.copyDB("parades.sqlite");
            p.edit().putBoolean("first_time", false).commit();
        }
    }



}
