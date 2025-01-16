package com.example.graphicsmaker.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.graphicsmaker.R;
import com.example.graphicsmaker.sticker_fragment.FragmentBackgrounds;
import com.example.graphicsmaker.sticker_fragment.FragmentCard;
import com.example.graphicsmaker.sticker_fragment.FragmentColor;
import com.example.graphicsmaker.sticker_fragment.FragmentGradient;
import com.example.graphicsmaker.sticker_fragment.FragmentImage;
import com.example.graphicsmaker.sticker_fragment.FragmentPoster;
import com.example.graphicsmaker.sticker_fragment.FragmentTexture;

import java.util.ArrayList;

public class pagerAdapter extends FragmentPagerAdapter {


    ArrayList<Fragment> fragments = new ArrayList();
    Context context;
    Typeface ttf;
    String typeGradient = "";
    int[] colors = null;
    GradientDrawable.Orientation orient;
    int prog_radious = 0;
    
    public pagerAdapter(@NonNull FragmentManager fm,Context context,Typeface ttf,String typeGradient,int[] colors,GradientDrawable.Orientation orientint, int prog_radious) {
        super(fm);
        Fragment f = new Fragment();
        for (int i = 0; i < 7; i++) {
            fragments.add(f);
        }
    }


    @Override
    public int getCount()
    {
        return 7;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        CharSequence text = "";
        if (position == 0) {
            return "Poster";
        }
        if (position == 1) {
            return "Card";
        }
        if (position == 2) {
            return "Background";
        }
        if (position == 3) {
            return "Texture";
        }
        if (position == 4) {
            return "Image";
        }
        if (position == 5) {
            return "Gradient";
        }
        if (position == 6) {
            return "Color";
        }
        return text;
    }

    @NonNull
    @Override
    public Fragment getItem(int num) {
        Fragment fragmentbackgund = null;
        if (num == 0){
            fragmentbackgund = new FragmentPoster();
        } else if (num == 1){
            fragmentbackgund = new FragmentCard();
        } else if (num == 2) {
            fragmentbackgund = new FragmentBackgrounds();
        } else if (num == 3) {
            fragmentbackgund = new FragmentTexture();
        } else if (num == 4) {
            fragmentbackgund = new FragmentImage();
        } else if (num == 5) {
            fragmentbackgund = new FragmentGradient();
            Bundle bundle = new Bundle();
            bundle.putString("typeGradient", typeGradient);
            bundle.putIntArray("colorArr", colors);
            bundle.putSerializable("orintation", orient);
            bundle.putInt("prog_radious", prog_radious);
            fragmentbackgund.setArguments(bundle);
        } else if (num == 6) {
            fragmentbackgund = new FragmentColor();
        }

        fragments.set(num, fragmentbackgund);
        return fragmentbackgund;
    }
}