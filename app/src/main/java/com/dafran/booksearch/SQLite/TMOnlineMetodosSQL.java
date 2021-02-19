package com.dafran.booksearch.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dafran.booksearch.Activities.TMO.TMOnlineListaMangasSiguiendo;
import com.dafran.booksearch.Clases.CSVOpenOffice.CSVWriter;
import com.dafran.booksearch.Clases.TMOClases.TMOnlineCapitulosLeidos;
import com.dafran.booksearch.Clases.TMOClases.TMOnlineSeguirManga;
import com.dafran.booksearch.R;
import com.google.android.gms.measurement.api.AppMeasurementSdk;

import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.ArrayList;

public class TMOnlineMetodosSQL implements Serializable {
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public TMOnlineMetodosSQL(Context context) {
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

    private ContentValues mapaSiguiendo(TMOnlineSeguirManga sm){
        ContentValues cv = new ContentValues();
        cv.put(TMOnlineTablasSQL.NOMBRE_MANGA, sm.getNombre());
        cv.put(TMOnlineTablasSQL.URL_MANGA, sm.getUrl());
        cv.put(TMOnlineTablasSQL.URL_IMAGEN, sm.getUrlImagen());
        cv.put(TMOnlineTablasSQL.CONTADOR_CAPITULOS, sm.getContador());
        cv.put(TMOnlineTablasSQL.BIT_SEGUIR_NO, sm.getValorSeguir());
        cv.put(TMOnlineTablasSQL.TIPO_MANGA, sm.getTipo());
        return cv;
    }

    private ContentValues mapaLeidos(TMOnlineCapitulosLeidos TMOnlineCapitulosLeidos){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TMOnlineTablasSQL.NOMBRE, TMOnlineCapitulosLeidos.getNombre_manga());
        contentValues.put(TMOnlineTablasSQL.BIT_LEIDO, TMOnlineCapitulosLeidos.getLeido());
        contentValues.put(TMOnlineTablasSQL.ULTIMO_LEIDO, TMOnlineCapitulosLeidos.getNombre_capitulo_leido());
        return contentValues;
    }

    private long guardar(TMOnlineSeguirManga sm){
        this.openWriteableDB();
        long filaID = db.insert(TMOnlineTablasSQL.TABLA_SEGUIR, null, mapaSiguiendo(sm));
        this.closeDB();
        return filaID;
    }

    private void actualizar(TMOnlineSeguirManga sm, String id) {//este es para pasar el manga a no visible
        this.openWriteableDB();
        ContentValues cv = new ContentValues();
        cv.put(TMOnlineTablasSQL.BIT_SEGUIR_NO, sm.getValorSeguir());
        String[] idValor = {String.valueOf(id)};
        db.update(TMOnlineTablasSQL.TABLA_SEGUIR, cv, TMOnlineTablasSQL.ID_ELEMENTO + " = ?" , idValor);
        db.close();
    }

    private void actualizarCero(String id) {//esto es para que cuando el manga no sea visible, su estado vuelva a 1 para ser visible
        this.openWriteableDB();
        ContentValues cv = new ContentValues();
        cv.put(TMOnlineTablasSQL.BIT_SEGUIR_NO, 1);
        String[] idValor = {String.valueOf(id)};
        db.update(TMOnlineTablasSQL.TABLA_SEGUIR, cv, TMOnlineTablasSQL.ID_ELEMENTO + " = ?" , idValor);
        db.close();
    }

