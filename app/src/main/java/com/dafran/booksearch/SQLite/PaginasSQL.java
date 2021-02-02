package com.dafran.booksearch.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.dafran.booksearch.Clases.Paginas;
import com.dafran.booksearch.Clases.SeguirManga;

import java.io.Serializable;
import java.util.ArrayList;

public class PaginasSQL implements Serializable {
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public PaginasSQL(Context context) {
        dbHelper = new DBHelper(context);
    }
    private void openReadableDB() {
        db = dbHelper.getReadableDatabase();
    }
    private void openWriteableDB() {
        db = dbHelper.getWritableDatabase();
    }

    private void closeDB() {
        if (db != null) {
            db.close();
        }
    }

    private ContentValues mapaSiguiendo(SeguirManga sm){
        ContentValues cv = new ContentValues();
        cv.put(PaginasTabla.NOMBRE_MANGA, sm.getNombre());
        cv.put(PaginasTabla.URL_MANGA, sm.getUrl());
        cv.put(PaginasTabla.URL_IMAGEN, sm.getUrlImagen());
        cv.put(PaginasTabla.CONTADOR_CAPITULOS, sm.getContador());
        cv.put(PaginasTabla.BIT_SEGUIR_NO, sm.getValorSeguir());
        return cv;
    }

    private long guardar(SeguirManga sm){
        long filaID = db.insert(PaginasTabla.TABLA_SEGUIR, null, mapaSiguiendo(sm));
        this.closeDB();
        return filaID;
    }

    public void validar(SeguirManga sm, Context actividad, String nommbre){
        Cursor cursor = db.rawQuery("SELECT * FROM " + PaginasTabla.TABLA_SEGUIR + " WHERE " + PaginasTabla.NOMBRE_MANGA + "='" + nommbre + "''", null);
        if (cursor.moveToNext()) {
            actualizar(sm, actividad, nommbre);
        } else {
            guardar(sm);
        }
    }

    private void actualizar(SeguirManga sm, Context actividad, String nommbre) {
        this.openWriteableDB();
            ContentValues cv = new ContentValues();
            cv.put(PaginasTabla.BIT_SEGUIR_NO, sm.getValorSeguir());
            String[] whereArgs = {String.valueOf(nommbre)};
            db.update(PaginasTabla.TABLA_SEGUIR, cv, PaginasTabla.NOMBRE_MANGA + " = ?" , whereArgs);
            db.close();
            Toast.makeText(actividad, "Modificado", Toast.LENGTH_SHORT).show();
    }

    public ArrayList llenarListaMangas() {
        ArrayList list = new ArrayList<>();
        this.openReadableDB();
        String[] campos = new String[]{PaginasTabla.ID_ELEMENTO, PaginasTabla.NOMBRE_MANGA, PaginasTabla.URL_MANGA, PaginasTabla.URL_IMAGEN, PaginasTabla.CONTADOR_CAPITULOS, PaginasTabla.BIT_SEGUIR_NO};
        String where = PaginasTabla.BIT_SEGUIR_NO + " = 1";
        Cursor c = db.query(PaginasTabla.TABLA_SEGUIR, campos, where, null, null, null, null);
        try {
            while (c.moveToNext()) {
                SeguirManga sm = new SeguirManga();
                sm.setId(c.getInt(0));
                sm.setNombre(c.getString(1));
                sm.setUrl(c.getString(2));
                sm.setUrlImagen(c.getString(3));
                sm.setContador(c.getString(4));
                sm.setValorSeguir(c.getInt(5));
                list.add(sm);
            }
        } finally { c.close(); }
        this.closeDB();
        return list;
    }

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, PaginasTabla.DB_NAME, null, PaginasTabla.DB_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(PaginasTabla.TABLA_PARA_SEGUIR);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
    }
}
