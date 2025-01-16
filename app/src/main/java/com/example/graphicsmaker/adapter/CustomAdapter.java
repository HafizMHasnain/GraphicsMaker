package com.example.graphicsmaker.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import androidx.legacy.app.FragmentPagerAdapter;

import com.example.graphicsmaker.sticker_fragment.TextFragment;


public class CustomAdapter extends FragmentPagerAdapter {
    private Activity activity;
    String[] arrIs = new String[]{"1", "2", "5"};

    public CustomAdapter(Activity context, FragmentManager fm) {
        super(fm);
        this.activity = context;
    }

    public Fragment getItem(int position) {
        String numbers = this.arrIs[position];
        Fragment f = new TextFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Number", numbers);
        f.setArguments(bundle);
        return f;
    }

    public CharSequence getPageTitle(int position) {
        return this.arrIs[position];
    }

    public int getCount() {
        return this.arrIs.length;
    }
}
