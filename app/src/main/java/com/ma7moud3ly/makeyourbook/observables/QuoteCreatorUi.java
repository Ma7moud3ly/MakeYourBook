package com.ma7moud3ly.makeyourbook.observables;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ma7moud3ly.makeyourbook.BR;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;

public class QuoteCreatorUi extends BaseObservable {
    public ObservableField<String> quoteFontName = new ObservableField<>();
    public ObservableField<Integer> quoteAlignment = new ObservableField<>();
    public ObservableField<Integer> quoteTextColor = new ObservableField<>();
    public ObservableField<Integer> quoteSignatureColor = new ObservableField<>();
    private ObservableField<Integer> quoteTextSize = new ObservableField<>();
    private ObservableField<Integer> quoteSignatureSize = new ObservableField<>();
    private ObservableField<Integer> quoteTextWidth = new ObservableField<>();
    private ObservableField<Integer> quoteGravity = new ObservableField<>();
    public ObservableField<Boolean> moveItems = new ObservableField<>();
    public ObservableField<Boolean> quoteShowSignature = new ObservableField<>();
    private static int maxWidth;

    public QuoteCreatorUi(int maxWidth) {
        this.maxWidth = maxWidth;
        moveItems.set(true);
        quoteShowSignature.set(true);
    }

    public void toggleSignature(View v) {
        quoteShowSignature.set(((CheckBox) v).isChecked());
    }

    public void align(View v, int direction) {
        quoteAlignment.set(direction);
    }

    @Bindable
    public int getQuoteTextSize() {
        return quoteTextSize.get();
    }

    public void setQuoteTextSize(int value) {
        if (quoteTextSize.get() == null || quoteTextSize.get() != value) {
            quoteTextSize.set(value);
            notifyPropertyChanged(BR.quoteTextSize);
        }
    }

    @Bindable
    public int getQuoteSignatureSize() {
        return quoteSignatureSize.get();
    }

    public void setQuoteSignatureSize(int value) {
        if (quoteSignatureSize.get() == null || quoteSignatureSize.get() != value) {
            quoteSignatureSize.set(value);
            notifyPropertyChanged(BR.quoteSignatureSize);
        }
    }

    @Bindable
    public int getQuoteTextWidth() {
        return quoteTextWidth.get();
    }

    public void setQuoteTextWidth(int value) {
        if (quoteTextWidth.get() == null || quoteTextWidth.get() != value) {
            quoteTextWidth.set(value);
            notifyPropertyChanged(BR.quoteTextWidth);
        }
    }


    private static double[] width_factor = {0.2, 0.4, 0.6, 0.8, 1.0};

    @BindingAdapter("layout_width")
    public static void layoutWidth(TextView textView, float value) {
        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        if (value < width_factor.length)
            layoutParams.width = (int) (width_factor[(int) value] * maxWidth);
        textView.setLayoutParams(layoutParams);
    }

    @Bindable
    public int getQuoteGravity() {
        return quoteGravity.get();
    }

    public void setQuoteGravity(int value) {
        if (quoteGravity.get() == null || quoteGravity.get() != value) {
            quoteGravity.set(value);
            notifyPropertyChanged(BR.quoteGravity);
        }
    }

    public void moveItemsToggle(View v) {
        moveItems.set(!moveItems.get());
    }

}
