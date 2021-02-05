package com.ma7moud3ly.makeyourbook.activities;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */
import android.os.Bundle;
import android.view.View;

import com.ma7moud3ly.makeyourbook.R;
import com.ma7moud3ly.makeyourbook.databinding.ActivityQuoteBinding;
import com.ma7moud3ly.makeyourbook.di.DaggerActivityGraph;
import com.ma7moud3ly.makeyourbook.fragments.library.LibrarySearchFragment;
import com.ma7moud3ly.makeyourbook.fragments.quotes.QuotesHomeFragment;
import com.ma7moud3ly.makeyourbook.fragments.quotes.QuotesSearchFragment;

public class QuoteActivity extends BaseActivity {

    public ActivityQuoteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //request the dependencies from dagger
        activityGraph = DaggerActivityGraph.factory().create(this);
        activityGraph.inject(this);

        super.onCreate(savedInstanceState);

        binding = ActivityQuoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.setUi(uiState);
        uiState.showHeader.set(true);
        binding.headerLayout.searchLabel.setText(getText(R.string.quote_search_title));

        navigateTo(new QuotesHomeFragment(), null, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        back(new View(this));
    }

}
