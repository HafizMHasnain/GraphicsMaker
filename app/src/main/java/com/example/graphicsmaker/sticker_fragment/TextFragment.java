package com.example.graphicsmaker.sticker_fragment;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.core.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.graphicsmaker.R;
import com.example.graphicsmaker.main.Constants;
import com.example.graphicsmaker.utility.ImageUtils;


public class TextFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_text_fragment, container, false);
        String Number = getArguments().getString("Number");
        TextView textHi = (TextView) view.findViewById(R.id.auto_fit_edit_text);
        ImageView img_Up = (ImageView) view.findViewById(R.id.img_Up);
        DisplayMetrics dimension = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dimension);
        int screenHeight = (dimension.heightPixels - ImageUtils.dpToPx(getActivity(), 90)) / 2;
        if (Number.equals("1")) {
            img_Up.getLayoutParams().width = screenHeight / 5;
            img_Up.getLayoutParams().height = screenHeight / 5;
            img_Up.setImageResource(R.drawable.frag1);
            textHi.setText(getActivity().getResources().getString(R.string.txt_Frag1));
        } else if (Number.equals("2")) {
            img_Up.getLayoutParams().width = screenHeight / 5;
            img_Up.getLayoutParams().height = screenHeight / 5;
            img_Up.setImageResource(R.drawable.frag2);
            textHi.setText(getActivity().getResources().getString(R.string.txt_Frag2));
        } else if (Number.equals("3")) {
            img_Up.setImageResource(R.drawable.frag3);
            textHi.setText(getActivity().getResources().getString(R.string.txt_Frag3) + "\n" + getActivity().getResources().getString(R.string.txt_Frag31));
        } else if (Number.equals("4")) {
            img_Up.setImageResource(R.drawable.frag3);
            textHi.setText(getActivity().getResources().getString(R.string.txt_Frag4) + "\n" + getActivity().getResources().getString(R.string.txt_Frag41));
        } else if (Number.equals("5")) {
            img_Up.getLayoutParams().width = screenHeight / 5;
            img_Up.getLayoutParams().height = screenHeight / 5;
            img_Up.setImageResource(R.drawable.frag5);
            textHi.setText(getActivity().getResources().getString(R.string.txt_Frag5));
        }
        textHi.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "akifont4.ttf"));
        textHi.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        return view;
    }

    public void onDestroyView() {
        super.onDestroyView();
        Constants.freeMemory();
    }

    public void onDestroy() {
        super.onDestroy();
        Constants.freeMemory();
    }
}
