package com.example.graphicsmaker.sticker_fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.graphicsmaker.msl.demo.view.ResizableStickerView;
import com.example.graphicsmaker.msl.textmodule.AutofitTextRel;
import com.example.graphicsmaker.R;
import com.example.graphicsmaker.adapter.ItemAdapter;
import com.example.graphicsmaker.main.Constants;
import com.woxthebox.draglistview.DragItem;
import com.woxthebox.draglistview.DragListView;
import com.woxthebox.draglistview.DragListView.DragListListenerAdapter;

import java.util.ArrayList;

public class ListFragment extends Fragment {
    private boolean[] arrBoolean;
    ImageButton btn_layControls;
    String checkAll = "Lock_Mixed";
    ImageView img_All;
    RelativeLayout lay_Notext;
    FrameLayout lay_container;
    RelativeLayout lay_selectAll;
    private DragListView mDragListView;
    private ArrayList<Pair<Long, View>> mItemArray = new ArrayList();
    RelativeLayout txt_stkr_rel;

    private static class MyDragItem extends DragItem {
        MyDragItem(Context context, int layoutId) {
            super(context, layoutId);
        }

        public void onBindDragView(View clickedView, View dragView) {
            Bitmap thumbBit1 = Bitmap.createBitmap(clickedView.getWidth(), clickedView.getHeight(), Config.ARGB_8888);
            clickedView.draw(new Canvas(thumbBit1));
            ((ImageView) dragView.findViewById(R.id.backimg)).setImageBitmap(thumbBit1);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_layout, container, false);
        LinearLayout mRefreshLayout = (LinearLayout) view.findViewById(R.id.swipe_refresh_layout);
        this.mDragListView = (DragListView) view.findViewById(R.id.drag_list_view);
        this.mDragListView.getRecyclerView().setVerticalScrollBarEnabled(true);
        this.mDragListView.setDragListListener(new DragListListenerAdapter() {
            public void onItemDragStarted(int position) {
            }

            public void onItemDragEnded(int fromPosition, int toPosition) {
                if (fromPosition != toPosition) {
                    for (int i = ListFragment.this.mItemArray.size() - 1; i >= 0; i--) {
                        ((View) ((Pair) ListFragment.this.mItemArray.get(i)).second).bringToFront();
                    }
                    ListFragment.this.txt_stkr_rel.requestLayout();
                    ListFragment.this.txt_stkr_rel.postInvalidate();
                }
            }
        });
        ((TextView) view.findViewById(R.id.txt_Nolayers)).setTypeface(Constants.getTextTypeface(getActivity()));
        this.lay_Notext = (RelativeLayout) view.findViewById(R.id.lay_text);
        this.lay_selectAll = (RelativeLayout) view.findViewById(R.id.lay_selectAll);
        this.img_All = (ImageView) view.findViewById(R.id.img_selectAll);
        ((RelativeLayout) view.findViewById(R.id.lay_frame)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ListFragment.this.lay_container.getVisibility() == View.VISIBLE) {
                    ListFragment.this.lay_container.animate().translationX((float) (-ListFragment.this.lay_container.getRight())).setDuration(200).setInterpolator(new AccelerateInterpolator()).start();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            ListFragment.this.lay_container.setVisibility(View.GONE);
                            ListFragment.this.btn_layControls.setVisibility(View.VISIBLE);
                        }
                    }, 200);
                }
            }
        });
        this.lay_selectAll.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            }
        });
        this.img_All.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ListFragment.this.txt_stkr_rel.getChildCount() != 0) {
                    if (ListFragment.this.checkAll.equals("Lock_All")) {
                        ListFragment.this.checkAll = "UnLock_All";
                        ListFragment.this.img_All.setImageResource(R.drawable.on_fp);
                    } else {
                        ListFragment.this.checkAll = "Lock_All";
                        ListFragment.this.img_All.setImageResource(R.drawable.off_fp);
                    }
                    ListFragment.this.lock_unLockAll(ListFragment.this.checkAll);
                }
            }
        });
        return view;
    }

    private void lock_unLockAll(String checkAll) {
        for (int i = this.txt_stkr_rel.getChildCount() - 1; i >= 0; i--) {
            View view = this.txt_stkr_rel.getChildAt(i);
            if (checkAll.equals("Lock_All")) {
                if (view instanceof ResizableStickerView) {
                    ((ResizableStickerView) view).isMultiTouchEnabled = ((ResizableStickerView) view).setDefaultTouchListener(false);
                }
                if (view instanceof AutofitTextRel) {
                    ((AutofitTextRel) view).isMultiTouchEnabled = ((AutofitTextRel) view).setDefaultTouchListener(false);
                }
            } else {
                if (view instanceof ResizableStickerView) {
                    ((ResizableStickerView) view).isMultiTouchEnabled = ((ResizableStickerView) view).setDefaultTouchListener(true);
                }
                if (view instanceof AutofitTextRel) {
                    ((AutofitTextRel) view).isMultiTouchEnabled = ((AutofitTextRel) view).setDefaultTouchListener(true);
                }
            }
        }
        setupListRecyclerView();
    }

    public void getLayoutChild(boolean addBoolean) {
        if (addBoolean) {
            this.mItemArray.clear();
        }
        if (this.txt_stkr_rel.getChildCount() != 0) {
            if (addBoolean) {
                this.lay_Notext.setVisibility(View.GONE);
                this.lay_selectAll.setVisibility(View.VISIBLE);
            }
            this.arrBoolean = new boolean[this.txt_stkr_rel.getChildCount()];
            int totalnum = this.txt_stkr_rel.getChildCount();
            int lockedNum = 0;
            int unlockednum = 0;
            for (int i = this.txt_stkr_rel.getChildCount() - 1; i >= 0; i--) {
                if (addBoolean) {
                    this.mItemArray.add(new Pair(Long.valueOf((long) i), this.txt_stkr_rel.getChildAt(i)));
                }
                View view = this.txt_stkr_rel.getChildAt(i);
                if (view instanceof AutofitTextRel) {
                    this.arrBoolean[i] = ((AutofitTextRel) view).isMultiTouchEnabled;
                }
                if (view instanceof ResizableStickerView) {
                    this.arrBoolean[i] = ((ResizableStickerView) view).isMultiTouchEnabled;
                }
                if (this.arrBoolean[i]) {
                    unlockednum++;
                } else {
                    lockedNum++;
                }
            }
            if (totalnum == lockedNum) {
                this.checkAll = "Lock_All";
                this.img_All.setImageResource(R.drawable.off_fp);
            } else if (totalnum == unlockednum) {
                this.checkAll = "UnLock_All";
                this.img_All.setImageResource(R.drawable.on_fp);
            } else {
                this.checkAll = "Lock_Mixed";
                this.img_All.setImageResource(R.drawable.on_fp);
            }
        } else {
            this.lay_Notext.setVisibility(View.VISIBLE);
            this.lay_selectAll.setVisibility(View.INVISIBLE);
        }
        if (addBoolean) {
            setupListRecyclerView();
        }
    }

    private void setupListRecyclerView() {
        this.mDragListView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mDragListView.setAdapter(new ItemAdapter(getActivity(), this.mItemArray, R.layout.list_item, R.id.touch_rel, false), true);
        this.mDragListView.setCanDragHorizontally(false);
        this.mDragListView.setCustomDragItem(new MyDragItem(getContext(), R.layout.list_item));
    }

    public void setRelativeLayout(RelativeLayout txt_stkr_rel, ImageButton btn_layControls, FrameLayout lay_container) {
        this.txt_stkr_rel = txt_stkr_rel;
        this.btn_layControls = btn_layControls;
        this.lay_container = lay_container;
    }
}
