package com.dexafree.andfgc.app.services;

/**
 * Created by Carlos on 26/05/2014.
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.dexafree.andfgc.app.R;
import com.dexafree.andfgc.app.events.BusProvider;
import com.dexafree.andfgc.app.events.DownloadFinishedEvent;
import com.dexafree.andfgc.app.utils.Logger;

public class DownloadService extends IntentService {

    private int result = Activity.RESULT_CANCELED;
    public static final String URL = "urlpath";
    public static final String FILENAME = "filename";
    public static final String TIMETABLE = "timetable";

    int id = 1;


    public DownloadService() {
        super("DownloadService");
    }

    // will be called asynchronously by Android
    @Override
    protected void onHandleIntent(Intent intent) {
        String urlPath = intent.getStringExtra(URL);
        String fileName = intent.getStringExtra(FILENAME);
        String timetable = intent.getStringExtra(TIMETABLE);

        try{

            File SDCardRoot = Environment.getExternalStorageDirectory();
            File wallpaperDirectory = new File(SDCardRoot.getPath()+"/FGC/");
            wallpaperDirectory.mkdirs();

            File file = new File(SDCardRoot+"/FGC/",fileName+".pdf");

            //Intent tempIntent = new Intent(this, DownloadService.class);
            Intent tempIntent = new Intent(Intent.ACTION_VIEW);
            tempIntent.setDataAndType(Uri.fromFile(file), "application/pdf");
            tempIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, tempIntent, 0);

            // build notification
            // the addAction re-use the same intent to keep the example short
            NotificationCompat.Builder n  = new NotificationCompat.Builder(this)
                    .setContentTitle(getString(R.string.downloading))
                    .setContentText(getString(R.string.downloading_timetable)+" "+timetable)
                    .setSmallIcon(R.drawable.fgclogo)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true)
                    .setOngoing(true);


            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            notificationManager.notify(id, n.build());



            //set the download URL, a url that points to a file on the internet
            //this is the file to be downloaded
            URL url = new URL(urlPath);

            //create the new connection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            //set up some things on the connection


            urlConnection.setRequestMethod("GET");
            urlConnection.connect();




            FileOutputStream fileOutput = new FileOutputStream(file);

            InputStream inputStream = urlConnection.getInputStream();

            int totalSize = urlConnection.getContentLength();

            int downloadedSize = 0;

            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            int tempValue=0;
            int increment = totalSize/20;
            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;

                if(tempValue < downloadedSize){
                    tempValue+=increment;
                    n.setProgress(totalSize, downloadedSize, false);
                    notificationManager.notify(id, n.build());
                }
            }

            fileOutput.close();

            n.setContentText(getString(R.string.download_finished))
                    .setProgress(0, 0, false)
                    .setOngoing(false)
            .setContentIntent(pIntent);
            notificationManager.notify(id, n.build());

            publishResults(fileName, file);
        } catch (IOException f){
            f.printStackTrace();

            Toast.makeText(this, getString(R.string.download_error), Toast.LENGTH_SHORT).show();
        }

    }

    private void publishResults(String filename, File file) {
        BusProvider.getInstance().post(new DownloadFinishedEvent(filename, file));
    }
}
