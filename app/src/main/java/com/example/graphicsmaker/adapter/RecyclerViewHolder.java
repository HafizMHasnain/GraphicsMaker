package com.example.graphicsmaker.adapter;

import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;

import com.example.graphicsmaker.R;


public class RecyclerViewHolder extends ViewHolder {
    ImageView imageView;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        this.imageView = (ImageView) itemView.findViewById(R.id.image);
    }
}
