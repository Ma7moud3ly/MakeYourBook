package com.ma7moud3ly.makeyourbook.models;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */
import com.ma7moud3ly.makeyourbook.data.MyPager;
import com.ma7moud3ly.makeyourbook.data.Quote;
import com.ma7moud3ly.makeyourbook.repositories.QuotesRepository;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class QuotesViewModel extends ViewModel {
    public MutableLiveData<List<Quote>> data = new MutableLiveData<>();
    public MutableLiveData<Long> items_count = new MutableLiveData<>();
    private QuotesRepository repo;
    public String ref = "";


    @Inject
    public QuotesViewModel(QuotesRepository repo) {
        this.repo = repo;
        data = repo.data;
        items_count = repo.items_count;
    }

    public void read(MyPager pager) {
        repo.read(pager, ref);
    }

    public void count() {
        repo.count(ref);
    }

    public void search(String ref, String query) {
        repo.search(ref, query);
    }

}
