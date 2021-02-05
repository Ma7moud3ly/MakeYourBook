package com.ma7moud3ly.makeyourbook.storages.fav;

import android.database.Cursor;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Favourite row);

    @Insert
    long[] insertAll(Favourite[] rows);

    @Query("SELECT * FROM " + Favourite.TABLE_NAME)
    Cursor selectAll();

    @Query("DELETE FROM " + Favourite.TABLE_NAME + " WHERE  _id = :val")
    int delete(String val);

    @Query("DELETE FROM " + Favourite.TABLE_NAME)
    int deleteAll();


    @Query("SELECT * FROM " + Favourite.TABLE_NAME)
    LiveData<List<Favourite>> getAll();

    @Update
    int update(Favourite row);


    @Query("SELECT EXISTS (SELECT 1 FROM " + Favourite.TABLE_NAME + " WHERE _id = :col)")
    boolean exists(String col);

}