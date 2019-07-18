
package com.example.eventapp.Service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;


import com.example.eventapp.DownloadFileActivity;
import com.example.eventapp.Model.Download;
import com.example.eventapp.Model.apiInterface;
import com.example.eventapp.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;


import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class DownloadService extends IntentService {

    private String url;
    private String fname;
    DownloadFile downloadZipFileTask;
    NotificationChannel notificationChannel, notificationChannel2;

    String NOTIFICATION_CHANNEL_ID = "download_procces";
    String NOTIFICATION_CHANNEL_ID_2 = "download_finished";

    public DownloadService() {
        super("Download Service");
    }

    private NotificationCompat.Builder notificationBuilder, notificationBuilder2;
    private NotificationManager notificationManager;
    private NotificationManager notificationManager2;
    private double totalFileSize;

    @Override
    protected void onHandleIntent(Intent intent) {

        url = intent.getStringExtra("url");
        fname = intent.getStringExtra("fname");
        Log.i("downloadx",url);
        Log.i("downloadx",fname);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager2 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "Notification", NotificationManager.IMPORTANCE_LOW);

            notificationChannel.setDescription("Download Information");
            notificationManager.createNotificationChannel(notificationChannel);


            notificationChannel2 = new NotificationChannel(NOTIFICATION_CHANNEL_ID_2,
                    "Notification", NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel2.setDescription("Download Information Finish");
            notificationManager2.createNotificationChannel(notificationChannel2);



        }

        notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder2 = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID_2);


        notificationBuilder
                .setSmallIcon(R.drawable.ic_notif)
                .setContentTitle("Download")
                .setContentText("Downloading File")
                .setAutoCancel(true);

        notificationBuilder2
                .setSmallIcon(R.drawable.ic_notif)
                .setContentTitle("Download")
                .setContentText("File Downloaded")
                .setAutoCancel(true);

        notificationManager.notify(0, notificationBuilder.build());


        try{

            initDownload();

        }
        catch (Exception e){

        }
    }

    private void initDownload(){

        apiInterface apiI = apiService.getClient().create(apiInterface.class);
        Call<ResponseBody> call = apiI.downloadFile();


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();

                    downloadZipFileTask = new DownloadFile();
                    downloadZipFileTask.execute(response.body());

                } else {
                    Log.d(TAG, "Connection failed " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });


    }



    private void sendNotification(Download download){

        sendIntent(download);

        notificationBuilder.setVibrate(new long[]{0});
        notificationBuilder.setProgress(100,download.getProgress(),false);
        notificationBuilder.setContentText("Downloading file "+ download.getCurrentFileSize() +"/"+totalFileSize +" MB");
        notificationManager.notify(0, notificationBuilder.build());

    }

    private void sendIntent(Download download){


        Intent intent = new Intent(DownloadFileActivity.MESSAGE_PROGRESS);
        intent.putExtra("download",download);
        LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(intent);

    }

    private void onDownloadComplete(){


        Download download = new Download();
        download.setProgress(100);
        sendIntent(download);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            notificationManager.cancel(0);
            notificationBuilder.setProgress(0,0,false);
            notificationManager2.notify(1, notificationBuilder2.build());


        }
        else {

            notificationBuilder.setVibrate(new long[]{0,300,200,300});
            notificationManager.cancel(0);
            notificationBuilder.setProgress(0,0,false);
            notificationBuilder.setContentText("File Downloaded");
            notificationManager.notify(0, notificationBuilder.build());

        }



    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        notificationManager.cancel(0);

    }

    private class DownloadFile extends AsyncTask<ResponseBody, Pair<Integer, Long>, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(ResponseBody... urls) {
            //Copy you logic to calculate progress and call
            saveToDisk(urls[0], fname);
            return null;

        }

        protected void onProgressUpdate(Pair<Integer, Long>... progress) {

            Log.d("API123", progress[0].second + " ");

            if (progress[0].first == 100)
                Toast.makeText(getApplicationContext(), "File downloaded successfully", Toast.LENGTH_SHORT).show();


            if (progress[0].second > 0) {


            }

            if (progress[0].first == -1) {
                Toast.makeText(getApplicationContext(), "Download failed", Toast.LENGTH_SHORT).show();
            }

        }

        public void doProgress(Pair<Integer, Long> progressDetails) {
            publishProgress(progressDetails);
        }

        @Override
        protected void onPostExecute(String result) {

        }

        private void saveToDisk(ResponseBody body, String filename) {

            try {

                File destinationFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);
                DecimalFormat two = new DecimalFormat("0.00");

                InputStream inputStream = null;
                OutputStream outputStream = null;

                try {

                    inputStream = body.byteStream();
                    outputStream = new FileOutputStream(destinationFile);
                    byte data[] = new byte[4096];
                    int count;
                    long total = 0;
                    int progress = 0;
                    long fileSize = body.contentLength();
                    Log.d(TAG, "File Size=" + fileSize);


                    while ((count = inputStream.read(data)) != -1) {

                        total += count;

                        outputStream.write(data, 0, count);
                        progress += count;
                        Pair<Integer, Long> pairs = new Pair<>(progress, fileSize);
                        downloadZipFileTask.doProgress(pairs);

                        Log.d(TAG, "Progress: " + progress + "/" + fileSize + " >>>> " + (float) progress / fileSize);

                        totalFileSize = Double.valueOf(two.format(fileSize / (Math.pow(1024, 2))));
                        double current = total / (Math.pow(1024, 2));

                        Download download = new Download();
                        download.setTotalFileSize(totalFileSize);

                        double p = (float) progress / fileSize * 100.00;

                        int progressFile = (int) (p);

                        download.setCurrentFileSize(Double.valueOf(two.format(current)));
                        download.setProgress(progressFile);
                        sendNotification(download);
                    }

                    outputStream.flush();
                    onDownloadComplete();


                    Log.d(TAG, destinationFile.getParent());
                    Pair<Integer, Long> pairs = new Pair<>(100, 100L);
                    downloadZipFileTask.doProgress(pairs);

                    return;

                } catch (IOException e) {
                    e.printStackTrace();
                    Pair<Integer, Long> pairs = new Pair<>(-1, Long.valueOf(-1));
                    downloadZipFileTask.doProgress(pairs);
                    Log.d(TAG, "Failed to save the file!");
                    return;
                } finally {
                    if (inputStream != null) inputStream.close();
                    if (outputStream != null) outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed to save the file!");
                return;
            }
        }
    }





}

