package br.ufpe.cin.if710.podcast;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by eduardo on 09/10/2017.
 */

public class util {
    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
