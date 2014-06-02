package com.dexafree.andfgc.app.connections;

import android.content.Context;
import android.content.Intent;
import com.dexafree.andfgc.app.beans.Timetable;
import com.dexafree.andfgc.app.services.DownloadService;
import com.dexafree.andfgc.app.utils.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Carlos on 26/05/2014.
 */
public class GetTimetables {

    public static ArrayList<Timetable> loadTables(){
        ArrayList<Timetable> timetablesArray = new ArrayList<Timetable>();

        try {
            Document doc = Jsoup.connect("http://www.fgc.cat/cat/cercador.asp")
                    .timeout(6000)
                    .get();

            Elements timetables = doc.select(".content > ul li a");

            Timetable t;
            for(int i=0;i<timetables.size();i++){
                String name = timetables.get(i).text();
                String url = timetables.get(i).attr("href");
                Logger.d("URL", url);
                url = url.replace("..", "http://www.fgc.cat");

                t = new Timetable(name, url);
                timetablesArray.add(t);
            }

        } catch (IOException e){
            e.printStackTrace();
        }

        return timetablesArray;
    }

    public static void downloadTimetable(Timetable t, Context context){
        Intent i = new Intent(context, DownloadService.class);
        i.putExtra(DownloadService.FILENAME, t.getLinea());
        i.putExtra(DownloadService.URL, t.getUrl());
        context.startService(i);
    }
}
