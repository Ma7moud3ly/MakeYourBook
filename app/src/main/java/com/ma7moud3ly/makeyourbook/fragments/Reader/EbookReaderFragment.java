

package com.ma7moud3ly.makeyourbook.fragments.Reader;
/**
 * اصنع كتابك Make your Book
 *
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 20204
 */

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ma7moud3ly.makeyourbook.App;
import com.ma7moud3ly.makeyourbook.R;
import com.ma7moud3ly.makeyourbook.data.Book;
import com.ma7moud3ly.makeyourbook.data.EBook;
import com.ma7moud3ly.makeyourbook.databinding.FragmentEbookReaderBinding;
import com.ma7moud3ly.makeyourbook.databinding.ItemDrawerHeaderBinding;
import com.ma7moud3ly.makeyourbook.fragments.BaseFragment;
import com.ma7moud3ly.makeyourbook.models.EbookReaderViewModel;
import com.ma7moud3ly.makeyourbook.util.CONSTANTS;
import com.ma7moud3ly.makeyourbook.util.FilesHelper;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;

public class EbookReaderFragment extends BaseFragment {
    private FragmentEbookReaderBinding binding;
    private EbookReaderViewModel model;
    private Book book;
    private final String CLASS_NAME = getClass().getSimpleName();
    private Drawer navDrawer;
    private int scroll = 0;
    private String chapter = "";
    private boolean isFirst = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentEbookReaderBinding.inflate(inflater, container, false);
        model = new ViewModelProvider(getActivity(), viewModelFactory).get(EbookReaderViewModel.class);
        binding.setUi(eReaderUiState);
        if (getArguments() != null && getArguments().containsKey("book"))
            book = new Book(getArguments().getString("book")).init();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUi();
        restoreSettings();

        model.chapterContent.observe(this, content -> {
            if (content == null) return;
            eReaderUiState.content = content;
            eReaderUiState.updateContent();
            if (isFirst) {
                binding.pageContainer.postDelayed(() -> {
                    binding.pageContainer.scrollTo(0, scroll);
                }, 200);
                isFirst = false;
            } else
                binding.pageContainer.scrollTo(0, 0);
            int index = model.chapterIndex(chapter);
            binding.pageTop.setText((index + 1) + " - " + model.allChapters.length);
            binding.pageBottom.setText((index + 1) + " - " + model.allChapters.length);
            binding.chapterProgress.setProgress(index);
        });

        model.bookPath.observe(this, dir -> {
            File path;
            if (dir == null || !(path = new File(dir, "content.json")).exists()) {
                networkState(CONSTANTS.RETRY);
                return;
            }
            networkState(CONSTANTS.LOADED);
            binding.ebookTitle.setText(book.name);
            EBook ebook = new EBook(FilesHelper.read(path.getPath())).init();
            ebook.dir = dir;
            model.ebook.setValue(ebook);
            model.allChapters = ebook.contents.keySet().toArray(new String[ebook.contents.keySet().size()]);
            binding.chapterProgress.setMax(model.allChapters.length - 1);

            if (chapter.equals("")) chapter = model.allChapters[0];

            model.openChapter(chapter);
            initNavDrawer();
        });

        networkState(CONSTANTS.LOADING);
        binding.ebookIndex.setVisibility(View.GONE);

