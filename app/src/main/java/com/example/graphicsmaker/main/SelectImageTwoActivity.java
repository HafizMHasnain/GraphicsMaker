package com.example.graphicsmaker.main;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import com.example.graphicsmaker.R;
import com.example.graphicsmaker.adapter.pagerAdapter;
import com.example.graphicsmaker.utility.ImageUtils;
import com.example.graphicsmaker.utility.NonScrollableViewPager;
import com.example.graphicsmaker.utility.SlowScroller;
import com.example.graphicsmaker.utility.ZoomOutSlideTransformer;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.lang.reflect.Field;

import it.neokree.materialtabs.MaterialTab;
import vocsy.ads.GoogleAds;

public class SelectImageTwoActivity extends AppCompatActivity implements OnClickListener, OnGetImageOnTouch {
    private static final int OPEN_CUSTOM_ACITIVITY = 4;
    private static final int SELECT_PICTURE_FROM_CAMERA = 9062;
    private static final int SELECT_PICTURE_FROM_GALLERY = 9072;
    public static Bitmap bitmap;
    public static String hex = "";
    String backgroundName;
    int[] colorArr = null;
    int[] colors = null;
    File f;
    String hex12 = "";
    RelativeLayout lay_crop;
    Orientation orient;
    private Orientation orintn;
    NonScrollableViewPager pager;
    String profile;
    int prog_radious = 0;
    SharedPreferences remove_ad_pref;
    float screenHeight;
    float screenWidth;
    TabLayout tabHost;
    private Typeface ttf;
    private Typeface ttfHeader;
    String typeGradient = "";
    boolean updateSticker = false;
    pagerAdapter pagerAdapter;
    TextView txt_appname;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        setContentView((int) R.layout.activity_select_image);
        this.ttf = Constants.getTextTypeface(this);
        this.ttfHeader = Constants.getHeaderTypeface(this);
        this.tabHost = (TabLayout) findViewById(R.id.tabHost);
        this.pager = (NonScrollableViewPager) findViewById(R.id.pager);
        this.pager.setScrollEnabled(false);
        pagerAdapter = new pagerAdapter(getSupportFragmentManager(), SelectImageTwoActivity.this, ttf, typeGradient, colors, orient, prog_radious);
        tabHost.setupWithViewPager(pager);
        pager.setAdapter(pagerAdapter);

        this.remove_ad_pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        MaterialTab t1 = new MaterialTab(this, false);
        MaterialTab t2 = new MaterialTab(this, false);
        MaterialTab t3 = new MaterialTab(this, false);
        MaterialTab t4 = new MaterialTab(this, false);
        MaterialTab t5 = new MaterialTab(this, false);
        MaterialTab t6 = new MaterialTab(this, false);
        MaterialTab t7 = new MaterialTab(this, false);
        TabLayout.Tab t11 = new TabLayout.Tab();
        try {
            Field fieldObj = MaterialTab.class.getDeclaredField("text");
            fieldObj.setAccessible(true);
            ((TextView) fieldObj.get(t3)).setText(getResources().getString(R.string.poster_design));
            ((TextView) fieldObj.get(t3)).setText(getResources().getString(R.string.card_design));
            ((TextView) fieldObj.get(t3)).setText(getResources().getString(R.string.txt_backgrounds));
            ((TextView) fieldObj.get(t4)).setText(getResources().getString(R.string.txt_texture));
            ((TextView) fieldObj.get(t5)).setText(getResources().getString(R.string.txt_image));
            ((TextView) fieldObj.get(t6)).setText(getResources().getString(R.string.txt_gdcolor));
            ((TextView) fieldObj.get(t7)).setText(getResources().getString(R.string.txt_color));

            ((TextView) fieldObj.get(t1)).setTypeface(this.ttf, Typeface.BOLD);
            ((TextView) fieldObj.get(t2)).setTypeface(this.ttf, Typeface.BOLD);
            ((TextView) fieldObj.get(t3)).setTypeface(this.ttf, Typeface.BOLD);
            ((TextView) fieldObj.get(t4)).setTypeface(this.ttf, Typeface.BOLD);
            ((TextView) fieldObj.get(t5)).setTypeface(this.ttf, Typeface.BOLD);
            ((TextView) fieldObj.get(t6)).setTypeface(this.ttf, Typeface.BOLD);
            ((TextView) fieldObj.get(t7)).setTypeface(this.ttf, Typeface.BOLD);

            ((TextView) fieldObj.get(t1)).setTextSize(2, 17.0f);
            ((TextView) fieldObj.get(t2)).setTextSize(2, 17.0f);
            ((TextView) fieldObj.get(t3)).setTextSize(2, 17.0f);
            ((TextView) fieldObj.get(t4)).setTextSize(2, 17.0f);
            ((TextView) fieldObj.get(t5)).setTextSize(2, 17.0f);
            ((TextView) fieldObj.get(t6)).setTextSize(2, 17.0f);
            ((TextView) fieldObj.get(t7)).setTextSize(2, 17.0f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        DisplayMetrics dimension = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dimension);
        this.screenWidth = (float) dimension.widthPixels;
        this.screenHeight = (float) (dimension.heightPixels - ImageUtils.dpToPx(this, 50));
        this.f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
        this.txt_appname = findViewById(R.id.txt_appname);
                this.txt_appname.setTypeface(this.ttfHeader);
        initUI();
        this.lay_crop = (RelativeLayout) findViewById(R.id.lay_crop);
    }

