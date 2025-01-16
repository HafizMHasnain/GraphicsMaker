package com.example.graphicsmaker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.graphicsmaker.R;

import org.jetbrains.annotations.NotNull;


public class RecyclerTextFontsAdapter extends Adapter<RecyclerTextFontsAdapter.ViewHolder> {
    Context context;
    private final String[] fontNameArr;
    int selected_position = -1;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layItem;
        TextView txtView;

        public ViewHolder(View view) {
            super(view);
            this.layItem = (RelativeLayout) view.findViewById(R.id.layItem);
            this.txtView = (TextView) view.findViewById(R.id.grid_text);
        }
    }

    public RecyclerTextFontsAdapter(Context context, String[] fontNameArr1) {
        this.context = context;
        this.fontNameArr = fontNameArr1;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public int getItemCount() {
        return this.fontNameArr.length;
    }

    public void onBindViewHolder(@NotNull ViewHolder viewHolder, final int position) {
        if (position == 0) {
            viewHolder.txtView.setTypeface(Typeface.DEFAULT);
        } else {
            viewHolder.txtView.setTypeface(Typeface.createFromAsset(this.context.getAssets(), this.fontNameArr[position]));
        }
        if (this.selected_position == position) {
            viewHolder.layItem.setBackgroundColor(Color.parseColor("#525151"));
        } else {
            viewHolder.layItem.setBackgroundColor(0);
        }
        viewHolder.layItem.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RecyclerTextFontsAdapter.this.notifyItemChanged(RecyclerTextFontsAdapter.this.selected_position);
                RecyclerTextFontsAdapter.this.selected_position = position;
                RecyclerTextFontsAdapter.this.notifyItemChanged(RecyclerTextFontsAdapter.this.selected_position);
            }
        });
    }

    public void setSelected(int position) {
        this.selected_position = position;
        notifyDataSetChanged();
    }

    @NotNull
    public ViewHolder onCreateViewHolder(ViewGroup arg0, int position) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(arg0.getContext()).inflate(R.layout.libtext_grid_assets_main, arg0, false));
        arg0.setId(position);
        arg0.setFocusable(false);
        arg0.setFocusableInTouchMode(false);
        return viewHolder;
    }

    public int getItemViewType(int position) {
        return position;
    }
}
