package com.example.graphicsmaker.utility;

import android.content.Context;
import android.util.AttributeSet;

public class CustomSquareImageView extends androidx.appcompat.widget.AppCompatImageView {
    public CustomSquareImageView(Context context) {
        super(context);
    }

    public CustomSquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
    }
}
