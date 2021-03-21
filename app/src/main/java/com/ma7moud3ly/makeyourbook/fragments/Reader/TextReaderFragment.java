package com.ma7moud3ly.makeyourbook.fragments.Reader;
/**
 * اصنع كتابك Make your Book
 *
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Html;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ma7moud3ly.makeyourbook.App;
import com.ma7moud3ly.makeyourbook.R;
import com.ma7moud3ly.makeyourbook.activities.BaseActivity;
import com.ma7moud3ly.makeyourbook.activities.UserQuotesActivity;
import com.ma7moud3ly.makeyourbook.data.Quote;
import com.ma7moud3ly.makeyourbook.data.Story;
import com.ma7moud3ly.makeyourbook.databinding.FragmentTextReaderBinding;
import com.ma7moud3ly.makeyourbook.fragments.BaseFragment;
import com.ma7moud3ly.makeyourbook.models.TextReaderViewModel;
import com.ma7moud3ly.makeyourbook.util.CONSTANTS;
import com.ma7moud3ly.makeyourbook.util.CheckInternet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

public class TextReaderFragment extends BaseFragment {
    private FragmentTextReaderBinding binding;
    private TextReaderViewModel model;
    private boolean firstRead = true;
    private int scroll;
    private Story story;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentTextReaderBinding.inflate(inflater, container, false);

        model = new ViewModelProvider(this, viewModelFactory).get(TextReaderViewModel.class);
        binding.setUi(readerUiState);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUi();
        restoreSettings();

        //observe text
        model.chapters.observe(this, chapters -> {
            if (chapters.size() == 0) {
                if (!CheckInternet.isConnected(getContext())) {
                    Toast.makeText(getContext(), getResources().getString(R.string.no_internet_to_download), Toast.LENGTH_LONG).show();
                    networkState(CONSTANTS.RETRY);
                    back();
                }
                return;
            }
            networkState(CONSTANTS.LOADED);
            binding.pages.setText("" + chapters.size());

            int page = pref.get("page_" + story.id, 0);
            if (page >= 0 & page < chapters.size())
                model.page.setValue(page);
        });
        //observe page change
        model.page.observe(this, page -> {
            if (page == -1) return;
            String content = model.chapters.getValue().get(page);
            if (model.isStorySearch()) {
                String query = model.storySearch.getValue().query;
                binding.editor.setText(Html.fromHtml(content.replace(query, "<font color='red'>" + query + "</font>") + "..."));
            } else {
                binding.editor.setText(content + "...");
            }
            binding.page.setText("" + ++page);

            int size = model.chapters.getValue().size();
            binding.pageTop.setText(page + " - " + size);
            binding.pageBottom.setText(page + " - " + size);

            if (model.isStorySearch()) {
                binding.editorLayout.postDelayed(() -> {
                    int position = model.storySearch.getValue().position;
                    int line = binding.editor.getLayout().getLineForOffset(position);
                    scroll = binding.editor.getLayout().getLineTop(line);
                    binding.editorLayout.scrollTo(0, scroll);
                }, 200);
            } else {
                if (firstRead) {
                    binding.editorLayout.setVisibility(View.VISIBLE);
                    readerUiState.isLoading.set(false);
                    binding.editorLayout.postDelayed(() -> {
                        binding.editorLayout.scrollTo(0, scroll);
                    }, 200);
                    firstRead = false;
                } else if (model.hasPageNext)
                    binding.editorLayout.scrollTo(0, 0);
            }
        });

        model.storySearch.observe(getActivity(), textSearch -> {
            if (textSearch == null || (textSearch != null && textSearch.position == null)) return;
            model.page.setValue(textSearch.page);
        });

        //read text source
        if (getArguments() != null && getArguments().containsKey("story")) {
            story = new Story(getArguments().getString("story")).init();
            binding.title.setText(story.title + " - " + story.author);
            if (model.isStorySearch() || firstRead == false) {
                return;
            }
            scroll = pref.get("scroll" + story.id, 0);
            model.read(story.ref, story.id);
            networkState(CONSTANTS.LOADING);
        }

    }

    private void initUi() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.font_names));

        binding.fontType.setAdapter(adapter);
        binding.fontType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                readerUiState.selectedFont = CONSTANTS.fonts[i];
                readerUiState.buildTypeFace(getContext());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        binding.forwardTop.setOnClickListener(v -> model.pageNext(true));
        binding.forwardBottom.setOnClickListener(v -> model.pageNext(true));
        binding.backwardTop.setOnClickListener(v -> model.pageNext(false));
        binding.backwardBottom.setOnClickListener(v -> model.pageNext(false));
        binding.pageTop.setOnClickListener(v -> model.pageJump(v));
        binding.pageBottom.setOnClickListener(v -> model.pageJump(v));
        binding.pageLayout.setOnClickListener(v -> model.pageJump(v));
        binding.textSearch.setOnClickListener(v -> {
            if (model.isStorySearch()) back();
            else ((BaseActivity) v.getContext()).navigateTo(new TextSearchFragment(), null, true);
        });
        binding.editor.setCustomSelectionActionModeCallback(onTextSelect);

    }

    private ActionMode.Callback onTextSelect = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            menu.clear();
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.qoute_selection_menu, menu);
            menu.removeItem(android.R.id.selectAll);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem item) {
            if (item.getItemId() == R.id.save) {
                Quote quote = new Quote();
                TextView ed = binding.editor;
                quote.text = ed.getText().toString().substring(ed.getSelectionStart(), ed.getSelectionEnd());
                quote.source = story.title;
                quote.author = story.author;
                quote.author_id = story.author_id;
                quote.book_id = story.id;
                saveQuoteDialog(quote);
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {

        }
    };

    private void saveQuoteDialog(Quote quote) {
        Intent intent = new Intent(getContext(), UserQuotesActivity.class);
        intent.putExtra("quote", quote.toString());
        startActivity(intent);
    }

    private void storeSettings() {
        pref.put("scroll" + story.id, binding.editorLayout.getScrollY());
        pref.put("align", readerUiState.textAlignment.get());
        pref.put("text_color", readerUiState.textColor.get());
        pref.put("text_size", readerUiState.getTextSize());
        pref.put("font", binding.fontType.getSelectedItemPosition());
        pref.put("is_italic", readerUiState.isItalic.get());
        pref.put("is_bold", readerUiState.isBold.get());
        pref.put("show_tools", readerUiState.showTools.get());
        pref.put("show_header", readerUiState.showHeader.get());
        pref.put("line_spacing", readerUiState.lineSpacing.get());

        if (model.page.getValue() != null)
            pref.put("page_" + story.id, model.page.getValue());
    }

    private void restoreSettings() {
        readerUiState.textAlignment.set(pref.get("align", Gravity.START));
        readerUiState.textColor.set(pref.get("text_color", Color.BLACK));
        binding.textColor.setColorFilter(readerUiState.textColor.get(), PorterDuff.Mode.SRC_ATOP);
        readerUiState.setTextSize(pref.get("text_size", 3));
        readerUiState.isItalic.set(pref.get("is_italic", false));
        readerUiState.isBold.set(pref.get("is_bold", false));
        readerUiState.showTools.set(pref.get("show_tools", true));
        readerUiState.showHeader.set(pref.get("show_header", true));
        readerUiState.lineSpacing.set(pref.get("line_spacing", false));

        int selected_font = pref.get("font", 1);
        if (selected_font >= CONSTANTS.fonts.length) selected_font = 0;
        binding.fontType.setSelection(selected_font);
        readerUiState.selectedFont = CONSTANTS.fonts[selected_font];
        readerUiState.buildTypeFace(getContext());
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        storeSettings();
        super.onPause();
    }

    @Override
    public void back() {
        super.back();
    }
}