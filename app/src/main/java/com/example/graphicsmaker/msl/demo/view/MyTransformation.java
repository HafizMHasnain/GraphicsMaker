package com.example.graphicsmaker.msl.demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;

public class MyTransformation extends BitmapTransformation {
    private boolean Is_flip;

    public MyTransformation(Context context, boolean Is_flip) {
        super();// remove context
        this.Is_flip = Is_flip;
    }

    protected Bitmap transform(@NotNull BitmapPool pool, @NotNull Bitmap toTransform, int outWidth, int outHeight) {
        return rotateBitmap(toTransform, this.Is_flip);
    }

    public String getId() {
        return "com.example.helpers.MyTransformation";
    }

    public static Bitmap rotateBitmap(Bitmap source, boolean Is_flip) {
        Matrix matrix = new Matrix();
        if (!Is_flip) {
            return null;
        }
        matrix.preScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
