package br.ufpe.cin.if710.podcast;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by eduardo on 09/10/2017.
 */

public class Util {

    private static HashMap<String, MediaPlayer> mediaPlayerRegistry  = new HashMap<String, MediaPlayer>();

    public static void addReg(String link, MediaPlayer mp){
        mediaPlayerRegistry.put(link, mp);
    }

    public static void stopAllPlayers(){

        Set<Map.Entry<String, MediaPlayer>> entrySet = mediaPlayerRegistry.entrySet();
        Iterator iter = entrySet.iterator();

        while (iter.hasNext()) {
            MediaPlayer player = (MediaPlayer) iter.next();
            if (player != null && player.isPlaying()) {
                player.release();
            }
        }
        mediaPlayerRegistry.clear();

    }

    public static HashMap<String, MediaPlayer> getMPReg() {
        return mediaPlayerRegistry;
    }

    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void verifyPermissions(Activity activity, String[] allPermissionNeeded) {
        // Check if we have write permission
        for (int i = 0; i < allPermissionNeeded.length; i++) {
            int permission = ActivityCompat.checkSelfPermission(activity, allPermissionNeeded[i]);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                        activity,
                        allPermissionNeeded,
                        1
                );
                i = allPermissionNeeded.length;
            }

        }

    }

    public static boolean hasPermissions(Context context, String[] allPermissionNeeded)
    {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && context != null && allPermissionNeeded != null)
            for (String permission : allPermissionNeeded)
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                    return false;
        return true;
    }

}
