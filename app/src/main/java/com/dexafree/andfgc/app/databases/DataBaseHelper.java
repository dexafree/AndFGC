package com.dexafree.andfgc.app.databases;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Carlos on 27/04/2014.
 */
public class DataBaseHelper {

    private Context context;
    private ParadasSQLiteHelper paradasHelper;
    private SQLiteDatabase db;
    private ProgressDialog copiandoDB;
    public static int VERSION = 1;
    public static String DB_NAME = "parades.sqlite";

    public DataBaseHelper(Context context) {
        this.context = context;
    }

    public void copyDB(String dbName) {
        ParadasSQLiteHelper paradasHelper = new ParadasSQLiteHelper(context, dbName, null, 1);

        SQLiteDatabase db = paradasHelper.getWritableDatabase();
        db.close();
        try {
            InputStream is = context.getAssets().open(dbName);
            String destino = context.getDatabasePath(dbName).getAbsolutePath();
            FileOutputStream fos = new FileOutputStream(destino);
            byte[] buf = new byte[1024];
            int len;
            while((len = is.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            fos.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getDB(){

        try {
            paradasHelper = new ParadasSQLiteHelper(context, DB_NAME, null, 1);

            db = paradasHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            errorBD(e);
        }
    }

    public SQLiteDatabase getDataBase(){
        SQLiteDatabase db;

        try{
            db = new ParadasSQLiteHelper(context, DB_NAME, null, VERSION).getReadableDatabase();
        } catch (SQLiteException e){
            db = null;
        }

        return db;
    }


    private class ParadasSQLiteHelper extends SQLiteOpenHelper {

        public ParadasSQLiteHelper(Context context, String name,
                                   SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int lastVersion, int newVersion) {
            db.execSQL("DELETE FROM parades");
            copyDB(DB_NAME);
        }

    }

    public void errorBD(final Exception e) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error al cargar la BD");
        builder.setMessage("La base de datos está corrupta. Se copiará de nuevo la base de datos para intentar arreglar el error.");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                copiandoDB = new ProgressDialog(context);
                copiandoDB.setMessage("Copiando base de datos...");
                copiandoDB.setIndeterminate(true);
                copiandoDB.setCancelable(false);
                copiandoDB.show();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        context.deleteDatabase(DB_NAME);
                        copyDB("parades");
                        getDB();
                        copiandoDB.dismiss();
                    }
                };
                new Thread(r).start();

            }
        });
        builder.setNegativeButton("Cerrar aplicación", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                ((Activity) context).finish();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface arg0) {
                ((Activity) context).finish();
            }
        });
        builder.show();
    }

}