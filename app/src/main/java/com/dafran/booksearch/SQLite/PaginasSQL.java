package com.dafran.booksearch.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dafran.booksearch.Clases.Paginas;

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
    private ContentValues paginasMapa(Paginas p) {
        ContentValues cv = new ContentValues();
        cv.put(PaginasTabla.PAGINA_ID, p.getId());
        cv.put(PaginasTabla.PAGINA_NOMBRE, p.getPagina());
        return cv;
    }
    public long guardar(Paginas p) {
        this.openWriteableDB();
        long rowID = db.insert(PaginasTabla.TABLA_PAGINAS, null, paginasMapa(p));
        this.closeDB();
        return rowID;
    }

    public ArrayList llenar_AL() {
        ArrayList list = new ArrayList<>();
        this.openReadableDB();
        String[] campos = new String[]{PaginasTabla.PAGINA_ID, PaginasTabla.PAGINA_NOMBRE};
        Cursor c = db.query(PaginasTabla.TABLA_PAGINAS, campos, null, null, null, null, null);
        try {
            while (c.moveToNext()) {
                Paginas p = new Paginas();
                p.setId(c.getInt(0));
                p.setPagina(c.getString(1));
                list.add(p);
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
            db.execSQL(PaginasTabla.TABLA_PAGINA_SQL);
            db.execSQL("INSERT INTO " + PaginasTabla.TABLA_PAGINAS + "(" + PaginasTabla.PAGINA_NOMBRE + ") VALUES ('Trantor.is')" );
            db.execSQL("INSERT INTO " + PaginasTabla.TABLA_PAGINAS + "(" + PaginasTabla.PAGINA_NOMBRE + ") VALUES ('TMOnline')" );
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
    }
}
