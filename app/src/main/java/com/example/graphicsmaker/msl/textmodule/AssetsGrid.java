package com.example.graphicsmaker.msl.textmodule;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.graphicsmaker.R;


public class AssetsGrid extends BaseAdapter {
    private final String[] Imageid;
    private Context mContext;
    int selected_position = -1;

    public class ViewHolder {
        RelativeLayout lay_text;
        TextView txtView;
    }

    public AssetsGrid(Context c, String[] imageid) {
        this.mContext = c;
        this.Imageid = imageid;
    }

    public int getCount() {
        return this.Imageid.length;
    }

    public Object getItem(int position) {
        return this.Imageid[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = ((LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.libtext_grid_assets, null);
            holder = new ViewHolder();
            holder.txtView = (TextView) convertView.findViewById(R.id.grid_text);
            holder.lay_text = (RelativeLayout) convertView.findViewById(R.id.lay_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtView.setTypeface(Typeface.createFromAsset(this.mContext.getAssets(), this.Imageid[position]));
        if (this.selected_position == position) {
            holder.lay_text.setBackgroundColor(-7829368);
        } else {
            holder.lay_text.setBackgroundColor(0);
        }
        return convertView;
    }

    public void setSelected(int position) {
        this.selected_position = position;
        notifyDataSetChanged();
    }
}
