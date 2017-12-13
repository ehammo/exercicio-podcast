package br.ufpe.cin.if710.podcast.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import br.ufpe.cin.if710.podcast.PodcastApp;
import br.ufpe.cin.if710.podcast.db.entity.ItemFeedEntity;

/**
 * Created by pedro on 13/12/17.
 */

public class ItemFeedListViewModel extends AndroidViewModel{

    private final MediatorLiveData<List<ItemFeedEntity>> mObservableItemFeed;

    public ItemFeedListViewModel(@NonNull Application application) {
        super(application);

        mObservableItemFeed = new MediatorLiveData<>();

        mObservableItemFeed.setValue(null);

        LiveData<List<ItemFeedEntity>> itemFeedList = ((PodcastApp) application).getRepository().getItemFeedList();

        mObservableItemFeed.addSource(itemFeedList, mObservableItemFeed::setValue);
    }

    public LiveData<List<ItemFeedEntity>> getItemFeedList() {
        return mObservableItemFeed;
    }

}
