package com.example.graphicsmaker.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import androidx.legacy.app.FragmentPagerAdapter;

import com.example.graphicsmaker.R;
import com.example.graphicsmaker.sticker_fragment.FragmentDefault;



public class ViewPagerAdapter extends FragmentPagerAdapter {
    private String[] TITLES;
    private Context _context;
    String[] cateName = new String[]{"MY_TEMP"};

    public ViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this._context = context;
        this.TITLES = new String[]{context.getResources().getString(R.string.my_design)};
    }

    public Fragment getItem(int position) {
        String categoryName = this.cateName[position];
        FragmentDefault bakgrndFragment = new FragmentDefault();
        Bundle bundle = new Bundle();
        bundle.putString("categoryName", categoryName);
        bakgrndFragment.setArguments(bundle);
        return bakgrndFragment;
    }

    public CharSequence getPageTitle(int position) {
        return this.TITLES[position];
    }

    public int getCount() {
        return this.TITLES.length;
    }
}
