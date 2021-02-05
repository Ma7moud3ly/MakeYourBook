package com.ma7moud3ly.makeyourbook.fragments.quotes;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ma7moud3ly.makeyourbook.R;
import com.ma7moud3ly.makeyourbook.adapters.QuotesRecyclerAdapter;
import com.ma7moud3ly.makeyourbook.data.Quote;
import com.ma7moud3ly.makeyourbook.databinding.FragmentSearchBinding;
import com.ma7moud3ly.makeyourbook.fragments.BaseFragment;
import com.ma7moud3ly.makeyourbook.models.QuotesViewModel;
import com.ma7moud3ly.makeyourbook.util.CONSTANTS;
import com.ma7moud3ly.makeyourbook.util.RecyclerPagination;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

public class QuotesSearchFragment extends BaseFragment {
    private FragmentSearchBinding binding;
    private QuotesRecyclerAdapter adapter;
    private List<Quote> list = new ArrayList<>();
    private QuotesViewModel model;
    private RecyclerPagination pagination;
    private String author_id;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        binding.setUi(uiState);
        if (getArguments() != null && getArguments().containsKey("author_id"))
            author_id = getArguments().getString("author_id");
        else
            back();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.search.setHint(getString(R.string.quote_search_title));
        initSearch(binding.appBar, binding.search);
        initSearchRecycler();
        model = new ViewModelProvider(this, viewModelFactory).get(QuotesViewModel.class);
        model.data.observe(this, results -> {
            if (results == null || adapter == null) return;
            this.list.clear();
            uiState.searchResults.set(results.size());
            networkState(CONSTANTS.LOADED);
            initPagination(results.size());
        });
    }

    private void initPagination(int size) {
        pagination = new RecyclerPagination(binding.searchRecycler, size, CONSTANTS.PAGE_MAX_ITEMS);
        pagination.observer.observe(this, pager -> {
            if (pager == null) return;
            int start = pager.current_page;
            int end = pager.last_page;
            ArrayList<Quote> temp = new ArrayList<>();
            for (int i = start; i < end; i++) {
                Quote quote = model.data.getValue().get(i);
                temp.add(quote);
            }
            list.addAll(temp);
            adapter.notifyDataSetChanged();
        });
    }


    private void initSearchRecycler() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        adapter = new QuotesRecyclerAdapter(list);
        initRecycler(binding.searchRecycler, adapter, gridLayoutManager);
    }


    @Override
    public void onSearch(String query) {
        networkState(CONSTANTS.LOADING);
        model.search(author_id,query);
    }

    @Override
    public void onResume() {
        super.onResume();
        requestFocus(binding.search);
        uiState.showFooter.set(false);
        uiState.showHeader.set(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        uiState.searchResults.set(0);
        uiState.showFooter.set(true);
        uiState.showHeader.set(true);
    }
}