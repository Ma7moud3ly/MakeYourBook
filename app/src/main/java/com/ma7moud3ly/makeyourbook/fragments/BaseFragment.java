package com.ma7moud3ly.makeyourbook.fragments;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ma7moud3ly.makeyourbook.App;
import com.ma7moud3ly.makeyourbook.R;
import com.ma7moud3ly.makeyourbook.activities.BaseActivity;
import com.ma7moud3ly.makeyourbook.activities.MainActivity;
import com.ma7moud3ly.makeyourbook.data.MyPager;
import com.ma7moud3ly.makeyourbook.di.ViewModelFactory;
import com.ma7moud3ly.makeyourbook.interfaces.FragmentCallbacks;
import com.ma7moud3ly.makeyourbook.observables.ReaderUi;
import com.ma7moud3ly.makeyourbook.observables.UiState;
import com.ma7moud3ly.makeyourbook.util.CONSTANTS;
import com.ma7moud3ly.makeyourbook.util.CheckInternet;
import com.ma7moud3ly.makeyourbook.util.RecyclerTouchListener;
import com.ma7moud3ly.ustore.UPref;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BaseFragment extends Fragment implements FragmentCallbacks {
    @Inject
    public ViewModelFactory viewModelFactory;
    @Inject
    public UPref pref;
    @Inject
    public UiState uiState;
    @Inject
    public ReaderUi readerUiState;
    public MyPager pager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //request dependencies from dagger
        ((BaseActivity) getActivity()).activityGraph.inject(this);

        if (((BaseActivity) getActivity()).retry != null)
            ((BaseActivity) getActivity()).retry.setOnClickListener(v -> onNetworkRetry());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        networkState(CONSTANTS.LOADED);
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        uiState.isHome.set(false);
    }

    public void initRecycler(RecyclerView recyclerView, RecyclerView.Adapter adapter, GridLayoutManager gridLayoutManager) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void initRecycler(RecyclerView recyclerView, RecyclerView.Adapter adapter, GridLayoutManager gridLayoutManager, boolean addListeners) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        //respond to recycler items click event and call onItemSelected method implemented by several fragments
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                onItemSelected(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        //call onScrollToBottom when the recycler reach the end, this important for pagination
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(-1)) {
                    onScrollToTop();
                } else if (!recyclerView.canScrollVertically(1)) {
                    onScrollToBottom();
                }
            }
        });
    }



    public void showSearch(boolean b) {
        uiState.showHeader.set(true);
        uiState.showSearch.set(b);
    }

    public void showFooter(boolean b) {
        uiState.showFooter.set(b);
    }

    public void showHeader(boolean b) {
        uiState.showHeader.set(b);
    }

    public void initSearch(Toolbar toolbar, EditText searchBox) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }
        toolbar.setNavigationOnClickListener(view -> {
            if (searchBox.getText().toString().trim().isEmpty()) back();
            else searchBox.setText("");
        });
        searchBox.setOnTouchListener((v, event) -> {
            searchBox.setFocusableInTouchMode(true);
            return false;
        });
        searchBox.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = textView.getText().toString().trim();
                if (query.length() > 3) {
                    onSearch(query);
                    hideKeyboard(searchBox);
                } else App.toast(getString(R.string.search_query_short));
            }
            return false;
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void initPager() {
        pager = new MyPager();
        pager.last_key = "";
        pager.current_page = 1;
        pager.page_size = CONSTANTS.PAGE_MAX_ITEMS;
    }

    //on no internet show an error message and retry button
    public void noInternetMessage() {
        // Snackbar.make(getView(), getResources().getText(R.string.no_internet), Snackbar.LENGTH_LONG)
        //         .setAction("Action", null).show();
        //networkState(CONSTANTS.RETRY);
        App.toast(getResources().getText(R.string.no_internet));
    }

    public void networkState(int state) {
        uiState.state.set(state);
    }

    public void setTitle(String title) {
        ((MainActivity) getActivity()).binding.headerLayout.title.setText(title);
    }

    public void back() {
        ((BaseActivity) getActivity()).back();
    }

    public void requestFocus(EditText editText) {
        if (editText.length() > 0) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        } else {
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }
    @Override
    public void onSearch(String query) {

    }

    @Override
    public void read() {
        if (!CheckInternet.isConnected(getActivity())) {
            networkState(CONSTANTS.RETRY);
        } else
            networkState(CONSTANTS.LOADING);
    }

    @Override
    public void onSearchCleared() {

    }

    @Override
    public void onItemSelected(int index) {

    }

    @Override
    public void onScrollToTop() {

    }

    @Override
    public void onScrollToBottom() {

    }

    @Override
    public void onNetworkRetry() {
        if (!CheckInternet.isConnected(getActivity())) {
            networkState(CONSTANTS.RETRY);
        } else
            networkState(CONSTANTS.LOADING);
        //FirebaseDatabase.getInstance().getReference().keepSynced(true);
    }

}
