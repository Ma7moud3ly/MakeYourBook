package com.ma7moud3ly.makeyourbook.fragments.authors;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ma7moud3ly.makeyourbook.R;
import com.ma7moud3ly.makeyourbook.adapters.BookRecyclerAdapter;
import com.ma7moud3ly.makeyourbook.data.Author;
import com.ma7moud3ly.makeyourbook.data.Book;
import com.ma7moud3ly.makeyourbook.databinding.FragmentAboutAuthorBinding;
import com.ma7moud3ly.makeyourbook.fragments.BaseFragment;
import com.ma7moud3ly.makeyourbook.models.AuthorsViewModel;
import com.ma7moud3ly.makeyourbook.util.CONSTANTS;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import static com.ma7moud3ly.makeyourbook.util.CONSTANTS.PAGE_MAX_ITEMS;

public class AboutAuthorFragment extends BaseFragment {
    private FragmentAboutAuthorBinding binding;
    private AuthorsViewModel model;
    private ArrayList<Book> list = new ArrayList<>();
    private BookRecyclerAdapter adapter;
    private String author_id;
    private int index = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentAboutAuthorBinding.inflate(inflater, container, false);
        binding.setUi(uiState);
        uiState.library.set(CONSTANTS.ALL_BOOKS);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initBooksRecycler();
        model = new ViewModelProvider(this, viewModelFactory).get(AuthorsViewModel.class);
        model.author.observe(this, author -> {
            if (author != null) {
                networkState(CONSTANTS.LOADED);
                binding.setAuthor(author);
                int all = author.e_books.size() + author.txt_books.size() + author.pdf_books.size();
                binding.booksAll.setText(getText(R.string.all_books) + " : " + all);
                binding.booksE.setText(getText(R.string.e_books) + " : " + author.e_books.size());
                binding.booksTxt.setText(getText(R.string.text_books) + " : " + author.txt_books.size());
                binding.booksPdf.setText(getText(R.string.pdf_books) + " : " + author.pdf_books.size());
                model.initBooks(CONSTANTS.ALL_BOOKS);
                Collections.shuffle(model.books);
                filterBooks(CONSTANTS.ALL_BOOKS);
            }
        });

        getAuthor();

        binding.booksAll.setOnClickListener(v -> filterBooks(CONSTANTS.ALL_BOOKS));
        binding.booksE.setOnClickListener(v -> filterBooks(CONSTANTS.E_BOOKS));
        binding.booksTxt.setOnClickListener(v -> filterBooks(CONSTANTS.TXT_BOOKS));
        binding.booksPdf.setOnClickListener(v -> filterBooks(CONSTANTS.PDF_BOOKS));
        binding.authorSmallImage.setOnClickListener(v -> {
            binding.aboutAuthorContent.scrollTo(0, 0);
        });
        binding.more.setOnClickListener(v -> {
            index += PAGE_MAX_ITEMS;
            loadMore();
        });
        binding.aboutAuthorContent.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)
                (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                    if (scrollY < 100) expandUi(true);
                    else expandUi(false);
                });
        expandUi(true);
    }

    private void expandUi(boolean b) {
        binding.authorSmallImage.setVisibility(b ? View.GONE : View.VISIBLE);
        binding.title.setGravity(b ? Gravity.CENTER : Gravity.START);
    }

    private void filterBooks(int from) {
        list.clear();
        index = 0;
        model.initBooks(from);
        uiState.library.set(from);
        binding.aboutAuthorContent.scrollTo(0, 0);
        binding.more.setVisibility(model.books.size() > PAGE_MAX_ITEMS ? View.VISIBLE : View.GONE);
        loadMore();
    }

    private void getAuthor() {
        if (getArguments() != null && getArguments().containsKey("author")) {
            Author author = new Author(getArguments().getString("author")).init();
            model.author.setValue(author);
        } else if (getArguments() != null && getArguments().containsKey("author_id")) {
            author_id = getArguments().getString("author_id");
            model.read(author_id);
            super.read();
        }
    }

    private void initBooksRecycler() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        adapter = new BookRecyclerAdapter(list);
        initRecycler(binding.booksRecycler, adapter, gridLayoutManager);

    }

    private void loadMore() {
        if(model.books.size()==0){
            list.addAll(new ArrayList<>());
            adapter.notifyDataSetChanged();
            return;
        }
        if (index >= model.books.size()) {
            binding.more.setVisibility(View.GONE);
            return;
        }
        int start, end;
        start = index;
        if (index + PAGE_MAX_ITEMS < model.books.size()) end = index + PAGE_MAX_ITEMS;
        else end = model.books.size();
        ArrayList<Book> temp = new ArrayList<>();
        for (int i = start; i < end; i++) {
            Book book = model.books.get(i);
            temp.add(book);
        }
        list.addAll(temp);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onResume() {
        super.onResume();
        showFooter(false);
        showHeader(false);
    }

    @Override
    public void onDestroyView() {
        model.author.setValue(null);
        super.onDestroyView();
    }

    @Override
    public void onNetworkRetry() {
        model.read(author_id);
        super.onNetworkRetry();
    }
}