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
import com.dafran.booksearch.SQLite.TMOSQL.TMOnlineMetodosSQL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class TMOnlineMangaSeleccionado extends AppCompatActivity {
    private TextView tu, manga, online, tvTituloSeleccion;
    private int cont;
    private String nombreManga;
    private String numero_capitulo;
    private String url = "";
    private String coloresSeleccion = "";
    private Button mangaElegidoSeguir, mangaElegidoDejar;
    private RecyclerView rvMangaSeleccionadoCapitulos;
    private TMOnlineMangaSeleccionAdaptador tmOnlineMangaSeleccionAdaptador;
    private ArrayList<TMODatosSeleccion> tmoDatosSeleccionArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tmonline_manga_elegido);
        nombreManga = getIntent().getStringExtra("nombre");
        coloresSeleccion = getIntent().getStringExtra("tipo");
        url = getIntent().getStringExtra("valor");
        final String urlImagen = getIntent().getStringExtra("urlImagen");

        LinearLayout ll = (LinearLayout)findViewById(R.id.linearColores);

        mangaElegidoSeguir = (Button)findViewById(R.id.mangaElegidoSeguir);
        mangaElegidoDejar = (Button)findViewById(R.id.mangaElegidoDejar);

        if(coloresSeleccion.contains("MANGA")){
            ll.setBackgroundColor(getResources().getColor(R.color.tmoManga));
        }else if(coloresSeleccion.contains("MANHWA")){
            ll.setBackgroundColor(getResources().getColor(R.color.tmoManhwa));
        }else if(coloresSeleccion.contains("MANHUA")){
            ll.setBackgroundColor(getResources().getColor(R.color.tmoManhua));
        }else if(coloresSeleccion.contains("NOVELA")){
            ll.setBackgroundColor(getResources().getColor(R.color.tmoNovela));
        }else if(coloresSeleccion.contains("ONE SHOT")){
            ll.setBackgroundColor(getResources().getColor(R.color.tmoOneShot));
        }else if(coloresSeleccion.contains("DOUJINSHI")){
            ll.setBackgroundColor(getResources().getColor(R.color.tmoDou));
        }else if(coloresSeleccion.contains("OEL")){
            ll.setBackgroundColor(getResources().getColor(R.color.tmoOel));
        }

        mangaElegidoSeguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seguirMetodoDato(TMOnlineMangaSeleccionado.this, urlImagen, url);
            }
        });

        mangaElegidoDejar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dejarMetodoDaato();
            }
        });

        tvTituloSeleccion = (TextView)findViewById(R.id.tvTituloSeleccion);
        tvTituloSeleccion.setText(nombreManga);

        rvMangaSeleccionadoCapitulos = findViewById(R.id.rvMangaSeleccionadoCapitulos);

        rvMangaSeleccionadoCapitulos.setHasFixedSize(true);
        rvMangaSeleccionadoCapitulos.setLayoutManager(new LinearLayoutManager(this));
        tmOnlineMangaSeleccionAdaptador = new TMOnlineMangaSeleccionAdaptador(tmoDatosSeleccionArrayList, TMOnlineMangaSeleccionado.this, nombreManga, numero_capitulo);
        rvMangaSeleccionadoCapitulos.setAdapter(tmOnlineMangaSeleccionAdaptador);

        CapitulosPorMangaSeleccionado capitulosPorMangaSeleccionado = new CapitulosPorMangaSeleccionado();
        capitulosPorMangaSeleccionado.execute();

        titulo();
        actualizarAutomaticamenteCantidadCapitulos(TMOnlineMangaSeleccionado.this);
    }

    private void seguirMetodoDato(Context actividad, String imagen, String direccion){
        TMOnlineMetodosSQL psql = new TMOnlineMetodosSQL(actividad);
        TMOnlineSeguirManga sm = new TMOnlineSeguirManga();
        sm.setNombre(nombreManga);
        sm.setUrl(direccion);
        sm.setUrlImagen(imagen);
        sm.setContador(cont+"");
        sm.setValorSeguir(1);
        sm.setTipo(coloresSeleccion);
        psql.validarGuardado(actividad, nombreManga, sm);
    }

    private void dejarMetodoDaato(){
        TMOnlineMetodosSQL TMOnlineMetodosSQL = new TMOnlineMetodosSQL(TMOnlineMangaSeleccionado.this);
        TMOnlineSeguirManga sm = new TMOnlineSeguirManga();
        sm.setValorSeguir(0);
        TMOnlineMetodosSQL.validarUpdateEstadoSiguiendo(TMOnlineMangaSeleccionado.this, nombreManga, sm);
    }

    private void actualizarAutomaticamenteCantidadCapitulos(Context actividad){
        TMOnlineMetodosSQL TMOnlineMetodosSQL = new TMOnlineMetodosSQL(actividad);
        TMOnlineSeguirManga sm = new TMOnlineSeguirManga();
        sm.setContador(cont+"");
        TMOnlineMetodosSQL.actualizadorDeCapitulos(nombreManga, sm);
    }

    private class CapitulosPorMangaSeleccionado extends AsyncTask<Void,Void, ArrayList<TMODatosSeleccion>> {

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
            String url = getIntent().getStringExtra("valor");
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
                        numero_capitulo = numeroCap;
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

    private void titulo(){
        tu = (TextView)findViewById(R.id.tvMangaSeleccionTu);
        manga = (TextView)findViewById(R.id.tvMangaSeleccionManga);
        online = (TextView)findViewById(R.id.tvMangaSeleccionOnline);

        tu.setText("TU");
        manga.setText("MANGA");
        online.setText("ONLINE");

        tu.setTextColor(ContextCompat.getColor(TMOnlineMangaSeleccionado.this, R.color.tmoTitulo));
        manga.setTextColor(ContextCompat.getColor(TMOnlineMangaSeleccionado.this, R.color.tmoTitulo));
        online.setTextColor(ContextCompat.getColor(TMOnlineMangaSeleccionado.this, R.color.tmoTitulo));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        TMOnlineMangaSeleccionado.this.finish();
    }
}