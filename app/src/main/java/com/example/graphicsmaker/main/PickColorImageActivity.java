package com.example.graphicsmaker.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.graphicsmaker.R;


public class PickColorImageActivity extends Activity {
    int height;
    float initialX;
    float initialY;
    GetColorListener onGetColor;
    int pixel = -1;
    float screenHeight;
    float screenWidth;
    int visiPosition = 0;
    String way;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.dialog_color);

        this.onGetColor = CreatePoster.Companion.getActivity();
        this.way = getIntent().getStringExtra("way");
        this.visiPosition = getIntent().getIntExtra("visiPosition", 0);
        this.pixel = getIntent().getIntExtra("color", 0);
        DisplayMetrics dimension = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dimension);
        this.screenWidth = (float) dimension.widthPixels;
        this.height = 55;
        this.screenHeight = (float) (dimension.heightPixels - this.height);
        ImageView img_base = (ImageView) findViewById(R.id.img_base);
        ((TextView) findViewById(R.id.txt_head)).setTypeface(Constants.getTextTypeface(this));
        img_base.setImageBitmap(CreatePoster.Companion.getBitmapNot());
        ImageView img_done = (ImageView) findViewById(R.id.img_done);
        ImageView img_back = (ImageView) findViewById(R.id.img_back);
        final ImageView img_putcolor = (ImageView) findViewById(R.id.img_putcolor);
        img_putcolor.setBackgroundColor(this.pixel);
        img_base.setOnTouchListener(new OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        PickColorImageActivity.this.initialX = event.getX();
                        PickColorImageActivity.this.initialY = event.getY();
                        try {
                            PickColorImageActivity.this.pixel = CreatePoster.getBitmapNot().getPixel((int) PickColorImageActivity.this.initialX, (int) PickColorImageActivity.this.initialY);
                            img_putcolor.setBackgroundColor(PickColorImageActivity.this.pixel);
                            break;
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                            PickColorImageActivity.this.pixel = 0;
                            break;
                        }
                    case 2:
                        PickColorImageActivity.this.initialX = event.getX();
                        PickColorImageActivity.this.initialY = event.getY();
                        try {
                            PickColorImageActivity.this.pixel = CreatePoster.getBitmapNot().getPixel((int) PickColorImageActivity.this.initialX, (int) PickColorImageActivity.this.initialY);
                            img_putcolor.setBackgroundColor(PickColorImageActivity.this.pixel);
                            break;
                        } catch (IllegalArgumentException e2) {
                            e2.printStackTrace();
                            PickColorImageActivity.this.pixel = 0;
                            break;
                        }
                }
                return true;
            }
        });
        img_done.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PickColorImageActivity.this.onGetColor.onColor(PickColorImageActivity.this.pixel, PickColorImageActivity.this.way, PickColorImageActivity.this.visiPosition);
                PickColorImageActivity.this.finish();
            }
        });
        img_back.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                PickColorImageActivity.this.onBackPressed();
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        this.onGetColor.onColor(0, this.way, this.visiPosition);
        finish();
    }
}
