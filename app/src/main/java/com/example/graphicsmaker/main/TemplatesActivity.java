package com.example.graphicsmaker.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.graphicsmaker.R;
import com.example.graphicsmaker.adapter.ViewPagerAdapter;


import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import vocsy.ads.GoogleAds;

public class TemplatesActivity extends Activity implements MaterialTabListener {
    public static final int OPEN_UPDATE_ACITIVITY_TEMP = 1124;
    ViewPagerAdapter adapter;
    private Editor editor;
    boolean isChanged = false;

    ViewPager pager;
    SharedPreferences prefs;
    SharedPreferences remove_ad_pref;
    MaterialTabHost tabHost;
    private Typeface ttfHeader;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_templates);

        this.ttfHeader = Constants.getHeaderTypeface(this);
        this.prefs = getSharedPreferences("MY_PREFS_NAME", 0);
        this.editor = getSharedPreferences("MY_PREFS_NAME", 0).edit();
        this.isChanged = this.prefs.getBoolean("isChanged", false);
        this.remove_ad_pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        GoogleAds.getInstance().admobBanner(this, findViewById(R.id.adView));

        ((TextView) findViewById(R.id.txt_appname)).setTypeface(this.ttfHeader);
        initUI();
        ((ImageButton) findViewById(R.id.btn_bck)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                TemplatesActivity.this.onBackPressed();
            }
        });
    }

    private void initUI() {
        this.tabHost = (MaterialTabHost) findViewById(R.id.tabHost);
        this.pager = (ViewPager) findViewById(R.id.pager);
        this.adapter = new ViewPagerAdapter(this, getFragmentManager());
        this.adapter.notifyDataSetChanged();
        this.pager.setAdapter(this.adapter);
        this.pager.setOnPageChangeListener(new SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                TemplatesActivity.this.tabHost.setSelectedNavigationItem(position);
            }
        });
        for (int i = 0; i < this.adapter.getCount(); i++) {
            this.tabHost.addTab(this.tabHost.newTab().setText(this.adapter.getPageTitle(i)).setTabListener(this));
        }
    }

    public void onTabSelected(MaterialTab tab) {
        if (this.pager != null) {
            this.pager.setCurrentItem(tab.getPosition(), true);
        }
    }

    public void onTabReselected(MaterialTab tab) {
    }

    public void onTabUnselected(MaterialTab tab) {
    }

    protected void onResume() {
        super.onResume();
        this.isChanged = this.prefs.getBoolean("isChanged", false);
        if (this.isChanged) {
            this.tabHost.setSelectedNavigationItem(0);
            this.adapter = new ViewPagerAdapter(this, getFragmentManager());
            this.adapter.notifyDataSetChanged();
            this.pager.setAdapter(this.adapter);
            this.pager.setCurrentItem(0, true);
            this.editor.putBoolean("isChanged", false);
            this.editor.commit();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1 && requestCode == OPEN_UPDATE_ACITIVITY_TEMP) {
            this.isChanged = this.prefs.getBoolean("isChanged", false);
            if (this.isChanged) {
                this.adapter = new ViewPagerAdapter(this, getFragmentManager());
                this.adapter.notifyDataSetChanged();
                this.pager.setAdapter(this.adapter);
                this.pager.setCurrentItem(0, true);
                this.editor.putBoolean("isChanged", false);
                this.editor.commit();
            }
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onDestroy() {
        super.onDestroy();
        this.tabHost = null;
        this.pager = null;
        this.ttfHeader = null;
        this.adapter = null;
        Constants.freeMemory();
    }
}
