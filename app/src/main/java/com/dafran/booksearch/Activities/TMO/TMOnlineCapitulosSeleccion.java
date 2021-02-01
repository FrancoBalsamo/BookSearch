package com.dafran.booksearch.Activities.TMO;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dafran.booksearch.Adaptador.TMOAdapters.TMODatoSeleccionAdapter;
import com.dafran.booksearch.Clases.SeguirManga;
import com.dafran.booksearch.Clases.TMOClases.TMODatosSeleccion;
import com.dafran.booksearch.R;
import com.dafran.booksearch.SQLite.PaginasSQL;
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

public class TMOnlineCapitulosSeleccion extends AppCompatActivity {
    AdView adView;
    TextView tu, manga, online, tvTituloSeleccion;
    private String tipo= "";

    private String nombreManga;
    private String contador;
    private String urlFinal;

    ImageView seguirDato;

    private RecyclerView recyclerView;
    private TMODatoSeleccionAdapter adapter;
    private ArrayList<TMODatosSeleccion> tmoDatosSeleccions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmonline_capitulos_seleccion);
        nombreManga = getIntent().getStringExtra("nombre");
        tipo = getIntent().getStringExtra("tipo");

        LinearLayout ll = (LinearLayout)findViewById(R.id.linearColores);

        seguirDato = (ImageView)findViewById(R.id.seguirDato);

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
                seguirMetodoDato();
            }
        });

        tvTituloSeleccion = (TextView)findViewById(R.id.tvTituloSeleccion);
        tvTituloSeleccion.setText(nombreManga);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TMODatoSeleccionAdapter(tmoDatosSeleccions, TMOnlineCapitulosSeleccion.this);
        recyclerView.setAdapter(adapter);

        Content content = new Content();
        content.execute();

        titulo();
        bannerBookSearh();
    }

    private void seguirMetodoDato(){
        PaginasSQL psql = new PaginasSQL(TMOnlineCapitulosSeleccion.this);
        SeguirManga sm = new SeguirManga();
        sm.setNombre(nombreManga);
        sm.setUrl(urlFinal);
        sm.setContador(contador);
        sm.setValorSeguir(1);
        psql.guardar(sm);//guardamos
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
        }

        @Override
        protected ArrayList<TMODatosSeleccion> doInBackground(Void... voids) {
            String url = getIntent().getStringExtra("valor");
            urlFinal = url;

            tmoDatosSeleccions.clear();
            try {
                Document doc = Jsoup.connect(url).get();
                Log.d("", "doInBackground: "+ doc);

                Elements data = doc.select("li.list-group-item.p-0.bg-light.upload-link");

                for (Element e1 : data) {
                    String numeroCap = "";
                    String urlMan = "";
                    if(e1.select("div.col-10.text-truncate").size() > 0){
                        numeroCap = e1.select("a").get(0).text();
                        numeroCap = numeroCap.replaceAll("\\<.*?\\>", "").trim();
                        contador = numeroCap;
                        if(e1.select("div.col-2.col-sm-1.text-right").size() > 0 ){
                            urlMan = e1.select("a.btn.btn-default.btn-sm").get(0).attr("href");
                            if(urlMan.contains("/paginated")){
                                urlMan.replace("/paginated", "/cascade");
                                tmoDatosSeleccions.add(new TMODatosSeleccion(numeroCap, urlMan));
                            }else{
                                tmoDatosSeleccions.add(new TMODatosSeleccion(numeroCap, urlMan));
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
        tu = (TextView)findViewById(R.id.tvTu);
        manga = (TextView)findViewById(R.id.tvManga);
        online = (TextView)findViewById(R.id.tvOnline);

        tu.setText("TU");
        manga.setText("MANGA");
        online.setText("ONLINE");

        tu.setTextColor(ContextCompat.getColor(TMOnlineCapitulosSeleccion.this, R.color.tmoTitulo));
        manga.setTextColor(ContextCompat.getColor(TMOnlineCapitulosSeleccion.this, R.color.tmoTitulo));
        online.setTextColor(ContextCompat.getColor(TMOnlineCapitulosSeleccion.this, R.color.tmoTitulo));
    }

    private void bannerBookSearh(){
        MobileAds.initialize(TMOnlineCapitulosSeleccion.this, new OnInitializationCompleteListener() {
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
        TMOnlineCapitulosSeleccion.this.finish();
    }
}