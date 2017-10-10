package br.ufpe.cin.if710.podcast.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import br.ufpe.cin.if710.podcast.ui.MainActivity;
import br.ufpe.cin.if710.podcast.util.GetFromDatabase;

/**
 * Created by eduardo on 09/10/2017.
 */

public class PodcastReceiver extends BroadcastReceiver {

    Context mContext;
    ListView items;

    public PodcastReceiver(){}

    public PodcastReceiver(Context context, ListView items){
        mContext = context;
        this.items = items;
    }

    public void onReceive(Context context, Intent intent) {
        Log.d("service", "receive");
        // Chamado quando o DownloadXMLService retorna com o XML baixado e colocado no banco de dados
        Toast.makeText(mContext, "Itens carregados do XML pelo service", Toast.LENGTH_SHORT).show();

        // Carregar view com os dados do banco
        (new GetFromDatabase(mContext, items)).execute();
    }
}
