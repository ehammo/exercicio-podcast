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

    private static PodcastDBHelper mPodcastDBHelper;
    private Context mContext;
    private final SQLiteDatabase db;

    public PodcastProvider() {
        db = this.mPodcastDBHelper.getWritableDatabase();
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

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
        Uri returnUri;

        Long id = db.insertOrThrow(PodcastProviderContract.EPISODE_TABLE, null, values);

        if (id > 0) {
            returnUri = ContentUris.withAppendedId(PodcastProviderContract.EPISODE_LIST_URI, id);
            Log.d("PodcastProvider", returnUri.toString());
        } else {
            throw new android.database.SQLException("falha na inserção em: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        mContext = this.getContext();
        mPodcastDBHelper = PodcastDBHelper.getInstance(mContext);
        return false;
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

        int numUpdated = db.update(PodcastProviderContract.EPISODE_TABLE,
                values,
                selection,
                selectionArgs);

       if (numUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
       }

        return numUpdated;
    }
}
