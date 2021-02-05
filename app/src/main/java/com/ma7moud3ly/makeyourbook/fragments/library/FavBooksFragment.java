package com.ma7moud3ly.makeyourbook.fragments.library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ma7moud3ly.makeyourbook.R;
import com.ma7moud3ly.makeyourbook.data.Book;
import com.ma7moud3ly.makeyourbook.adapters.BookRecyclerAdapter;
import com.ma7moud3ly.makeyourbook.databinding.FragmentFavBooksBinding;
import com.ma7moud3ly.makeyourbook.fragments.BaseFragment;
import com.ma7moud3ly.makeyourbook.models.FavBooksViewModel;
import com.ma7moud3ly.makeyourbook.storages.fav.Favourite;
import com.ma7moud3ly.makeyourbook.util.CONSTANTS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

public class FavBooksFragment extends BaseFragment {
    private FragmentFavBooksBinding binding;
    private BookRecyclerAdapter adapter;
    private List<Book> list = new ArrayList<>();
    private FavBooksViewModel model;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentFavBooksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(getActivity(), viewModelFactory).get(FavBooksViewModel.class);
        model.data.observe(getViewLifecycleOwner(), favourites -> {
            if (favourites != null) {
                list.clear();
                for (Favourite fav : favourites) list.add(fav.toBook());
                Collections.reverse(list);
                if (adapter != null)
                    adapter.notifyDataSetChanged();
                networkState(CONSTANTS.LOADED);
                if (favourites.size() > 0) binding.noFavBooks.setVisibility(View.GONE);
                else binding.noFavBooks.setVisibility(View.VISIBLE);
            }
        });


        initBooksRecycler();
    }


    private void initBooksRecycler() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        adapter = new BookRecyclerAdapter(list);
        initRecycler(binding.booksRecycler, adapter, gridLayoutManager);
    }

    @Override
    public void onResume() {
        super.onResume();
        uiState.isHome.set(false);
        setTitle(getString(R.string.books_fav));
        showSearch(false);
    }
}