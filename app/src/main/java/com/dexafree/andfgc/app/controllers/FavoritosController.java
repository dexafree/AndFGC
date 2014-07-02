package com.dexafree.andfgc.app.controllers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dexafree.andfgc.app.beans.Favorito;
import com.dexafree.andfgc.app.databases.FavoritosDataBaseHelper;
import com.dexafree.andfgc.app.utils.Logger;

import java.util.ArrayList;

/**
 * Created by Carlos on 02/07/14.
 */
public class FavoritosController {

    private static SQLiteDatabase getDb(Context context, boolean readable) {

        if(context == null) Logger.d("ES", "NULL");

        FavoritosDataBaseHelper dbHelper = new FavoritosDataBaseHelper(context, null, 1);

        SQLiteDatabase db;

        if(readable) db = dbHelper.getReadableDatabase();
        else db = dbHelper.getWritableDatabase();


        return db;
    }

    public static void insertFavorito(Context context, Favorito fav){

        SQLiteDatabase db = getDb(context, false);

        if(db != null) {

            String sql =
                    "INSERT INTO Favoritos (from_stop, to_stop, line, title) VALUES (" +
                            "'" + fav.getOrigen() +
                            "','" + fav.getDesti() +
                            "'," + fav.getLinia() +
                            ", '"+fav.getTitle()+"')";

            db.execSQL(sql);

            db.close();
        }

    }

    public static ArrayList<Favorito> getAllFavoritos(Context context){

        SQLiteDatabase db = getDb(context, true);
        ArrayList<Favorito> favList = new ArrayList<Favorito>();

        if(db != null){
            String sql = "SELECT * FROM Favoritos";

            Cursor cursor = db.rawQuery(sql, null);

            if(cursor.moveToFirst()){
                while(cursor.moveToNext()){

                    String from = cursor.getString(cursor.getColumnIndex("from_stop"));
                    String to = cursor.getString(cursor.getColumnIndex("to_stop"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    int line = cursor.getInt(cursor.getColumnIndex("line"));
                    int id = cursor.getInt(cursor.getColumnIndex("ID"));

                    favList.add(new Favorito(from, to, title, id, line));

                }
            }

            db.close();
        }

        return favList;
    }

    public static void deleteFavorito(Context context, int id){

        SQLiteDatabase db = getDb(context, false);

        if(db != null) {

            String sql =
                    "DELETE FROM Favoritos WHERE ID = "+id;

            db.execSQL(sql);

            db.close();
        }
    }

}
