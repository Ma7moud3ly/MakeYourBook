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
import com.ma7moud3ly.makeyourbook.activities.QuoteActivity;
import com.ma7moud3ly.makeyourbook.adapters.QuotesHomeRecyclerAdapter;
import com.ma7moud3ly.makeyourbook.data.Author;
import com.ma7moud3ly.makeyourbook.databinding.FragmentQuoteHomeBinding;
import com.ma7moud3ly.makeyourbook.fragments.BaseFragment;
import com.ma7moud3ly.makeyourbook.models.HomeViewModel;
import com.ma7moud3ly.makeyourbook.util.CONSTANTS;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

public class QuotesHomeFragment extends BaseFragment {
    private FragmentQuoteHomeBinding binding;
    private HomeViewModel model;
    private List<Author> list = new ArrayList<>();
    private QuotesHomeRecyclerAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentQuoteHomeBinding.inflate(inflater, container, false);
        binding.setUi(uiState);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        networkState(CONSTANTS.LOADING);
        initBooksRecycler();
        model = new ViewModelProvider(this, viewModelFactory).get(HomeViewModel.class);
        model.data.observe(this, home -> {
            if (home == null || home.quotes == null) {
                networkState(CONSTANTS.RETRY);
                return;
            }
            networkState(CONSTANTS.LOADED);
            list.clear();
            list.addAll(home.quotes);
            if (adapter != null) adapter.notifyDataSetChanged();
        });
        model.read();
    }

    private void initBooksRecycler() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        adapter = new QuotesHomeRecyclerAdapter(list);
        initRecycler(binding.quotesRecycler, adapter, gridLayoutManager);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((QuoteActivity) getActivity()).binding.headerLayout.title.setText(getText(R.string.quotes));
        uiState.showSearch.set(false);
    }
}