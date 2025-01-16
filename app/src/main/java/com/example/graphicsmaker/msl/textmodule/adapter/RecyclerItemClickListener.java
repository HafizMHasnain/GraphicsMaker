package com.example.graphicsmaker.msl.textmodule.adapter;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener;

import org.jetbrains.annotations.NotNull;

public class RecyclerItemClickListener implements OnItemTouchListener {
    GestureDetector mGestureDetector;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int i);
    }

    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        this.mListener = listener;
        this.mGestureDetector = new GestureDetector(context, new SimpleOnGestureListener() {
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (!(childView == null || this.mListener == null || !this.mGestureDetector.onTouchEvent(e))) {
            this.mListener.onItemClick(childView, view.getChildPosition(childView));
        }
        return false;
    }

    public void onTouchEvent(@NotNull RecyclerView view, @NotNull MotionEvent motionEvent) {
    }

    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }
}
