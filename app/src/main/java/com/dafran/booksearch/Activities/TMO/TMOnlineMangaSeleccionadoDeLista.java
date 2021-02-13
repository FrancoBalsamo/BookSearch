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
import com.dafran.booksearch.Clases.TMOClases.TMOnlineSeguirManga;
import com.dafran.booksearch.Clases.TMOClases.TMODatosSeleccion;
import com.dafran.booksearch.R;
import com.dafran.booksearch.SQLite.TMOnlineMetodosSQL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class TMOnlineMangaSeleccionadoDeLista extends AppCompatActivity {
    private TextView tu, manga, online, tvTituloSeleccion;
    private int cont;
    private String nombreManga;
    private String capitulo_manga;
    private String tipo = "";
    private String url = "";
    private Button listaSeguir, listaDejar;
    private RecyclerView rvListaCapitulosDeLista;
    private TMOnlineMangaSeleccionAdaptador tmOnlineMangaSeleccionAdaptador;
    private ArrayList<TMODatosSeleccion> tmoDatosSeleccionArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tmonline_lista_mangas_seleccionado);

        titulo();
        nombreManga = getIntent().getStringExtra("nombre");
        tipo = getIntent().getStringExtra("tipo");
        url = getIntent().getStringExtra("url");
        final String imgUrl = getIntent().getStringExtra("urlImagen");

        LinearLayout ll = (LinearLayout)findViewById(R.id.linearColoresDeSeleccion);

        listaSeguir = (Button)findViewById(R.id.seguirDatoListaSeleccion);
        listaDejar = (Button)findViewById(R.id.dejarDatoListaSeleccion);

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

        listaSeguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seguirMetodoDato(TMOnlineMangaSeleccionadoDeLista.this, imgUrl, url);
            }
        });

        listaDejar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dejarMetodoDaato();
            }
        });

        tvTituloSeleccion = (TextView)findViewById(R.id.tvTituloSeleccionLista);
        tvTituloSeleccion.setText(nombreManga);

        rvListaCapitulosDeLista = findViewById(R.id.rvCapitulosListaSeleccion);

        rvListaCapitulosDeLista.setHasFixedSize(true);
        rvListaCapitulosDeLista.setLayoutManager(new LinearLayoutManager(this));
        tmOnlineMangaSeleccionAdaptador = new TMOnlineMangaSeleccionAdaptador(tmoDatosSeleccionArrayList, TMOnlineMangaSeleccionadoDeLista.this, nombreManga, capitulo_manga);
        rvListaCapitulosDeLista.setAdapter(tmOnlineMangaSeleccionAdaptador);

        CargarRecyclerViewConDatosDeLaLista cargarRecyclerViewConDatosDeLaLista = new CargarRecyclerViewConDatosDeLaLista();
        cargarRecyclerViewConDatosDeLaLista.execute();

        actualizarAutomaticamenteCantidadCapitulos(TMOnlineMangaSeleccionadoDeLista.this);
    }

    private void seguirMetodoDato(Context actividad, String imagen, String direccion){
        TMOnlineMetodosSQL psql = new TMOnlineMetodosSQL(actividad);
        TMOnlineSeguirManga sm = new TMOnlineSeguirManga();
        sm.setNombre(nombreManga);
        sm.setUrl(direccion);
        sm.setUrlImagen(imagen);
        sm.setContador(cont+"");
        sm.setValorSeguir(1);
        sm.setTipo(tipo);
        psql.validarGuardado(actividad, nombreManga, sm);
    }

    private void dejarMetodoDaato(){
        TMOnlineMetodosSQL TMOnlineMetodosSQL = new TMOnlineMetodosSQL(TMOnlineMangaSeleccionadoDeLista.this);
        TMOnlineSeguirManga sm = new TMOnlineSeguirManga();
        sm.setValorSeguir(0);
        TMOnlineMetodosSQL.validarUpdateEstadoSiguiendo(TMOnlineMangaSeleccionadoDeLista.this, nombreManga, sm);
    }

    private void actualizarAutomaticamenteCantidadCapitulos(Context actividad){
        TMOnlineMetodosSQL TMOnlineMetodosSQL = new TMOnlineMetodosSQL(actividad);
        TMOnlineSeguirManga sm = new TMOnlineSeguirManga();
        sm.setContador(cont+"");
        TMOnlineMetodosSQL.actualizadorDeCapitulos(nombreManga, sm);
    }

    private void titulo(){
        tu = (TextView)findViewById(R.id.tvMangaSeleccionTuLista);
        manga = (TextView)findViewById(R.id.tvMangaSeleccionMangaLista);
        online = (TextView)findViewById(R.id.tvMangaSeleccionOnlineLista);

        tu.setText("TU");
        manga.setText("MANGA");
        online.setText("ONLINE");

        tu.setTextColor(ContextCompat.getColor(TMOnlineMangaSeleccionadoDeLista.this, R.color.tmoTitulo));
        manga.setTextColor(ContextCompat.getColor(TMOnlineMangaSeleccionadoDeLista.this, R.color.tmoTitulo));
        online.setTextColor(ContextCompat.getColor(TMOnlineMangaSeleccionadoDeLista.this, R.color.tmoTitulo));
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        TMOnlineMangaSeleccionadoDeLista.this.finish();
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
            tmOnlineMangaSeleccionAdaptador.updateData(items);
            tmOnlineMangaSeleccionAdaptador.notifyDataSetChanged();

            items.size();
            cont = items.size();
        }

        @Override
        protected ArrayList<TMODatosSeleccion> doInBackground(Void... voids) {
            String url = getIntent().getStringExtra("url");
            tmoDatosSeleccionArrayList.clear();
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
                                tmoDatosSeleccionArrayList.add(new TMODatosSeleccion(numeroCap, urlMan, nombreManga));
                            }else{
                                tmoDatosSeleccionArrayList.add(new TMODatosSeleccion(numeroCap, urlMan, nombreManga));
                            }
                        }
                    }
                }
            }  catch (IOException e) {
                e.printStackTrace();
            }
            return tmoDatosSeleccionArrayList;
        }
    }
}