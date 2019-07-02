package com.example.eventapp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {

    private WebView webView;
    private WebViewClient webViewClient;
    private String url;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Bundle extras = getIntent().getExtras();
        if(extras != null){

            url = extras.getString("value");

        }
        else{
            url = "https://xxxxxxxxxx/xxxxxxx";
        }

        //init dialog
        pDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        pDialog.setMessage(" Wait For Page ");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        showDialog();

        //init web view
        webView = findViewById(R.id.webview);
        webViewClient = new WebViewClient();
        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                hideDialog();
            }
        });
        webView.loadUrl(url);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //init toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_web);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }

        });

    }

    @Override
    public void onBackPressed() {

        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
