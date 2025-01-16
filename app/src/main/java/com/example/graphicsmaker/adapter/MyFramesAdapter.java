package com.example.graphicsmaker.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.graphicsmaker.R;
import com.example.graphicsmaker.create.BitmapDataObject;
import com.example.graphicsmaker.create.TemplateInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class MyFramesAdapter extends ArrayAdapter<TemplateInfo> {
    Context context;
    float height;

    class ViewHolder {
        RelativeLayout lay_image;
        ImageView mThumbnail;
        Uri uri;

        public ViewHolder(View view) {
            this.mThumbnail = (ImageView) view.findViewById(R.id.image);
            this.lay_image = (RelativeLayout) view.findViewById(R.id.lay_image);
        }
    }

    public MyFramesAdapter(Context context, List<TemplateInfo> images, float height1) {
        super(context, 0, images);
        this.context = context;
        this.height = height1;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item22, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Uri mUri = Uri.parse(((TemplateInfo) getItem(position)).getTHUMB_URI());
        holder.lay_image.getLayoutParams().height = (int) this.height;
        holder.mThumbnail.getLayoutParams().height = (int) this.height;
        if (holder.uri == null || !holder.uri.equals(mUri)) {
            holder.uri = mUri;
            try {
                Glide.with(this.context).load(getBitmapDataObject(mUri.getPath()).imageByteArray).placeholder((int) R.drawable.loading2).error((int) R.drawable.error2).into(holder.mThumbnail);
            } catch (NullPointerException e) {
                holder.mThumbnail.setImageResource(R.drawable.no_image);
                e.printStackTrace();
            } catch (Exception e2) {
                holder.mThumbnail.setImageResource(R.drawable.no_image);
            }
        }
        return convertView;
    }

    private BitmapDataObject getBitmapDataObject(String path) {
        try {
            return (BitmapDataObject) new ObjectInputStream(new FileInputStream(new File(path))).readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (ClassNotFoundException e3) {
            e3.printStackTrace();
        }
        return null;
    }
}
