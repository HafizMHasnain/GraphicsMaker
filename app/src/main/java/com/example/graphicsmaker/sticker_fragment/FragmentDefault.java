package com.example.graphicsmaker.sticker_fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.graphicsmaker.R;
import com.example.graphicsmaker.adapter.RecyclerTemplateAdapter;
import com.example.graphicsmaker.create.DatabaseHandler;
import com.example.graphicsmaker.create.OnDataChanged;
import com.example.graphicsmaker.create.TemplateInfo;
import com.example.graphicsmaker.main.Constants;
import com.example.graphicsmaker.main.CreatePoster;
import com.example.graphicsmaker.main.TemplatesActivity;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FragmentDefault extends Fragment implements OnDataChanged {
    private Animation animSlideDown;
    private Animation animSlideUp;
    String catName;
    boolean gridScrolled = false;
    RelativeLayout lay_dialog;
    RecyclerTemplateAdapter myFramesAdapter;
    ProgressBar progress_bar;
    RecyclerView recylr_templates;
    float screenWidth;
    private ArrayList<TemplateInfo> templateList = new ArrayList();
    LordDataOperationAsync thumbLoadAsync;
    Typeface ttf;
    TextView txt_dialog;

    public class LordDataOperationAsync extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            FragmentDefault.this.progress_bar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... params) {
            try {
                FragmentDefault.this.templateList.clear();
                DatabaseHandler dh = DatabaseHandler.getDbHandler(FragmentDefault.this.getActivity());
                FragmentDefault.this.templateList = dh.getTemplateListDes("USER", "DESC");
                dh.close();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return "yes";
        }

        @SuppressLint("ResourceAsColor")
        protected void onPostExecute(String result) {
            FragmentDefault.this.progress_bar.setVisibility(View.GONE);
            if (FragmentDefault.this.templateList.size() != 0) {
                FragmentDefault.this.myFramesAdapter = new RecyclerTemplateAdapter(FragmentDefault.this.getActivity(), FragmentDefault.this.templateList, FragmentDefault.this.screenWidth);
                FragmentDefault.this.recylr_templates.setAdapter(FragmentDefault.this.myFramesAdapter);
            }
            try {
                if (!FragmentDefault.this.catName.equals("MY_TEMP")) {
                    return;
                }
                if (FragmentDefault.this.templateList.size() == 0) {
                    FragmentDefault.this.txt_dialog.setText(FragmentDefault.this.getActivity().getResources().getString(R.string.NoDesigns));

                    FragmentDefault.this.lay_dialog.setVisibility(View.VISIBLE);
                    FragmentDefault.this.lay_dialog.startAnimation(FragmentDefault.this.animSlideUp);
                } else if (FragmentDefault.this.templateList.size() <= 4) {
                    FragmentDefault.this.txt_dialog.setText(FragmentDefault.this.getActivity().getResources().getString(R.string.DesignOptionsInstruction));
                    FragmentDefault.this.lay_dialog.setVisibility(View.VISIBLE);
                    FragmentDefault.this.lay_dialog.startAnimation(FragmentDefault.this.animSlideUp);
                } else if (FragmentDefault.this.lay_dialog.getVisibility() == View.VISIBLE) {
                    FragmentDefault.this.lay_dialog.startAnimation(FragmentDefault.this.animSlideDown);
                    FragmentDefault.this.lay_dialog.setVisibility(View.GONE);
                }
            } catch (NotFoundException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e2) {
                e2.printStackTrace();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_template, container, false);
        this.catName = getArguments().getString("categoryName");
        this.progress_bar = (ProgressBar) view.findViewById(R.id.progress_bar);
        this.progress_bar.setVisibility(View.GONE);
        this.lay_dialog = (RelativeLayout) view.findViewById(R.id.lay_dialog);
        this.txt_dialog = (TextView) view.findViewById(R.id.txt_dialog);
        this.animSlideUp = Constants.getAnimUp(getActivity());
        this.animSlideDown = Constants.getAnimDown(getActivity());
        this.ttf = Constants.getTextTypeface(getActivity());
        this.txt_dialog.setTypeface(this.ttf);
        this.templateList.clear();
        DisplayMetrics dimension = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dimension);
        this.screenWidth = (float) (dimension.widthPixels / 2);
        this.recylr_templates = (RecyclerView) view.findViewById(R.id.gridview);
        this.recylr_templates.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        this.recylr_templates.setHasFixedSize(true);
        this.recylr_templates.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), this.recylr_templates, new RecyclerTouchListener.ClickListener() {
            public void onClick(View view, int position) {
                RecyclerTemplateAdapter.isTemplateLoaded = true;
                Intent intent = new Intent(FragmentDefault.this.getActivity(), CreatePoster.class);
                intent.putExtra("templateId", ((TemplateInfo) FragmentDefault.this.templateList.get(position)).getTEMPLATE_ID());
                intent.putExtra("loadUserFrame", false);
                intent.putExtra("Temp_Type", FragmentDefault.this.catName);
                FragmentDefault.this.getActivity().startActivityForResult(intent, TemplatesActivity.OPEN_UPDATE_ACITIVITY_TEMP);
            }

            public void onLongClick(View view, int position) {
                FragmentDefault.this.showOptionsDialog(position, view);
            }
        }));
        this.recylr_templates.addOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        this.thumbLoadAsync = new LordDataOperationAsync();
        this.thumbLoadAsync.execute(new String[]{""});
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 4) {
                    if (FragmentDefault.this.thumbLoadAsync.getStatus() == Status.PENDING) {
                        FragmentDefault.this.thumbLoadAsync.cancel(true);
                    }
                    if (FragmentDefault.this.thumbLoadAsync.getStatus() == Status.RUNNING) {
                        FragmentDefault.this.thumbLoadAsync.cancel(true);
                    }
                }
                return false;
            }
        });
        return view;
    }

    public void setMenuVisibility(boolean visible) {
        if (visible) {
            super.setMenuVisibility(visible);
        } else {
            super.setMenuVisibility(visible);
        }
    }

    private void showOptionsDialog(final int position, View view) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(1);
        dialog.setContentView(R.layout.options_dialog);
        TextView open = (TextView) dialog.findViewById(R.id.open);
        open.setTypeface(this.ttf);
        open.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                int template_id = ((TemplateInfo) FragmentDefault.this.templateList.get(position)).getTEMPLATE_ID();
                Intent intent = new Intent(FragmentDefault.this.getActivity(), CreatePoster.class);
                intent.putExtra("templateId", template_id);
                intent.putExtra("loadUserFrame", false);
                intent.putExtra("Temp_Type", FragmentDefault.this.catName);
                FragmentDefault.this.getActivity().startActivityForResult(intent, TemplatesActivity.OPEN_UPDATE_ACITIVITY_TEMP);
                dialog.dismiss();
            }
        });
        TextView delete = (TextView) dialog.findViewById(R.id.delete);
        delete.setTypeface(this.ttf);
        delete.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                TemplateInfo tInfo = (TemplateInfo) FragmentDefault.this.templateList.get(position);
                DatabaseHandler dh = DatabaseHandler.getDbHandler(FragmentDefault.this.getActivity());
                boolean b = dh.deleteTemplateInfo(tInfo.getTEMPLATE_ID());
                dh.close();
                if (b) {
                    if (!FragmentDefault.this.templateList.isEmpty()) {
                        FragmentDefault.this.templateList.clear();
                    }
                    FragmentDefault.this.myFramesAdapter.notifyDataSetChanged();
                    FragmentDefault.this.thumbLoadAsync = new LordDataOperationAsync();
                    FragmentDefault.this.thumbLoadAsync.execute(new String[]{""});
                } else {
                    Toast.makeText(FragmentDefault.this.getActivity(), FragmentDefault.this.getResources().getString(R.string.del_error_toast), Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        cancel.setTypeface(this.ttf);
        cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_;
        dialog.show();
    }

    private boolean deleteFile(Uri uri) {
        boolean bl = false;
        try {
            File file = new File(uri.getPath());
            bl = file.delete();
            if (file.exists()) {
                try {
                    bl = file.getCanonicalFile().delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (file.exists()) {
                    bl = getActivity().getApplicationContext().deleteFile(file.getName());
                }
            }
            getActivity().sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file)));
        } catch (Exception e2) {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
        }
        return bl;
    }

    public void updateAdapter() {
        DatabaseHandler dh = DatabaseHandler.getDbHandler(getActivity());
        this.templateList = dh.getTemplateListDes("USER", "DESC");
        dh.close();
        this.myFramesAdapter = new RecyclerTemplateAdapter(getActivity(), this.templateList, this.screenWidth);
        this.recylr_templates.setAdapter(this.myFramesAdapter);
    }

    public void updateInstLay(boolean close) {
        if (this.templateList.size() == 0) {
            Log.i("testing", "Frame  LAy Updated");
        }
    }

    public void onResume() {
        super.onResume();
        RecyclerTemplateAdapter.isTemplateLoaded = false;
        if (this.myFramesAdapter != null) {
            this.myFramesAdapter.notifyDataSetChanged();
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.recylr_templates = null;
        this.myFramesAdapter = null;
        Constants.freeMemory();
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Glide.get(FragmentDefault.this.getActivity()).clearDiskCache();
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            Glide.get(getActivity()).clearMemory();
            if (this.thumbLoadAsync != null) {
                if (this.thumbLoadAsync.getStatus() == Status.PENDING) {
                    this.thumbLoadAsync.cancel(true);
                }
                if (this.thumbLoadAsync.getStatus() == Status.RUNNING) {
                    this.thumbLoadAsync.cancel(true);
                }
            }
            this.recylr_templates = null;
            this.myFramesAdapter = null;
            this.templateList.clear();
            this.progress_bar = null;
            this.txt_dialog = null;
            this.ttf = null;
            this.animSlideUp = null;
            this.animSlideDown = null;
            this.lay_dialog = null;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        Constants.freeMemory();
    }
}
