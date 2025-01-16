package com.example.graphicsmaker.utility;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class SlowScroller extends Scroller {
    private int mScrollDuration = 800;

    public SlowScroller(Context context) {
        super(context);
    }

    public SlowScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, this.mScrollDuration);
    }

    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, this.mScrollDuration);
    }
}
