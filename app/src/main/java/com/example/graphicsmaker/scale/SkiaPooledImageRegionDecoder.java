package com.example.graphicsmaker.scale;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
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
import android.util.Log;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Pattern;

public class SkiaPooledImageRegionDecoder implements ImageRegionDecoder {
    private static final String ASSET_PREFIX = "file:///android_asset/";
    private static final String FILE_PREFIX = "file://";
    private static final String RESOURCE_PREFIX = "android.resource://";
    private static final String TAG = SkiaPooledImageRegionDecoder.class.getSimpleName();
    private static boolean debug = false;
    private final Config bitmapConfig;
    private Context context;
    private final ReadWriteLock decoderLock;
    private DecoderPool decoderPool;
    private long fileLength;
    private Point imageDimensions;
    private final AtomicBoolean lazyInited;
    private Uri uri;

    private static class DecoderPool {
        private final Semaphore available;
        private Map<BitmapRegionDecoder, Boolean> decoders;

        private DecoderPool() {
            this.available = new Semaphore(0, true);
            this.decoders = new ConcurrentHashMap();
        }

        private synchronized boolean isEmpty() {
            return this.decoders.isEmpty();
        }

        private synchronized int size() {
            return this.decoders.size();
        }

        private BitmapRegionDecoder acquire() {
            this.available.acquireUninterruptibly();
            return getNextAvailable();
        }

        private void release(BitmapRegionDecoder decoder) {
            if (markAsUnused(decoder)) {
                this.available.release();
            }
        }

        private synchronized void add(BitmapRegionDecoder decoder) {
            this.decoders.put(decoder, Boolean.valueOf(false));
            this.available.release();
        }

        private synchronized void recycle() {
            while (!this.decoders.isEmpty()) {
                BitmapRegionDecoder decoder = acquire();
                decoder.recycle();
                this.decoders.remove(decoder);
            }
        }

        private synchronized BitmapRegionDecoder getNextAvailable() {
            BitmapRegionDecoder bitmapRegionDecoder;
            for (Entry<BitmapRegionDecoder, Boolean> entry : this.decoders.entrySet()) {
                if (!((Boolean) entry.getValue()).booleanValue()) {
                    entry.setValue(Boolean.valueOf(true));
                    bitmapRegionDecoder = (BitmapRegionDecoder) entry.getKey();
                    break;
                }
            }
            bitmapRegionDecoder = null;
            return bitmapRegionDecoder;
        }

        private synchronized boolean markAsUnused(BitmapRegionDecoder decoder) {
            boolean z;
            for (Entry<BitmapRegionDecoder, Boolean> entry : this.decoders.entrySet()) {
                if (decoder == entry.getKey()) {
                    if (((Boolean) entry.getValue()).booleanValue()) {
                        entry.setValue(Boolean.valueOf(false));
                        z = true;
                    } else {
                        z = false;
                    }
                }
            }
            z = false;
            return z;
        }
    }

    public SkiaPooledImageRegionDecoder() {
        this(null);
    }

    public SkiaPooledImageRegionDecoder(Config bitmapConfig) {
        this.decoderPool = new DecoderPool();
        this.decoderLock = new ReentrantReadWriteLock(true);
        this.fileLength = Long.MAX_VALUE;
        this.imageDimensions = new Point(0, 0);
        this.lazyInited = new AtomicBoolean(false);
        Config globalBitmapConfig = SubsamplingScaleImageView.getPreferredBitmapConfig();
        if (bitmapConfig != null) {
            this.bitmapConfig = bitmapConfig;
        } else if (globalBitmapConfig != null) {
            this.bitmapConfig = globalBitmapConfig;
        } else {
            this.bitmapConfig = Config.RGB_565;
        }
    }

    public static void setDebug(boolean debug) {
        debug = debug;
    }

    public Point init(Context context, Uri uri) throws Exception {
        this.context = context;
        this.uri = uri;
        initialiseDecoder();
        return this.imageDimensions;
    }

    private void lazyInit() {
        if (this.lazyInited.compareAndSet(false, true) && this.fileLength < Long.MAX_VALUE) {
            debug("Starting lazy init of additional decoders");
            new Thread() {
                public void run() {
                    while (SkiaPooledImageRegionDecoder.this.decoderPool != null && SkiaPooledImageRegionDecoder.this.allowAdditionalDecoder(SkiaPooledImageRegionDecoder.this.decoderPool.size(), SkiaPooledImageRegionDecoder.this.fileLength)) {
                        try {
                            if (SkiaPooledImageRegionDecoder.this.decoderPool != null) {
                                long start = System.currentTimeMillis();
                                SkiaPooledImageRegionDecoder.this.debug("Starting decoder");
                                SkiaPooledImageRegionDecoder.this.initialiseDecoder();
                                SkiaPooledImageRegionDecoder.this.debug("Started decoder, took " + (System.currentTimeMillis() - start) + "ms");
                            }
                        } catch (Exception e) {
                            SkiaPooledImageRegionDecoder.this.debug("Failed to start decoder: " + e.getMessage());
                        }
                    }
                }
            }.start();
        }
    }

