package com.dafran.booksearch.Activities.TMO;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dafran.booksearch.Adaptador.TMOAdapters.TMOnlineMangaSeleccionAdaptador;
import com.dafran.booksearch.Clases.SeguirManga;
import com.dafran.booksearch.Clases.TMOClases.TMODatosSeleccion;
import com.dafran.booksearch.R;
import com.dafran.booksearch.SQLite.PaginasSQL;
import com.dafran.booksearch.SQLite.PaginasTabla;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class TMOnlineMangaSeleccion extends AppCompatActivity {
    private AdView adView;
    private TextView tu, manga, online, tvTituloSeleccion;
    private String tipo= "";
    private int cont;
    private String nombreManga;
    private Button seguirDato, dejarDato;
    private RecyclerView recyclerView;
    private TMOnlineMangaSeleccionAdaptador adapter;
    private ArrayList<TMODatosSeleccion> tmoDatosSeleccions = new ArrayList<>();
    private ArrayList<SeguirManga> seguirMangaArrayList = new ArrayList<>();
    private SeguirManga seguirManga = new SeguirManga();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmonline_manga_seleccion);
        nombreManga = getIntent().getStringExtra("nombre");
        tipo = getIntent().getStringExtra("tipo");
        final String urlImagen = getIntent().getStringExtra("urlImagen");

        LinearLayout ll = (LinearLayout)findViewById(R.id.linearColores);

        seguirDato = (Button)findViewById(R.id.seguirDato);
        dejarDato = (Button)findViewById(R.id.dejarDato);

        String consulta = "SELECT * FROM " + PaginasTabla.TABLA_SEGUIR + " WHERE " + PaginasTabla.NOMBRE_MANGA + "='" + nombreManga + "''";

        if(tipo.contains("MANGA")){
            ll.setBackgroundColor(getResources().getColor(R.color.tmoManga));
        }else if(tipo.contains("MANHWA")){
            ll.setBackgroundColor(getResources().getColor(R.color.tmoManhwa));
        }else if(tipo.contains("MANHUA")){
            ll.setBackgroundColor(getResources().getColor(R.color.tmoManhua));
        }else if(tipo.contains("NOVELA")){
            ll.setBackgroundColor(getResources().getColor(R.color.tmoNovela));
        }else if(tipo.contains("ONE SHOT")){
            ll.setBackgroundColor(getResources().getColor(R.color.tmoOneShot));
        }else if(tipo.contains("DOUJINSHI")){
            ll.setBackgroundColor(getResources().getColor(R.color.tmoDou));
        }else if(tipo.contains("OEL")){
            ll.setBackgroundColor(getResources().getColor(R.color.tmoOel));
        }

        seguirDato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seguirMetodoDato(urlImagen);
            }
        });

        dejarDato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dejarMetodoDaato();
            }
        });

        tvTituloSeleccion = (TextView)findViewById(R.id.tvTituloSeleccion);
        tvTituloSeleccion.setText(nombreManga);

        recyclerView = findViewById(R.id.rvCapitulosSeleccion);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TMOnlineMangaSeleccionAdaptador(tmoDatosSeleccions, TMOnlineMangaSeleccion.this);
        recyclerView.setAdapter(adapter);

        Content content = new Content();
        content.execute();

        titulo();
        bannerBookSearh();
    }

    private void seguirMetodoDato(String urlImagen){
        String url = getIntent().getStringExtra("valor");
        PaginasSQL psql = new PaginasSQL(TMOnlineMangaSeleccion.this);
        SeguirManga sm = new SeguirManga();
        sm.setNombre(nombreManga);
        sm.setUrl(url);
        sm.setUrlImagen(urlImagen);
        sm.setContador(cont+"");
        sm.setValorSeguir(1);
        psql.validar(sm, this, nombreManga);
    }

    private void dejarMetodoDaato(){
        PaginasSQL paginasSQL = new PaginasSQL(TMOnlineMangaSeleccion.this);
        seguirManga.setValorSeguir(0);
        paginasSQL.validar(seguirManga, TMOnlineMangaSeleccion.this, nombreManga);
    }

    private class Content extends AsyncTask<Void,Void, ArrayList<TMODatosSeleccion>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<TMODatosSeleccion> items) {
            super.onPostExecute(items);
            //Actualizar información
            adapter.updateData(items);
            adapter.notifyDataSetChanged();

            items.size();
            cont = items.size();
        }

        @Override
        protected ArrayList<TMODatosSeleccion> doInBackground(Void... voids) {
            String url = getIntent().getStringExtra("valor");
            tmoDatosSeleccions.clear();
            try {
                Document doc = Jsoup.connect(url).get();
                Elements data = doc.select("li.list-group-item.p-0.bg-light.upload-link");
                for (Element e1 : data) {
                    String numeroCap = "";
                    String urlMan = "";
                    if(e1.select("div.col-10.text-truncate").size() > 0){
                        numeroCap = e1.select("a").get(0).text();
                        numeroCap = numeroCap.replaceAll("\\<.*?\\>", "").trim();
                        if(e1.select("div.col-2.col-sm-1.text-right").size() > 0 ){
                            urlMan = e1.select("a.btn.btn-default.btn-sm").get(0).attr("href");
                            if(urlMan.contains("/paginated")){
                                urlMan.replace("/paginated", "/cascade");
                                tmoDatosSeleccions.add(new TMODatosSeleccion(numeroCap, urlMan, "urlImagen"));
                            }else{
                                tmoDatosSeleccions.add(new TMODatosSeleccion(numeroCap, urlMan, "urlImagen"));
                            }
                        }
                    }
                }
            }  catch (IOException e) {
                e.printStackTrace();
            }
            return tmoDatosSeleccions;
        }
    }

    private void titulo(){
        tu = (TextView)findViewById(R.id.tvMangaSeleccionTu);
        manga = (TextView)findViewById(R.id.tvMangaSeleccionManga);
        online = (TextView)findViewById(R.id.tvMangaSeleccionOnline);

        tu.setText("TU");
        manga.setText("MANGA");
        online.setText("ONLINE");

        tu.setTextColor(ContextCompat.getColor(TMOnlineMangaSeleccion.this, R.color.tmoTitulo));
        manga.setTextColor(ContextCompat.getColor(TMOnlineMangaSeleccion.this, R.color.tmoTitulo));
        online.setTextColor(ContextCompat.getColor(TMOnlineMangaSeleccion.this, R.color.tmoTitulo));
    }

    private void bannerBookSearh(){
        MobileAds.initialize(TMOnlineMangaSeleccion.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) { }
        });
        adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        TMOnlineMangaSeleccion.this.finish();
    }
}