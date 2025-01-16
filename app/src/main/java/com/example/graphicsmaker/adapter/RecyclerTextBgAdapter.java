package com.example.graphicsmaker.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.example.graphicsmaker.R;

import org.jetbrains.annotations.NotNull;


public class RecyclerTextBgAdapter extends Adapter<RecyclerTextBgAdapter.ViewHolder> {
    Context context;
    String[] makeUpEditImage;
    int selected_position = 500;

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

    public RecyclerTextBgAdapter(Context context, String[] makeUpEditImage) {
        this.context = context;
        this.makeUpEditImage = makeUpEditImage;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public int getItemCount() {
        return this.makeUpEditImage.length;
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Glide.with(this.context).load(Integer.valueOf(this.context.getResources().getIdentifier(this.makeUpEditImage[position], "drawable", this.context.getPackageName()))).thumbnail(0.1f).dontAnimate().centerCrop().placeholder((int) R.drawable.loading2).error((int) R.drawable.error2).into(viewHolder.imageView);
        if (this.selected_position == position) {
            viewHolder.viewImage.setVisibility(View.VISIBLE);
        } else {
            viewHolder.viewImage.setVisibility(View.INVISIBLE);
        }
        viewHolder.layout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RecyclerTextBgAdapter.this.notifyItemChanged(RecyclerTextBgAdapter.this.selected_position);
                RecyclerTextBgAdapter.this.selected_position = position;
                RecyclerTextBgAdapter.this.notifyItemChanged(RecyclerTextBgAdapter.this.selected_position);
            }
        });
    }

    public void setSelected(int position) {
        this.selected_position = position;
        notifyDataSetChanged();
    }

    @NotNull
    public ViewHolder onCreateViewHolder(ViewGroup arg0, int position) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(arg0.getContext()).inflate(R.layout.recycler_adapterbg, arg0, false));
        arg0.setId(position);
        arg0.setFocusable(false);
        arg0.setFocusableInTouchMode(false);
        return viewHolder;
    }

    public int getItemViewType(int position) {
        return position;
    }
}
