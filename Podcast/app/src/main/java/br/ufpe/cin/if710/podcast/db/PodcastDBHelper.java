package br.ufpe.cin.if710.podcast.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PodcastDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_TABLE = "episodes";
    public final static String _ID = "_id";
    public final static String EPISODE_TITLE = "title";
    public final static String EPISODE_DATE = "pubDate";
    public final static String EPISODE_LINK = "link";
    public final static String EPISODE_DESC = "description";
    public final static String EPISODE_DOWNLOAD_LINK = "downloadLink";
    public final static String EPISODE_FILE_URI = "downloadUri";
    public final static String EPISODE_CURRENT_TIME = "currentTime";
    public final static String[] columns = {
            _ID, EPISODE_TITLE, EPISODE_DATE, EPISODE_LINK,
            EPISODE_DESC, EPISODE_DOWNLOAD_LINK, EPISODE_CURRENT_TIME, EPISODE_FILE_URI
    };
    private static final String DATABASE_NAME = "podcasts.db";
    private static final int DB_VERSION = 1;
    final private static String CREATE_CMD =
            "CREATE TABLE "+DATABASE_TABLE+" (" + _ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + EPISODE_TITLE + " TEXT NOT NULL, "
                    + EPISODE_DATE + " TEXT NOT NULL, "
                    + EPISODE_LINK + " TEXT UNIQUE NOT NULL, "
                    + EPISODE_DESC + " TEXT NOT NULL, "
                    + EPISODE_DOWNLOAD_LINK + " TEXT NOT NULL, "
                    + EPISODE_CURRENT_TIME + " TEXT NOT NULL, "
                    + EPISODE_FILE_URI + " TEXT NOT NULL)";
    private static PodcastDBHelper db;

    private PodcastDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    public static PodcastDBHelper getInstance(Context c) {
        if (db == null) {
            db = new PodcastDBHelper(c.getApplicationContext());
        }
        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("helper","onCreate");
        sqLiteDatabase.execSQL(CREATE_CMD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        throw new RuntimeException("inutilizado");
    }
}
