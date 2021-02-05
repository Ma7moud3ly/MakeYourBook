package com.ma7moud3ly.makeyourbook.adapters;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;

import com.ma7moud3ly.makeyourbook.activities.BaseActivity;
import com.ma7moud3ly.makeyourbook.observables.UiState;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BaseViewHolder extends RecyclerView.ViewHolder {
    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        Context context = itemView.getContext();
        this.uiState = ((BaseActivity) context).uiState;
    }

    public UiState uiState;

    public void setHeight(View view, double divider) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) view.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int deviceHeight = (int) (displaymetrics.heightPixels / divider);
        view.getLayoutParams().height = deviceHeight;
    }

    public void setWidth(View view, double divider) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) view.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int deviceWidth = (int) (displaymetrics.widthPixels / divider);
        view.getLayoutParams().width = deviceWidth;
    }

    public void setWH(View view, int divider) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) view.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels / divider - 30;
        view.getLayoutParams().height = width - 10;
        view.getLayoutParams().width = width;

    }
}
