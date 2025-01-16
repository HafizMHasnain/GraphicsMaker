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


public class RecyclerTextFontAdapter extends Adapter<RecyclerTextFontAdapter.ViewHolder> {
    Context context;
    int seled_position = -1;
    String[] txtfontArr;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_txt;
        RelativeLayout layout;

        public ViewHolder(View view) {
            super(view);
            this.item_txt = (TextView) view.findViewById(R.id.item_txt);
            this.layout = (RelativeLayout) view.findViewById(R.id.lay);
        }
    }

    public RecyclerTextFontAdapter(Context context, String[] txtfontArr1) {
        this.context = context;
        this.txtfontArr = txtfontArr1;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public int getItemCount() {
        return this.txtfontArr.length;
    }

    public void onBindViewHolder(@NotNull ViewHolder viewHolder, final int position) {
        if (this.seled_position == position) {
            viewHolder.item_txt.setBackgroundColor(Color.parseColor("#525151"));
        } else {
            viewHolder.item_txt.setBackgroundColor(0);
        }
        viewHolder.item_txt.setTypeface(Typeface.createFromAsset(this.context.getAssets(), this.txtfontArr[position]));
        viewHolder.layout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RecyclerTextFontAdapter.this.notifyItemChanged(RecyclerTextFontAdapter.this.seled_position);
                RecyclerTextFontAdapter.this.seled_position = position;
                RecyclerTextFontAdapter.this.notifyItemChanged(RecyclerTextFontAdapter.this.seled_position);
            }
        });
    }

    public void setSelected(int position) {
        this.seled_position = position;
        notifyDataSetChanged();
    }

    @NotNull
    public ViewHolder onCreateViewHolder(ViewGroup arg0, int position) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(arg0.getContext()).inflate(R.layout.recycler_adapter_txt, arg0, false));
        arg0.setId(position);
        arg0.setFocusable(false);
        arg0.setFocusableInTouchMode(false);
        return viewHolder;
    }

    public int getItemViewType(int position) {
        return position;
    }
}
