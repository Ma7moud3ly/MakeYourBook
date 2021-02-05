package com.ma7moud3ly.makeyourbook.models;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */

import com.ma7moud3ly.makeyourbook.data.Author;
import com.ma7moud3ly.makeyourbook.data.Book;
import com.ma7moud3ly.makeyourbook.data.MyPager;
import com.ma7moud3ly.makeyourbook.repositories.AuthorsRepository;
import com.ma7moud3ly.makeyourbook.util.CONSTANTS;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AuthorsViewModel extends ViewModel {
    public MutableLiveData<List<Author>> data = new MutableLiveData<>();
    public List<Book>books=new ArrayList<>();
    public MutableLiveData<Long> items_count = new MutableLiveData<>();
    public MutableLiveData<Author> author = new MutableLiveData<>();
    private AuthorsRepository repo;

    @Inject
    public AuthorsViewModel(AuthorsRepository repo) {
        this.repo = repo;
        data = repo.data;
        items_count = repo.items_count;
        author = repo.author;
    }

    public void initBooks(int load_from) {
        books.clear();
        if (load_from == CONSTANTS.TXT_BOOKS) {
            books.addAll(author.getValue().txt_books);
        } else if (load_from == CONSTANTS.PDF_BOOKS) {
            books.addAll(author.getValue().pdf_books);
        } else if (load_from == CONSTANTS.ALL_BOOKS) {
            books.addAll(author.getValue().txt_books);
            books.addAll(author.getValue().pdf_books);
        }
    }

    public void read(MyPager pager) {
        repo.read(pager);
    }

    public void read(String author_id) {
        repo.read(author_id);
    }

    public void count() {
        repo.count();
    }

    public boolean nullOrEmpty() {
        return data.getValue() == null || data.getValue().isEmpty();
    }

}
