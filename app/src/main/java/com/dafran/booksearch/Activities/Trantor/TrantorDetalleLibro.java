package com.dafran.booksearch.Activities.Trantor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.dafran.booksearch.Adaptador.TrantorAdapter.TrantorBookAdapter;
import com.dafran.booksearch.Clases.Trantor.TrantorBookDetail;
import com.dafran.booksearch.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class TrantorDetalleLibro extends AppCompatActivity {
    private long id;
    private DownloadManager downloadManager;
    Button descargar, leer;
    private String urlDescarga = "";
    private String titulo = "";

    private RecyclerView recyclerView;
    private TrantorBookAdapter adapter;
    private ArrayList<TrantorBookDetail> trantorDetalleLibros = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trantor_detalle_libro);
        recyclerView = findViewById(R.id.rvCapitulosSeleccion);

        leer = (Button) findViewById(R.id.btnLeerTrantor);

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver, filter);

        urlDescarga = getIntent().getStringExtra("descarga");
        titulo = getIntent().getStringExtra("titulo");

        descargar = (Button) findViewById(R.id.btnDescargaTrantor);

        descargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Las descargas no estan disponibles por el momento.", Toast.LENGTH_LONG).show();
//                descargarArchivo(urlDescarga, titulo + ".epub");
//                descargaTemporal(TrantorDetalleLibro.this, urlDescarga);
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(TrantorDetalleLibro.this));
        adapter = new TrantorBookAdapter(trantorDetalleLibros, TrantorDetalleLibro.this);
        recyclerView.setAdapter(adapter);

        Content content = new Content();
        content.execute();
    }

    private class Content extends AsyncTask<Void, Void, ArrayList<TrantorBookDetail>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<TrantorBookDetail> items) {
            super.onPostExecute(items);
            //Actualizar información
            adapter.updateData(items);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected ArrayList<TrantorBookDetail> doInBackground(Void... voids) {
            trantorDetalleLibros.clear();
            String url = getIntent().getStringExtra("urlLibro");
            try {
                Document doc = Jsoup.connect(url).get();

                Elements tit = doc.select("header.row");

                for (Element e : tit) {
                    String titulo = e.select("div").select("h1").text();
                    Log.d("Items", "Descripcion: " + titulo);

                    Elements datos = doc.select("div.row");
                    for (Element e1 : datos) {
                        String imgUrl = e1.select("div.span4").select("img").attr("src");
                        String descripcion = e1.select("div.span8").select("p").text();
                        String descargaUrl = e1.select("div.span3").select("a.btn.btn-large.btn-inverse").attr("href");
                        final String lectorUrl = e1.select("div.span3").select("a.btn.btn-large.btn-warning").attr("href");
                        if (!isNullorEmpty(imgUrl) && !isNullorEmpty(descargaUrl) && !isNullorEmpty(lectorUrl)) {
                            String autor = ""; //valor por defecto
                            String idioma = "";
                            Log.d("Items", "Descripcion: " + autor + "" + idioma);
                            imgUrl = "https://trantor.is" + imgUrl;
                            Element primero = e1.select("div.span8").select("dd").select("a").first();
                            Element ultimo = e1.select("div.span8").select("dd").select("a").last();
                            autor = primero.text();
                            idioma = ultimo.text();
                            leer.setOnClickListener(new View.OnClickListener() {
                                @SuppressLint("WrongThread")
                                @Override
                                public void onClick(View v) {
                                    Intent alLector = new Intent(TrantorDetalleLibro.this, TrantorLector.class);
                                    alLector.putExtra("lectorurl", "https://trantor.is" + lectorUrl);
                                    startActivity(alLector);
                                }
                            });
                            trantorDetalleLibros.add(new TrantorBookDetail(titulo, imgUrl, "Autor/a: " + autor, "Idioma: [" + idioma + "]", descripcion,lectorUrl));
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return trantorDetalleLibros;
        }
    }

    public static boolean isNullorEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        TrantorDetalleLibro.this.finish();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver, intentFilter);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        unregisterReceiver(downloadReceiver);
    }

    public void ver() {
        Intent intent = new Intent();
        intent.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
        startActivity(intent);
    }

    public void descargarArchivo(String url, String archivo){
        downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("Descarga");
        request.setDescription("Prueba del servicio Download Manager.");

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            request.setDestinationInExternalFilesDir(this, Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "descarga",archivo);
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
                        Toast.makeText(TrantorDetalleLibro.this, "Descarga exitosa.", Toast.LENGTH_LONG).show();
                    } catch (FileNotFoundException ex) {
                        Toast.makeText(TrantorDetalleLibro.this, "Exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (status == DownloadManager.STATUS_FAILED) {
                    Toast.makeText(TrantorDetalleLibro.this, "Fallo: " + reason, Toast.LENGTH_LONG).show();
                } else if (status == DownloadManager.STATUS_PAUSED) {
                    Toast.makeText(TrantorDetalleLibro.this, "Pausadi: " + reason, Toast.LENGTH_LONG).show();
                } else if (status == DownloadManager.STATUS_PENDING) {
                    Toast.makeText(TrantorDetalleLibro.this, "Pendiente: " + reason, Toast.LENGTH_LONG).show();

                } else if (status == DownloadManager.STATUS_RUNNING) {
                    Toast.makeText(TrantorDetalleLibro.this, "Descargando: " + reason, Toast.LENGTH_LONG).show();
                }
            }
        }

    };

    private boolean verificarReadRa(String nombrePaquete, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(nombrePaquete, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    private void descargaReadEra() {
        LayoutInflater li = LayoutInflater.from(TrantorDetalleLibro.this);
        View promptsView = li.inflate(R.layout.alert_descarga, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TrantorDetalleLibro.this);
        final Button cancelar = (Button) promptsView.findViewById(R.id.cancelar);
        final Button aceptar = (Button) promptsView.findViewById(R.id.aceptar);

        alertDialogBuilder.setView(promptsView);

        cancelar.setText("Cancelar");
        aceptar.setText("Aceptar");

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Toast.makeText(TrantorDetalleLibro.this, "Cancelado", Toast.LENGTH_LONG).show();
            }
        });
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=org.readera&hl=es_AR");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("com.android.vending");
                alertDialog.dismiss();
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    //No encontró la aplicación, abre la versión web.
                    alertDialog.dismiss();
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                }
            }
        });
    }

    private void readEra() {
        Intent intent = getPackageManager().getLaunchIntentForPackage("org.readera");
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        startActivity(intent);
    }

    private void descargaTemporal(Context context, String url){
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }
}