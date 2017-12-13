package br.ufpe.cin.if710.podcast.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import br.ufpe.cin.if710.podcast.PodcastApp;
import br.ufpe.cin.if710.podcast.db.entity.ItemFeedEntity;

/**
 * Created by pedro on 13/12/17.
 */

public class ItemFeedViewModel extends AndroidViewModel {

    private final LiveData<ItemFeedEntity> mObservableItemFeed;

    private final String mDownloadLink;

    public ItemFeedViewModel(@NonNull Application application, final String downloadLink) {
        super(application);
        mDownloadLink = downloadLink;

        mObservableItemFeed = ((PodcastApp) application).getRepository().getItemFeed(mDownloadLink);
    }

    public LiveData<ItemFeedEntity> getItemFeed() {
        return mObservableItemFeed;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final Application mApplication;

        private final String mDownloadLink;

        public Factory(@NonNull Application application, String downloadLink) {
            mApplication = application;
            mDownloadLink = downloadLink;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ItemFeedViewModel(mApplication, mDownloadLink);
        }
    }
}
