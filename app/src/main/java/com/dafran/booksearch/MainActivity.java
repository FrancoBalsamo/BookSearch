package com.dafran.booksearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.dafran.booksearch.Activities.Lectulandia.LectulandiaInicio;
import com.dafran.booksearch.Activities.TMO.TMOnlinePrincipal;
import com.dafran.booksearch.Activities.Trantor.TrantorActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity {
    AdView adView;
    ImageView logobook, ivTMO, trantorIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bannerBookSearh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        logobook = (ImageView)findViewById(R.id.logobook);
        ivTMO = (ImageView)findViewById(R.id.ivTMO);
        trantorIS = (ImageView)findViewById(R.id.ivTRANTOR);
        ivTMO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aTMOnline();
            }
        });
        trantorIS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aTrantorIs();
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
        Intent tmo = new Intent(MainActivity.this, TMOnlinePrincipal.class);
        startActivity(tmo);
    }

    private void bannerBookSearh(){
        MobileAds.initialize(MainActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) { }
        });
        adView = (AdView)findViewById(R.id.adViewUnicoMain);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
}