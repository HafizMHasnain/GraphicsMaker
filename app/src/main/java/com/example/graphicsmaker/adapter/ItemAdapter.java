package com.example.graphicsmaker.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import androidx.core.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.graphicsmaker.main.CreatePoster;
import com.example.graphicsmaker.msl.demo.view.ResizableStickerView;
import com.example.graphicsmaker.msl.textmodule.AutofitTextRel;
import com.example.graphicsmaker.R;
import com.example.graphicsmaker.utility.ImageUtils;
import com.woxthebox.draglistview.DragItemAdapter;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ItemAdapter extends DragItemAdapter<Pair<Long, View>, ItemAdapter.ViewHolder> {
    Activity activity;
    private boolean mDragOnLongPress;
    private int mGrabHandleId;
    private int mLayoutId;

    class ViewHolder extends DragItemAdapter.ViewHolder {
        ImageView img_lock;
        ImageView mImage;
        TextView mText;
        ImageView textView;

        ViewHolder(View itemView) {
            super(itemView, ItemAdapter.this.mGrabHandleId, ItemAdapter.this.mDragOnLongPress);
            this.mText = (TextView) itemView.findViewById(R.id.text);
            this.mImage = (ImageView) itemView.findViewById(R.id.image1);
            this.textView = (ImageView) itemView.findViewById(R.id.auto_fit_edit_text);
            this.img_lock = (ImageView) itemView.findViewById(R.id.img_lock);
        }

        public void onItemClicked(View view) {
        }

        public boolean onItemLongClicked(View view) {
            return true;
        }
    }

    public ItemAdapter(Activity activity1, ArrayList<Pair<Long, View>> list, int layoutId, int grabHandleId, boolean dragOnLongPress) {
        this.mLayoutId = layoutId;
        this.mGrabHandleId = grabHandleId;
        this.activity = activity1;
        this.mDragOnLongPress = dragOnLongPress;
        setItemList(list);
    }

    @NotNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(this.mLayoutId, parent, false));
    }

    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        super.onBindViewHolder((ViewHolder) holder, position);
        View v = (View) ((Pair) this.mItemList.get(position)).second;
        try {
            View cChild;
            Bitmap thumbBit;
            if (v instanceof ResizableStickerView) {
                cChild = ((ResizableStickerView) v).getChildAt(1);
                thumbBit = Bitmap.createBitmap(cChild.getWidth(), cChild.getHeight(), Config.ARGB_8888);
                cChild.draw(new Canvas(thumbBit));
                float[] f = new float[9];
                ((ImageView) cChild).getImageMatrix().getValues(f);
                float scaleX = f[0];
                float scaleY = f[4];
                Drawable d = ((ImageView) cChild).getDrawable();
                int origW = d.getIntrinsicWidth();
                int origH = d.getIntrinsicHeight();
                int width = Math.round(((float) origW) * scaleX);
                int height = Math.round(((float) origH) * scaleY);
                holder.mImage.setImageBitmap(Bitmap.createBitmap(thumbBit, (thumbBit.getWidth() - width) / 2, (thumbBit.getHeight() - height) / 2, width, height));
                holder.mImage.setRotationY(cChild.getRotationY());
                holder.mImage.setTag(this.mItemList.get(position));
                holder.mImage.setAlpha(1.0f);
                holder.textView.setImageBitmap(null);
            }
            if (v instanceof AutofitTextRel) {
                if (((AutofitTextRel) v).getTextInfo().getBG_COLOR() != 0) {
                    Bitmap bmp = Bitmap.createBitmap(150, 150, Config.ARGB_8888);
                    new Canvas(bmp).drawColor(((AutofitTextRel) v).getTextInfo().getBG_COLOR());
                    holder.mImage.setImageBitmap(bmp);
                    holder.mImage.setAlpha(((float) ((AutofitTextRel) v).getTextInfo().getBG_ALPHA()) / 255.0f);
                } else if (((AutofitTextRel) v).getTextInfo().getBG_DRAWABLE().equals("0")) {
                    holder.mImage.setAlpha(1.0f);
                    holder.mImage.setImageResource(R.drawable.trans);
                } else {
                    holder.mImage.setImageBitmap(ImageUtils.getTiledBitmap(this.activity, this.activity.getResources().getIdentifier(((AutofitTextRel) v).getTextInfo().getBG_DRAWABLE(), "drawable", this.activity.getPackageName()), 150, 150, false));
                    holder.mImage.setAlpha(((float) ((AutofitTextRel) v).getTextInfo().getBG_ALPHA()) / 255.0f);
                }
                cChild = ((AutofitTextRel) v).getChildAt(2);
                thumbBit = Bitmap.createBitmap(cChild.getWidth(), cChild.getHeight(), Config.ARGB_8888);
                cChild.draw(new Canvas(thumbBit));
                holder.textView.setImageBitmap(thumbBit);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (v instanceof ResizableStickerView) {
            if (((ResizableStickerView) v).isMultiTouchEnabled) {
                holder.img_lock.setImageResource(R.drawable.ic_unlock);
            } else {
                holder.img_lock.setImageResource(R.drawable.ic_lock);
            }
        }
        if (v instanceof AutofitTextRel) {
            if (((AutofitTextRel) v).isMultiTouchEnabled) {
                holder.img_lock.setImageResource(R.drawable.ic_unlock);
            } else {
                holder.img_lock.setImageResource(R.drawable.ic_lock);
            }
        }
        final View view = v;
        final ViewHolder viewHolder = holder;
        holder.img_lock.setOnClickListener(new OnClickListener() {
            public void onClick(View v1) {
                if (view instanceof ResizableStickerView) {
                    if (((ResizableStickerView) view).isMultiTouchEnabled) {
                        ((ResizableStickerView) view).isMultiTouchEnabled = ((ResizableStickerView) view).setDefaultTouchListener(false);
                        viewHolder.img_lock.setImageResource(R.drawable.ic_lock);
                    } else {
                        ((ResizableStickerView) view).isMultiTouchEnabled = ((ResizableStickerView) view).setDefaultTouchListener(true);
                        viewHolder.img_lock.setImageResource(R.drawable.ic_unlock);
                    }
                }
                if (view instanceof AutofitTextRel) {
                    if (((AutofitTextRel) view).isMultiTouchEnabled) {
                        ((AutofitTextRel) view).isMultiTouchEnabled = ((AutofitTextRel) view).setDefaultTouchListener(false);
                        viewHolder.img_lock.setImageResource(R.drawable.ic_lock);
                    } else {
                        ((AutofitTextRel) view).isMultiTouchEnabled = ((AutofitTextRel) view).setDefaultTouchListener(true);
                        viewHolder.img_lock.setImageResource(R.drawable.ic_unlock);
                    }
                }
                ((CreatePoster) ItemAdapter.this.activity).getListFragment().getLayoutChild(false);
            }
        });
    }

    public long getUniqueItemId(int position) {
        return ((Long) ((Pair) this.mItemList.get(position)).first).longValue();
    }
}
