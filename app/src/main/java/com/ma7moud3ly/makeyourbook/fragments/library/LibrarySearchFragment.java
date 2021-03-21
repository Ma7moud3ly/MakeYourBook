package com.ma7moud3ly.makeyourbook.fragments.library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ma7moud3ly.makeyourbook.R;
import com.ma7moud3ly.makeyourbook.adapters.BookRecyclerAdapter;
import com.ma7moud3ly.makeyourbook.data.Book;
import com.ma7moud3ly.makeyourbook.databinding.FragmentSearchBinding;
import com.ma7moud3ly.makeyourbook.fragments.BaseFragment;
import com.ma7moud3ly.makeyourbook.models.LibrarySearchModel;
import com.ma7moud3ly.makeyourbook.util.CONSTANTS;
import com.ma7moud3ly.makeyourbook.util.RecyclerPagination;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

public class LibrarySearchFragment extends BaseFragment {
    private FragmentSearchBinding binding;
    private BookRecyclerAdapter adapter;
    private List<Book> list = new ArrayList<>();
    private LibrarySearchModel model;
    private RecyclerPagination pagination;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        binding.setUi(uiState);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.search.setHint(getString(R.string.search_title));
        initSearch(binding.appBar, binding.search);
        initSearchRecycler();
        model = new ViewModelProvider(this, viewModelFactory).get(LibrarySearchModel.class);
        model.data.observe(this, results -> {
            if (results == null || adapter == null) {
                uiState.searchResults.set(0);
                return;
            }
            ;
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
            if (start >= model.data.getValue().size()) return;
            int end = pager.last_page;
            ArrayList<Book> temp = new ArrayList<>();
            for (int i = start; i < end; i++) {
                Book book = model.data.getValue().get(i);
                temp.add(book);
            }
            list.addAll(temp);
            adapter.notifyDataSetChanged();
        });
    }

    private void initSearchRecycler() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        adapter = new BookRecyclerAdapter(list);
        initRecycler(binding.searchRecycler, adapter, gridLayoutManager);
    }


    @Override
    public void onSearch(String query) {
        networkState(CONSTANTS.LOADING);
        model.search(query);
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
        uiState.searchResults.set(-1);
        uiState.showFooter.set(true);
        uiState.showHeader.set(true);
    }
}