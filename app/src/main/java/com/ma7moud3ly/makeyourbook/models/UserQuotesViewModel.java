package com.ma7moud3ly.makeyourbook.models;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */
import android.view.View;

import com.ma7moud3ly.makeyourbook.data.Quote;
import com.ma7moud3ly.makeyourbook.storages.quotes.SaveQuote;
import com.ma7moud3ly.makeyourbook.repositories.UserQuotesRepository;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class UserQuotesViewModel extends ViewModel {
    private final UserQuotesRepository repo;
    private LiveData<List<SaveQuote>> all;

    @Inject
    public UserQuotesViewModel(UserQuotesRepository repo) {
        this.repo = repo;
        all = repo.getAll();
    }

    public void insert(Quote quote) {
        repo.insert(quote);
    }

    public void delete(View v, Quote entry) {
        repo.delete(entry.toString());
    }

    public void clear() {
        repo.clear();
    }

    public LiveData<List<SaveQuote>> getAll() {
        return all;
    }

}