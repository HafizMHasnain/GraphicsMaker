package com.example.graphicsmaker.utility

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NonScrollableViewPager @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ViewPager(context, attrs) {

    private var isScrollEnabled: Boolean = false

    fun setScrollEnabled(enabled: Boolean) {
        isScrollEnabled = enabled
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        // Disable touch event handling if scrolling is disabled
        return isScrollEnabled && super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        // Prevent intercepting touch events if scrolling is disabled
        return isScrollEnabled && super.onInterceptTouchEvent(ev)
    }
}
