package com.example.graphicsmaker;

import android.content.Context;

import java.io.IOException;

import vocsy.ads.AdsHandler;


public class JniUtils {
    public static byte[] decryptResourceJNI(Context context, String str) {
        int i = context.getResources().getIdentifier(str, "raw", context.getPackageName());

        try {
            return AdsHandler.getByte(context, i);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
