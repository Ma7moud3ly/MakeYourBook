package com.ma7moud3ly.makeyourbook.storages.fav;

import com.ma7moud3ly.makeyourbook.data.Book;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = Favourite.TABLE_NAME)
public class Favourite {
    public static final String DB_NAME = "makeubook";

    public static final String TABLE_NAME = "fav_books";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "_id")
    public String id;
    @NonNull
    @ColumnInfo(name = "data")
    public String data;

    public Favourite(String id, String data) {
        this.id = id;
        this.data = data;
    }

    public Book toBook() {
        return new Book(this.data).init();
    }
}