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

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.receivers.PodcastReceiver;
import br.ufpe.cin.if710.podcast.services.DownloadXMLService;
import br.ufpe.cin.if710.podcast.db.PodcastDBHelper;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.ui.adapter.XmlFeedAdapter;

import static br.ufpe.cin.if710.podcast.services.DownloadXMLService.BROADCAST_ACTION;

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
        verifyStoragePermissions(this, permissions);
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
        // Register Dynamic Receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(
                podcastReceiver,
                new IntentFilter(BROADCAST_ACTION)
        );

        // Calls service to download podcasts info
        Log.d("onStart","serviceMethod");

        DownloadXMLService.startActionGetData(this,RSS_FEED);
//        new DownloadXmlTask().execute(RSS_FEED);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(podcastReceiver);

        XmlFeedAdapter adapter = (XmlFeedAdapter) items.getAdapter();
        if (adapter != null) adapter.clear();
    }

    //TODO Opcional - pesquise outros meios de obter arquivos da internet
    private String getRssFeed(String feed) throws IOException {
        InputStream in = null;
        String rssFeed = "";
        try {
            URL url = new URL(feed);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int count; (count = in.read(buffer)) != -1; ) {
                out.write(buffer, 0, count);
            }
            byte[] response = out.toByteArray();
            rssFeed = new String(response, "UTF-8");
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return rssFeed;
    }



    public static void verifyStoragePermissions(Activity activity, String[] per) {
        // Check if we have write permission
        for (int i = 0; i < per.length; i++) {
            int permission = ActivityCompat.checkSelfPermission(activity, per[i]);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                        activity,
                        per,
                        1
                );
                i = per.length;
            }

        }

    }
}
