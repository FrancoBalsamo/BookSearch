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

public class TMOnlineListaMangasSiguiendo extends AppCompatActivity {
    private ArrayList<SeguirManga> seguirMangaArrayList;
    ListView lvMangaLista;
    TextView tu, manga, online;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tmonline_manga_lista_seleccionado);

        titulo();

        lvMangaLista = (ListView)findViewById(R.id.lvListaMangas);
        seguirMangaArrayList = new PaginasSQL(TMOnlineListaMangasSiguiendo.this).llenarListaMangas();
        lvMangaLista.setAdapter(new TMOnlineListaSeleccionAdaptador(seguirMangaArrayList, TMOnlineListaMangasSiguiendo.this));

        lvMangaLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TMOnlineListaMangasSiguiendo.this, TMOnlineMangaSeleccionadoDeLista.class);
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

        tu.setTextColor(ContextCompat.getColor(TMOnlineListaMangasSiguiendo.this, R.color.tmoTitulo));
        manga.setTextColor(ContextCompat.getColor(TMOnlineListaMangasSiguiendo.this, R.color.tmoTitulo));
        online.setTextColor(ContextCompat.getColor(TMOnlineListaMangasSiguiendo.this, R.color.tmoTitulo));
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        TMOnlineListaMangasSiguiendo.this.finish();
        Toast.makeText(getApplicationContext(),"Vuelvas prontos...", Toast.LENGTH_SHORT).show();
    }
}