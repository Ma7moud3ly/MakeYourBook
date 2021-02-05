package com.ma7moud3ly.makeyourbook.observables;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */
import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.ma7moud3ly.makeyourbook.BR;
import com.ma7moud3ly.makeyourbook.R;
import com.ma7moud3ly.makeyourbook.activities.BaseActivity;
import com.ma7moud3ly.makeyourbook.activities.MainActivity;
import com.ma7moud3ly.makeyourbook.fragments.Reader.TextSearchFragment;

import javax.inject.Inject;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;

public class ReaderUi extends BaseObservable {
    public ObservableField<Integer> textAlignment = new ObservableField<>();
    public ObservableField<Integer> textColor = new ObservableField<>();
    private ObservableField<Integer> textSize = new ObservableField<>();
    public ObservableField<Typeface> typeface = new ObservableField<>();
    public ObservableField<Boolean> isItalic = new ObservableField<>();
    public ObservableField<Boolean> isBold = new ObservableField<>();
    public ObservableField<Boolean> showTools = new ObservableField<>();
    public ObservableField<Boolean> showHeader = new ObservableField<>();
    public ObservableField<Boolean> lineSpacing = new ObservableField<>();
    public ObservableField<Boolean> isLoading = new ObservableField<>();
    public int selectedFont = 0;

    @Inject
    public ReaderUi(){}

    public void align(View v, int direction) {
        textAlignment.set(direction);
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
        }
    }

    public static final int[] FONTS_SIZE = {14, 16, 18, 20, 22, 25, 30, 35};

    @BindingAdapter("android:textSize")
    public static void textSize(TextView textView, int size) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, ReaderUi.FONTS_SIZE[size]);
    }

    public void toggleBold(View v) {
        isBold.set(!isBold.get());
        buildTypeFace(v.getContext());
    }

    public void toggleItalic(View v) {
        isItalic.set(!isItalic.get());
        buildTypeFace(v.getContext());
    }

    public void buildTypeFace(Context c) {
        Typeface tf = ResourcesCompat.getFont(c, selectedFont);
        TextView tv = new TextView(c);
        tv.setTypeface(tf, isBold.get() ? Typeface.BOLD : Typeface.NORMAL);
        if (isItalic.get())
            tv.setTypeface(tv.getTypeface(), isBold.get() ? Typeface.BOLD_ITALIC : Typeface.ITALIC);
        typeface.set(tv.getTypeface());
    }

    @BindingAdapter("typeFace")
    public static void setBold(TextView textView, Typeface typeface) {
        textView.setTypeface(typeface);
    }

    @BindingAdapter("tint")
    public static void tint(ImageView view, boolean selected) {
        if (selected)
            view.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
        else
            view.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.semiBlack), android.graphics.PorterDuff.Mode.MULTIPLY);

    }

    public void toggleToolBox(View v) {
        showTools.set(!showTools.get());
    }

    public void toggleLineSpacing(View v) {
        lineSpacing.set(!lineSpacing.get());
    }

}