    private void initialiseDecoder() throws Exception {
        BitmapRegionDecoder decoder = null;
        String uriString = this.uri.toString();
        long fileLength = Long.MAX_VALUE;
        if (uriString.startsWith(RESOURCE_PREFIX)) {
            Resources res;
            String packageName = this.uri.getAuthority();
            if (this.context.getPackageName().equals(packageName)) {
                res = this.context.getResources();
            } else {
                res = this.context.getPackageManager().getResourcesForApplication(packageName);
            }
            int id = 0;
            List<String> segments = this.uri.getPathSegments();
            int size = segments.size();
            if (size == 2 && ((String) segments.get(0)).equals("drawable")) {
                id = res.getIdentifier((String) segments.get(1), "drawable", packageName);
            } else if (size == 1 && TextUtils.isDigitsOnly((CharSequence) segments.get(0))) {
                try {
                    id = Integer.parseInt((String) segments.get(0));
                } catch (NumberFormatException e) {
                }
            }
            try {
                fileLength = this.context.getResources().openRawResourceFd(id).getLength();
            } catch (Exception e2) {
            }
            decoder = BitmapRegionDecoder.newInstance(this.context.getResources().openRawResource(id), false);
        } else if (uriString.startsWith(ASSET_PREFIX)) {
            String assetName = uriString.substring(ASSET_PREFIX.length());
            try {
                fileLength = this.context.getAssets().openFd(assetName).getLength();
            } catch (Exception e3) {
            }
            decoder = BitmapRegionDecoder.newInstance(this.context.getAssets().open(assetName, 1), false);
        } else if (uriString.startsWith(FILE_PREFIX)) {
            decoder = BitmapRegionDecoder.newInstance(uriString.substring(FILE_PREFIX.length()), false);
            try {
                File file = new File(uriString);
                if (file.exists()) {
                    fileLength = file.length();
                }
            } catch (Exception e4) {
            }
        } else {
            InputStream inputStream = null;
            try {
                ContentResolver contentResolver = this.context.getContentResolver();
                inputStream = contentResolver.openInputStream(this.uri);
                decoder = BitmapRegionDecoder.newInstance(inputStream, false);
                try {
                    AssetFileDescriptor descriptor = contentResolver.openAssetFileDescriptor(this.uri, "r");
                    if (descriptor != null) {
                        fileLength = descriptor.getLength();
                    }
                } catch (Exception e5) {
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e6) {
                    }
                }
            } catch (Throwable th) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e7) {
                    }
                }
            }
        }
        this.fileLength = fileLength;
        this.imageDimensions.set(decoder.getWidth(), decoder.getHeight());
        this.decoderLock.writeLock().lock();
        try {
            if (this.decoderPool != null) {
                this.decoderPool.add(decoder);
            }
            this.decoderLock.writeLock().unlock();
        } catch (Throwable th2) {
            this.decoderLock.writeLock().unlock();
        }
    }

    public Bitmap decodeRegion(Rect sRect, int sampleSize) {
        debug("Decode region " + sRect + " on thread " + Thread.currentThread().getName());
        if (sRect.width() < this.imageDimensions.x || sRect.height() < this.imageDimensions.y) {
            lazyInit();
        }
        this.decoderLock.readLock().lock();
        BitmapRegionDecoder decoder;
        try {
            if (this.decoderPool != null) {
                decoder = this.decoderPool.acquire();
                if (decoder != null) {
                    if (!decoder.isRecycled()) {
                        Options options = new Options();
                        options.inSampleSize = sampleSize;
                        options.inPreferredConfig = this.bitmapConfig;
                        Bitmap bitmap = decoder.decodeRegion(sRect, options);
                        if (bitmap == null) {
                            throw new RuntimeException("Skia image decoder returned null bitmap - image format may not be supported");
                        }
                        if (decoder != null) {
                            this.decoderPool.release(decoder);
                        }
                        this.decoderLock.readLock().unlock();
                        return bitmap;
                    }
                }
                if (decoder != null) {
                    this.decoderPool.release(decoder);
                }
            }
            throw new IllegalStateException("Cannot decode region after decoder has been recycled");
        } catch (Throwable th) {
            this.decoderLock.readLock().unlock();
        }
        return null;
    }

    public synchronized boolean isReady() {
        boolean z;
        z = (this.decoderPool == null || this.decoderPool.isEmpty()) ? false : true;
        return z;
    }

    public synchronized void recycle() {
        this.decoderLock.writeLock().lock();
        try {
            if (this.decoderPool != null) {
                this.decoderPool.recycle();
                this.decoderPool = null;
                this.context = null;
                this.uri = null;
            }
            this.decoderLock.writeLock().unlock();
        } catch (Throwable th) {
            this.decoderLock.writeLock().unlock();
        }
    }

    protected boolean allowAdditionalDecoder(int numberOfDecoders, long fileLength) {
        if (numberOfDecoders >= 4) {
            debug("No additional decoders allowed, reached hard limit (4)");
            return false;
        } else if (((long) numberOfDecoders) * fileLength > 20971520) {
            debug("No additional encoders allowed, reached hard memory limit (20Mb)");
            return false;
        } else if (numberOfDecoders >= getNumberOfCores()) {
            debug("No additional encoders allowed, limited by CPU cores (" + getNumberOfCores() + ")");
            return false;
        } else if (isLowMemory()) {
            debug("No additional encoders allowed, memory is low");
            return false;
        } else {
            debug("Additional decoder allowed, current count is " + numberOfDecoders + ", estimated native memory " + ((((long) numberOfDecoders) * fileLength) / 1048576) + "Mb");
            return true;
        }
    }

    private int getNumberOfCores() {
        if (VERSION.SDK_INT >= 17) {
            return Runtime.getRuntime().availableProcessors();
        }
        return getNumCoresOldPhones();
    }

    private int getNumCoresOldPhones() {
        try {
            return new File("/sys/devices/system/cpu/").listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    return Pattern.matches("cpu[0-9]+", pathname.getName());
                }
            }).length;
        } catch (Exception e) {
            return 1;
        }
    }

    private boolean isLowMemory() {
        ActivityManager activityManager = (ActivityManager) this.context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) {
            return true;
        }
        MemoryInfo memoryInfo = new MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.lowMemory;
    }

    private void debug(String message) {
        if (debug) {
            Log.d(TAG, message);
        }
    }
}
