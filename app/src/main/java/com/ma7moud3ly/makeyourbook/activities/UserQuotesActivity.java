package com.ma7moud3ly.makeyourbook.activities;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ma7moud3ly.makeyourbook.App;
import com.ma7moud3ly.makeyourbook.R;
import com.ma7moud3ly.makeyourbook.data.Quote;
import com.ma7moud3ly.makeyourbook.databinding.ActivityUserQuotesBinding;
import com.ma7moud3ly.makeyourbook.di.DaggerActivityGraph;
import com.ma7moud3ly.makeyourbook.models.UserQuotesViewModel;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

public class UserQuotesActivity extends BaseActivity {
    private ActivityUserQuotesBinding binding;
    private UserQuotesViewModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //request the dependencies from dagger
        activityGraph = DaggerActivityGraph.factory().create(this);
        activityGraph.inject(this);
        super.onCreate(savedInstanceState);
        binding = ActivityUserQuotesBinding.inflate(getLayoutInflater());
        model = new ViewModelProvider(this, viewModelFactory).get(UserQuotesViewModel.class);

        View view = binding.getRoot();
        setContentView(view);
        Intent intent;
        if ((intent = getIntent()) != null && intent.hasExtra("quote")) {
            Quote quote = new Quote(intent.getStringExtra("quote")).init();
            binding.setQuote(quote);
            binding.setModel(model);
            binding.btnBack.setOnClickListener(v -> finish());
            binding.btnSave.setOnClickListener(v -> {
                quote.text = binding.quoteText.getText().toString();
                model.insert(quote);
                App.toast(getString(R.string.quote_save_done));
                finish();
            });
        }


    }


}