package com.ma7moud3ly.makeyourbook.repositories;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */
import android.content.Context;

import com.ma7moud3ly.makeyourbook.data.Book;
import com.ma7moud3ly.makeyourbook.storages.fav.Favourite;
import com.ma7moud3ly.makeyourbook.storages.fav.FavouriteDao;
import com.ma7moud3ly.makeyourbook.storages.MyRoomDatabase;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class FavouriteRepository {

    private FavouriteDao dao;
    public MutableLiveData<Boolean> exists = new MutableLiveData<>();
    private LiveData<List<Favourite>> favList;

    @Inject
    public FavouriteRepository(Context context) {
        MyRoomDatabase db = MyRoomDatabase.getDatabase(context);
        this.dao = db.favouriteDao();
        this.favList = dao.getAll();
    }

    public LiveData<List<Favourite>> getAll() {
        return favList;
    }

    public void insert(Book book) {
        Favourite row = new Favourite(book.id, book.toString());
        MyRoomDatabase.databaseWriteExecutor.execute(() -> {
            Long i = dao.insert(row);
            exists.postValue(true);
        });
    }

    public void update(Book book) {
        Favourite row = new Favourite(book.id, book.toString());
        MyRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.update(row);
        });
    }

    public void clear() {
        MyRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.deleteAll();
        });
    }

    public void delete(String col) {
        MyRoomDatabase.databaseWriteExecutor.execute(() -> {
            int i = dao.delete(col);
            exists.postValue(false);
        });
    }

    public void isExist(String col) {
        //exists.setValue(false);
        MyRoomDatabase.databaseWriteExecutor.execute(() -> {
            exists.postValue(dao.exists(col));
        });
        //return exists;
    }

}
