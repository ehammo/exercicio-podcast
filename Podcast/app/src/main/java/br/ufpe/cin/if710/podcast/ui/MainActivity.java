package br.ufpe.cin.if710.podcast.ui;


import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import br.ufpe.cin.if710.podcast.PodcastApp;
import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.Util;
import br.ufpe.cin.if710.podcast.receivers.PodcastReceiver;
import br.ufpe.cin.if710.podcast.services.DownloadXMLService;
import br.ufpe.cin.if710.podcast.db.PodcastDBHelper;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.ui.adapter.XmlFeedAdapter;

import static br.ufpe.cin.if710.podcast.services.DownloadXMLService.DOWNLOAD_BROADCAST;
import static br.ufpe.cin.if710.podcast.services.DownloadXMLService.GET_DATA_BROADCAST;
import static br.ufpe.cin.if710.podcast.services.DownloadXMLService.UPDATE_DATA_BROADCAST;

public class MainActivity extends Activity {

    //ao fazer envio da resolucao, use este link no seu codigo!
    private final String RSS_FEED = "http://leopoldomt.com/if710/fronteirasdaciencia.xml";
    //TODO teste com outros links de podcast

    private ListView items;
    private PodcastReceiver podcastReceiver;
    private final String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        items = (ListView) findViewById(R.id.items);
        Util.verifyPermissions(this, permissions);
        podcastReceiver = new PodcastReceiver(this, items);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this,SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DOWNLOAD_BROADCAST);
        filter.addAction(GET_DATA_BROADCAST);
        filter.addAction(UPDATE_DATA_BROADCAST);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                podcastReceiver,
               filter
        );

        // Calls service to download podcasts info
        Log.d("Main","onStart");
        if(Util.hasPermissions(this, permissions)) {
            if (!DownloadXMLService.isDownloading) {
                podcastReceiver.getFromDatabase();
                DownloadXMLService.startActionGetData(this, RSS_FEED);
            } else {
                podcastReceiver.getFromDatabase();
            }
        }
//        new DownloadXmlTask().execute(RSS_FEED);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if(Util.hasPermissions(this, permissions)) {
            if (!DownloadXMLService.isDownloading) {
                podcastReceiver.getFromDatabase();
                DownloadXMLService.startActionGetData(this, RSS_FEED);
            } else {
                podcastReceiver.getFromDatabase();
            }
        }else{
            Util.verifyPermissions(this, permissions);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Main","onResume");
        PodcastApp.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Main","onPause");
        PodcastApp.activityPaused();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Main","onStop");
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(podcastReceiver);

        XmlFeedAdapter adapter = (XmlFeedAdapter) items.getAdapter();
        if (adapter != null) adapter.clear();
    }




}
