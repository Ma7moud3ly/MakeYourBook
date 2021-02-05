package com.ma7moud3ly.makeyourbook.fragments.book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ma7moud3ly.makeyourbook.adapters.QuotesRecyclerAdapter;
import com.ma7moud3ly.makeyourbook.data.Book;
import com.ma7moud3ly.makeyourbook.data.Quote;
import com.ma7moud3ly.makeyourbook.databinding.FragmentUserQuotesBinding;
import com.ma7moud3ly.makeyourbook.fragments.BaseFragment;
import com.ma7moud3ly.makeyourbook.models.UserQuotesViewModel;
import com.ma7moud3ly.makeyourbook.storages.quotes.SaveQuote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

public class UserQuotesFragment extends BaseFragment {
    private FragmentUserQuotesBinding binding;
    private QuotesRecyclerAdapter adapter;
    private List<Quote> quotes = new ArrayList<>();
    private Book book;
    private UserQuotesViewModel model;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = FragmentUserQuotesBinding.inflate(inflater, container, false);

        if (getArguments() != null && getArguments().containsKey("book"))
            book = new Book(getArguments().getString("book")).init();
        else back();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(getActivity(), viewModelFactory).get(UserQuotesViewModel.class);
        model.getAll().observe(this, list -> {
            quotes.clear();
            for (SaveQuote saveQuote : list) {
                Quote quote = new Quote(saveQuote.quote).init();
                if (quote.book_id.equals(book.id)) quotes.add(quote);
            }
            Collections.reverse(quotes);
            if (adapter != null)
                adapter.notifyDataSetChanged();
            if (!quotes.isEmpty()) binding.noSavedQuotes.setVisibility(View.GONE);
            else binding.noSavedQuotes.setVisibility(View.VISIBLE);
        });

        initQuotesRecycler();


    }

    private void initQuotesRecycler() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        adapter = new QuotesRecyclerAdapter(quotes, model);
        initRecycler(binding.savedQuotesRecycler, adapter, gridLayoutManager);
    }

    @Override
    public void onResume() {
        uiState.showHeader.set(true);
        uiState.showSearch.set(false);
        super.onResume();
    }
}