package com.dafran.booksearch.Activities.TMO;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dafran.booksearch.Adaptador.TMOAdapters.TMOnlineListaSeleccionAdaptador;
import com.dafran.booksearch.Clases.SeguirManga;
import com.dafran.booksearch.R;
import com.dafran.booksearch.SQLite.PaginasSQL;

import java.util.ArrayList;

public class TMOnlineMangasSiguiendo extends AppCompatActivity {
    private ArrayList<SeguirManga>seguirMangaArrayList;
    ListView lv;
    TextView tu, manga, online;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmonline_mangas_siguiendo);

        titulo();

        lv = (ListView)findViewById(R.id.lvListaMangas);
        seguirMangaArrayList = new PaginasSQL(TMOnlineMangasSiguiendo.this).llenarListaMangas();
        lv.setAdapter(new TMOnlineListaSeleccionAdaptador(seguirMangaArrayList, TMOnlineMangasSiguiendo.this));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TMOnlineMangasSiguiendo.this, TMOnlineMangasSiguiendoSeleccion.class);
                intent.putExtra("nombre", seguirMangaArrayList.get(position).getNombre());
                intent.putExtra("url", seguirMangaArrayList.get(position).getUrl());
                intent.putExtra("tipo", seguirMangaArrayList.get(position).getTipo());
                intent.putExtra("urlImagen", seguirMangaArrayList.get(position).getUrlImagen());
                startActivity(intent);
            }
        });
    }

    private void titulo(){
        tu = (TextView)findViewById(R.id.tvMangaListaTu);
        manga = (TextView)findViewById(R.id.tvMangaListaManga);
        online = (TextView)findViewById(R.id.tvMangaListaOnline);

        tu.setText("TU");
        manga.setText("MANGA");
        online.setText("ONLINE");

        tu.setTextColor(ContextCompat.getColor(TMOnlineMangasSiguiendo.this, R.color.tmoTitulo));
        manga.setTextColor(ContextCompat.getColor(TMOnlineMangasSiguiendo.this, R.color.tmoTitulo));
        online.setTextColor(ContextCompat.getColor(TMOnlineMangasSiguiendo.this, R.color.tmoTitulo));
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        TMOnlineMangasSiguiendo.this.finish();
        Toast.makeText(getApplicationContext(),"Vuelvas prontos...", Toast.LENGTH_SHORT).show();
    }
}