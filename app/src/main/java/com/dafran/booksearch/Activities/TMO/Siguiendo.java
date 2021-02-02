package com.dafran.booksearch.Activities.TMO;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.dafran.booksearch.Adaptador.TMOAdapters.TMOnlineListaSeleccionAdaptador;
import com.dafran.booksearch.Clases.SeguirManga;
import com.dafran.booksearch.R;
import com.dafran.booksearch.SQLite.PaginasSQL;
import com.dafran.booksearch.SQLite.PaginasTabla;

import java.util.ArrayList;
import java.util.List;

public class Siguiendo extends AppCompatActivity {
    private ArrayList<SeguirManga>seguirMangaArrayList;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siguiendo);

        lv = (ListView)findViewById(R.id.lvListaMangas);
        seguirMangaArrayList = new PaginasSQL(Siguiendo.this).llenarListaMangas(1);
        lv.setAdapter(new TMOnlineListaSeleccionAdaptador(seguirMangaArrayList,Siguiendo.this));

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Siguiendo.this.finish();
        Toast.makeText(getApplicationContext(),"Vuelvas prontos...", Toast.LENGTH_SHORT).show();
    }
}