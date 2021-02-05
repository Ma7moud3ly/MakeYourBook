package com.ma7moud3ly.makeyourbook.util;

import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;

public class KeyboardState {

    final EditText edit;

    public KeyboardState(EditText edit, KeyboardVisibility keyboard) {
        this.edit = edit;

        edit.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (keyboardShown(edit.getRootView())) keyboard.onKeyboardShown();
            else keyboard.onKeyboardhidden();
        });
    }

    private boolean keyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }

}

abstract class KeyboardVisibility {
    public void onKeyboardhidden() {

    }

    public void onKeyboardShown() {

    }
}