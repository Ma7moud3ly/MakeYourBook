package com.ma7moud3ly.makeyourbook.models;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */
import com.ma7moud3ly.makeyourbook.data.Book;
import com.ma7moud3ly.makeyourbook.repositories.LibraryRepository;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class LibrarySearchModel extends ViewModel {

    public MutableLiveData<List<Book>> data = new MutableLiveData<>();
    private LibraryRepository repo;

    @Inject
    public LibrarySearchModel(LibraryRepository repo) {
        this.repo = repo;
        data = repo.data;
    }

    public void search(String query) {
        repo.search(query);
    }

    public boolean nullOrEmpty() {
        return data.getValue() == null;
    }

}
