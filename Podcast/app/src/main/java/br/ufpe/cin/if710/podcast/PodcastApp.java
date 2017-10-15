package br.ufpe.cin.if710.podcast;

import android.app.Application;

/**
 * Created by eduardo on 15/10/2017.
 */

public class PodcastApp extends Application {


    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    private static boolean activityVisible;
}
