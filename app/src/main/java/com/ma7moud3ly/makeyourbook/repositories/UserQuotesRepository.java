package com.ma7moud3ly.makeyourbook.repositories;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */
import android.content.Context;

import com.ma7moud3ly.makeyourbook.data.Quote;
import com.ma7moud3ly.makeyourbook.storages.MyRoomDatabase;
import com.ma7moud3ly.makeyourbook.storages.quotes.SaveQuote;
import com.ma7moud3ly.makeyourbook.storages.quotes.SaveQuoteDao;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class UserQuotesRepository {

    private SaveQuoteDao dao;
    private LiveData<List<SaveQuote>> list;
    private MutableLiveData<Boolean> exists=new MutableLiveData<>();

    @Inject
    public UserQuotesRepository(Context context) {
        MyRoomDatabase db = MyRoomDatabase.getDatabase(context.getApplicationContext());
        dao = db.saveQuoteDao();
        list = dao.getAll();
    }

    public LiveData<List<SaveQuote>> getAll() {
        return list;
    }


    public void insert(Quote quote) {
        SaveQuote row = new SaveQuote(quote.toString());
        MyRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.insert(row);
        });
    }

    public void clear() {
        MyRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.deleteAll();
        });
    }

    public void delete(String entry) {
        MyRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.delete(entry);
        });
    }

    public void update(SaveQuote row) {
        MyRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.update(row);
        });
    }

    public MutableLiveData<Boolean> isExists(String entry) {
        exists.setValue(false);
        MyRoomDatabase.databaseWriteExecutor.execute(() -> {
            exists.postValue(dao.exists(entry));
        });
        return exists;
    }

}
