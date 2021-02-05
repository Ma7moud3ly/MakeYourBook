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

import com.ma7moud3ly.makeyourbook.data.Story;
import com.ma7moud3ly.makeyourbook.databinding.ActivityBookBinding;
import com.ma7moud3ly.makeyourbook.di.DaggerActivityGraph;
import com.ma7moud3ly.makeyourbook.fragments.Reader.TextReaderFragment;
import com.ma7moud3ly.makeyourbook.fragments.Reader.TextSearchFragment;
import com.ma7moud3ly.makeyourbook.models.TextReaderViewModel;

import androidx.lifecycle.ViewModelProvider;

public class ReaderActivity extends BaseActivity {
    public ActivityBookBinding binding;
    public TextReaderViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //request the dependencies from dagger
        activityGraph = DaggerActivityGraph.factory().create(this);
        activityGraph.inject(this);

        super.onCreate(savedInstanceState);

        binding = ActivityBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.setUi(uiState);

        model = new ViewModelProvider(this, viewModelFactory).get(TextReaderViewModel.class);


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("story")) {
            Story story = new Story(intent.getStringExtra("story")).init();
            model.story.setValue(story);
            navigateTo(new TextReaderFragment(), null, false);
        }


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
