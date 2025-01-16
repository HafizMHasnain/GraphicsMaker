package com.example.graphicsmaker.adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.graphicsmaker.R;
import com.example.graphicsmaker.sticker_fragment.StickersFragment;


import java.util.ArrayList;

public class StickerViewPagerAdapter extends FragmentPagerAdapter {
    private String[] TITLES;
    private Context _context;
    String[] cateName = new String[]{
            /* new */
          "badge",
            "bakery",
            "ribbon",
            "icon",
            "payment",
            "beauty",
            "bistro",
            "profession",
            "people",
            "christianity",
            "pets",
            "letters",
            "babymom",
            "fashion",
            "business",
            "threeD",
            /* new */
            "corp",
            "pro",
            "rest",
            "cam",
            "vid",
            "shape",
            "cir",
            "lef",
            NotificationCompat.CATEGORY_SOCIAL,
            "party",
            "text",
            "ngo",
            "sport",
            "squre",
            "star",
            "toys",
            "butter",
            "cars",
            "music",
            "festi",
            "tattoo",
            "flower",
            "heart",
            "hallow",
            "holi",
            "animals"
    };
    ArrayList<Fragment> fragments;

    public StickerViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this._context = context;
        this.fragments = new ArrayList<>();
        Fragment f = new StickersFragment();
        for (int i = 0; i < 42; i++) {
            this.fragments.add(f);
        }
        this.TITLES = new String[]{
                _context.getResources().getString(R.string.badge),
                _context.getResources().getString(R.string.bakery),
                _context.getResources().getString(R.string.ribbon),
                _context.getResources().getString(R.string.icon),
                _context.getResources().getString(R.string.payment),
                _context.getResources().getString(R.string.beautyy),
                _context.getResources().getString(R.string.bistroo),
                _context.getResources().getString(R.string.profession),
                _context.getResources().getString(R.string.peopl),
                _context.getResources().getString(R.string.christianity),
                _context.getResources().getString(R.string.pets),
                _context.getResources().getString(R.string.letters),
                _context.getResources().getString(R.string.babymom),
                _context.getResources().getString(R.string.fashion),
                _context.getResources().getString(R.string.business),
                _context.getResources().getString(R.string.threeD),
                _context.getResources().getString(R.string.corporate),
                _context.getResources().getString(R.string.property),
                _context.getResources().getString(R.string.restaurant_cafe),
                _context.getResources().getString(R.string.camera),
                _context.getResources().getString(R.string.video),
                _context.getResources().getString(R.string.shapes),
                _context.getResources().getString(R.string.circle),
                _context.getResources().getString(R.string.leaf),
                _context.getResources().getString(R.string.social),
                _context.getResources().getString(R.string.party),
                _context.getResources().getString(R.string.text),
                _context.getResources().getString(R.string.ngo),
                _context.getResources().getString(R.string.sports),
                _context.getResources().getString(R.string.square),
                _context.getResources().getString(R.string.star),
                _context.getResources().getString(R.string.toys),
                _context.getResources().getString(R.string.butterfly),
                _context.getResources().getString(R.string.cars),
                _context.getResources().getString(R.string.music),
                _context.getResources().getString(R.string.festival),
                _context.getResources().getString(R.string.tattoo),
                _context.getResources().getString(R.string.flower),
                _context.getResources().getString(R.string.heart),
                _context.getResources().getString(R.string.halloween),
                _context.getResources().getString(R.string.holiday),
                _context.getResources().getString(R.string.animals_birds)
        };
    }

    @NonNull
    public Fragment getItem(int position) {
        String categoryName = this.cateName[position];
        Fragment f = new StickersFragment();
        Bundle bundle = new Bundle();
        bundle.putString("cataName", categoryName);
        f.setArguments(bundle);
        this.fragments.set(position, f);
        return f;
    }

    public CharSequence getPageTitle(int position) {
        return this.TITLES[position];
    }

    public int getCount() {
        return this.TITLES.length;
    }

    public Fragment currentFragment(int position) {
        return (Fragment) this.fragments.get(position);
    }
}
