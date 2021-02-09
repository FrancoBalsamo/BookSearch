package com.dafran.booksearch.Activities.TMO;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dafran.booksearch.Adaptador.TMOAdapters.TMOnlineMangaSeleccionAdaptador;
import com.dafran.booksearch.Clases.SeguirManga;
import com.dafran.booksearch.Clases.TMOClases.TMODatosSeleccion;
import com.dafran.booksearch.R;
import com.dafran.booksearch.SQLite.PaginasSQL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class TMOnlineMangasSiguiendoSeleccion extends AppCompatActivity {
    private TextView tu, manga, online, tvTituloSeleccion;
    private int cont;
    private String nombreManga;
    private String capitulo_manga;
    private String tipo = "";
    private String url = "";
    private Button seguirDato, dejarDato;
    private RecyclerView recyclerView;
    private TMOnlineMangaSeleccionAdaptador adapter;
    private ArrayList<TMODatosSeleccion> tmoDatosSeleccions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_m_online_mangas_siguiendo_seleccion);

        titulo();
        nombreManga = getIntent().getStringExtra("nombre");
        tipo = getIntent().getStringExtra("tipo");
        url = getIntent().getStringExtra("url");
        final String imgUrl = getIntent().getStringExtra("urlImagen");

        LinearLayout ll = (LinearLayout)findViewById(R.id.linearColoresDeSeleccion);

        seguirDato = (Button)findViewById(R.id.seguirDatoListaSeleccion);
        dejarDato = (Button)findViewById(R.id.dejarDatoListaSeleccion);

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
                seguirMetodoDato(TMOnlineMangasSiguiendoSeleccion.this, imgUrl, url);
            }
        });

        dejarDato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dejarMetodoDaato();
            }
        });

        tvTituloSeleccion = (TextView)findViewById(R.id.tvTituloSeleccionLista);
        tvTituloSeleccion.setText(nombreManga);

        recyclerView = findViewById(R.id.rvCapitulosListaSeleccion);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TMOnlineMangaSeleccionAdaptador(tmoDatosSeleccions, TMOnlineMangasSiguiendoSeleccion.this, nombreManga, capitulo_manga);
        recyclerView.setAdapter(adapter);

        CargarRecyclerViewConDatosDeLaLista cargarRecyclerViewConDatosDeLaLista = new CargarRecyclerViewConDatosDeLaLista();
        cargarRecyclerViewConDatosDeLaLista.execute();

        actualizarAutomaticamenteCantidadCapitulos(TMOnlineMangasSiguiendoSeleccion.this);
    }

    private void seguirMetodoDato(Context actividad, String imagen, String direccion){
        PaginasSQL psql = new PaginasSQL(actividad);
        SeguirManga sm = new SeguirManga();
        sm.setNombre(nombreManga);
        sm.setUrl(direccion);
        sm.setUrlImagen(imagen);
        sm.setContador(cont+"");
        sm.setValorSeguir(1);
        sm.setTipo(tipo);
        psql.validarGuardado(actividad, nombreManga, sm);
    }

    private void dejarMetodoDaato(){
        PaginasSQL paginasSQL = new PaginasSQL(TMOnlineMangasSiguiendoSeleccion.this);
        SeguirManga sm = new SeguirManga();
        sm.setValorSeguir(0);
        paginasSQL.validarUpdateEstadoSiguiendo(TMOnlineMangasSiguiendoSeleccion.this, nombreManga, sm);
    }

    private void actualizarAutomaticamenteCantidadCapitulos(Context actividad){
        PaginasSQL paginasSQL = new PaginasSQL(actividad);
        SeguirManga sm = new SeguirManga();
        sm.setContador(cont+"");
        paginasSQL.actualizadorDeCapitulos(nombreManga, sm);
    }

    private void titulo(){
        tu = (TextView)findViewById(R.id.tvMangaSeleccionTuLista);
        manga = (TextView)findViewById(R.id.tvMangaSeleccionMangaLista);
        online = (TextView)findViewById(R.id.tvMangaSeleccionOnlineLista);

        tu.setText("TU");
        manga.setText("MANGA");
        online.setText("ONLINE");

        tu.setTextColor(ContextCompat.getColor(TMOnlineMangasSiguiendoSeleccion.this, R.color.tmoTitulo));
        manga.setTextColor(ContextCompat.getColor(TMOnlineMangasSiguiendoSeleccion.this, R.color.tmoTitulo));
        online.setTextColor(ContextCompat.getColor(TMOnlineMangasSiguiendoSeleccion.this, R.color.tmoTitulo));
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        TMOnlineMangasSiguiendoSeleccion.this.finish();
    }

    private class CargarRecyclerViewConDatosDeLaLista extends AsyncTask<Void,Void, ArrayList<TMODatosSeleccion>> {

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
            String url = getIntent().getStringExtra("url");
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
                        capitulo_manga = numeroCap;
                        if(e1.select("div.col-2.col-sm-1.text-right").size() > 0 ){
                            urlMan = e1.select("a.btn.btn-default.btn-sm").get(0).attr("href");
                            if(urlMan.contains("/paginated")){
                                urlMan.replace("/paginated", "/cascade");
                                tmoDatosSeleccions.add(new TMODatosSeleccion(numeroCap, urlMan, nombreManga));
                            }else{
                                tmoDatosSeleccions.add(new TMODatosSeleccion(numeroCap, urlMan, nombreManga));
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
}