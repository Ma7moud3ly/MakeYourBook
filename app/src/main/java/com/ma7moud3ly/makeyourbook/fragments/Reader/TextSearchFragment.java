package com.ma7moud3ly.makeyourbook.fragments.Reader;
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
import com.ma7moud3ly.makeyourbook.activities.BaseActivity;
import com.ma7moud3ly.makeyourbook.adapters.TextSearchRecyclerAdapter;
import com.ma7moud3ly.makeyourbook.data.TextSearch;
import com.ma7moud3ly.makeyourbook.databinding.FragmentSearchBinding;
import com.ma7moud3ly.makeyourbook.fragments.BaseFragment;
import com.ma7moud3ly.makeyourbook.models.TextReaderViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

public class TextSearchFragment extends BaseFragment {
    private FragmentSearchBinding binding;
    private TextSearchRecyclerAdapter adapter;
    private TextReaderViewModel model;
    List<TextSearch> searchResults = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        binding.setUi(uiState);
        model = new ViewModelProvider(getActivity(), viewModelFactory).get(TextReaderViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.search.setHint(getString(R.string.story_search_title));
        initSearch(binding.appBar, binding.search);
        initSearchRecycler();
    }


    private void initSearchRecycler() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        adapter = new TextSearchRecyclerAdapter(searchResults);
        initRecycler(binding.searchRecycler, adapter, gridLayoutManager, true);
    }


    @Override
    public void onSearch(String query) {
        searchResults.clear();
        int i = 0, last_index = 0;
        for (String page : model.chapters.getValue()) {
            while (true) {
                TextSearch temp = new TextSearch();
                int n = page.indexOf(query, last_index + query.length());
                last_index = n;
                if (n == -1) break;
                temp.position = n;
                int from = n, to;
                if (n - 50 > 0) from = n - 50;
                if (n + 100 < page.length()) to = n + 100;
                else to = page.length();
                temp.text = page.substring(from, to);
                temp.text = temp.text.replace(query, "<font color='red'>" + query + "</font>");
                temp.text += "...";
                temp.page = i;
                temp.query = query;
                searchResults.add(temp);
            }
            i++;
        }
        uiState.searchResults.set(searchResults.size());
    }

    @Override
    public void onItemSelected(int index) {
        model.storySearch.setValue(searchResults.get(index));
        ((BaseActivity)getActivity()).navigateTo(new TextReaderFragment(), null, true);
       // back();
    }

    @Override
    public void onResume() {
        super.onResume();
        uiState.showFooter.set(false);
        uiState.showHeader.set(false);
        requestFocus(binding.search);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        uiState.searchResults.set(0);
        uiState.showFooter.set(true);
        uiState.showHeader.set(true);
    }
}