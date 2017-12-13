package br.ufpe.cin.if710.podcast.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.ufpe.cin.if710.podcast.db.entity.ItemFeedEntity;

/**
 * Created by pedro on 12/12/17.
 */
@Dao
public interface ItemFeedDAO {
    @Query("SELECT * FROM ItemFeedEntity")
    LiveData<List<ItemFeedEntity>> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertAll(List<ItemFeedEntity> itemFeedList);

    @Query("UPDATE ItemFeedEntity SET uri = :uri  WHERE downloadLink = :downloadLink")
    int updateItemFeedUri(String downloadLink, String uri);

    @Query("SELECT * from ItemFeedEntity WHERE downloadLink = :downloadLink")
    LiveData<ItemFeedEntity> load(String downloadLink);
}

