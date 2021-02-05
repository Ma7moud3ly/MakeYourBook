package com.ma7moud3ly.makeyourbook.storages.quotes;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = SaveQuote.TABLE_NAME)
public class SaveQuote {

    public static final String DB_NAME = "makeubook";

    public static final String TABLE_NAME = "saved_quotes";

    public static final String COLUMN1 = "quote";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = COLUMN1)
    public String quote;

    public SaveQuote(String quote) {
        this.quote = quote;
    }

}