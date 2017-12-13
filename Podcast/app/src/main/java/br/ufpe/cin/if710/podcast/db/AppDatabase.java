package br.ufpe.cin.if710.podcast.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import br.ufpe.cin.if710.podcast.db.dao.ItemFeedDAO;
import br.ufpe.cin.if710.podcast.db.entity.ItemFeedEntity;

/**
 * Created by pedro on 12/12/17.
 */
@Database(entities = ItemFeedEntity.class, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "exercicio-podcast-database";

    private static AppDatabase sInstance;

    public abstract ItemFeedDAO itemFeedDAO();

    public static AppDatabase getInstance(final Context context){
        if ( sInstance == null ){
            return Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
        }
        return sInstance;
    }

}