    private void initUI() {
        this.pager.setAdapter(pagerAdapter);
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
           this.txt_appname.setText(this.pagerAdapter.getPageTitle(extra.getInt("tabposition", 0)));

            if (extra.getInt("tabposition", 0) == 0) {
                this.pager.setCurrentItem(0, true);
            }
            else if (extra.getInt("tabposition", 0) == 1) {
                this.pager.setCurrentItem(1, true);
            }
            else if (extra.getInt("tabposition", 0) == 2) {
                this.pager.setCurrentItem(2, true);
            }
            else if (extra.getInt("tabposition", 0) == 3) {
                this.pager.setCurrentItem(3, true);
            }
            else if (extra.getInt("tabposition", 0) == 4) {
                this.pager.setCurrentItem(4, true);
            }
            else if (extra.getInt("tabposition", 0) == 5) {
                this.colors = extra.getIntArray("colorArr");
                this.typeGradient = extra.getString("typeGradient");
                this.orient = (Orientation) extra.get("orintation");
                this.prog_radious = extra.getInt("prog_radious");
                this.pager.setCurrentItem(5, true);
            }
            else if (extra.getInt("tabposition", 0) == 6) {
                hex = extra.getString("hex");
                this.pager.setCurrentItem(6, true);
            } else {
                this.pager.setCurrentItem(0, true);
            }
        }
        this.pager.setPageTransformer(true, new ZoomOutSlideTransformer());
        changePagerScroller();
        this.pager.addOnPageChangeListener(new OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //SelectImageTwoActivity.this.tabHost.setSelectedNavigationItem(position);
            }

            public void onPageSelected(int position) {
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void changePagerScroller() {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(this.pager, new SlowScroller(this.pager.getContext()));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("texting", "error of change scroller ", e);
        }
    }


    public void onClick(View view) {
        if (view.getId() == R.id.btn_bck) {
            onBackPressed();
            return;
        }
    }

    private void callActivity(String s) {
        Bundle bundle = new Bundle();
        bundle.putString("ratio", s);
        bundle.putString("backgroundName", this.backgroundName);
        bundle.putString("profile", this.profile);
        bundle.putString("color", this.hex12);
        bundle.putString("typeGradient", this.typeGradient);
        bundle.putIntArray("colorArr", this.colorArr);
        bundle.putSerializable("orintation", this.orintn);
        bundle.putInt("prog_radious", this.prog_radious);
        bundle.putBoolean("updateSticker", this.updateSticker);
        Intent returnIntent = new Intent();
            returnIntent.putExtras(bundle);
            setResult(-1, returnIntent);
        finish();
    }

