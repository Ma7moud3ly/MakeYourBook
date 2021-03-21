package com.ma7moud3ly.makeyourbook.fragments.library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ma7moud3ly.makeyourbook.adapters.LibraryRecyclerAdapter;
import com.ma7moud3ly.makeyourbook.data.Book;
import com.ma7moud3ly.makeyourbook.databinding.FragmentLibraryBinding;
import com.ma7moud3ly.makeyourbook.fragments.BaseFragment;
import com.ma7moud3ly.makeyourbook.models.LibraryViewModel;
import com.ma7moud3ly.makeyourbook.util.CONSTANTS;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

public class LibraryTextFragment extends BaseFragment {

    private FragmentLibraryBinding binding;
    private LibraryRecyclerAdapter adapter;
    private List<Book> list = new ArrayList<>();
    private LibraryViewModel model;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentLibraryBinding.inflate(inflater, container, false);
        binding.setUi(uiState);
        uiState.library.set(CONSTANTS.LIB_TXT_BOOKS);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initBooksRecycler();
        initPager();
        model = new ViewModelProvider(this, viewModelFactory).get(LibraryViewModel.class);
        model.ref=CONSTANTS.TXT_BOOKS_DIR;
        model.items_count.observe(getViewLifecycleOwner(), items_count -> {
            if (items_count == null) return;
            pager.last_page = (int) Math.ceil(items_count * 1.0 / pager.page_size);
            model.read(pager);
        });
        model.data.observe(this, books -> {
            if (books != null && books.size() > 0) {
                pager.last_key = books.get(books.size() - 1).id;
                this.list.addAll(books);
                adapter.notifyDataSetChanged();
                networkState(CONSTANTS.LOADED);
            }
        });

        read();
    }

    private void initBooksRecycler() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        adapter = new LibraryRecyclerAdapter(list, 4);
        initRecycler(binding.recycler, adapter, gridLayoutManager, true);
    }

    private boolean pageNext() {
        if (pager.current_page < pager.last_page) {
            pager.current_page += 1;
            return true;
        }
        return false;
    }

    @Override
    public void read() {
        list.clear();
        adapter.notifyDataSetChanged();
        model.count();
        super.read();
    }

    @Override
    public void onNetworkRetry() {
        super.onNetworkRetry();
        model.read(pager);
    }

    @Override
    public void onScrollToBottom() {
        if (!pager.last_key.isEmpty() && pageNext()) {
            networkState(CONSTANTS.LOADING);
            model.read(pager);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showFooter(true);
        showSearch(true);
    }
}
