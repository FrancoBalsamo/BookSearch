package com.dafran.booksearch.Activities.TMO;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dafran.booksearch.Clases.Conexion;
import com.dafran.booksearch.Clases.TMOClases.TMOItems;
import com.dafran.booksearch.R;
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

public class TMOnline extends AppCompatActivity {
    Button buscar;
    EditText textoBusqueda;
    AdView adView;
    TextView tu, manga, online;
    ImageView lista;
    private RecyclerView rvCapitulosSeleccion;
    private com.dafran.booksearch.Adaptador.TMOAdapters.TMOnline adapter;
    private ArrayList<TMOItems> tmoItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_m_online);

        titulo();

        rvCapitulosSeleccion = findViewById(R.id.rvCapitulosSeleccion);
        lista = (ImageView)findViewById(R.id.btnAbrirLista);

        rvCapitulosSeleccion.setHasFixedSize(true);
        rvCapitulosSeleccion.setLayoutManager(new LinearLayoutManager(this));
        adapter = new com.dafran.booksearch.Adaptador.TMOAdapters.TMOnline(tmoItems, TMOnline.this);
        rvCapitulosSeleccion.setAdapter(adapter);

        textoBusqueda = (EditText)findViewById(R.id.etBuscar);
        buscar = (Button)findViewById(R.id.btnBusqueda);

        textoBusqueda.clearFocus();

        textoBusqueda.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(textoBusqueda.requestFocus()){
                    textoBusqueda.setBackgroundResource(R.drawable.tmo_confoco);
                    textoBusqueda.setTextColor(ContextCompat.getColor(TMOnline.this, R.color.negro));
                }else{
                    textoBusqueda.setBackgroundResource(R.drawable.tmo_sinfoco);
                }
            }
        });

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboardFrom(getApplicationContext(), textoBusqueda);

                if(new Conexion().isOnline(TMOnline.this)){
                    if(TextUtils.isEmpty(textoBusqueda.getText())){
                        Toast.makeText(TMOnline.this, "Debe ingresar el nombre del libro.", Toast.LENGTH_LONG).show();
                        textoBusqueda.setFocusable(true);
                    }else{
                        Content content = new Content();
                        content.execute();
                    }
                }else {
                    Toast.makeText(TMOnline.this, "Se requiere conexión a internet.", Toast.LENGTH_LONG).show();
                }
            }
        });

        lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirLista();
            }
        });

        bannerBookSearh();
    }

    private class Content extends AsyncTask<Void,Void, ArrayList<TMOItems>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<TMOItems> items) {
            super.onPostExecute(items);
            //Actualizar información
            adapter.updateData(items);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected ArrayList<TMOItems> doInBackground(Void... voids) {
            tmoItems.clear();

            String texto = textoBusqueda.getText().toString();
            String arreglo = texto.replace(' ', '+');
            String url = "https://lectortmo.com/library?_page=1&title=" + arreglo;
            try {
                Document doc = Jsoup.connect(url).get();

                Elements data = doc.select("div.row>.element");
                for (Element e : data) {
                    String title = e.select("h4").attr("title"); // nombre del manhwa
                    String imgUrl = e.select("style").first().html().split("url\\('")[1].split("'\\)")[0]; // imagen del manga
                    String detailUrl = e.select("a").attr("href").trim();
                    String tipo = e.select("span.book-type.badge").text();

                    tmoItems.add(new TMOItems(imgUrl, title, detailUrl, tipo));
                }
            }  catch (IOException e) {
                e.printStackTrace();
            }
            return tmoItems;
        }
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean isNullorEmpty(String s ) {
        return s == null || s.trim().isEmpty();
    }

    private void bannerBookSearh(){
        MobileAds.initialize(TMOnline.this, new OnInitializationCompleteListener() {
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
        TMOnline.this.finish();
    }

    private void titulo(){
        tu = (TextView)findViewById(R.id.tvTuSeleccionManga);
        manga = (TextView)findViewById(R.id.tvMangaSeleccionManga);
        online = (TextView)findViewById(R.id.tvOnlineSeleccionManga);

        tu.setText("TU");
        manga.setText("MANGA");
        online.setText("ONLINE");

        tu.setTextColor(ContextCompat.getColor(TMOnline.this, R.color.tmoTitulo));
        manga.setTextColor(ContextCompat.getColor(TMOnline.this, R.color.tmoTitulo));
        online.setTextColor(ContextCompat.getColor(TMOnline.this, R.color.tmoTitulo));
    }

    private void abrirLista(){
        Intent abrir = new Intent(TMOnline.this, Siguiendo.class);
        startActivity(abrir);
    }
}