package com.dafran.booksearch.Activities.Trantor;

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

import com.dafran.booksearch.Adaptador.TrantorAdapter.TrantorAdapter;
import com.dafran.booksearch.Clases.Conexion;
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

public class TrantorActivity extends AppCompatActivity {
    Button buscar;
    EditText textoBusqueda;

    AdView adView;

    private RecyclerView recyclerView;
    private TrantorAdapter adapter;
    private ArrayList<TrantorItems> trantorItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trantor);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TrantorAdapter(trantorItems, this);
        recyclerView.setAdapter(adapter);

        textoBusqueda = (EditText)findViewById(R.id.etBuscar);

        textoBusqueda.clearFocus();

        //CargaInicio cargaInicio = new CargaInicio();
        //cargaInicio.execute();

        textoBusqueda.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(textoBusqueda.requestFocus()){
                    textoBusqueda.setBackgroundResource(R.drawable.trantor_confoco);
                    textoBusqueda.setTextColor(ContextCompat.getColor(TrantorActivity.this, R.color.negro));
                }else{
                    textoBusqueda.setBackgroundResource(R.drawable.trantor_sinfoco);
                }
            }
        });

        buscar = (Button)findViewById(R.id.btnBusqueda);

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboardFrom(getApplicationContext(), textoBusqueda);
                if(new Conexion().isOnline(TrantorActivity.this)){
                    if(TextUtils.isEmpty(textoBusqueda.getText())){
                        Toast.makeText(TrantorActivity.this, "Debe ingresar el nombre del libro.", Toast.LENGTH_LONG).show();
                        textoBusqueda.setFocusable(true);
                    }else{
                        BuscandoDato buscandoDato = new BuscandoDato();
                        buscandoDato.execute();
                    }
                }else {
                    Toast.makeText(TrantorActivity.this, "Se requiere conexión a internet.", Toast.LENGTH_LONG).show();
                }
            }
        });

        bannerBookSearh();
    }

    private class BuscandoDato extends AsyncTask<Void,Void, ArrayList<TrantorItems>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<TrantorItems> items) {
            super.onPostExecute(items);
            //Actualizar información
            adapter.updateData(items);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected ArrayList<TrantorItems> doInBackground(Void... voids) {
            trantorItems.clear();
            String texto = textoBusqueda.getText().toString();
            texto = texto.replaceAll(" ", "+");
            String url = "https://trantor.is/search/?q=" + texto;
            Log.d("", "doInBackground: "+url);;
            try {
                Document doc = Jsoup.connect(url).get();
                Log.d("", "doInBackground: "+doc);;
                Elements data = doc.select("div.row");
                for (Element e : data) {
                    String title = e.select("div.span1").select("img").attr("alt");
                    String imgUrl = e.select("div.span1").select("img").attr("src");
                    String detailUrl = e.select("div.span1").select("a").attr("href");
                    String urlDescarga = e.select("div.span3").select("a").attr("href");

                    if(!isNullorEmpty(title) && !isNullorEmpty(imgUrl)){
                        imgUrl = "https://trantor.is" + imgUrl; //Add basepath
                        detailUrl =  "https://trantor.is" + detailUrl; //Add basepath
                        urlDescarga =  "https://trantor.is" + urlDescarga; //Add basepath
                        trantorItems.add(new TrantorItems(imgUrl, title, detailUrl, urlDescarga));
                        Log.d("items", "title: " + title + " img: " + imgUrl  + " urlDescarga: " + urlDescarga);
                    }
                }
            }  catch (IOException e) {
                e.printStackTrace();
            }
            return trantorItems;
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
        MobileAds.initialize(TrantorActivity.this, new OnInitializationCompleteListener() {
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
        TrantorActivity.this.finish();
    }
}