        model.read(book.id);

    }

    private void initNavDrawer() {
        File bookImg = new File(App.DATA_DIR, book.img);
        ItemDrawerHeaderBinding header = ItemDrawerHeaderBinding.inflate(getLayoutInflater());
        if (bookImg.exists()) {
            RequestOptions options = new RequestOptions().placeholder(R.drawable.icon2).error(R.drawable.icon2);
            header.headerBook.setText(book.name);
            header.headerAuthor.setText(book.author);
            Glide.with(this)
                    .load(bookImg.getAbsolutePath())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .apply(options)
                    .into(header.headerImage);
        }
        List<IDrawerItem> items = new ArrayList<>();
        final Typeface tf = ResourcesCompat.getFont(getContext(), R.font.cairo_regular);
        EBook ebook = model.ebook.getValue();
        for (Map.Entry<String, String> entry : ebook.chapters.entrySet()) {
            String title = entry.getValue();
            PrimaryDrawerItem item = new PrimaryDrawerItem()
                    .withTag(entry.getKey())
                    .withName(title)
                    .withTypeface(tf);
            items.add(item);
        }

        navDrawer = new DrawerBuilder()
                .withActivity(getActivity())
                .withDrawerItems(items)
                .withDrawerWidthDp(200)
                .withDisplayBelowStatusBar(true)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    chapter = drawerItem.getTag().toString();
                    model.openChapter(chapter);
                    return false;
                })
                .withDrawerGravity(GravityCompat.START)
                .build();

        if (bookImg.exists()) navDrawer.setHeader(header.getRoot());
        navDrawer.setSelection(0);
        binding.ebookIndex.setVisibility(View.VISIBLE);
        binding.ebookIndex.setOnClickListener(v -> navDrawer.openDrawer());
    }

    private void initUi() {


        WebSettings settings = binding.editor.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(false);
        eReaderUiState.editor = binding.editor;

        GestureDetector detector = new GestureDetector(getActivity(), new GestureTap());
        binding.editor.setOnTouchListener((view, motionEvent) -> {
            detector.onTouchEvent(motionEvent);
            return false;
        });

        getActivity().getWindow().setNavigationBarColor(Color.WHITE);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.font_names));

       /* binding.fontType.setAdapter(adapter);
        binding.fontType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                eReaderUiState.selectedFont = CONSTANTS.fonts[i];
                eReaderUiState.buildTypeFace(getContext());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });*/

        binding.chapterProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if (isFirst) return;
                String ch = model.allChapters[progress];
                if (!ch.equals(chapter)) {
                    chapter = ch;
                    model.openChapter(chapter);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        binding.forward.setOnClickListener(v -> nexChapter(true));
        binding.forwardTop.setOnClickListener(v -> nexChapter(true));
        binding.forwardBottom.setOnClickListener(v -> nexChapter(true));
        binding.backward.setOnClickListener(v -> nexChapter(false));
        binding.backwardTop.setOnClickListener(v -> nexChapter(false));
        binding.backwardBottom.setOnClickListener(v -> nexChapter(false));

    }

    private void nexChapter(boolean forward) {
        int progress = binding.chapterProgress.getProgress();
        int max = binding.chapterProgress.getMax();
        if (forward && progress < max) progress++;
        if (!forward && progress != 0) progress--;
        binding.chapterProgress.setProgress(progress);
    }

    class GestureTap extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            eReaderUiState.toggleFullscreen(new View(getContext()));
            return true;
        }
    }

    private void storeSettings() {
        pref.put(CLASS_NAME + "_font", binding.fontType.getSelectedItemPosition());

        pref.put(CLASS_NAME + "_text_color", eReaderUiState.textColor.get());
        pref.put(CLASS_NAME + "_text_size", eReaderUiState.getTextSize());
        pref.put(CLASS_NAME + "_align", eReaderUiState.textAlignment.get());
        pref.put(CLASS_NAME + "_justify", eReaderUiState.isJustify.get());
        pref.put(CLASS_NAME + "_font_weight", eReaderUiState.fontWeight.get());
        pref.put(CLASS_NAME + "_line_height", eReaderUiState.lineHeight.get());
        pref.put(CLASS_NAME + "_font_style", eReaderUiState.fontStyle.get());
        pref.put(CLASS_NAME + "_show_tools", eReaderUiState.showTools.get());
        pref.put(CLASS_NAME + "_full_screen", eReaderUiState.fullScreen.get());

        pref.put(CLASS_NAME + "_chapter_" + book.id, chapter);
        pref.put(CLASS_NAME + "_scroll_" + book.id, binding.pageContainer.getScrollY());

    }

    private void restoreSettings() {

        int selected_font = pref.get(CLASS_NAME + "_font", 1);
        if (selected_font >= CONSTANTS.fonts.length) selected_font = 0;
        binding.fontType.setSelection(selected_font);
        //eReaderUiState.fontFamily = CONSTANTS.fonts[selected_font];
        //eReaderUiState.buildTypeFace(getContext());

        eReaderUiState.textColor.set(pref.get(CLASS_NAME + "_text_color", Color.BLACK));
        binding.textColor.setColorFilter(eReaderUiState.textColor.get(), PorterDuff.Mode.SRC_ATOP);

        eReaderUiState.setTextSize(pref.get(CLASS_NAME + "_text_size", 3));
        eReaderUiState.textAlignment.set(pref.get(CLASS_NAME + "_align", "center"));
        eReaderUiState.isJustify.set(pref.get(CLASS_NAME + "_justify", true));

        eReaderUiState.fontWeight.set(pref.get(CLASS_NAME + "_font_weight", "normal"));
        eReaderUiState.lineHeight.set(pref.get(CLASS_NAME + "_line_height", "normal"));
        eReaderUiState.fontStyle.set(pref.get(CLASS_NAME + "_font_style", "normal"));

        eReaderUiState.showTools.set(pref.get(CLASS_NAME + "_show_tools", false));
        eReaderUiState.fullScreen.set(pref.get(CLASS_NAME + "_full_screen", false));
        eReaderUiState.setFullScreen(getActivity(), eReaderUiState.fullScreen.get());

        chapter = pref.get(CLASS_NAME + "_chapter_" + book.id, "");
        scroll = pref.get(CLASS_NAME + "_scroll_" + book.id, 0);
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
        if (eReaderUiState.fullScreen.get()) eReaderUiState.fullScreen.set(false);
    }

}