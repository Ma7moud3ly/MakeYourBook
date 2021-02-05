package com.ma7moud3ly.makeyourbook.activities;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ma7moud3ly.makeyourbook.data.Book;
import com.ma7moud3ly.makeyourbook.databinding.ActivityBookBinding;
import com.ma7moud3ly.makeyourbook.di.DaggerActivityGraph;
import com.ma7moud3ly.makeyourbook.fragments.book.AboutBookFragment;
import com.ma7moud3ly.makeyourbook.models.BookViewModel;
import com.ma7moud3ly.makeyourbook.util.CONSTANTS;

import androidx.lifecycle.ViewModelProvider;

public class BookActivity extends BaseActivity {
    public ActivityBookBinding binding;
    public BookViewModel model;
    public Book books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //request the dependencies from dagger
        activityGraph = DaggerActivityGraph.factory().create(this);
        activityGraph.inject(this);

        super.onCreate(savedInstanceState);

        binding = ActivityBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.setUi(uiState);

        model = new ViewModelProvider(this, viewModelFactory).get(BookViewModel.class);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("book")) {
            Book book = new Book(intent.getStringExtra("book")).init();
            model.book.setValue(book);
        } else if (intent != null && intent.hasExtra("book_id")) {
            String book_id = intent.getStringExtra("book_id");
            model.ref = intent.getStringExtra("ref");
            model.read(book_id);
            uiState.state.set(CONSTANTS.LOADING);
        }
        model.book.observe(this, book -> {
            if (book != null) {
                uiState.state.set(CONSTANTS.LOADED);
                navigateTo(new AboutBookFragment(), null, false);
            } else {
                uiState.state.set(CONSTANTS.RETRY);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        back(new View(this));
    }

}
