package com.dafran.booksearch.Activities.TMO;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dafran.booksearch.Adaptador.TMOAdapters.TMOnlineListaSeleccionAdaptador;
import com.dafran.booksearch.Clases.CSVOpenOffice.CSVWriter;
import com.dafran.booksearch.Clases.TMOClases.TMOnlineSeguirManga;
import com.dafran.booksearch.R;
import com.dafran.booksearch.SQLite.TMOnlineMetodosSQL;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class TMOnlineListaMangasSiguiendo extends AppCompatActivity {
    private ArrayList<TMOnlineSeguirManga> TMOnlineSeguirMangaArrayList;
    ListView lvMangaLista;
    TextView tu, manga, online;
    Button btnDescargarLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tmonline_manga_lista_siguiendo);

        titulo();
        btnDescargarLista = (Button)findViewById(R.id.tmoDescargarLista);
        lvMangaLista = (ListView)findViewById(R.id.lvListaMangas);

        btnDescargarLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pedirPermisoAlmacenamiento()){
                    TMOnlineMetodosSQL tmOnlineMetodosSQL = new TMOnlineMetodosSQL(TMOnlineListaMangasSiguiendo.this);
                    tmOnlineMetodosSQL.descargarLista(TMOnlineListaMangasSiguiendo.this);
                }else {
                    toastRojo("Debe aceptar los permisos de almacenamiento.");
                }
            }
        });

        TMOnlineSeguirMangaArrayList = new TMOnlineMetodosSQL(TMOnlineListaMangasSiguiendo.this).llenarListaMangas();
        lvMangaLista.setAdapter(new TMOnlineListaSeleccionAdaptador(TMOnlineSeguirMangaArrayList, TMOnlineListaMangasSiguiendo.this));
        lvMangaLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TMOnlineListaMangasSiguiendo.this, TMOnlineMangaSeleccionadoDeLista.class);
                intent.putExtra("nombre", TMOnlineSeguirMangaArrayList.get(position).getNombre());
                intent.putExtra("url", TMOnlineSeguirMangaArrayList.get(position).getUrl());
                intent.putExtra("tipo", TMOnlineSeguirMangaArrayList.get(position).getTipo());
                intent.putExtra("urlImagen", TMOnlineSeguirMangaArrayList.get(position).getUrlImagen());
                startActivity(intent);
            }
        });
    }

    public  boolean pedirPermisoAlmacenamiento() {
        if (Build.VERSION.SDK_INT >= 23) {//Corrobora el permiso
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {//Al no tenerlo, lo pide
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //los permisos son aceptados automáticamente en los sdk < 23 durante la instalación
            return true;
        }
    }

    private void titulo(){
        tu = (TextView)findViewById(R.id.tvMangaListaTu);
        manga = (TextView)findViewById(R.id.tvMangaListaManga);
        online = (TextView)findViewById(R.id.tvMangaListaOnline);

        tu.setText("TU");
        manga.setText("MANGA");
        online.setText("ONLINE");

        tu.setTextColor(ContextCompat.getColor(TMOnlineListaMangasSiguiendo.this, R.color.tmoTitulo));
        manga.setTextColor(ContextCompat.getColor(TMOnlineListaMangasSiguiendo.this, R.color.tmoTitulo));
        online.setTextColor(ContextCompat.getColor(TMOnlineListaMangasSiguiendo.this, R.color.tmoTitulo));
    }

    private void toastRojo(String mensaje){
        Toast toast = Toast.makeText(TMOnlineListaMangasSiguiendo.this, mensaje, Toast.LENGTH_LONG);
        View view = toast.getView();
        view.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        TextView toastTextView = view.findViewById(R.id.toastTextView);
        toast.show();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        TMOnlineListaMangasSiguiendo.this.finish();
        Toast.makeText(getApplicationContext(),"Vuelvas prontos...", Toast.LENGTH_SHORT).show();
    }
}