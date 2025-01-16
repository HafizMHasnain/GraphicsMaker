package com.example.graphicsmaker.sticker_fragment;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

import org.jetbrains.annotations.NotNull;

public class RecyclerTouchListener implements OnItemTouchListener {
    private ClickListener clicklistener;
    private GestureDetector gestureDetector;

    public interface ClickListener {
        void onClick(View view, int i);

        void onLongClick(View view, int i);
    }

    public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener) {
        this.clicklistener = clicklistener;
        this.gestureDetector = new GestureDetector(context, new SimpleOnGestureListener() {
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            public void onLongPress(MotionEvent e) {
                View child = recycleView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && clicklistener != null) {
                    clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child));
                }
            }
        });
    }

    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (!(child == null || this.clicklistener == null || !this.gestureDetector.onTouchEvent(e))) {
            this.clicklistener.onClick(child, rv.getChildAdapterPosition(child));
        }
        return false;
    }

    public void onTouchEvent(@NotNull RecyclerView rv, @NotNull MotionEvent e) {
    }

    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }
}
