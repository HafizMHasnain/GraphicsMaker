package com.example.graphicsmaker.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.example.graphicsmaker.R;
import com.example.graphicsmaker.utility.ImageUtils;
import com.example.graphicsmaker.utility.StorageConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import vocsy.ads.GoogleAds;

public class SavedHistoryActivity extends Activity {
    static File[] listFile;
    static ArrayList<String> pathsOfFile = new ArrayList();
    static int pos;
    Context c;
    int count = 0;
    ImageAdapter imageAdapter;
    GridView imagegrid;
    RelativeLayout mAdView;

    TextView no_image;
    RelativeLayout rel_text;
    SharedPreferences remove_ad_pref;
    int screenWidth;
    TextView toolbar_title;

    class ImageAdapter extends BaseAdapter {
        Context context;
        LayoutInflater mInflater;

        class ViewHolder {
            ImageView imageview;
            RelativeLayout lay_image;

            ViewHolder() {
            }
        }

        public ImageAdapter(Context c) {
            this.context = c;
            mInflater = ((LayoutInflater) this.context.getSystemService(LAYOUT_INFLATER_SERVICE));
        }

        public int getCount() {
            return SavedHistoryActivity.this.count;
        }

        public Object getItem(int position) {
            return Integer.valueOf(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            int p = position;
            SavedHistoryActivity.pos = position;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = this.mInflater.inflate(R.layout.picker_default_thumbnail, null);
                holder.lay_image = (RelativeLayout) convertView.findViewById(R.id.lay_image);
                holder.imageview = (ImageView) convertView.findViewById(R.id.thumbnail_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.lay_image.getLayoutParams().height = SavedHistoryActivity.this.screenWidth / 2;
            holder.imageview.setId(position);
            Glide.with(this.context).load(SavedHistoryActivity.listFile[position]).placeholder((int) R.drawable.loading2).error((int) R.drawable.error2).into(holder.imageview);
            return convertView;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        requestWindowFeature(1);
        setContentView(R.layout.activity_saved_history);

        this.remove_ad_pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        GoogleAds.getInstance().admobBanner(this, findViewById(R.id.adView));

        DisplayMetrics dimension = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dimension);
        this.screenWidth = dimension.widthPixels - ImageUtils.dpToPx(this, 15);
        this.no_image = (TextView) findViewById(R.id.no_image);
        this.rel_text = (RelativeLayout) findViewById(R.id.rel_text);
        this.no_image.setTypeface(Constants.getTextTypeface(this));
        ((TextView) findViewById(R.id.SavedPictures)).setTypeface(Constants.getHeaderTypeface(this));
        findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SavedHistoryActivity.this.finish();
            }
        });
        findViewById(R.id.btn_home).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SavedHistoryActivity.this.setResult(-1);
                SavedHistoryActivity.this.finish();
            }
        });
        this.imagegrid = (GridView) findViewById(R.id.gridView);
        getImageAndView();
        this.imagegrid.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent i = new Intent(SavedHistoryActivity.this, ShareImageActivity.class);
                i.putExtra("uri", SavedHistoryActivity.listFile[position].getAbsolutePath());
                i.putExtra("way", "Gallery");
                i.putExtra("view", "histry");
                SavedHistoryActivity.this.startActivity(i);
                if (!SavedHistoryActivity.this.remove_ad_pref.getBoolean("isAdsDisabled", false)) {

                }
            }
        });
        this.imagegrid.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                SavedHistoryActivity.this.showOptionsDialog(position, view);
                return true;
            }
        });
    }

    public void Banner(final RelativeLayout Ad_Layout, final Context context,
                       String Ad_Id) {

    }


    private void getImageAndView() {

        SavedHistoryActivity.this.getFromSdcard();
        SavedHistoryActivity.this.c = SavedHistoryActivity.this;
        SavedHistoryActivity.this.imageAdapter = new ImageAdapter(SavedHistoryActivity.this.c);


        SavedHistoryActivity.this.imagegrid.setAdapter(SavedHistoryActivity.this.imageAdapter);
        if (SavedHistoryActivity.this.count == 0) {
            SavedHistoryActivity.this.rel_text.setVisibility(View.VISIBLE);
        } else {
            SavedHistoryActivity.this.rel_text.setVisibility(View.GONE);
        }
    }

    private void showOptionsDialog(final int position, View view) {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(1);
        dialog.setContentView(R.layout.options_dialog);
        ((TextView) dialog.findViewById(R.id.open)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(SavedHistoryActivity.this, ShareImageActivity.class);
                i.putExtra("uri", SavedHistoryActivity.listFile[position].getAbsolutePath());
                i.putExtra("way", "Gallery");
                i.putExtra("view", "histry");
                SavedHistoryActivity.this.startActivity(i);
                if (!SavedHistoryActivity.this.remove_ad_pref.getBoolean("isAdsDisabled", false)) {

                }
                dialog.dismiss();
            }
        });
        ((TextView) dialog.findViewById(R.id.delete)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (SavedHistoryActivity.this.deleteFile(Uri.parse(SavedHistoryActivity.listFile[position].getAbsolutePath()))) {
                    SavedHistoryActivity.listFile = null;
                    SavedHistoryActivity.this.getImageAndView();
                }
                dialog.dismiss();
            }
        });
        ((TextView) dialog.findViewById(R.id.cancel)).setOnClickListener(new OnClickListener() {
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
                    bl = getApplicationContext().deleteFile(file.getName());
                }
            }
            sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file)));
        } catch (Exception e2) {
            Toast.makeText(this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
        }
        return bl;
    }

    public void getFromSdcard() {
        File file = StorageConfiguration.getSavedImagesPath();
        if (file.isDirectory()) {
            listFile = file.listFiles();
            this.count = listFile.length;
            Arrays.sort(listFile, new Comparator<File>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                public int compare(File f1, File f2) {
                    return Long.compare(f2.lastModified(), f1.lastModified());
                }
            });
        }
    }

    private boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    protected void onResume() {
        super.onResume();
    }

    public void onBackPressed() {
        GoogleAds.getInstance().showCounterInterstitialAd(SavedHistoryActivity.this,
                () -> {
                    finish();
                });
    }


    public void onDestroy() {
        super.onDestroy();
        try {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Glide.get(SavedHistoryActivity.this).clearDiskCache();
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            Glide.get(this).clearMemory();
            pathsOfFile.clear();
            this.toolbar_title = null;
            this.no_image = null;
            this.c = null;
            this.rel_text = null;
            this.imagegrid = null;
            this.imageAdapter = null;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        Constants.freeMemory();
    }


}
