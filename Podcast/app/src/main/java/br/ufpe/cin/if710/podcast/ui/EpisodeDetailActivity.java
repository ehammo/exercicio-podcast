package br.ufpe.cin.if710.podcast.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.ui.adapter.XmlFeedAdapter;

public class EpisodeDetailActivity extends Activity {

    private TextView mTitleTV;
    private TextView mDescTV;
    private TextView mDateTV;
    private Button mDownloadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_detail);

        this.mTitleTV = findViewById(R.id.title_tv);
        this.mDescTV = findViewById(R.id.description_tv);
        this.mDateTV = findViewById(R.id.pubDate_tv);
        this.mDownloadBtn = findViewById(R.id.downloadBtn);

        Bundle bundle = this.getIntent().getExtras();

        this.mTitleTV.setText(bundle.getString(XmlFeedAdapter.TITLE_EXTRA));
        this.mDescTV.setText(bundle.getString(XmlFeedAdapter.DESCRIPTION_EXTRA));
        this.mDateTV.setText(bundle.getString(XmlFeedAdapter.PUBDATE_EXTRA));

        this.mDownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo:Transform download in a service and execute this service here
            }
        });
    }
}
