package br.ufpe.cin.if710.podcast.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import br.ufpe.cin.if710.podcast.db.PodcastDBHelper;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.domain.XmlFeedParser;
import br.ufpe.cin.if710.podcast.Util;


/**
 * Created by eduardo on 09/10/2017.
 */

public class DownloadXMLService extends IntentService {

    private static final String ACTION_GET_DATA = "br.ufpe.cin.if710.podcast.services.action.ACTION_GET_DATA";
    private static final String ACTION_DOWNLOAD_PODCAST = "br.ufpe.cin.if710.podcast.services.action.ACTION_DOWNLOAD_PODCAST";
    private static final String PARAM1 = "br.ufpe.cin.if710.podcast.services.extra.PARAM1";
    public static final String GET_DATA_BROADCAST = "br.ufpe.cin.if710.broadcasts.GET_DATA_BROADCAST";
    public static final String UPDATE_DATA_BROADCAST = "br.ufpe.cin.if710.broadcasts.UPDATE_DATA_BROADCAST";
    public static final String DOWNLOAD_BROADCAST = "br.ufpe.cin.if710.broadcasts.DOWNLOAD_BROADCAST";

    public static boolean isDownloading = false;
    public static String currentDownloadingItem = "NONE";

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

    public static void startActionDownloadPodcast(Context context, ItemFeed item) {
        Intent intent = new Intent(context, DownloadXMLService.class);
        intent.setAction(ACTION_DOWNLOAD_PODCAST);
        currentDownloadingItem = item.getDownloadLink();
        intent.putExtra(PARAM1, item.getLink());
        intent.setData(Uri.parse(item.getDownloadLink()));
        context.startService(intent);
    }


    @Override
    public void onHandleIntent(Intent intent) {
        Log.d("service","Service onHandleIntent");
        if (intent != null) {
            Log.d("service","intent notNull");
            final String action = intent.getAction();
            if (ACTION_GET_DATA.equals(action)) {
                Log.d("service","getData");
                final String feedLink = intent.getStringExtra(PARAM1);
                try {
                    handleActionGetData(feedLink);
                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                }
            } else if (ACTION_DOWNLOAD_PODCAST.equals(action)) {
                Log.d("service", "download");
                Log.d("service", intent.getData().getLastPathSegment());

                final String pk = intent.getStringExtra(PARAM1);
                final Uri uri = intent.getData();
                handleActionDownloadPodcast(pk, uri);
            }
        }
    }



    private void handleActionGetData(String feed) throws IOException, XmlPullParserException {
        Log.d("service","getDataStart");
        List<ItemFeed> itemList = new ArrayList<>();

        if(Util.isNetworkAvailable(getApplicationContext())) {
            try {
                // Usar parser para extrair itens provenientes do XML e salvá-los no banco de dados
                itemList = XmlFeedParser.parse(getRssFeed(feed));
            } catch (XmlPullParserException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            for (ItemFeed item : itemList) {
                ContentValues content = new ContentValues();

                content.put(PodcastDBHelper.EPISODE_DATE, item.getPubDate());
                content.put(PodcastDBHelper.EPISODE_DESC, item.getDescription());
                content.put(PodcastDBHelper.EPISODE_DOWNLOAD_LINK, item.getDownloadLink());
                content.put(PodcastDBHelper.EPISODE_LINK, item.getLink());
                content.put(PodcastDBHelper.EPISODE_TITLE, item.getTitle());
                content.put(PodcastDBHelper.EPISODE_FILE_URI, item.getUri());
                content.put(PodcastDBHelper.EPISODE_CURRENT_TIME, item.getCurrentTime());

                getContentResolver().insert(PodcastProviderContract.EPISODE_LIST_URI, content);
            }
        }
        sendBroadcast(UPDATE_DATA_BROADCAST);
    }

    private void handleActionDownloadPodcast(String pk, Uri uri) {
        File root = new File(Environment.getExternalStorageDirectory() + "/Podcasts");

        root.mkdirs();

        File file_output = new File(root, uri.getLastPathSegment());

        if (!file_output.exists()) {
            Log.d("service","exists");
            Log.d("service","download");
            downloadItem(file_output.getPath(),uri.toString());
            isDownloading = false;
            currentDownloadingItem = "NONE";
            updateItem(pk, file_output.getPath());
        }else{
            updateItem(pk, file_output.getPath());
        }
        sendBroadcast(DOWNLOAD_BROADCAST);
    }

    private void sendBroadcast(String action){
        Intent intent = new Intent(action);
        Log.d("service","broadcast");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void downloadItem(String path, String uriToString){
        isDownloading = true;
        HttpURLConnection c = null;
        FileOutputStream fos = null;
        BufferedOutputStream out = null;
        try {
            Log.d("service", "conexao");
            URL url = new URL(uriToString);
            Log.d("service", "url: "+url);
            c = (HttpURLConnection) url.openConnection();
            fos = new FileOutputStream(path);
            out = new BufferedOutputStream(fos);
            Log.d("service", "start podcast service");

            Log.d("service", "download starting");
            InputStream in = c.getInputStream();

            byte[] buffer = new byte[8192];
            int len = 0;
            int count = 0;
            while ((len = in.read(buffer)) >= 0) {
                out.write(buffer, 0, len);
                if (count % 100 == 0) {
                    Log.d("service", "Buffer " + count);
                }
                count++;
            }
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Log.d("service", "download ended");
            try {
                fos.getFD().sync();
                out.close();
                c.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void updateItem(String pk, String path){
        Log.d("service","update");
        Log.d("service","path="+path);
        // Atualizar URI do podcast baixado (no banco de dados)
        ContentValues content = new ContentValues();
        content.put(PodcastDBHelper.EPISODE_FILE_URI, path);

        String selection = PodcastProviderContract.EPISODE_LINK + " = ?";
        String[] selection_args = new String[]{pk};

        getContentResolver().update(PodcastProviderContract.EPISODE_LIST_URI, content, selection, selection_args);

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

}
