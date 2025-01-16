package com.example.graphicsmaker.msl.demo.view;

import android.content.Context;
import android.util.AttributeSet;

public class SquareImageViewList extends androidx.appcompat.widget.AppCompatImageView {
    public SquareImageViewList(Context context) {
        super(context);
    }

    public SquareImageViewList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(heightMeasureSpec, heightMeasureSpec);
    }
}
