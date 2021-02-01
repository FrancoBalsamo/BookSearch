package com.dafran.booksearch.Activities.TMO;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.dafran.booksearch.Adaptador.TMOAdapters.TMOLectorAdapter;
import com.dafran.booksearch.Clases.TMOClases.TMOLectorClase;
import com.dafran.booksearch.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class TMOnlineLector extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TMOLectorAdapter adapter;
    private ArrayList<TMOLectorClase> tmoLectorClases = new ArrayList<>();
    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmonline_lector);

        url = getIntent().getStringExtra("url");

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TMOLectorAdapter(tmoLectorClases, TMOnlineLector.this);
        recyclerView.setAdapter(adapter);

        Content content = new Content();
        content.execute();
    }

    private class Content extends AsyncTask<Void,Void, ArrayList<TMOLectorClase>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<TMOLectorClase> items) {
            super.onPostExecute(items);
            //Actualizar información
            adapter.updateData(items);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected ArrayList<TMOLectorClase> doInBackground(Void... voids) {

            tmoLectorClases.clear();
            try {
                String nuevaUrl = Jsoup.connect(url).followRedirects(true).execute().url().toExternalForm();
                Log.d("Items", "Url: " + nuevaUrl);

                if(nuevaUrl.contains("/paginated")){
                    nuevaUrl = nuevaUrl.replaceAll("/paginated", "/cascade");
                    Log.d("items", "doInBackground: " + nuevaUrl);

                    Document doc = Jsoup.connect(nuevaUrl).get();
                    Log.d("Items", "Url: " + doc);

                    Elements data = doc.select("div.img-container.text-center");
                    for (Element e : data){
                        String imgUrl = "";
                        if(e.select("div.img-container.text-center").size() > 0)
                        imgUrl = e.select("img").get(0).attr("data-src");
                        Log.d("TAG", "doInBackground: " + imgUrl);
                        //String imgUrl = e.select("img").attr("data-src");
                        tmoLectorClases.add(new TMOLectorClase(imgUrl));
                    }
                }else{
                    Document doc = Jsoup.connect(nuevaUrl).get();

                    Elements data = doc.select("div.img-container.text-center");
                    for (Element e : data){
                        String imgUrl = e.select("img").attr("data-src");
                        Log.d("items", "doInBackground: "+ imgUrl);
                        tmoLectorClases.add(new TMOLectorClase(imgUrl));
                    }
                }
            }  catch (IOException e) {
                e.printStackTrace();
            }
            return tmoLectorClases;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        TMOnlineLector.this.finish();
    }
}