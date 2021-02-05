package com.ma7moud3ly.makeyourbook.storages.quotes;

import android.database.Cursor;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface SaveQuoteDao {


    @Query("SELECT COUNT(*) FROM " + SaveQuote.TABLE_NAME)
    int count();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(SaveQuote row);

    @Insert
    long[] insertAll(SaveQuote[] rows);

    @Query("SELECT * FROM " + SaveQuote.TABLE_NAME)
    Cursor selectAll();

    @Query("DELETE FROM " + SaveQuote.TABLE_NAME + " WHERE " + SaveQuote.COLUMN1 + " = :book")
    int delete(String book);

    @Query("DELETE FROM " + SaveQuote.TABLE_NAME)
    int deleteAll();

    @Query("SELECT * FROM " + SaveQuote.TABLE_NAME + " ORDER BY " + SaveQuote.COLUMN1 + " ASC")
    LiveData<List<SaveQuote>> getAllAlphabetized();

    //@Query("SELECT * FROM " + FavBooks.TABLE_NAME + " ORDER BY " + FavBooks.COLUMN1 + " DESC")
    @Query("SELECT * FROM " + SaveQuote.TABLE_NAME)
    LiveData<List<SaveQuote>> getAll();

    @Update
    int update(SaveQuote row);

    @Query("SELECT EXISTS (SELECT 1 FROM " + SaveQuote.TABLE_NAME + " WHERE " + SaveQuote.COLUMN1 + " = :book)")
    boolean exists(String book);

}