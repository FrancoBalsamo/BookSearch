package com.dafran.booksearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dafran.booksearch.Activities.Lectulandia.LectulandiaInicio;
import com.dafran.booksearch.Activities.TMO.TMOnline;
import com.dafran.booksearch.Activities.Trantor.TrantorActivity;
import com.dafran.booksearch.Adaptador.AdaptadorMainListView;
import com.dafran.booksearch.Clases.Paginas;
import com.dafran.booksearch.SQLite.PaginasSQL;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    ListView lv;
    AdView adView;

    ArrayList<Paginas> array = new ArrayList<Paginas>();
    PaginasSQL psql = new PaginasSQL(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bannerBookSearh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv = (TextView)findViewById(R.id.bookTV);
        lv = (ListView)findViewById(R.id.lvElecciones);

        tv.setText("Book Search");
        tv.setTypeface(ResourcesCompat.getFont(MainActivity.this, R.font.saowttregular));

        array = psql.llenar_AL();

        lv.setAdapter(new AdaptadorMainListView(MainActivity.this, array));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(array.get(position).getId() == 1){
                    aTrantorIs();
                    Toast.makeText(getApplicationContext(), "Esta página recibe muchas solicitudes diariamiente, por lo que puede tardar em cargar. Por favor, ¡Se paciente!", Toast.LENGTH_LONG).show();
                } else if(array.get(position).getId() == 2){
                    aLectulandia();
                }else if(array.get(position).getId() == 3){
                    aTMOnline();
                }
            }
        });
    }

    private void aTrantorIs(){
        Intent trantor = new Intent(MainActivity.this, TrantorActivity.class);
        startActivity(trantor);
    }

    private void aLectulandia(){
        Intent lectulandia = new Intent(MainActivity.this, LectulandiaInicio.class);
        startActivity(lectulandia);
    }

    private void aTMOnline(){
        //Toast.makeText(getApplicationContext(), "Próximamente.", Toast.LENGTH_LONG).show();
        Intent tmo = new Intent(MainActivity.this, TMOnline.class);
        startActivity(tmo);
    }

    private void bannerBookSearh(){
        MobileAds.initialize(MainActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) { }
        });
        adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
}