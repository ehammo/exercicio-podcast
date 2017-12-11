package br.ufpe.cin.if710.podcast.db;

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
    }

    @Override
    protected List<ItemFeed> doInBackground(Object... params) {
//        Log.d("task","task start");
        List<ItemFeed> itemList = new ArrayList<>();
        Cursor queryCursor = mContext.getContentResolver().query(
                PodcastProviderContract.EPISODE_LIST_URI,
                null, "", null, null
        );
        int count = 0;
        while (queryCursor.moveToNext()) {
            String item_title = queryCursor.getString(queryCursor.getColumnIndex(PodcastDBHelper.EPISODE_TITLE));
            String item_link = queryCursor.getString(queryCursor.getColumnIndex(PodcastDBHelper.EPISODE_LINK));
            String item_date = queryCursor.getString(queryCursor.getColumnIndex(PodcastDBHelper.EPISODE_DATE));
            String item_description = queryCursor.getString(queryCursor.getColumnIndex(PodcastDBHelper.EPISODE_DESC));
            String item_download_link = queryCursor.getString(queryCursor.getColumnIndex(PodcastDBHelper.EPISODE_DOWNLOAD_LINK));
            String item_uri = queryCursor.getString(queryCursor.getColumnIndex(PodcastDBHelper.EPISODE_FILE_URI));
            int item_time = queryCursor.getInt(queryCursor.getColumnIndex(PodcastDBHelper.EPISODE_CURRENT_TIME));
            count++;
            itemList.add(new ItemFeed(item_title, item_link, item_date, item_description, item_download_link, item_uri, item_time));

        }
//        Log.d("task","task ended");
        return itemList;
    }

    @Override
    protected void onPostExecute(List<ItemFeed> feed) {
//        Toast.makeText(mContext, "Getting data from DB", Toast.LENGTH_SHORT).show();

        //Adapter Personalizado
        XmlFeedAdapter adapter = new XmlFeedAdapter(mContext, R.layout.itemlista, feed);

        //atualizar o list view
        items.setAdapter(adapter);
        items.setTextFilterEnabled(true);

    }
}

