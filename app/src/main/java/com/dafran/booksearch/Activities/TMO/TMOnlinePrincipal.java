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
import android.widget.TextView;
import android.widget.Toast;

import com.dafran.booksearch.Adaptador.TMOAdapters.TMOnlinePrincipalAdaptador;
import com.dafran.booksearch.Clases.Conexion;
import com.dafran.booksearch.Clases.TMOClases.TMOItems;
import com.dafran.booksearch.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class TMOnlinePrincipal extends AppCompatActivity {
    private Button buscar;
    private EditText textoBusqueda;
    private TextView tu, manga, online;
    private Button lista;
    private RecyclerView rvTMOPrincipal;
    private TMOnlinePrincipalAdaptador principalAdaptador;
    private ArrayList<TMOItems> tmoItemsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tmonline_pincipal);

        titulo();

        textoBusqueda = (EditText)findViewById(R.id.etBuscarTMOPrincipal);
        buscar = (Button)findViewById(R.id.btnBusquedaTMOPrincipal);
        lista = (Button)findViewById(R.id.btnAbrirListaTMOPrincipal);
        rvTMOPrincipal = findViewById(R.id.rvTMOPrincipal);

        rvTMOPrincipal.setHasFixedSize(true);
        rvTMOPrincipal.setLayoutManager(new LinearLayoutManager(this));
        principalAdaptador = new TMOnlinePrincipalAdaptador(tmoItemsArrayList, TMOnlinePrincipal.this);
        rvTMOPrincipal.setAdapter(principalAdaptador);
        textoBusqueda.clearFocus();
        textoBusqueda.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(textoBusqueda.requestFocus()){
                    textoBusqueda.setBackgroundResource(R.drawable.tmo_confoco);
                    textoBusqueda.setTextColor(ContextCompat.getColor(TMOnlinePrincipal.this, R.color.negro));
                }else{
                    textoBusqueda.setBackgroundResource(R.drawable.tmo_sinfoco);
                }
            }
        });

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboardFrom(getApplicationContext(), textoBusqueda);

                if(new Conexion().isOnline(TMOnlinePrincipal.this)){
                    if(TextUtils.isEmpty(textoBusqueda.getText())){
                        Toast.makeText(TMOnlinePrincipal.this, "El campo no puede estar vacío.", Toast.LENGTH_LONG).show();
                        textoBusqueda.setFocusable(true);
                    }else{
                        TMOPrincipalCargarBuscador TMOPrincipalCargarBuscador = new TMOPrincipalCargarBuscador();
                        TMOPrincipalCargarBuscador.execute();
                    }
                }else {
                    Toast.makeText(TMOnlinePrincipal.this, "Se requiere conexión a internet.", Toast.LENGTH_LONG).show();
                }
            }
        });

        lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirLista();
            }
        });
    }

    private class TMOPrincipalCargarBuscador extends AsyncTask<Void,Void, ArrayList<TMOItems>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<TMOItems> items) {
            super.onPostExecute(items);
            //Actualizar información
            principalAdaptador.updateData(items);
            principalAdaptador.notifyDataSetChanged();
        }

        @Override
        protected ArrayList<TMOItems> doInBackground(Void... voids) {
            tmoItemsArrayList.clear();

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

                    tmoItemsArrayList.add(new TMOItems(imgUrl, title, detailUrl, tipo));
                }
            }  catch (IOException e) {
                e.printStackTrace();
            }
            return tmoItemsArrayList;
        }
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void titulo(){
        tu = (TextView)findViewById(R.id.tvTuPrincipal);
        manga = (TextView)findViewById(R.id.tvMangaPrincipal);
        online = (TextView)findViewById(R.id.tvOnlinePrincipal);

        tu.setText("TU");
        manga.setText("MANGA");
        online.setText("ONLINE");

        tu.setTextColor(ContextCompat.getColor(TMOnlinePrincipal.this, R.color.tmoTitulo));
        manga.setTextColor(ContextCompat.getColor(TMOnlinePrincipal.this, R.color.tmoTitulo));
        online.setTextColor(ContextCompat.getColor(TMOnlinePrincipal.this, R.color.tmoTitulo));
    }

    private void abrirLista(){
        Intent abrir = new Intent(TMOnlinePrincipal.this, TMOnlineListaMangasSiguiendo.class);
        startActivity(abrir);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        TMOnlinePrincipal.this.finish();
    }
}