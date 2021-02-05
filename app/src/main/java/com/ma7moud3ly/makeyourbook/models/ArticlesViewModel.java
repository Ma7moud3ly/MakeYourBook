package com.ma7moud3ly.makeyourbook.models;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */
import com.ma7moud3ly.makeyourbook.data.Article;
import com.ma7moud3ly.makeyourbook.data.Book;
import com.ma7moud3ly.makeyourbook.data.MyPager;
import com.ma7moud3ly.makeyourbook.repositories.ArticlesRepository;
import com.ma7moud3ly.makeyourbook.repositories.LibraryRepository;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ArticlesViewModel extends ViewModel {
    public MutableLiveData<List<Article>> data = new MutableLiveData<>();
    public MutableLiveData<Long> items_count = new MutableLiveData<>();
    private ArticlesRepository repo;


    @Inject
    public ArticlesViewModel(ArticlesRepository repo) {
        this.repo = repo;
        data = repo.data;
        items_count = repo.items_count;
    }

    public void read(MyPager pager) {
        repo.read(pager);
    }

    public void count() {
        repo.count();
    }

}
