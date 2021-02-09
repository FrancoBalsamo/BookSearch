package com.dafran.booksearch.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dafran.booksearch.Clases.Paginas;
import com.dafran.booksearch.Clases.SeguirManga;
import com.dafran.booksearch.R;

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
        cv.put(PaginasTabla.TIPO_MANGA, sm.getTipo());
        return cv;
    }

    public long guardar(SeguirManga sm){
        this.openWriteableDB();
        long filaID = db.insert(PaginasTabla.TABLA_SEGUIR, null, mapaSiguiendo(sm));
        this.closeDB();
        return filaID;
    }

    public void actualizar(SeguirManga sm, String id) {//este es para pasar el manga a no visible
        this.openWriteableDB();
        ContentValues cv = new ContentValues();
        cv.put(PaginasTabla.BIT_SEGUIR_NO, sm.getValorSeguir());
        String[] idValor = {String.valueOf(id)};
        db.update(PaginasTabla.TABLA_SEGUIR, cv, PaginasTabla.ID_ELEMENTO + " = ?" , idValor);
        db.close();
    }

    public void actualizarCero(String id) {//esto es para que cuando el manga no sea visible, su estado vuelva a 1 para ser visible
        this.openWriteableDB();
        ContentValues cv = new ContentValues();
        cv.put(PaginasTabla.BIT_SEGUIR_NO, 1);
        String[] idValor = {String.valueOf(id)};
        db.update(PaginasTabla.TABLA_SEGUIR, cv, PaginasTabla.ID_ELEMENTO + " = ?" , idValor);
        db.close();
    }

    public boolean validarGuardado(Context actividad, String nombre, SeguirManga sm){
        this.openWriteableDB();
        String[] nom = {String.valueOf(nombre)};
        String consulta = "SELECT * FROM " + PaginasTabla.TABLA_SEGUIR + " WHERE " + PaginasTabla.NOMBRE_MANGA + " = ?" ;
        String consulta2 = "SELECT * FROM " + PaginasTabla.TABLA_SEGUIR + " WHERE " + PaginasTabla.NOMBRE_MANGA + " = ? " + "AND " + PaginasTabla.BIT_SEGUIR_NO + " = 1";
        String consulta3 = "SELECT * FROM " + PaginasTabla.TABLA_SEGUIR + " WHERE " + PaginasTabla.NOMBRE_MANGA + " = ? " + "AND " + PaginasTabla.BIT_SEGUIR_NO + " = 0";
        String consulta4 = "SELECT " + PaginasTabla.ID_ELEMENTO + " FROM " + PaginasTabla.TABLA_SEGUIR + " WHERE " + PaginasTabla.NOMBRE_MANGA + " = ? " + "AND " + PaginasTabla.BIT_SEGUIR_NO + " = 0";
        Cursor cursor = db.rawQuery(consulta, nom);
        Cursor cursor1 = db.rawQuery(consulta2, nom);
        Cursor cursor3 = db.rawQuery(consulta3, nom);
        Cursor cursor4 = db.rawQuery(consulta4, nom);
        if(cursor.getCount() <= 0){
            cursor.close();
            guardar(sm);
            String mensaje = "Has comenzado a seguir este manga, aparecerá en tu lista.";
            toastVerde(actividad, mensaje);
            return false;
        }else if(cursor1.getCount() > 0){
            cursor1.close();
            String mensaje = "Ya estas siguiendo este manga.";
            toastAzul(actividad, mensaje);
            return false;
        }else if(cursor3.getCount() > 0){
            cursor3.close();
            if(cursor4.moveToFirst()){
                String id = cursor4.getString(cursor4.getColumnIndex(PaginasTabla.ID_ELEMENTO));
                actualizarCero(id);
                String mensaje = "Añadido nuevamente a tu lista.";
                toastVerde(actividad, mensaje);
                return false;
            }
        }
        cursor.close();
        return true;
    }

    public boolean validarUpdate(Context actividad, String nombre, SeguirManga sm){//meétodo para validar el estado no visible del manga
        this.openWriteableDB();
        String[] nom = {String.valueOf(nombre)};
        String consulta = "SELECT * FROM " + PaginasTabla.TABLA_SEGUIR + " WHERE " + PaginasTabla.NOMBRE_MANGA + " = ?" ;
        String consulta2 = "SELECT * FROM " + PaginasTabla.TABLA_SEGUIR + " WHERE " + PaginasTabla.NOMBRE_MANGA + " = ? " + "AND " + PaginasTabla.BIT_SEGUIR_NO + " = 1";
        String consulta3 = "SELECT " + PaginasTabla.ID_ELEMENTO + " FROM " + PaginasTabla.TABLA_SEGUIR + " WHERE " + PaginasTabla.NOMBRE_MANGA + " = ? " + "AND " + PaginasTabla.BIT_SEGUIR_NO + " = 1";
        String consulta4 = "SELECT 0 FROM " + PaginasTabla.TABLA_SEGUIR + " WHERE " + PaginasTabla.NOMBRE_MANGA + " = ? " + "AND " + PaginasTabla.BIT_SEGUIR_NO + " = 0";
        Cursor cursor = db.rawQuery(consulta, nom);
        Cursor cursor1 = db.rawQuery(consulta2, nom);
        Cursor cursor2 = db.rawQuery(consulta3, nom);
        Cursor cursor3 = db.rawQuery(consulta4, nom);
        if(cursor.getCount() <= 0){
            cursor.close();
            String mensaje = "No sigues este manga.";
            toastAzul(actividad, mensaje);
            return false;
        }else if(cursor1.getCount() > 0){
            cursor1.close();
            if(cursor2.moveToFirst()){
                String id = cursor2.getString(cursor2.getColumnIndex(PaginasTabla.ID_ELEMENTO));
                actualizar(sm, id);
                String mensaje = "Has dejado de seguir este manga.";
                toastRojo(actividad, mensaje);
                return false;
            }
        }else if(cursor3.getCount() > 0){
            String mensaje = "Ya dejaste de seguir este manga.";
            toastAzul(actividad, mensaje);
        }
        cursor.close();
        return true;
    }

    public ArrayList llenarListaMangas() {
        ArrayList list = new ArrayList<>();
        this.openReadableDB();
        String[] campos = new String[]{PaginasTabla.ID_ELEMENTO, PaginasTabla.NOMBRE_MANGA, PaginasTabla.URL_MANGA, PaginasTabla.URL_IMAGEN, PaginasTabla.CONTADOR_CAPITULOS, PaginasTabla.BIT_SEGUIR_NO,PaginasTabla.TIPO_MANGA};
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
                sm.setTipo(c.getString(6));
                list.add(sm);
            }
        } finally { c.close(); }
        this.closeDB();
        return list;
    }

    private void toastAzul(Context actividad, String mensaje){
        Toast toast = Toast.makeText(actividad, mensaje, Toast.LENGTH_LONG);
        View view = toast.getView();
        view.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        TextView toastTextView = view.findViewById(R.id.toastTextView);
        toast.show();
    }

    private void toastRojo(Context actividad, String mensaje){
        Toast toast = Toast.makeText(actividad, mensaje, Toast.LENGTH_LONG);
        View view = toast.getView();
        view.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        TextView toastTextView = view.findViewById(R.id.toastTextView);
        toast.show();
    }

    private void toastVerde(Context actividad, String mensaje){
        Toast toast = Toast.makeText(actividad, mensaje, Toast.LENGTH_LONG);
        View view = toast.getView();
        view.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        TextView toastTextView = view.findViewById(R.id.toastTextView);
        toast.show();
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
