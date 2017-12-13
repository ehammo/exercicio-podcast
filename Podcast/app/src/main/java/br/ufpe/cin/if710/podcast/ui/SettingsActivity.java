package br.ufpe.cin.if710.podcast.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.squareup.leakcanary.RefWatcher;

import br.ufpe.cin.if710.podcast.PodcastApp;
import br.ufpe.cin.if710.podcast.R;

public class SettingsActivity extends Activity {
    public static final String FEED_LINK = "feedlink";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d("LeakCanary","Settings parou");
    }

    @Override
    protected void onDestroy(){
        Log.d("LeakCanary","Settings foi destruido");
        RefWatcher refWatcher = PodcastApp.getRefWatcher(this);
        refWatcher.watch(this);
        super.onDestroy();
    }

    public static class FeedPreferenceFragment extends PreferenceFragment {

        protected static final String TAG = "FeedPreferenceFragment";
        private SharedPreferences.OnSharedPreferenceChangeListener mListener;
        private Preference feedLinkPref;
        private SharedPreferences prefs;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // carrega preferences de um recurso XML em /res/xml
            addPreferencesFromResource(R.xml.preferences);

            // pega o valor atual de FeedLink
            feedLinkPref = getPreferenceManager().findPreference(FEED_LINK);

            // cria listener para atualizar summary ao modificar link do feed
            mListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    feedLinkPref.setSummary(sharedPreferences.getString(FEED_LINK, getActivity().getResources().getString(R.string.feed_link)));
                }
            };

            // pega objeto SharedPreferences gerenciado pelo PreferenceManager deste fragmento
            prefs = getPreferenceManager().getSharedPreferences();

            // registra o listener no objeto SharedPreferences
            prefs.registerOnSharedPreferenceChangeListener(mListener);

            // força chamada ao metodo de callback para exibir link atual
            mListener.onSharedPreferenceChanged(prefs, FEED_LINK);
        }

        @Override
        public void onStop(){
            super.onStop();
        }

        @Override
        public void onDestroy(){
            prefs.unregisterOnSharedPreferenceChangeListener(mListener);
            RefWatcher refWatcher = PodcastApp.getRefWatcher(getActivity());
            refWatcher.watch(this);
            super.onDestroy();
        }
    }
}