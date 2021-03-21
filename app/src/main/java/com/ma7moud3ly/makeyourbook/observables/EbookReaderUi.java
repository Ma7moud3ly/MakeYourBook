package com.ma7moud3ly.makeyourbook.observables;
/**
 * اصنع كتابك Make your Book
 *
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */

import android.app.Activity;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.ma7moud3ly.makeyourbook.BR;

import javax.inject.Inject;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

public class EbookReaderUi extends BaseObservable {

    public ObservableField<String> fontFamily = new ObservableField<>();
    public ObservableField<Integer> textColor = new ObservableField<>();
    private ObservableField<Integer> textSize = new ObservableField<>();
    public ObservableField<String> textAlignment = new ObservableField<>();
    public ObservableField<Boolean> isJustify = new ObservableField<>();
    public ObservableField<String> fontWeight = new ObservableField<>();
    public ObservableField<String> lineHeight = new ObservableField<>();
    public ObservableField<String> fontStyle = new ObservableField<>();
    public ObservableField<Boolean> showTools = new ObservableField<>();
    public ObservableField<Boolean> fullScreen = new ObservableField<>();
    public String content = "";
    public WebView editor;

    @Inject
    public EbookReaderUi() {
    }

    public void updateContent() {
        if (content == null || content == "" || editor == null) return;

        String hexColor = String.format("#%06X", (0xFFFFFF & textColor.get()));
        String alignment;
        if (isJustify.get())
            alignment = "text-align: justify;text-align-last: " + textAlignment.get();
        else
            alignment = "text-align:" + textAlignment.get();
        String size = (textSize.get() + 1) * 5 + "px";
        String style = String.format("color:%s;font-family:%s;font-size:%s;%s;" +
                        "font-weight:%s;line-height:%s;font-style:%s", hexColor, fontFamily.get(),
                size, alignment, fontWeight.get(), lineHeight.get(),
                fontStyle.get());

        String src = "<html>\n" +
                "<head>\n" +
                "    <style>\n" +
                "        @font-face {\n" +
                "            font-family: 'cairo';\n" +
                "            src: url('font/cairo_regular.ttf');\n" +
                "        }\n" +
                "        #page_format {\n" +
                style +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div dir='auto' id='page_format'>" + content + "</div>" +
                "</body>";

        String encodedHtml = Base64.encodeToString(src.getBytes(), Base64.NO_PADDING);
        editor.clearCache(true);
        editor.loadData(encodedHtml, "text/html; charset=utf-8", "base64");

    }

    public void align(View v, String direction) {
        textAlignment.set(direction);
        updateContent();
    }

    public void justifyToggle(View v) {
        isJustify.set(!isJustify.get());
        updateContent();
    }

    public void textColor(View v) {
        ColorPickerDialogBuilder
                .with(v.getContext())
                .setTitle("Choose color")
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(selectedColor -> {
                })
                .setPositiveButton("ok", (dialog, selectedColor, allColors) -> {
                    textColor.set(selectedColor);
                    ((ImageView) v).setColorFilter(selectedColor);
                    updateContent();
                })
                .setNegativeButton("cancel", (dialog, which) -> {
                })
                .build()
                .show();

    }

    @Bindable
    public int getTextSize() {
        return textSize.get();
    }

    public void setTextSize(int value) {
        if (textSize.get() == null || textSize.get() != value) {
            textSize.set(value);
            notifyPropertyChanged(BR.textSize);
            updateContent();
        }
    }

    public void toggleBold(View v) {
        if (fontWeight.get().equals("bold")) fontWeight.set("normal");
        else if (fontWeight.get().equals("normal")) fontWeight.set("bold");
        updateContent();
    }

    public void toggleItalic(View v) {
        if (fontStyle.get().equals("italic")) fontStyle.set("normal");
        else if (fontStyle.get().equals("normal")) fontStyle.set("italic");
        updateContent();
    }

    public void toggleLineHeight(View v) {
        if (lineHeight.get().equals("normal")) lineHeight.set("4");
        else if (lineHeight.get().equals("4")) lineHeight.set("normal");
        updateContent();
    }

    public void toggleTools(View v) {
        showTools.set(!showTools.get());
    }

    public void toggleFullscreen(View v) {
        showTools.set(false);
        boolean full = !fullScreen.get();
        fullScreen.set(full);
        Activity activity = (Activity) v.getContext();
        setFullScreen(activity, full);
    }

    public void setFullScreen(Activity activity, boolean full) {
        fullScreen.set(full);
        if (full) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE
            );

            //View mDecorView = activity.getWindow().getDecorView();
            //mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.GONE);

        } else {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

}
