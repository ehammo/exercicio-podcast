package br.ufpe.cin.if710.podcast;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
}
