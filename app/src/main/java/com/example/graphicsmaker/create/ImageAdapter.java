package com.example.graphicsmaker.create;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.graphicsmaker.R;
import com.example.graphicsmaker.utility.ImageUtils;


public class ImageAdapter extends BaseAdapter {
    private final int[] Imageid;
    private Context mContext;

    public class ViewHolder {
        ImageView imageView;
    }

    public ImageAdapter(Context c, int[] Imageid) {
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
            convertView = ((LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.bg_lst_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.grid_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        fillImage(this.Imageid[position], holder.imageView);
        return convertView;
    }

    void fillImage(int id, ImageView img) {
        Options options = new Options();
        options.inSampleSize = 12;
        img.setImageBitmap(ImageUtils.getThumbnail(BitmapFactory.decodeResource(this.mContext.getResources(), id, options), 50, 50));
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = 3;
        options.inJustDecodeBounds = false;
        return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, resId, options), 100, 100, false);
    }
}
