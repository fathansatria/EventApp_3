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
import android.widget.LinearLayout;
import android.widget.Toast;

public class WebActivity extends AppCompatActivity {

    private WebView webView;
    private String url;
    private ProgressDialog pDialog;
    private Toolbar toolbar;
    private LinearLayout emptyPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        toolbar = findViewById(R.id.toolbar_web);
        webView = findViewById(R.id.webview);
        emptyPage = findViewById(R.id.empty_page);

        Bundle extras = getIntent().getExtras();
        if(extras != null){

            url = extras.getString("value");
            webView.setVisibility(View.VISIBLE);
            emptyPage.setVisibility(View.GONE);

        }
        else{

            url = "https://xxxxxxxxxx/xxxxxxx";
            webView.setVisibility(View.GONE);
            emptyPage.setVisibility(View.VISIBLE);

        }

        //init dialog
        initDialog();
        showDialog();


        //init web view
        openWebView();


        //init toolbar
        setUpToolbar();


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

    private void initDialog() {

        pDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        pDialog.setMessage(" Wait For Page ");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

    }

    private void openWebView(){
        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                hideDialog();
            }

        });
        webView.loadUrl(url);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    private void setUpToolbar(){

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


}
