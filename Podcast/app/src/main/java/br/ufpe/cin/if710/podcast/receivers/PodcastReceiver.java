package br.ufpe.cin.if710.podcast.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;

import java.io.Serializable;

import br.ufpe.cin.if710.podcast.PodcastApp;
import br.ufpe.cin.if710.podcast.ui.MainActivity;

import static android.content.Context.NOTIFICATION_SERVICE;
import static br.ufpe.cin.if710.podcast.services.DownloadXMLService.UPDATE_DATA_BROADCAST;

/**
 * Created by eduardo on 09/10/2017.
 */
public class PodcastReceiver extends BroadcastReceiver implements Serializable {

    private Context mContext;

    public PodcastReceiver(Context context){
        mContext = context;
    }

    public void onReceive(Context context, Intent intent) {
        Log.d("receiver", "receive");
        if(intent != null){
            final String action = intent.getAction();
            if (UPDATE_DATA_BROADCAST.equals(action)) {
                notifyUser();
            }
        }

    }

    public void notifyUser(){
        if(!PodcastApp.isActivityVisible()){
            final Intent notificationIntent = new Intent(mContext, MainActivity.class);
            final PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);

            final Notification notification = new Notification.Builder(
                    mContext)
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setOngoing(false).setContentTitle("Download finalizado!")
                    .setContentText("Clique para acessar a lista de episódios.")
                    .setContentIntent(pendingIntent).build();

            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(1, notification);
        }
    }

}
