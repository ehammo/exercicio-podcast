package br.ufpe.cin.if710.podcast.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.icu.text.UnicodeSetSpanner;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.leakcanary.RefWatcher;

import java.io.File;

import br.ufpe.cin.if710.podcast.PodcastApp;
import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.Util;
import br.ufpe.cin.if710.podcast.db.PodcastDBHelper;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.services.DownloadXMLService;
import br.ufpe.cin.if710.podcast.ui.adapter.XmlFeedAdapter;

public class EpisodeDetailActivity extends Activity {

    private TextView mTitleTV;
    private TextView mDescTV;
    private TextView mDateTV;
    private TextView mEpisodeLink;
    private Button mDownloadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_detail);
        this.mTitleTV = findViewById(R.id.title_tv);
        this.mEpisodeLink = findViewById(R.id.link_tv);
        this.mDescTV = findViewById(R.id.description_tv);
        this.mDateTV = findViewById(R.id.pubDate_tv);
        this.mDownloadBtn = findViewById(R.id.downloadBtn);

        final Bundle bundle = this.getIntent().getExtras();

        final ItemFeed item = new ItemFeed(bundle.getString(PodcastDBHelper.EPISODE_TITLE),
                bundle.getString(PodcastDBHelper.EPISODE_LINK),
                bundle.getString(PodcastDBHelper.EPISODE_DATE),
                bundle.getString(PodcastDBHelper.EPISODE_DESC),
                bundle.getString(PodcastDBHelper.EPISODE_DOWNLOAD_LINK),
                bundle.getString(PodcastDBHelper.EPISODE_FILE_URI),
                bundle.getInt(PodcastDBHelper.EPISODE_CURRENT_TIME));

        this.mTitleTV.setText(item.getTitle());
        this.mDescTV.setText(item.getDescription());
        this.mDateTV.setText(item.getPubDate());
        this.mEpisodeLink.setText(item.getLink());

        this.mDownloadBtn.setText("Download");
        this.mDownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(item.getUri());
                if(!file.exists()){
                    if(!DownloadXMLService.isDownloading){
                        Toast.makeText(getApplicationContext(), "Starting download", Toast.LENGTH_LONG).show();
                        DownloadXMLService.startActionDownloadPodcast(getApplicationContext(), item);
                    }else{
                        Toast.makeText(getApplicationContext(), "Already downloading", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Already downloaded", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        RefWatcher refWatcher = PodcastApp.getRefWatcher(this);
        refWatcher.watch(this);
    }
}
