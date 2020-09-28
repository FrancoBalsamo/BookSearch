package com.dafran.booksearch.Activities.Trantor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dafran.booksearch.R;

public class TrantorLector extends AppCompatActivity {
    private String urllector = "";
    private WebView lector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trantor_lector);

        urllector = getIntent().getStringExtra("lectorurl");
        Log.d("", "onCreate: " + urllector);

        lector = (WebView)findViewById(R.id.wvTrantor);
        final WebSettings ajustesVisorWeb = lector.getSettings();
        ajustesVisorWeb.setJavaScriptEnabled(true);
        lector.setWebViewClient(new WebViewClient());
        lector.loadUrl(urllector);
    }
}