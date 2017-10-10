package br.ufpe.cin.if710.podcast.util;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.conn.ConnectTimeoutException;

import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.ui.adapter.XmlFeedAdapter;

/**
 * Created by eduardo on 09/10/2017.
 */

public class GetFromDatabase extends AsyncTask<Object, Void, List<ItemFeed>> {

    Context mContext;
    ListView items;

    public GetFromDatabase(Context context, ListView items){
        mContext = context;
        this.items = items;
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(mContext, "Getting podcast from database...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected List<ItemFeed> doInBackground(Object... params) {
        Log.d("task","background");
        List<ItemFeed> itemList = new ArrayList<>();
        Log.d("task","query");
        Cursor queryCursor = mContext.getContentResolver().query(
                PodcastProviderContract.EPISODE_LIST_URI,
                null, "", null, null
        );
        int count = 0;
        Log.d("task","while");
        while (queryCursor.moveToNext()) {
            String item_title = queryCursor.getString(queryCursor.getColumnIndex(PodcastProviderContract.TITLE));
            String item_link = queryCursor.getString(queryCursor.getColumnIndex(PodcastProviderContract.EPISODE_LINK));
            String item_date = queryCursor.getString(queryCursor.getColumnIndex(PodcastProviderContract.DATE));
            String item_description = queryCursor.getString(queryCursor.getColumnIndex(PodcastProviderContract.DESCRIPTION));
            String item_download_link = queryCursor.getString(queryCursor.getColumnIndex(PodcastProviderContract.DOWNLOAD_LINK));
            String item_uri = queryCursor.getString(queryCursor.getColumnIndex(PodcastProviderContract.EPISODE_URI));
            count++;
            itemList.add(new ItemFeed(item_title, item_link, item_date, item_description, item_download_link, item_uri));
        }

        return itemList;
    }

    @Override
    protected void onPostExecute(List<ItemFeed> feed) {
        Toast.makeText(mContext, "terminando...", Toast.LENGTH_SHORT).show();

        //Adapter Personalizado
        XmlFeedAdapter adapter = new XmlFeedAdapter(mContext, R.layout.itemlista, feed);

        //atualizar o list view
        items.setAdapter(adapter);
        items.setTextFilterEnabled(true);

    }
}

