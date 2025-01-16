package com.example.graphicsmaker.utility;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;

import com.example.graphicsmaker.App;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

//public class StorageConfiguration {
//
//    public static final String APP_NAME = "Logo Maker";
//    public static final String SAVED = "Saved Images";
//    public static final String DESIGN = "Design";
//    public static final String DATA = ".data";
//
//    private static File createFileIfNeeded(File file) {
//
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//
//        return file;
//    }
//
//    private static File getBaseDirectory() {
//        File file;
//        ContextWrapper wrapper = new ContextWrapper(App.getApp());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            file = wrapper.getExternalMediaDirs()[0];
//        } else {
//            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), APP_NAME);
//        }
//        return createFileIfNeeded(file);
//    }
//
//    public static File getSavedImagesPath() {
//        File file = new File(getBaseDirectory(), SAVED);
//        return createFileIfNeeded(file);
//    }
//
//    public static File getDesignPath() {
//        File file = new File(getBaseDirectory(), DESIGN);
//        return createFileIfNeeded(file);
//    }
//
//    public static File getDataPath() {
//        File file = new File(getBaseDirectory(), DATA);
//        return createFileIfNeeded(file);
//    }
//
//}


public class StorageConfiguration {

    public static final String APP_NAME = "Logo Maker";
    public static final String SAVED = "Saved Images";
    public static final String DESIGN = "Design";
    public static final String DATA = ".data";

    private static File createFileIfNeeded(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    private static File getBaseDirectory() {
        ContextWrapper wrapper = new ContextWrapper(App.getApp());

        // Use app-specific storage directory
        return wrapper.getExternalFilesDir(null);
    }

    public static File getSavedImagesPath() {
        File file = new File(getBaseDirectory(), SAVED);
        return createFileIfNeeded(file);
    }

    public static File getDesignPath() {
        File file = new File(getBaseDirectory(), DESIGN);
        return createFileIfNeeded(file);
    }

    public static File getDataPath() {
        File file = new File(getBaseDirectory(), DATA);
        return createFileIfNeeded(file);
    }

    public static File saveImageToAppStorage(Context context, Bitmap bitmap, String fileName) {
        File imagesDir = getSavedImagesPath();
        File imageFile = new File(imagesDir, fileName);

        try (FileOutputStream out = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageFile;
    }
}

