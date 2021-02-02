package com.dafran.booksearch.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    public long guardar(SeguirManga sm){
        this.openWriteableDB();
        long filaID = db.insert(PaginasTabla.TABLA_SEGUIR, null, mapaSiguiendo(sm));
        this.closeDB();
        return filaID;
    }

    public void actualizar(SeguirManga sm) {
        this.openWriteableDB();
        String where = PaginasTabla.ID_ELEMENTO + " = ?";
        db.update(PaginasTabla.TABLA_SEGUIR, mapaSiguiendo(sm), where, new String[]{String.valueOf(sm.getId())});
        db.close();
    }

    public ArrayList llenarListaMangas(int valor) {
        ArrayList list = new ArrayList<>();
        this.openReadableDB();
        String[] campos = new String[]{PaginasTabla.ID_ELEMENTO, PaginasTabla.NOMBRE_MANGA, PaginasTabla.URL_MANGA, PaginasTabla.URL_IMAGEN, PaginasTabla.CONTADOR_CAPITULOS, PaginasTabla.BIT_SEGUIR_NO};
        String where = PaginasTabla.BIT_SEGUIR_NO + " = " + valor + ";";
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
