package com.ma7moud3ly.makeyourbook.models;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */
import com.ma7moud3ly.makeyourbook.repositories.FavouriteRepository;
import com.ma7moud3ly.makeyourbook.storages.fav.Favourite;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FavBooksViewModel extends ViewModel {
    public LiveData<List<Favourite>> data = new MutableLiveData<>();
    private FavouriteRepository repo;

    @Inject
    public FavBooksViewModel(FavouriteRepository repo) {
        this.repo = repo;
        data = repo.getAll();
    }

}
