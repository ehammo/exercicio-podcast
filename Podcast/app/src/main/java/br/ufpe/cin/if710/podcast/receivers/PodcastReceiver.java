package br.ufpe.cin.if710.podcast.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.GenericArrayType;

import br.ufpe.cin.if710.podcast.db.GetFromDatabase;
import br.ufpe.cin.if710.podcast.services.DownloadXMLService;

import static br.ufpe.cin.if710.podcast.services.DownloadXMLService.DOWNLOAD_BROADCAST;
import static br.ufpe.cin.if710.podcast.services.DownloadXMLService.GET_DATA_BROADCAST;
import static br.ufpe.cin.if710.podcast.services.DownloadXMLService.UPDATE_DATA_BROADCAST;

/**
 * Created by eduardo on 09/10/2017.
 */

public class PodcastReceiver extends BroadcastReceiver {

    Context mContext;
    ListView items;

    public PodcastReceiver(){}

    public PodcastReceiver(Context context, ListView items){
        mContext = context;
        this.items = items;
    }

    public void onReceive(Context context, Intent intent) {
        Log.d("receiver", "receive");
        if(intent != null){
            final String action = intent.getAction();
            if (UPDATE_DATA_BROADCAST.equals(action)) {
                Toast.makeText(mContext, "Updating XML items from service", Toast.LENGTH_SHORT).show();
                getFromDatabase();
            }else{
                getFromDatabase();
            }
        }

    }

    public void getFromDatabase(){
        (new GetFromDatabase(mContext, items)).execute();
    }
}
