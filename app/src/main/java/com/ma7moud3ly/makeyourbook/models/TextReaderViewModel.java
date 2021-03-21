package com.ma7moud3ly.makeyourbook.models;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */
import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

import com.ma7moud3ly.makeyourbook.R;
import com.ma7moud3ly.makeyourbook.data.TextSearch;
import com.ma7moud3ly.makeyourbook.repositories.TextReaderRepository;

import java.util.ArrayList;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TextReaderViewModel extends ViewModel {
    public MutableLiveData<ArrayList<String>> chapters;
    public MutableLiveData<Integer> page = new MutableLiveData<>();
    public MutableLiveData<TextSearch> storySearch = new MutableLiveData<>();
    public boolean hasPageNext = true;
    private TextReaderRepository repo;

    @Inject
    public TextReaderViewModel(TextReaderRepository repo) {
        this.repo = repo;
        chapters = repo.data;
    }

    public void read(String ref, String id) {
        repo.read(ref, id + ".txt");
    }

    public void pageNext(boolean next) {
        if (next && page.getValue() < chapters.getValue().size() - 1) {
            page.setValue(page.getValue() + 1);
            hasPageNext = true;
        } else if (!next && page.getValue() > 0) {
            page.setValue(page.getValue() - 1);
            hasPageNext = true;
        } else hasPageNext = false;
    }

    public void pageJump(View v) {
        Context c = v.getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(c.getString(R.string.page_to_jump));

        final EditText input = new EditText(c);
        input.setHint("1 - " + chapters.getValue().size());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setGravity(Gravity.CENTER);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            try {
                int p = (int) Double.parseDouble(input.getText().toString());
                p -= 1;
                if (p >= 0 && p <= chapters.getValue().size()) page.setValue(p);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }


    public boolean isStorySearch() {
        return storySearch.getValue() != null && storySearch.getValue().position != null;
    }

}
