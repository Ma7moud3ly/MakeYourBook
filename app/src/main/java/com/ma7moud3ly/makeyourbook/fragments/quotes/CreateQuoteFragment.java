package com.ma7moud3ly.makeyourbook.fragments.quotes;
/**
 * اصنع كتابك Make your Book
 *
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.ma7moud3ly.makeyourbook.App;
import com.ma7moud3ly.makeyourbook.R;
import com.ma7moud3ly.makeyourbook.activities.BaseActivity;
import com.ma7moud3ly.makeyourbook.activities.MainActivity;
import com.ma7moud3ly.makeyourbook.activities.PreviewActivity;
import com.ma7moud3ly.makeyourbook.activities.QuoteActivity;
import com.ma7moud3ly.makeyourbook.data.Quote;
import com.ma7moud3ly.makeyourbook.databinding.FragmentQuoteCreatorBinding;
import com.ma7moud3ly.makeyourbook.fragments.BaseFragment;
import com.ma7moud3ly.makeyourbook.observables.QuoteCreatorUi;
import com.ma7moud3ly.makeyourbook.util.CONSTANTS;
import com.ma7moud3ly.makeyourbook.util.CapAndShare;

import java.io.ByteArrayOutputStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

public class CreateQuoteFragment extends BaseFragment {
    private FragmentQuoteCreatorBinding binding;
    private QuoteCreatorUi creatorUiState;
    private int design_index = 1;
    private int font_index = 0;
    private CapAndShare capAndShare;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentQuoteCreatorBinding.inflate(getLayoutInflater());

        if (getArguments() != null && getArguments().containsKey("quote")) {
            Quote quote = new Quote(getArguments().getString("quote")).init();
            String text = quote.text;
            String signature = quote.author.replace(",", "");
            if (!quote.source.equals("")) signature += " - " + quote.source;
            binding.quote.setText(text);
            binding.quoteSignature.setText(signature);
        }

        capAndShare = new CapAndShare(getContext(), binding.quoteDesign);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        binding.quotesLayout.post(() -> {
            int maxWidth = binding.quotesLayout.getWidth();
            creatorUiState = new QuoteCreatorUi(maxWidth);
            restoreSettings();
            binding.setUi(creatorUiState);
        });

        binding.quoteBackground.post(() -> {
            binding.quote.setMaxHeight(binding.quoteBackground.getHeight() - 150);
        });

        binding.forward.setOnClickListener(v -> changeDesign(++design_index));
        binding.backward.setOnClickListener(v -> changeDesign(--design_index));
        binding.fontForward.setOnClickListener(v -> changeFont(++font_index));
        binding.fontBackward.setOnClickListener(v -> changeFont(--font_index));

        binding.quoteTextColor.setOnClickListener(v -> textColor((ImageView) v));
        binding.quoteSignatureColor.setOnClickListener(v -> signatureColor((ImageView) v));


        binding.quoteSave.setOnClickListener(v -> saveQuote());
        binding.quoteShare.setOnClickListener(v -> shareQuote());
        binding.quotePreview.setOnClickListener(v -> previewQuote());

        binding.appLogo.setOnTouchListener(onTouchListener);
        binding.quoteTextLayout.setOnTouchListener(onTouchListener);
        binding.quoteSignatureLayout.setOnTouchListener(onTouchListener);

        binding.quote.setOnTouchListener((v, event) -> {
            v.setFocusableInTouchMode(true);
            return false;
        });
        binding.quoteSignature.setOnTouchListener((v, event) -> {
            v.setFocusableInTouchMode(true);
            return false;
        });
    }

    private void beforeCapture() {
        binding.quoteDesign.setFocusable(false);
        binding.quote.clearComposingText();
        binding.quote.setFocusable(false);
        binding.quoteSignature.clearComposingText();
        binding.quoteSignature.setFocusable(false);
    }

    private void saveQuote() {
        if (creatorUiState.moveItems.get()) {
            creatorUiState.moveItems.set(false);
            return;
        }
        beforeCapture();
        if (((QuoteActivity) getActivity()).checkStoragePermission()) {
            if (capAndShare.save())
                App.toast(getString(R.string.quote_saved_in_storage));
        }
    }

    private void shareQuote() {
        if (creatorUiState.moveItems.get()) {
            creatorUiState.moveItems.set(false);
            return;
        }
        beforeCapture();
        if (((QuoteActivity) getActivity()).checkStoragePermission()) {
            capAndShare.share(getString(R.string.share_subject), getString(R.string.share_text));
        }
    }

    private void previewQuote() {
        if (creatorUiState.moveItems.get()) {
            creatorUiState.moveItems.set(false);
            return;
        }
        beforeCapture();
        try {
            View screenshot = binding.quoteDesign;
            screenshot.setDrawingCacheEnabled(true);
            screenshot.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            Bitmap bitmap = screenshot.getDrawingCache();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
            screenshot.setDrawingCacheEnabled(false);
            byte[] byteArray = stream.toByteArray();
            Intent intent = new Intent(getContext(), PreviewActivity.class);
            intent.putExtra("image", byteArray);
            getActivity().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeFont(int index) {
        if (index < 0) font_index = BaseActivity.fontNames.length - 1;
        else if (index >= BaseActivity.fontNames.length) font_index = 0;
        selectFont();
    }

    private void selectFont() {
        Typeface font = ResourcesCompat.getFont(getContext(), CONSTANTS.fonts[font_index]);
        String fontName = BaseActivity.fontNames[font_index];
        binding.quote.setTypeface(font);
        binding.quoteSignature.setTypeface(font);
        creatorUiState.quoteFontName.set(fontName);
    }

    private void changeDesign(int index) {
        if (index < 0) design_index = CONSTANTS.quoteDesigns.length - 1;
        else if (index >= CONSTANTS.quoteDesigns.length) design_index = 0;
        binding.quoteBackground.setImageResource(CONSTANTS.quoteDesigns[design_index]);
    }

    public void textColor(final ImageView icon) {
        ColorPickerDialogBuilder
                .with(getActivity())
                .setTitle("Choose color")
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(12)
                .setOnColorSelectedListener(selectedColor -> {
                })
                .setPositiveButton("ok", (dialog, selectedColor, allColors) -> {
                    creatorUiState.quoteTextColor.set(selectedColor);
                    binding.quote.setHintTextColor(selectedColor);
                    icon.setColorFilter(selectedColor);
                })
                .setNegativeButton("cancel", (dialog, which) -> {
                })
                .build()
                .show();
    }

    public void signatureColor(final ImageView icon) {
        ColorPickerDialogBuilder
                .with(getActivity())
                .setTitle("Choose color")
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(12)
                .setOnColorSelectedListener(selectedColor -> {
                })
                .setPositiveButton("ok", (dialog, selectedColor, allColors) -> {
                    creatorUiState.quoteSignatureColor.set(selectedColor);
                    binding.quoteSignature.setHintTextColor(selectedColor);
                    icon.setColorFilter(selectedColor);
                })
                .setNegativeButton("cancel", (dialog, which) -> {
                })
                .build()
                .show();
    }

    private void storeSettings() {
        pref.put(getClass().getSimpleName() + "design_index", design_index);
        pref.put(getClass().getSimpleName() + "font_index", font_index);
        pref.put(getClass().getSimpleName() + "text_align", creatorUiState.quoteAlignment.get());
        pref.put(getClass().getSimpleName() + "text_color", creatorUiState.quoteTextColor.get());
        pref.put(getClass().getSimpleName() + "signature_color", creatorUiState.quoteSignatureColor.get());
        pref.put(getClass().getSimpleName() + "text_size", creatorUiState.getQuoteTextSize());
        pref.put(getClass().getSimpleName() + "signature_size", creatorUiState.getQuoteSignatureSize());
        pref.put(getClass().getSimpleName() + "text_width", creatorUiState.getQuoteTextWidth());
        pref.put(getClass().getSimpleName() + "gravity", creatorUiState.getQuoteGravity());
    }

    private void restoreSettings() {
        design_index = pref.get(getClass().getSimpleName() + "design_index", 1);
        binding.quoteBackground.setImageResource(CONSTANTS.quoteDesigns[design_index]);
        font_index = pref.get(getClass().getSimpleName() + "font_index", 5);
        selectFont();
        creatorUiState.quoteAlignment.set(pref.get(getClass().getSimpleName() + "text_align", Gravity.CENTER));
        creatorUiState.quoteTextColor.set(pref.get(getClass().getSimpleName() + "text_color", Color.WHITE));
        creatorUiState.quoteSignatureColor.set(pref.get(getClass().getSimpleName() + "signature_color", Color.WHITE));
        binding.quote.setHintTextColor(pref.get(getClass().getSimpleName() + "text_color", Color.WHITE));
        binding.quoteSignature.setHintTextColor(pref.get(getClass().getSimpleName() + "signature_color", Color.WHITE));

        binding.quoteTextColor.setColorFilter(creatorUiState.quoteTextColor.get(), PorterDuff.Mode.SRC_ATOP);
        binding.quoteSignatureColor.setColorFilter(creatorUiState.quoteSignatureColor.get(), PorterDuff.Mode.SRC_ATOP);
        creatorUiState.setQuoteTextSize(pref.get(getClass().getSimpleName() + "text_size", 3));
        creatorUiState.setQuoteSignatureSize(pref.get(getClass().getSimpleName() + "signature_size", 1));
        creatorUiState.setQuoteTextWidth(pref.get(getClass().getSimpleName() + "text_width", 3));
        creatorUiState.setQuoteGravity(pref.get(getClass().getSimpleName() + "gravity", 1));

    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        float dX = 0, dY = 0;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    float x = v.getX();
                    float y = v.getY();

                    dX = x - event.getRawX();
                    dY = y - event.getRawY();
                    break;

                case MotionEvent.ACTION_MOVE:
                    float widgetX = event.getRawX() + dX;
                    float widgetY = event.getRawY() + dY;
                    v.setX(widgetX);
                    v.setY(widgetY);
                    break;
                default:
                    return false;
            }
            return true;
        }
    };

    @Override
    public void onPause() {
        storeSettings();
        super.onPause();
    }

    @Override
    public void onResume() {
        uiState.showSearch.set(false);
        try {
            ((QuoteActivity) getActivity()).binding.headerLayout.title.setText(getText(R.string.quote_create));
        } catch (Exception e) {
            ((MainActivity) getActivity()).binding.headerLayout.title.setText(getText(R.string.quote_create));
        }
        super.onResume();
    }

}