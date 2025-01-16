package com.example.graphicsmaker.msl.demo.view;

import android.content.Context;
import android.util.AttributeSet;

public class SquareImageViewGrid extends androidx.appcompat.widget.AppCompatImageView {
    public SquareImageViewGrid(Context context) {
        super(context);
    }

    public SquareImageViewGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
    }
}
