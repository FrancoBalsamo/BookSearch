package com.dafran.booksearch.Activities.TMO;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.dafran.booksearch.Adaptador.TMOAdapters.TMOnlineLectorAdaptador;
import com.dafran.booksearch.Clases.TMOClases.TMOLectorClase;
import com.dafran.booksearch.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TimerTask;

public class TMOnlineLector extends AppCompatActivity {
    private RecyclerView rvLector;
    private TMOnlineLectorAdaptador tmOnlineLectorAdaptador;
    private ArrayList<TMOLectorClase> tmoLectorClaseArrayList = new ArrayList<>();
    private String url = "";
    private TimerTask _timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tmonline_lector_actividad);

        url = getIntent().getStringExtra("url");

        rvLector = findViewById(R.id.rvLector);

        ImagenesParaCargarElRecycler imagenesParaCargarElRecycler = new ImagenesParaCargarElRecycler();
        imagenesParaCargarElRecycler.execute();

        rvLector.setHasFixedSize(true);
        rvLector.setLayoutManager(new LinearLayoutManager(this));
        tmOnlineLectorAdaptador = new TMOnlineLectorAdaptador(tmoLectorClaseArrayList, TMOnlineLector.this);
        rvLector.setAdapter(tmOnlineLectorAdaptador);
    }

    private class ImagenesParaCargarElRecycler extends AsyncTask<Void,Void, ArrayList<TMOLectorClase>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<TMOLectorClase> items) {
            super.onPostExecute(items);
            //Actualizar información
            tmOnlineLectorAdaptador.updateData(items);
            tmOnlineLectorAdaptador.notifyDataSetChanged();
        }

        @Override
        protected ArrayList<TMOLectorClase> doInBackground(Void... voids) {

            tmoLectorClaseArrayList.clear();
            try {
                String nuevaUrl = Jsoup.connect(url).followRedirects(true).execute().url().toExternalForm();
                Log.d("Items", "Url: " + nuevaUrl);

                if(nuevaUrl.contains("/paginated")){
                    nuevaUrl = nuevaUrl.replaceAll("/paginated", "/cascade");
                    Log.d("URLS", "doInBackground: " + nuevaUrl);

                    Document doc = Jsoup.connect(nuevaUrl).get();
                    Log.d("Items", "Url: " + doc);

                    Elements data = doc.select("div.img-container.text-center");
                    for (Element e : data){
                        String imgUrl = "";
                        if(e.select("div.img-container.text-center").size() > 0)
                        imgUrl = e.select("img").get(0).attr("data-src");
                        Log.d("finalurl", "url nueva:" + nuevaUrl + "\nimagenes: " + imgUrl);
                        //String imgUrl = e.select("img").attr("data-src");
                        tmoLectorClaseArrayList.add(new TMOLectorClase(imgUrl));
                    }
                }else{
                    Document doc = Jsoup.connect(nuevaUrl).get();

                    Elements data = doc.select("div.img-container.text-center");
                    for (Element e : data){
                        String imgUrl = e.select("img").attr("data-src");
                        Log.d("finalurl", "url nueva:" + nuevaUrl + "\nimagenes: " + imgUrl);
                        tmoLectorClaseArrayList.add(new TMOLectorClase(imgUrl));
                    }
                }
            }  catch (IOException e) {
                e.printStackTrace();
            }
            return tmoLectorClaseArrayList;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        TMOnlineLector.this.finish();
    }
}