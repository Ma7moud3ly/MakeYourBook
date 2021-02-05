package com.ma7moud3ly.makeyourbook.fragments;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ma7moud3ly.makeyourbook.R;
import com.ma7moud3ly.makeyourbook.activities.MainActivity;
import com.ma7moud3ly.makeyourbook.adapters.AuthorRecyclerAdapter;
import com.ma7moud3ly.makeyourbook.adapters.CollectionsRecyclerAdapter;
import com.ma7moud3ly.makeyourbook.data.Author;
import com.ma7moud3ly.makeyourbook.data.Collection;
import com.ma7moud3ly.makeyourbook.databinding.FragmentHomeBinding;
import com.ma7moud3ly.makeyourbook.fragments.library.LibrarySearchFragment;
import com.ma7moud3ly.makeyourbook.models.HomeViewModel;
import com.ma7moud3ly.makeyourbook.util.CONSTANTS;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

public class HomeFragment extends BaseFragment {
    private FragmentHomeBinding binding;
    private HomeViewModel model;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.setUi(uiState);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        model = new ViewModelProvider(getActivity(), viewModelFactory).get(HomeViewModel.class);
        model.data.observe(getActivity(), home -> {
            if (home != null) {
                initCollectionsRecycler(home.collections);
                initAuthorsRecycler(home.authors);
                networkState(CONSTANTS.LOADED);
            }
        });

        read();
    }

    @Override
    public void read() {
        model.read();
        //super.read();
    }

    private void initAuthorsRecycler(List<Author> authors) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        AuthorRecyclerAdapter adapter = new AuthorRecyclerAdapter(authors);
        initRecycler(binding.authorsRecycler, adapter, gridLayoutManager);
        adapter.notifyDataSetChanged();
    }

    private void initCollectionsRecycler(List<Collection> collections) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        CollectionsRecyclerAdapter adapter = new CollectionsRecyclerAdapter(collections);
        initRecycler(binding.collectionsRecycler, adapter, gridLayoutManager);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search) {
            ((MainActivity) getActivity()).navigateTo(new LibrarySearchFragment(), null, true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        uiState.library.set(CONSTANTS.LIB_MOST_READ);
        super.onResume();
        uiState.isHome.set(true);
        showSearch(true);
        uiState.showFooter.set(true);
    }

    @Override
    public void onNetworkRetry() {
        model.read();
        super.onNetworkRetry();
    }
}