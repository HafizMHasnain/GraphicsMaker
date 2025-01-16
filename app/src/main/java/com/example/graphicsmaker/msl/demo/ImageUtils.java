package com.example.graphicsmaker.msl.demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore.Images.Media;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {
    public static Bitmap getBitmapFromUri(Context context, Uri uri, float screenWidth, float screenHeight) throws IOException {
        try {
            ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Options bfo = new Options();
            bfo.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fileDescriptor, null, bfo);
            Options optsDownSample = new Options();
            if (screenWidth <= screenHeight) {
                screenWidth = screenHeight;
            }
            int maxDim = (int) screenWidth;
            optsDownSample.inSampleSize = getClosestResampleSize(bfo.outWidth, bfo.outHeight, maxDim);
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, optsDownSample);
            Matrix m = new Matrix();
            if (image.getWidth() > maxDim || image.getHeight() > maxDim) {
                Options optsScale = getResampling(image.getWidth(), image.getHeight(), maxDim);
                m.postScale(((float) optsScale.outWidth) / ((float) image.getWidth()), ((float) optsScale.outHeight) / ((float) image.getHeight()));
            }
            String pathInput = getRealPathFromURI(uri, context);
            if (new Integer(VERSION.SDK).intValue() > 4) {
                int rotation = ExifUtils.getExifRotation(pathInput);
                if (rotation != 0) {
                    m.postRotate((float) rotation);
                }
            }
            image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), m, true);
            parcelFileDescriptor.close();
            return image;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getResampleImageBitmap(Uri uri, Context context, int maxDim) throws IOException {
        Bitmap bmp = null;
        try {
            bmp = resampleImage(getRealPathFromURI(uri, context), maxDim);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    public static Bitmap getResampleImageBitmap(Uri uri, Context context) throws IOException {
        String pathInput = getRealPathFromURI(uri, context);
        try {
            Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            return resampleImage(pathInput, width);
        } catch (Exception e) {
            e.printStackTrace();
            return Media.getBitmap(context.getContentResolver(), Uri.parse(pathInput));
        }
    }

    @SuppressLint({"UseValueOf"})
    public static Bitmap resampleImage(String path, int maxDim) throws Exception {
        Options bfo = new Options();
        bfo.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bfo);
        Options optsDownSample = new Options();
        optsDownSample.inSampleSize = getClosestResampleSize(bfo.outWidth, bfo.outHeight, maxDim);
        Bitmap bmpt = BitmapFactory.decodeFile(path, optsDownSample);
        Matrix m = new Matrix();
        if (bmpt.getWidth() > maxDim || bmpt.getHeight() > maxDim) {
            Options optsScale = getResampling(bmpt.getWidth(), bmpt.getHeight(), maxDim);
            m.postScale(((float) optsScale.outWidth) / ((float) bmpt.getWidth()), ((float) optsScale.outHeight) / ((float) bmpt.getHeight()));
        }
        if (new Integer(VERSION.SDK).intValue() > 4) {
            int rotation = ExifUtils.getExifRotation(path);
            if (rotation != 0) {
                m.postRotate((float) rotation);
            }
        }
        return Bitmap.createBitmap(bmpt, 0, 0, bmpt.getWidth(), bmpt.getHeight(), m, true);
    }

    public static Options getResampling(int cx, int cy, int max) {
        float scaleVal;
        Options bfo = new Options();
        if (cx > cy) {
            scaleVal = ((float) max) / ((float) cx);
        } else if (cy > cx) {
            scaleVal = ((float) max) / ((float) cy);
        } else {
            scaleVal = ((float) max) / ((float) cx);
        }
        bfo.outWidth = (int) ((((float) cx) * scaleVal) + 0.5f);
        bfo.outHeight = (int) ((((float) cy) * scaleVal) + 0.5f);
        return bfo;
    }

    public static int getClosestResampleSize(int cx, int cy, int maxDim) {
        int max = Math.max(cx, cy);
        int resample = 1;
        while (resample < Integer.MAX_VALUE) {
            if (resample * maxDim > max) {
                resample--;
                break;
            }
            resample++;
        }
        return resample > 0 ? resample : 1;
    }

    public static Options getBitmapDims(String path) throws Exception {
        Options bfo = new Options();
        bfo.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bfo);
        return bfo;
    }

    public static String getRealPathFromURI(Uri contentURI, Context context) {
        try {
            Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
            if (cursor == null) {
                return contentURI.getPath();
            }
            cursor.moveToFirst();
            String result = cursor.getString(cursor.getColumnIndex("_data"));
            cursor.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return contentURI.toString();
        }
    }

    public static Bitmap resizeBitmap(Bitmap bit, int width, int height) {
        float wr = (float) width;
        float hr = (float) height;
        float wd = (float) bit.getWidth();
        float he = (float) bit.getHeight();
        Log.i("testings", wr + "  " + hr + "  and  " + wd + "  " + he);
        float rat1 = wd / he;
        float rat2 = he / wd;
        if (wd > wr) {
            wd = wr;
            he = wd * rat2;
            Log.i("testings", "if (wd > wr) " + wd + "  " + he);
            if (he > hr) {
                he = hr;
                wd = he * rat1;
                Log.i("testings", "  if (he > hr) " + wd + "  " + he);
            } else {
                wd = wr;
                he = wd * rat2;
                Log.i("testings", " in else " + wd + "  " + he);
            }
        } else if (he > hr) {
            he = hr;
            wd = he * rat1;
            Log.i("testings", "  if (he > hr) " + wd + "  " + he);
            if (wd > wr) {
                wd = wr;
                he = wd * rat2;
            } else {
                Log.i("testings", " in else " + wd + "  " + he);
            }
        } else if (rat1 > 0.75f) {
            wd = wr;
            he = wd * rat2;
            Log.i("testings", " if (rat1 > .75f) ");
        } else if (rat2 > 1.5f) {
            he = hr;
            wd = he * rat1;
            Log.i("testings", " if (rat2 > 1.5f) ");
        } else {
            wd = wr;
            he = wd * rat2;
            Log.i("testings", " in else ");
        }
        return Bitmap.createScaledBitmap(bit, (int) wd, (int) he, false);
    }

    public static int dpToPx(Context c, int dp) {
        float f = (float) dp;
        c.getResources();
        return (int) (f * Resources.getSystem().getDisplayMetrics().density);
    }

    public static float pxToDp(Context context, float px) {
        return px / (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f);
    }

    public static Bitmap bitmapmasking(Bitmap original, Bitmap mask) {
        Bitmap result = Bitmap.createBitmap(original.getWidth(), original.getHeight(), Config.ARGB_8888);
        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(1);
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
        mCanvas.drawBitmap(original, 0.0f, 0.0f, null);
        mCanvas.drawBitmap(mask, 0.0f, 0.0f, paint);
        paint.setXfermode(null);
        return result;
    }

    public static Bitmap getThumbnail(Bitmap bitmap, int width, int height) {
        Bitmap b = bitmap.copy(bitmap.getConfig(), true);
        int w = b.getWidth();
        int h = b.getHeight();
        if (h > w) {
            b = cropCenterBitmap(b, w, w);
        } else {
            b = cropCenterBitmap(b, h, h);
        }
        return Bitmap.createScaledBitmap(b, width, height, true);
    }

    public static Bitmap cropCenterBitmap(Bitmap src, int w, int h) {
        if (src == null) {
            return null;
        }
        int width = src.getWidth();
        int height = src.getHeight();
        if (width < w && height < h) {
            return src;
        }
        int x = 0;
        int y = 0;
        if (width > w) {
            x = (width - w) / 2;
        }
        if (height > h) {
            y = (height - h) / 2;
        }
        int cw = w;
        int ch = h;
        if (w > width) {
            cw = width;
        }
        if (h > height) {
            ch = height;
        }
        return Bitmap.createBitmap(src, x, y, cw, ch);
    }

    public static Bitmap mergelogo(Bitmap bitmap, Bitmap logo) {
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        float wr = (float) bitmap.getWidth();
        float hr = (float) bitmap.getHeight();
        float wd = (float) logo.getWidth();
        float he = (float) logo.getHeight();
        float rat1 = wd / he;
        float rat2 = he / wd;
        if (wd > wr) {
            wd = wr;
            logo = Bitmap.createScaledBitmap(logo, (int) wd, (int) (wd * rat2), false);
        } else if (he > hr) {
            he = hr;
            logo = Bitmap.createScaledBitmap(logo, (int) (he * rat1), (int) he, false);
        }
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
        canvas.drawBitmap(logo, (float) (bitmap.getWidth() - logo.getWidth()), (float) (bitmap.getHeight() - logo.getHeight()), null);
        return result;
    }

    public static Bitmap CropBitmapTransparency(Bitmap sourceBitmap) {
        int minX = sourceBitmap.getWidth();
        int minY = sourceBitmap.getHeight();
        int maxX = -1;
        int maxY = -1;
        for (int y = 0; y < sourceBitmap.getHeight(); y++) {
            for (int x = 0; x < sourceBitmap.getWidth(); x++) {
                if (((sourceBitmap.getPixel(x, y) >> 24) & 255) > 0) {
                    if (x < minX) {
                        minX = x;
                    }
                    if (x > maxX) {
                        maxX = x;
                    }
                    if (y < minY) {
                        minY = y;
                    }
                    if (y > maxY) {
                        maxY = y;
                    }
                }
            }
        }
        if (maxX < minX || maxY < minY) {
            return null;
        }
        return Bitmap.createBitmap(sourceBitmap, minX, minY, (maxX - minX) + 1, (maxY - minY) + 1);
    }

    public static Bitmap getTiledBitmap(Context ctx, int resId, int width, int height) {
        Rect rect = new Rect(0, 0, width, height);
        Paint paint = new Paint();
        Options options = new Options();
        options.inScaled = false;
        paint.setShader(new BitmapShader(BitmapFactory.decodeResource(ctx.getResources(), resId, options), TileMode.REPEAT, TileMode.REPEAT));
        Bitmap b = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        new Canvas(b).drawRect(rect, paint);
        return b;
    }

    public static Bitmap getColoredBitmap(int color, int width, int height) {
        Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        new Canvas(result).drawColor(color);
        return result;
    }

    public static CharSequence getSpannableString(Context ctx, Typeface ttf, int stringId) {
        SpannableString spannableString = new SpannableString(ctx.getResources().getString(stringId));
        spannableString.setSpan(new CustomTypefaceSpan(ttf), 0, ctx.getResources().getString(stringId).length(), 0);
        SpannableStringBuilder builder = new SpannableStringBuilder().append(spannableString);
        return builder.subSequence(0, builder.length());
    }

    public static Bitmap getRotatedBitmap(Bitmap bit, float deg) {
        Bitmap result = Bitmap.createBitmap(bit.getWidth(), bit.getHeight(), Config.ARGB_8888);
        Canvas mCanvas = new Canvas(result);
        Matrix matrix = new Matrix();
        float px = (float) (bit.getWidth() / 2);
        float py = (float) (bit.getHeight() / 2);
        matrix.postTranslate((float) ((-bit.getWidth()) / 2), (float) ((-bit.getHeight()) / 2));
        matrix.postRotate(deg);
        matrix.postTranslate(px, py);
        mCanvas.drawBitmap(bit, matrix, null);
        return result;
    }

    public static void resampleImageAndSaveToNewLocation(String pathInput, String pathOutput) throws Exception {
        resampleImage(pathInput, 800).compress(CompressFormat.PNG, 100, new FileOutputStream(pathOutput));
    }
}
