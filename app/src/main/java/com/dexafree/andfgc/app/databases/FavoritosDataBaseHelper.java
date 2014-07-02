package com.dexafree.andfgc.app.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Carlos on 02/07/14.
 */
public class FavoritosDataBaseHelper extends SQLiteOpenHelper {

    String sqlCreate = "CREATE TABLE Favoritos (from_stop TEXT, to_stop TEXT, line INTEGER, title TEXT, ID INTEGER PRIMARY KEY AUTOINCREMENT)";

    public FavoritosDataBaseHelper(Context context,
                                   SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "FavoritosDB", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Favoritos");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
    }

}
