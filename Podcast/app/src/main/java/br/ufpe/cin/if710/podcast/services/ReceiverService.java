package br.ufpe.cin.if710.podcast.services;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import br.ufpe.cin.if710.podcast.receivers.PodcastReceiver;

/**
 * Created by eduardo on 13/12/2017.
 */

public class ReceiverService extends IntentService
{
    private static BroadcastReceiver m_ScreenOffReceiver;

    public ReceiverService() {
        super("ReceiverService");
    }


    @Override
    public void onHandleIntent(Intent intent) {
        if (intent != null) {
            PodcastReceiver podcastReceiver = (PodcastReceiver) intent.getExtras().getSerializable("receiver");
            if(podcastReceiver!=null){
                registerScreenOffReceiver(podcastReceiver);
            }else{
                Log.e("receiverService", "receiver nulo");
            }
        }
    }

    @Override
    public void onDestroy()
    {
        unregisterReceiver(m_ScreenOffReceiver);
        m_ScreenOffReceiver = null;
    }

    private void registerScreenOffReceiver(PodcastReceiver podcastReceiver)
    {

        m_ScreenOffReceiver = podcastReceiver;
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(m_ScreenOffReceiver, filter);
    }
}
