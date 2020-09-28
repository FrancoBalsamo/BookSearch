package com.dafran.booksearch.Activities.Lectulandia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dafran.booksearch.Adaptador.LectulandiaAdapters.LectulandiaInicioAdaptador;
import com.dafran.booksearch.Adaptador.LectulandiaAdapters.LectulandiaSeleccionLibroAdaptador;
import com.dafran.booksearch.Clases.Conexion;
import com.dafran.booksearch.Clases.Lectulandia.LectuPrincipalClase;
import com.dafran.booksearch.Clases.Lectulandia.LectulandiaSeleccionItemsClase;
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

public class LectulandiaSeleccion extends AppCompatActivity {
    AdView adView;

    private RecyclerView recyclerView;
    private LectulandiaSeleccionLibroAdaptador lectulandiaSeleccionLibroAdaptador;
    private ArrayList<LectulandiaSeleccionItemsClase> lectulandiaSeleccionItemsClaseArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectulandia_seleccion);

        recyclerView = findViewById(R.id.rvLEctuSeleccion);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        lectulandiaSeleccionLibroAdaptador = new LectulandiaSeleccionLibroAdaptador(lectulandiaSeleccionItemsClaseArrayList, this);
        recyclerView.setAdapter(lectulandiaSeleccionLibroAdaptador);

        BuscandoDato buscandoDato = new BuscandoDato();
        buscandoDato.execute();

        bannerBookSearh();
    }

    private class BuscandoDato extends AsyncTask<Void,Void, ArrayList<LectulandiaSeleccionItemsClase>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<LectulandiaSeleccionItemsClase> items) {
            super.onPostExecute(items);
            //Actualizar información
            lectulandiaSeleccionLibroAdaptador.updateData(items);
            lectulandiaSeleccionLibroAdaptador.notifyDataSetChanged();
        }

        @Override
        protected ArrayList<LectulandiaSeleccionItemsClase> doInBackground(Void... voids) {
            lectulandiaSeleccionItemsClaseArrayList.clear();
            String datoUrl = getIntent().getStringExtra("url");
            String datoImagen = getIntent().getStringExtra("imagen");
            String datoTitulo = getIntent().getStringExtra("titulo");

            Log.d("", "doInBackground: "+ datoUrl + "-" + datoImagen + "-" + datoTitulo);
            try {
                Document doc = Jsoup.connect(datoUrl).get();
                Elements data = doc.select("div>.content-area");
                for (Element e : data) {
                    String autor = e.getElementById("autor").select("a").text();
                    String generos = e.getElementById("genero").select("a").text();
                    String sinopsis = e.getElementById("sinopsis").select("span").text();
                    String linkPdf = "";
                    if(e.getElementById("downloadContainer").select("a").size() > 0){
                        linkPdf = e.getElementById("downloadContainer").select("a").get(1).attr("href");
                        Log.d("", "doInBackground: "+ linkPdf);
                        lectulandiaSeleccionItemsClaseArrayList.add(new LectulandiaSeleccionItemsClase(datoTitulo, datoImagen, autor,
                                generos, sinopsis, linkPdf));
                    }
                }
            }  catch (IOException e) {
                e.printStackTrace();
            }
            return lectulandiaSeleccionItemsClaseArrayList;
        }
    }

    private void bannerBookSearh(){
        MobileAds.initialize(LectulandiaSeleccion.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) { }
        });
        adView = (AdView)findViewById(R.id.adViewLectuSeleccion);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LectulandiaSeleccion.this.finish();
    }
}