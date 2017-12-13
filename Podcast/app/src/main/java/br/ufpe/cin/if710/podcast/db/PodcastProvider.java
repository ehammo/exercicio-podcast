package br.ufpe.cin.if710.podcast.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class PodcastProvider extends ContentProvider {

    private PodcastDBHelper mPodcastDBHelper;
    private Context mContext;

    public PodcastProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = this.mPodcastDBHelper.getWritableDatabase();

        int numDeleted = db.delete(PodcastProviderContract.EPISODE_TABLE,
                selection,
                selectionArgs);

        if (numDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numDeleted;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri returnUri = null;
        final SQLiteDatabase db = this.mPodcastDBHelper.getWritableDatabase();

        String link = PodcastProviderContract.EPISODE_LINK;

        if(values.getAsString(link)!=null) {

            Cursor cursor = query(uri, null, link+" = ?", new String[]{values.getAsString(link)}, null);
            int count = cursor.getCount();
            cursor.close();
            if(count<=0){
                Log.d("PodcastProvider",values.getAsString(link));

                Log.d("PodcastProvider", "CommonInsert");
                Long id = db.insert(PodcastProviderContract.EPISODE_TABLE,
                        null,
                        values);

                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(PodcastProviderContract.EPISODE_LIST_URI, id);
                    Log.d("PodcastProvider", returnUri.toString());
                } else {
                    throw new android.database.SQLException("falha na inserção em: " + uri);
                }
            }else{
//Somehow update without changing currentTime or uri unless download_link changed
//                long id = db.update(PodcastProviderContract.EPISODE_TABLE,
//                        values,
//                        link + "= \"" + values.getAsString(link) + "\"",
//                        null);
            }
        }else{
            throw new android.database.SQLException("falha na inserção em: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public boolean onCreate() {
        mContext = this.getContext();
        mPodcastDBHelper = PodcastDBHelper.getInstance(mContext);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = this.mPodcastDBHelper.getReadableDatabase();

        Cursor cursor = db.query(PodcastProviderContract.EPISODE_TABLE,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final SQLiteDatabase db = this.mPodcastDBHelper.getWritableDatabase();
        Log.d("PodcastProvider", "update");

        int numUpdated = db.update(PodcastProviderContract.EPISODE_TABLE,
                values,
                selection,
                selectionArgs);

       if (numUpdated != 0) {
           Log.d("PodcastProvider", "updated");

           Log.d("PodcastProvider", "testing");

           Log.d("PodcastProvider", "end testing");


           getContext().getContentResolver().notifyChange(uri, null);
       }

       return numUpdated;
    }

}
