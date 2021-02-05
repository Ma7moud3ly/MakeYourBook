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

import com.ma7moud3ly.makeyourbook.activities.QuoteActivity;
import com.ma7moud3ly.makeyourbook.adapters.QuotesRecyclerAdapter;
import com.ma7moud3ly.makeyourbook.data.Quote;
import com.ma7moud3ly.makeyourbook.databinding.FragmentQuotesBinding;
import com.ma7moud3ly.makeyourbook.fragments.BaseFragment;
import com.ma7moud3ly.makeyourbook.models.QuotesViewModel;
import com.ma7moud3ly.makeyourbook.util.CONSTANTS;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

public class QuotesFragment extends BaseFragment {

    private FragmentQuotesBinding binding;
    private QuotesRecyclerAdapter adapter;
    private List<Quote> list = new ArrayList<>();
    private QuotesViewModel model;
    private String author_id;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentQuotesBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initQuotesRecycler();
        initPager();

        model = new ViewModelProvider(this, viewModelFactory).get(QuotesViewModel.class);
        model.items_count.observe(this, items_count -> {
            if (items_count == null) return;
            pager.last_page = (int) Math.ceil(items_count * 1.0 / pager.page_size);
            model.read(pager);
        });
        model.data.observe(this, quotes -> {
            if (quotes != null && quotes.size() > 0) {
                pager.last_key = quotes.get(quotes.size() - 1).id;
                this.list.addAll(quotes);
                adapter.notifyDataSetChanged();
                networkState(CONSTANTS.LOADED);
            }
        });

        if (getArguments() != null && getArguments().containsKey("author_id")) {
            author_id = getArguments().getString("author_id");
            model.ref = author_id;
            read();
        }
        ((QuoteActivity) getActivity()).binding.headerLayout.searchBox.setOnClickListener(v ->
                uiState.quoteSearch(getContext(), author_id));
    }

    private void initQuotesRecycler() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        adapter = new QuotesRecyclerAdapter(list, null);
        initRecycler(binding.quotesRecycler, adapter, gridLayoutManager, true);
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
