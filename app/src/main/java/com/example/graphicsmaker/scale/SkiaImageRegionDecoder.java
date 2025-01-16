package com.example.graphicsmaker.scale;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build.VERSION;
import android.text.TextUtils;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SkiaImageRegionDecoder implements ImageRegionDecoder {
    private static final String ASSET_PREFIX = "file:///android_asset/";
    private static final String FILE_PREFIX = "file://";
    private static final String RESOURCE_PREFIX = "android.resource://";
    private final Config bitmapConfig;
    private BitmapRegionDecoder decoder;
    private final ReadWriteLock decoderLock;

    public SkiaImageRegionDecoder() {
        this(null);
    }

    public SkiaImageRegionDecoder(Config bitmapConfig) {
        this.decoderLock = new ReentrantReadWriteLock(true);
        Config globalBitmapConfig = SubsamplingScaleImageView.getPreferredBitmapConfig();
        if (bitmapConfig != null) {
            this.bitmapConfig = bitmapConfig;
        } else if (globalBitmapConfig != null) {
            this.bitmapConfig = globalBitmapConfig;
        } else {
            this.bitmapConfig = Config.RGB_565;
        }
    }

    public Point init(Context context, Uri uri) throws Exception {
        String uriString = uri.toString();
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
            this.decoder = BitmapRegionDecoder.newInstance(context.getResources().openRawResource(id), false);
        } else if (uriString.startsWith(ASSET_PREFIX)) {
            this.decoder = BitmapRegionDecoder.newInstance(context.getAssets().open(uriString.substring(ASSET_PREFIX.length()), 1), false);
        } else if (uriString.startsWith(FILE_PREFIX)) {
            this.decoder = BitmapRegionDecoder.newInstance(uriString.substring(FILE_PREFIX.length()), false);
        } else {
            InputStream inputStream = null;
            try {
                inputStream = context.getContentResolver().openInputStream(uri);
                this.decoder = BitmapRegionDecoder.newInstance(inputStream, false);
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e2) {
                    }
                }
            } catch (Throwable th) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e3) {
                    }
                }
            }
        }
        return new Point(this.decoder.getWidth(), this.decoder.getHeight());
    }

    public Bitmap decodeRegion(Rect sRect, int sampleSize) {
        getDecodeLock().lock();
        try {
            if (this.decoder == null || this.decoder.isRecycled()) {
                throw new IllegalStateException("Cannot decode region after decoder has been recycled");
            }
            Options options = new Options();
            options.inSampleSize = sampleSize;
            options.inPreferredConfig = this.bitmapConfig;
            Bitmap bitmap = this.decoder.decodeRegion(sRect, options);
            if (bitmap != null) {
                return bitmap;
            }
            throw new RuntimeException("Skia image decoder returned null bitmap - image format may not be supported");
        } finally {
            getDecodeLock().unlock();
        }
    }

    public synchronized boolean isReady() {
        boolean z;
        z = (this.decoder == null || this.decoder.isRecycled()) ? false : true;
        return z;
    }

    public synchronized void recycle() {
        this.decoderLock.writeLock().lock();
        try {
            this.decoder.recycle();
            this.decoder = null;
            this.decoderLock.writeLock().unlock();
        } catch (Throwable th) {
            this.decoderLock.writeLock().unlock();
        }
    }

    private Lock getDecodeLock() {
        if (VERSION.SDK_INT < 21) {
            return this.decoderLock.writeLock();
        }
        return this.decoderLock.readLock();
    }
}
