package com.example.graphicsmaker.utility;

import androidx.viewpager.widget.ViewPager.PageTransformer;
import android.view.View;

import org.jetbrains.annotations.NotNull;

public abstract class BaseTransformer implements PageTransformer {
    protected abstract void onTransform(View view, float f);

    public void transformPage(@NotNull View view, float position) {
        onPreTransform(view, position);
        onTransform(view, position);
        onPostTransform(view, position);
    }

    protected boolean hideOffscreenPages() {
        return true;
    }

    protected boolean isPagingEnabled() {
        return false;
    }

    protected void onPreTransform(View view, float position) {
        float f = 0.0f;
        float width = (float) view.getWidth();
        view.setRotationX(0.0f);
        view.setRotationY(0.0f);
        view.setRotation(0.0f);
        view.setScaleX(1.0f);
        view.setScaleY(1.0f);
        view.setPivotX(0.0f);
        view.setPivotY(0.0f);
        view.setTranslationY(0.0f);
        view.setTranslationX(isPagingEnabled() ? 0.0f : (-width) * position);
        if (hideOffscreenPages()) {
            if (position > -1.0f && position < 1.0f) {
                f = 1.0f;
            }
            view.setAlpha(f);
            return;
        }
        view.setAlpha(1.0f);
    }

    protected void onPostTransform(View view, float position) {
    }
}
