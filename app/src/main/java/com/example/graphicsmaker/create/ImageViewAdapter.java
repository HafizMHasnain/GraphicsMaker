package com.example.graphicsmaker.create;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.graphicsmaker.R;


public class ImageViewAdapter extends BaseAdapter {
    private final int[] Imageid;
    private Context mContext;

    public class ViewHolder {
        ImageView imageView;
    }

    public ImageViewAdapter(Context c, int[] Imageid) {
        this.mContext = c;
        this.Imageid = Imageid;
    }

    public int getCount() {
        return this.Imageid.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = ((LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.btxt_lst_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.grid_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imageView.setImageResource(this.Imageid[position]);
        return convertView;
    }
}
