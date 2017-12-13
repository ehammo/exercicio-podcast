package br.ufpe.cin.if710.podcast;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.util.Log;

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

        mObservableItemFeedList.addSource(mDatabase.itemFeedDAO().getAll(), new Observer<List<ItemFeedEntity>>() {

            @Override
            public void onChanged(@Nullable List<ItemFeedEntity> itemFeedList) {
                if (mDatabase == null) {
                    Log.d("ARCREPOSITORY", "FODEU");
                }
                Log.d("ARCREPOSITORY", "" + mDatabase.itemFeedDAO().getAll());
                Log.d("ARCREPOSITORY", "" + itemFeedList.size());
                mObservableItemFeedList.postValue(itemFeedList);
            }
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

    public int updateLiveItemUri(String downloadLink, String uri){
        return mDatabase.itemFeedDAO().updateItemFeedUri(downloadLink, uri);
    }

}
