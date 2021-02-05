package com.ma7moud3ly.makeyourbook.models;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */
import com.ma7moud3ly.makeyourbook.data.Book;
import com.ma7moud3ly.makeyourbook.repositories.FavouriteRepository;
import com.ma7moud3ly.makeyourbook.repositories.LibraryRepository;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class BookViewModel extends ViewModel {

    private LibraryRepository repo;
    private FavouriteRepository favRepo;
    public MutableLiveData<Book> book = new MutableLiveData<>();
    public MutableLiveData<Boolean> isFaved = new MutableLiveData<>();
    public String ref = "";

    @Inject
    public BookViewModel(LibraryRepository repo, FavouriteRepository favRepo) {
        this.repo = repo;
        book = repo.book;
        this.favRepo = favRepo;
        isFaved = this.favRepo.exists;
    }

    public void read(String id) {
        repo.read(id, ref);
    }

    public void count() {
        repo.count(ref);
    }

    public boolean nullOrEmpty() {
        return book.getValue() == null;
    }

    public void isExist(Book row) {
        favRepo.isExist(row.id);
    }

    public void addFav(Book row) {
        favRepo.insert(row);
    }

    public void delFav(Book row) {
        favRepo.delete(row.id);
    }

}
