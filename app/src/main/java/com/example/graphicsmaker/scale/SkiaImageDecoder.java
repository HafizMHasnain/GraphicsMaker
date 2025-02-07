package com.example.graphicsmaker.scale;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.text.TextUtils;
import java.io.InputStream;
import java.util.List;

public class SkiaImageDecoder implements ImageDecoder {
    private static final String ASSET_PREFIX = "file:///android_asset/";
    private static final String FILE_PREFIX = "file://";
    private static final String RESOURCE_PREFIX = "android.resource://";
    private final Config bitmapConfig;

    public SkiaImageDecoder() {
        this(null);
    }

    public SkiaImageDecoder(Config bitmapConfig) {
        Config globalBitmapConfig = SubsamplingScaleImageView.getPreferredBitmapConfig();
        if (bitmapConfig != null) {
            this.bitmapConfig = bitmapConfig;
        } else if (globalBitmapConfig != null) {
            this.bitmapConfig = globalBitmapConfig;
        } else {
            this.bitmapConfig = Config.RGB_565;
        }
    }

    public Bitmap decode(Context context, Uri uri) throws Exception {
        Bitmap bitmap = null;
        String uriString = uri.toString();
        Options options = new Options();
        options.inPreferredConfig = this.bitmapConfig;
        if (uriString.startsWith(RESOURCE_PREFIX)) {
            Resources res;
            String packageName = uri.getAuthority();
            if (context.getPackageName().equals(packageName)) {
                res = context.getResources();
            } else {
                res = context.getPackageManager().getResourcesForApplication(packageName);
            }
            int id = 0;
            List<String> segments = uri.getPathSegments();
            int size = segments.size();
            if (size == 2 && ((String) segments.get(0)).equals("drawable")) {
                id = res.getIdentifier((String) segments.get(1), "drawable", packageName);
            } else if (size == 1 && TextUtils.isDigitsOnly((CharSequence) segments.get(0))) {
                try {
                    id = Integer.parseInt((String) segments.get(0));
                } catch (NumberFormatException e) {
                }
            }
            bitmap = BitmapFactory.decodeResource(context.getResources(), id, options);
        } else if (uriString.startsWith(ASSET_PREFIX)) {
            bitmap = BitmapFactory.decodeStream(context.getAssets().open(uriString.substring(ASSET_PREFIX.length())), null, options);
        } else if (uriString.startsWith(FILE_PREFIX)) {
            try {
                bitmap = BitmapFactory.decodeFile(uriString.substring(FILE_PREFIX.length()), options);
            } catch (OutOfMemoryError e2) {
                bitmap = null;
            }
        } else {
            InputStream inputStream = null;
            try {
                inputStream = context.getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e3) {
                    }
                }
            } catch (Throwable th) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e4) {
                    }
                }
            }
        }
        if (bitmap != null) {
            return bitmap;
        }
        throw new RuntimeException("Skia image region decoder returned null bitmap - image format may not be supported");
    }
}
