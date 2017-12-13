package br.ufpe.cin.if710.podcast;

import android.app.Application;
import android.content.Context;

import com.frogermcs.androiddevmetrics.AndroidDevMetrics;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import br.ufpe.cin.if710.podcast.db.AppDatabase;

/**
 * Created by eduardo on 15/10/2017.
 */

public class PodcastApp extends Application {

    private static boolean activityVisible;
    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        PodcastApp application = (PodcastApp) context.getApplicationContext();
        return application.refWatcher;
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }

        refWatcher = LeakCanary.install(this);

        if (BuildConfig.DEBUG) {
            AndroidDevMetrics.initWith(this);
        }

    }

    public AppDatabase getDatabase(){
        return AppDatabase.getInstance(this);
    }

    public DataRepository getRepository(){
        return DataRepository.getInstance(getDatabase());
    }
}
