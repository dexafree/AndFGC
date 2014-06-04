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
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import com.dexafree.andfgc.app.events.BusProvider;
import com.dexafree.andfgc.app.events.DownloadFinishedEvent;
import com.dexafree.andfgc.app.utils.Logger;

public class DownloadService extends IntentService {

    private int result = Activity.RESULT_CANCELED;
    public static final String URL = "urlpath";
    public static final String FILENAME = "filename";
    public static final String FILEPATH = "filepath";
    public static final String RESULT = "result";


    public DownloadService() {
        super("DownloadService");
    }

    // will be called asynchronously by Android
    @Override
    protected void onHandleIntent(Intent intent) {
        String urlPath = intent.getStringExtra(URL);
        String fileName = intent.getStringExtra(FILENAME);
        /*File output = new File(Environment.getExternalStorageDirectory(),
                fileName);
        File output = new File(Environment.get)*/
        /*File sdCardRoot = Environment.getExternalStorageDirectory();
        File tempOutput = new File(sdCardRoot.getPath()+"/FGC/");
        tempOutput.mkdirs();
        File output = new File(sdCardRoot+"/FGC/", fileName+".pdf");
        if (output.exists()) {
            output.delete();
        }

        InputStream stream = null;
        FileOutputStream fos = null;
        try {

            URL url = new URL(urlPath);
            stream = url.openConnection().getInputStream();
            InputStreamReader reader = new InputStreamReader(stream);
            fos = new FileOutputStream(output.getPath());
            int next = -1;
            while ((next = reader.read()) != -1) {
                fos.write(next);
            }
            // successfully finished
            result = Activity.RESULT_OK;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/


        try{
            //set the download URL, a url that points to a file on the internet
            //this is the file to be downloaded
            URL url = new URL(urlPath);

            //create the new connection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            //set up some things on the connection


            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            urlConnection.connect();


            File SDCardRoot = Environment.getExternalStorageDirectory();
            File wallpaperDirectory = new File(SDCardRoot.getPath()+"/FGC/");
            wallpaperDirectory.mkdirs();

            File file = new File(SDCardRoot+"/FGC/",fileName+".pdf");

            FileOutputStream fileOutput = new FileOutputStream(file);

            InputStream inputStream = urlConnection.getInputStream();

            int totalSize = urlConnection.getContentLength();

            int downloadedSize = 0;

            byte[] buffer = new byte[1024];
            int bufferLength = 0;
            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
            }

            fileOutput.close();
        } catch (IOException f){
            f.printStackTrace();
        }



        publishResults(fileName);
    }

    private void publishResults(String filename) {
        BusProvider.getInstance().post(new DownloadFinishedEvent(filename));
    }
}
