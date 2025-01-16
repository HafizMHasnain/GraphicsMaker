package com.example.graphicsmaker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.graphicsmaker.R;

import org.jetbrains.annotations.NotNull;

public class filter_recycler_adapter  extends RecyclerView.Adapter<RecyclerFilterImageAdapter.ViewHolder> {
    Context context;
    String[] filterImageArr;
    int selected_position = -1;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        LinearLayout layout;
        ImageView viewImage;

        public ViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.item_image);
            this.viewImage = (ImageView) view.findViewById(R.id.view_image);
            this.layout = (LinearLayout) view.findViewById(R.id.lay);
        }
    }

    public filter_recycler_adapter(Context context, String[] filterImageArr) {
        this.context = context;
        this.filterImageArr = filterImageArr;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public int getItemCount() {
        return this.filterImageArr.length;
    }

    public void onBindViewHolder(RecyclerFilterImageAdapter.ViewHolder viewHolder, final int position) {
        Glide.with(this.context).load(Integer.valueOf(this.context.getResources().getIdentifier(this.filterImageArr[position], "drawable", this.context.getPackageName()))).thumbnail(0.1f).dontAnimate().placeholder((int) R.drawable.loading2).error((int) R.drawable.error2).into(viewHolder.imageView);
        if (this.selected_position == position) {
            viewHolder.viewImage.setVisibility(View.VISIBLE);
        } else {
            viewHolder.viewImage.setVisibility(View.INVISIBLE);
        }
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                filter_recycler_adapter.this.notifyItemChanged(filter_recycler_adapter.this.selected_position);
                filter_recycler_adapter.this.selected_position = position;
                filter_recycler_adapter.this.notifyItemChanged(filter_recycler_adapter.this.selected_position);
            }
        });
    }

    public int setSelected(String nameIs) {
        int po = -1;
        for (int i = 0; i < this.filterImageArr.length; i++) {
            if (this.filterImageArr[i].equals(nameIs)) {
                po = i;
                break;
            }
        }
        this.selected_position = po;
        notifyDataSetChanged();
        return po;
    }

    @NotNull
    public RecyclerFilterImageAdapter.ViewHolder onCreateViewHolder(ViewGroup arg0, int position) {
        RecyclerFilterImageAdapter.ViewHolder viewHolder = new RecyclerFilterImageAdapter.ViewHolder(LayoutInflater.from(arg0.getContext()).inflate(R.layout.filter_adapter, arg0, false));
        arg0.setId(position);
        arg0.setFocusable(false);
        arg0.setFocusableInTouchMode(false);
        return viewHolder;
    }

    public int getItemViewType(int position) {
        return position;
    }
}
