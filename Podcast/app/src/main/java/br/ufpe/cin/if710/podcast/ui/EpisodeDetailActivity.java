package br.ufpe.cin.if710.podcast.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.leakcanary.RefWatcher;

import java.io.File;

import br.ufpe.cin.if710.podcast.PodcastApp;
import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.db.entity.ItemFeedEntity;
import br.ufpe.cin.if710.podcast.services.DownloadXMLService;
import br.ufpe.cin.if710.podcast.viewmodel.ItemFeedViewModel;

public class EpisodeDetailActivity extends AppCompatActivity {

    private TextView mTitleTV;
    private TextView mDescTV;
    private TextView mDateTV;
    private TextView mEpisodeLink;
    private Button mDownloadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AppCompat_DayNight);
        setContentView(R.layout.activity_episode_detail);
        this.mTitleTV = findViewById(R.id.title_tv);
        this.mEpisodeLink = findViewById(R.id.link_tv);
        this.mDescTV = findViewById(R.id.description_tv);
        this.mDateTV = findViewById(R.id.pubDate_tv);
        this.mDownloadBtn = findViewById(R.id.downloadBtn);

        final Bundle bundle = this.getIntent().getExtras();

        final String downloadLink = bundle.getString("downloadLink");

        ItemFeedViewModel.Factory factory = new ItemFeedViewModel.Factory(getApplication(), downloadLink);

        final ItemFeedViewModel viewModel = ViewModelProviders.of(this, factory).get(ItemFeedViewModel.class);

        viewModel.getItemFeed().observe(this,
                (@Nullable ItemFeedEntity itemFeed) -> {
                    if (itemFeed != null) {
                        this.mTitleTV.setText(itemFeed.getTitle());
                        this.mDescTV.setText(itemFeed.getDescription());
                        this.mDateTV.setText(itemFeed.getPubDate());
                        this.mEpisodeLink.setText(itemFeed.getLink());

                        this.mDownloadBtn.setText("Download");
                        this.mDownloadBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                File file = new File(itemFeed.getUri());
                                if (!file.exists()) {
                                    if (!DownloadXMLService.isDownloading) {
                                        Toast.makeText(getApplicationContext(), "Starting download", Toast.LENGTH_LONG).show();
                                        DownloadXMLService.startActionDownloadPodcast(getApplicationContext(), itemFeed);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Already downloading", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Already downloaded", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
    }

    @Override
    public void onDestroy() {
        RefWatcher refWatcher = PodcastApp.getRefWatcher(this);
        refWatcher.watch(this);
        super.onDestroy();
    }
}
