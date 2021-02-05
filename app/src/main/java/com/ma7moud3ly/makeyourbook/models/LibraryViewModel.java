package com.ma7moud3ly.makeyourbook.models;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */
import com.ma7moud3ly.makeyourbook.data.Book;
import com.ma7moud3ly.makeyourbook.data.MyPager;
import com.ma7moud3ly.makeyourbook.repositories.LibraryRepository;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LibraryViewModel extends ViewModel {
    public MutableLiveData<List<Book>> data = new MutableLiveData<>();
    public MutableLiveData<Long> items_count = new MutableLiveData<>();
    public String ref = "";
    private LibraryRepository repo;


    @Inject
    public LibraryViewModel(LibraryRepository repo) {
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

    public boolean nullOrEmpty() {
        return data.getValue() == null || data.getValue().isEmpty();
    }

}
