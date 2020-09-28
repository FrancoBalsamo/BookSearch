package com.dafran.booksearch.Activities.Lectulandia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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

import com.dafran.booksearch.Activities.Trantor.TrantorActivity;
import com.dafran.booksearch.Adaptador.LectulandiaAdapters.LectulandiaInicioAdaptador;
import com.dafran.booksearch.Adaptador.TrantorAdapter.TrantorAdapter;
import com.dafran.booksearch.Clases.Conexion;
import com.dafran.booksearch.Clases.Lectulandia.LectuPrincipalClase;
import com.dafran.booksearch.Clases.Trantor.TrantorItems;
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

public class LectulandiaInicio extends AppCompatActivity {
    Button buscar;
    EditText textoBusqueda;

    AdView adView;

    private RecyclerView recyclerView;
    private LectulandiaInicioAdaptador adapter;
    private ArrayList<LectuPrincipalClase> lectuPrincipalClaseArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectulandia_inicio);

        recyclerView = findViewById(R.id.recyclerViewLector);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LectulandiaInicioAdaptador(lectuPrincipalClaseArrayList, this);
        recyclerView.setAdapter(adapter);

        textoBusqueda = (EditText)findViewById(R.id.etLectorBuscar);

        textoBusqueda.clearFocus();

        //CargaInicio cargaInicio = new CargaInicio();
        //cargaInicio.execute();

        textoBusqueda.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(textoBusqueda.requestFocus()){
                    textoBusqueda.setBackgroundResource(R.drawable.trantor_confoco);
                    textoBusqueda.setTextColor(ContextCompat.getColor(LectulandiaInicio.this, R.color.negro));
                }else{
                    textoBusqueda.setBackgroundResource(R.drawable.trantor_sinfoco);
                }
            }
        });

        buscar = (Button)findViewById(R.id.lectorBtn);

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboardFrom(getApplicationContext(), textoBusqueda);
                if(new Conexion().isOnline(LectulandiaInicio.this)){
                    if(TextUtils.isEmpty(textoBusqueda.getText())){
                        Toast.makeText(LectulandiaInicio.this, "Debe ingresar el nombre del libro.", Toast.LENGTH_LONG).show();
                        textoBusqueda.setFocusable(true);
                    }else{
                        BuscandoDato buscandoDato = new BuscandoDato();
                        buscandoDato.execute();
                    }
                }else {
                    Toast.makeText(LectulandiaInicio.this, "Se requiere conexión a internet.", Toast.LENGTH_LONG).show();
                }
            }
        });

        bannerBookSearh();
    }

    private class BuscandoDato extends AsyncTask<Void,Void, ArrayList<LectuPrincipalClase>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<LectuPrincipalClase> items) {
            super.onPostExecute(items);
            //Actualizar información
            adapter.updateData(items);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected ArrayList<LectuPrincipalClase> doInBackground(Void... voids) {
            lectuPrincipalClaseArrayList.clear();
            String texto = textoBusqueda.getText().toString();
            texto = texto.replaceAll(" ", "+");
            String url = "https://www.lectulandia.co/search/" + texto;
            Log.d("", "doInBackground: "+url);;
            try {
                Document doc = Jsoup.connect(url).get();
                Elements data = doc.select("article");
                Log.d("", "doInBackground: " + doc);
                for (Element e : data) {
                    String titulo = e.select("div.span1").select("img").attr("alt");
                    String imgUrl = e.select("div.span1").select("img").attr("src");
                    String urlLink = e.select("div.span1").select("a").attr("href");

                    if(!isNullorEmpty(titulo) && !isNullorEmpty(imgUrl)){
                        imgUrl = "https://trantor.is" + imgUrl; //Add basepath
                        urlLink =  "https://trantor.is" + urlLink; //Add basepath
                        lectuPrincipalClaseArrayList.add(new LectuPrincipalClase(imgUrl, titulo, urlLink));
                        Log.d("items", "titulo: " + titulo + " img: " + imgUrl  + " urlDescarga: " + urlLink);
                    }
                }
            }  catch (IOException e) {
                e.printStackTrace();
            }
            return lectuPrincipalClaseArrayList;
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
        MobileAds.initialize(LectulandiaInicio.this, new OnInitializationCompleteListener() {
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
        LectulandiaInicio.this.finish();
    }
}