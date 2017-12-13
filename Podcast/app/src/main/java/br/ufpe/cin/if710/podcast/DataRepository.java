package br.ufpe.cin.if710.podcast;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import java.util.List;

import br.ufpe.cin.if710.podcast.db.AppDatabase;
import br.ufpe.cin.if710.podcast.db.entity.ItemFeedEntity;

/**
 * Created by pedro on 13/12/17.
 */

public class DataRepository {

    private static DataRepository sInstance;

    private final AppDatabase mDatabase;

    private MediatorLiveData<List<ItemFeedEntity>> mObservableItemFeedList;

    private DataRepository(final AppDatabase database) {
        mDatabase = database;

        mObservableItemFeedList = new MediatorLiveData<>();

        mObservableItemFeedList.addSource(mDatabase.itemFeedDAO().getAll(), itemFeedList -> {
            mObservableItemFeedList.postValue(itemFeedList);
        });

    }

    public static DataRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<ItemFeedEntity>> getItemFeedList() {
        return mObservableItemFeedList;
    }

    public LiveData<ItemFeedEntity> getItemFeed(String downloadLink) {
        return mDatabase.itemFeedDAO().load(downloadLink);
    }

    public int updateLiveItemUri(String downloadLink, String uri) {
        return mDatabase.itemFeedDAO().updateItemFeedUri(downloadLink, uri);
    }

    public long[] insertAll(List<ItemFeedEntity> itemFeedList) {
        return mDatabase.itemFeedDAO().insertAll(itemFeedList);
    }

}
