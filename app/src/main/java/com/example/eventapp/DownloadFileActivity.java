package com.example.eventapp;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eventapp.Adapter.FileAdapter;
import com.example.eventapp.Model.Download;
import com.example.eventapp.Model.Files;
import com.example.eventapp.Service.DownloadService;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import static android.support.v4.content.FileProvider.getUriForFile;
import static java.security.AccessController.getContext;

public class DownloadFileActivity extends AppCompatActivity {

    public static final String MESSAGE_PROGRESS = "message_progress";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private RecyclerView recyclerView;
    private ArrayList<Files> listOfFiles = new ArrayList<>();
    private FileAdapter fileAdapter;
    private Dialog dialog;
    private String url,name = " ", detail, urlPass, fname;
    private AppCompatButton btn_open, btn_download;
    private ProgressBar progressBar;
    private TextView tv_progressText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_file);


        Files f = new Files();
        f.setUrl("https://vignette.wikia.nocookie.net/bandori/images/f/f7/01.Neo-Aspect.ogg/revision/latest?cb=20180520101512");
        f.setName("Roselia - Neo Aspect.ogg");
        f.setDetail("Test Download");


        listOfFiles.add(f);

        //init popUp
        setupDialog();

        //initRecyclerView
        fileExist();
        setupRecycleView();


    }

    public void setupRecycleView(){

        fileAdapter = new FileAdapter(listOfFiles, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView = findViewById(R.id.rv_file);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(fileAdapter);

        fileExist();

        fileAdapter.setOnItemClickListener(new FileAdapter.OnItemClickListener() {


            @Override
            public void onItemClick(int position) {

                //can't click

            }

            @Override
            public void onDownloadClick(int position) {


                url = listOfFiles.get(position).getUrl();
                name = listOfFiles.get(position).getName();
                detail = listOfFiles.get(position).getDetail();

                fileExist();
                registerReceiver();

                dialog.show();


            }


        });

    }

    public void setupDialog(){



        dialog = new Dialog(DownloadFileActivity.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.download_popup);
        dialog.setCancelable(true);

        btn_open = dialog.findViewById(R.id.btn_open);
        btn_download = dialog.findViewById(R.id.btn_download);
        progressBar = dialog.findViewById(R.id.pb_progress);
        tv_progressText = dialog.findViewById(R.id.tv_progress_text);

        btn_open.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                File file = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS).getPath() +"/"+ name);
                try
                {

                    Log.d("Open", "SUCCES");
                    Intent myIntent = new Intent(android.content.Intent.ACTION_VIEW);
                    Uri uri = getUriForFile( getApplicationContext(),"com.example.eventapp.fileprovider", file);
                    String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(uri.toString());

                    Log.d("ex", extension);
                    Context context = getApplicationContext();

                    dialog.dismiss();

                    context.grantUriPermission(context.getPackageName(), uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                    myIntent.setDataAndType(uri, mimetype);
                    myIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    context.startActivity(myIntent);

                }
                catch (Exception e)
                {

                    // TODO: handle exception
                    Log.d("Error Open", e.getMessage());

                }

            }

        });

        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkPermission()){

                    startDownload();

                } else {

                    requestPermission();

                }

            }
        });




    }

    private void fileExist(){

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() +"/"+ name);

        if(file.exists()) {

            btn_open.setBackgroundColor(Color.parseColor("#FF4345"));
            btn_open.setEnabled(true);


        } else {

            btn_open.setBackgroundColor(Color.parseColor("#616161"));
            btn_open.setEnabled(false);
            tv_progressText.setText(" ");



        }
    }

    private void registerReceiver(){

        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(MESSAGE_PROGRESS);
        bManager.registerReceiver(broadcastReceiver, intentFilter);

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(MESSAGE_PROGRESS)){

                Download download = intent.getParcelableExtra("download");

                progressBar.setProgress(download.getProgress());

                if(download.getProgress() == 100){

                    tv_progressText.setText("File "+ name + " Berhasil didownload ");
                    fileExist();

                } else {

                    tv_progressText.
                            setText("Downloaded (" + download.getCurrentFileSize() + "/"
                                            + download.getTotalFileSize() + ") MB");

                }
            }
        }
    };

    private void startDownload(){

        tv_progressText.setText("Please Wait");

        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra("url", url);
        intent.putExtra("fname", name);
        startService(intent);


    }

    private boolean checkPermission(){

        int result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;
        }
    }

    private void requestPermission(){

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.i("Download", "00000" +String.valueOf(requestCode));


        switch (requestCode) {

            case PERMISSION_REQUEST_CODE:

                Log.i("Download", "AAAAAA" +String.valueOf(PERMISSION_REQUEST_CODE));

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("Download", "BBBBB" +String.valueOf(PERMISSION_REQUEST_CODE));
                    startDownload();

                } else {

                    Log.i("Download", "CCCCCC" +String.valueOf(PERMISSION_REQUEST_CODE));


                }
                break;
        }
    }



}
