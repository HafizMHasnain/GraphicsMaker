package com.example.graphicsmaker.msl.textmodule;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class TextRelativeDraw extends View {
    private Context context;
    int progress = 0;
    RelativeLayout rel_artv;
    int x1;
    int x10;
    int x11;
    int x12;
    int x13;
    int x14;
    int x15;
    int x16;
    int x17;
    int x18;
    int x19;
    int x2;
    int x20;
    int x21;
    int x22;
    int x23;
    int x24;
    int x25;
    int x26;
    int x27;
    int x28;
    int x29;
    int x3;
    int x30;
    int x31;
    int x32;
    int x33;
    int x34;
    int x35;
    int x36;
    int x37;
    int x38;
    int x39;
    int x4;
    int x40;
    int x41;
    int x42;
    int x43;
    int x44;
    int x45;
    int x46;
    int x47;
    int x48;
    int x49;
    int x5;
    int x50;
    int x51;
    int x52;
    int x53;
    int x54;
    int x55;
    int x56;
    int x57;
    int x58;
    int x59;
    int x6;
    int x60;
    int x61;
    int x62;
    int x63;
    int x64;
    int x65;
    int x66;
    int x67;
    int x68;
    int x69;
    int x7;
    int x70;
    int x71;
    int x72;
    int x73;
    int x74;
    int x75;
    int x76;
    int x77;
    int x78;
    int x79;
    int x8;
    int x80;
    int x81;
    int x9;
    int y1;
    int y10;
    int y11;
    int y12;
    int y13;
    int y14;
    int y15;
    int y16;
    int y17;
    int y18;
    int y19;
    int y2;
    int y20;
    int y21;
    int y22;
    int y23;
    int y24;
    int y25;
    int y26;
    int y27;
    int y28;
    int y29;
    int y3;
    int y30;
    int y31;
    int y32;
    int y33;
    int y34;
    int y35;
    int y36;
    int y37;
    int y38;
    int y39;
    int y4;
    int y40;
    int y41;
    int y42;
    int y43;
    int y44;
    int y45;
    int y46;
    int y47;
    int y48;
    int y49;
    int y5;
    int y50;
    int y51;
    int y52;
    int y53;
    int y54;
    int y55;
    int y56;
    int y57;
    int y58;
    int y59;
    int y6;
    int y60;
    int y61;
    int y62;
    int y63;
    int y64;
    int y65;
    int y66;
    int y67;
    int y68;
    int y69;
    int y7;
    int y70;
    int y71;
    int y72;
    int y73;
    int y74;
    int y75;
    int y76;
    int y77;
    int y78;
    int y79;
    int y8;
    int y80;
    int y81;
    int y9;

    public TextRelativeDraw(Context context, RelativeLayout rel_artv) {
        super(context);
        this.context = context;
        this.rel_artv = rel_artv;
        init(context);
    }

    public void init(Context ctx) {
        this.context = ctx;
    }

    public void setTextCurveRotateProg(int curveRotateProg) {
        this.progress = curveRotateProg;
        invalidate();
    }

    protected void onDraw(Canvas canvas) {
        if (this.rel_artv != null) {
            Paint p = new Paint();
            p.setColor(-16711936);
            Bitmap bit = Bitmap.createBitmap(this.rel_artv.getWidth(), this.rel_artv.getHeight(), Config.ARGB_8888);
            this.rel_artv.draw(new Canvas(bit));
            int w = bit.getWidth();
            int h = bit.getHeight();
            int wd = w / 8;
            int hd = h / 8;
            this.x1 = 0;
            this.y1 = 0;
            this.x2 = (wd * 1) + 0;
            this.y2 = 0;
            this.x3 = (wd * 2) + 0;
            this.y3 = 0;
            this.x4 = (wd * 3) + 0;
            this.y4 = 0;
            this.x5 = (wd * 4) + 0;
            this.y5 = 0;
            this.x6 = (wd * 5) + 0;
            this.y6 = 0;
            this.x7 = (wd * 6) + 0;
            this.y7 = 0;
            this.x8 = (wd * 7) + 0;
            this.y8 = 0;
            this.x9 = 0 + w;
            this.y9 = 0;
            this.x10 = 0;
            this.y10 = 0 + hd;
            this.x11 = (wd * 1) + 0;
            this.y11 = 0 + hd;
            this.x12 = (wd * 2) + 0;
            this.y12 = 0 + hd;
            this.x13 = (wd * 3) + 0;
            this.y13 = 0 + hd;
            this.x14 = (wd * 4) + 0;
            this.y14 = 0 + hd;
            this.x15 = (wd * 5) + 0;
            this.y15 = 0 + hd;
            this.x16 = (wd * 6) + 0;
            this.y16 = 0 + hd;
            this.x17 = (wd * 7) + 0;
            this.y17 = 0 + hd;
            this.x18 = 0 + w;
            this.y18 = 0 + hd;
            this.x19 = 0;
            this.y19 = (hd * 2) + 0;
            this.x20 = (wd * 1) + 0;
            this.y20 = (hd * 2) + 0;
            this.x21 = (wd * 2) + 0;
            this.y21 = (hd * 2) + 0;
            this.x22 = (wd * 3) + 0;
            this.y22 = (hd * 2) + 0;
            this.x23 = (wd * 4) + 0;
            this.y23 = (hd * 2) + 0;
            this.x24 = (wd * 5) + 0;
            this.y24 = (hd * 2) + 0;
            this.x25 = (wd * 6) + 0;
            this.y25 = (hd * 2) + 0;
            this.x26 = (wd * 7) + 0;
            this.y26 = (hd * 2) + 0;
            this.x27 = 0 + w;
            this.y27 = (hd * 2) + 0;
            this.x28 = 0;
            this.y28 = (hd * 3) + 0;
            this.x29 = (wd * 1) + 0;
            this.y29 = (hd * 3) + 0;
            this.x30 = (wd * 2) + 0;
            this.y30 = (hd * 3) + 0;
            this.x31 = (wd * 3) + 0;
            this.y31 = (hd * 3) + 0;
            this.x32 = (wd * 4) + 0;
            this.y32 = (hd * 3) + 0;
            this.x33 = (wd * 5) + 0;
            this.y33 = (hd * 3) + 0;
            this.x34 = (wd * 6) + 0;
            this.y34 = (hd * 3) + 0;
            this.x35 = (wd * 7) + 0;
            this.y35 = (hd * 3) + 0;
            this.x36 = 0 + w;
            this.y36 = (hd * 3) + 0;
            this.x37 = 0;
            this.y37 = (hd * 4) + 0;
            this.x38 = (wd * 1) + 0;
            this.y38 = (hd * 4) + 0;
            this.x39 = (wd * 2) + 0;
            this.y39 = (hd * 4) + 0;
            this.x40 = (wd * 3) + 0;
            this.y40 = (hd * 4) + 0;
            this.x41 = (wd * 4) + 0;
            this.y41 = (hd * 4) + 0;
            this.x42 = (wd * 5) + 0;
            this.y42 = (hd * 4) + 0;
            this.x43 = (wd * 6) + 0;
            this.y43 = (hd * 4) + 0;
            this.x44 = (wd * 7) + 0;
            this.y44 = (hd * 4) + 0;
            this.x45 = 0 + w;
            this.y45 = (hd * 4) + 0;
            this.x46 = 0;
            this.y46 = (hd * 5) + 0;
            this.x47 = (wd * 1) + 0;
            this.y47 = (hd * 5) + 0;
            this.x48 = (wd * 2) + 0;
            this.y48 = (hd * 5) + 0;
            this.x49 = (wd * 3) + 0;
            this.y49 = (hd * 5) + 0;
            this.x50 = (wd * 4) + 0;
            this.y50 = (hd * 5) + 0;
            this.x51 = (wd * 5) + 0;
            this.y51 = (hd * 5) + 0;
            this.x52 = (wd * 6) + 0;
            this.y52 = (hd * 5) + 0;
            this.x53 = (wd * 7) + 0;
            this.y53 = (hd * 5) + 0;
            this.x54 = 0 + w;
            this.y54 = (hd * 5) + 0;
            this.x55 = 0;
            this.y55 = (hd * 6) + 0;
            this.x56 = (wd * 1) + 0;
            this.y56 = (hd * 6) + 0;
            this.x57 = (wd * 2) + 0;
            this.y57 = (hd * 6) + 0;
            this.x58 = (wd * 3) + 0;
            this.y58 = (hd * 6) + 0;
            this.x59 = (wd * 4) + 0;
            this.y59 = (hd * 6) + 0;
            this.x60 = (wd * 5) + 0;
            this.y60 = (hd * 6) + 0;
            this.x61 = (wd * 6) + 0;
            this.y61 = (hd * 6) + 0;
            this.x62 = (wd * 7) + 0;
            this.y62 = (hd * 6) + 0;
            this.x63 = 0 + w;
            this.y63 = (hd * 6) + 0;
            this.x64 = 0;
            this.y64 = (hd * 7) + 0;
            this.x65 = (wd * 1) + 0;
            this.y65 = (hd * 7) + 0;
            this.x66 = (wd * 2) + 0;
            this.y66 = (hd * 7) + 0;
            this.x67 = (wd * 3) + 0;
            this.y67 = (hd * 7) + 0;
            this.x68 = (wd * 4) + 0;
            this.y68 = (hd * 7) + 0;
            this.x69 = (wd * 5) + 0;
            this.y69 = (hd * 7) + 0;
            this.x70 = (wd * 6) + 0;
            this.y70 = (hd * 7) + 0;
            this.x71 = (wd * 7) + 0;
            this.y71 = (hd * 7) + 0;
            this.x72 = 0 + w;
            this.y72 = (hd * 7) + 0;
            this.x73 = 0;
            this.y73 = 0 + h;
            this.x74 = (wd * 1) + 0;
            this.y74 = 0 + h;
            this.x75 = (wd * 2) + 0;
            this.y75 = 0 + h;
            this.x76 = (wd * 3) + 0;
            this.y76 = 0 + h;
            this.x77 = (wd * 4) + 0;
            this.y77 = 0 + h;
            this.x78 = (wd * 5) + 0;
            this.y78 = 0 + h;
            this.x79 = (wd * 6) + 0;
            this.y79 = 0 + h;
            this.x80 = (wd * 7) + 0;
            this.y80 = 0 + h;
            this.x81 = 0 + w;
            this.y81 = 0 + h;
            Path path1 = mPath(this.x1, this.y1, this.x9, this.y9, this.progress, canvas, p);
            Path path2 = mPath(this.x10, this.y10, this.x18, this.y18, this.progress, canvas, p);
            Path path3 = mPath(this.x19, this.y19, this.x27, this.y27, this.progress, canvas, p);
            Path path4 = mPath(this.x28, this.y28, this.x36, this.y36, this.progress, canvas, p);
            Path path5 = mPath(this.x37, this.y37, this.x45, this.y45, this.progress, canvas, p);
            Path path6 = mPath(this.x46, this.y46, this.x54, this.y54, this.progress, canvas, p);
            Path path7 = mPath(this.x55, this.y55, this.x63, this.y63, this.progress, canvas, p);
            Path path8 = mPath(this.x64, this.y64, this.x72, this.y72, this.progress, canvas, p);
            Path path9 = mPath(this.x73, this.y73, this.x81, this.y81, this.progress, canvas, p);
            Path[] arrayPath = new Path[]{path1, path2, path3, path4, path5, path6, path7, path8, path9};
            List<Float> mVerts1 = new ArrayList();
            for (Path pathMeasure : arrayPath) {
                PathMeasure pathMeasure2 = new PathMeasure(pathMeasure, false);
                float[] fArr = new float[2];
                fArr = new float[]{0.0f, 0.0f};
                for (float i = 0.0f; i <= 1.0f; i += 0.125f) {
                    pathMeasure2.getPosTan(pathMeasure2.getLength() * i, fArr, null);
                    mVerts1.add(Float.valueOf(fArr[0]));
                    mVerts1.add(Float.valueOf(fArr[1]));
                }
            }
            float[] stockArr = new float[mVerts1.size()];
            for (int k = 0; k < stockArr.length; k++) {
                stockArr[k] = ((Float) mVerts1.get(k)).floatValue();
            }
            canvas.drawBitmapMesh(bit, 8, 8, stockArr, 0, null, 0, null);
            return;
        }
        Log.e("not show", "visiblity");
    }

    public int getClosestResampleSize(int cx, int cy, int maxDim) {
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

    public Path mPath(int x1, int y1, int x2, int y2, int radius, Canvas canvas, Paint p) {
        Path path = new Path();
        int midX = x1 + ((x2 - x1) / 2);
        int midY = y1 + ((y2 - y1) / 2);
        double angleRadians = Math.toRadians((Math.atan2((double) ((float) (midY - y1)), (double) ((float) (midX - x1))) * 57.29577951308232d) - 90.0d);
        float pointX = (float) (((double) midX) + (((double) radius) * Math.cos(angleRadians)));
        float pointY = (float) (((double) midY) + (((double) radius) * Math.sin(angleRadians)));
        path.moveTo((float) x1, (float) y1);
        path.cubicTo((float) x1, (float) y1, pointX, pointY, (float) x2, (float) y2);
        return path;
    }

    public int dpToPx(Context c, int dp) {
        float f = (float) dp;
        c.getResources();
        return (int) (f * Resources.getSystem().getDisplayMetrics().density);
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
            he = wr * rat2;
            Log.i("testings", " in else ");
            if (he > hr) {
                he = hr;
                wd = he * rat1;
                Log.i("testings", "  if (he > hr) " + wd + "  " + he);
            } else {
                wd = wr;
                he = wd * rat2;
                Log.i("testings", " in else " + wd + "  " + he);
            }
        }
        return Bitmap.createScaledBitmap(bit, (int) wd, (int) he, false);
    }
}
