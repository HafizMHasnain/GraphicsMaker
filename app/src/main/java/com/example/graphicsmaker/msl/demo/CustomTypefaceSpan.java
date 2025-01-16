package com.example.graphicsmaker.msl.demo;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class CustomTypefaceSpan extends MetricAffectingSpan {
    private final Typeface typeface;

    public CustomTypefaceSpan(Typeface typeface) {
        this.typeface = typeface;
    }

    public void updateDrawState(TextPaint drawState) {
        apply(drawState);
    }

    public void updateMeasureState(TextPaint paint) {
        apply(paint);
    }

    private void apply(Paint paint) {
        paint.setFakeBoldText(true);
        paint.setTypeface(this.typeface);
    }
}