    private void actualizarCantidadCapitulos(TMOnlineSeguirManga TMOnlineSeguirManga, String id){
        this.openWriteableDB();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TMOnlineTablasSQL.CONTADOR_CAPITULOS, TMOnlineSeguirManga.getContador());
        String[] id_ = {String.valueOf(id)};
        db.update(TMOnlineTablasSQL.TABLA_SEGUIR, contentValues, TMOnlineTablasSQL.ID_ELEMENTO + " = ?", id_);
        db.close();
    }

    public boolean validarGuardado(Context actividad, String nombre, TMOnlineSeguirManga sm){
        this.openWriteableDB();
        String[] nom = {String.valueOf(nombre)};
        String consulta = "SELECT * FROM " + TMOnlineTablasSQL.TABLA_SEGUIR + " WHERE " + TMOnlineTablasSQL.NOMBRE_MANGA + " = ?" ;
        String consulta2 = "SELECT * FROM " + TMOnlineTablasSQL.TABLA_SEGUIR + " WHERE " + TMOnlineTablasSQL.NOMBRE_MANGA + " = ? " + "AND " + TMOnlineTablasSQL.BIT_SEGUIR_NO + " = 1";
        String consulta3 = "SELECT * FROM " + TMOnlineTablasSQL.TABLA_SEGUIR + " WHERE " + TMOnlineTablasSQL.NOMBRE_MANGA + " = ? " + "AND " + TMOnlineTablasSQL.BIT_SEGUIR_NO + " = 0";
        String consulta4 = "SELECT " + TMOnlineTablasSQL.ID_ELEMENTO + " FROM " + TMOnlineTablasSQL.TABLA_SEGUIR + " WHERE " + TMOnlineTablasSQL.NOMBRE_MANGA + " = ? " + "AND " + TMOnlineTablasSQL.BIT_SEGUIR_NO + " = 0";
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
                String id = cursor4.getString(cursor4.getColumnIndex(TMOnlineTablasSQL.ID_ELEMENTO));
                actualizarCero(id);
                String mensaje = "Añadido nuevamente a tu lista.";
                toastVerde(actividad, mensaje);
                return false;
            }
        }
        cursor.close();
        return true;
    }

    public boolean validarUpdateEstadoSiguiendo(Context actividad, String nombre, TMOnlineSeguirManga sm){//meétodo para validar el estado no visible del manga
        this.openWriteableDB();
        String[] nom = {String.valueOf(nombre)};
        String consulta = "SELECT * FROM " + TMOnlineTablasSQL.TABLA_SEGUIR + " WHERE " + TMOnlineTablasSQL.NOMBRE_MANGA + " = ?";
        String consulta2 = "SELECT * FROM " + TMOnlineTablasSQL.TABLA_SEGUIR + " WHERE " + TMOnlineTablasSQL.NOMBRE_MANGA + " = ? " + "AND " + TMOnlineTablasSQL.BIT_SEGUIR_NO + " = 1";
        String consulta3 = "SELECT " + TMOnlineTablasSQL.ID_ELEMENTO + " FROM " + TMOnlineTablasSQL.TABLA_SEGUIR + " WHERE " + TMOnlineTablasSQL.NOMBRE_MANGA + " = ? " + "AND " + TMOnlineTablasSQL.BIT_SEGUIR_NO + " = 1";
        String consulta4 = "SELECT 0 FROM " + TMOnlineTablasSQL.TABLA_SEGUIR + " WHERE " + TMOnlineTablasSQL.NOMBRE_MANGA + " = ? " + "AND " + TMOnlineTablasSQL.BIT_SEGUIR_NO + " = 0";
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
                String id = cursor2.getString(cursor2.getColumnIndex(TMOnlineTablasSQL.ID_ELEMENTO));
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

    public boolean actualizadorDeCapitulos(String nombre, TMOnlineSeguirManga sm){
        this.openWriteableDB();
        String[] nombre_manga = {String.valueOf(nombre)};
        String consulta = "SELECT * FROM " + TMOnlineTablasSQL.TABLA_SEGUIR + " WHERE " + TMOnlineTablasSQL.NOMBRE_MANGA + " = ?";
        String extraerIDA = "SELECT " + TMOnlineTablasSQL.ID_ELEMENTO + " FROM " + TMOnlineTablasSQL.TABLA_SEGUIR + " WHERE " + TMOnlineTablasSQL.NOMBRE_MANGA + " = ? " + "AND " + TMOnlineTablasSQL.BIT_SEGUIR_NO + " = 1";
        String extraerIDB = "SELECT " + TMOnlineTablasSQL.ID_ELEMENTO + " FROM " + TMOnlineTablasSQL.TABLA_SEGUIR + " WHERE " + TMOnlineTablasSQL.NOMBRE_MANGA + " = ? " + "AND " + TMOnlineTablasSQL.BIT_SEGUIR_NO + " = 0";
        Cursor inicial = db.rawQuery(consulta, nombre_manga);
        Cursor cursor = db.rawQuery(extraerIDA, nombre_manga);
        Cursor cursor1 = db.rawQuery(extraerIDB, nombre_manga);
        if(inicial.getCount() > 0){
            inicial.close();
            if(cursor.moveToFirst() || cursor1.moveToFirst()){
                String id_ = cursor.getString(cursor.getColumnIndex(TMOnlineTablasSQL.ID_ELEMENTO));
                actualizarCantidadCapitulos(sm, id_);
                return false;
            }
        }
        return true;
    }

    public ArrayList llenarListaMangas() {
        ArrayList list = new ArrayList<>();
        this.openReadableDB();
        String[] campos = new String[]{TMOnlineTablasSQL.ID_ELEMENTO, TMOnlineTablasSQL.NOMBRE_MANGA, TMOnlineTablasSQL.URL_MANGA, TMOnlineTablasSQL.URL_IMAGEN, TMOnlineTablasSQL.CONTADOR_CAPITULOS, TMOnlineTablasSQL.BIT_SEGUIR_NO, TMOnlineTablasSQL.TIPO_MANGA};
        String where = TMOnlineTablasSQL.BIT_SEGUIR_NO + " = 1";
        String orderBy = TMOnlineTablasSQL.NOMBRE_MANGA + " ASC";
        Cursor c = db.query(TMOnlineTablasSQL.TABLA_SEGUIR, campos, where, null, null, null, orderBy);
        try {
            while (c.moveToNext()) {
                TMOnlineSeguirManga sm = new TMOnlineSeguirManga();
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

    public boolean consultarSiguiendoYGuardarLeido(Context actividad, String nombreManga, String capitulos, TMOnlineCapitulosLeidos TMOnlineCapitulosLeidos){
        this.openWriteableDB();
        String[] nombre = {String.valueOf(nombreManga)}; //para el nombre del manga
        String[] capitulos_leidos = {String.valueOf(capitulos)}; //para el capítulo leído
        String consultaSiguiendoActivo = "SELECT * FROM " + TMOnlineTablasSQL.TABLA_SEGUIR + " WHERE " + TMOnlineTablasSQL.NOMBRE_MANGA + " = ? AND " + TMOnlineTablasSQL.BIT_SEGUIR_NO + " = 1";
        String consultaSiguiendoInactivo = "SELECT * FROM " + TMOnlineTablasSQL.TABLA_SEGUIR + " WHERE " + TMOnlineTablasSQL.NOMBRE_MANGA + " = ? AND " + TMOnlineTablasSQL.BIT_SEGUIR_NO + " = 0";
        String consultaNombreMangaLeido = "SELECT * FROM " + TMOnlineTablasSQL.TABLA_CAPITULO_LEIDO + " WHERE " + TMOnlineTablasSQL.NOMBRE + " = ?";
        String consultaCapituloMangaLeido = "SELECT * FROM " + TMOnlineTablasSQL.TABLA_CAPITULO_LEIDO + " WHERE " + TMOnlineTablasSQL.ULTIMO_LEIDO + " = ?";
        Cursor a = db.rawQuery(consultaSiguiendoActivo, nombre);
        Cursor b = db.rawQuery(consultaSiguiendoInactivo, nombre);
        Cursor c = db.rawQuery(consultaNombreMangaLeido, nombre);
        Cursor d = db.rawQuery(consultaCapituloMangaLeido, capitulos_leidos);
        if(a.getCount() > 0 || b.getCount() > 0){
            a.close();
            b.close();
            if(c.getCount() <= 0){//se hace un insert por no existir este dato en la tabla
                c.close();
                guardarLeidos(TMOnlineCapitulosLeidos);
                return false;
            }else if(c.getCount() > 0){//si existe el nombre, nos fijamos si tiene ese último capítulo guardado
                c.close();
                if(d.getCount() <= 0){//sino existe ese capítulo
                    d.close();
                    guardarLeidos(TMOnlineCapitulosLeidos);
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
        String consultaSiguiendoActivo = "SELECT * FROM " + TMOnlineTablasSQL.TABLA_SEGUIR + " WHERE " + TMOnlineTablasSQL.NOMBRE_MANGA + " = ? AND " + TMOnlineTablasSQL.BIT_SEGUIR_NO + " = 1";
        String consultarNombre = "SELECT * FROM " + TMOnlineTablasSQL.TABLA_CAPITULO_LEIDO + " WHERE " + TMOnlineTablasSQL.NOMBRE + " = ?";
        String consultarCapitulo = "SELECT * FROM " + TMOnlineTablasSQL.TABLA_CAPITULO_LEIDO + " WHERE " + TMOnlineTablasSQL.ULTIMO_LEIDO + " = ?";
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

    private long guardarLeidos(TMOnlineCapitulosLeidos TMOnlineCapitulosLeidos){
        this.openWriteableDB();
        long filaID = db.insert(TMOnlineTablasSQL.TABLA_CAPITULO_LEIDO, null, mapaLeidos(TMOnlineCapitulosLeidos));
        this.closeDB();
        return filaID;
    }

    public void descargarLista(Context actividad) {
        if(Build.VERSION.SDK_INT >= 29){
            //para targetSDK >= 29
            File directorio = new File(actividad.getExternalFilesDir(null), "TMO_Descarga");
            if(!directorio.exists()){
                directorio.mkdirs();
            }
            File archivo = new File(directorio, "Lista.csv");
            try{
                archivo.createNewFile();
                CSVWriter csvWriter = new CSVWriter(new FileWriter(archivo));
                this.openReadableDB();
                Cursor buscarDatos = db.rawQuery("SELECT * FROM " + TMOnlineTablasSQL.TABLA_SEGUIR, null);
                if(buscarDatos.getCount() > 0){
                    buscarDatos.close();
                    Cursor cursorCSV = db.rawQuery("SELECT "
                            + TMOnlineTablasSQL.NOMBRE_MANGA + " AS Nombre, "
                            + TMOnlineTablasSQL.TIPO_MANGA + " AS Tipo, "
                            + TMOnlineTablasSQL.BIT_SEGUIR_NO + " AS Siguiendo "
                            + " FROM " + TMOnlineTablasSQL.TABLA_SEGUIR
                            + " ORDER BY " + TMOnlineTablasSQL.NOMBRE_MANGA
                            + " ASC", null);
                    csvWriter.writeNext(cursorCSV.getColumnNames());
                    while (cursorCSV.moveToNext()){
                        if(cursorCSV.getString(2).equals(String.valueOf(1))){
                            String siguiendo = cursorCSV.getString(2);
                            siguiendo = siguiendo.replaceAll(String.valueOf(1), "Sí");
                            String[] columnas = {cursorCSV.getString(0), cursorCSV.getString(1), siguiendo};
                            csvWriter.writeNext(columnas);
                        }else{
                            String siguiendo = cursorCSV.getString(2);
                            siguiendo = siguiendo.replaceAll(String.valueOf(0), "No");
                            String[] columnas = {cursorCSV.getString(0), cursorCSV.getString(1), siguiendo};
                            csvWriter.writeNext(columnas);
                        }
                    }
                    csvWriter.close();
                    cursorCSV.close();
                    toastVerde(actividad, "¡Has descargado con éxito tu lista! (La descarga no consume datos).");
                }
                else {
                    toastAzul(actividad, "¡Ups! Actualmente no tienes ningún elemento en tu lista.");
                }
            }catch (Exception sqlException){
                Log.e("TMODescargaError", sqlException.getMessage(), sqlException);
            }
        }else{
            //para targetSDK < 29
            File directorio = new File(Environment.getExternalStorageDirectory(), "TMO_Descarga");
            if(!directorio.exists()){
                directorio.mkdirs();
            }
            File archivo = new File(directorio, "Lista.csv");
            try{
                archivo.createNewFile();
                CSVWriter csvWriter = new CSVWriter(new FileWriter(archivo));
                this.openReadableDB();
                Cursor buscarDatos = db.rawQuery("SELECT * FROM " + TMOnlineTablasSQL.TABLA_SEGUIR, null);
                if(buscarDatos.getCount() > 0) {
                    buscarDatos.close();
                    Cursor cursorCSV = db.rawQuery("SELECT "
                            + TMOnlineTablasSQL.NOMBRE_MANGA + " AS Nombre, "
                            + TMOnlineTablasSQL.TIPO_MANGA + " AS Tipo, "
                            + TMOnlineTablasSQL.BIT_SEGUIR_NO + " AS Siguiendo "
                            + " FROM " + TMOnlineTablasSQL.TABLA_SEGUIR
                            + " ORDER BY " + TMOnlineTablasSQL.NOMBRE_MANGA
                            + " ASC", null);
                    csvWriter.writeNext(cursorCSV.getColumnNames());
                    while (cursorCSV.moveToNext()) {
                        if (cursorCSV.getString(2).equals(String.valueOf(1))) {
                            String siguiendo = cursorCSV.getString(2);
                            siguiendo = siguiendo.replaceAll(String.valueOf(1), "Sí");
                            String[] columnas = {cursorCSV.getString(0), cursorCSV.getString(1), siguiendo};
                            csvWriter.writeNext(columnas);
                        } else {
                            String siguiendo = cursorCSV.getString(2);
                            siguiendo = siguiendo.replaceAll(String.valueOf(0), "No");
                            String[] columnas = {cursorCSV.getString(0), cursorCSV.getString(1), siguiendo};
                            csvWriter.writeNext(columnas);
                        }
                    }
                    csvWriter.close();
                    cursorCSV.close();
                    toastVerde(actividad, "¡Has descargado con éxito tu lista! (La descarga no consume datos).");
                }
                else {
                    toastAzul(actividad, "¡Ups! Actualmente no tienes ningún elemento en tu lista.");
                }
            }catch (Exception sqlException){
                Log.e("TMODescargaError", sqlException.getMessage(), sqlException);
            }
        }
    }

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, TMOnlineTablasSQL.DB_NAME, null, TMOnlineTablasSQL.DB_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TMOnlineTablasSQL.TABLA_PARA_SEGUIR);
            db.execSQL(TMOnlineTablasSQL.TABLA_CAPITULO_LEIDO_SQL);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
    }
}
