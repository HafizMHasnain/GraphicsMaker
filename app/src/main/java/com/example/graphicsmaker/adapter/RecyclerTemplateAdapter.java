package com.example.graphicsmaker.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.example.graphicsmaker.R;
import com.example.graphicsmaker.create.DynamicTemplateView;
import com.example.graphicsmaker.create.TemplateInfo;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RecyclerTemplateAdapter extends Adapter<RecyclerTemplateAdapter.ViewHolder> {
    public static boolean isTemplateLoaded = false;
    Context context;
    float screenH_half;
    float screenW_half;
    int selected_position = -1;
    private ArrayList<TemplateInfo> templateList = new ArrayList();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout center_rel;
        DynamicTemplateView iv_OutputView;
        RelativeLayout layout;
        TemplateInfo templateInfo;

        public ViewHolder(View view) {
            super(view);
            this.center_rel = (RelativeLayout) view.findViewById(R.id.center_rel);
            this.iv_OutputView = (DynamicTemplateView) view.findViewById(R.id.iv_OutputView);
            this.layout = (RelativeLayout) view.findViewById(R.id.lay);
        }
    }

    public RecyclerTemplateAdapter(Context context, ArrayList<TemplateInfo> tempInfoList, float screenW) {
        this.context = context;
        this.screenW_half = screenW;
        this.screenH_half = screenW;
        this.templateList = tempInfoList;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public int getItemCount() {
        return this.templateList.size();
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.center_rel.getLayoutParams().width = (int) this.screenW_half;
        viewHolder.center_rel.getLayoutParams().height = (int) this.screenH_half;
        viewHolder.templateInfo = (TemplateInfo) this.templateList.get(position);
        viewHolder.iv_OutputView.setViewParams(this.context, (int) this.screenW_half, (int) this.screenH_half, viewHolder.templateInfo);
        viewHolder.layout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            }
        });
    }

    public void setSelected(int position) {
        this.selected_position = position;
        notifyDataSetChanged();
    }

    public void onViewRecycled(@NotNull ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @NotNull
    public ViewHolder onCreateViewHolder(ViewGroup arg0, int position) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(arg0.getContext()).inflate(R.layout.recycler_adapter_templates, arg0, false));
        arg0.setId(position);
        arg0.setFocusable(false);
        arg0.setFocusableInTouchMode(false);
        return viewHolder;
    }

    public int getItemViewType(int position) {
        return position;
    }
}
