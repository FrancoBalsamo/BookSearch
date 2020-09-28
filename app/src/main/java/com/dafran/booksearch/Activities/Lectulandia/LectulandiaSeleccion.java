package com.dafran.booksearch.Activities.Lectulandia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dafran.booksearch.Activities.Trantor.TrantorDetalleLibro;
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

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class LectulandiaSeleccion extends AppCompatActivity {
    private long id;
    AdView adView;
    Button btn;
    private DownloadManager downloadManager;

    private RecyclerView recyclerView;
    private LectulandiaSeleccionLibroAdaptador lectulandiaSeleccionLibroAdaptador;
    private ArrayList<LectulandiaSeleccionItemsClase> lectulandiaSeleccionItemsClaseArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectulandia_seleccion);

        recyclerView = findViewById(R.id.rvLEctuSeleccion);
        btn = (Button)findViewById(R.id.DEscargarLEctulandia);

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
            final String datoTitulo = getIntent().getStringExtra("titulo");

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
                        final String finalLinkPdf1 = linkPdf;
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.lectulandia.co"+finalLinkPdf1));
                                startActivity(intent);
                            }
                        });
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

    public void descargarArchivo(String url, String archivo){
        downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse("https://www.lectulandia.co" + url));
        request.setTitle(archivo);
        request.setDescription("Descargando gracias a DownloadManager.");

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            request.setDestinationInExternalFilesDir(this, Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "descarga",archivo+".pdf");
        }
        id = downloadManager.enqueue(request);
    }

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(id, 0);
            Cursor cursor = downloadManager.query(query);

            if (cursor.moveToFirst()) {
                int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                int reason = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON));

                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    //podemos recuperar el fichero descargado
                    ParcelFileDescriptor file = null;
                    try {
                        file = downloadManager.openDownloadedFile(id);
                        Toast.makeText(LectulandiaSeleccion.this, "Descarga exitosa.", Toast.LENGTH_LONG).show();
                    } catch (FileNotFoundException ex) {
                        Toast.makeText(LectulandiaSeleccion.this, "Exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (status == DownloadManager.STATUS_FAILED) {
                    Toast.makeText(LectulandiaSeleccion.this, "Fallo: " + reason, Toast.LENGTH_LONG).show();
                } else if (status == DownloadManager.STATUS_PAUSED) {
                    Toast.makeText(LectulandiaSeleccion.this, "Pausadi: " + reason, Toast.LENGTH_LONG).show();
                } else if (status == DownloadManager.STATUS_PENDING) {
                    Toast.makeText(LectulandiaSeleccion.this, "Pendiente: " + reason, Toast.LENGTH_LONG).show();

                } else if (status == DownloadManager.STATUS_RUNNING) {
                    Toast.makeText(LectulandiaSeleccion.this, "Descargando: " + reason, Toast.LENGTH_LONG).show();
                }
            }
        }

    };
}