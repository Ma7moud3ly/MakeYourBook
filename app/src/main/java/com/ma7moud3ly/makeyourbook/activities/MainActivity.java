package com.ma7moud3ly.makeyourbook.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseUser;
import com.ma7moud3ly.makeyourbook.App;
import com.ma7moud3ly.makeyourbook.data.Book;
import com.ma7moud3ly.makeyourbook.databinding.ActivityMainBinding;
import com.ma7moud3ly.makeyourbook.di.DaggerActivityGraph;
import com.ma7moud3ly.makeyourbook.fragments.HomeFragment;
import com.ma7moud3ly.makeyourbook.fragments.library.LibrarySearchFragment;

public class MainActivity extends BaseActivity {
    public ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //request the dependencies from dagger
        activityGraph = DaggerActivityGraph.factory().create(this);
        activityGraph.inject(this);

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.setUi(uiState);

        setContentView(binding.getRoot());

        binding.retryLayout.retry.setOnClickListener(v -> {
            if (!App.isLoggedIn) login();
        });


        retry = binding.retryLayout.retry;
        binding.headerLayout.searchBox.setOnClickListener(v ->
                navigateTo(new LibrarySearchFragment(), null, true));

        login();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (checkForLink(intent))
            handleIntent(intent);
    }


    private void handleIntent(Intent intent) {
        Uri appLinkData = intent.getData();
        String id = appLinkData.getLastPathSegment();
        String ref = appLinkData.getPath().replace(id, "").replace("/", "");
        if (ref.equals("author") && !id.isEmpty()) {
            uiState.openAuthor(new View(this), id);
        } else if (ref.equals("text-book") || ref.equals("pdf-book")) {
            Intent bookIntent = new Intent(this, BookActivity.class);
            bookIntent.putExtra("book_id", id);
            bookIntent.putExtra("book_id", id);
            Book book = new Book();
            book.id = id;
            book.is_text = ref.equals("text-book") ? true : false;
            uiState.bookDetails(new View(this), book);
        } else if (ref.equals("quote")) {
            uiState.quotes(new View(this));
        }
    }

    @Override
    public void onLogin(FirebaseUser user) {
        if (pref.get("first", true)) {
            subAllNotifications();
            createNotificationChannel();
        }
        navigateTo(new HomeFragment(), null, false);
        if (checkForLink(getIntent())) handleIntent(getIntent());
    }

    private boolean checkForLink(Intent intent) {
        return (Intent.ACTION_VIEW.equals(intent.getAction()) && intent.getData() != null);
    }

    @Override
    public void onBackPressed() {
        back(new View(this));
    }

    private void subAllNotifications() {
        subscribeToTopics("books");
        subscribeToTopics("authors");
        subscribeToTopics("quotes");
        pref.put("first", false);
    }

}
