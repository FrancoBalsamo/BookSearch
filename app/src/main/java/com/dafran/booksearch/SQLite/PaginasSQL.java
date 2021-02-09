package com.dafran.booksearch.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dafran.booksearch.Clases.CapitulosLeidos;
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

    private ContentValues mapaLeidos(CapitulosLeidos capitulosLeidos){
        ContentValues contentValues = new ContentValues();
        contentValues.put(PaginasTabla.NOMBRE, capitulosLeidos.getNombre_manga());
        contentValues.put(PaginasTabla.BIT_LEIDO, capitulosLeidos.getLeido());
        contentValues.put(PaginasTabla.ULTIMO_LEIDO, capitulosLeidos.getNombre_capitulo_leido());
        return contentValues;
    }

    private long guardar(SeguirManga sm){
        this.openWriteableDB();
        long filaID = db.insert(PaginasTabla.TABLA_SEGUIR, null, mapaSiguiendo(sm));
        this.closeDB();
        return filaID;
    }

    private void actualizar(SeguirManga sm, String id) {//este es para pasar el manga a no visible
        this.openWriteableDB();
        ContentValues cv = new ContentValues();
        cv.put(PaginasTabla.BIT_SEGUIR_NO, sm.getValorSeguir());
        String[] idValor = {String.valueOf(id)};
        db.update(PaginasTabla.TABLA_SEGUIR, cv, PaginasTabla.ID_ELEMENTO + " = ?" , idValor);
        db.close();
    }

    private void actualizarCero(String id) {//esto es para que cuando el manga no sea visible, su estado vuelva a 1 para ser visible
        this.openWriteableDB();
        ContentValues cv = new ContentValues();
        cv.put(PaginasTabla.BIT_SEGUIR_NO, 1);
        String[] idValor = {String.valueOf(id)};
        db.update(PaginasTabla.TABLA_SEGUIR, cv, PaginasTabla.ID_ELEMENTO + " = ?" , idValor);
        db.close();
    }

    private void actualizarCantidadCapitulos(SeguirManga seguirManga, String id){
        this.openWriteableDB();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PaginasTabla.CONTADOR_CAPITULOS, seguirManga.getContador());
        String[] id_ = {String.valueOf(id)};
        db.update(PaginasTabla.TABLA_SEGUIR, contentValues, PaginasTabla.ID_ELEMENTO + " = ?", id_);
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

    public boolean validarUpdateEstadoSiguiendo(Context actividad, String nombre, SeguirManga sm){//meétodo para validar el estado no visible del manga
        this.openWriteableDB();
        String[] nom = {String.valueOf(nombre)};
        String consulta = "SELECT * FROM " + PaginasTabla.TABLA_SEGUIR + " WHERE " + PaginasTabla.NOMBRE_MANGA + " = ?";
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
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean actualizadorDeCapitulos(String nombre, SeguirManga sm){
        this.openWriteableDB();
        String[] nombre_manga = {String.valueOf(nombre)};
        String consulta = "SELECT * FROM " + PaginasTabla.TABLA_SEGUIR + " WHERE " + PaginasTabla.NOMBRE_MANGA + " = ?";
        String extraerIDA = "SELECT " + PaginasTabla.ID_ELEMENTO + " FROM " + PaginasTabla.TABLA_SEGUIR + " WHERE " + PaginasTabla.NOMBRE_MANGA + " = ? " + "AND " + PaginasTabla.BIT_SEGUIR_NO + " = 1";
        String extraerIDB = "SELECT " + PaginasTabla.ID_ELEMENTO + " FROM " + PaginasTabla.TABLA_SEGUIR + " WHERE " + PaginasTabla.NOMBRE_MANGA + " = ? " + "AND " + PaginasTabla.BIT_SEGUIR_NO + " = 0";
        Cursor inicial = db.rawQuery(consulta, nombre_manga);
        Cursor cursor = db.rawQuery(extraerIDA, nombre_manga);
        Cursor cursor1 = db.rawQuery(extraerIDB, nombre_manga);
        if(inicial.getCount() > 0){
            inicial.close();
            if(cursor.moveToFirst() || cursor1.moveToFirst()){
                String id_ = cursor.getString(cursor.getColumnIndex(PaginasTabla.ID_ELEMENTO));
                actualizarCantidadCapitulos(sm, id_);
                return false;
            }
        }
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

    public boolean consultarSiguiendoYGuardarLeido(Context actividad, String nombreManga, String capitulos, CapitulosLeidos capitulosLeidos){
        this.openWriteableDB();
        String[] nombre = {String.valueOf(nombreManga)}; //para el nombre del manga
        String[] capitulos_leidos = {String.valueOf(capitulos)}; //para el capítulo leído
        String consultaSiguiendoActivo = "SELECT * FROM " + PaginasTabla.TABLA_SEGUIR + " WHERE " + PaginasTabla.NOMBRE_MANGA + " = ? AND " + PaginasTabla.BIT_SEGUIR_NO + " = 1";
        String consultaSiguiendoInactivo = "SELECT * FROM " + PaginasTabla.TABLA_SEGUIR + " WHERE " + PaginasTabla.NOMBRE_MANGA + " = ? AND " + PaginasTabla.BIT_SEGUIR_NO + " = 0";
        String consultaNombreMangaLeido = "SELECT * FROM " + PaginasTabla.TABLA_CAPITULO_LEIDO + " WHERE " + PaginasTabla.NOMBRE + " = ?";
        String consultaCapituloMangaLeido = "SELECT * FROM " + PaginasTabla.TABLA_CAPITULO_LEIDO + " WHERE " + PaginasTabla.ULTIMO_LEIDO + " = ?";
        Cursor a = db.rawQuery(consultaSiguiendoActivo, nombre);
        Cursor b = db.rawQuery(consultaSiguiendoInactivo, nombre);
        Cursor c = db.rawQuery(consultaNombreMangaLeido, nombre);
        Cursor d = db.rawQuery(consultaCapituloMangaLeido, capitulos_leidos);
        if(a.getCount() > 0 || b.getCount() > 0){
            a.close();
            b.close();
            if(c.getCount() <= 0){//se hace un insert por no existir este dato en la tabla
                c.close();
                guardarLeidos(capitulosLeidos);
                return false;
            }else if(c.getCount() > 0){//si existe el nombre, nos fijamos si tiene ese último capítulo guardado
                c.close();
                if(d.getCount() <= 0){//sino existe ese capítulo
                    d.close();
                    guardarLeidos(capitulosLeidos);
                    return false;
                }else{
                    d.close();
                    return false;
                }
            }
        }
        a.close();
        return true;
    }

    public boolean textoLeido(Context actividad, String nombre, String capitulo){
        this.openReadableDB();
        String[] nombre_manga = {String.valueOf(nombre)};
        String[] nombre_capitulo = {String.valueOf(capitulo)};
        String consultaSiguiendoActivo = "SELECT * FROM " + PaginasTabla.TABLA_SEGUIR + " WHERE " + PaginasTabla.NOMBRE_MANGA + " = ? AND " + PaginasTabla.BIT_SEGUIR_NO + " = 1";
        String consultarNombre = "SELECT * FROM " + PaginasTabla.TABLA_CAPITULO_LEIDO + " WHERE " + PaginasTabla.NOMBRE + " = ?";
        String consultarCapitulo = "SELECT * FROM " + PaginasTabla.TABLA_CAPITULO_LEIDO + " WHERE " + PaginasTabla.ULTIMO_LEIDO + " = ?";
        Cursor inicial = db.rawQuery(consultaSiguiendoActivo, nombre_manga);
        Cursor a = db.rawQuery(consultarNombre, nombre_manga);
        Cursor b = db.rawQuery(consultarCapitulo, nombre_capitulo);
        if(inicial.getCount() > 0){
            inicial.close();
            if(a.getCount() > 0){
                a.close();
                if(b.getCount() > 0){
                    b.close();
                    return false;
                }
            }
        }
        return true;
    }

    private long guardarLeidos(CapitulosLeidos capitulosLeidos){
        this.openWriteableDB();
        long filaID = db.insert(PaginasTabla.TABLA_CAPITULO_LEIDO, null, mapaLeidos(capitulosLeidos));
        this.closeDB();
        return filaID;
    }

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, PaginasTabla.DB_NAME, null, PaginasTabla.DB_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(PaginasTabla.TABLA_PARA_SEGUIR);
            db.execSQL(PaginasTabla.TABLA_CAPITULO_LEIDO_SQL);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
    }
}
