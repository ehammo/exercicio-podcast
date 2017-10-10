package br.ufpe.cin.if710.podcast.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.if710.podcast.db.PodcastDBHelper;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.domain.XmlFeedParser;
import br.ufpe.cin.if710.podcast.util;


/**
 * Created by eduardo on 09/10/2017.
 */

public class DownloadXMLService extends IntentService {

    private static final String ACTION_GET_DATA = "br.ufpe.cin.if710.podcast.services.action.ACTION_GET_DATA";
    private static final String ACTION_DOWNLOAD_PODCAST = "br.ufpe.cin.if710.podcast.services.action.ACTION_DOWNLOAD_PODCAST";
    private static final String PARAM1 = "br.ufpe.cin.if710.podcast.services.extra.PARAM1";
    public static final String BROADCAST_TYPE = "BROADCAST_TYPE";
    public static final String GET_DATA_BROADCAST = "br.ufpe.cin.if710.broadcasts.GET_DATA_BROADCAST";
    public static final String DOWNLOAD_PODCAST_BROADCAST = "br.ufpe.cin.if710.broadcasts.DOWNLOAD_PODCAST_BROADCAST";
    public static final String BROADCAST_ACTION = "br.ufpe.cin.if710.broadcasts.BROADCAST_ACTION";
    public static final String UPDATE_DATA_BROADCAST = "br.ufpe.cin.if710.broadcasts.UPDATE_DATA_BROADCAST";

    public static final boolean DEBUG = true;

    public DownloadXMLService() {
        super("DownloadXMLService");
    }

    public static void startActionGetData(Context context, String feedLink) {
        Log.d("service","getData");
        Intent intent = new Intent(context, DownloadXMLService.class);
        intent.setAction(ACTION_GET_DATA);
        intent.putExtra(PARAM1, feedLink);
        context.startService(intent);
    }

    public static void startActionDownloadPodcast(Context context, String param1, String param2) {
        Intent intent = new Intent(context, DownloadXMLService.class);
        intent.setAction(ACTION_DOWNLOAD_PODCAST);
        intent.putExtra(PARAM1, param1);
        context.startService(intent);
    }


    @Override
    public void onHandleIntent(Intent intent) {
        Log.d("service","onHandle");
        if (intent != null) {
            Log.d("service","notNull");
            final String action = intent.getAction();
            if (ACTION_GET_DATA.equals(action)) {
                Log.d("service","filter");
                final String feedLink = intent.getStringExtra(PARAM1);
                try {
                    handleActionGetData(feedLink);
                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                }
            } else if (ACTION_DOWNLOAD_PODCAST.equals(action)) {
                final String downloadLink = intent.getStringExtra(PARAM1);
                handleActionDownloadPodcast(downloadLink);
            }
        }
    }



    private void handleActionGetData(String feed) throws IOException, XmlPullParserException {
        Log.d("service","getDataStart");
        List<ItemFeed> itemList = new ArrayList<>();

        if(util.isNetworkAvailable(getApplicationContext()) && !DEBUG) {
            try {
                // Usar parser para extrair itens provenientes do XML e salvá-los no banco de dados
                itemList = XmlFeedParser.parse(getRssFeed(feed));
            } catch (XmlPullParserException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            for (ItemFeed item : itemList) {
                ContentValues cv = new ContentValues();

                cv.put(PodcastDBHelper.EPISODE_DATE, item.getPubDate());
                cv.put(PodcastDBHelper.EPISODE_DESC, item.getDescription());
                cv.put(PodcastDBHelper.EPISODE_DOWNLOAD_LINK, item.getDownloadLink());
                cv.put(PodcastDBHelper.EPISODE_LINK, item.getLink());
                cv.put(PodcastDBHelper.EPISODE_TITLE, item.getTitle());
                //cv.put(PodcastDBHelper.EPISODE_FILE_URI, item.getUri());

                getContentResolver().insert(PodcastProviderContract.EPISODE_LIST_URI, cv);
            }
        }
            Intent intent = new Intent(BROADCAST_ACTION);
            Log.d("service","broadcast");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void handleActionDownloadPodcast(String downloadLink) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

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

}
