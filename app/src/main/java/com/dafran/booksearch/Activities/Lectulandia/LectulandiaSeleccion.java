package com.dafran.booksearch.Activities.Lectulandia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.dafran.booksearch.Adaptador.LectulandiaAdapters.LectulandiaSeleccionLibroAdaptador;
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
                Elements data = doc.select("article");
                Log.d("", "doInBackground: " + doc);
                for (Element e : data) {
                    String imgUrl = e.select("article").select("img").attr("src");
                    String titulo = e.select("article").select("img").attr("title");
                    String urlLink = e.select("article").select("a").attr("href");
                    Log.d("", "doInBackground: " + urlLink);
                    if(!isNullorEmpty(titulo) && !isNullorEmpty(imgUrl)){
                        urlLink =  "https://www.lectulandia.co" + urlLink;
                        lectulandiaSeleccionItemsClaseArrayList.add(new LectulandiaSeleccionItemsClase("", "", "", imgUrl, titulo, urlLink));
                        Log.d("items", "titulo: " + titulo + " img: " + imgUrl  + " urlDescarga: " + urlLink);
                    }
                }
            }  catch (IOException e) {
                e.printStackTrace();
            }
            return lectulandiaSeleccionItemsClaseArrayList;
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
        MobileAds.initialize(LectulandiaSeleccion.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) { }
        });
        adView = (AdView)findViewById(R.id.adViewLector);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LectulandiaSeleccion.this.finish();
    }
}