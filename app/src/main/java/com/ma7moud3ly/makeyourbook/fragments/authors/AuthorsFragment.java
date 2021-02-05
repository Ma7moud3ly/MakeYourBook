package com.ma7moud3ly.makeyourbook.fragments.authors;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ma7moud3ly.makeyourbook.adapters.AuthorsRecyclerAdapter;
import com.ma7moud3ly.makeyourbook.data.Author;
import com.ma7moud3ly.makeyourbook.databinding.FragmentLibraryBinding;
import com.ma7moud3ly.makeyourbook.fragments.BaseFragment;
import com.ma7moud3ly.makeyourbook.models.AuthorsViewModel;
import com.ma7moud3ly.makeyourbook.util.CONSTANTS;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

public class AuthorsFragment extends BaseFragment {

    private FragmentLibraryBinding binding;
    private AuthorsRecyclerAdapter adapter;
    private List<Author> list = new ArrayList<>();
    private AuthorsViewModel model;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentLibraryBinding.inflate(inflater, container, false);
        binding.setUi(uiState);
        uiState.library.set(CONSTANTS.LIB_AUTHORS);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initAuthorsRecycler();
        initPager();

        model = new ViewModelProvider(getActivity(), viewModelFactory).get(AuthorsViewModel.class);
        model.items_count.observe(getViewLifecycleOwner(), items_count -> {
            if (items_count == null) return;
            pager.last_page = (int) Math.ceil(items_count * 1.0 / pager.page_size);
            model.read(pager);
        });
        model.data.observe(this, authors -> {
            if (authors != null && authors.size() > 0) {
                pager.last_key = authors.get(authors.size() - 1).id;
                this.list.addAll(authors);
                adapter.notifyDataSetChanged();
                networkState(CONSTANTS.LOADED);
            }
        });

        read();
    }


    private void initAuthorsRecycler() {
        int divider = 3;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), divider, GridLayoutManager.VERTICAL, false);
        adapter = new AuthorsRecyclerAdapter(list, divider);
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
