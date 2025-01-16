package com.example.graphicsmaker.adapter;

import android.content.Context;
import android.graphics.Typeface;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.graphicsmaker.R;


public class AssetsGridMain extends BaseAdapter {
    private final String[] Imageid;
    private Context mContext;
    int selPos = 0;

    public class ViewHolder {
        RelativeLayout layItem;
        TextView txtView;
    }

    public AssetsGridMain(Context c, String[] imageid) {
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
        View view;
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            view = inflater.inflate(R.layout.item_txtfonts, null);
            holder = new ViewHolder();
            holder.layItem = (RelativeLayout) view.findViewById(R.id.layItem);
            holder.txtView = (TextView) view.findViewById(R.id.grid_text);
            holder.txtView.setText("Text");
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        if (position == 0) {
            holder.txtView.setTypeface(Typeface.DEFAULT);
        } else {
            Log.e("position", "" + position);
            holder.txtView.setTypeface(Typeface.createFromAsset(this.mContext.getAssets(), this.Imageid[position]));
        }
        holder.txtView.setTextColor(ContextCompat.getColor(this.mContext, R.color.white));
        if (position == this.selPos) {
            holder.txtView.setTextColor(ContextCompat.getColor(this.mContext, R.color.colorPrimary));
        }
        return view;
    }

    public void setSelected(int position) {
        this.selPos = position;
        notifyDataSetChanged();
    }
}