    public void ongetPosition(String backgroundName1, String profileIs, String hex, Orientation orientation, int[] colors, String typeGradient1, int prog_radious1, String ratioIs, String visibleVideo, boolean updateSticker1) {
        this.backgroundName = backgroundName1;
        this.profile = profileIs;
        this.hex12 = hex;
        this.updateSticker = updateSticker1;
        this.colorArr = colors;
        this.orintn = orientation;
        this.typeGradient = typeGradient1;
        this.prog_radious = prog_radious1;
        if (!visibleVideo.equals("showVideo")) {
            callActivity("1:1");
        } else if (this.remove_ad_pref.getBoolean("isAdsDisabled", false)) {
            callActivity("1:1");
        } else {
            GoogleAds.getInstance().showRewardedAd(SelectImageTwoActivity.this, () -> {
                callActivity("1:1");
            });

            final Dialog dialog1 = new Dialog(this/*, 16974126*/);
            dialog1.requestWindowFeature(1);
            dialog1.setContentView(R.layout.interner_connection_dialog);
            dialog1.setCancelable(true);
            LayoutParams lp = new LayoutParams();
            lp.copyFrom(dialog1.getWindow().getAttributes());
            lp.width = -1;
            lp.height = -2;
            ((TextView) dialog1.findViewById(R.id.heater)).setTypeface(this.ttfHeader);
            ((TextView) dialog1.findViewById(R.id.txt_free)).setTypeface(this.ttf);
            Button btn_ok = (Button) dialog1.findViewById(R.id.btn_ok);
            btn_ok.setTypeface(this.ttf);
            btn_ok.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    dialog1.dismiss();
                }
            });
            lp.dimAmount = 0.7f;

            if (!GoogleAds.checkConnection(this)) {
                dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_;
                dialog1.show();
                dialog1.getWindow().setAttributes(lp);
                dialog1.getWindow().addFlags(2);
            }
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (!(this.pager == null || this.pager.getChildCount() == 0 || this.pagerAdapter.currentFragment(this.pager.getCurrentItem()) == null)) {
//            this.adapter.currentFragment(this.pager.getCurrentItem()).onActivityResult(requestCode, resultCode, data);
//        }
        if (resultCode != -1) {
            return;
        }
        if (data != null || requestCode == SELECT_PICTURE_FROM_CAMERA || requestCode == 4) {
            Intent customGalleryIntent;
            if (requestCode == SELECT_PICTURE_FROM_GALLERY) {
                try {
                    bitmap = ImageUtils.getBitmapFromUri(this, data.getData(), this.screenWidth, this.screenHeight);
                    if (this.screenWidth > ((float) bitmap.getWidth()) && this.screenHeight > ((float) bitmap.getHeight())) {
                        bitmap = ImageUtils.resizeBitmap(bitmap, (int) this.screenWidth, (int) this.screenHeight);
                    }
                    if (bitmap != null) {
                        customGalleryIntent = new Intent(this, CropActivityTwo.class);
                        customGalleryIntent.putExtra("value", "image");
                        startActivityForResult(customGalleryIntent, 4);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (requestCode == SELECT_PICTURE_FROM_CAMERA) {
                try {
                    bitmap = ImageUtils.getBitmapFromUri(this, Uri.fromFile(this.f), this.screenWidth, this.screenHeight);
                    if (bitmap != null) {
                        customGalleryIntent = new Intent(this, CropActivityTwo.class);
                        customGalleryIntent.putExtra("value", "image");
                        startActivityForResult(customGalleryIntent, 4);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            if (requestCode == 4) {
                Bundle bundle = new Bundle();
                bundle.putString("profile", "no");
                Intent returnIntent = new Intent();
                returnIntent.putExtras(bundle);
                setResult(-1, returnIntent);
                finish();
            }
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        try {
            this.tabHost = null;
            this.pager = null;
            this.ttf = null;
            this.ttfHeader = null;
            pagerAdapter = null;
            this.lay_crop = null;
            Constants.freeMemory();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}

