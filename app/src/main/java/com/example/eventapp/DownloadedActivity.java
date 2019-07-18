//package com.example.eventapp;
//
//import android.Manifest;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.pm.PackageManager;
//import android.graphics.Color;
//import android.net.Uri;
//import android.os.Environment;
//import android.support.design.widget.Snackbar;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.content.LocalBroadcastManager;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.AppCompatButton;
//import android.util.Log;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.example.eventapp.Model.Download;
//
//import java.io.File;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//public class DownloadedActivity extends AppCompatActivity {
//
//    public static final String MESSAGE_PROGRESS = "message_progress";
//    private static final int PERMISSION_REQUEST_CODE = 1;
//
//    @BindView(R.id.btn_open) AppCompatButton btn_open;
//    @BindView(R.id.progress) ProgressBar mProgressBar;
//    @BindView(R.id.progress_text) TextView mProgressText;
//
//    private String url;
//    private String fname;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_downloaded);
//        ButterKnife.bind(this);
//
//        url = getIntent().getStringExtra("url") ;
//        fname = getIntent().getStringExtra("fname") ;
//
//        Log.i("download", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() +"/"+ fname);
//
//        fileExist();
//        registerReceiver();
//
//    }
//
//    @OnClick(R.id.btn_download)
//    public void downloadFile(){
//
//        if(checkPermission()){
//            startDownload();
//        } else {
//            requestPermission();
//        }
//    }
//
//
//    @OnClick(R.id.btn_open)
//    public void openfile(){
//
//        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() +"/"+ fname);
//        //if(file.exists())
//        try
//        {
//            Intent myIntent = new Intent(android.content.Intent.ACTION_VIEW);
//            String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
//            String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
//            myIntent.setDataAndType(Uri.fromFile(file),mimetype);
//            startActivity(myIntent);
//        }
//        catch (Exception e)
//        {
//            // TODO: handle exception
//            String data = e.getMessage();
//        }
//
//    }
//
//    private void startDownload(){
//
//        Intent intent = new Intent(this,DownloadService.class);
//        intent.putExtra("url", url);
//        intent.putExtra("fname", fname);
//        startService(intent);
//
//    }
//    private void fileExist(){
//
//        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() +"/"+ fname);
//
//        if(file.exists()) {
//            btn_open.setBackgroundColor(Color.parseColor("#FF4345"));
//            btn_open.setEnabled(true);
//        } else {
//
//            btn_open.setBackgroundColor(Color.parseColor("#616161"));
//            btn_open.setEnabled(false);
//
//        }
//    }
//
//    private void registerReceiver(){
//
//        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(MESSAGE_PROGRESS);
//        bManager.registerReceiver(broadcastReceiver, intentFilter);
//
//    }
//
//    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            if(intent.getAction().equals(MESSAGE_PROGRESS)){
//
//                Download download = intent.getParcelableExtra("download");
//
//                mProgressBar.setProgress(download.getProgress());
//                if(download.getProgress() == 100){
//
//                    mProgressText.setText("File Berhasil didownload " + fname);
//                    fileExist();
//                } else {
//
//                    mProgressText.setText(String.format("Downloaded (%d/%d) MB",download.getCurrentFileSize(),download.getTotalFileSize()));
//
//                }
//            }
//        }
//    };
//
//    private boolean checkPermission(){
//        int result = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if (result == PackageManager.PERMISSION_GRANTED){
//
//            return true;
//
//        } else {
//
//            return false;
//        }
//    }
//
//    private void requestPermission(){
//
//        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        Log.i("Download", "00000" +String.valueOf(requestCode));
//
//
//        switch (requestCode) {
//
//            case PERMISSION_REQUEST_CODE:
//
//                Log.i("Download", "AAAAAA" +String.valueOf(PERMISSION_REQUEST_CODE));
//
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Log.i("Download", "BBBBB" +String.valueOf(PERMISSION_REQUEST_CODE));
//                    startDownload();
//
//                } else {
//                    Log.i("Download", "CCCCCC" +String.valueOf(PERMISSION_REQUEST_CODE));
//
//                    Snackbar.make(findViewById(R.id.coordinatorLayout),"Permission Denied, Please allow to proceed !", Snackbar.LENGTH_LONG).show();
//
//                }
//                break;
//        }
//    }
//
//}